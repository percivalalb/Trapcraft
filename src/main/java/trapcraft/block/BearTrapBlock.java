package trapcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import trapcraft.block.tileentity.BearTrapTileEntity;

public class BearTrapBlock extends ContainerBlock implements IWaterLoggable {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
	public static final BooleanProperty TRIGGERED = BooleanProperty.create("triggered");

	public BearTrapBlock() {
		super(Block.Properties.create(Material.IRON).notSolid().hardnessAndResistance(2.0F, 2.0F).sound(SoundType.METAL));
		this.setDefaultState(this.stateContainer.getBaseState().with(TRIGGERED, Boolean.valueOf(false)).with(WATERLOGGED, Boolean.valueOf(false)));

	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(!worldIn.isRemote) {
			BearTrapTileEntity bearTrap = (BearTrapTileEntity)worldIn.getTileEntity(pos);
			if(state.get(TRIGGERED) && !bearTrap.hasTrappedEntity()) {
				worldIn.setBlockState(pos, state.with(TRIGGERED, false), 3);
		    	return ActionResultType.SUCCESS;
			}
		}

    	return ActionResultType.PASS;
    }

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext selectionContext) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext selectionContext) {
		return VoxelShapes.empty();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new BearTrapTileEntity();
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if(state.get(TRIGGERED)) {
			return;
		}


        if ((entity instanceof PlayerEntity) || !(entity instanceof MobEntity)) {
            return;
        }

        MobEntity livingEntity = (MobEntity)entity;
        world.setBlockState(pos, state.with(TRIGGERED, true), 3);
        BearTrapTileEntity bearTrap = (BearTrapTileEntity)world.getTileEntity(pos);
        bearTrap.setTrappedEntity(livingEntity);
    }

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if(stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());

		return this.getDefaultState().with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
	}

	@Override
	public IFluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return Block.hasEnoughSolidSide(worldIn, pos.down(), Direction.UP);
	}

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }


    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return blockState.get(TRIGGERED) ? 15 : 0;
    }

    @Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(TRIGGERED, WATERLOGGED);
	}

    @Override
    public boolean canProvidePower(BlockState state) {
        return state.get(TRIGGERED);
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockReader, BlockPos pos, Direction side) {
        if (!blockState.canProvidePower()) {
            return 0;
        }
        else {
            return 15;
        }
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockReader, BlockPos pos, Direction side) {
        return side == Direction.UP ? blockState.getWeakPower(blockReader, pos, side) : 0;
    }
}
