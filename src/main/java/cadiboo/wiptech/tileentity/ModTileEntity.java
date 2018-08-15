package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.capability.ModEnergyStorage;
import cadiboo.wiptech.capability.ModItemStackHandler;
import cadiboo.wiptech.network.ModNetworkManager;
import cadiboo.wiptech.network.play.client.CPacketSyncModTileEntity;
import cadiboo.wiptech.network.play.server.SPacketSyncModTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public abstract class ModTileEntity extends TileEntity {

	public abstract ModEnergyStorage getEnergy();

	public abstract ModItemStackHandler getInventory();

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY)
			return (T) getEnergy();
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return this.getCapability(capability, facing) != null;
	}

	@Override
	public final void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.readNBT(compound);
	}

	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		this.writeNBT(compound);
		return compound;
	}

	public void writeNBT(NBTTagCompound nbt) {
		nbt.setInteger("energy", this.getEnergy().getEnergyStored());
	}

	public void readNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("energy"))
			this.getEnergy().setEnergy(nbt.getInteger("energy"));
	}

	public void handleSync() {
		if (!this.world.isRemote) {
			if (world.getTotalWorldTime() % 10 == 0) {
				for (EntityPlayer player : this.world.playerEntities) {
					syncToClient(player);
				}
				return;
			}

			for (EntityPlayer player : this.world.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(-getInstaSyncRange(), -getInstaSyncRange(), -getInstaSyncRange(), getInstaSyncRange()
					- 1, getInstaSyncRange() - 1, getInstaSyncRange() - 1).offset(this.pos))) {
				syncToClient(player);
			}
		} else {

		}

	}

	private int getInstaSyncRange() {
		return 6;
	}

	public void syncToClient(EntityPlayer player) {
		NBTTagCompound syncTag = new NBTTagCompound();
		this.writeNBT(syncTag);

		if (player instanceof EntityPlayerMP && shouldSyncToPlayer(player)) {
			ModNetworkManager.NETWORK.sendTo(new SPacketSyncModTileEntity(syncTag, this.pos), (EntityPlayerMP) player);
		}
	}

	public boolean shouldSyncToPlayer(EntityPlayer player) {
		return player.getDistanceSq(this.getPos()) <= this.getMaxSyncDistanceSquared();
	}

	public void syncToServer() {
		NBTTagCompound syncTag = new NBTTagCompound();
		this.writeNBT(syncTag);

		ModNetworkManager.NETWORK.sendToServer(new CPacketSyncModTileEntity(syncTag, this.pos, Minecraft.getMinecraft().world.provider.getDimension()));
	}

	public int getMaxSyncDistanceSquared() {
		return 64;
	}

	public BlockPos getPosition() {
		return getPos();
	}

}
