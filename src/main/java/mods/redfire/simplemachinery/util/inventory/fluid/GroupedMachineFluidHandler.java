/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util.inventory.fluid;

import mods.redfire.simplemachinery.util.IMachineInventoryCallback;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GroupedMachineFluidHandler extends MachineFluidHandler {
	@Nonnull
	private final TankGroup group;

	public GroupedMachineFluidHandler(@Nonnull TankGroup group) {
		super();
		this.group = group;
	}

	public GroupedMachineFluidHandler(@Nonnull TankGroup group, @Nullable IMachineInventoryCallback tile) {
		super(tile);
		this.group = group;
	}

	public GroupedMachineFluidHandler(@Nonnull TankGroup group, @Nullable IMachineInventoryCallback tile, @Nonnull List<MachineFluidTank> tanks) {
		super(tile, tanks);
		this.group = group;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (group == TankGroup.OUTPUT) {
			return 0;
		}
		return super.fill(resource, action);
	}

	@Nonnull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (group == TankGroup.OUTPUT) {
			return FluidStack.EMPTY;
		}
		return super.drain(resource, action);
	}

	@Nonnull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		if (group == TankGroup.OUTPUT) {
			return FluidStack.EMPTY;
		}
		return super.drain(maxDrain, action);
	}
}
