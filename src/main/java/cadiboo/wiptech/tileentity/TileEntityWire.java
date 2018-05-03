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
		// electrocutionTime--;
		if (!world.isRemote && energy.getEnergyStored() > 0) {
			if (Utils.getBlockFromPos(world, pos) instanceof BlockWire && !((BlockWire) Utils.getBlockFromPos(world, pos)).isEnamel()) {
				getAllEntitiesWithinRangeAt(pos.getX(), pos.getY(), pos.getZ(), 3).forEach(entity -> {
					if (entity instanceof EntityCreeper) {
						if (!((EntityCreeper) entity).getPowered()) {
							this.electrocutionTime = 7;
							syncToClients();
							((EntityCreeper) entity).onStruckByLightning(null);
							((EntityCreeper) entity).extinguish();
						}
					} else if (entity instanceof EntityParamagneticProjectile) { // TODO remove this in 1.13
						((EntityParamagneticProjectile) entity).setAmmoId(9); // PLASMA
						this.electrocutionTime = 7;
						syncToClients();
					} else if (entity instanceof EntityParamagneticProjectile113) {
						((EntityParamagneticProjectile113) entity).setType(ParamagneticProjectiles.PLASMA);
						this.electrocutionTime = 7;
						syncToClients();
					} else if (!(entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative() && EntitySelectors.NOT_SPECTATING.apply(entity)) && entity instanceof EntityLivingBase) {
						entity.attackEntityFrom(DamageSource.causeElectricityDamage(), (float) (0.001 * energy.extractEnergy((int) ((EntityLivingBase) entity).getHealth() * 1000, false)));
						this.electrocutionTime = 7;
						return; // stop transmission logic
					}
				});
			}

			for (int i = 0; i < EnumFacing.VALUES.length; i++) {
				if (lastRecieved != null)
					transmitEnergy(EnumFacing.VALUES[(lastRecieved.getIndex() + i + 1) % EnumFacing.VALUES.length]);
				else
					transmitEnergy(EnumFacing.VALUES[i]);
			}
			ArrayList<EntityPlayer> playersInRange = getAllPlayersWithinRangeAt(pos.getX(), pos.getY(), pos.getZ(), 6);

			if (getWorld().getWorldTime() % 200 == 0)
				syncToClients();
			else if (playersInRange.size() > 0)
				playersInRange.forEach(player -> {
					this.syncToClient(player);
				});
		}
		this.energy.setCapacity(Math.round(Configuration.energy.BaseWireStorage * ((BlockWire) this.getBlockType()).getMetal().getConductivityFraction()));
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

	public List<Entity> getAllEntitiesWithinRangeAt(double x, double y, double z, double range) {
		return this.getWorld().getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(x - range / 2, y - range / 2, z - range / 2, x + range / 2, y + range / 2, z + range / 2));
	}

	public ArrayList<EntityPlayer> getAllPlayersWithinRangeAt(double x, double y, double z, double range) {
		ArrayList<EntityPlayer> list = new ArrayList<EntityPlayer>();
		for (int j2 = 0; j2 < this.getWorld().playerEntities.size(); ++j2) {
			EntityPlayer entityplayer = this.getWorld().playerEntities.get(j2);

			if (EntitySelectors.NOT_SPECTATING.apply(entityplayer)) {
				double d0 = entityplayer.getDistanceSq(x, y, z);

				if (range < 0.0D || d0 < range * range) {
					list.add(entityplayer);
				}
			}
		}
		return list;
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
		return 16384;
	}

}
