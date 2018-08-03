package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.capability.ModEnergyStorage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.energy.CapabilityEnergy;

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
		// WIPTech.info("update");
		this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPos()).grow(20 * 2)).forEach((entity) -> {
			entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 10);
		});
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
