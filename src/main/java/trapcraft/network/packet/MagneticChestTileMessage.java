package trapcraft.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import trapcraft.network.AbstractMessage.AbstractClientMessage;
import trapcraft.tileentity.TileEntityTC;

public class MagneticChestTileMessage extends AbstractClientMessage {
	
	public BlockPos pos;
    public String owner;
    public String customName;
    public String state;
	
	public MagneticChestTileMessage() {}
    public MagneticChestTileMessage(BlockPos pos, String owner, String customName, String state) {
		this.pos = pos;
		this.owner = owner;
		this.customName = customName;
		this.state = state;
	}
    
	@Override
	public void read(PacketBuffer buffer) {
		this.pos = buffer.readBlockPos();
		this.owner = ByteBufUtils.readUTF8String(buffer);
		this.customName = ByteBufUtils.readUTF8String(buffer);
		this.state = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeBlockPos(this.pos);
		ByteBufUtils.writeUTF8String(buffer, this.owner);
		ByteBufUtils.writeUTF8String(buffer, this.customName);
		ByteBufUtils.writeUTF8String(buffer, this.state);
	}
	
	@Override
	public void process(EntityPlayer player, Side side) {
		TileEntity target = player.world.getTileEntity(this.pos);
		
		if(!(target instanceof TileEntityTC))
			return;
		
		TileEntityTC magneticChest = (TileEntityTC)target;
		magneticChest.setState(this.state);
		magneticChest.setOwner(this.owner);
		magneticChest.setInvName(this.customName);
	}
}
