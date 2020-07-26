package trapcraft.handler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
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
                if(face != null) {
                    BlockPos tPos = pos.up(face.getYOffset());


                    if(world.isAirBlock(tPos)) {
                        IBlockState variant = world.getBlockState(tPos.down());
                        IBlockState test = world.getBlockState(tPos.down(2));

                        if(variant.getBlock() == Blocks.PLANKS && test.getBlock() == Blocks.PLANKS && Blocks.PLANKS.getMetaFromState(variant) == Blocks.PLANKS.getMetaFromState(test)) {
                            if(!player.canPlayerEdit(tPos, face, item) || !Blocks.SKULL.canPlaceBlockAt(world, tPos))
                                return;
                            else {
                                world.setBlockState(tPos, Blocks.SKULL.getDefaultState().withProperty(Blocks.SKULL.FACING, player.getHorizontalFacing()).withProperty(Blocks.SKULL.NODROP, Boolean.valueOf(false)));

                                if(!player.capabilities.isCreativeMode)
                                    item.shrink(1);

                                world.setBlockToAir(tPos);
                                world.setBlockToAir(tPos.down());
                                world.setBlockToAir(tPos.down(2));
                                float rotation = player.rotationYaw + 180F;

                                if (rotation >= 360F) {
                                    rotation -= 360F;
                                }

                                EntityDummy entitydummy = new EntityDummy(world);
                                entitydummy.setVariant((byte)variant.getBlock().getMetaFromState(variant));
                                entitydummy.setLocationAndAngles((double)tPos.getX() + 0.5D, (double)tPos.getY() - 1.95D, (double)tPos.getZ() + 0.5D, rotation, 0.0F);
                                world.spawnEntity(entitydummy);

                                event.setCanceled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();

        if(entity instanceof EntityMob) {
            EntityMob mob = (EntityMob)entity;
            mob.targetTasks.addTask(0, new EntityAINearestAttackableTarget<>(mob, EntityDummy.class, 10, true, false, (dummy) -> {
              return Math.abs(dummy.posY - mob.posY) <= 6.0D;
           }));
        }
    }

    public static void spawnDummy(World world, BlockPos tPos, float rotation, byte variant) {
        EntityDummy entitydummy = new EntityDummy(world);
        entitydummy.setVariant(variant);
        entitydummy.setLocationAndAngles((double)tPos.getX() + 0.5D, (double)tPos.getY() - 1.95D, (double)tPos.getZ() + 0.5D, MathHelper.wrapDegrees(rotation), 0.0F);
        world.spawnEntity(entitydummy);
    }
}
