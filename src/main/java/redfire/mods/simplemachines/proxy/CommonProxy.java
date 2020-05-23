package redfire.mods.simplemachines.proxy;

import net.minecraft.block.Block;
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
import redfire.mods.simplemachines.Blocks;
import redfire.mods.simplemachines.Config;
import redfire.mods.simplemachines.SimpleMachines;
import redfire.mods.simplemachines.blocks.Regolith;
import redfire.mods.simplemachines.tileentities.autoclave.BlockAutoclave;
import redfire.mods.simplemachines.tileentities.autoclave.TileAutoclave;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy {
	public static Configuration config;

	public void preInit(FMLPreInitializationEvent e) {
		File directory = e.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "simplemachines.cfg"));
		Config.readConfig();
	}

	public void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(SimpleMachines.instance, new GuiProxy());
	}

	public void postInit(FMLPostInitializationEvent e) {
		if (config.hasChanged()) {
			config.save();
		}
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new Regolith());
		event.getRegistry().register(new BlockAutoclave());
		GameRegistry.registerTileEntity(TileAutoclave.class, SimpleMachines.modid + "_autoclave");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new ItemBlock(Blocks.regolith).setRegistryName(Blocks.regolith.getRegistryName()));
		event.getRegistry().register(new ItemBlock(Blocks.autoclave).setRegistryName(Blocks.autoclave.getRegistryName()));
	}
}
