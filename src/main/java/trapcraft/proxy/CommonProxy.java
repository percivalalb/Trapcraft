package trapcraft.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import trapcraft.client.gui.GuiIgniter;
import trapcraft.inventory.ContainerIgniter;
import trapcraft.tileentity.TileEntityIgniter;

/**
 * @author ProPercivalalb
 **/
public class CommonProxy implements IGuiHandler {

    public void onModPre() {}
    public void onModLoad() {}
    public void onModPost() {}

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);

        TileEntity tileentity = world.getTileEntity(pos);
        if(id == 1)
            if(tileentity instanceof TileEntityIgniter)
                return new ContainerIgniter((TileEntityIgniter)tileentity, player);

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);

        TileEntity tileentity = world.getTileEntity(pos);
        if(id == 1)
            if(tileentity instanceof TileEntityIgniter)
                return new GuiIgniter((TileEntityIgniter)tileentity, player);

        return null;
    }

    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return ctx.getServerHandler().player;
    }

    public EntityPlayer getPlayerEntity() {
        return null;
    }

    public IThreadListener getThreadFromContext(MessageContext ctx) {
        return ctx.getServerHandler().player.getServer();
    }
}
