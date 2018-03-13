package cadiboo.wiptech;

import org.apache.logging.log4j.Logger;

import cadiboo.wiptech.network.PacketRequestUpdateCoiler;
import cadiboo.wiptech.network.PacketRequestUpdateCrusher;
import cadiboo.wiptech.network.PacketUpdateCoiler;
import cadiboo.wiptech.network.PacketUpdateCrusher;
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
	public static Registry proxy;
	
	public static Logger logger;
	
	public static SimpleNetworkWrapper network;

	//public static SimpleNetworkWrapper network;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		
		proxy.logLogicalSide();
		proxy.addToCreativeTab();
		
		GameRegistry.registerWorldGenerator(new WorldGen(), 3);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
		network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ID);
		int networkIds = 0;
		network.registerMessage(new PacketUpdateCrusher.Handler(), PacketUpdateCrusher.class, networkIds++, Side.CLIENT);
		network.registerMessage(new PacketRequestUpdateCrusher.Handler(), PacketRequestUpdateCrusher.class, networkIds++, Side.SERVER);
		network.registerMessage(new PacketUpdateCoiler.Handler(), PacketUpdateCoiler.class, networkIds++, Side.CLIENT);
		network.registerMessage(new PacketRequestUpdateCoiler.Handler(), PacketRequestUpdateCoiler.class, networkIds++, Side.SERVER);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
	}
	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
	}

}