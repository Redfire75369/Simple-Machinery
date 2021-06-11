/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.registry;

import mods.redfire.simplemachinery.SimpleMachinery;
import mods.redfire.simplemachinery.blocks.RegolithBlock;
import mods.redfire.simplemachinery.tileentities.autoclave.AutoclaveTile;
import mods.redfire.simplemachinery.tileentities.machine.MachineBlock;
import mods.redfire.simplemachinery.tileentities.sieve.SieveTile;
import mods.redfire.simplemachinery.tileentities.turntable.TurntableTile;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.block.AbstractBlock.Properties;

public class Blocks {
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleMachinery.MODID);

	public static final RegistryObject<Block> REGOLITH_BLOCK = BLOCKS.register(Names.REGOLITH, RegolithBlock::new);

	public static final RegistryObject<MachineBlock> AUTOCLAVE_BLOCK = BLOCKS.register(Names.AUTOCLAVE, () -> new MachineBlock(
			Properties.of(Material.METAL).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).strength(2.0f),
			AutoclaveTile::new
	));
	public static final RegistryObject<MachineBlock> SIEVE_BLOCK = BLOCKS.register(Names.SIEVE, () -> new MachineBlock(
			Properties.of(Material.METAL).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).strength(2.0f),
			SieveTile::new
	));
	public static final RegistryObject<MachineBlock> TURNTABLE_BLOCK = BLOCKS.register(Names.TURNTABLE, () -> new MachineBlock(
			Properties.of(Material.METAL).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).strength(2.0f),
			TurntableTile::new
	));

	public static void register(IEventBus bus) {
		BLOCKS.register(bus);
	}
}
