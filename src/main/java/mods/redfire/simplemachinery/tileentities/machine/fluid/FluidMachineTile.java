package mods.redfire.simplemachinery.tileentities.machine.fluid;

import mods.redfire.simplemachinery.tileentities.autoclave.AutoclaveTile;
import mods.redfire.simplemachinery.tileentities.machine.MachineRecipe;
import mods.redfire.simplemachinery.tileentities.machine.MachineTile;
import mods.redfire.simplemachinery.util.energy.EnergyCoil;
import mods.redfire.simplemachinery.util.fluid.MachineFluidTank;
import mods.redfire.simplemachinery.util.inventory.MachineItemSlot;
import net.minecraft.tileentity.TileEntityType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FluidMachineTile<T extends FluidMachineRecipe> extends MachineTile<T> {
	public FluidMachineTile(TileEntityType<?> type, List<MachineItemSlot> inputSlots, List<MachineItemSlot> outputSlots, List<MachineFluidTank> inputTanks, List<MachineFluidTank> outputTanks, List<MachineFluidTank> fuelTanks) {
		super(type, inputSlots, outputSlots, inputTanks, outputTanks, fuelTanks, new EnergyCoil(0));
	}

	@Override
	public void tick() {
		if (level == null || level.isClientSide) {
			return;
		}

		MachineFluidTank fuelTank = tankInventory.getTank(0);

		if (currentRecipe.isPresent()) {
			T recipe = currentRecipe.get();
			fuelTank.modify(-recipe.getFuelRate());
			data.setInternal(0, data.getInternal(0) - 1);

			if (canComplete()) {
				complete();
				clear();
			} else if (fuelTank.getStored() < recipe.getFuelRate()) {
				clear();
			}
			// TODO: [FIX] Recipe is not cleared when inputs are removed.
		} else {
			Optional<T> recipe = getRecipe();
			if (recipe.isPresent() && canBegin(recipe.get())) {
				begin(recipe.get());
			}
		}
	}

	@Override
	public void begin(T recipe) {
		currentRecipe = Optional.of(recipe);
		data.setInternal(0, recipe.getTime());
		data.setInternal(1, recipe.getTime());
		itemInputCounts = recipe.getInputItemCounts(inventory);
		fluidInputCounts = recipe.getInputFluidCounts(tankInventory);
	}

	@Override
	public boolean canBegin(T recipe) {
		return tankInventory.getTank(0).getStored() >= recipe.getFuel() && super.canBegin(recipe);
	}
}
