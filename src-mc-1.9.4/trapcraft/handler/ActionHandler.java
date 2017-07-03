package trapcraft.handler;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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

		if(!world.isRemote) {
			
			ItemStack item = player.getHeldItemMainhand();
			if(item != null && item.getItem() == Items.SKULL && item.getItemDamage() == 3) {
				FMLLog.info("OUtput");
				if(face != null) {
					BlockPos tPos = pos.up(face.getFrontOffsetY());
					
		
					if(world.isAirBlock(tPos)) {
						if(world.getBlockState(tPos.down()).getBlock() == Blocks.PLANKS && world.getBlockState(tPos.down(2)).getBlock() == Blocks.PLANKS) {
							if(!player.canPlayerEdit(tPos, face, item) || !Blocks.SKULL.canPlaceBlockAt(world, tPos))
				                return;
				            else {
				                world.setBlockState(tPos, Blocks.SKULL.getDefaultState().withProperty(Blocks.SKULL.FACING, player.getHorizontalFacing()).withProperty(Blocks.SKULL.NODROP, Boolean.valueOf(false)));
				                int i1 = 0;
				                if (face == EnumFacing.UP) {
				                    i1 = MathHelper.floor_double((double)(player.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
				                }
		
				                if(!player.capabilities.isCreativeMode)
				                	--item.stackSize;
				                
				                world.setBlockToAir(tPos);
				        		world.setBlockToAir(tPos.down());
				        		world.setBlockToAir(tPos.down(2));
				        		float rotation = player.rotationYaw;
				        		
				        		if (rotation >= 360F) {
				        			rotation -= 360F;
				        		}

				        		EntityDummy entitydummy = new EntityDummy(world);
				        		entitydummy.setLocationAndAngles((double)tPos.getX() + 0.5D, (double)tPos.getY() - 1.95D, (double)tPos.getZ() + 0.5D, 50F, 0.0F);
				        		world.spawnEntityInWorld(entitydummy);
				                event.setCanceled(true);
				            }
						}
					}
				}
			}
		}
	}
}
