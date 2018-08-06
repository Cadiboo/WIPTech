package cadiboo.wiptech.network;

import cadiboo.wiptech.network.play.client.CPacketSyncModTileEntity;
import cadiboo.wiptech.network.play.server.SPacketSyncModTileEntity;
import cadiboo.wiptech.util.ModReference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetworkManager {

	/**
	 * CANT BE LONGER THAN 20 CHARS
	 */
	public static final String					CHANNEL	= ModReference.ID + "_chanel";
	public static final SimpleNetworkWrapper	NETWORK	= NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL);

	public ModNetworkManager() {
		int networkIds = 0;
		/* Client -> Server */
		NETWORK.registerMessage(CPacketSyncModTileEntity.class, CPacketSyncModTileEntity.class, networkIds++, Side.SERVER);
		/* Server -> Client */
		NETWORK.registerMessage(SPacketSyncModTileEntity.class, SPacketSyncModTileEntity.class, networkIds++, Side.CLIENT);
	}

}
