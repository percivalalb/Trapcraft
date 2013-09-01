package trapcraft.common.handler;

import java.util.logging.Level;

import trapcraft.api.Properties;
import trapcraft.common.entity.EntityDummy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class ActionHandler {

	@ForgeSubscribe
	public void action(PlayerInteractEvent var1) {
		if(!var1.entityPlayer.worldObj.isRemote && var1.action == Action.RIGHT_CLICK_BLOCK) {
			ItemStack item = var1.entityPlayer.getCurrentEquippedItem();
			if(item != null && item.itemID == Item.skull.itemID && item.getItemDamage() == 3) {
				if(var1.face != -1) {
					int x = var1.x;
					int y = Facing.offsetsYForSide[var1.face] + var1.y;
					int z = var1.z;
					World world = var1.entityPlayer.worldObj;
					EntityPlayer player = var1.entityPlayer;
					if(world.getBlockId(x, y, z) == 0) {
						if(world.getBlockId(x, y - 1, z) == Block.planks.blockID && world.getBlockId(x, y - 2, z) == Block.planks.blockID) {
							if (!player.canPlayerEdit(x, y, z, var1.face, item)) {
				                return;
				            }
				            else if (!Block.skull.canPlaceBlockAt(world, x, y, z)) {
				                return;
				            }
				            else {
				                world.setBlock(x, y, z, Block.skull.blockID, var1.face, 2);
				                int i1 = 0;
				                if (var1.face == 1) {
				                    i1 = MathHelper.floor_double((double)(player.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
				                }
				                TileEntity tileentity = world.getBlockTileEntity(x, y, z);
				                if (tileentity != null && tileentity instanceof TileEntitySkull) {
				                    String s = "";
				                    if (item.hasTagCompound() && item.getTagCompound().hasKey("SkullOwner")) {
				                        s = item.getTagCompound().getString("SkullOwner");
				                    }
				                    ((TileEntitySkull)tileentity).setSkullType(item.getItemDamage(), s);
				                    ((TileEntitySkull)tileentity).setSkullRotation(i1);
				                }
				                --item.stackSize;
				                
				                Properties.logger.log(Level.INFO, "Made Dummy");
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
				                var1.setCanceled(true);
				            }
						}
					}
				}
			}
		}
	}
}
