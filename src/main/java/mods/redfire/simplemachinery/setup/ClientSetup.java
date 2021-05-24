package mods.redfire.simplemachinery.setup;

import mods.redfire.simplemachinery.SimpleMachinery;
import mods.redfire.simplemachinery.registry.Containers;
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
        ScreenManager.register(Containers.CONTAINER_TURNTABLE.get(), TurntableScreen::new);
    }

    @SubscribeEvent
    public void onTooltipPre(RenderTooltipEvent.Pre event) {
        Item item = event.getStack().getItem();
        if (item.getRegistryName().getNamespace().equals(SimpleMachinery.MODID)) {
            event.setMaxWidth(200);
        }
    }
}
