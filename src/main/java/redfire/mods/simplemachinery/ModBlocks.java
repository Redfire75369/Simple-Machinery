package redfire.mods.simplemachinery;

import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import redfire.mods.simplemachinery.tileentities.autoclave.BlockAutoclave;
import redfire.mods.simplemachinery.tileentities.turntable.BlockTurntable;
import redfire.mods.simplemachinery.util.GenericBlock;

public class ModBlocks {
	@GameRegistry.ObjectHolder("simplemachinery:autoclave")
	public static BlockAutoclave autoclave;

	@GameRegistry.ObjectHolder("simplemachinery:turntable")
	public static BlockTurntable turntable;

	@GameRegistry.ObjectHolder("simplemachinery:regolith")
	public static GenericBlock regolith = new GenericBlock("regolith", Material.SAND);

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		regolith.initModel();
		autoclave.initModel();
		turntable.initModel();
	}
}
