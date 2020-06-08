package redfire.mods.simplemachines.util;

import net.minecraft.item.Item;
import redfire.mods.simplemachines.SimpleMachinery;

public class GenericItem extends Item {
	public GenericItem(String registryName) {
		setRegistryName(registryName);
		setUnlocalizedName(SimpleMachinery.modid + "." + registryName);
		setCreativeTab(SimpleMachinery.creativeTab);
	}
}
