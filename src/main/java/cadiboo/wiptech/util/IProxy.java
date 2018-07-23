package cadiboo.wiptech.util;

import cadiboo.wiptech.WIPTech;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Some basic functions that differ depending on the logical side
 * 
 * @author Cadiboo
 */

public interface IProxy {

	public String localize(String unlocalized);

	public String localizeAndFormat(String unlocalized, Object... args);

	default public void logLogicalSide() {
		WIPTech.info("Logical Side: " + getSide());
	}

	public Side getSide();
}