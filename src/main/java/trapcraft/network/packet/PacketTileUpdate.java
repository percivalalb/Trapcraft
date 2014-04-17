package trapcraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;
import trapcraft.common.tileentitys.TileEntityTC;
import trapcraft.network.IPacket;

/**
 * @author ProPercivalalb.
 **/
public class PacketTileUpdate extends IPacket {

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
	public void read(DataInputStream data) throws IOException {
		this.x = data.readInt();
		this.y = data.readInt();
	    this.z = data.readInt();
	    this.owner = data.readUTF();
	    this.customName = data.readUTF();
	    this.direction = ForgeDirection.getOrientation(data.readInt());
	    this.state = data.readUTF();
	}

	@Override
	public void write(DataOutputStream data)  throws IOException {
		data.writeInt(this.x);
	    data.writeInt(this.y);
	    data.writeInt(this.z);
	    data.writeUTF(this.owner);
	    data.writeUTF(this.customName);
	    data.writeInt(this.direction.ordinal());
	    data.writeUTF(this.state);
	}

	@Override
	public void execute(EntityPlayer player) {
		FMLLog.info("" + customName);
	    TileEntity tileEntity = player.worldObj.getTileEntity(x, y, z);

        if (tileEntity != null) {
            if (tileEntity instanceof TileEntityTC) {
                ((TileEntityTC)tileEntity).setDirection(direction);
                ((TileEntityTC)tileEntity).setState(state);
                ((TileEntityTC)tileEntity).setOwner(owner);
                ((TileEntityTC)tileEntity).setInvName(customName);
            }
        }
		
		//TrapcraftMod.proxy.handleTileEntityPacket(x, y, z, direction, owner, customName, state);
	}
}
