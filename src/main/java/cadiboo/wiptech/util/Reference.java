package cadiboo.wiptech.util;

import net.minecraft.util.ResourceLocation;

public class Reference {

	public static final String CLIENT_PROXY_CLASS = "cadiboo.wiptech.client.Proxy";
	public static final String SERVER_PROXY_CLASS = "cadiboo.wiptech.server.Proxy";

	public static final String ID = "wiptech";
	public static final String NAME = "WIPTech";
	public static final String VERSION = "1.0";
	public static final String ACCEPTED_VERSIONS = "[1.12.2]";
	
	public static final String ENERGY_UNIT = "Pirate Ninjas";
	
	public static final ResourceLocation DEBUG_TEXTURE = new ResourceLocation("minecraft", "textures/blocks/debug.png");
	public static final ResourceLocation DEBUG2_TEXTURE = new ResourceLocation("minecraft", "textures/blocks/debug2.png");
	public static final ResourceLocation TRANSPARENT_TEXTURE = new ResourceLocation(Reference.ID, "textures/util/transparent1x1.png");
	
	public static final boolean DEBUG_ENABLED = false;

}