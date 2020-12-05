package redfire.mods.simplemachinery.tileentities.fluidcentrifuge;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import redfire.mods.simplemachinery.SimpleMachinery;
import redfire.mods.simplemachinery.tileentities.generic.BlockMachine;

import javax.annotation.Nullable;

public class BlockFluidCentrifuge extends BlockMachine implements ITileEntityProvider {
    public BlockFluidCentrifuge() {
        super("fluid_centrifuge", 2);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileFluidCentrifuge();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!(tileEntity instanceof TileFluidCentrifuge)) {
            return false;
        }
        playerIn.openGui(SimpleMachinery.instance, guiId, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileFluidCentrifuge) {
            ((TileFluidCentrifuge) tileentity).dropInventoryItems(worldIn, pos);
        }

        super.breakBlock(worldIn, pos, state);
    }
}
