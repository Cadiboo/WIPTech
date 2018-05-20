package cadiboo.wiptech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cadiboo.wiptech.handler.GuiHandler;
import cadiboo.wiptech.handler.network.PacketHandler;
import cadiboo.wiptech.init.Capabilities;
import cadiboo.wiptech.init.Recipes;
import cadiboo.wiptech.util.IProxy;
import cadiboo.wiptech.util.Reference;
import cadiboo.wiptech.world.WorldGen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS)
public class WIPTech {

	@Instance(Reference.ID)
	public static WIPTech instance;

	@SidedProxy(serverSide = Reference.SERVER_PROXY_CLASS, clientSide = Reference.CLIENT_PROXY_CLASS)
	public static IProxy proxy;

	public static final Logger logger = LogManager.getLogger(Reference.ID);

	// HALF DONE STOP ARROW-LIKE BOUNCE OFF - Partly done, they now either go
	// through the mob or teleport into the ground a block below it...
	// ^ DONE maybe reenable it
	// TODO Maybe implement a custom itemstack renderer for modules? so that you can
	// see what
	// augments you have on it
	// HALF DONE make Plasma Cannon!!!
	// TODO and drill
	// HALF DONE capabilities
	// TODO make EntityRailgun rideable
	// MAYBE TODO Overheat
	// TODO capacitors
	// TODO check Capabilities modules NBT

	// TODO plasma tools
	// TODO taser
	// TODO Tasers, Mounted railguns, Tesla Cannons & Electric Lighters

	// old todo list
	// TODO gas tank for flamethrower
	// TODO Laser Weapon - Placeable erases stuff, handheld starts fires
	// TODO EMP?
	// TODO

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		WIPTech.proxy.logLogicalSide();
		WIPTech.proxy.addToCreativeTab();

		new PacketHandler();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		Capabilities.registerCapabilities();

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		GameRegistry.registerWorldGenerator(new WorldGen(), 3);
		Recipes.registerRecipes();
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		// Mod compatibility, or anything which depends on other mods’ init phases being
		// finished.
	}

	public static void info(Object... msgs) {
		for (Object msg : msgs) {
			logger.info(msg);
		}
	}

	public static void error(Object msg) {
		logger.error(msg);
	}

}