package trapcraft.proxy;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderCaveSpider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;
import trapcraft.client.renders.ItemRenderHelperBearTrap;
import trapcraft.client.renders.ItemRenderHelperFan;
import trapcraft.client.renders.ItemRenderHelperGrassCovering;
import trapcraft.client.renders.ItemRenderHelperMagneticChest;
import trapcraft.client.renders.RenderDummy;
import trapcraft.client.renders.TileEntityMagneticChestRenderer;
import trapcraft.entity.EntityDummy;
import trapcraft.handler.ClientTickHandler;
import trapcraft.handler.ModelBakeHandler;
import trapcraft.helper.ModelHelper;
import trapcraft.tileentity.TileEntityMagneticChest;
import trapcraft.tileentity.TileEntityTC;

/**
 * @author ProPercivalalb
 **/
public class ClientProxy extends CommonProxy {
    
	@Override
	public void onModPre() {
		MinecraftForge.EVENT_BUS.register(new ModelBakeHandler());
    }
	
	@Override
    public void onModLoad() {
       
    }
    
	@Override
    public void onModPost(){
		//ModelHelper.registerItem(TrapcraftMod.magneticChest);
		//ModelLoader.setCustomModelResourceLocation(StartupCommon.itemSimple, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(TrapcraftMod.magneticChest), 0, TileEntityMagneticChest.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMagneticChest.class, new TileEntityMagneticChestRenderer());
		//MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TrapcraftMod.magneticChest), new ItemRenderHelperMagneticChest());
		//RenderingRegistry.registerBlockHandler(Properties.RENDER_ID_FAN, new ItemRenderHelperFan());
		//RenderingRegistry.registerBlockHandler(Properties.RENDER_ID_GRASS_COVERING, new ItemRenderHelperGrassCovering());
		//RenderingRegistry.registerBlockHandler(Properties.RENDER_ID_BEAR_TRAP, new ItemRenderHelperBearTrap());
		MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
		Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap = Maps.<Class<? extends Entity >, Render<? extends Entity>>newHashMap();
		entityRenderMap.put(EntityCaveSpider.class, new RenderDummy(Minecraft.getMinecraft().getRenderManager(), new ModelBiped(0.0F), 0.5F, Properties.RES_MOB_DUMMY));
		RenderingRegistry.loadEntityRenderers(entityRenderMap);
		
		ModelHelper.registerBlock(TrapcraftMod.magneticChest, "trapcraft:magnetic_chest");
	}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}
	
	@Override
	public EntityPlayer getPlayerEntity() {
		return Minecraft.getMinecraft().thePlayer;
	}
	
	@Override
	public IThreadListener getThreadFromContext(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx));
	}
	
	@Override
    public void handleTileEntityPacket(BlockPos pos, EnumFacing facing, String owner, String customName, String state) {
        TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(pos);

        if (tileEntity != null) {
            if (tileEntity instanceof TileEntityTC) {
                ((TileEntityTC)tileEntity).setState(state);
                ((TileEntityTC)tileEntity).setOwner(owner);
                ((TileEntityTC)tileEntity).setInvName(customName);
            }
        }
    }
}
