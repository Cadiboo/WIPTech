package cadiboo.wiptech.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class WrappedItemStackHandler extends ItemStackHandler {

	protected final ItemStackHandler	handler;
	protected final int					offset;

	public WrappedItemStackHandler(ItemStackHandler handlerIn, int offsetIn) {
		this(1, handlerIn, offsetIn);
	}

	public WrappedItemStackHandler(int size, ItemStackHandler handlerIn, int offsetIn) {
		super(size);
		this.handler = handlerIn;
		this.offset = offsetIn;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		validateSlotIndex(slot);
		return handler.getStackInSlot(slot + offset);
	}

	@Override
	public int getSlotLimit(int slot) {
		return handler.getSlotLimit(slot + offset);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		handler.setStackInSlot(slot + offset, stack);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return handler.insertItem(slot + offset, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return handler.extractItem(slot + offset, amount, simulate);
	}

	@Override
	public int getSlots() {
		return handler.getSlots() - offset;
	}

	@Override
	public void setSize(int size) {
		handler.setSize(size + offset);
	}

}
