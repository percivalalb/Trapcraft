package trapcraft.common.packet;

import java.util.Hashtable;
import java.util.Map;

import trapcraft.api.Properties;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class PacketHandler implements IPacketHandler {

	private Map<String, Class> customPackages = new Hashtable<String, Class>();
	
	public PacketHandler() {
		customPackages.put(Properties.PACKET_MAGENTIC_CHEST, PacketTileUpdate.class);
	}
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		try {
			((PacketTC)customPackages.get(packet.channel).newInstance()).readPacket(packet, player);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
