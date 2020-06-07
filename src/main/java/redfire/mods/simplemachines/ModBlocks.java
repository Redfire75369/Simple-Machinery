package redfire.mods.simplemachines;

import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import redfire.mods.simplemachines.tileentities.autoclave.BlockAutoclave;
import redfire.mods.simplemachines.util.GenericBlock;

public class ModBlocks {
	@GameRegistry.ObjectHolder("simplemachines:autoclave")
	public static BlockAutoclave autoclave;

	@GameRegistry.ObjectHolder("simplemachines:regolith")
	public static GenericBlock regolith = new GenericBlock("regolith", Material.SAND);

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		regolith.initModel();
		autoclave.initModel();
	}
}
