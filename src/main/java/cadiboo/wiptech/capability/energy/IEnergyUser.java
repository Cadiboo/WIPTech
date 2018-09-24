package cadiboo.wiptech.capability.energy;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface IEnergyUser extends ICapabilitySerializable<NBTTagCompound> {

	String ENERGY_TAG = "energy";

	@Nonnull
	ModEnergyStorage getEnergy();

	@Override
	default NBTTagCompound serializeNBT() {
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger(ENERGY_TAG, getEnergy().getEnergyStored());
		return compound;
	}

	@Override
	default void deserializeNBT(final NBTTagCompound compound) {
		if (compound.hasKey(ENERGY_TAG)) {
			this.getEnergy().setEnergyStored(compound.getInteger(ENERGY_TAG), false);
		}
	}

}
