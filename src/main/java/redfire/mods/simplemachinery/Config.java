package redfire.mods.simplemachinery;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import redfire.mods.simplemachinery.proxy.CommonProxy;


public class Config {
	private static final String category_general = "General";
	private static final String category_other = "other";

	public static boolean testVar = true;
	public static String realName = "Steve";

	public static void readConfig() {
		Configuration cfg = CommonProxy.config;

		try {
			cfg.load();
			initGeneralConfig(cfg);
		} catch (Exception e1) {
			SimpleMachinery.logger.log(Level.ERROR, "Problem loading config file!", e1);
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}

	public static void initGeneralConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(category_general, "General Configuration");

		testVar = cfg.getBoolean("testVar", category_general, testVar, "Set to false for testing");
		realName = cfg.getString("realName", category_general, realName, "Set your real name here");
	}
}
