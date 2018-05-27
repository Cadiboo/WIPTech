package cadiboo.wiptech.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;

public class InventorySavedCraftResult extends InventoryCraftResult {

	/** A list of one item containing the result of the crafting formula */
	protected ItemStack					stack		= ItemStack.EMPTY;
	protected NonNullList<ItemStack>	stackResult	= NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
	protected final IItemHandler		inventory;

	InventorySavedCraftResult(IItemHandler inventory) {
		super();
		this.inventory = inventory;
		this.loadInventory();
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	/**
	 * Returns the stack in the given slot.
	 */
	@Override
	public ItemStack getStackInSlot(int index) {
		return stack;
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns
	 * them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {
		stack.shrink(count);
		stackResult.set(0, this.stack);
		return stack;
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack ret = stack;
		this.clear();
		stackResult.set(0, this.stack);
		return ret;
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.stack = stack;
		stackResult.set(0, this.stack);
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64,
	 * possibly will be extended.
	 */
	@Override
	public int getInventoryStackLimit() {
		return super.getInventoryStackLimit();
	}

	@Override
	public void clear() {
		stack = ItemStack.EMPTY;
		stackResult.set(0, this.stack);
	}

	@Override
	public void openInventory(EntityPlayer player) {
		this.loadInventory();
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		saveInventory();
	}

	public void loadInventory() {
		stack = inventory.getStackInSlot(0);
		stackResult.set(0, this.stack);
	}

	public void saveInventory() {
		inventory.extractItem(0, Integer.MAX_VALUE, false);
		inventory.insertItem(0, stack, false);
		stackResult.set(0, this.stack);
	}

}
