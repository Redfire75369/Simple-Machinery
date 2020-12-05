package redfire.mods.simplemachinery.tileentities.generic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class ContainerMachine<TE extends TileMachine> extends Container {
    protected final TE tileEntity;

    public ContainerMachine(IInventory playerInventory, TE tileEntity) {
        this.tileEntity = tileEntity;
        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    protected void addPlayerSlots(IInventory playerInventory) {}
    protected void addOwnSlots() {}

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.canInteractWith(playerIn);
    }
}
