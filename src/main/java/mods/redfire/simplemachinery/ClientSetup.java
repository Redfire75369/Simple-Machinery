/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery;

import mods.redfire.simplemachinery.registry.Containers;
import mods.redfire.simplemachinery.tileentities.autoclave.AutoclaveScreen;
import mods.redfire.simplemachinery.tileentities.fluidcentrifuge.FluidCentrifugeScreen;
import mods.redfire.simplemachinery.tileentities.sieve.SieveScreen;
import mods.redfire.simplemachinery.tileentities.turntable.TurntableScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SimpleMachinery.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
	public static void init(final FMLClientSetupEvent event) {
		ScreenManager.register(Containers.AUTOCLAVE_CONTAINER.get(), AutoclaveScreen::new);
		ScreenManager.register(Containers.FLUID_CENTRIFUGE_CONTAINER.get(), FluidCentrifugeScreen::new);
		ScreenManager.register(Containers.SIEVE_CONTAINER.get(), SieveScreen::new);
		ScreenManager.register(Containers.TURNTABLE_CONTAINER.get(), TurntableScreen::new);
	}

	@SubscribeEvent
	public void onTooltipPre(RenderTooltipEvent.Pre event) {
		Item item = event.getStack().getItem();
		if (item.getRegistryName().getNamespace().equals(SimpleMachinery.MODID)) {
			event.setMaxWidth(200);
		}
	}
}
