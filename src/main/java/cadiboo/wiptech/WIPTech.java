package cadiboo.wiptech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cadiboo.wiptech.network.PacketHandler;
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
	
	public static final Logger logger = LogManager.getLogger(Reference.ID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.logLogicalSide();
		proxy.addToCreativeTab();
		
		new PacketHandler();
		
		GameRegistry.registerWorldGenerator(new WorldGen(), 3);
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