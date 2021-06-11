/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

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
