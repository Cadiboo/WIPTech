package cadiboo.wiptech.entity;

import javax.annotation.Nonnull;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.network.ModNetworkManager;
import cadiboo.wiptech.network.play.client.CPacketSyncEntity;
import cadiboo.wiptech.network.play.server.SPacketSyncEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEntitySyncable {

	default void handleSync() {
		if (!getWorld().isRemote) {
			if ((getWorld().getTotalWorldTime() % getSyncFrequency()) == 0) {
				syncToTracking();
				return;
			}

			for (final EntityPlayer player : getWorld().getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(-getInstaSyncRange(), -getInstaSyncRange(), -getInstaSyncRange(), getInstaSyncRange() - 1, getInstaSyncRange() - 1, getInstaSyncRange() - 1).offset(getPosition()))) {
				syncToClient(player);
			}
		} else if (getWorld().isRemote) {

		}

	}

	default void syncToTracking() {
		if (getWorld().isRemote) {
			WIPTech.error("Entity on the client attempted to send a server side sync to all clients that are tracking it. Only the server knows this list");
			new Exception().printStackTrace();
			return;
		}
		final NBTTagCompound syncTag = new NBTTagCompound();
		writeSyncTag(syncTag);
		ModNetworkManager.NETWORK.sendToAllTracking(new SPacketSyncEntity(getEntity().getEntityId(), syncTag), getEntity());
	};

	Entity getEntity();

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
		if (getWorld().isRemote) {
			WIPTech.error("Entity on the client attempted to send a server side sync to " + player.getName());
			new Exception().printStackTrace();
			return;
		}
		if ((player instanceof EntityPlayerMP) && shouldSyncToPlayer(player)) {
			final NBTTagCompound syncTag = new NBTTagCompound();
			writeSyncTag(syncTag);
			ModNetworkManager.NETWORK.sendTo(new SPacketSyncEntity(getEntity().getEntityId(), syncTag), (EntityPlayerMP) player);
		}
	}

	void writeSyncTag(NBTTagCompound compound);

	void readSyncTag(NBTTagCompound compound);

	default boolean shouldSyncToPlayer(final EntityPlayer player) {
		return player.getDistanceSq(getPosition()) <= this.getMaxSyncDistanceSquared();
	}

	default void syncToServer() {
		if (!getWorld().isRemote) {
			WIPTech.error("Entity on the sever attempted to send a client side sync to itself");
			new Exception().printStackTrace();
			return;
		}
		final NBTTagCompound syncTag = new NBTTagCompound();
		writeSyncTag(syncTag);
		ModNetworkManager.NETWORK.sendToServer(new CPacketSyncEntity(Minecraft.getMinecraft().world.provider.getDimension(), getEntity().getEntityId(), syncTag));
	}

	default int getMaxSyncDistanceSquared() {
		return 64;
	}

}
