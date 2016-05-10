package trapcraft.api;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

/**
 * @author ProPercivalalb
 **/
public class Properties {

	/** Reference to the mod id which is used by forge **/
	public static final String MOD_ID = "Trapcraft";
	/** The readable name used by forge **/
	public static final String MOD_NAME = "Trapcraft";
	/** Version string for the mod **/
	public static final String MOD_VERSION = "v2.3.0a";
	
	public static final String SP_CLIENT = "trapcraft.proxy.ClientProxy";
	public static final String SP_SERVER = "trapcraft.proxy.CommonProxy";

	/** The configuration file where all variables can be accessed**/
	public static Configuration config;
	
	//Texture Path
	public static final String TEX_PACKAGE = "trapcraft:";
	public static final ResourceLocation RES_BLOCK_MAGNETIC_CHEST = new ResourceLocation(TEX_PACKAGE + "magneticChest.png");
	public static final ResourceLocation RES_GUI_IGNITER = new ResourceLocation(TEX_PACKAGE + "gui/igniter.png");
	public static final ResourceLocation RES_MOB_DUMMY = new ResourceLocation(TEX_PACKAGE + "mob/dummy.png");
	
	//Render ID
	public static int RENDER_ID_MAGNETIC_CHEST;
	public static int RENDER_ID_FAN;
	public static int RENDER_ID_GRASS_COVERING;
	public static int RENDER_ID_BEAR_TRAP;
	
    //NBT Data
    public static final String NBT_OWNER_KEY = "teOwner";
    public static final String NBT_CUSTOM_NAME = "CustomName";
    public static final String NBT_ROTATION = "rotation";
    public static final String NBT_STATE = "state";
    
    
	public static final String CHANNEL_NAME = "TRAPCRAFT";
}