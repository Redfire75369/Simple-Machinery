package redfire.mods.simplemachines.util;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import redfire.mods.simplemachines.SimpleMachines;

public class GenericBlock extends Block {
	public GenericBlock(Material materialIn) {
		super(materialIn);
		setCreativeTab(SimpleMachines.creativeTab);
	}
}
