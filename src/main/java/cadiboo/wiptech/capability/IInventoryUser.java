package cadiboo.wiptech.capability;

import javax.annotation.Nonnull;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IInventoryUser extends ICapabilityProvider {

	@Nonnull
	public ModItemStackHandler getInventory();

}
