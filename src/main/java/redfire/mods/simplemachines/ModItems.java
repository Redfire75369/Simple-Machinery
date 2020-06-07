package redfire.mods.simplemachines;

import net.minecraftforge.fml.common.registry.GameRegistry;
import redfire.mods.simplemachines.util.GenericItem;

public class ModItems {
	@GameRegistry.ObjectHolder("simplemachines:cell")
	public static GenericItem cell = new GenericItem("cell");
}
