package mods.redfire.simplemachinery.util.fluid;

import mods.redfire.simplemachinery.util.IMachineInventoryCallback;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.fluids.FluidStack.EMPTY;

public class MachineFluidHandler implements IFluidHandlerModifiable {
	@Nullable
	protected IMachineInventoryCallback tile;
	protected List<MachineFluidTank> tanks;

	public MachineFluidHandler() {
		this(null);
	}

	public MachineFluidHandler(@Nullable IMachineInventoryCallback tile) {
		this.tile = tile;
		this.tanks = new ArrayList<>();
	}

	public MachineFluidHandler(@Nullable IMachineInventoryCallback tile, @Nonnull List<MachineFluidTank> tanks) {
		this.tile = tile;
		this.tanks = new ArrayList<>(tanks);
	}

	public boolean hasTanks() {
		return tanks.size() > 0;
	}

	public boolean isEmpty() {
		for (MachineFluidTank tank : tanks) {
			if (!tank.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public void onTankChange(int tank) {
		if (tile == null) {
			return;
		}
		tile.onTankChange(tank);
	}

	@Override
	public int getTanks() {
		return tanks.size();
	}

	@Nonnull
	@Override
	public FluidStack getFluidInTank(int tank) {
		if (tank < 0 || tank >= getTanks()) {
			return EMPTY;
		}
		return tanks.get(tank).getFluidStack();
	}

	@Override
	public void setFluidInTank(int tank, @Nonnull FluidStack stack) {
		if (tank < 0 || tank > getTanks()) {
			return;
		}
		tanks.get(tank).setFluidStack(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		int ret;
		for (MachineFluidTank tank : tanks) {
			ret = tank.fill(resource, action);
			if (ret > 0) {
				return ret;
			}
		}
		return 0;
	}

	@Nonnull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		FluidStack ret;
		for (MachineFluidTank tank : tanks) {
			ret = tank.drain(resource, action);
			if (!ret.isEmpty()) {
				return ret;
			}
		}
		return FluidStack.EMPTY;
	}

	@Nonnull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		FluidStack ret;
		for (MachineFluidTank tank : tanks) {
			ret = tank.drain(maxDrain, action);
			if (!ret.isEmpty()) {
				return ret;
			}
		}
		return FluidStack.EMPTY;
	}

	@Override
	public int getTankCapacity(int tank) {
		if (tank < 0 || tank > getTanks()) {
			return 0;
		}
		return tanks.get(tank).getTankCapacity(tank);
	}

	@Override
	public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
		if (tank < 0 || tank > getTanks()) {
			return false;
		}
		return tanks.get(tank).isFluidValid(stack);
	}
}
