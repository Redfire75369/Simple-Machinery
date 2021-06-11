/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery;

import mods.redfire.simplemachinery.network.Networking;
import mods.redfire.simplemachinery.registry.*;
import mods.redfire.simplemachinery.setup.ClientSetup;
import mods.redfire.simplemachinery.setup.Config;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(SimpleMachinery.MODID)
public class SimpleMachinery {
	public static final String MODID = "simplemachinery";
	public static final Logger LOGGER = LogManager.getLogger();

	public static final ItemGroup TAB_BLOCKS = new ItemGroup("simplemachinery.blocks") {
		@Nonnull
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.REGOLITH_ITEM.get());
		}
	};
	public static final ItemGroup TAB_MACHINES = new ItemGroup("simplemachinery.machines") {
		@Nonnull
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.TURNTABLE_ITEM.get());
		}
	};


	public SimpleMachinery() {
		ModLoadingContext ctxLoading = ModLoadingContext.get();
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		ctxLoading.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
		ctxLoading.registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

		Blocks.register(bus);
		Items.register(bus);
		TileEntities.register(bus);
		Containers.register(bus);

		bus.addGenericListener(IRecipeSerializer.class, RecipeSerializers::register);
		bus.addListener(ClientSetup::init);

		Networking.registerMessages();
	}
}
