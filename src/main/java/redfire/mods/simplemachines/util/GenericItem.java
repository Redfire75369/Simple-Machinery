package redfire.mods.simplemachines.util;

import net.minecraft.item.Item;
import redfire.mods.simplemachines.SimpleMachines;

public class GenericItem extends Item {
	public GenericItem(String registryName) {
		setRegistryName(registryName);
		setUnlocalizedName(SimpleMachines.modid + "." + registryName);
		setCreativeTab(SimpleMachines.creativeTab);
	}
}
