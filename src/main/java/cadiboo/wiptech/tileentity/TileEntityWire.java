package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.config.Configuration;
import cadiboo.wiptech.util.CustomEnergyStorage;
import cadiboo.wiptech.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityWire extends TileEntityBase implements ITickable {

	private CustomEnergyStorage energy = new CustomEnergyStorage(Configuration.energy.BaseWireStorage, Integer.MAX_VALUE) {
		@Override
		public boolean canExtract() {
			return super.canExtract();
		};

		@Override
		public boolean canReceive() {
			return super.canReceive();
		};
	};

	@Override
	public void update() {
		if (!world.isRemote) {
			for (EnumFacing face : EnumFacing.VALUES) {
				if (isConnectedTo(face)) {
					if (!(Utils.getBlockFromPos(world, pos.offset(face)) instanceof BlockWire)) {
						IEnergyStorage storage = world.getTileEntity(pos.offset(face)).getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
						if (storage.canReceive() /* && !storage.canExtract() */) {
							energy.extractEnergy(storage.receiveEnergy(energy.extractEnergy(energy.getEnergyStored() / (face.getIndex() + 1), true), false), false);
						}
					} else {
						IEnergyStorage storage = world.getTileEntity(pos.offset(face)).getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
						if (storage.getEnergyStored() <= energy.getEnergyStored()) {
							energy.extractEnergy(storage.receiveEnergy(energy.extractEnergy(energy.getEnergyStored(), true), false), false);
						}
					}
				}
			}
			if (getWorld().getWorldTime() % 5 == 0)
				syncToClients();
		} else if (Minecraft.getMinecraft().objectMouseOver.getBlockPos().equals(this.getPos()))
			syncToClients();
		this.energy.setCapacity(Math.round(Configuration.energy.BaseWireStorage * ((BlockWire) this.getBlockType()).getMetal().getConductivityFraction()));
	}

	public boolean isConnectedTo(EnumFacing face) {
		return (world.getTileEntity(pos.offset(face)) != null && world.getTileEntity(pos.offset(face)).getCapability(CapabilityEnergy.ENERGY, face.getOpposite()) != null);
	}

	@Override
	public IEnergyStorage getEnergy(EnumFacing side) {
		return this.energy;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox();// .grow(0.5D);
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

}
