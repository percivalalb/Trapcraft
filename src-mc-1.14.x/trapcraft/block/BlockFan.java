package trapcraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import trapcraft.tileentity.TileEntityFan;

/**
 * @author ProPercivalalb
 **/
public class BlockFan extends BlockContainer {
	
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final DirectionProperty FACING = BlockDirectional.FACING;
	
    public BlockFan() {
    	super(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 2.0F).sound(SoundType.STONE));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.WEST).with(POWERED, false));
    }
    
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TileEntityFan();
    }
    
    @Override
   	public IBlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        World world = context.getWorld();
    	boolean flag = world.isBlockPowered(blockpos) || world.isBlockPowered(blockpos.up());
    	
   		return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite()).with(POWERED, flag);
   	}
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
	public IBlockState rotate(IBlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate((EnumFacing)state.get(FACING)));
    }

    @Override
	public IBlockState mirror(IBlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation((EnumFacing)state.get(FACING)));
    }

    @Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(FACING, POWERED);
	}
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    	 boolean flag = worldIn.isBlockPowered(pos);
         if (blockIn != this && flag != state.get(POWERED)) {
            worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(flag)), 2);
         }
	}
    
    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
 
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

            return super.onBlockActivated(state, worldIn, pos, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
