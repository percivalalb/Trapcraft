package trapcraft.common.handler;

import java.util.logging.Level;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import trapcraft.api.Properties;
import trapcraft.common.entity.EntityDummy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class ActionHandler {

	@SubscribeEvent
	public void action(PlayerInteractEvent event) {
		if(!event.entityPlayer.worldObj.isRemote && event.action == Action.RIGHT_CLICK_BLOCK) {
			ItemStack item = event.entityPlayer.getCurrentEquippedItem();
			if(item != null && item.getItem() == Items.skull && item.getItemDamage() == 3) {
				if(event.face != -1) {
					int x = event.x;
					int y = Facing.offsetsYForSide[event.face] + event.y;
					int z = event.z;
					World world = event.entityPlayer.worldObj;
					EntityPlayer player = event.entityPlayer;
					if(world.getBlock(x, y, z) == Blocks.air) {
						if(world.getBlock(x, y - 1, z) == Blocks.planks && world.getBlock(x, y - 2, z) == Blocks.planks) {
							if (!player.canPlayerEdit(x, y, z, event.face, item)) {
				                return;
				            }
				            else if (!Blocks.skull.canPlaceBlockAt(world, x, y, z)) {
				                return;
				            }
				            else {
				                world.setBlock(x, y, z, Blocks.skull, event.face, 2);
				                int i1 = 0;
				                if (event.face == 1) {
				                    i1 = MathHelper.floor_double((double)(player.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
				                }
				                TileEntity tileentity = world.getTileEntity(x, y, z);
				                if (tileentity != null && tileentity instanceof TileEntitySkull) {
				                    String s = "";
				                    if (item.hasTagCompound() && item.getTagCompound().hasKey("SkullOwner")) {
				                        s = item.getTagCompound().getString("SkullOwner");
				                    }
				                    ((TileEntitySkull)tileentity).func_145905_a(item.getItemDamage(), s);
				                    ((TileEntitySkull)tileentity).func_145903_a(i1);
				                }
				                
				                
				                if(!event.entityPlayer.capabilities.isCreativeMode)
				                	--item.stackSize;
				                
				                world.setBlockToAir(x, y, z);
				        		world.setBlockToAir(x, y - 1, z);
				        		world.setBlockToAir(x, y - 2, z);
				        		float rotation = player.rotationYaw + 180F;

				        		if (rotation >= 360F) {
				        			rotation -= 360F;
				        		}

				        		EntityDummy entitydummy = new EntityDummy(world);
				        		entitydummy.setLocationAndAngles((double)x + 0.5D, (double)y - 1.95D, (double)z + 0.5D, rotation, 0.0F);
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
