package cadiboo.wiptech.tileentity;

import java.lang.reflect.Field;

import cadiboo.wiptech.handler.network.CPacketSyncTileEntity;
import cadiboo.wiptech.handler.network.PacketHandler;
import cadiboo.wiptech.inventory.ContainerAssemblyTable;
import cadiboo.wiptech.inventory.InventorySavedCrafting;
import cadiboo.wiptech.inventory.WrappedItemStackHandler;
import cadiboo.wiptech.recipes.AssembleRecipe;
import cadiboo.wiptech.util.Utils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAssemblyTable extends TileEntityBase implements ITickable {

	public static final int[]	SLOTS			= new int[] { 1, 2, 3, 4, 5, 6 };
	private static final int[]	SLOTS_BOTTOM	= new int[] { 0 };
	private static final int	ENERGY_CAPACITY	= 10000;
	// private static final int ASSEMBLY_COST_TICK = 100;

	private int	assemblyTime		= 0;
	private int	assemblyCost		= 0;
	private int	assemblyCostTick	= 0;

	private ItemStack assembleItem = ItemStack.EMPTY;

	private ItemStackHandler inventory = new ItemStackHandler(SLOTS.length + SLOTS_BOTTOM.length) {
		@Override
		protected void onContentsChanged(int slot) {
			if (world != null && !world.isRemote) {
				syncToClients();
			}
			ItemStackHandler debugging = this;
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

	private ItemStackHandler inventoryNonBottom = new WrappedItemStackHandler(SLOTS.length, inventory, SLOTS_BOTTOM.length);

	private ItemStackHandler inventoryBottom = new WrappedItemStackHandler(SLOTS_BOTTOM.length, inventory, 0);

	private EnergyStorage energy = new EnergyStorage(ENERGY_CAPACITY, ENERGY_CAPACITY, 0) {
		@Override
		public boolean canExtract() {
			return super.canExtract();
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
		EntityPlayer player = world.getClosestPlayer(this.pos.getX(), this.pos.getY(), this.pos.getZ(), -1, false);
		if (player == null)
			return;
		ContainerAssemblyTable container = new ContainerAssemblyTable(player.inventory, this);

		if (assemblyTime > 0 && this.forceExtractEnergy(assemblyCostTick, true) == assemblyCostTick) {
			--assemblyTime;
			this.forceExtractEnergy(assemblyCostTick, false);
			if (assemblyTime == 0) {
				// AssembleRecipe recipe = Recipes.getAssembleRecipeFor(this.getAssembleItem());
				this.assemblyCost = 0;
				this.assemblyCostTick = 0;
				container.assemble();
			}
		} else {
			tryStartAssemble(container.craftMatrix, player);
		}
		this.handleSync();

	}

	@Override
	public void readNBT(NBTTagCompound nbt, NBTType type) {
		super.readNBT(nbt, type);
		if (!ItemStack.areItemsEqual(getAssembleItem(), new ItemStack(nbt.getCompoundTag("assembleItem")))) {
			for (int i = 0; i < inventory.getSlots(); i++) {
				ItemStack stack = inventory.getStackInSlot(i);
				if (!stack.isEmpty()) {
					EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
					world.spawnEntity(item);
				}
			}
		}
		this.setAssembleItem(new ItemStack(nbt.getCompoundTag("assembleItem")));
		this.assemblyTime = Integer.valueOf(nbt.getInteger("assemblyTime"));
		// WIPTech.info("readNBT", nbt);
	}

	@Override
	public void writeNBT(NBTTagCompound nbt, NBTType type) {
		super.writeNBT(nbt, type);
		NBTTagCompound assembleItemNBT = new NBTTagCompound();
		this.getAssembleItem().writeToNBT(assembleItemNBT);
		nbt.setTag("assembleItem", assembleItemNBT);
		nbt.setInteger("assemblyTime", assemblyTime);
		// WIPTech.info("writeNBT: " + nbt.toString());
	}

	public int getAssemblyTime() {
		return assemblyTime;
	}

	@Override
	public boolean handleSync() {
		return super.handleSync();
	}

	private int forceExtractEnergy(int maxExtract, boolean simulate) {
		if (energy.canExtract())
			return energy.extractEnergy(maxExtract, simulate);

		try {
			// Get the private field
			Field maxExtractField = EnergyStorage.class.getDeclaredField("maxExtract");
			// Allow modification on the field
			maxExtractField.setAccessible(true);
			// get field value
			int oldMaxExtract = (int) maxExtractField.get(energy);
			// set to highest needed
			maxExtractField.set(energy, energy.getMaxEnergyStored());
			// do it
			int result = energy.extractEnergy(maxExtract, simulate);
			// restore old value
			maxExtractField.set(energy, oldMaxExtract);

			return result;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public boolean tryStartAssemble(InventorySavedCrafting craftMatrix, EntityPlayer player) {
		IRecipe irecipe = CraftingManager.findMatchingRecipe(craftMatrix, player.world);
		if (irecipe == null)
			return false;
		if (!(irecipe instanceof AssembleRecipe))
			return false;
		AssembleRecipe arecipe = (AssembleRecipe) irecipe;

		int tempACT = arecipe.getEnergyCost() / arecipe.getDuration();
		// WIPTech.info(forceExtractEnergy(tempACT, false));
		// WIPTech.info(energy.extractEnergy(tempACT, false));
		if (this.forceExtractEnergy(tempACT, true) == tempACT) {
			this.assemblyTime = arecipe.getDuration();
			this.assemblyCost = arecipe.getEnergyCost();
			this.assemblyCostTick = tempACT;
		}

		PacketHandler.NETWORK.sendToServer(new CPacketSyncTileEntity(writeToNBT(new NBTTagCompound()), pos, world.provider.getDimension()));
		return true;
	}

}
