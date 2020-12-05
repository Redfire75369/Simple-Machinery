package redfire.mods.simplemachinery.tileentities.fluidcentrifuge;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import redfire.mods.simplemachinery.Config;
import redfire.mods.simplemachinery.tileentities.generic.TileMachine;
import redfire.mods.simplemachinery.tileentities.turntable.RecipesTurntable;
import redfire.mods.simplemachinery.util.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static java.util.Objects.isNull;

public class TileFluidCentrifuge extends TileMachine implements ITickable {
    public EnergyStorage energy = new EnergyStorage(Config.fluidcentrifuge_fe_storage, Config.fluidcentrifuge_fe_io, 0,0);
    private int progress = 0;
    private String currentRecipe = "";

    public TileFluidCentrifuge() {
        super(0, 2, 16000, 8000, 8000);
    }

    public int getProgress() {
        return progress;
    }

    public String getCurrentRecipe() {
        return currentRecipe;
    }

    public EnergyStorage getEnergyStorage() {
        return energy;
    }

    @Override
    public void update() {
        if (!(world.isRemote)) {
            if (progress > 0) {
                if (energy.getEnergyStored() > RecipesTurntable.instance().getPower(currentRecipe)) {
                    progress--;
                    energy.subtractEnergy(RecipesTurntable.instance().getPower(currentRecipe));
                }
                if (progress <= 0) {
                    attemptFluidCentrifuge();
                }
            } else {
                startFluidCentrifuge();
            }
            notifyUpdate();
        }
    }

    private void startFluidCentrifuge() {
        FluidStack fluidInput;
        if (isNull(tanks.get(0).getFluid())) {
            fluidInput = null;
        } else {
            fluidInput = new FluidStack(tanks.get(0).getFluid(), tanks.get(0).getFluidAmount());
        }

        String recipe = RecipesFluidCentrifuge.searchRecipes(fluidInput);

        RecipesFluidCentrifuge instance = RecipesFluidCentrifuge.instance();

        List<ItemStack> outputs = instance.getOutputs(recipe);
        List<FluidStack> fluidOutputs = instance.getFluidOutputs(recipe);
        if ((!(outputs.get(0).isEmpty()) && (fluidOutputs.get(0) != null))) {
            FluidStack fluidUsed = instance.getFluidInput(recipe);
            if (insertOutputs(outputs, true)
                    && tanks.get(1).canDrainFluidType(fluidUsed)
                    && energy.getEnergyStored() >= instance.getTotalEnergy(recipe)) {
                currentRecipe = recipe;
                progress = instance.getTicks(recipe);
            } else {
                currentRecipe = "";
            }
        }
    }

    private void attemptFluidCentrifuge() {
        RecipesFluidCentrifuge instance = RecipesFluidCentrifuge.instance();
        List<ItemStack> outputs = instance.getOutputs(currentRecipe);
        List<FluidStack> fluidOutputs = instance.getFluidOutputs(currentRecipe);
        if ((!(outputs.get(0).isEmpty()) && (fluidOutputs.get(0) != null))) {
            FluidStack fluidUsed = instance.getFluidInput(currentRecipe);
            if (insertOutputs(outputs, false)
                    && tanks.get(0).canDrainFluidType(fluidUsed)) {
                tanks.get(0).drain(fluidUsed, true);
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
            } else {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputHandler);
            }
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.UP) {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tanks.get(0));
            } else if (tanks.get(1).getFluidAmount() != 0) {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tanks.get(1));
            } else {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tanks.get(2));
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
        energy.setEnergy(compound.getInteger("energyStored"));
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
        energy.setEnergy(compound.getInteger("energyStored"));
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
