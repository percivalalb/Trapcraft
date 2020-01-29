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
import trapcraft.tileentity.TileEntityFan;

/**
 * @author ProPercivalalb
 **/
public class BlockFan extends ContainerBlock {
	
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
	
    public BlockFan() {
    	super(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 2.0F).sound(SoundType.STONE));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.WEST).with(POWERED, false));
    }
    
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TileEntityFan();
    }
    
    @Override
   	public BlockState getStateForPlacement(BlockItemUseContext context) {
    	BlockPos blockpos = context.getPos();
        World world = context.getWorld();
    	boolean flag = world.isBlockPowered(blockpos) || world.isBlockPowered(blockpos.up());
   		return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite()).with(POWERED, flag);
   	}
    
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
	public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate((Direction)state.get(FACING)));
    }

    @Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation((Direction)state.get(FACING)));
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
	public void neighborChanged(BlockState p_220069_1_, World worldIn, BlockPos pos, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
    	if (!worldIn.isRemote) {
    		this.updateFanState(p_220069_1_, worldIn, pos);
    	}
	}

	@Override
    public void onBlockAdded(BlockState p_220082_1_, World worldIn, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
    	if (p_220082_4_.getBlock() != p_220082_1_.getBlock()) {
    		if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
    			this.updateFanState(p_220082_1_, worldIn, pos);
    		}
    	}
    }
    
    private void updateFanState(BlockState state, World worldIn, BlockPos pos) {
    	boolean flag = worldIn.isBlockPowered(pos);
        if(flag != state.get(POWERED)) {
        	worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(flag)), 2);
        }
		
	}
}
