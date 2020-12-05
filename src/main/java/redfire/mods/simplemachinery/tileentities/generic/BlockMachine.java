package redfire.mods.simplemachinery.tileentities.generic;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import redfire.mods.simplemachinery.SimpleMachinery;

public class BlockMachine<TE extends TileMachine> extends Block {
    public static final PropertyDirection faceDirection = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public int guiId;

    public BlockMachine(String name, int guiId) {
        super(Material.IRON);
        this.guiId = guiId;

        setRegistryName(name);
        setUnlocalizedName(SimpleMachinery.modid + "." + name);
        setHardness(3.5F);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(SimpleMachinery.creativeTab);

        setDefaultState(blockState.getBaseState().withProperty(faceDirection, EnumFacing.NORTH));
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(faceDirection, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {faceDirection});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(faceDirection, EnumFacing.getFront(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(faceDirection).getIndex();
    }
}
