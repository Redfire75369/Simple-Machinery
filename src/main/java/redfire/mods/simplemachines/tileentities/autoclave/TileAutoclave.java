package redfire.mods.simplemachines.tileentities.autoclave;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class TileAutoclave extends TileEntity implements ITickable {
	public static final int input_slots = 2;
	public static final int output_slots = 1;
	public static final int size = input_slots + output_slots;

	private int progress = 0;
	private int max_progress = 40;

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
					markDirty();
				}
				break;
			}
		}
	}

	private ItemStackHandler inputHandler = new ItemStackHandler(input_slots) {
		@Override
		protected void onContentsChanged(int slot) {
			TileAutoclave.this.markDirty();
		}
	};

	private ItemStackHandler outputHandler = new ItemStackHandler(output_slots) {
		@Override
		protected void onContentsChanged(int slot) {
			TileAutoclave.this.markDirty();
		}
	};

	private CombinedInvWrapper combinedHandler = new CombinedInvWrapper(inputHandler, outputHandler);

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("itemsIn")) {
			inputHandler.deserializeNBT((NBTTagCompound) compound.getTag("itemsIn"));
		}
		if (compound.hasKey("itemsOut")) {
			outputHandler.deserializeNBT((NBTTagCompound) compound.getTag("itemsOut"));
		}
		progress = compound.getInteger("progress");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("itemsIn", inputHandler.serializeNBT());
		compound.setTag("itemsOut", outputHandler.serializeNBT());
		compound.setInteger("progress", progress);
		return compound;
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(combinedHandler);
			} else if (!(facing == EnumFacing.DOWN)) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputHandler);
			} else {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputHandler);
			}
		}
		return super.getCapability(capability, facing);
	}
}
