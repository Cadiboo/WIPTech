package cadiboo.wiptech.inventory;

import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;

public class InventorySavedCrafting extends InventoryCrafting {

	protected final NonNullList<ItemStack>	stackList;
	protected final Container				eventHandler;
	protected final IItemHandler			inventory;

	public InventorySavedCrafting(IItemHandler inventory, Container eventHandlerIn, int width, int height) {
		super(eventHandlerIn, width, height);
		this.stackList = NonNullList.<ItemStack>withSize(width * height, ItemStack.EMPTY);
		this.eventHandler = eventHandlerIn;
		this.inventory = inventory;
		this.loadInventory();
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return this.stackList.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.stackList) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the stack in the given slot.
	 */
	@Override
	public ItemStack getStackInSlot(int index) {
		return index >= this.getSizeInventory() ? ItemStack.EMPTY : (ItemStack) this.stackList.get(index);
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.stackList, index);
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns
	 * them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.stackList, index, count);

		if (!itemstack.isEmpty()) {
			this.eventHandler.onCraftMatrixChanged(this);
		}

		return itemstack;
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.stackList.set(index, stack);
		this.eventHandler.onCraftMatrixChanged(this);
	}

	@Override
	public void clear() {
		this.stackList.clear();
	}

	@Override
	public void fillStackedContents(RecipeItemHelper helper) {
		for (ItemStack itemstack : this.stackList) {
			helper.accountStack(itemstack);
		}
	}

	@Override
	public void openInventory(EntityPlayer player) {
		this.loadInventory();
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		this.saveInventory();
	}

	public void loadInventory() {
		for (int i = 0; i < getWidth() * getHeight(); i++) {
			stackList.set(i, inventory.getStackInSlot(i).copy());
		}
	}

	public void saveInventory() {
		for (int i = 0; i < getWidth() * getHeight(); i++) {
			inventory.extractItem(i, Integer.MAX_VALUE, false);
			inventory.insertItem(i, stackList.get(i), false);
		}
	}

}
