package trapcraft.handler;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import trapcraft.entity.EntityDummy;

/**
 * @author ProPercivalalb
 **/
public class ActionHandler {

	@SubscribeEvent
	public void action(RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		EnumFacing face = event.getFace();

		
			
			ItemStack item = player.getHeldItemMainhand();
			if(item.getItem() == Items.PLAYER_HEAD) {
				if(face == EnumFacing.UP) {
					BlockPos tPos = pos.up(face.getYOffset());
					
		
					if(world.isAirBlock(tPos)) {
						Block top = world.getBlockState(tPos.down()).getBlock();
						Block bottom = world.getBlockState(tPos.down(2)).getBlock();
						List<Block> list = Arrays.asList(Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS);
						
						if(top == bottom && list.contains(top)) {
							if(!player.canPlayerEdit(tPos, face, item) || !Blocks.PLAYER_HEAD.isValidPosition(world.getBlockState(tPos), world, tPos))
				                return;
				            else {
				                //TODO world.setBlockState(tPos, Blocks.PLAYER_HEAD.getDefaultState().withProperty(BlockPlayerHead..FACING, player.getHorizontalFacing()).withProperty(Blocks.PLAYER_HEAD.NODROP, Boolean.valueOf(false)));
		
				                if(!player.abilities.isCreativeMode)
				                	item.shrink(1);
				                
				                world.setBlockState(tPos, Blocks.AIR.getDefaultState());
				        		world.setBlockState(tPos.down(), Blocks.AIR.getDefaultState());
				        		world.setBlockState(tPos.down(2), Blocks.AIR.getDefaultState());
				        		
				        		if(!world.isRemote) {
					        		float rotation = player.rotationYaw + 180F;
					        		
					        		if (rotation >= 360F) {
					        			rotation -= 360F;
					        		}
	
					        		EntityDummy entitydummy = new EntityDummy(world);
					        		entitydummy.setVariant((byte)list.indexOf(top));
					        		entitydummy.setLocationAndAngles((double)tPos.getX() + 0.5D, (double)tPos.getY() - 1.95D, (double)tPos.getZ() + 0.5D, rotation, 0.0F);
					        		world.spawnEntity(entitydummy);
				        	
					        		event.setCanceled(true);
				        		}
				            }
						}
					}
				}
			//}
		}
	}
}
