/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.machine;

import mods.redfire.simplemachinery.network.MachineTilePacket;
import mods.redfire.simplemachinery.network.Networking;
import mods.redfire.simplemachinery.util.IMachineInventoryCallback;
import mods.redfire.simplemachinery.util.MachineCombinedInventory;
import mods.redfire.simplemachinery.util.energy.EnergyCoil;
import mods.redfire.simplemachinery.util.inventory.fluid.MachineFluidInventory;
import mods.redfire.simplemachinery.util.inventory.fluid.MachineFluidTank;
import mods.redfire.simplemachinery.util.inventory.fluid.TankGroup;
import mods.redfire.simplemachinery.util.inventory.item.MachineInventory;
import mods.redfire.simplemachinery.util.inventory.item.MachineItemSlot;
import mods.redfire.simplemachinery.util.inventory.item.SlotGroup;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MachineTile<T extends MachineRecipe> extends TileEntity implements ITickableTileEntity, IMachineInventoryCallback {
	protected MachineInventory inventory = new MachineInventory(this);
	protected MachineFluidInventory tankInventory = new MachineFluidInventory(this);

	protected EnergyCoil energy;

	protected LazyOptional<?> itemCap = LazyOptional.empty();
	protected LazyOptional<?> fluidCap = LazyOptional.empty();
	protected LazyOptional<?> energyCap = LazyOptional.empty();

	protected List<Integer> itemInputCounts = new ArrayList<>();
	protected List<Integer> fluidInputCounts = new ArrayList<>();

	protected Optional<T> currentRecipe = Optional.empty();
	protected int progress = 0;
	protected int totalProgress = 0;

	protected MachineTile(TileEntityType<?> type, int itemInputs, int itemOutputs, int fluidInputs, int fluidInputsCapacity, int fluidOutputs, int fluidOutputsCapacity, EnergyCoil energy) {
		super(type);
		inventory.addSlots(SlotGroup.INPUT, itemInputs);
		tankInventory.addTanks(TankGroup.INPUT, fluidInputs, fluidInputsCapacity);
		inventory.addSlots(SlotGroup.OUTPUT, itemOutputs);
		tankInventory.addTanks(TankGroup.OUTPUT, fluidOutputs, fluidOutputsCapacity);
		this.energy = energy;

		updateHandlers();
	}

	protected MachineTile(TileEntityType<?> type, List<MachineItemSlot> inputSlots, List<MachineItemSlot> outputSlots, List<MachineFluidTank> inputTanks, List<MachineFluidTank> outputTanks, List<MachineFluidTank> fuelTanks, EnergyCoil energy) {
		super(type);
		inventory.addSlots(SlotGroup.INPUT, inputSlots);
		inventory.addSlots(SlotGroup.OUTPUT, outputSlots);

		tankInventory.addTanks(TankGroup.FUEL, fuelTanks);
		tankInventory.addTanks(TankGroup.INPUT, inputTanks);
		tankInventory.addTanks(TankGroup.OUTPUT, outputTanks);
		this.energy = energy;

		updateHandlers();
	}

	public MachineTile<T> worldContext(BlockState state, IBlockReader world) {
		return this;
	}

	@Override
	public void tick() {}

	public void begin(T recipe) {}

	public void complete() {
		if (!validateInputs(itemInputCounts, fluidInputCounts)) {
			clear();
			return;
		}
		transferInputs();
		transferOutputs();
		setChanged();
	}

	public void clear() {
		progress = 0;
		totalProgress = 0;
		currentRecipe = Optional.empty();
		itemInputCounts = new ArrayList<>();
	}

	public boolean canBegin(T recipe) {
		List<Integer> itemInputCounts = recipe.getInputItemCounts(inventory);
		List<Integer> fluidInputCounts = recipe.getInputFluidCounts(tankInventory);

		return validateInputs(itemInputCounts, fluidInputCounts) && validateOutputs(recipe);
	}

	public boolean canComplete() {
		return progress <= 0;
	}

	protected boolean validateInputs(List<Integer> itemInputCounts, List<Integer> fluidInputCounts) {
		List<MachineItemSlot> inputSlots = inputSlots();
		List<MachineFluidTank> inputTanks = inputTanks();

		for (int i = 0; i < inputSlots.size() && i < itemInputCounts.size(); i++) {
			int inputCount = itemInputCounts.get(i);
			if (inputCount == 0 || (inputCount > 0 && inputSlots.get(i).getItemStack().getCount() < inputCount)) {
				return false;
			}
		}
		for (int i = 0; i < inputTanks.size() && i < fluidInputCounts.size(); ++i) {
			int inputCount = fluidInputCounts.get(i);
			FluidStack input = inputTanks.get(i).getFluidStack();
			if (inputCount > 0 && (input.isEmpty() || input.getAmount() < inputCount)) {
				return false;
			}
		}
		return true;
	}

	protected boolean validateOutputs(T recipe) {
		List<MachineItemSlot> outputSlots = outputSlots();
		List<ItemStack> recipeOutputItems = recipe.getOutputItems();

		List<MachineFluidTank> outputTanks = outputTanks();
		List<FluidStack> recipeOutputFluids = recipe.getOutputFluids();

		boolean[] used = new boolean[outputSlots.size()];
		for (ItemStack recipeOutput : recipeOutputItems) {
			boolean matched = false;
			for (int i = 0; i < outputSlots.size(); i++) {
				if (used[i]) {
					continue;
				}
				ItemStack output = outputSlots.get(i).getItemStack();
				if (output.getCount() + Math.max(0, recipeOutput.getCount()) > output.getMaxStackSize()) {
					continue;
				}
				if (output.getItem() == recipeOutput.getItem() && ItemStack.tagMatches(output, recipeOutput)) {
					used[i] = true;
					matched = true;
					break;
				}
			}

			if (!matched) {
				for (int i = 0; i < outputSlots.size(); ++i) {
					if (used[i]) {
						continue;
					}
					if (outputSlots.get(i).isEmpty()) {
						used[i] = true;
						matched = true;
						break;
					}
				}
			}

			if (!matched) {
				return false;
			}
		}

		used = new boolean[outputTanks.size()];
		for (FluidStack recipeOutput : recipeOutputFluids) {
			boolean matched = false;
			for (int i = 0; i < outputTanks.size(); ++i) {
				FluidStack output = outputTanks.get(i).getFluidStack();
				if (used[i] || outputTanks.get(i).getCapacity() - output.getAmount() < recipeOutput.getAmount()) {
					continue;
				}
				if (output.getFluid() == recipeOutput.getFluid()) {
					used[i] = true;
					matched = true;
					break;
				}
			}
			if (!matched) {
				for (int i = 0; i < outputTanks.size(); ++i) {
					if (used[i]) {
						continue;
					}
					if (outputTanks.get(i).isEmpty()) {
						used[i] = true;
						matched = true;
						break;
					}
				}
			}
			if (!matched) {
				return false;
			}
		}

		return true;
	}

	protected void transferInputs() {
		for (int i = 0; i < itemInputCounts.size(); ++i) {
			inputSlots().get(i).consume(itemInputCounts.get(i));
		}
		for (int i = 0; i < fluidInputCounts.size(); ++i) {
			inputTanks().get(i).modify(-fluidInputCounts.get(i));
		}
	}

	protected void transferOutputs() {
		if (!currentRecipe.isPresent()) {
			return;
		}

		List<ItemStack> recipeOutputItems = currentRecipe.get().getOutputItems();
		List<Float> recipeOutputChances = currentRecipe.get().getOutputItemChances();
		List<FluidStack> recipeOutputFluids = currentRecipe.get().getOutputFluids();

		for (int i = 0, recipeOutputItemsSize = recipeOutputItems.size(); i < recipeOutputItemsSize; i++) {
			ItemStack recipeOutput = recipeOutputItems.get(i);
			float chance = recipeOutputChances.get(i);
			int outputCount = chance <= 1.0 ? recipeOutput.getCount() : (int) chance;

			while (level.random.nextFloat() < chance) {
				boolean matched = false;

				for (MachineItemSlot slot : outputSlots()) {
					ItemStack output = slot.getItemStack();

					if (output.getItem() == recipeOutput.getItem() && ItemStack.tagMatches(output, recipeOutput)
							&& output.getCount() + outputCount <= output.getMaxStackSize()) {
						output.grow(outputCount);
						matched = true;
						break;
					}
				}
				if (!matched) {
					for (MachineItemSlot slot : outputSlots()) {
						if (slot.isEmpty()) {
							slot.setItemStack(new ItemStack(recipeOutput.getItem(), outputCount));
							break;
						}
					}
				}

				chance -= outputCount;
				outputCount = 1;
			}
		}

		for (FluidStack recipeOutput : recipeOutputFluids) {
			boolean matched = false;
			for (MachineFluidTank tank : outputTanks()) {
				FluidStack output = tank.getFluidStack();
				if (tank.getCapacity() - tank.getStored() >= recipeOutput.getAmount() && output.getFluid().equals(recipeOutput.getFluid())) {
					output.setAmount(output.getAmount() + recipeOutput.getAmount());
					matched = true;
					break;
				}
			}
			if (!matched) {
				for (MachineFluidTank tank : outputTanks()) {
					if (tank.isEmpty()) {
						tank.setFluidStack(recipeOutput.copy());
						break;
					}
				}
			}
		}
	}

	protected Optional<T> getRecipe() {
		return Optional.empty();
	}

	public MachineInventory getItemInv() {
		return inventory;
	}

	public MachineFluidInventory getFluidInv() {
		return tankInventory;
	}

	public MachineCombinedInventory getCombinedInv() {
		return new MachineCombinedInventory(inventory, tankInventory);
	}

	public MachineCombinedInventory getCombinedInputInv() {
		return new MachineCombinedInventory(inventory.getInputInventory(), tankInventory.getInputInventory());
	}

	public int getProgress() {
		return progress;
	}

	public int getTotalProgress() {
		return totalProgress;
	}

	protected void initHandlers() {
		inventory.initHandler();
		tankInventory.initHandler();
	}

	public List<MachineItemSlot> inputSlots() {
		return inventory.getInputSlots();
	}

	protected List<MachineItemSlot> outputSlots() {
		return inventory.getOutputSlots();
	}

	public List<MachineFluidTank> inputTanks() {
		return tankInventory.getInputTanks();
	}

	protected List<MachineFluidTank> outputTanks() {
		return tankInventory.getOutputTanks();
	}

	protected List<MachineFluidTank> fuelTanks() {
		return tankInventory.getFuelTanks();
	}

	public MachineItemSlot getSlot(int slot) {
		return inventory.getSlot(slot);
	}

	public MachineFluidTank getTank(int tank) {
		return tankInventory.getTank(tank);
	}

	public EnergyCoil getCoil() {
		return energy;
	}

	@Override
	public boolean clearSlot(int slot) {
		if (slot >= inventory.getSlots()) {
			return false;
		}
		if (inventory.getSlot(slot).clear()) {
			onInventoryChange(slot);
			return true;
		}
		return false;
	}

	@Override
	public boolean clearTank(int tank) {
		if (tank >= tankInventory.getTanks()) {
			return false;
		}
		if (tankInventory.getTank(tank).clear()) {
			onTankChange(tank);
			return true;
		}
		return false;
	}

	@Override
	public boolean clearEnergy(int coil) {
		return energy.clear();
	}

	@Override
	public void load(@Nonnull BlockState state, @Nonnull CompoundNBT tag) {
		super.load(state, tag);
		inventory.read(tag);
		tankInventory.read(tag);
		energy.read(tag);
	}

	@Nonnull
	@Override
	public CompoundNBT save(@Nonnull CompoundNBT tag) {
		super.save(tag);
		inventory.write(tag);
		tankInventory.write(tag);
		energy.write(tag);

		return tag;
	}

	protected void updateHandlers() {
		LazyOptional<?> prevItemCap = itemCap;
		IItemHandler invHandler = inventory.getHandler();
		itemCap = inventory.hasAccessibleSlots() ? LazyOptional.of(() -> invHandler) : LazyOptional.empty();
		prevItemCap.invalidate();

		LazyOptional<?> prevFluidCap = fluidCap;
		IFluidHandler fluidHandler = tankInventory.getHandler();
		fluidCap = tankInventory.hasAccessibleTanks() ? LazyOptional.of(() -> fluidHandler) : LazyOptional.empty();
		prevFluidCap.invalidate();

		LazyOptional<?> prevEnergyCap = energyCap;
		energyCap = energy.getCapacity() > 0 ? LazyOptional.of(() -> energy) : LazyOptional.empty();
		prevEnergyCap.invalidate();
	}

	public MachineTilePacket getGuiPacket() {
		return new MachineTilePacket(getBlockPos(), tankInventory, energy.getEnergyStored(), progress, totalProgress);
	}

	public void handleGuiPacket(MachineTilePacket buffer) {
		for (int i = 0; i < buffer.fluids.size(); i++) {
			tankInventory.set(i, buffer.fluids.get(i));
		}

		energy.setEnergyStored(buffer.energy);
		progress = buffer.progress;
		totalProgress = buffer.totalProgress;
	}

	public void sendGuiNetworkData(MachineContainer<?> container, IContainerListener player) {
		if (player instanceof ServerPlayerEntity && (!(player instanceof FakePlayer))) {
			Networking.sendToClient(getGuiPacket(), (ServerPlayerEntity) player);
		}
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && inventory.hasAccessibleSlots()) {
			return getItemHandlerCapability(side);
		} else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && tankInventory.hasAccessibleTanks()) {
			return getFluidHandlerCapability(side);
		} else if (cap == CapabilityEnergy.ENERGY && energy.getMaxEnergyStored() > 0) {
			return getEnergyCapability(side);
		}
		return super.getCapability(cap, side);
	}

	protected <T> LazyOptional<T> getItemHandlerCapability(@Nullable Direction side) {
		if (!itemCap.isPresent() && inventory.hasAccessibleSlots()) {
			IItemHandler handler = inventory.getHandler();
			itemCap = LazyOptional.of(() -> handler);
		}
		return itemCap.cast();
	}

	protected <T> LazyOptional<T> getFluidHandlerCapability(@Nullable Direction side) {
		if (!fluidCap.isPresent() && tankInventory.hasAccessibleTanks()) {
			IFluidHandler handler = tankInventory.getHandler();
			fluidCap = LazyOptional.of(() -> handler);
		}
		return fluidCap.cast();
	}

	protected <T> LazyOptional<T> getEnergyCapability(@Nullable Direction side) {
		if (!energyCap.isPresent() && energy.getCapacity() > 0) {
			energyCap = LazyOptional.of(() -> energy);
		}
		return energyCap.cast();
	}
}
