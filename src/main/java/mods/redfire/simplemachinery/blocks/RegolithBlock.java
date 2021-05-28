package mods.redfire.simplemachinery.blocks;

import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class RegolithBlock extends FallingBlock {
    public RegolithBlock() {
        super(Properties.of(Material.SAND)
                .sound(SoundType.GRAVEL)
                .harvestTool(ToolType.SHOVEL)
                .harvestLevel(1)
                .strength(2.0f)
        );
    }
}
