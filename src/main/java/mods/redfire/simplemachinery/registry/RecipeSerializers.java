package mods.redfire.simplemachinery.registry;

import mods.redfire.simplemachinery.tileentities.autoclave.AutoclaveRecipe;
import mods.redfire.simplemachinery.tileentities.sieve.SieveRecipe;
import mods.redfire.simplemachinery.tileentities.turntable.TurntableRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;

public class RecipeSerializers {
	public static void register(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		event.getRegistry().register(new AutoclaveRecipe.Serializer().setRegistryName(Names.AUTOCLAVE));
		event.getRegistry().register(new SieveRecipe.Serializer().setRegistryName(Names.SIEVE));
		event.getRegistry().register(new TurntableRecipe.Serializer().setRegistryName(Names.TURNTABLE));
	}
}
