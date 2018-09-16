package cadiboo.wiptech.capability.inventory;

import javax.annotation.Nonnull;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IInventoryUser extends ICapabilityProvider {

	String INVENTORY_TAG = "inventory";

	@Nonnull
	ModItemStackHandler getInventory();

}
