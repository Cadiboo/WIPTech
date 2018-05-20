package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.util.CustomEnergyStorage;
import cadiboo.wiptech.util.Utils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAssemblyTable extends TileEntityBase implements ITickable {

	private static final int[]	SLOTS				= new int[] { 0, 1, 2, 3, 4, 5 };
	private static final int[]	SLOTS_BOTTOM		= new int[] { 6 };
	private static final int	ENERGY_CAPACITY		= 10000;
	private static final int	ASSEMBLY_COST_TICK	= 100;

	private int assemblyTime = 0;

	private Item assembleItem = net.minecraft.init.Items.AIR;

	private ItemStackHandler inventory = new ItemStackHandler(SLOTS.length + SLOTS_BOTTOM.length) {
		@Override
		protected void onContentsChanged(int slot) {
			if (!TileEntityAssemblyTable.this.world.isRemote) {
				TileEntityAssemblyTable.this.syncToClients();
			}
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (stack.isEmpty())
				return ItemStack.EMPTY;

			validateSlotIndex(slot);
			if (slot <= 5 /* && !recpies.ispartofrecipe */)
				return stack;
			return super.insertItem(slot, stack, simulate);
		};
	};

	private ItemStackHandler inventoryNonBottom = new ItemStackHandler(SLOTS.length) {
		@Override
		public ItemStack getStackInSlot(int slot) {
			validateSlotIndex(slot);
			return inventory.getStackInSlot(slot);
		}

		@Override
		public int getSlotLimit(int slot) {
			return inventory.getSlotLimit(slot);
		};

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

	private ItemStackHandler inventoryBottom = new ItemStackHandler(SLOTS_BOTTOM.length) {
		@Override
		public ItemStack getStackInSlot(int slot) {
			validateSlotIndex(slot);
			return inventory.getStackInSlot(slot + SLOTS.length);
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			inventory.setStackInSlot(slot + SLOTS.length, stack);
		};

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			return inventory.insertItem(slot + SLOTS.length, stack, simulate);
		};

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return inventory.extractItem(slot + SLOTS.length, amount, simulate);
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

	public Item getAssembleItem() {
		return this.assembleItem;
	}

	public void setAssembleItem(Item item) {
		this.assembleItem = item;
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
