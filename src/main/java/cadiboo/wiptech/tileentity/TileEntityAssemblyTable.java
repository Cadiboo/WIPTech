package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.util.CustomEnergyStorage;
import cadiboo.wiptech.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAssemblyTable extends TileEntityBase implements ITickable {

	private static final int[]	SLOTS				= new int[] { 1, 2, 3, 4, 5, 6 };
	private static final int[]	SLOTS_BOTTOM		= new int[] { 0 };
	private static final int	ENERGY_CAPACITY		= 10000;
	private static final int	ASSEMBLY_COST_TICK	= 100;

	private int assemblyTime = 0;

	private ItemStack assembleItem = ItemStack.EMPTY;

	private ItemStackHandler inventory = new ItemStackHandler(SLOTS.length + SLOTS_BOTTOM.length) {
		@Override
		protected void onContentsChanged(int slot) {
			if (!TileEntityAssemblyTable.this.world.isRemote) {
				TileEntityAssemblyTable.this.syncToClients();
			}
			markDirty();
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			// if (stack.isEmpty())
			// return ItemStack.EMPTY;
			//
			// validateSlotIndex(slot);
			// if (slot <= 5 /* && !recpies.ispartofrecipe */)
			// return stack;
			return super.insertItem(slot, stack, simulate);
		};
	};

	private ItemStackHandler inventoryNonBottom = new ItemStackHandler(SLOTS.length) {
		@Override
		public ItemStack getStackInSlot(int slot) {
			validateSlotIndex(slot + SLOTS_BOTTOM.length);
			return inventory.getStackInSlot(slot + SLOTS_BOTTOM.length);
		}

		@Override
		public int getSlotLimit(int slot) {
			return inventory.getSlotLimit(slot + SLOTS_BOTTOM.length);
		};

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			inventory.setStackInSlot(slot + SLOTS_BOTTOM.length, stack);
		};

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			return inventory.insertItem(slot + SLOTS_BOTTOM.length, stack, simulate);
		};

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return inventory.extractItem(slot + SLOTS_BOTTOM.length, amount, simulate);
		};
	};

	private ItemStackHandler inventoryBottom = new ItemStackHandler(SLOTS_BOTTOM.length) {
		@Override
		public ItemStack getStackInSlot(int slot) {
			validateSlotIndex(slot);
			return inventory.getStackInSlot(slot);
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			inventory.setStackInSlot(slot, stack);
		};

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			return inventory.insertItem(slot, stack, simulate);
		};

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return inventory.extractItem(slot, amount, simulate);
		};

	};

	private CustomEnergyStorage energy = new CustomEnergyStorage(ENERGY_CAPACITY) {
		@Override
		public boolean canExtract() {
			return false;
		};
	};

	@Override
	public IItemHandler getInventory(EnumFacing side) {
		if (side == null)
			return inventory;
		return side == EnumFacing.DOWN ? inventoryBottom : inventoryNonBottom;
	}

	@Override
	public IEnergyStorage getEnergy(EnumFacing side) {
		return energy;
	}

	public ItemStack getAssembleItem() {
		return this.assembleItem;
	}

	public void setAssembleItem(ItemStack stack) {
		this.assembleItem = stack;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return this.getBlockType().getSelectedBoundingBox(Utils.getStateFromPos(this.getWorld(), this.getPos()), this.getWorld(), this.getPos()).grow(2);
	}

	@Override
	public void update() {
		// if (assemblyTime > 0 && energy.extractEnergy(ASSEMBLY_COST_TICK, true) ==
		// ASSEMBLY_COST_TICK) {
		// --assemblyTime;
		// energy.extractEnergy(ASSEMBLY_COST_TICK, false);
		// if (assemblyTime == 0) {
		// AssembleRecipe recipe = Recipes.getAssembleRecipeFor(this.getAssembleItem());
		//
		// }
		// }
		this.handleSync();
	}

	public int getAssemblyTime() {
		return assemblyTime;
	}

	@Override
	public boolean handleSync() {
		return super.handleSync();
	}

}
