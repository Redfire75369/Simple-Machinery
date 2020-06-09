package redfire.mods.simplemachinery.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import redfire.mods.simplemachinery.*;
import redfire.mods.simplemachinery.tileentities.autoclave.BlockAutoclave;
import redfire.mods.simplemachinery.tileentities.autoclave.TileAutoclave;
import redfire.mods.simplemachinery.util.GenericBlock;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy {
	public static Configuration config;

	public void preInit(FMLPreInitializationEvent e) {
		File directory = e.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "simplemachinery.cfg"));
		Config.readConfig();

		ModFluids.init();
	}

	public void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(SimpleMachinery.instance, new GuiProxy());
	}

	public void postInit(FMLPostInitializationEvent e) {
		if (config.hasChanged()) {
			config.save();
		}
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new GenericBlock("regolith", Material.SAND));
		event.getRegistry().register(new BlockAutoclave());
		GameRegistry.registerTileEntity(TileAutoclave.class, SimpleMachinery.modid + "_autoclave");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		//event.getRegistry().register(ModItems.cell);

		event.getRegistry().register(new ItemBlock(ModBlocks.regolith).setRegistryName(ModBlocks.regolith.getRegistryName()));
		event.getRegistry().register(new ItemBlock(ModBlocks.autoclave).setRegistryName(ModBlocks.autoclave.getRegistryName()));
	}
}
