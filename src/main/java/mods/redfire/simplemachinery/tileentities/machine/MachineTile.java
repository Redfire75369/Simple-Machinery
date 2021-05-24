package mods.redfire.simplemachinery.tileentities.machine;

import mods.redfire.simplemachinery.util.energy.EnergyCoil;
import mods.redfire.simplemachinery.util.inventory.IMachineInventoryCallback;
import mods.redfire.simplemachinery.util.inventory.InventoryGroup;
import mods.redfire.simplemachinery.util.inventory.MachineInventory;
import mods.redfire.simplemachinery.util.inventory.MachineItemSlot;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
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

    public MachineTile(TileEntityType<?> type, int itemInputs, int itemOutputs, EnergyCoil energy) {
        super(type);
        inventory.addSlots(InventoryGroup.INPUT, itemInputs);
        inventory.addSlots(InventoryGroup.OUTPUT, itemOutputs);
        this.energy = energy;

        updateHandlers();
    }

    public boolean playerWithinDistance(PlayerEntity player, double distanceSq) {
        return worldPosition.distSqr(player.position(), true) <= distanceSq;
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
            progress--;

            if (canComplete()) {
                complete();
                if (canBegin(recipe)) {
                    begin();
                } else {
                    clear();
                }
            } else if (energy.getEnergyStored() < recipe.getEnergyRate()) {
                clear();
            }
        }
    }

    public void begin() {
        Optional<T> recipe = getRecipe();
        if (recipe.isPresent()) {
            if (energy.getEnergyStored() > recipe.get().getEnergy()) {
                currentRecipe = recipe;
                progress = progressTotal = recipe.get().getTime();
                itemInputCounts = recipe.get().getInputItemCounts(inventory.getInputSlots());
            }
        }
    }

    public void complete() {
        if (!validateInputs()) {
            clear();
            return;
        }
        resolveOutputs();
        resolveInputs();
        setChanged();
    }

    public void clear() {
        progress = 0;
        currentRecipe = Optional.empty();
        itemInputCounts = new ArrayList<>();
    }

    public boolean canBegin(T recipe) {
        if (energy.getEnergyStored() > recipe.getEnergy()) {
            return false;
        }
        return !validateInputs() && validateOutputs();
    }

    public boolean canComplete() {
        return progress <= 0;
    }

    protected boolean validateInputs() {
        List<MachineItemSlot> inputSlots = inputSlots();
        for (int i = 0; i < inputSlots.size() && i < itemInputCounts.size(); ++i) {
            int inputCount = itemInputCounts.get(i);
            if (inputCount > 0 && inputSlots.get(i).getItemStack().getCount() < inputCount) {
                return false;
            }
        }
        return true;
    }

    protected boolean validateOutputs() {
        if (!currentRecipe.isPresent()) {
            return false;
        }
        List<MachineItemSlot> outputSlots = outputSlots();
        List<ItemStack> recipeOutputItems = currentRecipe.get().getOutputItems();

        boolean[] used = new boolean[outputSlots().size()];
        for (ItemStack recipeOutput : recipeOutputItems) {
            boolean matched = false;
            for (int i = 0; i < outputSlots.size(); ++i) {
                if (used[i]) {
                    continue;
                }
                ItemStack output = outputSlots.get(i).getItemStack();
                if (output.getCount() >= output.getMaxStackSize()) {
                    continue;
                }
                if (ItemStack.matches(output, recipeOutput) && ItemStack.tagMatches(output, recipeOutput)) {
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

    protected void resolveOutputs() {
        if (!currentRecipe.isPresent()) {
            return;
        }

        List<ItemStack> recipeOutputItems = currentRecipe.get().getOutputItems();
        for (int i = 0; i < recipeOutputItems.size(); ++i) {
            ItemStack recipeOutput = recipeOutputItems.get(i);
            int outputCount = recipeOutput.getCount();
            boolean matched = false;
            for (MachineItemSlot slot : outputSlots()) {
                ItemStack output = slot.getItemStack();
                if (ItemStack.matches(output, recipeOutput) && ItemStack.tagMatches(output, recipeOutput) && output.getCount() < output.getMaxStackSize()) {
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

    protected void resolveInputs() {
        for (int i = 0; i < itemInputCounts.size(); ++i) {
            inputSlots().get(i).consume(itemInputCounts.get(i));
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

    public IIntArray getGuiData() {
        int energyStored = energy.getEnergyStored();
        return new IIntArray() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0:
                        return energyStored & 0xffff;
                    case 1:
                        return (energyStored >> 16) & 0xffff;
                    case 2:
                        return progress & 0xffff;
                    case 3:
                        return (progress >> 16) & 0xffff;
                    case 4:
                        return progressTotal & 0xffff;
                    case 5:
                        return (progressTotal >> 16) & 0xffff;
                    default:
                        return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        energy.setEnergyStored((this.get(1) << 16) | (value & 0xffff));
                        break;
                    case 1:
                        energy.setEnergyStored((value << 16) | (this.get(0) & 0xffff));
                        break;
                    case 2:
                        progress = (this.get(3) << 16) | (value & 0xffff);
                        break;
                    case 3:
                        progress = (value << 16) | (this.get(2) & 0xffff);
                        break;
                    case 4:
                        progressTotal = (this.get(5) << 16) | (value & 0xffff);
                        break;
                    case 5:
                        progressTotal = (value << 16) | (this.get(4) & 0xffff);
                        break;
                    default:
                }
            }

            @Override
            public int getCount() {
                return 6;
            }
        };
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

    @Override
    public CompoundNBT save(CompoundNBT tag) {
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
