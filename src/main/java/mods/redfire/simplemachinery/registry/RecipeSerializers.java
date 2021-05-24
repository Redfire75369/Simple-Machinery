package mods.redfire.simplemachinery.registry;

import mods.redfire.simplemachinery.tileentities.turntable.TurntableRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;

public class RecipeSerializers {
    public static void register(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().register(new TurntableRecipe.Serializer().setRegistryName("turntable"));
    }
}
