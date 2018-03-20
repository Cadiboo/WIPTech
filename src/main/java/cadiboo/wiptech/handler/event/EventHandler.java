package cadiboo.wiptech.handler.event;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.handler.GuiHandler;
import cadiboo.wiptech.handler.network.PacketHandler;
import cadiboo.wiptech.world.WorldGen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class EventHandler {

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		WIPTech.proxy.logLogicalSide();
		WIPTech.proxy.addToCreativeTab();
		
		new PacketHandler();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
		GameRegistry.registerWorldGenerator(new WorldGen(), 3);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
	}
	
	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
	}
	
}
