package trapcraft.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author ProPercivalalb
 **/
public class ClientTickHandler {
	
	public static boolean checkedVersion = false;
	
	@SubscribeEvent
	public void tickEnd(ClientTickEvent event) {
		if(event.phase != Phase.END || event.type != TickEvent.Type.CLIENT || event.side != Side.CLIENT)
			return;
		GuiScreen guiscreen = Minecraft.getMinecraft().currentScreen;
		if (guiscreen == null) {
          	if(!checkedVersion && Minecraft.getMinecraft().thePlayer != null) {
          		//VersionHelper.checkVersion(UpdateType.COLOURED);
          		checkedVersion = true;
          	}
		}
	}
}