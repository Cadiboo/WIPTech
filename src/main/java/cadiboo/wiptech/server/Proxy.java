package cadiboo.wiptech.server;

import cadiboo.wiptech.IProxy;
import cadiboo.wiptech.WIPTech;
import net.minecraft.util.text.translation.I18n;

public class Proxy implements IProxy {

	public String localize(String unlocalized, Object... args)
	{
		return I18n.translateToLocalFormatted(unlocalized, args);
	}

	public void logLogicalSide() {
		WIPTech.logger.info("Logical Side: Server");
	}

	public void addToCreativeTab() {
		//Client only
	}

}