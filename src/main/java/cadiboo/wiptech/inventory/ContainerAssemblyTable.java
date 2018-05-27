package cadiboo.wiptech.inventory;

import javax.annotation.Nonnull;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.init.Recipes;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

public class ContainerAssemblyTable extends Container {

	private final EntityPlayer				player;
	private final TileEntityAssemblyTable	te;

	public InventorySavedCrafting		craftMatrix;
	public InventorySavedCraftResult	craftResult;

	/**
	 * Callback for when the crafting matrix is changed.
	 */

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {

		// WIPTech.error(this.craftResult.getStackInSlot(0));
		// WIPTech.dump(this.craftResult.getStackInSlot(0));

		InventoryCraftResult dummyResult = new InventoryCraftResult();
		// this.slotChangedCraftingGrid(this.player.world, this.player,
		// this.craftMatrix, this.craftResult);
		this.slotChangedCraftingGrid(this.player.world, this.player, this.craftMatrix, dummyResult);

		WIPTech.info(dummyResult);

		// WIPTech.error("cr:", this.craftResult.getStackInSlot(0));
		// WIPTech.error("cm:", this.craftMatrix.getStackInSlot(0));
		// WIPTech.dump(this.craftResult.getStackInSlot(0));
		// if (dummyResult.getStackInSlot(0).isItemEqual(te.getAssembleItem()))
		// te.setAssembleItem(new ItemStack(Items.DRAGON_BREATH));
		//
		// WIPTech.info(te.getAssembleItem(), dummyResult.getStackInSlot(0),
		// dummyResult.getStackInSlot(0).isItemEqual(te.getAssembleItem()));

		// // protected void slotChangedCraftingGrid(World p_192389_1_, EntityPlayer
		// // p_192389_2_, InventoryCrafting p_192389_3_, InventoryCraftResult
		// p_192389_4_)
		// // {
		// if (true)
		// return;
		// World worldIn = this.player.world;
		// EntityPlayer playerIn = this.player;
		// InventoryCrafting matrix = this.craftMatrix;
		// InventoryCraftResult result = this.craftResult;
		// if (!worldIn.isRemote) {
		// EntityPlayerMP entityplayermp = (EntityPlayerMP) playerIn;
		// ItemStack itemstack = ItemStack.EMPTY;
		// IRecipe irecipe = CraftingManager.findMatchingRecipe(matrix, worldIn);
		//
		// if (irecipe != null && (irecipe.isDynamic() ||
		// !worldIn.getGameRules().getBoolean("doLimitedCrafting") ||
		// entityplayermp.getRecipeBook().isUnlocked(irecipe))) {
		// result.setRecipeUsed(irecipe);
		// itemstack = irecipe.getCraftingResult(matrix);
		// }
		//
		// result.setInventorySlotContents(0, itemstack);
		// entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId + 1, 0,
		// itemstack));
		// }
		// // }

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

		WIPTech.dump(te.getInventory(EnumFacing.UP));

		craftMatrix = new InventorySavedCrafting(te.getInventory(EnumFacing.UP), this, Math.min(2, te.getInventory(EnumFacing.UP).getSlots()), Math.round(te.getInventory(EnumFacing.UP).getSlots() / 2)) {
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
				WIPTech.info("___", this.getSlot(0), craftResult.getStackInSlot(0), craftMatrix.getStackInSlot(0));
				this.addSlotToContainer(new Slot(this.craftMatrix, w + h * 2, 54 + w * 18, 17 + h * 18) {
					@Override
					public boolean isItemValid(ItemStack stack) {
						if (false) {
							ShapelessRecipes recipe = Recipes.getAssembleRecipeFor(te.getAssembleItem());
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