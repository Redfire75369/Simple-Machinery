package mods.redfire.simplemachinery.tileentities.turntable;

import mods.redfire.simplemachinery.registry.Containers;
import mods.redfire.simplemachinery.tileentities.machine.MachineContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TurntableContainer extends MachineContainer<TurntableTile> {
    public TurntableContainer(int id, World world, BlockPos pos, PlayerInventory playerInv) {
        super(Containers.CONTAINER_TURNTABLE.get(), id, world, pos, playerInv);
    }

    public TurntableContainer(int id, World world, BlockPos pos, PlayerInventory playerInv, TurntableTile tile) {
        super(Containers.CONTAINER_TURNTABLE.get(), id, world, pos, playerInv, tile);
    }

    @Override
    protected void layoutMachineInventorySlots(IItemHandler inv) {
        addSlot(new SlotItemHandler(inv, 0, 53, 35));
        addSlot(new SlotItemHandler(inv, 1, 53, 35));
    }
}
