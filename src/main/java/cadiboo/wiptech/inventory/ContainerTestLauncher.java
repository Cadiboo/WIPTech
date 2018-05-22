package cadiboo.wiptech.inventory;

import cadiboo.wiptech.provider.TestLauncherProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTestLauncher extends Container {
	
	public ContainerTestLauncher( IItemHandler i, EntityPlayer p ) {
		
		int xPos = 8;
		int yPos = 18;
		int iid = 0;
	
		for( int y = 0; y < 6; ++y ) {
			for( int x = 0; x < 9; ++x ) {
				addSlotToContainer( new SlotItemHandler( i, iid, xPos + x * 18, yPos + y * 18 ));
				iid++;
			}
		}
		
		yPos = 140;
		
		for( int y = 0; y < 3; ++y ) {
			for( int x = 0; x < 9; ++x ) {
				addSlotToContainer( new Slot( p.inventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18 ));
			}
		}
		
		for( int x = 0; x < 9; ++x ) {
			addSlotToContainer( new Slot( p.inventory, x, xPos + x * 18, 198 ));
		}
		
	}
	
	@Override
	public boolean canInteractWith( EntityPlayer p ) {
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