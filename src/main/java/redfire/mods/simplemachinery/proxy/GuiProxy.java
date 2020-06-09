package redfire.mods.simplemachines.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import redfire.mods.simplemachines.tileentities.autoclave.ContainerAutoclave;
import redfire.mods.simplemachines.tileentities.autoclave.GuiAutoclave;
import redfire.mods.simplemachines.tileentities.autoclave.TileAutoclave;

public class GuiProxy implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileAutoclave) {
			return new ContainerAutoclave(player.inventory, (TileAutoclave) te);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileAutoclave) {
			TileAutoclave containerTileEntity = (TileAutoclave) te;
			return new GuiAutoclave(containerTileEntity, new ContainerAutoclave(player.inventory, containerTileEntity));
		}
		return null;
	}
}
