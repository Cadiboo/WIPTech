package cadiboo.wiptech.network;

import cadiboo.wiptech.network.play.client.CPacketSyncEntity;
import cadiboo.wiptech.network.play.client.CPacketSyncTileEntity;
import cadiboo.wiptech.network.play.server.SPacketSyncEntity;
import cadiboo.wiptech.network.play.server.SPacketSyncTileEntity;
import cadiboo.wiptech.util.ModReference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetworkManager {

	/**
	 * CANT BE LONGER THAN 20 CHARS
	 */
	public static final String CHANNEL = ModReference.Version.getModId() + "_chanel";
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL);

	public ModNetworkManager() {
		int networkIds = 0;
		/* Client -> Server */
		NETWORK.registerMessage(CPacketSyncTileEntity.class, CPacketSyncTileEntity.class, networkIds++, Side.SERVER);
		/* Server -> Client */
		NETWORK.registerMessage(SPacketSyncTileEntity.class, SPacketSyncTileEntity.class, networkIds++, Side.CLIENT);

		/* Client -> Server */
		NETWORK.registerMessage(CPacketSyncEntity.class, CPacketSyncEntity.class, networkIds++, Side.SERVER);
		/* Server -> Client */
		NETWORK.registerMessage(SPacketSyncEntity.class, SPacketSyncEntity.class, networkIds++, Side.CLIENT);
	}

}
