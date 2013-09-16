package trapcraft.common.blocks;

import java.util.Random;

import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;
import trapcraft.common.tileentitys.TileEntityMagneticChest;
import trapcraft.common.tileentitys.TileEntityTC;
import need4speed402.mods.barrels.TileEntityBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class BlockMagneticChest extends BlockContainer {

    private Random rand = new Random();

    public BlockMagneticChest(int id) {

        super(id, Material.wood);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityMagneticChest();
    }
    
    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
        if (par5Entity.isEntityAlive() && !par1World.isRemote) {
            if (par5Entity instanceof EntityItem)
            {
                TileEntityMagneticChest TileEntityMagneticChest = (TileEntityMagneticChest)par1World.getBlockTileEntity(par2, par3, par4);
                TileEntityMagneticChest.addItem(TileEntityMagneticChest, (EntityItem)par5Entity);
            }
        }
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
    public int getRenderType() {
        return Properties.RENDER_ID_MAGNETIC_CHEST;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int id, int meta) {
        dropInventory(world, x, y, z);
        super.breakBlock(world, x, y, z, id, meta);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (player.isSneaking())
            return true;
        else if (world.isBlockSolidOnSide(x, y + 1, z, ForgeDirection.DOWN))
            return true;
        else {
            if (!world.isRemote) {
            	TileEntityMagneticChest tileMagneticChest = (TileEntityMagneticChest) world.getBlockTileEntity(x, y, z);

                if (tileMagneticChest != null) {
                	player.displayGUIChest(tileMagneticChest);
                }
            }

            return true;
        }
    }

    private void dropInventory(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (!(tileEntity instanceof IInventory))
            return;

        IInventory inventory = (IInventory)tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {

            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack != null && itemStack.stackSize > 0) {
                float dX = rand.nextFloat() * 0.8F + 0.1F;
                float dY = rand.nextFloat() * 0.8F + 0.1F;
                float dZ = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, new ItemStack(itemStack.itemID, itemStack.stackSize, itemStack.getItemDamage()));

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
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
    	TileEntityTC tileEntity = (TileEntityTC) world.getBlockTileEntity(x, y, z);
    	
		tileEntity.setDirection(ForgeDirection.VALID_DIRECTIONS[Direction.directionToFacing[Direction.rotateOpposite[Math.round(player.rotationYaw / 90) & 3]]]);

        if (stack.hasDisplayName()) {
            tileEntity.setInvName(stack.getDisplayName());
        }
    }
    
    @Override
    public Icon getIcon(int side, int metadata) {
        return Block.planks.getIcon(side, metadata);
    }
}
