package cadiboo.wiptech.inventory;

import cadiboo.wiptech.entity.item.EntityRailgun;
import cadiboo.wiptech.item.ItemSlug;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerRailgun extends Container {

	public ContainerRailgun(final InventoryPlayer playerInv, final EntityRailgun railgun) {

		for (int i = 0; i < railgun.getInventory().getSlots(); i++) {
			addSlotToContainer(new SlotItemHandler(railgun.getInventory(), i, 62 + i * 18, 20) {
				@Override
				public boolean isItemValid(ItemStack stack) {
					if (stack.getItem() instanceof ItemSlug)
						return super.isItemValid(stack);
					return false;
				}
			});
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
			}
		}
		for (int k = 0; k < 9; k++) {
			addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 109));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		return ModUtil.transferStackInSlot(player, index, this);
	}

}
