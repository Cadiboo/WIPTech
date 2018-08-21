package cadiboo.wiptech.tileentity;

import javax.annotation.Nonnull;

import cadiboo.wiptech.network.ModNetworkManager;
import cadiboo.wiptech.network.play.client.CPacketSyncModTileEntity;
import cadiboo.wiptech.network.play.server.SPacketSyncModTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITileEntitySyncable {

	public default void handleSync() {
		if (!getWorld().isRemote) {
			if (getWorld().getTotalWorldTime() % getSyncFrequency() == 0) {
				for (EntityPlayer player : this.getWorld().playerEntities) {
					syncToClient(player);
				}
				return;
			}

			for (EntityPlayer player : getWorld().getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(-getInstaSyncRange(), -getInstaSyncRange(), -getInstaSyncRange(), getInstaSyncRange()
					- 1, getInstaSyncRange() - 1, getInstaSyncRange() - 1).offset(getPosition()))) {
				syncToClient(player);
			}
		} else {

		}

	}

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
		if (player instanceof EntityPlayerMP && shouldSyncToPlayer(player)) {
			NBTTagCompound syncTag = new NBTTagCompound();
			writeNBT(syncTag);
			ModNetworkManager.NETWORK.sendTo(new SPacketSyncModTileEntity(syncTag, getPosition()), (EntityPlayerMP) player);
		}
	}

	public void readFromNBT(NBTTagCompound compound);

	public NBTTagCompound writeToNBT(NBTTagCompound compound);

	@Nonnull
	public void readNBT(NBTTagCompound syncTag);

	@Nonnull
	public void writeNBT(NBTTagCompound syncTag);

	public default boolean shouldSyncToPlayer(EntityPlayer player) {
		return player.getDistanceSq(getPosition()) <= this.getMaxSyncDistanceSquared();
	}

	public default void syncToServer() {
		NBTTagCompound syncTag = new NBTTagCompound();
		writeNBT(syncTag);
		ModNetworkManager.NETWORK.sendToServer(new CPacketSyncModTileEntity(syncTag, getPosition(), Minecraft.getMinecraft().world.provider.getDimension()));
	}

	public default int getMaxSyncDistanceSquared() {
		return 64;
	}

}
