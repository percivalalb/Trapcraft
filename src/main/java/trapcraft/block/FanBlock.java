package trapcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import trapcraft.TrapcraftTileEntityTypes;
import trapcraft.block.tileentity.BearTrapTileEntity;
import trapcraft.block.tileentity.FanTileEntity;

import javax.annotation.Nullable;

/**
 * @author ProPercivalalb
 **/
public class FanBlock extends BaseEntityBlock {

    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public FanBlock() {
        super(Block.Properties.of(Material.STONE).strength(2.0F, 2.0F).sound(SoundType.STONE));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.WEST).setValue(POWERED, false));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new FanTileEntity(pos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, TrapcraftTileEntityTypes.FAN.get(), FanTileEntity::tick);
    }

    @Override
       public BlockState getStateForPlacement(BlockPlaceContext context) {
        final BlockPos blockpos = context.getClickedPos();
        final Level world = context.getLevel();
        boolean flag = world.hasNeighborSignal(blockpos) || world.hasNeighborSignal(blockpos.above());
           return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(POWERED, flag);
       }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (!worldIn.isClientSide) {
            this.updateFanState(state, worldIn, pos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            this.updateFanState(state, worldIn, pos);
        }
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            if (!worldIn.isClientSide && worldIn.getBlockEntity(pos) == null) {
                this.updateFanState(state, worldIn, pos);
            }
        }
    }

    private void updateFanState(BlockState state, Level worldIn, BlockPos pos) {
        final boolean flag = worldIn.hasNeighborSignal(pos);
        if (flag != state.getValue(POWERED)) {
            worldIn.setBlock(pos, state.setValue(POWERED, flag), 2);
        }

    }
}
