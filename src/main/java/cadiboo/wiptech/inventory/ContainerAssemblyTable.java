package cadiboo.wiptech.inventory;

import cadiboo.wiptech.item.IItemAttachment;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAssemblyTable extends Container {

	public static final int	FULL_WIDTH	= 256;
	public static final int	FULL_HEIGHT	= 214;

	public static final int	SLOT_WIDTH	= 18;
	public static final int	BORDER_SIZE	= 3;

	public static final int	WIDTH	= FULL_WIDTH - (BORDER_SIZE * 2);
	public static final int	HEIGHT	= FULL_HEIGHT - (BORDER_SIZE * 2);

	public static final int	INVENTORY_HEIGHT		= 76;
	public static final int	INVENTORY_BORDER_TOP	= 3;
	public static final int	INVENTORY_BORDER_BOTTOM	= 4;
	public static final int	FULL_INVENTORY_HEIGHT	= INVENTORY_HEIGHT + INVENTORY_BORDER_TOP + INVENTORY_BORDER_BOTTOM;

	public static final int TOP_HEIGHT = HEIGHT - FULL_INVENTORY_HEIGHT;

	public ContainerAssemblyTable(final InventoryPlayer playerInv, final TileEntityAssemblyTable assemblyTable) {

		final int attachmentsSize = assemblyTable.getInventory().getSlots() - 1 - 2;

		final int assembleBody = assemblyTable.getInventory().getSlots() - 1 - 1;

		final int output = assemblyTable.getInventory().getSlots() - 1 - 0;

		final int width = (((WIDTH / 2) - SLOT_WIDTH) + BORDER_SIZE) - 12;
		final int height = (TOP_HEIGHT - SLOT_WIDTH) + BORDER_SIZE;
		final int radiusX = width / 2;
		final int radiusY = height / 2;

		for (int attachmentSlotIndex = 0; attachmentSlotIndex < attachmentsSize; attachmentSlotIndex++) {

			final double t = (2 * Math.PI * attachmentSlotIndex) / attachmentsSize;
			final int posX = (int) Math.round((width / 2) + (radiusX * Math.cos(t))) + 6;
			final int posY = (int) Math.round((height / 2) + (radiusY * Math.sin(t)));

			this.addSlotToContainer(new SlotItemHandler(assemblyTable.getInventory(), attachmentSlotIndex, posX + BORDER_SIZE, posY + BORDER_SIZE) {
				@Override
				public boolean isItemValid(final ItemStack stack) {
					if (super.isItemValid(stack) && (stack.getItem() instanceof IItemAttachment)) {
						return true;
					} else {
						return false;
					}
				}
			});
		}

		// assembleBody
		this.addSlotToContainer(new SlotItemHandler(assemblyTable.getInventory(), assembleBody, ((width + (SLOT_WIDTH / 2)) / 2) + 5, ((height + (SLOT_WIDTH / 2)) / 2) - 1) {
			@Override
			public boolean isItemValid(final ItemStack stack) {
				return super.isItemValid(stack);
			}
		});

		// output
		this.addSlotToContainer(new SlotItemHandler(assemblyTable.getInventory(), output, 188, 91) {
			@Override
			public boolean isItemValid(final ItemStack stack) {
				return false;
			}
		});

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInv, j + (i * 9) + 9, 48 + (j * 18), 132 + (i * 18)));
			}
		}
		for (int k = 0; k < 9; k++) {
			this.addSlotToContainer(new Slot(playerInv, k, 48 + (k * 18), 190));
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
