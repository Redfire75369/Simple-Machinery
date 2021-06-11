/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mods.redfire.simplemachinery.SimpleMachinery;
import mods.redfire.simplemachinery.integration.jei.AutoclaveCategory;
import mods.redfire.simplemachinery.integration.jei.SieveCategory;
import mods.redfire.simplemachinery.integration.jei.TurntableCategory;
import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.tileentities.autoclave.AutoclaveRecipe;
import mods.redfire.simplemachinery.tileentities.sieve.SieveRecipe;
import mods.redfire.simplemachinery.tileentities.turntable.TurntableRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Objects;

@JeiPlugin
public class SimpleMachineryJEI implements IModPlugin {
	private static final ResourceLocation ID = new ResourceLocation(SimpleMachinery.MODID, "jei_plugin");

	@Nonnull
	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new AutoclaveCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SieveCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new TurntableCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(Blocks.AUTOCLAVE_BLOCK.get()), AutoclaveCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(Blocks.SIEVE_BLOCK.get()), SieveCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(Blocks.TURNTABLE_BLOCK.get()), TurntableCategory.UID);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ClientWorld world = Objects.requireNonNull(Minecraft.getInstance().level);

		registration.addRecipes(AutoclaveRecipe.getAllRecipes(world), AutoclaveCategory.UID);
		registration.addRecipes(SieveRecipe.getAllRecipes(world), SieveCategory.UID);
		registration.addRecipes(TurntableRecipe.getAllRecipes(world), TurntableCategory.UID);
	}
}
