package cadiboo.wiptech.network;

import cadiboo.wiptech.GuiHandler;
import cadiboo.wiptech.Reference;
//import cadiboo.wiptech.network.CSyncModular.CSyncModularHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

	private static final String CHANNEL = Reference.ID;//+"_channel";
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL);

	public PacketHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		int networkIds = 0;
		NETWORK.registerMessage(new PacketUpdateCrusher.Handler(), PacketUpdateCrusher.class, networkIds++, Side.CLIENT);
		NETWORK.registerMessage(new PacketRequestUpdateCrusher.Handler(), PacketRequestUpdateCrusher.class, networkIds++, Side.SERVER);
		NETWORK.registerMessage(new PacketUpdateCoiler.Handler(), PacketUpdateCoiler.class, networkIds++, Side.CLIENT);
		NETWORK.registerMessage(new PacketRequestUpdateCoiler.Handler(), PacketRequestUpdateCoiler.class, networkIds++, Side.SERVER);
		//NETWORK.registerMessage(CSyncModularHandler.class, CSyncModular.class, networkIds++, Side.CLIENT);
	}
}