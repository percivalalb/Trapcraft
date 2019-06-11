package trapcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import trapcraft.tileentity.TileEntityBearTrap;

public class BlockBearTrap extends BlockContainer {
	
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
	public static final BooleanProperty TRIGGERED = BooleanProperty.create("triggered");
	
	public BlockBearTrap() {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 2.0F).sound(SoundType.METAL));
		this.setDefaultState(this.stateContainer.getBaseState().with(TRIGGERED, Boolean.valueOf(false)));
		
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(state.get(TRIGGERED)) {
			worldIn.setBlockState(pos, state.with(TRIGGERED, false), 3);
	    	return true;
		}
		
    	return false;
    }
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return VoxelShapes.empty();
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
	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.SOLID;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityBearTrap();
	}
	
	@Override
	public void onEntityCollision(IBlockState state, World world, BlockPos pos, Entity entity) {
        if ((entity instanceof EntityPlayer) || !(entity instanceof EntityLiving)) {
            return;
        }

        EntityLiving entityliving = (EntityLiving)entity;
        world.setBlockState(pos, state.with(TRIGGERED, true), 3);
        TileEntityBearTrap tileentitybeartrap = (TileEntityBearTrap)world.getTileEntity(pos);
        tileentitybeartrap.entityliving = entityliving;
        tileentitybeartrap.moveSpeed = 0;
        tileentitybeartrap.prevHealth = (float)entityliving.getHealth();
        tileentitybeartrap.moveSpeed = entityliving.getAIMoveSpeed();
        tileentitybeartrap.posX = entityliving.posX;
        tileentitybeartrap.posY = entityliving.posY;
        tileentitybeartrap.posZ = entityliving.posZ;
        return;
    }

	@Override
	public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == EnumFacing.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	@Override
	public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos) {
		IBlockState down = worldIn.getBlockState(pos.down());
		return down.isTopSolid() || down.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID;
	}
    
    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {

        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.get(TRIGGERED) ? 15 : 0;
    }
  
    
    @Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(TRIGGERED);
	}
    
    @Override
    public boolean canProvidePower(IBlockState state) {
        return state.get(TRIGGERED);
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockReader blockReader, BlockPos pos, EnumFacing side) {
        if (!blockState.canProvidePower()) {
            return 0;
        }
        else {
            return 15;
        }
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockReader blockReader, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP ? blockState.getWeakPower(blockReader, pos, side) : 0;
    }
}
