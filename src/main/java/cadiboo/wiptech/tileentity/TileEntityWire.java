package cadiboo.wiptech.tileentity;

import java.util.List;

import cadiboo.wiptech.capability.energy.IEnergyUser;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import cadiboo.wiptech.capability.energy.network.CapabilityEnergyNetworkList;
import cadiboo.wiptech.util.ModDamageSource;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityWire extends TileEntity implements ITickable, IEnergyUser, ITileEntitySyncable {

	private final ModEnergyStorage energy;

	public TileEntityWire() {
		super();
		this.energy = new ModEnergyStorage(10000);
	}

	@Override
	public void setPos(final BlockPos pos) {
		super.setPos(pos);
	}

	@Override
	public void onLoad() {
		if (this.world.hasCapability(CapabilityEnergyNetworkList.NETWORK_LIST, null)) {
			if (this.world.getCapability(CapabilityEnergyNetworkList.NETWORK_LIST, null) != null) {
				this.world.getCapability(CapabilityEnergyNetworkList.NETWORK_LIST, null).addConnection(this.pos);
			}
		}
		super.onLoad();
	}

	@Override
	public final void update() {
		this.handleSync();
		this.getElectrocutableEntities().forEach((entity) -> {

			if (!(entity instanceof EntityLivingBase)) {
				return;
			}

			final EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

			if (this.shouldElectrocuteEntity(entityLivingBase) && !entityLivingBase.isDead) {

				final float damage = Math.min(this.getElectrocutionDamage(), entityLivingBase.getHealth());

				if (this.getEnergy().extractEnergy(this.getElectrocutionEnergy(), true) == this.getElectrocutionEnergy()) {

					entityLivingBase.attackEntityFrom(ModDamageSource.ELECTRICITY, damage);
					entityLivingBase.setFire(1);
				}

			}
		});
		this.transferEnergyToAllAround();
	}

	@Override
	public int transferEnergyTo(final EnumFacing side, final int energyToTransfer, final boolean simulate) {
		// if (IEnergyUser.super.transferEnergyTo(side, energyToTransfer, true) == energyToTransfer && (getEnergy().getStorage(side) != energy.getLastRecieved() || world.getTotalWorldTime() % 10 == 0))
		// return IEnergyUser.super.transferEnergyTo(side, energyToTransfer, simulate);
		return 0;
	}

	private float getElectrocutionDamage() {
		return this.getElectrocutionEnergy() / 0.0001f;
	}

	private int getElectrocutionEnergy() {
		return 1;
	}

	public boolean shouldElectrocuteEntity(final Entity entity) {
		return (entity instanceof EntityLivingBase) && (this.getEnergy().getEnergyStored() >= this.getElectrocutionEnergy());
	}

	public List<Entity> getElectrocutableEntities() {
		return this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.getPos()).grow(this.getElectrocutionRage()), EntitySelectors.CAN_AI_TARGET);
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return Block.FULL_BLOCK_AABB.offset(this.getPos());
	}

	@Override
	public ModEnergyStorage getEnergy() {
		return this.energy;
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return Math.pow(this.getElectrocutionRage(), 2);
	}

	private int getElectrocutionRage() {
		return 5;
	}

	@Override
	public BlockPos getPosition() {
		return this.getPos();
	}

	@Override
	public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return (T) this.getEnergy();
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.readNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		this.writeNBT(compound);
		return compound;
	}

	@Override
	public void readNBT(final NBTTagCompound syncTag) {
		if (syncTag.hasKey("energy")) {
			this.getEnergy().setEnergyStored(syncTag.getInteger("energy"), false);
		}
	}

	@Override
	public void writeNBT(final NBTTagCompound syncTag) {
		syncTag.setInteger("energy", this.getEnergy().getEnergyStored());
	}

}
