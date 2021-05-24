package mods.redfire.simplemachinery.tileentities.machine;

import mods.redfire.simplemachinery.util.inventory.MachineInventory;
import mods.redfire.simplemachinery.util.inventory.MachineItemSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class MachineRecipe implements IRecipe<MachineInventory> {
    protected final ResourceLocation id;
    protected final List<Ingredient> inputItems = new ArrayList<>();
    protected final List<ItemStack> outputItems = new ArrayList<>();

    protected int energy;
    protected int time;

    protected MachineRecipe(ResourceLocation id, int energy, int time, List<Ingredient> inputItems, List<ItemStack> outputItems) {
        this.id = id;

        this.energy = energy;
        this.time = time;

        if (inputItems != null) {
            this.inputItems.addAll(inputItems);
        }
        if (outputItems != null) {
            this.outputItems.addAll(outputItems);
        }
        trim();
    }

    private void trim() {
        ((ArrayList<Ingredient>) this.inputItems).trimToSize();
        ((ArrayList<ItemStack>) this.outputItems).trimToSize();
    }

    public List<Ingredient> getInputItems() {
        return inputItems;
    }

    public List<Integer> getInputItemCounts(List<MachineItemSlot> inputSlots) {
        List<Ingredient> inputs = getInputItems();
        List<Integer> counts = new ArrayList<>(inputSlots.size());

        boolean[] used = new boolean[inputSlots.size()];
        for (Ingredient input : inputs) {
            boolean matched = false;
            for (int i = 0; i < inputSlots.size(); i++) {
                if (!used[i] && !matched) {
                    MachineItemSlot slot = inputSlots.get(i);
                    ItemStack item = slot.getItemStack();

                    if (input.test(item)) {
                        counts.set(i, input.getItems()[0].getCount());
                        used[i] = true;
                        matched = true;
                    }
                }
            }
        }

        return counts;
    }

    public List<ItemStack> getOutputItems() {
        return outputItems;
    }

    public int getEnergy() {
        return energy;
    }

    public int getTime() {
        return time;
    }

    public int getEnergyRate() {
        return getEnergy() / getTime();
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public boolean matches(@Nonnull MachineInventory inv, @Nonnull World worldIn) {
        return true;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(inputItems);
        return list;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull MachineInventory inv) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public abstract IRecipeSerializer<?> getSerializer();

    @Nonnull
    @Override
    public abstract IRecipeType<?> getType();
}
