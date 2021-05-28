package mods.redfire.simplemachinery.tileentities.turntable;

import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.registry.Containers;
import mods.redfire.simplemachinery.tileentities.machine.MachineContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static mods.redfire.simplemachinery.tileentities.turntable.TurntableTile.ITEM_INPUTS;
import static mods.redfire.simplemachinery.tileentities.turntable.TurntableTile.ITEM_OUTPUTS;

public class TurntableContainer extends MachineContainer<TurntableTile> {
	public static final int INVENTORY_SIZE = ITEM_INPUTS + ITEM_OUTPUTS;

	public TurntableContainer(int id, World world, BlockPos pos, PlayerInventory playerInv) {
		super(Containers.TURNTABLE_CONTAINER.get(), INVENTORY_SIZE, id, world, pos, playerInv);
	}

	public TurntableContainer(int id, World world, BlockPos pos, PlayerInventory playerInv, TurntableTile tile) {
		super(Containers.TURNTABLE_CONTAINER.get(), INVENTORY_SIZE, id, world, pos, playerInv, tile);
	}

	@Override
	protected void layoutMachineInventorySlots(IItemHandler inv) {
		addSlot(new SlotItemHandler(inv, 0, 53, 26));
		addSlot(new SlotItemHandler(inv, 1, 107, 26));
	}

	@Override
	public boolean stillValid(@Nonnull PlayerEntity player) {
		return stillValid(IWorldPosCallable.create(world, pos), player, Blocks.TURNTABLE_BLOCK.get());
	}
}
