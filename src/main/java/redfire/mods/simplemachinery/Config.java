package redfire.mods.simplemachinery;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import redfire.mods.simplemachinery.proxy.CommonProxy;


public class Config {
	private static final String category_machine = "Machine";

	public static int autoclave_steam_storage;
	public static int autoclave_tank_storage;

	public static int fluidcentrifuge_fe_storage;
	public static int fluidcentrifuge_fe_io;
	public static int fluidcentrifuge_input_tank_storage;
	public static int fluidcentrifuge_output_tank1_storage;
	public static int fluidcentrifuge_output_tank2_storage;

	public static int turntable_fe_storage;
	public static int turntable_fe_io;

	public static void readConfig() {
		Configuration cfg = CommonProxy.config;

		try {
			cfg.load();
			initMachineConfig(cfg);
		} catch (Exception e1) {
			SimpleMachinery.logger.log(Level.ERROR, "Problem loading config file!", e1);
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}

	public static void initMachineConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(category_machine, "Machine Configuration");

		autoclave_steam_storage = cfg.getInt("autoclave_steam_storage", category_machine, 4000, 1, Integer.MAX_VALUE, "This is the amount of Steam (in mB) that the Autoclave can store in its steam tank.");
		autoclave_tank_storage = cfg.getInt("autoclave_tank_storage", category_machine, 4000, 1, Integer.MAX_VALUE, "This is the amount of fluid (in mB) that the Autoclave can store in its secondary tank.");

		fluidcentrifuge_fe_storage = cfg.getInt("fluidcentrifuge_fe_storage", category_machine, 96000, 1, Integer.MAX_VALUE, "This is the amount of Forge Energy that the Fluid Centrifuge can store in its buffer.");
		fluidcentrifuge_fe_io = cfg.getInt("fluidcentrifuge_fe_io", category_machine, 480, 1, Integer.MAX_VALUE, "This is the amount of Forge Energy that the Fluid Centrifuge can store in its buffer.");
		fluidcentrifuge_input_tank_storage = cfg.getInt("fluidcentrifuge_input_tank_storage", category_machine, 16000, 1, Integer.MAX_VALUE, "This is the amount of fluid (in mB) that the Fluid Centrifuge can store in its input tank.");
		fluidcentrifuge_output_tank1_storage = cfg.getInt("fluidcentrifuge_output_tank1_storage", category_machine, 8000, 1, Integer.MAX_VALUE, "This is the amount of fluid (in mB) that the Fluid Centrifuge can store in its first output tank.");
		fluidcentrifuge_output_tank2_storage = cfg.getInt("fluidcentrifuge_output_tank2_storage", category_machine, 8000, 1, Integer.MAX_VALUE, "This is the amount of fluid (in mB) that the Fluid Centrifuge can store in its second output tank.");

		turntable_fe_storage = cfg.getInt("turntable_fe_storage", category_machine, 48000, 1, Integer.MAX_VALUE, "This is the amount of Forge Energy that the Turntable can store in its buffer.");
		turntable_fe_io = cfg.getInt("turntable_fe_io", category_machine, 240, 1, Integer.MAX_VALUE, "This is the amount of Forge Energy that the Turntable can store in its buffer.");
	}
}
