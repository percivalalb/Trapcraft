package trapcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;
import trapcraft.common.tileentitys.TileEntityIgniter;
import trapcraft.common.tileentitys.TileEntityMagneticChest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class BlockIgniter extends BlockContainer {
	
	public static final int[] faceToSide = new int[] {1, 0, 3, 2, 5, 4};
    @SideOnly(Side.CLIENT)
    private IIcon iconBottom;
    @SideOnly(Side.CLIENT)
    private IIcon iconTop;
    
    private Random rand = new Random();

    public BlockIgniter() {
        super(Material.rock);
        this.setStepSound(Block.soundTypeStone);
        this.setHardness(0.5F);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        int k = getOrientation(meta);
    
        if(k > 5) {
        	return this.iconTop;
        }
        else if(side == k) {
        	return this.iconTop;
        }
        else {
        	return side == BlockIgniter.faceToSide[k] ? this.iconBottom : this.blockIcon;
        }
        	
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(Properties.TEX_PACKAGE + "igniter_side");
        this.iconTop = par1IconRegister.registerIcon(Properties.TEX_PACKAGE + "igniter_top");
        this.iconBottom = par1IconRegister.registerIcon(Properties.TEX_PACKAGE + "igniter_bottom");
    }

    @Override
    public int getRenderType() {
        return 16;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (par1World.isRemote) {
            return true;
        }
        else
        {
            TileEntityIgniter tileentityigniter = (TileEntityIgniter)par1World.getTileEntity(par2, par3, par4);

            if (tileentityigniter != null) {
                par5EntityPlayer.openGui(TrapcraftMod.instance, 1, par1World, par2, par3, par4);
            }

            return true;
        }
    }
    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack) {
        int l = determineOrientation(par1World, par2, par3, par4, par5EntityLiving);
        par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
        if (!par1World.isRemote) {
            this.updateIgniterState(par1World, par2, par3, par4);
        }
    }
    
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
        if (!par1World.isRemote) {
            this.updateIgniterState(par1World, par2, par3, par4);
        }
    }

    private boolean isIndirectlyPowered(World par1World, int par2, int par3, int par4, int par5) {
        return par1World.getIndirectPowerOutput(par2, par3 - 1, par4, 0) ? true : (par1World.getIndirectPowerOutput(par2, par3 + 1, par4, 1) ? true : (par1World.getIndirectPowerOutput(par2, par3, par4 - 1, 2) ? true : (par1World.getIndirectPowerOutput(par2, par3, par4 + 1, 3) ? true : (par1World.getIndirectPowerOutput(par2 + 1, par3, par4, 5) ? true : (par1World.getIndirectPowerOutput(par2 - 1, par3, par4, 4) ? true : (par1World.getIndirectPowerOutput(par2, par3, par4, 0) ? true : (par1World.getIndirectPowerOutput(par2, par3 + 2, par4, 1) ? true : (par1World.getIndirectPowerOutput(par2, par3 + 1, par4 - 1, 2) ? true : (par1World.getIndirectPowerOutput(par2, par3 + 1, par4 + 1, 3) ? true : (par1World.getIndirectPowerOutput(par2 - 1, par3 + 1, par4, 4) ? true : par1World.getIndirectPowerOutput(par2 + 1, par3 + 1, par4, 5)))))))))));
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }
    
    public static int getOrientation(int par0) {
        return par0 & 7;
    }
    
    public static int determineOrientation(World par0World, int par1, int par2, int par3, EntityLivingBase par4EntityLiving) {
        if (MathHelper.abs((float)par4EntityLiving.posX - (float)par1) < 2.0F && MathHelper.abs((float)par4EntityLiving.posZ - (float)par3) < 2.0F)
        {
            double d0 = par4EntityLiving.posY + 1.82D - (double)par4EntityLiving.yOffset;

            if (d0 - (double)par2 > 2.0D)
            {
                return 1;
            }

            if ((double)par2 - d0 > 0.0D)
            {
                return 0;
            }
        }

        int l = MathHelper.floor_double((double)(par4EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }
    
    public void updateIgniterState(World par1World, int par2, int par3, int par4) {
        int metadata = par1World.getBlockMetadata(par2, par3, par4);
        int direction = getOrientation(metadata);
        TileEntityIgniter igniter = (TileEntityIgniter)par1World.getTileEntity(par2, par3, par4);

        if (direction != 7) {
            boolean flag = this.isIndirectlyPowered(par1World, par2, par3, par4, direction);
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
        }
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityIgniter();
	}
	
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
    }
}
