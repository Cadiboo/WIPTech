package cadiboo.wiptech.network;

import cadiboo.wiptech.network.play.client.CPacketSyncEntity;
import cadiboo.wiptech.network.play.server.SPacketSyncEnergyNetworkList;
import cadiboo.wiptech.network.play.server.SPacketSyncEntity;
import cadiboo.wiptech.util.ModReference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Manages the registry of the of network packets (it does more than this but everythings handled automagically by forge)
 * @author Cadiboo
 */
public final class ModNetworkManager {

	/**
	 * CHANEL CAN'T be longer than 20 characters due to Minecraft & forge's packet system
	 */
	public static final String					CHANNEL	= ModReference.MOD_ID + "_chanel";
	public static final SimpleNetworkWrapper	NETWORK	= NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL);

	public ModNetworkManager() {
		int networkIds = 0;
//		/* Client -> Server */
//		NETWORK.registerMessage(CPacketSyncTileEntity.class, CPacketSyncTileEntity.class, networkIds++, Side.SERVER);
//		/* Server -> Client */
//		NETWORK.registerMessage(SPacketSyncTileEntity.class, SPacketSyncTileEntity.class, networkIds++, Side.CLIENT);

		/* Client -> Server */
		NETWORK.registerMessage(CPacketSyncEntity.class, CPacketSyncEntity.class, networkIds++, Side.SERVER);
		/* Server -> Client */
		NETWORK.registerMessage(SPacketSyncEntity.class, SPacketSyncEntity.class, networkIds++, Side.CLIENT);

		/* Server -> Client */
		NETWORK.registerMessage(SPacketSyncEnergyNetworkList.class, SPacketSyncEnergyNetworkList.class, networkIds++, Side.CLIENT);
	}

}
