package mods.redfire.simplemachinery.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockRegolith extends Block {
    public BlockRegolith() {
        super(Properties.of(Material.SAND)
                .sound(SoundType.GRAVEL)
                .harvestTool(ToolType.SHOVEL)
                .harvestLevel(1)
                .strength(2.0f)
        );
    }
}
