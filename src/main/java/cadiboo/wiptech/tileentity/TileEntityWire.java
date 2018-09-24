package cadiboo.wiptech.tileentity;

import java.util.List;

import cadiboo.wiptech.capability.energy.IEnergyUserAdvanced;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import cadiboo.wiptech.capability.energy.network.CapabilityEnergyNetworkList;
import cadiboo.wiptech.util.ModDamageSource;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityWire extends TileEntity implements IModTileEntity, ITickable, IEnergyUserAdvanced, ITileEntitySyncable, ITileEntityNetworkConnection {

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
	public boolean canTransferEnergyTo(final EnumFacing side, final int energyToTransfer) {
		if (!this.getEnergy().canExtract()) {
			return false;
		}

		if (this.getWorld() == null) {
			return false;
		}

		if (this.getWorld().isRemote) {
			return false;
		}

		if (this.getWorld().getTileEntity(this.getPosition().offset(side)) == null) {
			return false;
		}

		if (this.getWorld().getTileEntity(this.getPosition().offset(side)) instanceof ITileEntityNetworkConnection) {
			return false;
		}

		final IEnergyStorage storage = this.getWorld().getTileEntity(this.getPosition().offset(side)).getCapability(CapabilityEnergy.ENERGY, side.getOpposite());

		if (storage == null) {
			return false;
		}

		if (!storage.canReceive()) {
			return false;
		}

		return true;
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
		return this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.getPos()).grow(this.getElectrocutionRange()), EntitySelectors.CAN_AI_TARGET);
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
		return Math.pow(this.getElectrocutionRange(), 2);
	}

	private int getElectrocutionRange() {
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
		if (compound.hasKey(ENERGY_TAG)) {
			this.getEnergy().setEnergyStored(compound.getInteger(ENERGY_TAG), false);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger(ENERGY_TAG, this.getEnergy().getEnergyStored());
		return compound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

}
