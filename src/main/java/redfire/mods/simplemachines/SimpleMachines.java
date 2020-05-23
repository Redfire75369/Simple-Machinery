package redfire.mods.simplemachines;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import redfire.mods.simplemachines.proxy.CommonProxy;

@Mod(modid = SimpleMachines.modid, name = SimpleMachines.modname, version = SimpleMachines.version, dependencies = "required-after:forge@[11.16.0.1865,)", useMetadata = true)
public class SimpleMachines {

	public static final String modid = "simplemachines";
	public static final String modname = "Simple Machines";
	public static final String version = "0.0.1";

	@SidedProxy(clientSide = "redfire.mods.simplemachines.proxy.ClientProxy", serverSide = "redfire.mods.simplemachines.proxy.ServerProxy")
	public static CommonProxy proxy;

	public static final CreativeTabs creativeTab = new CreativeTabs("Simple Machines") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Blocks.autoclave);
		}
	};

	@Mod.Instance
	public static SimpleMachines instance;

	public static Logger logger;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
	    proxy.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
	    proxy.postInit(e);
	}
}