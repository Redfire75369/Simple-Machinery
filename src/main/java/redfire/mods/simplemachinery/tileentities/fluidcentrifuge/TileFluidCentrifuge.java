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
        super(0, 2, Config.fluidcentrifuge_input_tank_storage, Config.fluidcentrifuge_output_tank1_storage, Config.fluidcentrifuge_output_tank2_storage);
        tanks.get(0).setCanDrain(false);
        tanks.get(1).setCanFill(false);
        tanks.get(2).setCanFill(false);
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
                if (energy.getEnergyStored() >= RecipesTurntable.instance().getPower(currentRecipe)) {
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
        FluidStack fluidTank1;
        if (isNull(tanks.get(1).getFluid())) {
            fluidTank1 = null;
        } else {
            fluidTank1 = new FluidStack(tanks.get(1).getFluid(), tanks.get(1).getFluidAmount());
        }
        FluidStack fluidTank2;
        if (isNull(tanks.get(2).getFluid())) {
            fluidTank2 = null;
        } else {
            fluidTank2 = new FluidStack(tanks.get(2).getFluid(), tanks.get(2).getFluidAmount());
        }

        String recipe = RecipesFluidCentrifuge.searchRecipes(fluidInput);

        RecipesFluidCentrifuge instance = RecipesFluidCentrifuge.instance();

        List<ItemStack> outputs = instance.getOutputs(recipe);
        List<FluidStack> fluidOutputs = instance.getFluidOutputs(recipe);
        if ((!(outputs.get(0).isEmpty()) && (fluidOutputs.get(0) != null))) {
            FluidStack fluidUsed = instance.getFluidInput(recipe);
            if (insertOutputs(outputs, true)
                    && (fluidInput != null
                    && fluidInput.getFluid().getName() == fluidUsed.getFluid().getName()
                    && tanks.get(0).getFluidAmount() >= fluidUsed.amount)
                    && (fluidTank1 == null || (fluidTank1.getFluid().getName() == fluidOutputs.get(0).getFluid().getName()
                    && (tanks.get(1).getCapacity() - tanks.get(1).getFluidAmount() > fluidOutputs.get(0).amount)))
                    && (fluidTank2 == null || (fluidTank2.getFluid().getName() == fluidOutputs.get(1).getFluid().getName()
                    && (tanks.get(2).getCapacity() - tanks.get(2).getFluidAmount() > fluidOutputs.get(0).amount)))
                    && energy.getEnergyStored() >= instance.getTotalEnergy(recipe)) {
                currentRecipe = recipe;
                progress = instance.getTicks(recipe);
            } else {
                currentRecipe = "";
            }
        }
    }

    private void attemptFluidCentrifuge() {
        FluidStack fluidInput;
        if (isNull(tanks.get(0).getFluid())) {
            fluidInput = null;
        } else {
            fluidInput = new FluidStack(tanks.get(0).getFluid(), tanks.get(0).getFluidAmount());
        }
        FluidStack fluidTank1;
        if (isNull(tanks.get(1).getFluid())) {
            fluidTank1 = null;
        } else {
            fluidTank1 = new FluidStack(tanks.get(1).getFluid(), tanks.get(1).getFluidAmount());
        }
        FluidStack fluidTank2;
        if (isNull(tanks.get(2).getFluid())) {
            fluidTank2 = null;
        } else {
            fluidTank2 = new FluidStack(tanks.get(2).getFluid(), tanks.get(2).getFluidAmount());
        }

        RecipesFluidCentrifuge instance = RecipesFluidCentrifuge.instance();
        List<ItemStack> outputs = instance.getOutputs(currentRecipe);
        List<FluidStack> fluidOutputs = instance.getFluidOutputs(currentRecipe);
        if ((!(outputs.get(0).isEmpty()) && (fluidOutputs.get(0) != null))) {
            FluidStack fluidUsed = instance.getFluidInput(currentRecipe);
            if ((fluidInput != null
                    && fluidInput.getFluid().getName() == fluidUsed.getFluid().getName()
                    && tanks.get(0).getFluidAmount() >= fluidUsed.amount)
                    && (fluidTank1 == null || (fluidTank1.getFluid().getName() == fluidOutputs.get(0).getFluid().getName()
                    && (tanks.get(1).getCapacity() - tanks.get(1).getFluidAmount() > fluidOutputs.get(0).amount)))
                    && (fluidTank2 == null || (fluidTank2.getFluid().getName() == fluidOutputs.get(1).getFluid().getName()
                    && (tanks.get(2).getCapacity() - tanks.get(2).getFluidAmount() > fluidOutputs.get(0).amount)))
                    && insertOutputs(outputs, false)) {
                tanks.get(1).fillInternal(fluidOutputs.get(0), true);
                if (fluidOutputs.get(1) != null) {
                    tanks.get(2).fillInternal(fluidOutputs.get(1), true);
                }
                tanks.get(0).drainInternal(fluidUsed, true);
            }
            currentRecipe = "";
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
