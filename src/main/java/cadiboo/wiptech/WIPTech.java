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

	@SidedProxy(serverSide = Reference.PROXY_CLASS, clientSide = Reference.CLIENT_PROXY_CLASS)
	public static IProxy proxy;

	public static final Logger logger = LogManager.getLogger(Reference.ID);


	//TODO STOP ARROW-LIKE BOUNCE OFF

}