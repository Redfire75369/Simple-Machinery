package redfire.mods.simplemachinery.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class Tank extends FluidTank {
	public Tank(int capacity) {
		super(capacity);
	}
	public Tank(int capacity, FluidStack fluid) {
		super(fluid, capacity);
	}

	public final NBTTagCompound writeToNBT(NBTTagCompound nbt, String name) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("capacity", capacity);
		writeToNBT(tag);
		nbt.setTag(name, tag);
		return nbt;
	}
	public final Tank readFromNBT(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			setCapacity(tag.getInteger("capacity"));
			readFromNBT(tag);
		}
		return this;
	}
}
