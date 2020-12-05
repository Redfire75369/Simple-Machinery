package redfire.mods.simplemachinery;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import redfire.mods.simplemachinery.tileentities.autoclave.BlockAutoclave;
import redfire.mods.simplemachinery.tileentities.fluidcentrifuge.BlockFluidCentrifuge;
import redfire.mods.simplemachinery.tileentities.turntable.BlockTurntable;

public class ModBlocks {
	@GameRegistry.ObjectHolder("simplemachinery:autoclave")
	public static BlockAutoclave autoclave;

	@GameRegistry.ObjectHolder("simplemachinery:fluid_centrifuge")
	public static BlockFluidCentrifuge fluid_centrifuge;

	@GameRegistry.ObjectHolder("simplemachinery:turntable")
	public static BlockTurntable turntable;


	@SideOnly(Side.CLIENT)
	public static void initModels() {
		autoclave.initModel();
		fluid_centrifuge.initModel();
		turntable.initModel();
	}
}
