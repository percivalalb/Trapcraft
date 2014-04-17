package trapcraft.common.tileentitys;

import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;
import trapcraft.network.packet.PacketTileUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author ProPercivalalb
 **/
public class TileEntityTC extends TileEntity {
	
	private String owner;
    private String customName;
    private ForgeDirection direction;
    private String state;

    public TileEntityTC() {
        owner = "";
        customName = "";
        state = "";
        direction = ForgeDirection.SOUTH;
    }
    
    /* Block Owner */
    public String getOwner() { return owner; }
    public boolean hasOwner() { return owner != null && owner.length() > 0; }
    public void setOwner(String owner) { this.owner = owner; }

    /* Tile Entity Custom name */
    public void setInvName(String customName) { this.customName = customName; }
    
    /* Block Direction */
    public ForgeDirection getDirection() { return direction; }
    public void setDirection(ForgeDirection newDirection) { this.direction = newDirection; }
    
    /* Block State */
    public String getState() { return state; }
    public void setState(String newState) { this.state = newState; }
    

    public boolean isUseableByPlayer(EntityPlayer player) { return owner.equals(player.getCommandSenderName()); }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        if (nbtTagCompound.hasKey(Properties.NBT_ROTATION)) {
            direction = ForgeDirection.getOrientation(nbtTagCompound.getByte(Properties.NBT_ROTATION));
        }

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
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        nbtTagCompound.setByte(Properties.NBT_ROTATION, (byte) direction.ordinal());
        nbtTagCompound.setString(Properties.NBT_STATE, state);
        
        if (hasOwner()) {
            nbtTagCompound.setString(Properties.NBT_OWNER_KEY, owner);
        }
        nbtTagCompound.setString(Properties.NBT_CUSTOM_NAME, customName);

    }

    @Override
    public Packet getDescriptionPacket() {
    	return new PacketTileUpdate(this.xCoord, this.yCoord, this.zCoord, this.owner, this.customName, this.direction, this.state).getPacket();
    }
}
