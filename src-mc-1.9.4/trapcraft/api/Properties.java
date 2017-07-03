package trapcraft.api;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

/**
 * @author ProPercivalalb
 **/
public class Properties {

	/** The configuration file where all variables can be accessed**/
	public static Configuration config;
	
	//Texture Path
	public static final String TEX_PACKAGE = "trapcraft:";
	public static final ResourceLocation RES_BLOCK_MAGNETIC_CHEST = new ResourceLocation(TEX_PACKAGE + "textures/entity/chest/magnetic_chest.png");
	public static final ResourceLocation RES_GUI_IGNITER = new ResourceLocation(TEX_PACKAGE + "gui/igniter.png");
	public static final ResourceLocation RES_MOB_DUMMY_OAK = new ResourceLocation(TEX_PACKAGE + "textures/entity/dummy/dummy_oak.png");
	public static final ResourceLocation RES_MOB_DUMMY_BIRCH = new ResourceLocation(TEX_PACKAGE + "textures/entity/dummy/dummy_birch.png");
	public static final ResourceLocation RES_MOB_DUMMY_SPRUCE = new ResourceLocation(TEX_PACKAGE + "textures/entity/dummy/dummy_spruce.png");
	public static final ResourceLocation RES_MOB_DUMMY_JUNGLE = new ResourceLocation(TEX_PACKAGE + "textures/entity/dummy/dummy_jungle.png");
	public static final ResourceLocation RES_MOB_DUMMY_ACACIA = new ResourceLocation(TEX_PACKAGE + "textures/entity/dummy/dummy_acacia.png");
	public static final ResourceLocation RES_MOB_DUMMY_DARK_OAK = new ResourceLocation(TEX_PACKAGE + "textures/entity/dummy/dummy_dark_oak.png");
	
    //NBT Data
    public static final String NBT_OWNER_KEY = "teOwner";
    public static final String NBT_CUSTOM_NAME = "CustomName";
    public static final String NBT_ROTATION = "rotation";
    public static final String NBT_STATE = "state";
}