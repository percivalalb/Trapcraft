package trapcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;
import trapcraft.tileentity.TileEntityIgniter;

/**
 * @author ProPercivalalb
 **/
public class BlockIgniter extends ContainerBlock {
    
	public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public BlockIgniter() {
    	super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F, 2.0F).sound(SoundType.STONE));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.WEST));
    }

    @Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(worldIn.isRemote) {
            return true;
        } else {
            TileEntityIgniter tileentityigniter = (TileEntityIgniter)worldIn.getTileEntity(pos);

            if (tileentityigniter != null) {
            	if(player instanceof ServerPlayerEntity && !(player instanceof FakePlayer)) {
                    ServerPlayerEntity entityPlayerMP = (ServerPlayerEntity) player;

                    NetworkHooks.openGui(entityPlayerMP, tileentityigniter, pos);
                }
            }

            return true;
        }
    }
    
    @Override
	public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
    	this.updateIgniterState((World)world, pos);
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    	if (!worldIn.isRemote) {
    		this.updateIgniterState(worldIn, pos);
    	}
    }

    @Override
	public void neighborChanged(BlockState p_220069_1_, World worldIn, BlockPos pos, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
    	if (!worldIn.isRemote) {
    		this.updateIgniterState(worldIn, pos);
    	}
	}

    @Override
    public void onBlockAdded(BlockState p_220082_1_, World worldIn, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
    	if (p_220082_4_.getBlock() != p_220082_1_.getBlock()) {
    		if (!worldIn.isRemote) {
    			this.updateIgniterState(worldIn, pos);
    		}
    	}
    }
    
    @Override
   	public BlockState getStateForPlacement(BlockItemUseContext context) {
   		return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
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
    	builder.add(FACING);
	}
    
    public void updateIgniterState(World world, BlockPos pos) {
       Direction facing = world.getBlockState(pos).get(FACING);
       
       int distance = 1, oldDistance = 1;
       TileEntity tileEntity = world.getTileEntity(pos);
       if(tileEntity instanceof TileEntityIgniter) {
    	   TileEntityIgniter igniter = (TileEntityIgniter)world.getTileEntity(pos);
    	   distance = igniter.getRangeUpgrades() + 1;
    	   oldDistance = igniter.lastUpgrades + 1;
       }
       
       updateIgniterState(world, pos, world.isBlockPowered(pos), facing, distance, oldDistance);
       
       if(tileEntity instanceof TileEntityIgniter) {
    	   TileEntityIgniter igniter = (TileEntityIgniter)world.getTileEntity(pos);
    	   igniter.lastUpgrades = distance - 1;
       }
    }
    
    private void updateIgniterState(World world, BlockPos pos, boolean powered, Direction direction, int newDistance, int previousDistance) {
    	// If distance has changed remove old fire
        if(newDistance != previousDistance) {
        	 BlockPos oldPos = pos.offset(direction, previousDistance);
        	 removePossibleFire(world, oldPos);
        }

        BlockPos firePos = pos.offset(direction, newDistance);
        
        if(powered) {
     	   if(world.isAirBlock(firePos)) {
     		   world.setBlockState(firePos, Blocks.FIRE.getDefaultState());
     	   }
        }
        else if(!powered) {
        	removePossibleFire(world, firePos);
        }
     }
    
    public void removePossibleFire(World world, BlockPos pos) {
    	if(world.getBlockState(pos).getBlock() == Blocks.FIRE) {
    		world.setBlockState(pos, Blocks.AIR.getDefaultState());
    		world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5F, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F, true);
    	}
    }

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityIgniter();
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if(state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if(tileentity instanceof TileEntityIgniter) {
				int upgrades = ((TileEntityIgniter) tileentity).getRangeUpgrades() + 1;
				updateIgniterState(worldIn, pos, false, state.get(FACING), upgrades, upgrades);
				
				InventoryHelper.dropInventoryItems(worldIn, pos, ((TileEntityIgniter)tileentity).inventory);
				
				worldIn.updateComparatorOutputLevel(pos, this);
			}

		}
		
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
}
