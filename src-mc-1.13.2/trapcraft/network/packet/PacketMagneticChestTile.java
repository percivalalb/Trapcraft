package trapcraft.network.packet;

import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import trapcraft.tileentity.TileEntityTC;

public class PacketMagneticChestTile {
	
	public BlockPos pos;
    public String owner;
    public String customName;
    public String state;
	
    public PacketMagneticChestTile(BlockPos pos, String owner, String customName, String state) {
		this.pos = pos;
		this.owner = owner;
		this.customName = customName;
		this.state = state;
	}
    
    public static void encode(PacketMagneticChestTile msg, PacketBuffer buf) {
    	buf.writeBlockPos(msg.pos);
    	buf.writeString(msg.owner);
     	buf.writeString(msg.customName);
     	buf.writeString(msg.state);
	}
	
	public static PacketMagneticChestTile decode(PacketBuffer buf) {
		BlockPos pos = buf.readBlockPos();
		String owner = buf.readString(32767);
		String customName = buf.readString(32767);
		String state = buf.readString(32767);
		return new PacketMagneticChestTile(pos, owner, customName, state);
	}
	
	
	public static class Handler {
        public static void handle(final PacketMagneticChestTile msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
            	EntityPlayer player = ctx.get().getSender();
            	TileEntity target = player.world.getTileEntity(msg.pos);
        		
        		if(!(target instanceof TileEntityTC))
        			return;
        		
        		TileEntityTC magneticChest = (TileEntityTC)target;
        		magneticChest.setState(msg.state);
        		magneticChest.setOwner(msg.owner);
        		magneticChest.setInvName(msg.customName);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
