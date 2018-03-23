package cadiboo.wiptech.provider;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.WeaponModular;
import cadiboo.wiptech.init.Capabilities.CapabilityWeaponModular;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class TestModularWeaponProvidor implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

	private final WeaponModular weaponModules;

	public TestModularWeaponProvidor() {
		weaponModules = new WeaponModular();
	}

	@Override
	public boolean hasCapability( Capability<?> capability, EnumFacing facing ) {
		if( capability == CapabilityWeaponModular.MODULAR_WEAPON_CAPABILITY ) {
			return true;
		}
		return false;
	}

	@Override
	public <T> T getCapability( Capability<T> capability, EnumFacing facing ) {
		if( capability == CapabilityWeaponModular.MODULAR_WEAPON_CAPABILITY  ) {
			return (T) weaponModules; 
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return this.weaponModules.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		WIPTech.logger.info(nbt);
		//this.modules.circuit = nbt.getCompoundTag("circuit");
		this.weaponModules.deserializeNBT(nbt);
	}
	
}
