/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyCoil implements IEnergyStorage, INBTSerializable<CompoundNBT> {
	public final int MAX_CAPACITY = Integer.MAX_VALUE;
	protected int energy;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public EnergyCoil(int capacity) {
		this(capacity, capacity, capacity, 0);
	}

	public EnergyCoil(int capacity, int maxTransfer) {
		this(capacity, maxTransfer, maxTransfer, 0);
	}

	public EnergyCoil(int capacity, int maxReceive, int maxExtract) {
		this(capacity, maxReceive, maxExtract, 0);
	}

	public EnergyCoil(int capacity, int maxReceive, int maxExtract, int energy) {
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
		this.energy = Math.max(0, Math.min(capacity, energy));
	}

	public int getMaxReceive() {
		return maxReceive;
	}

	public EnergyCoil setMaxReceive(int maxReceive) {
		this.maxReceive = Math.max(0, capacity);
		return this;
	}

	public int getMaxExtract() {
		return maxExtract;
	}

	public EnergyCoil setMaxExtract(int maxExtract) {
		this.maxExtract = Math.max(0, capacity);
		return this;
	}

	public int receiveEnergyOverride(int maxReceive, boolean simulate) {
		int energyReceived = Math.min(capacity - energy, maxReceive);
		if (!simulate) {
			energy += energyReceived;
		}
		return energyReceived;
	}

	public int extractEnergyOverride(int maxExtract, boolean simulate) {
		int energyExtracted = Math.min(energy, maxExtract);
		if (!simulate) {
			energy -= energyExtracted;
		}
		return energyExtracted;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
		if (!simulate) {
			energy += energyReceived;
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
		if (!simulate) {
			energy -= energyExtracted;
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored() {
		return energy;
	}

	public void setEnergyStored(int amount) {

		energy = amount;
		energy = Math.max(0, Math.min(capacity, energy));
	}

	@Override
	public int getMaxEnergyStored() {
		return capacity;
	}

	@Override
	public boolean canExtract() {
		return maxExtract > 0;
	}

	@Override
	public boolean canReceive() {
		return maxReceive > 0;
	}

	public boolean clear() {
		if (isEmpty()) {
			return false;
		}
		energy = 0;
		return true;
	}

	public void modify(int amount) {
		energy += amount;
		if (energy > capacity) {
			energy = capacity;
		} else if (energy < 0) {
			energy = 0;
		}
	}

	public boolean isEmpty() {
		return energy <= 0 && capacity > 0;
	}

	public int getCapacity() {
		return getMaxEnergyStored();
	}

	public EnergyCoil setCapacity(int capacity) {
		this.capacity = Math.max(0, capacity);
		this.energy = Math.max(0, Math.min(capacity, energy));
		return this;
	}

	public int getStored() {
		return getEnergyStored();
	}

	public void readFromBuffer(PacketBuffer buffer) {
		setCapacity(buffer.readInt());
		setEnergyStored(buffer.readInt());
		setMaxExtract(buffer.readInt());
		setMaxReceive(buffer.readInt());
	}

	public void writeToBuffer(PacketBuffer buffer) {
		buffer.writeInt(getMaxEnergyStored());
		buffer.writeInt(getEnergyStored());
		buffer.writeInt(getMaxExtract());
		buffer.writeInt(getMaxReceive());
	}

	public EnergyCoil read(CompoundNBT nbt) {
		this.energy = nbt.getInt("Energy");
		return this;
	}

	public CompoundNBT write(CompoundNBT nbt) {
		if (this.capacity <= 0) {
			return nbt;
		}
		nbt.putInt("Energy", energy);
		return nbt;
	}

	public CompoundNBT writeWithParams(CompoundNBT nbt) {
		if (this.capacity <= 0) {
			return nbt;
		}
		nbt.putInt("Energy", energy);
		return nbt;
	}

	@Override
	public CompoundNBT serializeNBT() {
		return write(new CompoundNBT());
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		read(nbt);
	}
}
