/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.sieve;

import mods.redfire.simplemachinery.Config;
import mods.redfire.simplemachinery.registry.TileEntities;
import mods.redfire.simplemachinery.tileentities.machine.energy.EnergyMachineTile;
import mods.redfire.simplemachinery.util.energy.EnergyCoil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class SieveTile extends EnergyMachineTile<SieveRecipe> implements INamedContainerProvider {
	public static final int ITEM_INPUTS = 1;
	public static final int ITEM_OUTPUTS = 6;

	public SieveTile() {
		super(TileEntities.SIEVE_TILE.get(), ITEM_INPUTS, ITEM_OUTPUTS,
				0, 0, 0, 0,
				new EnergyCoil(Config.SIEVE_COIL_CAPACITY.get(), Config.SIEVE_COIL_IO.get()));
	}

	@Override
	protected Optional<SieveRecipe> getRecipe() {
		return SieveRecipe.getRecipe(level, getCombinedInputInv());
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("screen.simplemachinery.sieve");
	}

	@Nullable
	@Override
	public Container createMenu(int windowId, @Nonnull PlayerInventory playerInv, @Nonnull PlayerEntity player) {
		return new SieveContainer(windowId, level, worldPosition, playerInv, this);
	}
}
