package trapcraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import trapcraft.tileentity.TileEntityFan;

/**
 * @author ProPercivalalb
 **/
public class BlockFan extends BlockContainer {
	
	public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyDirection FACING = BlockDirectional.FACING;
	
    public BlockFan() {
        super(Material.ROCK);
        this.setHardness(0.8F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.WEST).withProperty(POWERED, false));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFan();
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)).withProperty(POWERED, worldIn.isBlockPowered(pos)), 2);
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
		return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta = meta | ((EnumFacing) state.getValue(FACING)).getIndex();

		if (((Boolean) state.getValue(POWERED)).booleanValue())
			meta |= 8;

		return meta;
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
        return new BlockStateContainer(this, new IProperty[] {FACING, POWERED});
    }
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if(!worldIn.isRemote) {
			if(state.getValue(POWERED) && !worldIn.isBlockPowered(pos))
				worldIn.scheduleUpdate(pos, this, 4);
			else if(!state.getValue(POWERED) && worldIn.isBlockPowered(pos))
				worldIn.setBlockState(pos, state.cycleProperty(POWERED), 3);
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if(!world.isRemote)
			if(state.getValue(POWERED) && !world.isBlockPowered(pos))
				world.setBlockState(pos, state.cycleProperty(POWERED), 3);

	}
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
 
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

            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
