package cadiboo.wiptech.container;

import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.init.Recipes;
import cadiboo.wiptech.tileentity.TileEntityCrusher;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCrusher
extends Container
{
	private void addSlotToContainerCrusher(final TileEntityCrusher crusher, final int index, int xPos, int yPos)
	{
		IItemHandler inventory = (IItemHandler)crusher.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		addSlotToContainer(new SlotItemHandler(inventory, index, xPos, yPos)
		{
			public void onSlotChanged()
			{
				crusher.markDirty();
			}

			public boolean isItemValid(@Nonnull ItemStack stack)
			{
				boolean returnResult = false;

				ItemStack slot0Stack = ((IItemHandler)crusher.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)).getStackInSlot(0);
				if (slot0Stack.getItem() == Items.CRUSHER_BIT) {
					for (ItemStack result : Recipes.getCrushResults(index - 1)) {
						if ((!returnResult) && (result != null) && (result.getItem() == stack.getItem())) {
							returnResult = true;
						}
					}
				} else if (slot0Stack.getItem() == Items.HAMMER) {
					for (ItemStack result : Recipes.getHammerResults(index - 1)) {
						if ((!returnResult) && (result != null) && (result.getItem() == stack.getItem())) {
							returnResult = true;
						}
					}
				}
				return returnResult;
			}
		});
	}

	public ContainerCrusher(InventoryPlayer playerInv, final TileEntityCrusher crusher)
	{
		IItemHandler inventory = (IItemHandler)crusher.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

		addSlotToContainer(new SlotItemHandler(inventory, 0, 24, 17)
		{
			public void onSlotChanged()
			{
				crusher.markDirty();
			}

			public boolean isItemValid(@Nonnull ItemStack stack)
			{
				if ((stack.getItem() == Items.CRUSHER_BIT) || (stack.getItem() == Items.HAMMER)) {
					return true;
				}
				return false;
			}

			public int getSlotStackLimit()
			{
				return 1;
			}
		});
		addSlotToContainer(new SlotItemHandler(inventory, 1, 24, 53)
		{
			public void onSlotChanged()
			{
				crusher.markDirty();
			}

			public boolean isItemValid(@Nonnull ItemStack stack)
			{
				ItemStack slot0Stack = ((IItemHandler)crusher.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)).getStackInSlot(0);
				if (slot0Stack.getItem() == Items.CRUSHER_BIT) {
					return (Recipes.getCrushResult(stack) != null) && (Recipes.getCrushResult(stack).size() > 0);
				}
				if (slot0Stack.getItem() == Items.HAMMER) {
					return (Recipes.getHammerResult(stack) != null) && (Recipes.getHammerResult(stack).size() > 0);
				}
				return false;
			}
		});
		addSlotToContainerCrusher(crusher, 2, 83, 19);
		addSlotToContainerCrusher(crusher, 3, 114, 19);
		addSlotToContainerCrusher(crusher, 4, 145, 19);
		addSlotToContainerCrusher(crusher, 5, 83, 51);
		addSlotToContainerCrusher(crusher, 6, 114, 51);
		addSlotToContainerCrusher(crusher, 7, 145, 51);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int k = 0; k < 9; k++) {
			addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
		}
	}

	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

			if (index < containerSlots) {
				if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}


}