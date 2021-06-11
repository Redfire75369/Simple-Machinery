/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.registry;

import mods.redfire.simplemachinery.SimpleMachinery;
import mods.redfire.simplemachinery.tileentities.autoclave.AutoclaveTile;
import mods.redfire.simplemachinery.tileentities.sieve.SieveTile;
import mods.redfire.simplemachinery.tileentities.turntable.TurntableTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntities {
	private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SimpleMachinery.MODID);

	public static final RegistryObject<TileEntityType<AutoclaveTile>> AUTOCLAVE_TILE = TILES.register(Names.AUTOCLAVE,
			() -> TileEntityType.Builder.of(AutoclaveTile::new, Blocks.AUTOCLAVE_BLOCK.get()).build(null)
	);
	public static final RegistryObject<TileEntityType<SieveTile>> SIEVE_TILE = TILES.register(Names.SIEVE,
			() -> TileEntityType.Builder.of(SieveTile::new, Blocks.SIEVE_BLOCK.get()).build(null)
	);
	public static final RegistryObject<TileEntityType<TurntableTile>> TURNTABLE_TILE = TILES.register(Names.TURNTABLE,
			() -> TileEntityType.Builder.of(TurntableTile::new, Blocks.TURNTABLE_BLOCK.get()).build(null)
	);

	public static void register(IEventBus bus) {
		TILES.register(bus);
	}
}
