package redfire.mods.simplemachinery.tileentities.turntable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import redfire.mods.simplemachinery.Config;
import redfire.mods.simplemachinery.util.GenericTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class TileTurntable extends GenericTileEntity implements ITickable {
	public static int size;
	public static EnergyStorage energy = new EnergyStorage(Config.turntable_fe_storage, Config.turntable_fe_usage, Config.turntable_fe_usage,0);
	private int progress = 0;

	public TileTurntable() {
		super(1, 1, 0, 0);
		size = input_slots + output_slots;
	}

	@Override
	public void update() {
		if (!(world.isRemote)) {
			if (progress > 0) {
				if (energy.getEnergyStored() > Config.turntable_fe_usage) {
					progress--;
					energy.extractEnergy(Config.turntable_fe_usage, false);
				}
				if (progress <= 0) {
					attemptTurntable();
				}
				markDirty();
			} else {
				startTurntable();
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

	private void startTurntable() {
		for (int i = 0; i < input_slots; i++) {
			ItemStack input =  inputHandler.getStackInSlot(i);
			ItemStack result = RecipesTurntable.instance().getTurntableOutput(input);
			if (!(result.isEmpty())) {
				if (insertOutput(result.copy(), true) && energy.getEnergyStored() >= RecipesTurntable.instance().getTurntableEnergyRequired(input)) {
					progress = (int) Math.ceil(RecipesTurntable.instance().getTurntableEnergyRequired(input) / Config.turntable_fe_usage);
					markDirty();
				}
				break;
			}
		}
	}

	private void attemptTurntable() {
		for (int i = 0; i < input_slots; i++) {
			ItemStack input =  inputHandler.getStackInSlot(i);
			ItemStack result = RecipesTurntable.instance().getTurntableOutput(input);
			if (!(result.isEmpty())) {
				if (insertOutput(result.copy(), false)) {
					inputHandler.extractItem(i, 1, false);
					markDirty();
				}
				break;
			}
		}
	}

	public void dropInventoryItems(World worldIn, BlockPos pos) {
		for (int i = 0, ii = size; i < ii; i++) {
			ItemStack stack = combinedHandler.getStackInSlot(i);
			if (!stack.isEmpty()) {
				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readInventory(compound);
		energy = new EnergyStorage(Config.turntable_fe_storage, Config.turntable_fe_usage, Config.turntable_fe_usage, compound.getInteger("energyStored"));
		progress = compound.getInteger("progress");
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeInventory(compound);
		compound.setInteger("energyStored", energy.getEnergyStored());
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
}
