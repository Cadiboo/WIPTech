package cadiboo.wiptech;

import java.lang.reflect.Field;

import org.apache.logging.log4j.Logger;

import cadiboo.wiptech.util.IProxy;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * WIPTech Alpha
 * 
 * @author Cadiboo
 */

@Mod(modid = ModReference.ID, name = ModReference.NAME, version = ModReference.VERSION, acceptedMinecraftVersions = ModReference.ACCEPTED_VERSIONS, canBeDeactivated = ModReference.CAN_BE_DEACTIVATED, clientSideOnly = false, serverSideOnly = false, modLanguage = "java", guiFactory = ModReference.GUI_FACTORY_CLASS)
public class WIPTech {

	@Instance(ModReference.ID)
	public static WIPTech instance;

	@SidedProxy(serverSide = ModReference.SERVER_PROXY_CLASS, clientSide = ModReference.CLIENT_PROXY_CLASS)
	public static IProxy proxy;

	private static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		// Mod compatibility, or anything which depends on other mods’ init phases being
		// finished.
		ModUtil.infoModMaterialsCode();
	}

	public static void info(Object... msgs) {
		for (Object msg : msgs) {
			logger.info(msg);
		}
	}

	public static void error(Object... msgs) {
		for (Object msg : msgs) {
			logger.error(msg);
		}
	}

	public static void dump(Object... objs) {
		for (Object obj : objs) {
			Field[] fields = obj.getClass().getDeclaredFields();
			info("Dump of " + obj + ":");
			for (int i = 0; i < fields.length; i++) {
				try {
					fields[i].setAccessible(true);
					info(fields[i].getName() + " - " + fields[i].get(obj));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					info("Error getting field " + fields[i].getName());
					info(e.getLocalizedMessage());
				}
			}
		}
	}
}
