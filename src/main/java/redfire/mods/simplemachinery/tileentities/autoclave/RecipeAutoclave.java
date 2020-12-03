package redfire.mods.simplemachinery.tileentities.autoclave;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import redfire.mods.simplemachinery.tileentities.generic.RecipeMachine;

import java.util.Collections;

public class RecipeAutoclave extends RecipeMachine {
    public int steamPower;
    public RecipeAutoclave(Ingredient input, ItemStack output, FluidStack fluidInput, int ticks, int steamPower) {
        super(Collections.singletonList(input), Collections.singletonList(output), Collections.singletonList(fluidInput), Collections.singletonList(null), ticks, 0);
        this.steamPower = steamPower;
    }

    public int getTotalSteam() {
        return ticks * steamPower;
    }
}
