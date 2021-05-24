package mods.redfire.simplemachinery.registry;

import mods.redfire.simplemachinery.SimpleMachinery;
import mods.redfire.simplemachinery.tileentities.turntable.TurntableTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntities {
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SimpleMachinery.MODID);

    public static final RegistryObject<TileEntityType<TurntableTile>> TILE_TURNTABLE = TILES.register(Names.TURNTABLE,
            () -> TileEntityType.Builder.of(TurntableTile::new, Blocks.TURNTABLE.get()).build(null)
    );

    public static void register(IEventBus bus) {
        TILES.register(bus);
    }
}
