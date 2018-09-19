package cadiboo.wiptech;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cadiboo.wiptech.capability.attachments.CapabilityAttachmentList;
import cadiboo.wiptech.capability.attachments.circuitdata.CapabilityCircuitData;
import cadiboo.wiptech.capability.energy.network.CapabilityEnergyNetworkList;
import cadiboo.wiptech.network.ModNetworkManager;
import cadiboo.wiptech.util.IProxy;
import cadiboo.wiptech.util.ModGuiHandler;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModWritingUtil;
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
import net.minecraftforge.fml.relauncher.Side;

/**
 * WIPTech Alpha
 * @author Cadiboo
 */
/*@formatter:off*/
@Mod(modid = ModReference.MOD_ID,
	name = ModReference.MOD_NAME,
	version = ModReference.Version.VERSION,
	acceptedMinecraftVersions = ModReference.ACCEPTED_VERSIONS,
	dependencies = ModReference.DEPENDENCIES,
	canBeDeactivated = ModReference.CAN_BE_DEACTIVATED,
	clientSideOnly = false,
	serverSideOnly = false,
	modLanguage = "java")
/*@formatter:on*/
public class WIPTech {

	/***** FOR 1.13 *****/
	// TODO: Texture locations
	// TODO: check lang generation

	/***** FOR ALL VERSIONS *****/
	// FIXME VITAL DO THIS NEXT FIXME TODO: DYNAMIC GLITCH MODELS
	// FIXME TODO: DYNAMIC ATTACHMENT MODELS
	// FIXME TODO: Slugs & SlugCasings
	// FIXME TODO: test camera transforms (on wires? i forgot what this was about)
	// FIXME TODO: Mod CreativeTab itemstack icon is air -> use @ItemStackHolder?
	// FIXME TODO: better electrocution -> actual list of entities?
	// FIXME TODO: Wire & Enamel ItemBlock Models->Wire & Enamel Item Models (Code & JSON)
	// FIXME TODO: glitch spool models (should be easy)
	// FIXME TODO: fix railgun rendering glitch when yaw = 0;
	// TESTME FIXME TODO: fix GUIs - this might be done?
	// TESTME FIXME TODO: Material Texture locations
	// TODO: world gen to subscriber
	// TESTME TODO: energy network performance
	// TODO: NetworkManager to subscriber
	// TODO: generators
	// TODO: Crushing & hammering
	// TODO: rail & coil models
	// TODO: handheld guns shooting
	// TODO: mounted guns
	// TODO: attachments doing stuff

	// TODO: steel & tungsten carbite - alloying -> custom furnace | craftable raw alloy?

	// TODO: radioactivity
	// TODO: EMP - if(hasCapability(energy) setblock lava);
	// TODO: SOPMOD achievement http://callofduty.wikia.com/wiki/SOPMOD

	@Instance(ModReference.MOD_ID)
	public static WIPTech instance;

	@SidedProxy(serverSide = ModReference.SERVER_PROXY_CLASS, clientSide = ModReference.CLIENT_PROXY_CLASS)
	public static IProxy proxy;

	private static Logger logger;

	/**
	 * Run before anything else. <s>Read your config, create blocks, items, etc, and register them with the GameRegistry</s>
	 * @see {@link net.minecraftforge.common.ForgeModContainer#preInit(FMLPreInitializationEvent) ForgeModContainer.preInit}
	 */
	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.logPhysicalSide();

		GameRegistry.registerWorldGenerator(new ModWorldGenerator(), 3);
		new ModNetworkManager();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());

		CapabilityEnergyNetworkList.register();
		CapabilityAttachmentList.register();
		CapabilityCircuitData.register();

	}

	/**
	 * Do your mod setup. Build whatever data structures you care about. Register recipes, send FMLInterModComms messages to other mods.
	 */
	@EventHandler
	public void init(final FMLInitializationEvent event) {
		if (proxy.getPhysicalSide() == Side.CLIENT) {
			ModWritingUtil.writeMod();
		}
	}

	/**
	 * Mod compatibility, or anything which depends on other mods’ init phases being finished.
	 * @see {@link net.minecraftforge.common.ForgeModContainer#postInit(FMLPostInitializationEvent) ForgeModContainer.postInit}
	 */
	@EventHandler
	public void postInit(final FMLPostInitializationEvent event) {
	}

	private static Logger getLogger() {
		if (logger == null) {
			final Logger tempLogger = LogManager.getLogger();
			tempLogger.error("[" + WIPTech.class.getSimpleName() + "]: getLogger called before logger has been initalised! Providing default logger");
			return tempLogger;
		}
		return logger;
	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#DEBUG DEBUG} level.
	 * @param messages the message objects to log.
	 * @author Cadiboo
	 */
	public static void debug(final Object... messages) {
		for (final Object msg : messages) {
			getLogger().debug(msg);
		}
	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#INFO ERROR} INFO.
	 * @param messages the message objects to log.
	 * @author Cadiboo
	 */
	public static void info(final Object... messages) {
		for (final Object msg : messages) {
			getLogger().info(msg);
		}
	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#WARN WARN} level.
	 * @param messages the message objects to log.
	 * @author Cadiboo
	 */
	public static void warn(final Object... messages) {
		for (final Object msg : messages) {
			getLogger().warn(msg);
		}
	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#ERROR ERROR} level.
	 * @param messages the message objects to log.
	 * @author Cadiboo
	 */
	public static void error(final Object... messages) {
		for (final Object msg : messages) {
			getLogger().error(msg);
		}
	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#FATAL FATAL} level.
	 * @param messages the message objects to log.
	 * @author Cadiboo
	 */
	public static void fatal(final Object... messages) {
		for (final Object msg : messages) {
			getLogger().fatal(msg);
		}
	}

	/**
	 * Logs all {@link java.lang.reflect.Field Field}s and their values of an object with the {@link org.apache.logging.log4j.Level#INFO INFO} level.
	 * @param objects the objects to dump.
	 * @author Cadiboo
	 */
	public static void dump(final Object... objects) {
		for (final Object obj : objects) {
			final Field[] fields = obj.getClass().getDeclaredFields();
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
