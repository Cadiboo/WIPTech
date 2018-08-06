package cadiboo.wiptech.tileentity;

import java.util.List;

import cadiboo.wiptech.capability.ModEnergyStorage;
import cadiboo.wiptech.util.ModDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
				if (energy.extractEnergy(getElectrocutionEnergy(), false) == getElectrocutionEnergy())
					entity.attackEntityFrom(ModDamageSource.ELECTRICITY, getElectrocutionDamage());
		});
		if (energy.getEnergyStored() > 0)
			for (EnumFacing face : EnumFacing.VALUES)
				transferEnergyTowards(face);
	}

	private void transferEnergyTowards(EnumFacing face) {
		TileEntity tile = this.getWorld().getTileEntity(this.pos.offset(face));
		if (tile == null || tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()) == null)
			return;
		IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
		if (storage.canReceive())
			energy.extractEnergy(storage.receiveEnergy(Math.round(energy.getEnergyStored() / EnumFacing.VALUES.length), false), false);

	}

	private float getElectrocutionDamage() {
		return getElectrocutionEnergy() / 0.0001f;
	}

	private int getElectrocutionEnergy() {
		return 1;
	}

	public boolean shouldElectrocuteEntity(Entity entity) {
		return entity instanceof EntityLivingBase && this.energy.getEnergyStored() >= getElectrocutionEnergy();
	}

	public List<Entity> getElectrocutableEntities() {
		return this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPos()).grow(20 * 2), EntitySelectors.CAN_AI_TARGET);
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

}
