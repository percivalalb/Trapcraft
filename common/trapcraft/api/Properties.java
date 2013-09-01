package trapcraft.api;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.base.Optional;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Configuration;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class Properties {

	/** Reference to the mod id which is used by forge **/
	public static final String MOD_ID = "Trapcraft";
	/** The readable name used by forge **/
	public static final String MOD_NAME = "Trapcraft";
	/** Version string for the mod **/
	public static final String MOD_VERSION = "v2.1.0a";
	public static Logger logger = Logger.getLogger(MOD_ID);
	
	public static final String SP_CLIENT = "trapcraft.client.ClientProxy";
	public static final String SP_SERVER = "trapcraft.common.CommonProxy";

	/** The configuration file where all variables can be accessed**/
	public static Configuration config;
	
	/** The trapcraft packet handler (Client & server) **/
	public static IPacketHandler packetHandler;
	
	//Texture Path
	public static final String TEX_PACKAGE = "trapcraft:";
	public static final ResourceLocation RES_BLOCK_MAGNETIC_CHEST = new ResourceLocation(TEX_PACKAGE + "magneticChest.png");
	public static final ResourceLocation RES_GUI_IGNITER = new ResourceLocation(TEX_PACKAGE + "gui/igniter.png");
	public static final ResourceLocation RES_MOB_DUMMY = new ResourceLocation(TEX_PACKAGE + "mob/dummy.png");
	
	//Packets
	public static final String PACKET_DEFAULT = "PH|DEFAULT";
	public static final String PACKET_MAGENTIC_CHEST = "PH|MAGNETIC";
	
	//Render ID
	public static int RENDER_ID_MAGNETIC_CHEST;
	public static int RENDER_ID_FAN;
	public static int RENDER_ID_GRASS_COVERING;
	public static int RENDER_ID_BEAR_TRAP;
	
	//ID'S
    public static int fanID = 3450;
    public static int igniterID = 3451;
    public static int magneticChestID = 3452;
    public static int bearTrapID = 3453;
    public static int grassCoveringID = 3454;
    public static int dummyHeadID = 3455;
    public static int spikesID = 3456;
    
    public static int igniter_RangeID = 5450;
	
    //NBT Data
    public static final String NBT_OWNER_KEY = "teOwner";
    public static final String NBT_CUSTOM_NAME = "CustomName";
    public static final String NBT_ROTATION = "rotation";
    public static final String NBT_STATE = "state";
    
	static {
		logger.setParent(FMLLog.getLogger());
	}
	
	public static String[] getPackets() throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = Properties.class.getFields();
		int count = 0;
		for(Field field : fields) {
			String name = field.getName();
			if(name.startsWith("PACKET_")) {
				count += 1;
			}
		}
		int newCount = 0;
		String[] packets = new String[count];
		for(Field field : fields) {
			String name = field.getName();
			if(name.startsWith("PACKET_")) {
				packets[newCount] = (String)field.get((Object)null);
				newCount += 1;
			}
		}
		return packets;
	}
}