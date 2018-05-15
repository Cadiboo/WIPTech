package cadiboo.wiptech.provider;

import javax.annotation.Nullable;

import cadiboo.wiptech.capability.WeaponModular;
import cadiboo.wiptech.init.Capabilities;
import cadiboo.wiptech.item.ItemEnergy;
import cadiboo.wiptech.util.CustomEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.CapabilityEnergy;

public class ModularWeaponProvider113 implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

	private final CustomEnergyStorage	energy;
	private final WeaponModular			weaponModules;

	public ModularWeaponProvider113(final ItemStack stack, ItemEnergy item) {
		this.weaponModules = new WeaponModular();
		this.energy = new CustomEnergyStorage(item.getMaxEnergy(), item.getMaxTransfer(), item.getMaxTransfer()) {
			@Override
			public int getEnergyStored() {
				if (stack.hasTagCompound()) {
					return stack.getTagCompound().getInteger("Energy");
				} else {
					return 0;
				}
			}

			@Override
			public void setEnergyStored(int energy) {
				if (!stack.hasTagCompound()) {
					stack.setTagCompound(new NBTTagCompound());
				}

				stack.getTagCompound().setInteger("Energy", energy);
			}
		};
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return this.getCapability(capability, facing) != null;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == Capabilities.MODULAR_WEAPON_CAPABILITY) {
			return (T) this.weaponModules;
		}
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this.energy);
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt = this.weaponModules.serializeNBT();
		this.energy.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.weaponModules.deserializeNBT(nbt);
		this.energy.readFromNBT(nbt);
	}

}