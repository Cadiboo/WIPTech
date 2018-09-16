package cadiboo.wiptech.inventory;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.attachments.AttachmentList;
import cadiboo.wiptech.capability.attachments.CapabilityAttachmentList;
import cadiboo.wiptech.item.IItemAttachment;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
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

		final boolean debugJeff = Boolean.valueOf(Boolean.valueOf(Boolean.parseBoolean("false")));

		final int width = 118 - 23;
		final int height = width;
		final int radiusX = width / 2;
		final int radiusY = height / 2;

		for (int attachmentSlotIndex = 0; attachmentSlotIndex < TileEntityAssemblyTable.ATTACHMENT_SLOTS_SIZE; attachmentSlotIndex++) {

			final double t = (2 * Math.PI * attachmentSlotIndex) / TileEntityAssemblyTable.ATTACHMENT_SLOTS_SIZE;
			final int posX = (int) Math.round((width / 2) + (radiusX * Math.cos(t + 90))) + 9;
			final int posY = (int) Math.round((height / 2) + (radiusY * Math.sin(t + 90))) + 20;

			final int index = attachmentSlotIndex;

			this.addSlotToContainer(new SlotItemHandler(assemblyTable.getInventory(), attachmentSlotIndex, posX + BORDER_SIZE, posY + BORDER_SIZE) {
				@Override
				public boolean isItemValid(final ItemStack stack) {
					if (super.isItemValid(stack) && (stack.getItem() instanceof IItemAttachment)) {
						final ItemStack body = assemblyTable.getInventory().getStackInSlot(TileEntityAssemblyTable.ASSEMBLY_SLOT);
						if (!body.isEmpty()) {
							final AttachmentList attachmentList = body.getCapability(CapabilityAttachmentList.ATTACHMENT_LIST, null);
							if (attachmentList != null) {
								final AttachmentPoints itemPoint = ((IItemAttachment) stack.getItem()).getAttachmentPoint();
								if (attachmentList.getPoints().contains(itemPoint)) {
									return true;
								}
							}
						}
						return false;
					} else {
						return false;
					}
				}

				@Override
				public void onSlotChanged() {
					if (debugJeff) {
						WIPTech.info("assembly table attachmentSlot " + index + " changed");
					}
				}
			});
		}

		// assembleBody
		this.addSlotToContainer(new SlotItemHandler(assemblyTable.getInventory(), TileEntityAssemblyTable.ASSEMBLY_SLOT, (width / 2) + 13, (height / 2) + 23) {
			@Override
			public boolean isItemValid(final ItemStack stack) {
				final AttachmentList attachmentList = stack.getCapability(CapabilityAttachmentList.ATTACHMENT_LIST, null);
				if (attachmentList != null) {
					return super.isItemValid(stack);
				}
				return false;
			}

			@Override
			public void onSlotChanged() {
				if (debugJeff) {
					WIPTech.info("assembly table body changed " + TileEntityAssemblyTable.ASSEMBLY_SLOT);
				}
			}
		});

		for (int topRow = 0; topRow < 3; topRow++) {
			for (int topColumn = 0; topColumn < 9; topColumn++) {
				final int row = topRow;
				final int column = topColumn;
				this.addSlotToContainer(new Slot(playerInv, topColumn + (topRow * 9) + 9, 48 + (topColumn * 18), 144 + (topRow * 18)) {
					@Override
					public void onSlotChanged() {
						if (debugJeff) {
							WIPTech.info("player main inventory changed " + row + " " + column);
						}
					}
				});
			}
		}

		for (int bottomColumn = 0; bottomColumn < 9; bottomColumn++) {
			final int column = bottomColumn;
			this.addSlotToContainer(new Slot(playerInv, bottomColumn, 48 + (bottomColumn * 18), 202) {
				@Override
				public void onSlotChanged() {
					if (debugJeff) {
						WIPTech.info("player hotbar inventory slot changed " + column);
					}
				}
			});
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
