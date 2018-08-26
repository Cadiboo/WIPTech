package cadiboo.wiptech;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cadiboo.wiptech.network.ModNetworkManager;
import cadiboo.wiptech.util.IProxy;
import cadiboo.wiptech.util.ModGuiHandler;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.world.gen.ModWorldGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * WIPTech Alpha
 * 
 * @author Cadiboo
 */

/*@formatter:off*/
@Mod(modid = ModReference.ID,
	name = ModReference.NAME,
	version = ModReference.VERSION,
	acceptedMinecraftVersions = ModReference.ACCEPTED_VERSIONS,
	dependencies = ModReference.DEPENDENCIES,
	canBeDeactivated = ModReference.CAN_BE_DEACTIVATED,
	clientSideOnly = false,
	serverSideOnly = false,
	modLanguage = "java",
	guiFactory = ModReference.GUI_FACTORY_CLASS)
/*@formatter:on*/
public class WIPTech {

	/***** FOR 1.13 *****/
	// TODO Texture locations
	// TODO vanilla recipes

	/***** FOR ALL VERSIONS *****/
	// TODO Armor textures
	// TODO clean up JSONs
	// TODO Wire & Enamel ItemBlock Models->Wire & Enamel Item Models (Code & JSON)
	// TODO radioactivity
	// TODO Slugs & SlugCasings
	// TODO world gen to subscriber
	// TODO NetworkManager to subscriber
	// TODO generators
	// TODO handheld coilgun
	// TODO Gauss cannon (mounted coilgun)
	// TODO handheld PlasmaGun & mounted Plasma Cannon
	// TODO Crushing & hammering
	// TODO weapon modules :( scopes, chips, etc
	// TODO steel & tungsten carbite

	@Instance(ModReference.ID)
	public static WIPTech instance;

	@SidedProxy(serverSide = ModReference.SERVER_PROXY_CLASS, clientSide = ModReference.CLIENT_PROXY_CLASS)
	public static IProxy proxy;

	private static Logger logger;

	/**
	 * Run before anything else. <s>Read your config, create blocks, items, etc, and
	 * register them with the GameRegistry</s>
	 */
	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.logLogicalSide();
		GameRegistry.registerWorldGenerator(new ModWorldGenerator(), 3);
		new ModNetworkManager();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
//		WIPTechAPI.addMaterial("bauxite", new ModMaterialProperties(true, false, false, false, false, ModMaterials.ALUMINIUM.getProperties().getHardness(), 0, false));

	}

	/**
	 * Do your mod setup. Build whatever data structures you care about. Register
	 * recipes, send FMLInterModComms messages to other mods.
	 */
	@EventHandler
	public void init(final FMLInitializationEvent event) {

	}

	/**
	 * Mod compatibility, or anything which depends on other mods’ init phases being
	 * finished.
	 */
	@EventHandler
	public void postinit(final FMLPostInitializationEvent event) {
	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#INFO
	 * ERROR} INFO.
	 * 
	 * @param messages the message objects to log.
	 * @author Cadiboo
	 */
	public static void info(final Object... messages) {
		for (Object msg : messages) {
			getLogger().info(msg);
		}
	}

	private static Logger getLogger() {
		if (logger == null) {
			Logger tempLogger = LogManager.getLogger();
			tempLogger.error("[" + WIPTech.class.getSimpleName() + "]: getLogger called before logger has been initalised! Providing default logger");
			return tempLogger;
		}
		return logger;
	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#ERROR
	 * ERROR} level.
	 * 
	 * @param messages the message objects to log.
	 * @author Cadiboo
	 */
	public static void error(final Object... messages) {
		for (Object msg : messages) {
			getLogger().error(msg);
		}
	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#DEBUG
	 * DEBUG} level.
	 * 
	 * @param messages the message objects to log.
	 * @author Cadiboo
	 */
	public static void debug(final Object... messages) {
		for (Object msg : messages) {
			getLogger().debug(msg);
		}
	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#FATAL
	 * FATAL} level.
	 * 
	 * @param messages the message objects to log.
	 * @author Cadiboo
	 */
	public static void fatal(final Object... messages) {
		for (Object msg : messages) {
			getLogger().fatal(msg);
		}
	}

	/**
	 * Logs all {@link java.lang.reflect.Field Field}s and their values of an object
	 * with the {@link org.apache.logging.log4j.Level#INFO INFO} level.
	 * 
	 * @param objects the objects to dump.
	 * @author Cadiboo
	 */
	public static void dump(final Object... objects) {
		for (Object obj : objects) {
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
