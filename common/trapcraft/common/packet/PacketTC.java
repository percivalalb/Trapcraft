package trapcraft.common.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.Player;

import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet250CustomPayload;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public abstract class PacketTC {
	
	public abstract void readPacketData(DataInputStream data, Player p) throws IOException;

	public abstract void writePacketData(DataOutputStream data)  throws IOException;

	public abstract void processPacket();
	
	public abstract String getChannel();
	
	public final Packet250CustomPayload buildPacket() throws IOException {
		 ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	     DataOutputStream data = new DataOutputStream(bytes);
	     this.writePacketData(data);
	     Packet250CustomPayload pack = new Packet250CustomPayload();
	     pack.channel = this.getChannel();
	     pack.data = bytes.toByteArray();
	     pack.length = bytes.size();
	     return pack;
	}
	
	public final void readPacket(Packet250CustomPayload packet, Player p) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
        DataInputStream dis = new DataInputStream(bis);
        this.readPacketData(dis, p);
        this.processPacket();
	}
}
