package redfire.mods.simplemachinery.tileentities.autoclave;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.items.CapabilityItemHandler;
import redfire.mods.simplemachinery.util.GenericTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Objects.isNull;


public class TileAutoclave extends GenericTileEntity implements ITickable {
	public static int size;
	private int progress = 0;

	public TileAutoclave() {
		super(2, 1, 4000, 4000);
		size = input_slots + output_slots;
	}

	@Override
	public void update() {
		if (!(world.isRemote)) {
			if (progress > 0) {
				progress--;
				for (int i = 0; i < input_slots; i++) {
					if (!inputHandler.getStackInSlot(i).isEmpty()) {
						tanks.get(0).drain(AutoclaveRecipes.instance().getAutoclaveSteamUsage(inputHandler.getStackInSlot(i), new FluidStack(tanks.get(1).getFluid(), tanks.get(1).getFluidAmount())),true);
						break;
					}
				}

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
			ItemStack input =  inputHandler.getStackInSlot(i);
			FluidStack tank;
			AutoclaveRecipes recipes = new AutoclaveRecipes();
			if (isNull(tanks.get(1).getFluid())) {
				tank = null;
			} else {
				tank = new FluidStack(tanks.get(1).getFluid(), tanks.get(1).getFluidAmount());
			}
			ItemStack result = recipes.getAutoclaveOutput(input, tank);
			if (!(result.isEmpty())) {
				FluidStack fluidUsed = AutoclaveRecipes.instance().getAutoclaveFluidInput(input, tank);
				if (insertOutput(result.copy(), true) && tanks.get(1).canDrainFluidType(fluidUsed) && recipes.getAutoclaveSteamRequired(input, tank) < tanks.get(0).getFluidAmount()) {
					progress = recipes.getAutoclaveTicks(input, tank);
					markDirty();
				}
				break;
			}
		}
	}

	private void attemptAutoclave() {
		for (int i = 0; i < input_slots; i++) {
			ItemStack input =  inputHandler.getStackInSlot(i);
			FluidStack tank = new FluidStack(tanks.get(1).getFluid(), tanks.get(1).getFluidAmount());
			ItemStack result = AutoclaveRecipes.instance().getAutoclaveOutput(input, tank);
			if (!(result.isEmpty())) {
				FluidStack fluidUsed = AutoclaveRecipes.instance().getAutoclaveFluidInput(input, tank);
				if (insertOutput(result.copy(), false) && tanks.get(1).canDrainFluidType(fluidUsed)) {
					inputHandler.extractItem(i, 1, false);
					tanks.get(1).drain(fluidUsed, true);
					markDirty();
				}
				break;
			}
		}
	}

	public void dropInventoryItems(World worldIn, BlockPos pos) {
		for (int i = 0, ii = size; i < ii; i++) {
			ItemStack stack = combinedHandler.getStackInSlot(i);
			FMLLog.log.info("{}", stack);
			if (!stack.isEmpty()) {
				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
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
			if (facing == EnumFacing.UP) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tanks.get(0));
			} else {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tanks.get(1));
			}
		}
		return super.getCapability(capability, facing);
	}
}