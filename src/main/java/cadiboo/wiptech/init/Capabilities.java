package cadiboo.wiptech.init;

import cadiboo.wiptech.capability.IWeaponModular;
import cadiboo.wiptech.capability.WeaponModular;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Coils;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Rails;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Scopes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {
	@CapabilityInject(IWeaponModular.class)
	public static Capability<IWeaponModular> MODULAR_WEAPON_CAPABILITY = null;

	public static void registerCapabilities() {
		CapabilityManager.INSTANCE.register(IWeaponModular.class, new CapabilityWeaponModular(), WeaponModular::new);
	}

	public static class CapabilityWeaponModular implements IStorage<IWeaponModular> {

		@Override
		public NBTTagCompound writeNBT(Capability<IWeaponModular> capability, IWeaponModular instance,
				EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			// WIPTech.logger.info(instance.getCircuit());
			// WIPTech.logger.info(instance.getCoil());
			// WIPTech.logger.info(instance.getScope());
			if (instance.getCircuit() != null)
				nbt.setInteger("circuit", instance.getCircuit().getID());
			if (instance.getCoil() != null)
				nbt.setInteger("coil", instance.getCoil().getID());
			if (instance.getScope() != null)
				nbt.setInteger("scope", instance.getScope().getID());
			if (instance.getRail() != null)
				nbt.setInteger("rail", instance.getRail().getID());

			return nbt;
		}

		@Override
		public void readNBT(Capability<IWeaponModular> capability, IWeaponModular instance, EnumFacing side,
				NBTBase nbtBase) {
			NBTTagCompound nbt = (NBTTagCompound) nbtBase;
			if (nbt.hasKey("circuit"))
				instance.setCircuit(Circuits.byID(nbt.getInteger("circuit")));
			if (nbt.hasKey("coil"))
				instance.setCoil(Coils.byID(nbt.getInteger("coil")));
			if (nbt.hasKey("scope"))
				instance.setScope(Scopes.byID(nbt.getInteger("scope")));
			if (nbt.hasKey("rail"))
				instance.setRail(Rails.byID(nbt.getInteger("rail")));
		}

	}
}
