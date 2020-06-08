package redfire.mods.simplemachines.tileentities.autoclave;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import redfire.mods.simplemachines.util.GenericTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileAutoclave extends GenericTileEntity implements ITickable {
	public static int size;
	private int progress = 0;
	private int max_progress = 40;

	public TileAutoclave() {
		super(2, 1, 4000, 4000);
		size = input_slots + output_slots;
	}

	@Override
	public void update() {
		if (!(world.isRemote)) {
			if (progress > 0) {
				progress--;
				if (progress <= 0) {
					attemptAutoclave();
				}
				markDirty();
			} else {
				startAutoclave();
			}
		}
	}

	private boolean insertOutput(ItemStack output, boolean simulate) {
		for (int i = 0; i < output_slots; i++) {
			ItemStack remaining = outputHandler.insertItem(i, output, simulate);
			if (remaining.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private void startAutoclave() {
		for (int i = 0; i < input_slots; i++) {
			ItemStack result = AutoclaveRecipes.instance().getAutoclaveOutput(inputHandler.getStackInSlot(i));
			if (!(result.isEmpty())) {
				if (insertOutput(result.copy(), true)) {
					progress = max_progress;
					markDirty();
				}
				break;
			}
		}
	}

	private void attemptAutoclave() {
		for (int i = 0; i < input_slots; i++) {
			ItemStack result = AutoclaveRecipes.instance().getAutoclaveOutput(inputHandler.getStackInSlot(i));
			if (!(result.isEmpty())) {
				if (insertOutput(result.copy(), false)) {
					inputHandler.extractItem(i, 1, false);
					tanks.get(1).drain(AutoclaveRecipes.instance().getAutoclaveFluidInput(inputHandler.getStackInSlot(i)), true);
					markDirty();
				}
				break;
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readInventory(compound);
		super.readInventory(compound);
		super.readTanks(compound);
		progress = compound.getInteger("progress");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeInventory(compound);
		super.writeTanks(compound);
		compound.setInteger("progress", progress);
		return compound;
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
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
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tanks.get(0));
		}
		return super.getCapability(capability, facing);
	}
}