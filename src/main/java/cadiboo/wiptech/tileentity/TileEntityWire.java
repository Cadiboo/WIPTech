package cadiboo.wiptech.tileentity;

import java.util.List;

import cadiboo.wiptech.capability.energy.EnergyNetwork;
import cadiboo.wiptech.capability.energy.EnergyNetworkList;
import cadiboo.wiptech.capability.energy.IEnergyUser;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
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

	private EnergyNetwork network;
	private final ModEnergyStorage energy;

	public TileEntityWire() {
		super();
		this.energy = new ModEnergyStorage(10000);
	}

	@Override
	public void setPos(BlockPos posIn) {
		if (this.network != null)
			this.network.remove(getPos());
		this.network = EnergyNetworkList.INSTANCE.getOrCreateNetworkFor(this.world, posIn, energy);
		super.setPos(posIn);
	}

	@Override
	public final void update() {
		handleSync();
		getElectrocutableEntities().forEach((entityIn) -> {

			if (!(entityIn instanceof EntityLivingBase))
				return;

			EntityLivingBase entity = (EntityLivingBase) entityIn;

			if (shouldElectrocuteEntity(entity) && !entity.isDead) {

				float damage = Math.min(getElectrocutionDamage(), entity.getHealth());

				if (getEnergy().extractEnergy(getElectrocutionEnergy(), true) == getElectrocutionEnergy()) {

					entity.attackEntityFrom(ModDamageSource.ELECTRICITY, damage);
					entity.setFire(1);
				}

			}
		});
		transferEnergyToAllAround();
		network.update();
	}

	@Override
	public int transferEnergyTo(EnumFacing side, int energyToTransfer, boolean simulate) {
//		if (IEnergyUser.super.transferEnergyTo(side, energyToTransfer, true) == energyToTransfer && (getEnergy().getStorage(side) != energy.getLastRecieved() || world.getTotalWorldTime() % 10 == 0))
//			return IEnergyUser.super.transferEnergyTo(side, energyToTransfer, simulate);
		return 0;
	}

	private float getElectrocutionDamage() {
		return getElectrocutionEnergy() / 0.0001f;
	}

	private int getElectrocutionEnergy() {
		return 1;
	}

	public boolean shouldElectrocuteEntity(Entity entity) {
		return entity instanceof EntityLivingBase && this.getEnergy().getEnergyStored() >= getElectrocutionEnergy();
	}

	public List<Entity> getElectrocutableEntities() {
		return this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPos()).grow(getElectrocutionRage()), EntitySelectors.CAN_AI_TARGET);
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return Block.FULL_BLOCK_AABB.offset(this.getPos());
	}

	@Override
	public ModEnergyStorage getEnergy() {
		return energy;
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return Math.pow(getElectrocutionRage(), 2);
	}

	private int getElectrocutionRage() {
		return 5;
	}

	@Override
	public BlockPos getPosition() {
		return getPos();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return (T) getEnergy();
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.readNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		this.writeNBT(compound);
		return compound;
	}

	@Override
	public void readNBT(NBTTagCompound syncTag) {
		if (syncTag.hasKey("energy"))
			this.getEnergy().setEnergyStored(syncTag.getInteger("energy"));
	}

	@Override
	public void writeNBT(NBTTagCompound syncTag) {
		syncTag.setInteger("energy", getEnergy().getEnergyStored());
	}

	public EnergyNetwork getNetwork() {
		return network;
	}

}
