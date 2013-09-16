package trapcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;
import trapcraft.common.tileentitys.TileEntityFan;
import trapcraft.common.tileentitys.TileEntityTC;
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
import net.minecraftforge.common.ForgeDirection;

/**
 * @author ProPercivalalb
 * @author need4speed402
 * You may look at this file to gain knowledge of java                    
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        int rotation = 0;
        
        if (Math.abs(player.rotationPitch) > 90D / 2D){
            if (player.rotationPitch > 0){
                rotation = 1;
            }else if (player.rotationPitch < 0){
                rotation = 0;
            }
        }else{
            rotation = Direction.directionToFacing[Direction.rotateOpposite[Math.round(player.rotationYaw / 90) & 3]];
        }
		
        world.setBlockMetadataWithNotify(x, y, z, rotation, 2);
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
    public void setBlockBoundsForItemRender() {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
 
           /* TileEntityFan var10 = (TileEntityFan)par1World.getBlockTileEntity(par2, par3, par4);

            if (var10 != null)
            {
                if(par5EntityPlayer.getCurrentEquippedItem().itemID == TrapcraftMod.upgradeRange.itemID)
                {
                	var10.extraRange = var10.extraRange + 0.5D;
                	par5EntityPlayer.getCurrentEquippedItem().stackSize--;
                	double totalRange = var10.extraRange + 5.0D;
                	if(!par1World.isRemote)
                	{
                		par5EntityPlayer.addChatMessage("Fan: Range is " + totalRange + " blocks.");
                	}
                    return true;
                }
                else if(par5EntityPlayer.getCurrentEquippedItem().itemID == TrapcraftMod.upgradeSpeed.itemID)
                {
                	var10.speed = var10.speed + 0.2F;
                	par5EntityPlayer.getCurrentEquippedItem().stackSize--;
                	double totalSpeed = var10.speed;
                	if(!par1World.isRemote)
                	{
                		par5EntityPlayer.addChatMessage("Fan: Speed is about " + totalSpeed + " blocks per second.");
                	}
                    return true;
                }
            }*/

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
