package trapcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import trapcraft.api.Properties;
import trapcraft.tileentity.TileEntityBearTrap;

public class BlockBearTrap extends BlockContainer {

	private IIcon iconItem;
	
	public BlockBearTrap() {
		super(Material.iron);
		this.setStepSound(SoundType.METAL);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.15F, 1.0F);
	}
	
	@Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
    	int metadata = par1World.getBlockMetadata(par2, par3, par4);
		if(metadata == 1) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 3);
	    	return true;
		}
    	return false;
    }
	
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
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
	    return Properties.RENDER_ID_BEAR_TRAP;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityBearTrap();
	}
	
	@Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
        if ((entity instanceof EntityPlayer) || !(entity instanceof EntityLiving)) {
            return;
        }

        EntityLiving entityliving = (EntityLiving)entity;
        world.setBlockMetadataWithNotify(i, j, k, 1, 3);
        TileEntityBearTrap tileentitybeartrap = (TileEntityBearTrap)world.getTileEntity(i, j, k);
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
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        if (!super.canPlaceBlockAt(world, i, j, k))
        {
            return false;
        }
        else
        {
            return canBlockStay(world, i, j, k);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block l) {
        if (!canBlockStay(world, i, j, k)) {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockToAir(i, j, k);
        }
    }

    @Override
    public boolean canBlockStay(World world, int i, int j, int k) {
        return world.getBlock(i, j - 1, k).getMaterial().isSolid() && world.getBlock(i, j - 1, k) != this;
    }
    
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(Properties.TEX_PACKAGE + "bearTrap");
        this.iconItem = par1IconRegister.registerIcon(Properties.TEX_PACKAGE + "bearTrapItem");
    }
    
    @Override
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return this.blockIcon;
    }
    
    @Override
    public IIcon getIcon(int par1, int par2) {
        return iconItem;
    }
}
