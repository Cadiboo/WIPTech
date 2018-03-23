package cadiboo.wiptech.init;

import cadiboo.wiptech.capability.IWeaponModules;
import cadiboo.wiptech.capability.WeaponModular;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {
	@CapabilityInject(IWeaponModules.class)
	public static Capability<IWeaponModules> CAPABILITY_WORKER = null;

	public static void registerCapabilities() {
		CapabilityManager.INSTANCE.register(IWeaponModules.class, new CapabilityWeaponModular(), WeaponModular.class);
	}

	public static class CapabilityWeaponModular implements IStorage<IWeaponModules> {

		public static final Capability<?> MODULAR_WEAPON_CAPABILITY = null;

		@Override
		public NBTBase writeNBT(Capability<IWeaponModules> capability, IWeaponModules instance, EnumFacing side) {
			return null;
		}

		@Override
		public void readNBT(Capability<IWeaponModules> capability, IWeaponModules instance, EnumFacing side, NBTBase nbt) {
		}

	}
}
