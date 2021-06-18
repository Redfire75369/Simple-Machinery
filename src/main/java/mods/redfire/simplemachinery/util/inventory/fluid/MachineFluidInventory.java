/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util.inventory.fluid;

import mods.redfire.simplemachinery.util.IMachineInventoryCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.fluids.FluidStack.EMPTY;

public class MachineFluidInventory extends MachineFluidHandler {
	protected List<MachineFluidTank> inputTanks = new ArrayList<>();
	protected List<MachineFluidTank> outputTanks = new ArrayList<>();
	protected List<MachineFluidTank> fuelTanks = new ArrayList<>();

	protected IFluidHandler inputHandler;
	protected IFluidHandler outputHandler;
	protected IFluidHandler fuelHandler;
	protected IFluidHandler allHandler;

	public MachineFluidInventory(IMachineInventoryCallback tile) {
		super(tile);
	}

	public MachineFluidInventory(@Nullable IMachineInventoryCallback tile, @Nonnull List<MachineFluidTank> tanks) {
		super(tile, tanks);
	}

	public void addTank(TankGroup group, MachineFluidTank tank) {
		if (allHandler != null) {
			return;
		}

		tanks.add(tank);
		switch (group) {
			case INPUT:
				inputTanks.add(tank);
				break;
			case OUTPUT:
				outputTanks.add(tank);
				break;
			case FUEL:
				fuelTanks.add(tank);
				break;
			default:
		}
	}

	public void addTanks(TankGroup group, int amount, int capacity) {
		for (int i = 0; i < amount; ++i) {
			addTank(group, new MachineFluidTank(capacity));
		}
	}

	public void addTanks(TankGroup group, List<MachineFluidTank> tanks) {
		for (MachineFluidTank tank : tanks) {
			addTank(group, tank);
		}
	}

	protected void optimize() {
		((ArrayList<MachineFluidTank>) tanks).trimToSize();
		((ArrayList<MachineFluidTank>) inputTanks).trimToSize();
		((ArrayList<MachineFluidTank>) outputTanks).trimToSize();
		((ArrayList<MachineFluidTank>) fuelTanks).trimToSize();
	}

	public void initHandlers() {
		optimize();

		inputHandler = new MachineFluidHandler(tile, inputTanks);
		outputHandler = new MachineFluidHandler(tile, outputTanks);
		fuelHandler = new MachineFluidHandler(tile, fuelTanks);
		allHandler = new MachineFluidHandler(tile, tanks);
	}

	@Nonnull
	public FluidStack get(int tank) {
		return tanks.get(tank).getFluidStack();
	}

	public void set(int tank, @Nonnull FluidStack stack) {
		tanks.get(tank).setFluidStack(stack);
	}

	public void clear() {
		for (MachineFluidTank tank : tanks) {
			tank.setFluidStack(EMPTY);
		}
	}

	public boolean hasInputTanks() {
		return inputTanks.size() > 0;
	}

	public boolean hasOutputTanks() {
		return outputTanks.size() > 0;
	}

	public boolean hasFuelTanks() {
		return fuelTanks.size() > 0;
	}

	public boolean hasAccessibleTanks() {
		return hasInputTanks() || hasOutputTanks() || hasFuelTanks();
	}

	public MachineFluidTank getTank(int tank) {
		return tanks.get(tank);
	}

	public List<MachineFluidTank> getInputTanks() {
		return inputTanks;
	}

	public List<MachineFluidTank> getOutputTanks() {
		return outputTanks;
	}

	public List<MachineFluidTank> getFuelTanks() {
		return fuelTanks;
	}

	public MachineFluidInventory getInputInventory() {
		return new MachineFluidInventory(tile, getInputTanks());
	}

	public MachineFluidInventory getOutputInventory() {
		return new MachineFluidInventory(tile, getOutputTanks());
	}

	public MachineFluidInventory getFuelInventory() {
		return new MachineFluidInventory(tile, getFuelTanks());
	}

	public IFluidHandler getHandler(TankGroup group) {
		if (allHandler == null) {
			initHandlers();
		}
		switch (group) {
			case INPUT:
				return inputHandler;
			case OUTPUT:
				return outputHandler;
			case FUEL:
				return fuelHandler;
			case ALL:
				return allHandler;
			default:
		}
		return EmptyFluidHandler.INSTANCE;
	}

	public MachineFluidInventory read(CompoundNBT tag) {
		for (MachineFluidTank tank : tanks) {
			tank.setFluidStack(EMPTY);
		}
		ListNBT list = tag.getList("TankInventory", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); ++i) {
			CompoundNBT tankTag = list.getCompound(i);
			int tank = tankTag.getByte("Tank");
			if (tank >= 0 && tank < tanks.size()) {
				tanks.get(tank).read(tankTag);
			}
		}
		return this;
	}

	public CompoundNBT write(CompoundNBT tag) {
		if (tanks.size() <= 0) {
			return tag;
		}
		ListNBT list = new ListNBT();
		for (int i = 0; i < tanks.size(); ++i) {
			if (!tanks.get(i).isEmpty()) {
				CompoundNBT tankTag = new CompoundNBT();
				tankTag.putByte("Tank", (byte) i);
				tanks.get(i).write(tankTag);
				list.add(tankTag);
			}
		}
		if (!list.isEmpty()) {
			tag.put("TankInventory", list);
		}
		return tag;
	}

	public int getContainerSize() {
		return tanks.size();
	}

	@Nonnull
	public FluidStack getFluid(int tank) {
		return get(tank);
	}

	@Nonnull
	public FluidStack removeFluid(int tank, int amount) {
		return tanks.get(tank).drain(amount, FluidAction.EXECUTE);
	}

	@Nonnull
	public FluidStack removeFluidNoUpdate(int tank) {
		tanks.get(tank).setFluidStack(EMPTY);
		return EMPTY;
	}

	public void setChanged() {
	}

	public boolean stillValid(@Nonnull PlayerEntity player) {
		return true;
	}

	public void clearContent() {
		clear();
	}
}
