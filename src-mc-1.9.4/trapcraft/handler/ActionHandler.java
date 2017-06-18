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
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import trapcraft.entity.EntityDummy;

/**
 * @author ProPercivalalb
 **/
public class ActionHandler {

	@SubscribeEvent
	public void action(PlayerInteractEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		World world = player.worldObj;
		BlockPos pos = event.getPos();
		
		Action action = event.getAction();
		EnumFacing face = event.getFace();
		FMLLog.info("OUtput");
		FMLLog.info("Rightclick");
		if(!world.isRemote && action == Action.RIGHT_CLICK_BLOCK) {
			FMLLog.info("Rightclick");
			ItemStack item = player.getHeldItemMainhand();
			if(item != null && item.getItem() == Items.skull && item.getItemDamage() == 3) {
				FMLLog.info("OUtput");
				if(face != null) {
					BlockPos tPos = pos.up(face.getFrontOffsetY());
		
		
					if(world.getBlockState(tPos).getBlock() == Blocks.air) {
						if(world.getBlockState(tPos.down()).getBlock() == Blocks.planks && world.getBlockState(tPos.down(2)).getBlock() == Blocks.planks) {
							if(!player.canPlayerEdit(tPos, face, item) || !Blocks.skull.canPlaceBlockAt(world, tPos))
				                return;
				            else {
				                world.setBlockState(tPos, Blocks.skull.getDefaultState().withProperty(Blocks.skull.FACING, player.getHorizontalFacing()).withProperty(Blocks.skull.NODROP, Boolean.valueOf(false)));
				                int i1 = 0;
				                if (face == EnumFacing.UP) {
				                    i1 = MathHelper.floor_double((double)(player.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
				                }
				                /**
				                TileEntity tileentity = world.getTileEntity(x, y, z);
				                if (tileentity != null && tileentity instanceof TileEntitySkull) {

				                    GameProfile gameprofile = null;

			                        if (item.hasTagCompound()) {
			                            NBTTagCompound nbttagcompound = item.getTagCompound();

			                            if (nbttagcompound.hasKey("SkullOwner", 10))
			                                gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
			                            else if (nbttagcompound.hasKey("SkullOwner", 8) && nbttagcompound.getString("SkullOwner").length() > 0)
			                                gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
			                        }

			                        ((TileEntitySkull)tileentity).func_152106_a(gameprofile);
				                    ((TileEntitySkull)tileentity).func_145903_a(i1);
				                }
				                **/
				                
				                if(!player.capabilities.isCreativeMode)
				                	--item.stackSize;
				                
				                world.setBlockToAir(tPos);
				        		world.setBlockToAir(tPos.down());
				        		world.setBlockToAir(tPos.down(2));
				        		float rotation = player.rotationYaw + 180F;

				        		if (rotation >= 360F) {
				        			rotation -= 360F;
				        		}

				        		EntityDummy entitydummy = new EntityDummy(world);
				        		entitydummy.setLocationAndAngles((double)tPos.getX() + 0.5D, (double)tPos.getY() - 1.95D, (double)tPos.getZ() + 0.5D, rotation, 0.0F);
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
