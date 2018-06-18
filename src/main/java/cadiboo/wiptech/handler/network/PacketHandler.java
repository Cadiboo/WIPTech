package cadiboo.wiptech.handler.network;

//import cadiboo.wiptech.handler.network.ClientSyncModular.ClientSyncModularHandler;
import cadiboo.wiptech.util.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

	private static final String					CHANNEL	= Reference.ID + "_net_chanel";							// CANT BE LONGER THAN 20 CHARS
	public static final SimpleNetworkWrapper	NETWORK	= NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL);

	public PacketHandler() {
		int networkIds = 0;
		NETWORK.registerMessage(new PacketUpdateCrusher.Handler(), PacketUpdateCrusher.class, networkIds++, Side.CLIENT);
		NETWORK.registerMessage(new PacketRequestUpdateCrusher.Handler(), PacketRequestUpdateCrusher.class, networkIds++, Side.SERVER);
		NETWORK.registerMessage(new PacketUpdateCoiler.Handler(), PacketUpdateCoiler.class, networkIds++, Side.CLIENT);
		NETWORK.registerMessage(new PacketRequestUpdateCoiler.Handler(), PacketRequestUpdateCoiler.class, networkIds++, Side.SERVER);

		// Client -> Server
		NETWORK.registerMessage(CPacketSyncTileEntity.class, CPacketSyncTileEntity.class, networkIds++, Side.SERVER);
		// Server -> Client
		NETWORK.registerMessage(SPacketSyncTileEntity.class, SPacketSyncTileEntity.class, networkIds++, Side.CLIENT);
	}
}