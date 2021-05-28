package mods.redfire.simplemachinery.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Config {
	public static final String CATEGORY_GENERAL = "General";

	public static ForgeConfigSpec SERVER_CONFIG;
	public static ForgeConfigSpec CLIENT_CONFIG;

	static {
		ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
		ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

		CLIENT_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);

		SERVER_CONFIG = SERVER_BUILDER.build();
		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {}

	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading configEvent) {}
}
