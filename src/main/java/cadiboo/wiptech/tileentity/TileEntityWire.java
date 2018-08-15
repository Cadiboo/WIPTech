package cadiboo.wiptech.tileentity;

import java.util.List;

import cadiboo.wiptech.capability.ModEnergyStorage;
import cadiboo.wiptech.capability.ModItemStackHandler;
import cadiboo.wiptech.util.IEnergyTransferer;
import cadiboo.wiptech.util.ModDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityWire extends ModTileEntity implements ITickable, IEnergyTransferer {

	private final ModEnergyStorage energy;

	public TileEntityWire() {
		this.energy = new ModEnergyStorage(10000);
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

				if (energy.extractEnergy(getElectrocutionEnergy(), true) == getElectrocutionEnergy()) {

					entity.attackEntityFrom(ModDamageSource.ELECTRICITY, damage);
					entity.setFire(1);
				}

			}
		});
		transferEnergyToAllAround();
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
		return this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPos()).grow(getElectrocutionRage()), EntitySelectors.CAN_AI_TARGET);
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
		return Math.pow(getElectrocutionRage(), 2);
	}

	private int getElectrocutionRage() {
		return 5;
	}

	@Override
	public ModItemStackHandler getInventory() {
		return null;
	}

}
