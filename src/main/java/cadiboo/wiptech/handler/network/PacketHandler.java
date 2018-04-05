package cadiboo.wiptech.handler.network;

//import cadiboo.wiptech.handler.network.ClientSyncModular.ClientSyncModularHandler;
import cadiboo.wiptech.util.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

	private static final String CHANNEL = Reference.ID + "_network_channel";
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL);

	public PacketHandler() {
		int networkIds = 0;
		NETWORK.registerMessage(new PacketUpdateCrusher.Handler(), PacketUpdateCrusher.class, networkIds++,
				Side.CLIENT);
		NETWORK.registerMessage(new PacketRequestUpdateCrusher.Handler(), PacketRequestUpdateCrusher.class,
				networkIds++, Side.SERVER);
		NETWORK.registerMessage(new PacketUpdateCoiler.Handler(), PacketUpdateCoiler.class, networkIds++, Side.CLIENT);
		NETWORK.registerMessage(new PacketRequestUpdateCoiler.Handler(), PacketRequestUpdateCoiler.class, networkIds++,
				Side.SERVER);

		NETWORK.registerMessage(PacketSyncTileEntity.class, PacketSyncTileEntity.class, networkIds++, Side.CLIENT);
		// NETWORK.registerMessage(ClientSyncModularHandler.class,
		// ClientSyncModular.class, networkIds++, Side.CLIENT);
		// NETWORK.registerMessage(CSyncModularHandler.class, CSyncModular.class,
		// networkIds++, Side.CLIENT);
	}
}