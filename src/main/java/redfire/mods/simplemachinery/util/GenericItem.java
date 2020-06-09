package redfire.mods.simplemachinery.util;

import net.minecraft.item.Item;
import redfire.mods.simplemachinery.SimpleMachinery;

public class GenericItem extends Item {
	public GenericItem(String registryName) {
		setRegistryName(registryName);
		setUnlocalizedName(SimpleMachinery.modid + "." + registryName);
		setCreativeTab(SimpleMachinery.creativeTab);
	}
}
