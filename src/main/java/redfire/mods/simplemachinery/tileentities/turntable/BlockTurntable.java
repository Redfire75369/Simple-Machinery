package redfire.mods.simplemachinery.tileentities.turntable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import redfire.mods.simplemachinery.SimpleMachinery;

import javax.annotation.Nullable;

public class BlockTurntable extends Block implements ITileEntityProvider {

	public static final PropertyDirection faceDirection = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final int guiId = 2;

	public BlockTurntable() {
		super(Material.IRON);
		setRegistryName("turntable");
		setUnlocalizedName(SimpleMachinery.modid + ".turntable");
		setHardness(3.5F);
		setHarvestLevel("pickaxe", 1);
		setCreativeTab(SimpleMachinery.creativeTab);

		setDefaultState(blockState.getBaseState().withProperty(faceDirection, EnumFacing.NORTH));
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileTurntable();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		}
		TileEntity te = worldIn.getTileEntity(pos);
		if (!(te instanceof TileTurntable)) {
			return false;
		}
		playerIn.openGui(SimpleMachinery.instance, guiId, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileTurntable) {
			((TileTurntable) tileentity).dropInventoryItems(worldIn, pos);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(faceDirection, placer.getHorizontalFacing().getOpposite());
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
