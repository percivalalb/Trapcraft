package trapcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class BlockSpikes extends Block {
	
    public BlockSpikes() {
        super(Material.iron);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        float f = 0.0625F;
        return AxisAlignedBB.getBoundingBox((float)i + f, (float)j + f, (float)k + f, (float)(i + 1) - f, (float)(j + 1) - f - 0.35F, (float)(k + 1) - f);
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
        return 1;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k) {
        return canBlockStay(world, i, j, k);
    }
    
    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block block) {
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
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
        if (entity instanceof EntityItem) {
            return;
        }

        if (entity.fallDistance >= 5F) {
            entity.attackEntityFrom(DamageSource.fall, 20);
            return;
        }
        else {
            double motionX = entity.motionX;
            double motionY = entity.motionY;
            double motionZ = entity.motionZ;
            int damageTodo = (int)((motionX + motionY + motionZ) / 1.5D);
            entity.attackEntityFrom(DamageSource.generic, 2 + damageTodo);
            return;
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(Properties.TEX_PACKAGE + "spikes");
    }
}
