package trapcraft.common.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.ForgeDirection;

import cpw.mods.fml.common.network.Player;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class PacketTileUpdate extends PacketTC {

    public int x, y, z;
    public String owner;
    public String customName;
    public ForgeDirection direction;
    public String state;

    public PacketTileUpdate() {}

    public PacketTileUpdate(int x, int y, int z, String owner, String customName, ForgeDirection direction, String state) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.owner = owner;
        this.customName = customName;
        this.direction = direction;
        this.state = state;
    }

	@Override
	public void readPacketData(DataInputStream data, Player p) throws IOException {
	    x = data.readInt();
	    y = data.readInt();
	    z = data.readInt();
	    owner = data.readUTF();
	    customName = data.readUTF();
	    direction = ForgeDirection.getOrientation(data.readInt());
	    state = data.readUTF();
	}

	@Override
	public void writePacketData(DataOutputStream data)  throws IOException {
		data.writeInt(x);
	    data.writeInt(y);
	    data.writeInt(z);
	    data.writeUTF(owner);
	    data.writeUTF(customName);
	    data.writeInt(direction.ordinal());
	    data.writeUTF(state);
	}

	@Override
	public void processPacket() {
		TrapcraftMod.proxy.handleTileEntityPacket(x, y, z, direction, owner, customName, state);
	}

	@Override
	public String getChannel() {
		return Properties.PACKET_MAGENTIC_CHEST;
	}
}
