package redfire.mods.simplemachinery.tileentities.autoclave;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import redfire.mods.simplemachinery.Config;
import redfire.mods.simplemachinery.tileentities.generic.TileMachine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Objects.isNull;


public class TileAutoclave extends TileMachine implements ITickable {
	public static int size;

	private int progress = 0;
	private String currentRecipe = "";

	public TileAutoclave() {
		super(2, 1, Config.autoclave_steam_storage, Config.autoclave_tank_storage);
		size = input_slots + output_slots;
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
				progress--;
				for (int i = 0; i < input_slots; i++) {
					if (!inputHandler.getStackInSlot(i).isEmpty()) {
						tanks.get(0).drain(RecipesAutoclave.instance().getSteamPower(currentRecipe),true);
						break;
					}
				}

				if (progress <= 0) {
					attemptAutoclave();
				}
			} else {
				startAutoclave();
			}
			notifyUpdate();
		}
	}

	private void startAutoclave() {
		for (int i = 0; i < input_slots; i++) {
			ItemStack input =  inputHandler.getStackInSlot(i);
			FluidStack fluidInput;
			if (isNull(tanks.get(1).getFluid())) {
				fluidInput = null;
			} else {
				fluidInput = new FluidStack(tanks.get(1).getFluid(), tanks.get(1).getFluidAmount());
			}
			String recipe = RecipesAutoclave.searchRecipes(input, fluidInput);

			RecipesAutoclave instance = RecipesAutoclave.instance();
			ItemStack output = instance.getOutput(recipe);
			if (!(output.isEmpty())) {
				FluidStack fluidUsed = instance.getFluidInput(recipe);
				if (insertOutput(output.copy(), true) && tanks.get(1).canDrainFluidType(fluidUsed)
						&& instance.getTotalSteam(recipe) < tanks.get(0).getFluidAmount()) {
					currentRecipe = recipe;
					progress = instance.getTicks(recipe);
				} else {
					currentRecipe = "";
				}
				break;
			}
		}
	}

	private void attemptAutoclave() {
		for (int i = 0; i < input_slots; i++) {
			RecipesAutoclave instance = RecipesAutoclave.instance();
			ItemStack output = instance.getOutput(currentRecipe);
			if (!(output.isEmpty())) {
				FluidStack fluidUsed = instance.getFluidInput(currentRecipe);
				if (insertOutput(output.copy(), false)
						&& tanks.get(1).canDrainFluidType(fluidUsed)) {
					inputHandler.extractItem(i, 1, false);
					tanks.get(1).drain(fluidUsed, true);
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
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
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
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.UP) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tanks.get(0));
			} else {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tanks.get(1));
			}
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		progress = compound.getInteger("progress");
		currentRecipe = compound.getString("recipe");
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setInteger("progress", progress);
		compound.setString("recipe", currentRecipe);
		return compound;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound compound) {
		super.handleUpdateTag(compound);
		progress = compound.getInteger("progress");
		currentRecipe = compound.getString("recipe");
	}
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound compound = super.getUpdateTag();
		compound.setInteger("progress", progress);
		compound.setString("recipe", currentRecipe);
		return compound;
	}
}