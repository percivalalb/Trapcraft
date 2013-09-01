package trapcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;
import trapcraft.common.tileentitys.TileEntityFan;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Direction;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class BlockFan extends BlockContainer {
	
	public Icon iconSide;
	
    public BlockFan(int i) {
        super(i, Material.rock);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    /**
     * Returns the TileEntity used by this block.
     */
    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileEntityFan();
    }

    public static int getOrientation(int par1) {
        return par1 & 7;
    }
    
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    @Override
    public Icon getIcon(int par1, int par2)
    {
        int var1 = getOrientation(par2);

        if (var1 > 5)
        {
            return this.blockIcon;
        }

        if (par1 == var1)
        {
            return this.blockIcon;
        }
        else
        {
        	return par1 == BlockIgniter.faceToSide[var1] ? iconSide : iconSide;
           // return i == Facing.faceToSide[k] ? mod_Trap.fanSide : mod_Trap.fanSide;
        }
    }
    
    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack) {
        int rotation = determineOrientation(par1World, x, y, z, par5EntityLiving);
        par1World.setBlockMetadataWithNotify(x, y, z, rotation, 2);
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
    public void setBlockBoundsForItemRender() {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    private static int determineOrientation(World world, int i, int j, int k, EntityLivingBase entityplayer) {
        if (MathHelper.abs((float)entityplayer.posX - (float)i) < 2.0F && MathHelper.abs((float)entityplayer.posZ - (float)k) < 2.0F)
        {
            double d = (entityplayer.posY + 1.8200000000000001D) - (double)entityplayer.yOffset;

            if (d - (double)j > 2D)
            {
                return 1;
            }

            if ((double)j - d > 0.0D)
            {
                return 0;
            }
        }

        int l = MathHelper.floor_double((double)((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;

        if (l == 0)
        {
            return 2;
        }

        if (l == 1)
        {
            return 5;
        }

        if (l == 2)
        {
            return 3;
        }
        else
        {
            return l == 3 ? 4 : 0;
        }
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
 
            TileEntityFan var10 = (TileEntityFan)par1World.getBlockTileEntity(par2, par3, par4);

            if (var10 != null)
            {
                //if(par5EntityPlayer.getCurrentEquippedItem().itemID == TrapcraftMod.upgradeRange.itemID)
                //{
                //	var10.extraRange = var10.extraRange + 0.5D;
                //	par5EntityPlayer.getCurrentEquippedItem().stackSize--;
                //	double totalRange = var10.extraRange + 5.0D;
                //	if(!par1World.isRemote)
                //	{
                //		par5EntityPlayer.addChatMessage("Fan: Range is " + totalRange + " blocks.");
                //	}
                //    return true;
                //}
                //else if(par5EntityPlayer.getCurrentEquippedItem().itemID == TrapcraftMod.upgradeSpeed.itemID)
                //{
                //	var10.speed = var10.speed + 0.2F;
                //	par5EntityPlayer.getCurrentEquippedItem().stackSize--;
                //	double totalSpeed = var10.speed;
                //	if(!par1World.isRemote)
                //	{
                //		par5EntityPlayer.addChatMessage("Fan: Speed is about " + totalSpeed + " blocks per second.");
                //	}
                //    return true;
                //}
            }

            return false;
    }
    
    @Override
    public void registerIcons(IconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(Properties.TEX_PACKAGE + "fanTop");
        this.iconSide = par1IconRegister.registerIcon(Properties.TEX_PACKAGE + "fanSide");
    }
    
    @Override
    public int getRenderType() {
        return Properties.RENDER_ID_FAN;
    }
}
