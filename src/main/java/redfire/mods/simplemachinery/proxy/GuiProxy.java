package redfire.mods.simplemachinery.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import redfire.mods.simplemachinery.tileentities.autoclave.ContainerAutoclave;
import redfire.mods.simplemachinery.tileentities.autoclave.GuiAutoclave;
import redfire.mods.simplemachinery.tileentities.autoclave.TileAutoclave;
import redfire.mods.simplemachinery.tileentities.turntable.ContainerTurntable;
import redfire.mods.simplemachinery.tileentities.turntable.GuiTurntable;
import redfire.mods.simplemachinery.tileentities.turntable.TileTurntable;

public class GuiProxy implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileAutoclave) {
			return new ContainerAutoclave(player.inventory, (TileAutoclave) te);
		} else if (te instanceof TileTurntable) {
			return new ContainerTurntable(player.inventory, (TileTurntable) te);
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
		else if (te instanceof TileTurntable) {
			TileTurntable containerTileEntity = (TileTurntable) te;
			return new GuiTurntable(containerTileEntity, new ContainerTurntable(player.inventory, containerTileEntity));
		}
		return null;
	}
}
