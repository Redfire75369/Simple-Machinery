package redfire.mods.simplemachinery.util;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import redfire.mods.simplemachinery.SimpleMachinery;

public class GenericBlock extends Block {
	public GenericBlock(String registryName, Material materialIn) {
		super(materialIn);
		setRegistryName(registryName);
		setUnlocalizedName(SimpleMachinery.modid + "." + registryName);
		setCreativeTab(SimpleMachinery.creativeTab);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
