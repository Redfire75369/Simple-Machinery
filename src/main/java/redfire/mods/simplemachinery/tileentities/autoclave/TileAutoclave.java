package redfire.mods.simplemachinery.tileentities.autoclave;

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
	private int progress = 0;
	private String currentRecipe = "";

	public TileAutoclave() {
		super(2, 1, Config.autoclave_steam_storage, Config.autoclave_tank_storage);
		tanks.get(0).setCanDrain(false);
		tanks.get(1).setCanDrain(false);
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
				if (tanks.get(0).getFluidAmount() > RecipesAutoclave.instance().getSteamPower(currentRecipe)) {
					progress--;
					tanks.get(0).drainInternal(RecipesAutoclave.instance().getSteamPower(currentRecipe),true);
				}
				int emptySlots = 0;
				for (int i = 0; i < input_slots; i++) {
					if (!RecipesAutoclave.instance().getInput(currentRecipe).apply(inputHandler.getStackInSlot(i))) {
						emptySlots++;
					}
				}
				if (emptySlots == input_slots) {
					currentRecipe = "";
					progress = 0;
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
		FluidStack fluidInput;
		if (isNull(tanks.get(1).getFluid())) {
			fluidInput = null;
		} else {
			fluidInput = new FluidStack(tanks.get(1).getFluid(), tanks.get(1).getFluidAmount());
		}
		RecipesAutoclave instance = RecipesAutoclave.instance();
		for (int i = 0; i < input_slots; i++) {
			ItemStack input =  inputHandler.getStackInSlot(i);
			String recipe = RecipesAutoclave.searchRecipes(input, fluidInput);

			ItemStack output = instance.getOutput(recipe);
			if (!(output.isEmpty())) {
				FluidStack fluidUsed = instance.getFluidInput(recipe);
				if (insertOutput(output.copy(), true)
						&& fluidInput != null
						&& fluidInput.getFluid().getName() == fluidUsed.getFluid().getName()
						&& tanks.get(1).getFluidAmount() >= fluidUsed.amount
						&& tanks.get(0).getFluidAmount() >= instance.getTotalSteam(recipe)) {
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
		FluidStack fluidInput;
		if (tanks.get(1).getFluid() == null) {
			fluidInput = null;
		} else {
			fluidInput = new FluidStack(tanks.get(1).getFluid(), tanks.get(1).getFluidAmount());
		}

		RecipesAutoclave instance = RecipesAutoclave.instance();
		ItemStack output = instance.getOutput(currentRecipe);

		for (int i = 0; i < input_slots; i++) {
			if (!(output.isEmpty())) {
				FluidStack fluidUsed = instance.getFluidInput(currentRecipe);
				if (fluidInput != null
						&& fluidInput.getFluid().getName() == fluidUsed.getFluid().getName()
						&& tanks.get(1).getFluidAmount() >= fluidUsed.amount
						&& insertOutput(output.copy(), false)) {
					inputHandler.extractItem(i, 1, false);
					tanks.get(1).drainInternal(fluidUsed, true);
				}
				currentRecipe = "";
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