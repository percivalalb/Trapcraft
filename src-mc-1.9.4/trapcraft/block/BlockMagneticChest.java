package trapcraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
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
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import trapcraft.api.Properties;
import trapcraft.tileentity.TileEntityMagneticChest;
import trapcraft.tileentity.TileEntityTC;

/**
 * @author ProPercivalalb
 **/
public class BlockMagneticChest extends BlockContainer {

    private Random rand = new Random();
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    protected static final AxisAlignedBB field_185569_b = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
    
    
    public BlockMagneticChest() {
        super(Material.WOOD);
        this.setHardness(2.5F);
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.WEST));
    }

    private boolean isBlocked(World worldIn, BlockPos pos) {
        return this.isBelowSolidBlock(worldIn, pos) || this.isOcelotSittingOnChest(worldIn, pos);
    }

    private boolean isBelowSolidBlock(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.up()).isSideSolid(worldIn, pos.up(), EnumFacing.DOWN);
    }

    private boolean isOcelotSittingOnChest(World worldIn, BlockPos pos) {
        for (Entity entity : worldIn.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB((double)pos.getX(), (double)(pos.getY() + 1), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 2), (double)(pos.getZ() + 1))))  {
            EntityOcelot entityocelot = (EntityOcelot)entity;

            if (entityocelot.isSitting())
                return true;
        }

        return false;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory(this.getLockableContainer(worldIn, pos));
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
            enumfacing = EnumFacing.NORTH;

        return this.getDefaultState().withProperty(FACING, enumfacing);
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
    
    @Override
    public TileEntity createNewTileEntity(World world, int meat) {
        return new TileEntityMagneticChest();
    }
    
    @Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if(entity.isEntityAlive() && !world.isRemote) {
            if (entity instanceof EntityItem) {
                TileEntityMagneticChest tileEntityMagneticChest = (TileEntityMagneticChest)world.getTileEntity(pos);
                tileEntityMagneticChest.insertStackFromEntity((EntityItem)entity);
            }
        }
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return field_185569_b;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if(tileentity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if(playerIn.isSneaking())
    		return true;
    	else if(worldIn.isSideSolid(pos.up(), EnumFacing.DOWN))
            return true;
    	else {
    		ILockableContainer ilockablecontainer = this.getLockableContainer(worldIn, pos);

            if(ilockablecontainer != null) {
                playerIn.displayGUIChest(ilockablecontainer);
                playerIn.addStat(StatList.CHEST_OPENED);
            }

            return true;
        }
    }
    
    public ILockableContainer getLockableContainer(World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (!(tileentity instanceof TileEntityMagneticChest))
        {
            return null;
        }
        else
        {
        	TileEntityMagneticChest ilockablecontainer = (TileEntityMagneticChest)tileentity;

            if (this.isBlocked(worldIn, pos))
            {
                return null;
            }
            else
            {
                

                return ilockablecontainer;
            }
        }
    }
    
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }
}
