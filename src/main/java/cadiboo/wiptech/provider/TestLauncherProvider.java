package cadiboo.wiptech.provider;

import cadiboo.wiptech.capability.WeaponModular;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Coils;
import cadiboo.wiptech.init.Capabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TestLauncherProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

	// IItemHandler inventory =
	// (IItemHandler)TestLauncherProvider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
	// EnumFacing.NORTH);

	private final ItemStackHandler inventory;
	private final WeaponModular weaponModules;

	public TestLauncherProvider() {
		inventory = new ItemStackHandler(54);
		weaponModules = new WeaponModular();
		weaponModules.setCoil(Coils.TIN);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		} else if (capability == Capabilities.MODULAR_WEAPON_CAPABILITY) {
			return true;
		}
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) inventory;
		} else if (capability == Capabilities.MODULAR_WEAPON_CAPABILITY) {
			return (T) weaponModules;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return this.inventory.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.inventory.deserializeNBT(nbt);
	}

}
