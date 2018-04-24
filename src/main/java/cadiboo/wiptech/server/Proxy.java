package cadiboo.wiptech.server;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.util.IProxy;
import net.minecraft.util.text.translation.I18n;

public class Proxy implements IProxy {

	@Override
	public String localize(String unlocalized, Object... args) {
		return I18n.translateToLocalFormatted(unlocalized, args);
	}

	@Override
	public void logLogicalSide() {
		WIPTech.logger.info("Logical Side: Server");
	}

	@Override
	public void addToCreativeTab() {
		// Client only
	}

	@Override
	public String getSide() {
		return "Server";
	}

}