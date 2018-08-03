package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.capability.ModEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public abstract class ModTileEntity extends TileEntity {

	public abstract ModEnergyStorage getEnergy();

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY)
			return (T) getEnergy();
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return this.getCapability(capability, facing) != null;
	}

	@Override
	public final void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.readNBT(compound);
	}

	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		this.writeNBT(compound);
		return compound;
	}

	public void writeNBT(NBTTagCompound nbt) {
		nbt.setInteger("energy", this.getEnergy().getEnergyStored());
	}

	public void readNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("energy"))
			this.getEnergy().setEnergy(nbt.getInteger("energy"));
	}

}
