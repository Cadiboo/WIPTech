package cadiboo.wiptech.tileentity;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITileEntitySyncable extends IModTileEntity {

	default void handleSync() {
		if (!getWorld().isRemote) {
			if ((getWorld().getTotalWorldTime() % getSyncFrequency()) == 0) {
				syncToClients();
				return;
			}

			for (final EntityPlayerMP player : getWorld().getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(-getInstaSyncRange(), -getInstaSyncRange(), -getInstaSyncRange(), getInstaSyncRange() - 1, getInstaSyncRange() - 1, getInstaSyncRange() - 1).offset(getPosition()))) {
				syncToClient(player);
			}
		} else {

		}

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
		for (final EntityPlayer player : this.getWorld().playerEntities) {
			if (!(player instanceof EntityPlayerMP)) {
				continue;
			}
			syncToClient((EntityPlayerMP) player);
		}
	}

	default void syncToClient(final EntityPlayerMP player) {
		if (shouldSyncToPlayer(player)) {
			final NBTTagCompound syncTag = writeToNBT(new NBTTagCompound());
			player.connection.sendPacket(getUpdatePacket());
		}
	}

	/**
	 * Implement this from Tile Entity class
	 */
	SPacketUpdateTileEntity getUpdatePacket();

	/**
	 * Implement this from Tile Entity class
	 */
	void readFromNBT(NBTTagCompound compound);

	/**
	 * Implement this from Tile Entity class
	 */
	NBTTagCompound writeToNBT(NBTTagCompound compound);

	default boolean shouldSyncToPlayer(final EntityPlayerMP player) {
		return player.getDistanceSq(getPosition()) <= this.getMaxSyncDistanceSquared();
	}

	default void syncToServer() {
		final NBTTagCompound syncTag = writeToNBT(new NBTTagCompound());
		new NoSuchMethodError().printStackTrace();
//		ModNetworkManager.NETWORK.sendToServer(new CPacketSyncTileEntity(syncTag, getPosition(), Minecraft.getMinecraft().world.provider.getDimension()));
	}

	default int getMaxSyncDistanceSquared() {
		return 64;
	}

}
