package trapcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trapcraft.api.Properties;
import trapcraft.tileentity.TileEntityBearTrap;

public class BlockBearTrap extends BlockContainer {
	
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.15D, 1.0D);
	public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
	
	public BlockBearTrap() {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
		this.setCreativeTab(CreativeTabs.REDSTONE);
		this.setDefaultState(this.blockState.getBaseState().withProperty(TRIGGERED, Boolean.valueOf(false)));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(state.getValue(TRIGGERED)) {
			worldIn.setBlockState(pos, state.withProperty(TRIGGERED, false), 3);
	    	return true;
		}
    	return false;
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }
	   
	@Override
	public boolean isFullCube(IBlockState state) {
	    return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
	    return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
	    return EnumBlockRenderType.MODEL;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityBearTrap();
	}
	
	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        if ((entity instanceof EntityPlayer) || !(entity instanceof EntityLiving)) {
            return;
        }

        EntityLiving entityliving = (EntityLiving)entity;
        world.setBlockState(pos, state.withProperty(TRIGGERED, true), 3);
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
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
	    return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		if(!this.canBlockStay((World)world, pos)) {
			this.dropBlockAsItem((World)world, pos, world.getBlockState(pos), 0);
			((World)world).setBlockToAir(pos);
		}
	}

    public boolean canBlockStay(World world, BlockPos pos) {
		IBlockState blockstate = world.getBlockState(pos.down());
		return blockstate.getBlock().isSideSolid(blockstate, world, pos.down(), EnumFacing.UP);
	}
    
    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(TRIGGERED) ? 15 : 0;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TRIGGERED, meta != 0);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TRIGGERED) ? 1 : 0;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {TRIGGERED});
    }
    
    @Override
    public boolean canProvidePower(IBlockState state) {
        return state.getValue(TRIGGERED);
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (!blockState.canProvidePower()) {
            return 0;
        }
        else {
            return 15;
        }
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP ? blockState.getWeakPower(blockAccess, pos, side) : 0;
    }
}
