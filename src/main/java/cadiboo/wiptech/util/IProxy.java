package cadiboo.wiptech.util;

import cadiboo.wiptech.WIPTech;

public interface IProxy {
	public String localize(String unlocalized, Object... args);

	default public void logLogicalSide() {
		WIPTech.info("Logical Side: " + getSide());
	}

	public void addToCreativeTab();

	public String getSide();
}
