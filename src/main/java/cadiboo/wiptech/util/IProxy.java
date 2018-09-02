package cadiboo.wiptech.util;

import cadiboo.wiptech.WIPTech;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Some basic functions that differ depending on the logical side
 * @author Cadiboo
 */
public interface IProxy {

	String localize(String unlocalized);

	String localizeAndFormat(String unlocalized, Object... args);

	default void logLogicalSide() {
		WIPTech.info("Logical Side: " + getSide());
	}

	Side getSide();
}