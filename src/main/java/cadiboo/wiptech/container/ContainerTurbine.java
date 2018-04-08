package cadiboo.wiptech.container;

import cadiboo.wiptech.tileentity.TileEntityTurbine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTurbine extends Container {

	public ContainerTurbine(InventoryPlayer playerInv, final TileEntityTurbine turbine) {
		IItemHandler inventory = turbine.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		addSlotToContainer(new SlotItemHandler(inventory, 0, 36, 35) {
			@Override
			public void onSlotChanged() {
				turbine.markDirty();
			}

			@Override
			public boolean isItemValid(ItemStack stack) {
				IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
				if (energy != null)
					if (energy.canExtract())
						return true;
				return false;
			}

		});
		addSlotToContainer(new SlotItemHandler(inventory, 1, 124, 35) {
			@Override
			public void onSlotChanged() {
				turbine.markDirty();
			}

			@Override
			public boolean isItemValid(ItemStack stack) {
				IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
				if (energy != null)
					if (energy.canReceive())
						return true;
				return false;
			}

		});
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