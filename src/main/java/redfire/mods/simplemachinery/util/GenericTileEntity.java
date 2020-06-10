package redfire.mods.simplemachinery.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.List;

public class GenericTileEntity extends TileEntity {
	public int input_slots;
	public int output_slots;


	public IntList fluidStorage = new IntArrayList(2);

	protected List<Tank> tanks = new ArrayList<>(2);
	protected CombinedTank fluidHandler;

	protected ItemStackHandler inputHandler;
	protected ItemStackHandler outputHandler;
	protected CombinedInvWrapper combinedHandler;

	public GenericTileEntity(int input, int output, int fluid1, int fluid2) {
		this.input_slots = input;
		this.output_slots = output;
		this.fluidStorage.add(0, fluid1);
		this.fluidStorage.add(1, fluid2);

		this.inputHandler = new ItemStackHandler(input_slots) {
			@Override
			protected void onContentsChanged(int slot) {
				GenericTileEntity.this.markDirty();
			}
		};
		this.outputHandler = new ItemStackHandler(output_slots) {
			@Override
			protected void onContentsChanged(int slot) {
				GenericTileEntity.this.markDirty();
			}
		};

		this.combinedHandler = new CombinedInvWrapper(inputHandler, outputHandler);
		this.tanks.add(0, new Tank(fluid1));
		this.tanks.add(1, new Tank(fluid2));
	}

	public NBTTagCompound writeInventory(NBTTagCompound nbt) {
		nbt.setTag("itemsIn", inputHandler.serializeNBT());
		nbt.setTag("itemsOut", outputHandler.serializeNBT());
		return nbt;
	}
	public void readInventory(NBTTagCompound nbt) {
		if (nbt.hasKey("itemsIn")) {
			inputHandler.deserializeNBT((NBTTagCompound) nbt.getTag("itemsIn"));
		}
		if (nbt.hasKey("itemsOut")) {
			outputHandler.deserializeNBT((NBTTagCompound) nbt.getTag("itemsOut"));
		}
	}

	public NBTTagCompound writeTanks(NBTTagCompound nbt) {
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).writeToNBT(nbt, "tanks" + i);
		}
		return nbt;
	}
	public void readTanks(NBTTagCompound nbt) {
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).readFromNBT(nbt, "tanks" + i);
		}
	}
}
