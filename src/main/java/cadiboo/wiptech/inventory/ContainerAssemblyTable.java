package cadiboo.wiptech.inventory;

import javax.annotation.Nonnull;

import cadiboo.wiptech.init.Recipes;
import cadiboo.wiptech.recipes.AssembleRecipe;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

public class ContainerAssemblyTable extends Container {

	private final EntityPlayer				player;
	private final TileEntityAssemblyTable	te;

	public InventorySavedCrafting		craftMatrix;
	public InventorySavedCraftResult	craftResult;

	public static final int	SLOT_X_OFFSET		= 54;
	public static final int	SLOT_Y_OFFSET		= 17;
	public static final int	SLOT_X_MULTIPLIER	= 18;
	public static final int	SLOT_Y_MULTIPLIER	= 18;

	/**
	 * Callback for when the crafting matrix is changed.
	 */

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		// super.onCraftMatrixChanged(inventoryIn);
	}

	public void assemble() {
		if (player.world.isRemote)
			return;
		IRecipe irecipe = CraftingManager.findMatchingRecipe(craftMatrix, player.world);
		if (irecipe != null && irecipe instanceof AssembleRecipe) {
			this.slotChangedCraftingGrid(player.world, player, craftMatrix, craftResult);
			this.craftMatrix.clear();
		}
	}

	/**
	 * Called to determine if the current slot is valid for the stack merging
	 * (double-click) code. The stack passed in is null for the initial slot that
	 * was double-clicked.
	 */
	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
	}

	public ContainerAssemblyTable(InventoryPlayer playerInv, final TileEntityAssemblyTable teIn) {
		this.player = playerInv.player;
		this.te = teIn;

		Recipes.addAssembleRecipes();

		craftMatrix = new InventorySavedCrafting(te.getInventory(EnumFacing.UP), this, Math.min(2, te.getInventory(EnumFacing.UP).getSlots()),
				Math.round(te.getInventory(EnumFacing.UP).getSlots() / Math.min(2, te.getInventory(EnumFacing.UP).getSlots()))) {
			@Override
			public void clear() {
				super.clear();
				this.saveInventory();
			}

			@Override
			public void setInventorySlotContents(int index, ItemStack stack) {
				super.setInventorySlotContents(index, stack);
				this.saveInventory();
			}
		};
		craftResult = new InventorySavedCraftResult(te.getInventory(EnumFacing.DOWN)) {
			@Override
			public void clear() {
				super.clear();
				this.saveInventory();
			}

			@Override
			public void setInventorySlotContents(int index, ItemStack stack) {
				super.setInventorySlotContents(index, stack);
				this.saveInventory();
			}
		};

		this.addSlotToContainer(new SlotCrafting(playerInv.player, this.craftMatrix, this.craftResult, 0, 130, 35) {
			@Override
			public boolean isItemValid(@Nonnull ItemStack stack) {
				return te.getAssembleItem().isItemEqual(stack);
			}

			@Override
			public void onSlotChanged() {
				super.onSlotChanged();
				te.markDirty();
			}
		});

		for (int w = 0; w < craftMatrix.getWidth(); ++w) {
			for (int h = 0; h < craftMatrix.getHeight(); ++h) {
				this.addSlotToContainer(new Slot(this.craftMatrix, w + h * 2, SLOT_X_OFFSET + w * SLOT_X_MULTIPLIER, SLOT_Y_OFFSET + h * SLOT_Y_MULTIPLIER) {
					@Override
					public boolean isItemValid(ItemStack stack) {
						if (true) {
							AssembleRecipe recipe = Recipes.getAssembleRecipeFor(te.getAssembleItem());
							if (recipe == null)
								return false;
							NonNullList<Ingredient> ingredients = recipe.getIngredients();
							for (int i = 0; i < ingredients.size(); i++) {
								if (ingredients.get(i).apply(stack))
									return true;
							}
							return false;
						} else
							return super.isItemValid(stack);
					}

					@Override
					public void onSlotChanged() {
						super.onSlotChanged();
						te.markDirty();
					}

				});
			}
		}

		// player inventory
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
		Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();
			if (index < containerSlots) {
				if (!mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
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

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		craftMatrix.closeInventory(playerIn);
		craftResult.closeInventory(playerIn);
	}
}