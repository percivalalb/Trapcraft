package trapcraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;
import trapcraft.tileentity.TileEntityIgniter;

/**
 * @author ProPercivalalb
 **/
public class BlockIgniter extends BlockContainer {
    
	public static final PropertyDirection FACING = BlockDirectional.FACING;
    private Random rand = new Random();

    public BlockIgniter() {
        super(Material.ROCK);
        this.setHardness(3.5F);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) {
            return true;
        }
        else
        {
            TileEntityIgniter tileentityigniter = (TileEntityIgniter)worldIn.getTileEntity(pos);

            if (tileentityigniter != null) {
            	playerIn.openGui(TrapcraftMod.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }
    
    @Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
    	this.updateIgniterState((World)world, pos);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, BlockPistonBase.getFacingFromEntity(pos, placer));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(pos, placer)), 2);
    }
    
    public static EnumFacing getFacing(int meta) {
        return EnumFacing.getFront(meta & 7);
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, getFacing(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }
    
    
    
    public void updateIgniterState(World world, BlockPos pos) {
       /**
    	int metadata = par1World.getBlockMetadata(pos);
        int direction = getOrientation(metadata);
        TileEntityIgniter igniter = (TileEntityIgniter)par1World.getTileEntity(pos);

        if (direction != 7) {
            boolean flag = world.isBlockIndirectlyGettingPowered(pos)
            int x = Facing.offsetsXForSide[direction];
        	int y = Facing.offsetsYForSide[direction];
        	int z = Facing.offsetsZForSide[direction];
        	
        	if(z != 0) {
        		if(z < 0) {
        			z -= igniter.getRangeUpgrades();
        		}
        		else if(z > 0) {
        			z += igniter.getRangeUpgrades();
        		}	
        	}
        	
        	if(x != 0) {
        		if(x < 0) {
        			x -= igniter.getRangeUpgrades();
        		}
        		else if(x > 0) {
        			x += igniter.getRangeUpgrades();
        		}	
        	}
        	
        	if(y != 0) {
        		if(y < 0) {
        			y -= igniter.getRangeUpgrades();
        		}
        		else if(y > 0) {
        			y += igniter.getRangeUpgrades();
        		}	
        	}
        	
            if(flag) {
            	if(par1World.getBlock(x + par2, y + par3, z + par4) == Blocks.air) {
            		if(World.doesBlockHaveSolidTopSurface(par1World, x + par2, y + par3 - 1, z + par4)) {
            			par1World.setBlock(x + par2, y + par3, z + par4, Blocks.fire);
            		}
            	}
            		
            }
            else if(!flag && par1World.getBlock(x + par2, y + par3, z + par4) == Blocks.fire) {
            	par1World.setBlockToAir(x + par2, y + par3, z + par4);
            	par1World.playSoundEffect(x + par2 + 0.5D, y + par3 + 0.5D, z + par4 + 0.5D, "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);
            }
        }**/
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityIgniter();
	}
	
	/**
	@Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        dropInventory(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }

    private void dropInventory(World world, int x, int y, int z) {

        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (!(tileEntity instanceof IInventory))
            return;

        IInventory inventory = (IInventory)tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {

            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack != null && itemStack.stackSize > 0) {
                float dX = rand.nextFloat() * 0.8F + 0.1F;
                float dY = rand.nextFloat() * 0.8F + 0.1F;
                float dZ = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, new ItemStack(itemStack.getItem(), itemStack.stackSize, itemStack.getItemDamage()));

                if (itemStack.hasTagCompound()) {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                itemStack.stackSize = 0;
            }
        }
    }**/
}
