package trapcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trapcraft.api.Properties;

/**
 * @author ProPercivalalb
 **/
public class BlockGrassCovering extends Block {
    
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 1.0D - 0.0625D, 0.0D, 1.0D, 1.0D, 1.0D);
	
	public BlockGrassCovering() {
        super(Material.GRASS);
        this.setHardness(0.2F);
        this.setSoundType(SoundType.GROUND);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.REDSTONE);
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
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
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
    	for(EnumFacing facing : EnumFacing.HORIZONTALS) {
    		BlockPos posOff = pos.offset(facing);
    		IBlockState blockstate = world.getBlockState(posOff);
    		if(blockstate.getBlock().isSideSolid(blockstate, world, posOff, facing.getOpposite()) || blockstate.getBlock() == this)
    			return true;

    	}
    	
    	return false;
	}
	
    @Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
    	if(entity instanceof EntityLivingBase && !world.isRemote) {
            world.setBlockToAir(pos);

            for (int l = 0; l < 2; l++) {
                float f = 0.7F;
                float f1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                float f2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                float f3 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                EntityItem entityitem = new EntityItem(world, (double)pos.getX() + f1, (double)pos.getY() + f2, (double)pos.getZ() + f3, new ItemStack(Items.STICK));
                entityitem.setPickupDelay(10);
                world.spawnEntity(entityitem);
            }
        }
    }
}
