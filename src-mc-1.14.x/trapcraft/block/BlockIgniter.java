package trapcraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;
import trapcraft.tileentity.TileEntityIgniter;

/**
 * @author ProPercivalalb
 **/
public class BlockIgniter extends BlockContainer {
    
	public static final DirectionProperty FACING = BlockDirectional.FACING;
    private Random rand = new Random();

    public BlockIgniter() {
    	super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F, 2.0F).sound(SoundType.STONE));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.WEST));
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) {
            return true;
        }
        else
        {
            TileEntityIgniter tileentityigniter = (TileEntityIgniter)worldIn.getTileEntity(pos);

            if (tileentityigniter != null) {
            	if(playerIn instanceof EntityPlayerMP && !(playerIn instanceof FakePlayer)) {
                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerIn;

                    NetworkHooks.openGui(entityPlayerMP, tileentityigniter, buf -> buf.writeBlockPos(pos));
                }
            }

            return true;
        }
    }
    
    @Override
	public void onNeighborChange(IBlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
    	this.updateIgniterState((World)world, pos);
    }
    
    @Override
   	public IBlockState getStateForPlacement(BlockItemUseContext context) {
   		return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
   	}
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState rotate(IBlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate((EnumFacing)state.get(FACING)));
    }

    @Override
    public IBlockState mirror(IBlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation((EnumFacing)state.get(FACING)));
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
    	builder.add(FACING);
	}
    
    
    
    public void updateIgniterState(World world, BlockPos pos) {
       EnumFacing facing = world.getBlockState(pos).get(FACING);
      
       TileEntityIgniter igniter = (TileEntityIgniter)world.getTileEntity(pos);

       
       
       boolean flag = world.isBlockPowered(pos);
       BlockPos firePos = pos.offset(facing, igniter.getRangeUpgrades() + 1);

       if(flag) {
    	   if(world.isAirBlock(firePos)) {
    		   world.setBlockState(firePos, Blocks.FIRE.getDefaultState());
    	   }
       }
       else if(!flag && world.getBlockState(firePos).getBlock() == Blocks.FIRE) {
    	   world.setBlockState(firePos, Blocks.AIR.getDefaultState());
           world.playSound(firePos.getX() + 0.5D, firePos.getY() + 0.5D, firePos.getZ() + 0.5F, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F, true);
       }
        
    }

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityIgniter();
	}
	
	@Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
		if(state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if(tileentity instanceof TileEntityIgniter) {
				InventoryHelper.dropInventoryItems(worldIn, pos, ((TileEntityIgniter)tileentity).inventory);
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
}
