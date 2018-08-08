package cadiboo.wiptech.tileentity;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.capability.ModEnergyStorage;
import cadiboo.wiptech.util.ModDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityWire extends ModTileEntity implements ITickable {

	private final ModEnergyStorage energy;

	public TileEntityWire() {
		this.energy = new ModEnergyStorage(10000);
	}

	public boolean isConnectedTo(EnumFacing side) {
		TileEntity tile = this.getWorld().getTileEntity(getPos().offset(side));
		if (tile == null)
			return false;
		return tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()) != null;
	}

	@Override
	public final void update() {
		handleSync();
		getElectrocutableEntities().forEach((entity) -> {
			if (shouldElectrocuteEntity(entity) && !entity.isDead)
				if (energy.extractEnergy(getElectrocutionEnergy(), false) == getElectrocutionEnergy()) {
					entity.attackEntityFrom(ModDamageSource.ELECTRICITY.setMagicDamage(), getElectrocutionDamage());
					entity.setFire(2);
				}
		});
		if (energy.getEnergyStored() > 0)
			transferEnergyToAllAround();
	}

	public void transferEnergyToAllAround() {
		ArrayList<EnumFacing> connectedSides = new ArrayList<>();
		for (EnumFacing side : EnumFacing.VALUES)
			if (isConnectedTo(side))
				connectedSides.add(side);
		connectedSides.forEach(side -> {
			IEnergyStorage storage = getWorld().getTileEntity(getPos().offset(side)).getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
			if (storage.canReceive())
				energy.extractEnergy(storage.receiveEnergy(Math.round(energy.getEnergyStored() / connectedSides.size()), false), false);
		});

	}
//
//	private void transferEnergyTo(EnumFacing face) {
//		TileEntity tile = this.getWorld().getTileEntity(this.pos.offset(face));
//		if (tile == null || tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()) == null)
//			return;
//		IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
//		if (storage.canReceive())
//			energy.extractEnergy(storage.receiveEnergy(Math.round(energy.getEnergyStored() / EnumFacing.VALUES.length), false), false);
//
//	}

	private float getElectrocutionDamage() {
		return 1;// getElectrocutionEnergy() / 0.0001f;
	}

	private int getElectrocutionEnergy() {
		return 0;// 1;
	}

	public boolean shouldElectrocuteEntity(Entity entity) {
		return entity instanceof Entity && this.energy.getEnergyStored() >= getElectrocutionEnergy();
	}

	public List<Entity> getElectrocutableEntities() {
		return this.getWorld().getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(getPos()).grow(5), EntitySelectors.CAN_AI_TARGET);
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		// TODO return Block.FULL_BLOCK_AABB; //FIXME full block AABB doesnt work
		return INFINITE_EXTENT_AABB;
	}

	@Override
	public ModEnergyStorage getEnergy() {
		return energy;
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return Math.pow(super.getMaxRenderDistanceSquared(), 2);
	}

}
