package trapcraft.handler;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.FMLPlayMessages;
import trapcraft.TrapcraftMod;
import trapcraft.client.gui.GuiIgniter;
import trapcraft.tileentity.TileEntityIgniter;

public class GuiHandler {

	public static GuiScreen openGui(FMLPlayMessages.OpenContainer openContainer) {
    	String guiId = openContainer.getId().toString();
    	
    	if(guiId.equals("trapcraft:igniter")) {
    		BlockPos pos = openContainer.getAdditionalData().readBlockPos();
    		EntityPlayer player = TrapcraftMod.PROXY.getPlayerEntity();
    		TileEntity tileentity = player.world.getTileEntity(pos);
    		return new GuiIgniter((TileEntityIgniter)tileentity, player);
    	}
    	else if(guiId.equals("trapcraft:magnetic_chest")) {
    		BlockPos pos = openContainer.getAdditionalData().readBlockPos();
    		EntityPlayer player = TrapcraftMod.PROXY.getPlayerEntity();
    		TileEntity tileentity = player.world.getTileEntity(pos);
    		//return new GuiMagneticChest((TileEntityIgniter)tileentity, player);
    	}
    	
    	return null;
	}
}
