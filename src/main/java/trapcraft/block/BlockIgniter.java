package trapcraft.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import trapcraft.TrapcraftMod;
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
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.WEST));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) {
            return true;
        }
        else {
            TileEntityIgniter tileentityigniter = (TileEntityIgniter)worldIn.getTileEntity(pos);

            if (tileentityigniter != null) {
                playerIn.openGui(TrapcraftMod.INSTANCE, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if(world instanceof World && !((World)world).isRemote)
            this.updateIgniterState((World)world, pos);
    }


    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            this.updateIgniterState(worldIn, pos);
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);

        if (!worldIn.isRemote) {
            this.updateIgniterState(worldIn, pos);
        }
    }

    public static EnumFacing getFacing(int meta) {
        return EnumFacing.byIndex(meta & 7);
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
        EnumFacing facing = world.getBlockState(pos).getValue(FACING);

        int distance = 1, oldDistance = 1;
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof TileEntityIgniter) {
            TileEntityIgniter igniter = (TileEntityIgniter)world.getTileEntity(pos);
            distance = igniter.getRangeUpgrades() + 1;
            oldDistance = igniter.lastUpgrades + 1;
        }

        updateIgniterState(world, pos, world.isBlockPowered(pos), facing, distance, oldDistance);

        if(tileEntity instanceof TileEntityIgniter) {
            TileEntityIgniter igniter = (TileEntityIgniter)world.getTileEntity(pos);
            igniter.lastUpgrades = distance - 1;
        }
     }

     private void updateIgniterState(World world, BlockPos pos, boolean powered, EnumFacing direction, int newDistance, int previousDistance) {
         // If distance has changed remove old fire
         if(newDistance != previousDistance) {
              BlockPos oldPos = pos.offset(direction, previousDistance);
              removePossibleFire(world, oldPos);
         }

         BlockPos firePos = pos.offset(direction, newDistance);

         if(powered) {
             if(world.isAirBlock(firePos)) {
                 world.setBlockState(firePos, Blocks.FIRE.getDefaultState());
             }
         }
         else if(!powered) {
             removePossibleFire(world, firePos);
         }
      }

     public void removePossibleFire(World world, BlockPos pos) {
         if(world.getBlockState(pos).getBlock() == Blocks.FIRE) {
             world.setBlockState(pos, Blocks.AIR.getDefaultState());
             world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5F, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F, true);
         }
     }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityIgniter();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if(tileentity instanceof TileEntityIgniter) {
            int upgrades = ((TileEntityIgniter) tileentity).getRangeUpgrades() + 1;
            updateIgniterState(worldIn, pos, false, state.getValue(FACING), upgrades, upgrades);

            InventoryHelper.dropInventoryItems(worldIn, pos, ((TileEntityIgniter)tileentity).inventory);
        }

        super.breakBlock(worldIn, pos, state);
    }
}
