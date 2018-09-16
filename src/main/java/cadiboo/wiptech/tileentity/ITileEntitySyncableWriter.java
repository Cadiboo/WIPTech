package cadiboo.wiptech.tileentity;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITileEntitySyncableWriter extends IModTileEntity {

	default void handleSync() {
		new NoSuchMethodError().printStackTrace();
	}

	@Nonnull
	BlockPos getPosition();

	default int getSyncFrequency() {
		return 20;
	}

	@Nonnull
	World getWorld();

	default int getInstaSyncRange() {
		return 6;
	}

	default void syncToClients() {
		new NoSuchMethodError().printStackTrace();
	}

	default void syncToClient(final EntityPlayerMP player) {
		new NoSuchMethodError().printStackTrace();
	}

	/**
	 * Implement this from Tile Entity class
	 */
	SPacketUpdateTileEntity _getUpdatePacket();

	/**
	 * Implement this from Tile Entity class
	 */
	@Nonnull
	NBTTagCompound _getUpdateTag();

	/**
	 * Implement this from Tile Entity class
	 */
	void _onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt);

	/**
	 * Implement this from Tile Entity class
	 */
	void _readFromNBT(final NBTTagCompound compound);

	/**
	 * Implement this from Tile Entity class
	 */
	NBTTagCompound _writeToNBT(final NBTTagCompound compound);

	default boolean shouldSyncToPlayer(final EntityPlayerMP player) {
		new NoSuchMethodError().printStackTrace();
		return false;
	}

	default void syncToServer() {
		new NoSuchMethodError().printStackTrace();
	}

	default int getMaxSyncDistanceSquared() {
		return 64;
	}

}
