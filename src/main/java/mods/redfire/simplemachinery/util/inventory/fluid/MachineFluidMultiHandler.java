/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util.inventory.fluid;

import net.minecraft.util.Tuple;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.fluids.FluidStack.EMPTY;

public class MachineFluidMultiHandler implements IFluidHandlerModifiable {
	List<GroupedMachineFluidHandler> handlers = new ArrayList<>();
	List<Integer> startIndexes = new ArrayList<>();

	public MachineFluidMultiHandler(GroupedMachineFluidHandler... handlers) {
		int index = 0;
		for (GroupedMachineFluidHandler handler : handlers) {
			this.handlers.add(handler);
			startIndexes.add(index);
			index += handler.getTanks();
		}
		startIndexes.add(index);
	}

	@Override
	public int getTanks() {
		return startIndexes.get(startIndexes.size() - 1);
	}

	@Nonnull
	@Override
	public FluidStack getFluidInTank(int tank) {
		if (tank < 0 || tank >= getTanks()) {
			return EMPTY;
		}

		Tuple<Integer, GroupedMachineFluidHandler> handler = getHandler(tank);
		return handler.getB().getFluidInTank(tank - startIndexes.get(handler.getA()));
	}

	@Override
	public void setFluidInTank(int tank, @Nonnull FluidStack stack) {
		if (tank < 0 || tank > getTanks()) {
			return;
		}

		Tuple<Integer, GroupedMachineFluidHandler> handler = getHandler(tank);
		handler.getB().setFluidInTank(tank - startIndexes.get(handler.getA()), stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		for (GroupedMachineFluidHandler handler : handlers) {
			int ret = handler.fill(resource, action);
			if (ret != 0) {
				return ret;
			}
		}
		return 0;
	}

	@Nonnull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		for (GroupedMachineFluidHandler handler : handlers) {
			FluidStack ret = handler.drain(resource, action);
			if (ret != EMPTY) {
				return ret;
			}
		}
		return EMPTY;
	}

	@Nonnull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		for (GroupedMachineFluidHandler handler : handlers) {
			FluidStack ret = handler.drain(maxDrain, action);
			if (ret != EMPTY) {
				return ret;
			}
		}
		return EMPTY;
	}

	@Override
	public int getTankCapacity(int tank) {
		if (tank < 0 || tank > getTanks()) {
			return 0;
		}

		Tuple<Integer, GroupedMachineFluidHandler> handler = getHandler(tank);
		return handler.getB().getTankCapacity(tank - startIndexes.get(handler.getA()));
	}

	@Override
	public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
		if (tank < 0 || tank > getTanks()) {
			return false;
		}

		Tuple<Integer, GroupedMachineFluidHandler> handler = getHandler(tank);
		return handler.getB().isFluidValid(tank - startIndexes.get(handler.getA()), stack);
	}

	private Tuple<Integer, GroupedMachineFluidHandler> getHandler(int slot) {
		int index = -1;
		for (int i = 0; i < startIndexes.size() - 1; i++) {
			if (startIndexes.get(i + 1) > slot) {
				index = i;
				break;
			}
		}

		return new Tuple<>(index, handlers.get(index));
	}
}
