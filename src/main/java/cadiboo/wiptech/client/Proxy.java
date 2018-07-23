package cadiboo.wiptech.client;

import cadiboo.wiptech.util.IProxy;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;

public class Proxy implements IProxy {

	@Override
	public String localizeAndFormat(String unlocalized, Object... args) {
		return I18n.format(unlocalized, args);
	}

	@Override
	public String localize(String unlocalized) {
		return localizeAndFormat(unlocalized, new Object[0]);
	}

	@Override
	public Side getSide() {
		return Side.CLIENT;
	}

}
