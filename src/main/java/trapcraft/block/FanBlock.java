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
    	super(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 2.0F).sound(SoundType.STONE));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.WEST).with(POWERED, false));
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new FanTileEntity();
    }

    @Override
   	public BlockState getStateForPlacement(BlockItemUseContext context) {
		final BlockPos blockpos = context.getPos();
		final World world = context.getWorld();
    	boolean flag = world.isBlockPowered(blockpos) || world.isBlockPowered(blockpos.up());
   		return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite()).with(POWERED, flag);
   	}

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
	public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    	if (!worldIn.isRemote) {
    		this.updateFanState(state, worldIn, pos);
    	}
    }

    @Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    	if (!worldIn.isRemote) {
    		this.updateFanState(state, worldIn, pos);
    	}
	}

	@Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
    	if (oldState.getBlock() != state.getBlock()) {
    		if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
    			this.updateFanState(state, worldIn, pos);
    		}
    	}
    }

    private void updateFanState(BlockState state, World worldIn, BlockPos pos) {
		final boolean flag = worldIn.isBlockPowered(pos);
        if (flag != state.get(POWERED)) {
        	worldIn.setBlockState(pos, state.with(POWERED, flag), 2);
        }

	}
}
