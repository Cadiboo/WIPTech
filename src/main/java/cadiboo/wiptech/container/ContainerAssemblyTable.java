package cadiboo.wiptech.container;

import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAssemblyTable extends Container {

	public ContainerAssemblyTable(InventoryPlayer playerInv, final TileEntityAssemblyTable te) {
		// addSlotToContainer(new SlotItemHandler(inventory, i, 30 + 18 * i, 17 + 18 *
		// j));

		IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		for (int i = 0; i < 6; i++) {
			addSlotToContainer(new SlotItemHandler(inventory, i, 54 + (i % 2) * 18, /* 54 + 18 * i / 2 */17 + 9 * ((i & 0x1) == 0 ? i : i - 1)) {

				// @Override
				// public boolean isItemValid(@Nonnull ItemStack stack) {
				// return true;
				// }

				// @Override
				// public void onSlotChanged()
				// {
				// coiler.markDirty();
				// }
				//
				// public boolean isItemValid(@Nonnull ItemStack stack)
				// {
				// boolean returnResult = false;
				//
				// ItemStack slot0Stack =
				// ((IItemHandler)coiler.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
				// EnumFacing.NORTH)).getStackInSlot(0);
				//
				// for (ItemStack result : Recipes.getCoilResult(0)) {
				// if ((!returnResult) && (result != null) && (result.getItem() ==
				// stack.getItem())) {
				// returnResult = true;
				// }
				// }
				// return returnResult;
				// }
			});
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int k = 0; k < 9; k++) {
			addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if ((slot != null) && (slot.getHasStack())) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();
			if (index < containerSlots) {
				if (!mergeItemStack(itemstack1, containerSlots, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(itemstack1, 0, containerSlots, false)) {
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