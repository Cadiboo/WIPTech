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

	public default void handleSync() {
		if (!getWorld().isRemote) {
			if (getWorld().getTotalWorldTime() % getSyncFrequency() == 0) {
				syncToTracking();
				return;
			}

			for (EntityPlayer player : getWorld().getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(-getInstaSyncRange(), -getInstaSyncRange(), -getInstaSyncRange(), getInstaSyncRange()
					- 1, getInstaSyncRange() - 1, getInstaSyncRange() - 1).offset(getPosition()))) {
				syncToClient(player);
			}
		} else if (getWorld().isRemote) {

		}

	}

	public default void syncToTracking() {
		if (getWorld().isRemote) {
			WIPTech.error("Entity on the client attempted to send a server side sync to all clients that are tracking it. Only the server knows this list");
			new Exception().printStackTrace();
			return;
		}
		NBTTagCompound syncTag = new NBTTagCompound();
		writeSyncTag(syncTag);
		ModNetworkManager.NETWORK.sendToAllTracking(new SPacketSyncEntity(getEntity().getEntityId(), syncTag), getEntity());
	};

	public Entity getEntity();

	@Nonnull
	public BlockPos getPosition();

	public default int getSyncFrequency() {
		return 20;
	}

	@Nonnull
	public World getWorld();

	public default int getInstaSyncRange() {
		return 6;
	}

	public default void syncToClient(EntityPlayer player) {
		if (getWorld().isRemote) {
			WIPTech.error("Entity on the client attempted to send a server side sync to " + player.getName());
			new Exception().printStackTrace();
			return;
		}
		if (player instanceof EntityPlayerMP && shouldSyncToPlayer(player)) {
			NBTTagCompound syncTag = new NBTTagCompound();
			writeSyncTag(syncTag);
			ModNetworkManager.NETWORK.sendTo(new SPacketSyncEntity(getEntity().getEntityId(), syncTag), (EntityPlayerMP) player);
		}
	}

	public void writeSyncTag(NBTTagCompound compound);

	public void readSyncTag(NBTTagCompound compound);

	public default boolean shouldSyncToPlayer(EntityPlayer player) {
		return player.getDistanceSq(getPosition()) <= this.getMaxSyncDistanceSquared();
	}

	public default void syncToServer() {
		if (!getWorld().isRemote) {
			WIPTech.error("Entity on the sever attempted to send a client side sync to itself");
			new Exception().printStackTrace();
			return;
		}
		NBTTagCompound syncTag = new NBTTagCompound();
		writeSyncTag(syncTag);
		ModNetworkManager.NETWORK.sendToServer(new CPacketSyncEntity(Minecraft.getMinecraft().world.provider.getDimension(), getEntity().getEntityId(), syncTag));
	}

	public default int getMaxSyncDistanceSquared() {
		return 64;
	}

}
