package redfire.mods.simplemachinery.util;

import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class CombinedTank {
	protected final Tank[] tanks;
	protected final int[] baseIndex;
	protected final int slotCount;

	public CombinedTank(Tank... tanks) {
		this.tanks = tanks;
		this.baseIndex = new int[tanks.length];
		int index = 0;
		for (int i = 0; i < tanks.length; i++) {
			index++;
			baseIndex[i] = index;
		}
		this.slotCount = index;
	}

	protected int getIndexForSlot(int slot) {
		if (slot < 0)
			return -1;

		for (int i = 0; i < baseIndex.length; i++)
		{
			if (slot - baseIndex[i] < 0)
			{
				return i;
			}
		}
		return -1;
	}

	protected Tank getHandlerFromIndex(int index) {
		if (index < 0 || index >= tanks.length)
		{
			return (Tank) new Tank(0);
		}
		return tanks[index];
	}

	protected int getSlotFromIndex(int slot, int index)
	{
		if (index <= 0 || index >= baseIndex.length)
		{
			return slot;
		}
		return slot - baseIndex[index - 1];
	}

	public void insertFluid(int slot, @Nonnull FluidStack stack, boolean simulate) {
		int index = getIndexForSlot(slot);
		Tank handler = getHandlerFromIndex(index);
		slot = getSlotFromIndex(slot, index);
		handler.fill(stack, !simulate);
	}
	public void extractFluid(int slot, @Nonnull FluidStack stack, boolean simulate) {
		int index = getIndexForSlot(slot);
		Tank handler = getHandlerFromIndex(index);
		slot = getSlotFromIndex(slot, index);
		handler.drain(stack, !simulate);
	}



}
