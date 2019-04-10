package trapcraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import trapcraft.api.Properties;
import trapcraft.network.PacketDispatcher;
import trapcraft.network.packet.MagneticChestTileMessage;

/**
 * @author ProPercivalalb
 **/
public class TileEntityTC extends TileEntity {
	
	private String owner;
    private String customName;
    private String state;

    public TileEntityTC() {
        owner = "";
        customName = "";
        state = "";
    }
    
    /* Block Owner */
    public String getOwner() { return owner; }
    public boolean hasOwner() { return owner != null && owner.length() > 0; }
    public void setOwner(String owner) { this.owner = owner; }

    /* Tile Entity Custom name */
    public void setInvName(String customName) { this.customName = customName; }

    /* Block State */
    public String getState() { return state; }
    public void setState(String newState) { this.state = newState; }
    

    public boolean isUseableByPlayer(EntityPlayer player) { return owner.equals(player.getName()); }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        if (nbtTagCompound.hasKey(Properties.NBT_STATE)) {
            state = nbtTagCompound.getString(Properties.NBT_STATE);
        }
        
        if (nbtTagCompound.hasKey(Properties.NBT_OWNER_KEY)) {
            owner = nbtTagCompound.getString(Properties.NBT_OWNER_KEY);
        }

        if (nbtTagCompound.hasKey(Properties.NBT_CUSTOM_NAME)) {
            customName = nbtTagCompound.getString(Properties.NBT_CUSTOM_NAME);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        nbtTagCompound.setString(Properties.NBT_STATE, state);
        
        if (hasOwner()) {
            nbtTagCompound.setString(Properties.NBT_OWNER_KEY, owner);
        }
        nbtTagCompound.setString(Properties.NBT_CUSTOM_NAME, customName);

        return nbtTagCompound;
    }
    //TODO
    //@Override
    //public Packet getDescriptionPacket() {
    //	return PacketDispatcher.getPacket(new MagneticChestTileMessage(this.pos, this.owner, this.customName, this.state));
    //}
}
