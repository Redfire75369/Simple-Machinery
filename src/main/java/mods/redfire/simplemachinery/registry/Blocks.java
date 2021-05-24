package mods.redfire.simplemachinery.registry;

import mods.redfire.simplemachinery.SimpleMachinery;
import mods.redfire.simplemachinery.blocks.BlockRegolith;
import mods.redfire.simplemachinery.tileentities.machine.MachineBlock;
import mods.redfire.simplemachinery.tileentities.turntable.TurntableTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Blocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleMachinery.MODID);

    public static final RegistryObject<BlockRegolith> REGOLITH = BLOCKS.register(Names.REGOLITH, BlockRegolith::new);
    public static final RegistryObject<MachineBlock> TURNTABLE = BLOCKS.register(Names.TURNTABLE, () -> {
        return new MachineBlock(
                AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).strength(2.0f),
                TurntableTile::new
        );
    });

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
