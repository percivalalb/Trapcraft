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
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
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
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    public static final BooleanProperty TRIGGERED = BooleanProperty.create("triggered");

    public BearTrapBlock() {
        super(Block.Properties.of(Material.METAL).noOcclusion().strength(2.0F, 2.0F).sound(SoundType.METAL));
        this.registerDefaultState(this.stateDefinition.any().setValue(TRIGGERED, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(false)));

    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide) {
            BearTrapTileEntity bearTrap = (BearTrapTileEntity)worldIn.getBlockEntity(pos);
            if (state.getValue(TRIGGERED) && !bearTrap.hasTrappedEntity()) {
                worldIn.setBlock(pos, state.setValue(TRIGGERED, false), 3);
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
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new BearTrapTileEntity();
    }

    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        if (state.getValue(TRIGGERED)) {
            return;
        }


        if ((entity instanceof PlayerEntity) || !(entity instanceof MobEntity)) {
            return;
        }

        final MobEntity livingEntity = (MobEntity)entity;
        world.setBlock(pos, state.setValue(TRIGGERED, true), 3);
        final BearTrapTileEntity bearTrap = (BearTrapTileEntity)world.getBlockEntity(pos);
        bearTrap.setTrappedEntity(livingEntity);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }

        return facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        final FluidState ifluidstate = context.getLevel().getFluidState(context.getClickedPos());

        return this.defaultBlockState().setValue(WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return Block.canSupportCenter(worldIn, pos.below(), Direction.UP);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }


    @Override
    public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(TRIGGERED) ? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TRIGGERED, WATERLOGGED);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return state.getValue(TRIGGERED);
    }

    @Override
    public int getSignal(BlockState blockState, IBlockReader blockReader, BlockPos pos, Direction side) {
        if (!blockState.isSignalSource()) {
            return 0;
        }
        else {
            return 15;
        }
    }

    @Override
    public int getDirectSignal(BlockState blockState, IBlockReader blockReader, BlockPos pos, Direction side) {
        return side == Direction.UP ? blockState.getSignal(blockReader, pos, side) : 0;
    }
}
