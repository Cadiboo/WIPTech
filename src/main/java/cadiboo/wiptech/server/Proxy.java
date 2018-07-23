package cadiboo.wiptech.server;

import cadiboo.wiptech.util.IProxy;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;

public class Proxy implements IProxy {

	@Override
	public String localizeAndFormat(String unlocalized, Object... args) {
		return I18n.translateToLocalFormatted(unlocalized, args);
	}

	public String localize(String unlocalized) {
		return I18n.translateToLocal(unlocalized);
	}

	@Override
	public Side getSide() {
		return Side.SERVER;
	}

}
