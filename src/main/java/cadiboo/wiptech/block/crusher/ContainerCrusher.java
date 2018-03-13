package cadiboo.wiptech.block.crusher;

import javax.annotation.Nonnull;

import cadiboo.wiptech.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCrusher extends Container {

	private void addSlotToContainerCrusher(TileEntityCrusher crusher, int index, int xPos, int yPos)
	{
		IItemHandler inventory = crusher.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		addSlotToContainer(new SlotItemHandler(inventory, index, xPos, yPos) {
			@Override
			public void onSlotChanged() {
				crusher.markDirty();
			};
			@Override
			public boolean isItemValid(@Nonnull ItemStack stack){
				//dont allow anything to be put i
				return false;
			}
		});
	}


	public ContainerCrusher(InventoryPlayer playerInv, final TileEntityCrusher crusher) {
		IItemHandler inventory = crusher.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

		//block inventory
		//addSlotToContainerCrusher(crusher, 0, 24, 17); //crusher bit
		addSlotToContainer(new SlotItemHandler(inventory, 0, 24, 17) {
			@Override
			public void onSlotChanged() {
				crusher.markDirty();
			}
			@Override
			public boolean isItemValid(@Nonnull ItemStack stack){
				//return stack.getItem() instanceof ItemCrusherBit;
				if (stack.getItem() == Items.CRUSHER_BIT)
					return true;
				else
					return false;
			}
			@Override
			public int getSlotStackLimit()
			{
				return 1;
			}
		});
		//addSlotToContainerCrusher(crusher, 1, 24, 53); //item to crush
		addSlotToContainer(new SlotItemHandler(inventory, 1, 24, 53) {
			@Override
			public void onSlotChanged() {
				crusher.markDirty();
			}
			/*@Override
			public boolean isItemValid(@Nonnull ItemStack stack){
				return getCrushResult(stack.getItem());
			}*/
		});
		addSlotToContainerCrusher(crusher, 2, 83, 19); //product 1 (gravel)
		addSlotToContainerCrusher(crusher, 3, 114, 19); //product 2 (alumina)
		addSlotToContainerCrusher(crusher, 4, 145, 19); //product 3 (Iron Oxides)
		addSlotToContainerCrusher(crusher, 5, 83, 51); //product 4 (Silica)
		addSlotToContainerCrusher(crusher, 6, 114, 51); //product 5 (Titania)
		addSlotToContainerCrusher(crusher, 7, 145, 51); //product 6 (Gallium)

		//addSlotToContainerCrusher(crusher, 0, 80, 35); //centre
		/*addSlotToContainer(new SlotItemHandler(inventory, 0, 20, 15) {
			@Override
			public void onSlotChanged() {
				crusher.markDirty();
			}
		});
		addSlotToContainer(new SlotItemHandler(inventory, 1, 80, 35) {
			@Override
			public void onSlotChanged() {
				crusher.markDirty();
			}
		});*/

		//player inventory
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		//player hotbar
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