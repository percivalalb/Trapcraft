package trapcraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import trapcraft.lib.Reference;
import trapcraft.proxy.ClientProxy;
import trapcraft.proxy.CommonProxy;
import trapcraft.proxy.ServerProxy;

/**
 * @author ProPercivalalb
 **/
@Mod(value = Reference.MOD_ID)
public class TrapcraftMod {

	public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

	public static TrapcraftMod INSTANCE;
	public static CommonProxy PROXY;

	public TrapcraftMod() {
		INSTANCE = this;
        PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
	}
}
