package cadiboo.wiptech.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.ItemStackHandler;

public class ItemEnergyItemHandler extends ItemStackHandler {
	public ItemEnergyItemHandler(int i) {
		super(i);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.getCapability(CapabilityEnergy.ENERGY, null) == null)
			return stack;
		return super.insertItem(slot, stack, simulate);
	}

	@Override
	protected void onContentsChanged(int slot) {
		super.onContentsChanged(slot);
	}

	@Override
	protected int getStackLimit(int slot, ItemStack stack) {
		return 1;
	}
}