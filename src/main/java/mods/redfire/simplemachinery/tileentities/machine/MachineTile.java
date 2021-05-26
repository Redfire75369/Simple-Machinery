package mods.redfire.simplemachinery.tileentities.machine;

import mods.redfire.simplemachinery.util.IntArrayWrapper;
import mods.redfire.simplemachinery.util.energy.EnergyCoil;
import mods.redfire.simplemachinery.util.inventory.IMachineInventoryCallback;
import mods.redfire.simplemachinery.util.inventory.InventoryGroup;
import mods.redfire.simplemachinery.util.inventory.MachineInventory;
import mods.redfire.simplemachinery.util.inventory.MachineItemSlot;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MachineTile<T extends MachineRecipe> extends TileEntity implements ITickableTileEntity, IMachineInventoryCallback {
    protected MachineInventory inventory = new MachineInventory(this);
    protected EnergyCoil energy;

    protected LazyOptional<?> itemCap = LazyOptional.empty();
    protected LazyOptional<?> energyCap = LazyOptional.empty();

    protected List<Integer> itemInputCounts = new ArrayList<>();

    protected int progress;
    protected int progressTotal;
    protected Optional<T> currentRecipe = Optional.empty();

    protected IntArrayWrapper data = new IntArrayWrapper(2);

    public MachineTile(TileEntityType<?> type, int itemInputs, int itemOutputs, EnergyCoil energy) {
        super(type);
        inventory.addSlots(InventoryGroup.INPUT, itemInputs);
        inventory.addSlots(InventoryGroup.OUTPUT, itemOutputs);
        this.energy = energy;

        updateHandlers();
    }

    public MachineTile<T> worldContext(BlockState state, IBlockReader world) {
        return this;
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) {
            return;
        }

        if (currentRecipe.isPresent()) {
            T recipe = currentRecipe.get();
            energy.modify(-recipe.getEnergyRate());
            data.setInternal(0, data.getInternal(0) - 1);

            if (canComplete()) {
                complete();
                clear();
            } else if (energy.getEnergyStored() < recipe.getEnergyRate()) {
                clear();
            }
            // TODO: Clear Recipe if input is removed
        } else {
            Optional<T> recipe = getRecipe();
            if (recipe.isPresent() && canBegin(recipe.get())) {
                begin(recipe.get());
            }
        }
    }

    public void begin(T recipe) {
        if (energy.getEnergyStored() > recipe.getEnergy()) {
            currentRecipe = Optional.of(recipe);
            data.setInternal(0, recipe.getTime());
            data.setInternal(1, recipe.getTime());
            itemInputCounts = recipe.getInputItemCounts(inventory.getInputSlots());
        }
    }

    public void complete() {
        if (!validateInputs(itemInputCounts)) {
            clear();
            return;
        }
        resolveInputs();
        resolveOutputs();
        setChanged();
    }

    public void clear() {
        data.setInternal(0, 0);
        data.setInternal(1, 0);
        currentRecipe = Optional.empty();
        itemInputCounts = new ArrayList<>();
    }

    public boolean canBegin(T recipe) {
        List<Integer> itemInputCounts = recipe.getInputItemCounts(inventory.getInputSlots());

        return energy.getEnergyStored() >= recipe.getEnergy()
               && validateInputs(itemInputCounts) && validateOutputs(recipe);
    }

    public boolean canComplete() {
        return data.getInternal(0) <= 0;
    }

    protected boolean validateInputs(List<Integer> itemInputCounts) {
        List<MachineItemSlot> inputSlots = inputSlots();

        for (int i = 0; i < inputSlots.size() && i < itemInputCounts.size(); i++) {
            int inputCount = itemInputCounts.get(i);
            if (inputCount == 0 || (inputCount > 0 && inputSlots.get(i).getItemStack().getCount() < inputCount)) {
                return false;
            }
        }
        return true;
    }

    protected boolean validateOutputs(T recipe) {
        List<MachineItemSlot> outputSlots = outputSlots();
        List<ItemStack> recipeOutputItems = recipe.getOutputItems();

        boolean[] used = new boolean[outputSlots().size()];
        for (ItemStack recipeOutput : recipeOutputItems) {
            boolean matched = false;
            for (int i = 0; i < outputSlots.size(); i++) {
                if (used[i]) {
                    continue;
                }
                ItemStack output = outputSlots.get(i).getItemStack();
                if (output.getCount() + Math.max(0, recipeOutput.getCount()) > output.getMaxStackSize()) {
                    continue;
                }
                if (ItemStack.isSame(output, recipeOutput) && ItemStack.tagMatches(output, recipeOutput)) {
                    used[i] = true;
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                for (int i = 0; i < outputSlots.size(); ++i) {
                    if (used[i]) {
                        continue;
                    }
                    if (outputSlots.get(i).isEmpty()) {
                        used[i] = true;
                        matched = true;
                        break;
                    }
                }
            }

            if (!matched) {
                return false;
            }
        }

        return true;
    }

    protected void resolveInputs() {
        for (int i = 0; i < itemInputCounts.size(); ++i) {
            inputSlots().get(i).consume(itemInputCounts.get(i));
        }
    }

    protected void resolveOutputs() {
        if (!currentRecipe.isPresent()) {
            return;
        }

        List<ItemStack> recipeOutputItems = currentRecipe.get().getOutputItems();
        for (ItemStack recipeOutput : recipeOutputItems) {
            int outputCount = recipeOutput.getCount();
            boolean matched = false;
            for (MachineItemSlot slot : outputSlots()) {
                ItemStack output = slot.getItemStack();
                if (ItemStack.isSame(output, recipeOutput) && ItemStack.tagMatches(output, recipeOutput) && output.getCount() + outputCount <= output.getMaxStackSize()) {
                    output.grow(outputCount);
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                for (MachineItemSlot slot : outputSlots()) {
                    if (slot.isEmpty()) {
                        slot.setItemStack(new ItemStack(recipeOutput.getItem(), outputCount));
                        break;
                    }
                }
            }
        }
    }

    protected Optional<T> getRecipe() {
        return Optional.empty();
    }

    public int invSize() {
        return inventory.getSlots();
    }

    public MachineInventory getItemInv() {
        return inventory;
    }

    protected void initHandlers() {
        inventory.initHandlers();
    }

    public List<MachineItemSlot> inputSlots() {
        return inventory.getInputSlots();
    }

    protected List<MachineItemSlot> outputSlots() {
        return inventory.getOutputSlots();
    }

    public MachineItemSlot getSlot(int slot) {
        return inventory.getSlot(slot);
    }

    public EnergyCoil getEnergy() {
        return energy;
    }

    public IntArrayWrapper getData() {
        return data;
    }

    @Override
    public boolean clearSlot(int slot) {
        if (slot >= inventory.getSlots()) {
            return false;
        }
        if (inventory.getSlot(slot).clear()) {
            onInventoryChange(slot);
            return true;
        }
        return false;
    }

    @Override
    public boolean clearEnergy(int coil) {
        return energy.clear();
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT tag) {
        super.load(state, tag);
        inventory.read(tag);
        energy.read(tag);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT tag) {
        super.save(tag);
        inventory.write(tag);
        energy.write(tag);

        return tag;
    }

    protected void updateHandlers() {
        LazyOptional<?> prevEnergyCap = energyCap;
        energyCap = energy.getCapacity() > 0 ? LazyOptional.of(() -> energy) : LazyOptional.empty();
        prevEnergyCap.invalidate();

        LazyOptional<?> prevItemCap = itemCap;
        IItemHandler invHandler = inventory.getHandler(InventoryGroup.ALL);
        itemCap = inventory.hasAccessibleSlots() ? LazyOptional.of(() -> invHandler) : LazyOptional.empty();
        prevItemCap.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && inventory.hasAccessibleSlots()) {
            return getItemHandlerCapability(side);
        } else if (cap == CapabilityEnergy.ENERGY && energy.getMaxEnergyStored() > 0) {
            return getEnergyCapability(side);
        }
        return super.getCapability(cap, side);
    }

    protected <T> LazyOptional<T> getItemHandlerCapability(@Nullable Direction side) {
        if (!itemCap.isPresent() && inventory.hasAccessibleSlots()) {
            IItemHandler handler = inventory.getHandler(InventoryGroup.ALL);
            itemCap = LazyOptional.of(() -> handler);
        }
        return itemCap.cast();
    }

    protected <T> LazyOptional<T> getEnergyCapability(@Nullable Direction side) {
        if (!energyCap.isPresent() && energy.getCapacity() > 0) {
            energyCap = LazyOptional.of(() -> energy);
        }
        return energyCap.cast();
    }
}
