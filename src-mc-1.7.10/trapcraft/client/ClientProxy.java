package trapcraft.client;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;
import trapcraft.client.renders.ItemRenderHelperBearTrap;
import trapcraft.client.renders.ItemRenderHelperFan;
import trapcraft.client.renders.ItemRenderHelperGrassCovering;
import trapcraft.client.renders.ItemRenderHelperMagneticChest;
import trapcraft.client.renders.RenderDummy;
import trapcraft.client.renders.TileEntityMagneticChestRenderer;
import trapcraft.common.CommonProxy;
import trapcraft.common.entity.EntityDummy;
import trapcraft.common.tileentitys.TileEntityMagneticChest;
import trapcraft.common.tileentitys.TileEntityTC;

/**
 * @author ProPercivalalb
 **/
public class ClientProxy extends CommonProxy {
    
	@Override
	public void onModPre() {
		
    }
	
	@Override
    public void onModLoad() {
       
    }
    
	@Override
    public void onModPost(){
		Properties.RENDER_ID_MAGNETIC_CHEST = RenderingRegistry.getNextAvailableRenderId();
		Properties.RENDER_ID_FAN = RenderingRegistry.getNextAvailableRenderId();
		Properties.RENDER_ID_GRASS_COVERING = RenderingRegistry.getNextAvailableRenderId();
		Properties.RENDER_ID_BEAR_TRAP = RenderingRegistry.getNextAvailableRenderId();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMagneticChest.class, new TileEntityMagneticChestRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TrapcraftMod.magneticChest), new ItemRenderHelperMagneticChest());
		RenderingRegistry.registerBlockHandler(Properties.RENDER_ID_FAN, new ItemRenderHelperFan());
		RenderingRegistry.registerBlockHandler(Properties.RENDER_ID_GRASS_COVERING, new ItemRenderHelperGrassCovering());
		RenderingRegistry.registerBlockHandler(Properties.RENDER_ID_BEAR_TRAP, new ItemRenderHelperBearTrap());
		FMLCommonHandler.instance().bus().register(new ClientTickHandler());
		RenderingRegistry.registerEntityRenderingHandler(EntityDummy.class, new RenderDummy(new ModelBiped(0.0F), 0.5F, Properties.RES_MOB_DUMMY));
	}
	
	@Override
	public EntityPlayer getClientPlayer() {
		return FMLClientHandler.instance().getClientPlayerEntity();
	}
	
	@Override
    public void handleTileEntityPacket(int x, int y, int z, ForgeDirection direction, String owner, String customName, String state) {
        TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(x, y, z);

        if (tileEntity != null) {
            if (tileEntity instanceof TileEntityTC) {
                ((TileEntityTC)tileEntity).setDirection(direction);
                ((TileEntityTC)tileEntity).setState(state);
                ((TileEntityTC)tileEntity).setOwner(owner);
                ((TileEntityTC)tileEntity).setInvName(customName);
            }
        }
    }
}
