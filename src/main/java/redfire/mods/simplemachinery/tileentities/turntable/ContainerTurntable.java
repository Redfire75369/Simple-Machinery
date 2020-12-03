package redfire.mods.simplemachinery.tileentities.turntable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import redfire.mods.simplemachinery.tileentities.generic.ContainerMachine;

public class ContainerTurntable extends ContainerMachine<TileTurntable> {
	public ContainerTurntable(IInventory playerInventory, TileTurntable tileTurntable) {
		super(playerInventory, tileTurntable);
	}

	@Override
	protected void addPlayerSlots(IInventory playerInventory) {
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				int x = 8 + col * 18;
				int y = row * 18 + 84;
				addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
			}
		}

		for (int row = 0; row < 9; ++row) {
			int x = 8 + row * 18;
			int y = 58 + 84;
			addSlotToContainer(new Slot(playerInventory, row, x, y));
		}
	}

	@Override
	protected void addOwnSlots() {
		IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int x = 53;
		int y = 35;

		int slotIndex = 0;
		addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex++, x, y));
		x = 107;
		addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
	}
}