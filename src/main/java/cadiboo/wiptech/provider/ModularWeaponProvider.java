package cadiboo.wiptech.provider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.capability.WeaponModular;
import cadiboo.wiptech.init.Capabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

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

	public static final int		WEAPON_ENERGY_CAPACITY	= 10000;
	private final WeaponModular	weaponModules;
	private final EnergyStorage	energy;

	public ModularWeaponProvider(ItemStack stack) {
		this.weaponModules = new WeaponModular();
		this.energy = new EnergyStorage(WEAPON_ENERGY_CAPACITY) {
			@Override
			public boolean canReceive() {
				return true;
			}

			@Override
			public boolean canExtract() {
				return true;
			}
		};
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return getCapability(capability, facing) != null;
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
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.weaponModules.deserializeNBT(nbt);
	}

}
