package mods.redfire.simplemachinery.tileentities.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MachineBlock extends Block {
	protected final Supplier<? extends MachineTile<?>> supplier;

	public MachineBlock(Properties props, Supplier<? extends MachineTile<?>> supplier) {
		super(props);
		this.supplier = supplier;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return supplier.get().worldContext(state, world);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING,
				ctx.getPlayer() != null ? ctx.getHorizontalDirection().getOpposite() : Direction.NORTH
		);
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public ActionResultType use(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult trace) {
		if (world.isClientSide) {
			return ActionResultType.SUCCESS;
		}

		TileEntity tile = world.getBlockEntity(pos);
		if (!(tile instanceof MachineTile) || tile.isRemoved()) {
			return ActionResultType.PASS;
		}

		NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tile, pos);
		return ActionResultType.SUCCESS;
	}
}
