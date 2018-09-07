package cadiboo.wiptech.tileentity;

import javax.annotation.Nonnull;

import cadiboo.wiptech.network.ModNetworkManager;
import cadiboo.wiptech.network.play.client.CPacketSyncTileEntity;
import cadiboo.wiptech.network.play.server.SPacketSyncTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITileEntitySyncable {

	default void handleSync() {
		if (!getWorld().isRemote) {
			if ((getWorld().getTotalWorldTime() % getSyncFrequency()) == 0) {
				for (final EntityPlayer player : this.getWorld().playerEntities) {
					syncToClient(player);
				}
				return;
			}

			for (final EntityPlayer player : getWorld().getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(-getInstaSyncRange(), -getInstaSyncRange(), -getInstaSyncRange(), getInstaSyncRange() - 1, getInstaSyncRange() - 1, getInstaSyncRange() - 1).offset(getPosition()))) {
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

	default void syncToClient(final EntityPlayer player) {
		if ((player instanceof EntityPlayerMP) && shouldSyncToPlayer(player)) {
			final NBTTagCompound syncTag = new NBTTagCompound();
			writeNBT(syncTag);
			ModNetworkManager.NETWORK.sendTo(new SPacketSyncTileEntity(syncTag, getPosition()), (EntityPlayerMP) player);
		}
	}

	/**
	 * Implement this from tile Entity class
	 */
	void readFromNBT(NBTTagCompound compound);

	/**
	 * Implement this from tile Entity class
	 */
	NBTTagCompound writeToNBT(NBTTagCompound compound);

	@Nonnull
	void readNBT(NBTTagCompound syncTag);

	@Nonnull
	void writeNBT(NBTTagCompound syncTag);

	default boolean shouldSyncToPlayer(final EntityPlayer player) {
		return player.getDistanceSq(getPosition()) <= this.getMaxSyncDistanceSquared();
	}

	default void syncToServer() {
		final NBTTagCompound syncTag = new NBTTagCompound();
		writeNBT(syncTag);
		ModNetworkManager.NETWORK.sendToServer(new CPacketSyncTileEntity(syncTag, getPosition(), Minecraft.getMinecraft().world.provider.getDimension()));
	}

	default int getMaxSyncDistanceSquared() {
		return 64;
	}

}
