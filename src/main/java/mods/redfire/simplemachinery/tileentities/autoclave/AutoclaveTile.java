/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.autoclave;

import mods.redfire.simplemachinery.Config;
import mods.redfire.simplemachinery.registry.TileEntities;
import mods.redfire.simplemachinery.tileentities.machine.fluid.FluidMachineTile;
import mods.redfire.simplemachinery.util.inventory.fluid.MachineFluidTank;
import mods.redfire.simplemachinery.util.inventory.item.MachineItemSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Optional;

public class AutoclaveTile extends FluidMachineTile<AutoclaveRecipe> implements INamedContainerProvider {
	public static final int ITEM_INPUTS = 1;
	public static final int ITEM_OUTPUTS = 1;

	public AutoclaveTile() {
		super(TileEntities.AUTOCLAVE_TILE.get(), Collections.singletonList(new MachineItemSlot()),
				Collections.singletonList(new MachineItemSlot()),
				Collections.singletonList(new MachineFluidTank(Config.AUTOCLAVE_TANK_CAPACITY.get())),
				Collections.emptyList(),
				new MachineFluidTank(Config.AUTOCLAVE_STEAM_CAPACITY.get(), e -> e.getFluid().getRegistryName().getPath().equals("steam")));
	}

	@Override
	protected Optional<AutoclaveRecipe> getRecipe() {
		return AutoclaveRecipe.getRecipe(level, getCombinedInputInv());
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("screen.simplemachinery.autoclave");
	}

	@Nullable
	@Override
	public Container createMenu(int windowId, @Nonnull PlayerInventory playerInv, @Nonnull PlayerEntity player) {
		return new AutoclaveContainer(windowId, level, worldPosition, playerInv, this);
	}
}
