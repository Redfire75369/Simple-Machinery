/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Config {
	public static final String CATEGORY_MACHINE = "Machines";
	public static final String SUBCATEGORY_AUTOCLAVE = "Autoclave";
	public static final String SUBCATEGORY_FLUID_CENTRIFUGE = "Fluid Centrifuge";
	public static final String SUBCATEGORY_SIEVE = "Sieve";
	public static final String SUBCATEGORY_TURNTABLE = "Turntable";

	public static ForgeConfigSpec SERVER_CONFIG;

	// Autoclave
	public static ForgeConfigSpec.IntValue AUTOCLAVE_STEAM_CAPACITY;
	public static ForgeConfigSpec.IntValue AUTOCLAVE_TANK_CAPACITY;

	// Fluid Centrifuge
	public static ForgeConfigSpec.IntValue FLUID_CENTRIFUGE_COIL_CAPACITY;
	public static ForgeConfigSpec.IntValue FLUID_CENTRIFUGE_COIL_IO;
	public static ForgeConfigSpec.IntValue FLUID_CENTRIFUGE_INPUT_TANK_CAPACITY;
	public static ForgeConfigSpec.IntValue FLUID_CENTRIFUGE_OUTPUT_TANKS_CAPACITY;

	// Sieve
	public static ForgeConfigSpec.IntValue SIEVE_COIL_CAPACITY;
	public static ForgeConfigSpec.IntValue SIEVE_COIL_IO;

	// Sieve
	public static ForgeConfigSpec.IntValue TURNTABLE_COIL_CAPACITY;
	public static ForgeConfigSpec.IntValue TURNTABLE_COIL_IO;

	static {
		ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

		SERVER_BUILDER.comment("Machine Settings").push(CATEGORY_MACHINE);

		setupAutoclaveConfig(SERVER_BUILDER);
		setupFluidCentrifugeConfig(SERVER_BUILDER);
		setupSieveConfig(SERVER_BUILDER);
		setupTurntableConfig(SERVER_BUILDER);

		SERVER_BUILDER.pop();

		SERVER_CONFIG = SERVER_BUILDER.build();
	}

	private static void setupAutoclaveConfig(ForgeConfigSpec.Builder server) {
		server.comment("Autoclave").push(SUBCATEGORY_AUTOCLAVE);

		AUTOCLAVE_STEAM_CAPACITY = server.comment("Autoclave Steam Capacity (mB)")
				.defineInRange("steamCapacity", 8000, 1, Integer.MAX_VALUE);
		AUTOCLAVE_TANK_CAPACITY = server.comment("Autoclave Tank Capacity (mB)")
				.defineInRange("tankCapacity", 8000, 1, Integer.MAX_VALUE);

		server.pop();
	}

	private static void setupFluidCentrifugeConfig(ForgeConfigSpec.Builder server) {
		server.comment("Fluid Centrifuge").push(SUBCATEGORY_FLUID_CENTRIFUGE);

		FLUID_CENTRIFUGE_COIL_CAPACITY = server.comment("Fluid Centrifuge Energy Capacity (FE)")
				.defineInRange("energyCapacity", 8000, 1, Integer.MAX_VALUE);
		FLUID_CENTRIFUGE_COIL_IO = server.comment("Fluid Centrifuge Energy IO (FE/t)")
				.defineInRange("energyIO", 8000, 1, Integer.MAX_VALUE);
		FLUID_CENTRIFUGE_INPUT_TANK_CAPACITY = server.comment("Fluid Centrifuge Input Tank Capacity (mB)")
				.defineInRange("inputTankCapacity", 8000, 1, Integer.MAX_VALUE);
		FLUID_CENTRIFUGE_OUTPUT_TANKS_CAPACITY = server.comment("Fluid Centrifuge Output Tanks Capacity (mB)")
				.defineInRange("outputTanksCapacity", 8000, 1, Integer.MAX_VALUE);

		server.pop();
	}

	private static void setupSieveConfig(ForgeConfigSpec.Builder server) {
		server.comment("Sieve").push(SUBCATEGORY_SIEVE);

		SIEVE_COIL_CAPACITY = server.comment("Sieve Energy Capacity (FE)")
				.defineInRange("energyCapacity", 8000, 1, Integer.MAX_VALUE);
		SIEVE_COIL_IO = server.comment("Sieve Energy IO (FE/t)")
				.defineInRange("energyIO", 8000, 1, Integer.MAX_VALUE);

		server.pop();
	}

	private static void setupTurntableConfig(ForgeConfigSpec.Builder server) {
		server.comment("Turntable").push(SUBCATEGORY_TURNTABLE);

		TURNTABLE_COIL_CAPACITY = server.comment("Turntable Energy Capacity (FE)")
				.defineInRange("energyCapacity", 8000, 1, Integer.MAX_VALUE);
		TURNTABLE_COIL_IO = server.comment("Turntable Energy IO (FE/t)")
				.defineInRange("energyIO", 8000, 1, Integer.MAX_VALUE);

		server.pop();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {}

	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading configEvent) {}
}
