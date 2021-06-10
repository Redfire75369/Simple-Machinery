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

	public static final RegistryObject<Item> REGOLITH_ITEM = ITEMS.register(Names.REGOLITH,
			() -> new BlockItem(Blocks.REGOLITH_BLOCK.get(), new Item.Properties().tab(SimpleMachinery.TAB_BLOCKS))
	);

	public static final RegistryObject<Item> AUTOCLAVE_ITEM = ITEMS.register(Names.AUTOCLAVE,
			() -> new BlockItem(Blocks.AUTOCLAVE_BLOCK.get(), new Item.Properties().tab(SimpleMachinery.TAB_MACHINES))
	);
	public static final RegistryObject<Item> SIEVE_ITEM = ITEMS.register(Names.SIEVE,
			() -> new BlockItem(Blocks.SIEVE_BLOCK.get(), new Item.Properties().tab(SimpleMachinery.TAB_MACHINES))
	);
	public static final RegistryObject<Item> TURNTABLE_ITEM = ITEMS.register(Names.TURNTABLE,
			() -> new BlockItem(Blocks.TURNTABLE_BLOCK.get(), new Item.Properties().tab(SimpleMachinery.TAB_MACHINES))
	);

	public static void register(IEventBus bus) {
		ITEMS.register(bus);
	}
}
