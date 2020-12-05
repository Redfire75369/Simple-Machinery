package redfire.mods.simplemachinery.tileentities.turntable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import redfire.mods.simplemachinery.tileentities.generic.RecipeMachine;

import java.util.Collections;

public class RecipeTurntable extends RecipeMachine {
    public RecipeTurntable(Ingredient input, ItemStack output, int ticks, int power) {
        super(Collections.singletonList(input), Collections.singletonList(output), Collections.singletonList(null), Collections.singletonList(null), ticks, power);
    }
}
