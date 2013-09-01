package trapcraft.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import trapcraft.api.Properties;

import cpw.mods.fml.common.Loader;

/**
 * @author ProPerivalalb
 */
public class Version {
	
	private static final String url = "https://dl.dropboxusercontent.com/s/l4bne0z2cqqor80/version.txt";
	
	public static String[] checkVersion(boolean par1) {
		try {
			URL updateURL = new URL(url);
            HttpURLConnection updateConnection = (HttpURLConnection)updateURL.openConnection();
            BufferedReader updateReader = new BufferedReader(new InputStreamReader(updateConnection.getInputStream()));
            String updateString = updateReader.readLine();
            String[] split = updateString.split(";");
            if(split.length >= 2) {
            	String newMCVersion = split[0];
            	String newModVersion = split[1];
            	String updateUrl = split[2];
            	
            	String oldMCVersion = Loader.instance().getMinecraftModContainer().getDisplayVersion();
            	String oldModVersion = Properties.MOD_VERSION;
            	if(!oldModVersion.equals(newModVersion)) {
            		if(!par1) {
            			return new String[] {"A new " + Properties.MOD_NAME + " version exists (" + newModVersion + ") for Minecraft " + newMCVersion + ". Get it here: " + updateUrl};
            		}
            		return new String[] {"A new \u00a7e" + Properties.MOD_NAME + " \u00a7fversion exists (\u00a7e" + newModVersion + ") \u00a7ffor Minecraft \u00a7e" + newMCVersion + ". \u00a7fGet it here: \u00a7e" + updateUrl};
            	}
            }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
