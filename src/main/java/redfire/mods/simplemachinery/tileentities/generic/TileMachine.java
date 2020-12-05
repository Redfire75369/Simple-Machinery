package redfire.mods.simplemachinery.tileentities.generic;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import redfire.mods.simplemachinery.util.CombinedTank;
import redfire.mods.simplemachinery.util.Tank;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileMachine extends TileEntity {
	public static int size;

	public int input_slots;
	public int output_slots;

	public IntList fluidStorage = new IntArrayList();

	protected List<Tank> tanks = new ArrayList<>();
	protected CombinedTank fluidHandler;

	protected ItemStackHandler inputHandler;
	protected ItemStackHandler outputHandler;
	protected CombinedInvWrapper combinedHandler;

	public TileMachine(int input, int output, int ...fluids) {
		input_slots = input;
		output_slots = output;
		size = input_slots + output_slots;

		for (int i = 0; i < fluids.length; i++) {
			fluidStorage.add(i, fluids[i]);
			tanks.add(i, new Tank(fluids[i]));
		}

		inputHandler = new ItemStackHandler(input_slots) {
			@Override
			protected void onContentsChanged(int slot) {
				TileMachine.this.notifyUpdate();
			}
		};
		outputHandler = new ItemStackHandler(output_slots) {
			@Override
			protected void onContentsChanged(int slot) {
				TileMachine.this.notifyUpdate();
			}
		};

		combinedHandler = new CombinedInvWrapper(inputHandler, outputHandler);
	}

	public List<Tank> getTanks() {
		return tanks;
	}

	protected boolean insertOutput(ItemStack output, boolean simulate) {
		for (int i = 0; i < output_slots; i++) {
			ItemStack remaining = outputHandler.insertItem(i, output, simulate);
			if (remaining.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	protected boolean insertOutputs(List<ItemStack> outputs, boolean simulate) {
		int index = 0;
		for (int i = 0; i < output_slots; i++) {
			ItemStack output = outputs.get(index);
			if (!simulate) {
				output = output.copy();
			}
			ItemStack remaining = outputHandler.insertItem(i, output, simulate);
			if (remaining.isEmpty()) {
				index++;
				if (index == outputs.size()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	public void dropInventoryItems(World worldIn, BlockPos pos) {
		for (int i = 0, ii = size; i < ii; i++) {
			ItemStack stack = combinedHandler.getStackInSlot(i);
			if (!stack.isEmpty()) {
				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
			}
		}
	}

	public void readInventory(NBTTagCompound compound) {
		if (compound.hasKey("itemsIn") && input_slots > 0) {
			inputHandler.deserializeNBT((NBTTagCompound) compound.getTag("itemsIn"));
		}
		if (compound.hasKey("itemsOut") && output_slots > 0) {
			outputHandler.deserializeNBT((NBTTagCompound) compound.getTag("itemsOut"));
		}
	}
	public NBTTagCompound writeInventory(NBTTagCompound compound) {
		if (input_slots > 0) {
			compound.setTag("itemsIn", inputHandler.serializeNBT());
		}
		if (output_slots > 0) {
			compound.setTag("itemsOut", outputHandler.serializeNBT());
		}
		return compound;
	}

	public void readTanks(NBTTagCompound nbt) {
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).readFromNBT(nbt, "tanks" + i);
		}
	}
	public NBTTagCompound writeTanks(NBTTagCompound nbt) {
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).writeToNBT(nbt, "tanks" + i);
		}
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		readInventory(compound);
		readTanks(compound);
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound = writeInventory(compound);
		compound = writeTanks(compound);
		return compound;
	}


	@Override
	public void handleUpdateTag(NBTTagCompound compound) {
		super.handleUpdateTag(compound);
		readInventory(compound);
		readTanks(compound);
	}
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound compound  = super.getUpdateTag();
		compound = writeInventory(compound);
		compound = writeTanks(compound);
		return compound;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, -1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		handleUpdateTag(pkt.getNbtCompound());
	}

	protected void notifyUpdate() {
		markDirty();
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), Constants.BlockFlags.DEFAULT);
	}
}
