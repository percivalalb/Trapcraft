package trapcraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import trapcraft.tileentity.TileEntityMagneticChest;

/**
 * @author ProPercivalalb
 **/
public class BlockMagneticChest extends BlockContainer {

    private Random rand = new Random();
    public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    
    
    public BlockMagneticChest() {
    	super(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.5F, 2.0F).sound(SoundType.WOOD));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.WEST));
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state) {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory(this.getLockableContainer(worldIn, pos));
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
    
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TileEntityMagneticChest();
    }
    
    @Override
	public void onEntityCollision(IBlockState state, World world, BlockPos pos, Entity entity) {
        if(entity.isAlive() && !world.isRemote) {
            if (entity instanceof EntityItem) {
                TileEntityMagneticChest tileEntityMagneticChest = (TileEntityMagneticChest)world.getTileEntity(pos);
                tileEntityMagneticChest.insertStackFromEntity((EntityItem)entity);
            }
        }
    }
    
    @Override
	public boolean isFullCube(IBlockState state) {
	    return false;
	}
	
	@Override
	public boolean isSolid(IBlockState state) {
		return false;
	}

    @Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPE;
	}
    
    @Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

    @Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
		if(state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if(tileentity instanceof IInventory) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
    
    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    	if(playerIn.isSneaking())
    		return true;
    	//else if(worldIn.isSideSolid(pos.up(), EnumFacing.DOWN))
        //    return true;
    	else {
    		ILockableContainer ilockablecontainer = this.getLockableContainer(worldIn, pos);

            if(ilockablecontainer != null) {
                playerIn.displayGUIChest(ilockablecontainer);
            }

            return true;
        }
    }
    
    public ILockableContainer getLockableContainer(World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (!(tileentity instanceof TileEntityMagneticChest))
        {
            return null;
        }
        else
        {
        	TileEntityMagneticChest ilockablecontainer = (TileEntityMagneticChest)tileentity;

                return ilockablecontainer;
            
        }
    }
    
    @Override
   	public IBlockState getStateForPlacement(BlockItemUseContext context) {
   		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
   	}
}
