package cadiboo.wiptech.provider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.capability.WeaponModular;
import cadiboo.wiptech.init.Capabilities;
import cadiboo.wiptech.util.CustomEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

//Partly coppied from Actually Additions so heres what they put at the top

/*
 * This file ("CustomEnergyStorage.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

public class ModularWeaponProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

	public static final int WEAPON_ENERGY_CAPACITY = 10000;
	private final WeaponModular weaponModules;
	private final CustomEnergyStorage energy;

	public ModularWeaponProvider() {
		this.weaponModules = new WeaponModular();
		this.energy = new CustomEnergyStorage(WEAPON_ENERGY_CAPACITY);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == Capabilities.MODULAR_WEAPON_CAPABILITY) {
			return true;
		}
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return false;
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == Capabilities.MODULAR_WEAPON_CAPABILITY) {
			return (T) this.weaponModules;
		}
		if (capability == CapabilityEnergy.ENERGY) {
			return (T) this.energy;
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

	public void setEnergy(ItemStack stack, int energy) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (storage instanceof CustomEnergyStorage) {
				((CustomEnergyStorage) storage).setEnergyStored(energy);
			}
		}
	}

	public int receiveEnergyInternal(ItemStack stack, int maxReceive, boolean simulate) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (storage instanceof CustomEnergyStorage) {
				((CustomEnergyStorage) storage).receiveEnergyInternal(maxReceive, simulate);
			}
		}
		return 0;
	}

	public int extractEnergyInternal(ItemStack stack, int maxExtract, boolean simulate) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (storage instanceof CustomEnergyStorage) {
				((CustomEnergyStorage) storage).extractEnergyInternal(maxExtract, simulate);
			}
		}
		return 0;
	}

	public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (storage != null) {
				return storage.receiveEnergy(maxReceive, simulate);
			}
		}
		return 0;
	}

	public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (storage != null) {
				return storage.extractEnergy(maxExtract, simulate);
			}
		}
		return 0;
	}

	public int getEnergyStored(ItemStack stack) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (storage != null) {
				return storage.getEnergyStored();
			}
		}
		return 0;
	}

	public int getMaxEnergyStored(ItemStack stack) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (storage != null) {
				return storage.getMaxEnergyStored();
			}
		}
		return 0;
	}

}
