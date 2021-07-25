package trapcraft.network;

import net.minecraftforge.fmllegacy.network.PacketDistributor;
import trapcraft.TrapcraftMod;

public final class PacketHandler
{

    public static void register()  {
        int disc = 0;

       // TrapcraftMod.HANDLER.registerMessage(disc++, ExamplePacket.class, ExamplePacket::encode, ExamplePacket::decode, ExamplePacket.Handler::handle);
    }

    public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
        TrapcraftMod.HANDLER.send(target, message);
    }
}
