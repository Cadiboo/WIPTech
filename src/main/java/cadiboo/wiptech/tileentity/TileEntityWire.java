package cadiboo.wiptech.tileentity;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.config.Configuration;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile113;
import cadiboo.wiptech.handler.EnumHandler.ParamagneticProjectiles;
import cadiboo.wiptech.util.CustomEnergyStorage;
import cadiboo.wiptech.util.DamageSource;
import cadiboo.wiptech.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityWire extends TileEntityBase implements ITickable {

	private CustomEnergyStorage energy = new CustomEnergyStorage(Configuration.energy.BaseWireStorage, Integer.MAX_VALUE) {
		@Override
		public int receiveEnergy(int receiveEnergy, boolean simulate, EnumFacing side) {
			lastRecieved = side;
			return super.receiveEnergy(receiveEnergy, simulate, side);
		};
	};

	private EnumFacing lastRecieved;

	public int electrocutionTime;

	@Override
	public void update() {
		electrocutionTime--;
		if (!world.isRemote && energy.getEnergyStored() > 0) {
			if (Utils.getBlockFromPos(world, pos) instanceof BlockWire && !((BlockWire) Utils.getBlockFromPos(world, pos)).isEnamel()) {

				this.getElectrocutableEntities().forEach(entity -> {
					this.electrocuteEntity(entity);
				});

				if (energy.getEnergyStored() < 10) // electrocution drained energy
					return;
			}

			for (int i = 0; i < EnumFacing.VALUES.length; i++) {
				if (lastRecieved != null)
					transmitEnergy(EnumFacing.VALUES[(lastRecieved.getIndex() + i + 1) % EnumFacing.VALUES.length]);
				else
					transmitEnergy(EnumFacing.VALUES[i]);
			}

			handleSync();
		}
		this.energy.setCapacity(Math.round(Configuration.energy.BaseWireStorage * ((BlockWire) this.getBlockType()).getMetal().getConductivityFraction()));
	}

	private void electrocuteEntity(Entity entity) {
		if (entity instanceof EntityCreeper) {
			if (!((EntityCreeper) entity).getPowered()) {
				this.electrocutionTime = 10;
				((EntityCreeper) entity).onStruckByLightning(null);
				((EntityCreeper) entity).extinguish();
			}
		} else if (entity instanceof EntityParamagneticProjectile) { // TODO remove this in 1.13
			if (((EntityParamagneticProjectile) entity).isPlasma())
				this.electrocutionTime = 7;
			else if (energy.extractEnergy(1000, true) == 1000) {
				((EntityParamagneticProjectile) entity).setAmmoId(9); // PLASMA
				energy.extractEnergy(1000, false);
				this.electrocutionTime = 7;
			}
		} else if (entity instanceof EntityParamagneticProjectile113) {
			if (((EntityParamagneticProjectile113) entity).isPlasma())
				this.electrocutionTime = 7;
			else if (energy.extractEnergy(1000, true) == 1000) {
				((EntityParamagneticProjectile113) entity).setType(ParamagneticProjectiles.PLASMA);
				energy.extractEnergy(1000, false);
				this.electrocutionTime = 7;
			}
		} else if (entity instanceof EntityLivingBase) {
			IItemHandler inventory = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if (inventory != null)
				for (int i = 0; i < inventory.getSlots(); i++) {
					if (inventory.getStackInSlot(i).getCapability(CapabilityEnergy.ENERGY, null) != null) {
						inventory.getStackInSlot(i).getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(energy.getEnergyStored(), false);
					}
				}
			entity.attackEntityFrom(DamageSource.causeElectricityDamage(), (float) (0.001 * energy.extractEnergy(Math.round(((EntityLivingBase) entity).getHealth()) * 1000, false)));
			this.electrocutionTime = 5;
		}
	}

	public List<Entity> getElectrocutableEntities() {
		ArrayList<Entity> electrocutable = new ArrayList<Entity>();

		List<Entity> entities = getAllEntitiesWithinRangeAt(pos.getX(), pos.getY(), pos.getZ(), 3);
		for (int i = 0; i < entities.size(); i++) {
			if (!(entities.get(i) instanceof EntityParamagneticProjectile) && !(entities.get(i) instanceof EntityParamagneticProjectile113) && !(entities.get(i) instanceof EntityCreeper)) {
				if (!EntitySelectors.NOT_SPECTATING.apply(entities.get(i)))
					continue;
				if (!entities.get(i).canBeCollidedWith())
					continue;
				if (entities.get(i) instanceof EntityPlayer && ((EntityPlayer) entities.get(i)).isCreative())
					continue;
			}
			// if (entities.get(i) instanceof EntityCreeper && ((EntityCreeper)
			// entities.get(i)).getPowered())
			// continue;

			electrocutable.add(entities.get(i));
		}
		return electrocutable;
	}

	public void transmitEnergy(EnumFacing side) {
		ArrayList<EnumFacing> connectedSides = new ArrayList<EnumFacing>();
		for (EnumFacing face : EnumFacing.VALUES)
			if (isConnectedTo(face))
				connectedSides.add(face);

		if (!isConnectedTo(side))
			return;
		if (side.equals(lastRecieved) && world.getTotalWorldTime() % 20 != 0)
			return;
		TileEntity tileEntity = world.getTileEntity(pos.offset(side));
		if (tileEntity == null)
			return;
		IEnergyStorage storage = tileEntity.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
		if (storage == null)
			return;
		if (isConnectedToWire(side)) {
			CustomEnergyStorage wireStorage = (CustomEnergyStorage) storage;
			if (wireStorage.getEnergyStored() < energy.getEnergyStored()) {
				energy.extractEnergy(wireStorage.receiveEnergy(energy.extractEnergy(energy.getEnergyStored() / connectedSides.size(), true), false, side.getOpposite()), false);
			}
		} else if (storage.canReceive() /* && !storage.canExtract() */) // not a wire from my mod or a generator from any mod (probably a machine)
			energy.extractEnergy(storage.receiveEnergy(energy.extractEnergy(energy.getEnergyStored() / connectedSides.size(), true), false), false);

	}

	public boolean isConnectedTo(EnumFacing face) {
		return (world.getTileEntity(pos.offset(face)) != null && world.getTileEntity(pos.offset(face)).getCapability(CapabilityEnergy.ENERGY, face.getOpposite()) != null);
	}

	public boolean isConnectedToWire(EnumFacing face) {
		return (isConnectedTo(face) && Utils.getBlockFromPos(world, pos.offset(face)) instanceof BlockWire);
	}

	@Override
	public IEnergyStorage getEnergy(EnumFacing side) {
		return this.energy;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(0111);
	}

	public boolean dumpEnergy() {
		if (!world.isRemote && energy.canExtract()) {
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = world.getTileEntity(pos.offset(side));
				if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite())) {
					IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
					if (storage != null && storage.canReceive()) {
						return energy.extractEnergy(storage.receiveEnergy(energy.extractEnergy(Integer.MAX_VALUE, true), false), false) == energy.getEnergyStored();
					}
				}
			}
		}
		return false;
	}

	@Override
	public void writeNBT(NBTTagCompound nbt, NBTType type) {
		super.writeNBT(nbt, type);
		if (type != NBTType.DROP)
			if (lastRecieved != null)
				nbt.setInteger("lastRecieved", lastRecieved.getIndex());
		if (electrocutionTime > 0)
			nbt.setInteger("electrocutionTime", electrocutionTime);
	}

	@Override
	public void readNBT(NBTTagCompound nbt, NBTType type) {
		super.readNBT(nbt, type);
		if (nbt.hasKey("lastRecieved"))
			this.lastRecieved = EnumFacing.VALUES[nbt.getInteger("lastRecieved")];
		if (nbt.hasKey("electrocutionTime"))
			this.electrocutionTime = Integer.valueOf(nbt.getInteger("electrocutionTime"));
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return 4096;
	}

	@Override
	protected double getMaxSyncDistanceSquared() {
		return getMaxRenderDistanceSquared();
	}

	public void electrocuteBreaker(EntityPlayer player) {
		this.electrocutionTime = 7;
		player.attackEntityFrom(DamageSource.causeElectricityDamage(), (float) (0.001 * energy.extractEnergy(energy.getEnergyStored(), false)));
	}

	@Override
	public boolean handleSync() {
		if (!super.handleSync() && this.electrocutionTime > 0) {
			syncToClients();
			return true;
		}
		return false;
	}

}
