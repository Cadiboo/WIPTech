package cadiboo.wiptech;

import net.minecraft.util.text.translation.I18n;

public interface IProxy {
	public String localize(String unlocalized, Object... args);
	public void logLogicalSide();
	public void addToCreativeTab();
}
