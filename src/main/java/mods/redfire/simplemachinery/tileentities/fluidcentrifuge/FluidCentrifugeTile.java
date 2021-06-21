/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.fluidcentrifuge;

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

public class FluidCentrifugeTile extends EnergyMachineTile<FluidCentrifugeRecipe> implements INamedContainerProvider {
	public static final int ITEM_INPUTS = 0;
	public static final int FLUID_INPUTS = 1;
	public static final int ITEM_OUTPUTS = 3;
	public static final int FLUID_OUTPUTS = 3;

	public FluidCentrifugeTile() {
		super(TileEntities.FLUID_CENTRIFUGE_TILE.get(), ITEM_INPUTS, ITEM_OUTPUTS,
				FLUID_INPUTS, Config.FLUID_CENTRIFUGE_INPUT_TANK_CAPACITY.get(),
				FLUID_OUTPUTS, Config.FLUID_CENTRIFUGE_OUTPUT_TANKS_CAPACITY.get(),
				new EnergyCoil(Config.FLUID_CENTRIFUGE_COIL_CAPACITY.get(), Config.FLUID_CENTRIFUGE_COIL_IO.get()));
	}

	@Override
	protected Optional<FluidCentrifugeRecipe> getRecipe() {
		return FluidCentrifugeRecipe.getRecipe(level, getCombinedInputInv());
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("screen.simplemachinery.fluid_centrifuge");
	}

	@Nullable
	@Override
	public Container createMenu(int windowId, @Nonnull PlayerInventory playerInv, @Nonnull PlayerEntity player) {
		return new FluidCentrifugeContainer(windowId, level, worldPosition, playerInv, this);
	}
}
