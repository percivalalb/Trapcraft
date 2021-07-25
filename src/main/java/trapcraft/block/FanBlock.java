package trapcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import trapcraft.block.tileentity.FanTileEntity;

/**
 * @author ProPercivalalb
 **/
public class FanBlock extends ContainerBlock {

    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public FanBlock() {
        super(Block.Properties.of(Material.STONE).strength(2.0F, 2.0F).sound(SoundType.STONE));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.WEST).setValue(POWERED, false));
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new FanTileEntity();
    }

    @Override
       public BlockState getStateForPlacement(BlockItemUseContext context) {
        final BlockPos blockpos = context.getClickedPos();
        final World world = context.getLevel();
        boolean flag = world.hasNeighborSignal(blockpos) || world.hasNeighborSignal(blockpos.above());
           return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(POWERED, flag);
       }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (!worldIn.isClientSide) {
            this.updateFanState(state, worldIn, pos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            this.updateFanState(state, worldIn, pos);
        }
    }

    @Override
    public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            if (!worldIn.isClientSide && worldIn.getBlockEntity(pos) == null) {
                this.updateFanState(state, worldIn, pos);
            }
        }
    }

    private void updateFanState(BlockState state, World worldIn, BlockPos pos) {
        final boolean flag = worldIn.hasNeighborSignal(pos);
        if (flag != state.getValue(POWERED)) {
            worldIn.setBlock(pos, state.setValue(POWERED, flag), 2);
        }

    }
}
