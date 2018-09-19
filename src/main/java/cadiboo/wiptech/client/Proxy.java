package cadiboo.wiptech.client;

import cadiboo.wiptech.util.IProxy;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Proxy implements IProxy {

	@Override
	public String localizeAndFormat(final String unlocalized, final Object... args) {
		return I18n.format(unlocalized, args);
	}

	@Override
	public String localize(final String unlocalized) {
		return this.localizeAndFormat(unlocalized, new Object[0]);
	}

	@Override
	public Side getPhysicalSide() {
		return Side.CLIENT;
	}

}
