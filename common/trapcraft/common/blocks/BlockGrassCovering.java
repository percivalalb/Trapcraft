package trapcraft.common.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class BlockGrassCovering extends Block {
    
	private static Icon iconGrassCoveringItem;
	
	public BlockGrassCovering(int par1) {
        super(par1, Material.grass);
        setTickRandomly(true);
        setBlockBounds(0.0F, 0.95F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
 
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        float f = 0.0625F;
        return AxisAlignedBB.getBoundingBox(i + 0.0F + f, j + 0.95F + f, k + 0.0F + f, i + 1.0F - f, j + 1.0F - f, k + 1.0F - f);
    } 
    
	@Override
	public boolean renderAsNormalBlock() {
        return false;
    }
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k) {
        return canBlockStay(world, i, j, k);
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
        if (!canBlockStay(world, i, j, k)) {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockToAir(i, j, k);
        }
    }

    @Override
    public boolean canBlockStay(World world, int i, int j, int k) {
        boolean flag = false;

        if (world.getBlockMaterial(i, j, k - 1).isSolid() || world.getBlockId(i, j, k - 1) == blockID) {
            flag = true;
        }

        if (world.getBlockMaterial(i, j, k + 1).isSolid() || world.getBlockId(i, j, k + 1) == blockID) {
            flag = true;
        }

        if (world.getBlockMaterial(i - 1, j, k).isSolid() || world.getBlockId(i - 1, j, k) == blockID) {
            flag = true;
        }

        if (world.getBlockMaterial(i + 1, j, k).isSolid() || world.getBlockId(i + 1, j, k) == blockID) {
            flag = true;
        }

        return flag;
    }

    @Override
    public int getBlockColor() {
        double d = 0.5D;
        double d1 = 1.0D;
        return ColorizerGrass.getGrassColor(d, d1);
    }

    @Override
    public int getRenderColor(int i) {
    	return this.getBlockColor();
    }
    
    

    @Override
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int k1 = -1; k1 <= 1; ++k1)
        {
            for (int l1 = -1; l1 <= 1; ++l1)
            {
                int i2 = par1IBlockAccess.getBiomeGenForCoords(par2 + l1, par4 + k1).getBiomeGrassColor();
                l += (i2 & 16711680) >> 16;
                i1 += (i2 & 65280) >> 8;
                j1 += i2 & 255;
            }
        }

        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
    	if (entity instanceof EntityLivingBase && !world.isRemote)
        {
            world.setBlockToAir(x, y, z);

            for (int l = 0; l < 2; l++) {
                float f = 0.7F;
                float f1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                float f2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                float f3 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                EntityItem entityitem = new EntityItem(world, (double)x + f1, (double)y + f2, (double)z + f3, new ItemStack(Item.stick));
                entityitem.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(entityitem);
            }
        }
    }
    
    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
    
    }
    
    @Override
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return grass.getBlockTexture(par1IBlockAccess, par2, par3, par4, 1);
    }
    
    @Override
    public Icon getIcon(int side, int meta) {
        return iconGrassCoveringItem;
    }
    
    @Override
    public int getRenderType() {
        return Properties.RENDER_ID_GRASS_COVERING;
    }
    
    @Override
    public void registerIcons(IconRegister iconRegister) {
        this.iconGrassCoveringItem = iconRegister.registerIcon(Properties.TEX_PACKAGE + "grassCovering");
    }
}
