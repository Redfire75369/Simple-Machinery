package mods.redfire.simplemachinery.registry;

import mods.redfire.simplemachinery.SimpleMachinery;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Items {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimpleMachinery.MODID);

    public static final RegistryObject<Item> ITEM_REGOLITH = ITEMS.register(Names.REGOLITH,
        () -> new BlockItem(Blocks.REGOLITH.get(), new Item.Properties().tab(SimpleMachinery.TAB_BLOCKS))
    );
    public static final RegistryObject<Item> ITEM_TURNTABLE = ITEMS.register(Names.TURNTABLE,
            () -> new BlockItem(Blocks.TURNTABLE.get(), new Item.Properties().tab(SimpleMachinery.TAB_MACHINES))
    );

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
