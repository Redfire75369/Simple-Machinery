package redfire.mods.simplemachinery.tileentities.turntable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import redfire.mods.simplemachinery.Config;
import redfire.mods.simplemachinery.tileentities.autoclave.RecipesAutoclave;
import redfire.mods.simplemachinery.tileentities.generic.TileMachine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileTurntable extends TileMachine implements ITickable {
	public static EnergyStorage energy = new EnergyStorage(Config.turntable_fe_storage, Config.turntable_fe_io, Config.turntable_fe_io,0);
	private int progress = 0;
	private String currentRecipe = "";

	public TileTurntable() {
		super(1, 1, 0);
	}

	public int getProgress() {
		return progress;
	}

	public String getCurrentRecipe() {
		return currentRecipe;
	}

	@Override
	public void update() {
		if (!(world.isRemote)) {
			if (progress > 0) {
				if (energy.getEnergyStored() > Config.turntable_fe_io) {
					progress--;
					energy.extractEnergy(Config.turntable_fe_io, false);
				}
				if (progress <= 0) {
					attemptTurntable();
				}
			} else {
				startTurntable();
			}
			notifyUpdate();
		}
	}

	private void startTurntable() {
		for (int i = 0; i < input_slots; i++) {
			ItemStack input =  inputHandler.getStackInSlot(i);

			String recipe = RecipesTurntable.searchRecipes(input);

			RecipesTurntable instance = RecipesTurntable.instance();

			ItemStack output = instance.getOutput(recipe);
			if (!(output.isEmpty())) {
				if (insertOutput(output.copy(), true)
						&& energy.getEnergyStored() >= instance.getTotalEnergy(recipe)) {
					currentRecipe = recipe;
					progress = instance.getTicks(recipe);
				} else {
					currentRecipe = "";
				}
				break;
			}
		}
	}

	private void attemptTurntable() {
		for (int i = 0; i < input_slots; i++) {
			RecipesTurntable instance = RecipesTurntable.instance();
			ItemStack result = instance.getOutput(currentRecipe);
			if (!(result.isEmpty())) {
				if (insertOutput(result.copy(), false)) {
					inputHandler.extractItem(i, 1, false);
				}
				break;
			}
		}
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(combinedHandler);
			} else if (!(facing == EnumFacing.DOWN)) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputHandler);
			} else {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputHandler);
			}
		}
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energy);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		energy = new EnergyStorage(Config.turntable_fe_storage, Config.turntable_fe_io, Config.turntable_fe_io, compound.getInteger("energyStored"));
		progress = compound.getInteger("progress");
		currentRecipe = compound.getString("recipe");
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setInteger("energyStored", energy.getEnergyStored());
		compound.setInteger("progress", progress);
		compound.setString("recipe", currentRecipe);
		return compound;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound compound) {
		super.handleUpdateTag(compound);
		energy = new EnergyStorage(Config.turntable_fe_storage, Config.turntable_fe_io, Config.turntable_fe_io, compound.getInteger("energyStored"));
		progress = compound.getInteger("progress");
		currentRecipe = compound.getString("recipe");
	}
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound compound = super.getUpdateTag();
		compound.setInteger("energyStored", energy.getEnergyStored());
		compound.setInteger("progress", progress);
		compound.setString("recipe", currentRecipe);
		return compound;
	}
}
