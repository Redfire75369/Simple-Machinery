package redfire.mods.simplemachines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import redfire.mods.simplemachines.blocks.Regolith;
import redfire.mods.simplemachines.tileentities.autoclave.BlockAutoclave;

public class Blocks {
	@GameRegistry.ObjectHolder("simplemachines:autoclave")
	public static BlockAutoclave autoclave;

	@GameRegistry.ObjectHolder("simplemachines:regolith")
	public static Regolith regolith;

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		regolith.initModel();
		autoclave.initModel();
	}
}
