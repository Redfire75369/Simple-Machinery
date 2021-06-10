package mods.redfire.simplemachinery.util.fluid;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

import static net.minecraftforge.fluids.FluidStack.EMPTY;

public class MachineFluidTank implements IFluidHandlerModifiable {
	protected Predicate<FluidStack> validator;

	@Nonnull
	protected FluidStack fluid = EMPTY;
	protected int capacity;

	public MachineFluidTank(int capacity) {
		this(capacity, e -> true);
	}

	public MachineFluidTank(int capacity, Predicate<FluidStack> validator) {
		this.capacity = capacity;
		this.validator = validator;
	}

	public MachineFluidTank setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	public MachineFluidTank setValidator(Predicate<FluidStack> validator) {
		if (validator != null) {
			this.validator = validator;
		}
		return this;
	}

	public boolean isFluidValid(@Nonnull FluidStack stack) {
		return validator.test(stack);
	}

	public void setFluidStack(@Nonnull FluidStack stack) {
		fluid = stack;
	}

	public MachineFluidTank read(CompoundNBT nbt) {
		FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
		setFluidStack(fluid);
		return this;
	}

	public CompoundNBT write(CompoundNBT nbt) {
		fluid.writeToNBT(nbt);
		nbt.putInt("Capacity", capacity);
		return nbt;
	}

	@Nonnull
	public FluidStack getFluidStack() {
		return fluid;
	}

	public int getAmount() {
		return fluid.getAmount();
	}

	public boolean isEmpty() {
		return fluid.isEmpty();
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@Nonnull
	@Override
	public FluidStack getFluidInTank(int tank) {
		return fluid;
	}

	@Override
	public void setFluidInTank(int tank, @Nonnull FluidStack stack) {
		setFluidStack(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (resource.isEmpty() || !isFluidValid(resource)) {
			return 0;
		}
		if (action.simulate()) {
			if (fluid.isEmpty()) {
				return Math.min(capacity, resource.getAmount());
			}
			if (!fluid.isFluidEqual(resource)) {
				return 0;
			}
			return Math.min(capacity - fluid.getAmount(), resource.getAmount());
		}
		if (fluid.isEmpty()) {
			setFluidStack(new FluidStack(resource, Math.min(capacity, resource.getAmount())));
			return fluid.getAmount();
		}
		if (!fluid.isFluidEqual(resource)) {
			return 0;
		}
		int filled = capacity - fluid.getAmount();

		if (resource.getAmount() < filled) {
			fluid.grow(resource.getAmount());
			filled = resource.getAmount();
		} else {
			fluid.setAmount(capacity);
		}
		return filled;
	}

	@Nonnull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (resource.isEmpty() || !resource.isFluidEqual(fluid)) {
			return EMPTY;
		}
		return drain(resource.getAmount(), action);
	}

	@Nonnull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		if (maxDrain <= 0 || fluid.isEmpty()) {
			return EMPTY;
		}
		int drained = maxDrain;
		if (fluid.getAmount() < drained) {
			drained = fluid.getAmount();
		}
		FluidStack stack = new FluidStack(fluid, drained);
		if (action.execute()) {
			fluid.shrink(drained);
			if (fluid.isEmpty()) {
				fluid = EMPTY;
			}
		}
		return stack;
	}

	@Override
	public int getTankCapacity(int tank) {
		return capacity;
	}

	@Override
	public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
		return isFluidValid(stack);
	}

	public boolean clear() {
		if (isEmpty()) {
			return false;
		}
		this.fluid = EMPTY;
		return true;
	}

	public void modify(int amount) {
		fluid.setAmount(Math.min(fluid.getAmount() + amount, getCapacity()));
		if (fluid.isEmpty()) {
			fluid = EMPTY;
		}
	}

	public int getCapacity() {
		return getTankCapacity(0);
	}

	public int getStored() {
		return fluid.getAmount();
	}
}
