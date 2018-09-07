package cadiboo.wiptech.inventory;

import cadiboo.wiptech.tileentity.TileEntityModFurnace;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerModFurnace extends Container {

	public ContainerModFurnace(final InventoryPlayer playerInv, final TileEntityModFurnace modFurnace) {

		this.addSlotToContainer(new SlotItemHandler(modFurnace.getInventory(), TileEntityModFurnace.INPUT_SLOT, 56, 17) {

		});

		this.addSlotToContainer(new SlotItemHandler(modFurnace.getInventory(), TileEntityModFurnace.FUEL_SLOT, 56, 53) {

		});

		this.addSlotToContainer(new SlotItemHandler(modFurnace.getInventory(), TileEntityModFurnace.OUTPUT_SLOT, 116, 35) {

		});

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInv, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
			}
		}
		for (int k = 0; k < 9; k++) {
			this.addSlotToContainer(new Slot(playerInv, k, 8 + (k * 18), 142));
		}

	}

	@Override
	public boolean canInteractWith(final EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer player, final int index) {
		return ModUtil.transferStackInSlot(player, index, this);
	}

}
