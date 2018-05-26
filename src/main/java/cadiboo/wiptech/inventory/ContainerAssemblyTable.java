package cadiboo.wiptech.inventory;

import javax.annotation.Nonnull;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.init.Recipes;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerAssemblyTable extends Container {

	/** The crafting matrix inventory. */
	public InventoryCrafting	craftMatrix	= new InventoryCrafting(this, 2, 3);
	public InventoryCraftResult	craftResult	= new InventoryCraftResult();
	private final EntityPlayer	player;

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		// this.slotChangedCraftingGrid(this.player.world, this.player,
		// this.craftMatrix, this.craftResult);

		// protected void slotChangedCraftingGrid(World p_192389_1_, EntityPlayer
		// p_192389_2_, InventoryCrafting p_192389_3_, InventoryCraftResult p_192389_4_)
		// {
		World worldIn = this.player.world;
		EntityPlayer playerIn = this.player;
		InventoryCrafting matrix = this.craftMatrix;
		InventoryCraftResult result = this.craftResult;
		if (!worldIn.isRemote) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) playerIn;
			ItemStack itemstack = ItemStack.EMPTY;
			IRecipe irecipe = CraftingManager.findMatchingRecipe(matrix, worldIn);

			if (irecipe != null && (irecipe.isDynamic() || !worldIn.getGameRules().getBoolean("doLimitedCrafting") || entityplayermp.getRecipeBook().isUnlocked(irecipe))) {
				result.setRecipeUsed(irecipe);
				itemstack = irecipe.getCraftingResult(matrix);
			}

			result.setInventorySlotContents(0, itemstack);
			entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId + 1, 0, itemstack));
		}
		// }

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

	public ContainerAssemblyTable(InventoryPlayer playerInv, final TileEntityAssemblyTable te) {
		this.player = playerInv.player;

		for (int w = 0; w < craftMatrix.getWidth(); ++w) {
			for (int h = 0; h < craftMatrix.getHeight(); ++h) {
				WIPTech.info(w, h, w + h * 2, "_");
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
						}
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

		WIPTech.info("slot 0");
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

		IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
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
}