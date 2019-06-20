package trapcraft.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import trapcraft.ModBlocks;
import trapcraft.client.renders.RenderDummy;
import trapcraft.client.renders.TileEntityMagneticChestRenderer;
import trapcraft.entity.EntityDummy;
import trapcraft.tileentity.TileEntityMagneticChest;
import trapcraft.tileentity.TileEntityTC;

/**
 * @author ProPercivalalb
 **/
public class ClientProxy extends CommonProxy {
    
	@Override
	public void onModPre() {
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMagneticChest.class, new TileEntityMagneticChestRenderer());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDummy.class, RenderDummy::new);
    }
	
	@Override
    public void onModLoad() {
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> ColorizerGrass.getGrassColor(0.5D, 1.0D), ModBlocks.GRASS_COVERING);
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex) -> (world != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(world, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D)), ModBlocks.GRASS_COVERING);
    }
    
	@Override
    public void onModPost() {
		
	}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
	}
	
	@Override
	public EntityPlayer getPlayerEntity() {
		return Minecraft.getMinecraft().player;
	}
	
	@Override
	public IThreadListener getThreadFromContext(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx));
	}
	
	@Override
    public void handleTileEntityPacket(BlockPos pos, EnumFacing facing, String owner, String customName, String state) {
        TileEntity tileEntity = FMLClientHandler.instance().getClient().world.getTileEntity(pos);

        if (tileEntity != null) {
            if (tileEntity instanceof TileEntityTC) {
                ((TileEntityTC)tileEntity).setState(state);
                ((TileEntityTC)tileEntity).setOwner(owner);
                ((TileEntityTC)tileEntity).setInvName(customName);
            }
        }
    }
}
