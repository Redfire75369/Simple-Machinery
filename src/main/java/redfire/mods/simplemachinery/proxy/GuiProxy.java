package redfire.mods.simplemachinery.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import redfire.mods.simplemachinery.tileentities.autoclave.ContainerAutoclave;
import redfire.mods.simplemachinery.tileentities.autoclave.GuiAutoclave;
import redfire.mods.simplemachinery.tileentities.autoclave.TileAutoclave;
import redfire.mods.simplemachinery.tileentities.fluidcentrifuge.ContainerFluidCentrifuge;
import redfire.mods.simplemachinery.tileentities.fluidcentrifuge.GuiFluidCentrifuge;
import redfire.mods.simplemachinery.tileentities.fluidcentrifuge.TileFluidCentrifuge;
import redfire.mods.simplemachinery.tileentities.turntable.ContainerTurntable;
import redfire.mods.simplemachinery.tileentities.turntable.GuiTurntable;
import redfire.mods.simplemachinery.tileentities.turntable.TileTurntable;

public class GuiProxy implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileAutoclave) {
			return new ContainerAutoclave(player.inventory, (TileAutoclave) tileEntity);
		} else if (tileEntity instanceof TileFluidCentrifuge) {
			return new ContainerFluidCentrifuge(player.inventory, (TileFluidCentrifuge) tileEntity);
		} else if (tileEntity instanceof TileTurntable) {
			return new ContainerTurntable(player.inventory, (TileTurntable) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileAutoclave) {
			TileAutoclave containerTileEntity = (TileAutoclave) tileEntity;
			return new GuiAutoclave(containerTileEntity, new ContainerAutoclave(player.inventory, containerTileEntity));
		} else if (tileEntity instanceof TileFluidCentrifuge) {
			TileFluidCentrifuge containerTileEntity = (TileFluidCentrifuge) tileEntity;
			return new GuiFluidCentrifuge(containerTileEntity, new ContainerFluidCentrifuge(player.inventory, containerTileEntity));
		} else if (tileEntity instanceof TileTurntable) {
			TileTurntable containerTileEntity = (TileTurntable) tileEntity;
			return new GuiTurntable(containerTileEntity, new ContainerTurntable(player.inventory, containerTileEntity));
		}
		return null;
	}
}
