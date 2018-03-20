package cadiboo.wiptech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cadiboo.wiptech.handler.GuiHandler;
import cadiboo.wiptech.handler.network.PacketHandler;
import cadiboo.wiptech.handler.network.PacketRequestUpdateCoiler;
import cadiboo.wiptech.handler.network.PacketRequestUpdateCrusher;
import cadiboo.wiptech.handler.network.PacketUpdateCoiler;
import cadiboo.wiptech.handler.network.PacketUpdateCrusher;
import cadiboo.wiptech.world.WorldGen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(
		modid = Reference.ID,
		name = Reference.NAME,
		version = Reference.VERSION,
		acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS
		)
public class WIPTech {

	@Instance(Reference.ID)
	public static WIPTech instance;

	@SidedProxy(serverSide = Reference.SERVER_PROXY_CLASS, clientSide = Reference.CLIENT_PROXY_CLASS)
	public static IProxy proxy;

	public static final Logger logger = LogManager.getLogger(Reference.ID);


	//TODO STOP ARROW-LIKE BOUNCE OFF
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		WIPTech.logger.info(WIPTech.proxy);
		WIPTech.proxy.logLogicalSide();
		WIPTech.proxy.addToCreativeTab();

		new PacketHandler();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		GameRegistry.registerWorldGenerator(new WorldGen(), 3);
		/*
		Registering world generators
		Registering recipes
		Registering event handlers
		 */
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		//Mod compatibility, or anything which depends on other mods’ init phases being finished.
	}

}