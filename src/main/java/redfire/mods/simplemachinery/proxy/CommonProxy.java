package redfire.mods.simplemachinery.proxy;

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
import redfire.mods.simplemachinery.Config;
import redfire.mods.simplemachinery.ModBlocks;
import redfire.mods.simplemachinery.SimpleMachinery;
import redfire.mods.simplemachinery.tileentities.autoclave.BlockAutoclave;
import redfire.mods.simplemachinery.tileentities.autoclave.TileAutoclave;
import redfire.mods.simplemachinery.tileentities.fluidcentrifuge.BlockFluidCentrifuge;
import redfire.mods.simplemachinery.tileentities.fluidcentrifuge.TileFluidCentrifuge;
import redfire.mods.simplemachinery.tileentities.turntable.BlockTurntable;
import redfire.mods.simplemachinery.tileentities.turntable.TileTurntable;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy {
	public static Configuration config;

	public void preInit(FMLPreInitializationEvent e) {
		File directory = e.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "simplemachinery.cfg"));
		Config.readConfig();
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
		event.getRegistry().register(new BlockAutoclave());
		event.getRegistry().register(new BlockFluidCentrifuge());
		event.getRegistry().register(new BlockTurntable());
		GameRegistry.registerTileEntity(TileAutoclave.class, SimpleMachinery.modid + "_autoclave");
		GameRegistry.registerTileEntity(TileFluidCentrifuge.class, SimpleMachinery.modid + "_fluid_centrifuge");
		GameRegistry.registerTileEntity(TileTurntable.class, SimpleMachinery.modid + "_turntable");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new ItemBlock(ModBlocks.autoclave).setRegistryName(ModBlocks.autoclave.getRegistryName()));
		event.getRegistry().register(new ItemBlock(ModBlocks.fluid_centrifuge).setRegistryName(ModBlocks.fluid_centrifuge.getRegistryName()));
		event.getRegistry().register(new ItemBlock(ModBlocks.turntable).setRegistryName(ModBlocks.turntable.getRegistryName()));
	}
}
