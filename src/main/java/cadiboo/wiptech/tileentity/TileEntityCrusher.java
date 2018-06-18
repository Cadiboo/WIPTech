package cadiboo.wiptech.tileentity;

import java.util.ArrayList;

import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.handler.network.PacketHandler;
import cadiboo.wiptech.handler.network.PacketRequestUpdateCrusher;
import cadiboo.wiptech.handler.network.PacketUpdateCrusher;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.init.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCrusher extends TileEntity implements ITickable {

	public float				crushTime;
	public long					lastChangeTime;
	private static final int[]	SLOTS_TOP		= new int[] { 0, 1 };
	private static final int[]	SLOTS_BOTTOM	= new int[] { 2, 3, 4, 5, 6, 7 };
	private static final int[]	SLOTS_SIDES		= new int[] { 0, 1 };

	public static int getSlots() {
		return 8;
	}

	public ItemStackHandler	inventory		= new ItemStackHandler(getSlots()) {
												@Override
												protected void onContentsChanged(int slot) {
													if (!TileEntityCrusher.this.world.isRemote) {
														TileEntityCrusher.this.lastChangeTime = TileEntityCrusher.this.world.getTotalWorldTime();
														PacketHandler.NETWORK.sendToAllAround(new PacketUpdateCrusher(TileEntityCrusher.this), new NetworkRegistry.TargetPoint(TileEntityCrusher.this.world.provider.getDimension(),
																TileEntityCrusher.this.pos.getX(), TileEntityCrusher.this.pos.getY(), TileEntityCrusher.this.pos.getZ(), 64.0D));
													}
												}

												@Override
												public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
													if (stack.isEmpty())
														return ItemStack.EMPTY;

													validateSlotIndex(slot);
													if (slot == 0 && !isItemValidTool(stack))
														return stack;
													if (slot == 1 && !isItemValidIngredient(stack))
														return stack;
													if (slot > 1 && !isItemValidProduct(stack, slot))
														return stack;
													return super.insertItem(slot, stack, simulate);
												};
											};
	public ItemStackHandler	inventoryTop	= new ItemStackHandler(SLOTS_TOP.length) {
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

	public ItemStackHandler inventorySides = new ItemStackHandler(SLOTS_SIDES.length) {
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

	public ItemStackHandler	inventoryBottom	= new ItemStackHandler(SLOTS_BOTTOM.length) {
												@Override
												public ItemStack getStackInSlot(int slot) {
													validateSlotIndex(slot);
													return inventory.getStackInSlot(slot + 2);
												}

												@Override
												public void setStackInSlot(int slot, ItemStack stack) {
													inventory.setStackInSlot(slot + 2, stack);
												};

												@Override
												public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
													return inventory.insertItem(slot + 2, stack, simulate);
												};

												@Override
												public ItemStack extractItem(int slot, int amount, boolean simulate) {
													return inventory.extractItem(slot + 2, amount, simulate);
												};

											};
	public double			lastCrushAnimation;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", this.inventory.serializeNBT());
		compound.setLong("lastChangeTime", this.lastChangeTime);
		compound.setFloat("crushTime", this.crushTime);
		compound.setDouble("lastCrushAnimation", this.lastCrushAnimation);
		return super.writeToNBT(compound);
	}

	public boolean isItemValidIngredient(ItemStack stack) {
		ItemStack slot0Stack = inventory.getStackInSlot(0);
		if (slot0Stack.getItem() == Items.CRUSHER_BIT) {
			return (Recipes.getCrushResult(stack) != null) && (Recipes.getCrushResult(stack).size() > 0);
		}
		if (slot0Stack.getItem() == Items.HAMMER) {
			return (Recipes.getHammerResult(stack) != null) && (Recipes.getHammerResult(stack).size() > 0);
		}
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		this.lastChangeTime = compound.getLong("lastChangeTime");
		this.crushTime = compound.getFloat("crushTime");
		this.lastCrushAnimation = compound.getDouble("lastCrushAnimation");
		super.readFromNBT(compound);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || (super.hasCapability(capability, facing));
	}

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			if (facing != null)
				if (facing == EnumFacing.DOWN)
					return (T) inventoryBottom;
				else if (facing == EnumFacing.UP)
					return (T) inventoryTop;
				else
					return (T) inventorySides;
			else
				return (T) inventory;
		return super.getCapability(capability, facing);
	}

	@Override
	public void onLoad() {
		if (this.world.isRemote) {
			PacketHandler.NETWORK.sendToServer(new PacketRequestUpdateCrusher(this));
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
	}

	@Override
	public void update() {
		if (this.world.getBlockState(this.pos).getBlock() != getBlockType()) {
			return;
		}

		if (this.crushTime > 0.0F) {
			this.crushTime--;

			if (canCrush()) {
				if (this.crushTime <= 0.0F) {
					crushItem();
				}
			}
		} else if (canCrush()) {
			ItemStack stack = this.inventory.getStackInSlot(0);
			if (stack.getItem() == Items.CRUSHER_BIT) {
				setCrushTime(((Integer) Recipes.getCrushResult(this.inventory.getStackInSlot(1)).get(7)).intValue() * 1.0F);
			} else if (stack.getItem() == Items.HAMMER) {
				setCrushTime(((Integer) Recipes.getHammerResult(this.inventory.getStackInSlot(1)).get(7)).intValue() * 1.0F);
			}
		} else {
			this.crushTime = 0.0F;
		}
	}

	private void crushItem() {
		if ((this.inventory.getStackInSlot(0) != null) && (!this.inventory.getStackInSlot(1).isEmpty())) {
			ArrayList resultList = null;
			ItemStack stack = this.inventory.getStackInSlot(0);
			if (stack.getItem() == Items.CRUSHER_BIT) {
				resultList = Recipes.getCrushResult(this.inventory.getStackInSlot(1));
			} else if (stack.getItem() == Items.HAMMER) {
				resultList = Recipes.getHammerResult(this.inventory.getStackInSlot(1));
			}
			if (resultList != null) {
				this.inventory.insertItem(2, ((ItemStack) resultList.get(1)).copy(), false);
				this.inventory.insertItem(3, ((ItemStack) resultList.get(2)).copy(), false);
				this.inventory.insertItem(4, ((ItemStack) resultList.get(3)).copy(), false);
				this.inventory.insertItem(5, ((ItemStack) resultList.get(4)).copy(), false);
				this.inventory.insertItem(6, ((ItemStack) resultList.get(5)).copy(), false);
				this.inventory.insertItem(7, ((ItemStack) resultList.get(6)).copy(), false);
				this.inventory.extractItem(1, 1, false);
			} else {
				WIPTech.info("ERROR resultList == null so could NOT CRUSH ITEM");
			}
			return;
		}
		WIPTech.info("ERROR COULD NOT CRUSH ITEM");
		this.crushTime = 0.0F;
	}

	private boolean canCrush() {
		if ((this.inventory.getStackInSlot(0) != null) && (!this.inventory.getStackInSlot(0).isEmpty())) {
			ArrayList resultsList = null;
			ItemStack stack = this.inventory.getStackInSlot(0);
			if (stack.getItem() == Items.CRUSHER_BIT) {
				resultsList = Recipes.getCrushResult(this.inventory.getStackInSlot(1));
			} else if (stack.getItem() == Items.HAMMER) {
				resultsList = Recipes.getHammerResult(this.inventory.getStackInSlot(1));
			}
			boolean recipeExists = resultsList != null;
			if (recipeExists) {
				boolean slot0 = !this.inventory.getStackInSlot(0).isEmpty();
				boolean slot1 = !this.inventory.getStackInSlot(1).isEmpty();
				boolean slot2 = this.inventory.getStackInSlot(2).getCount() < this.inventory.getStackInSlot(2).getMaxStackSize();
				boolean slot3 = this.inventory.getStackInSlot(3).getCount() < this.inventory.getStackInSlot(3).getMaxStackSize();
				boolean slot4 = this.inventory.getStackInSlot(4).getCount() < this.inventory.getStackInSlot(4).getMaxStackSize();
				boolean slot5 = this.inventory.getStackInSlot(5).getCount() < this.inventory.getStackInSlot(5).getMaxStackSize();
				boolean slot6 = this.inventory.getStackInSlot(6).getCount() < this.inventory.getStackInSlot(6).getMaxStackSize();
				boolean slot7 = this.inventory.getStackInSlot(7).getCount() < this.inventory.getStackInSlot(7).getMaxStackSize();
				if ((slot0) && (slot1) && (slot2) && (slot3) && (slot4) && (slot5) && (slot6) && (slot7)) {
					recipeExists = (recipeExists) && (resultsList.size() > 0);
					if (recipeExists) {
						recipeExists = (recipeExists) && (resultsList.get(0) != null);
						if (recipeExists) {
							slot2 = (slot2) && ((ItemStack) resultsList.get(1) != null);
							slot3 = (slot3) && ((ItemStack) resultsList.get(2) != null);
							slot4 = (slot4) && ((ItemStack) resultsList.get(3) != null);
							slot5 = (slot5) && ((ItemStack) resultsList.get(4) != null);
							slot6 = (slot6) && ((ItemStack) resultsList.get(5) != null);
							slot7 = (slot7) && ((ItemStack) resultsList.get(6) != null);
							if ((slot0) && (slot1) && (slot2) && (slot3) && (slot4) && (slot5) && (slot6) && (slot7)) {
								slot2 = (slot2) && (this.inventory.insertItem(2, ((ItemStack) resultsList.get(1)).copy(), true).isEmpty());
								slot3 = (slot3) && (this.inventory.insertItem(3, ((ItemStack) resultsList.get(2)).copy(), true).isEmpty());
								slot4 = (slot4) && (this.inventory.insertItem(4, ((ItemStack) resultsList.get(3)).copy(), true).isEmpty());
								slot5 = (slot5) && (this.inventory.insertItem(5, ((ItemStack) resultsList.get(4)).copy(), true).isEmpty());
								slot6 = (slot6) && (this.inventory.insertItem(6, ((ItemStack) resultsList.get(5)).copy(), true).isEmpty());
								slot7 = (slot7) && (this.inventory.insertItem(7, ((ItemStack) resultsList.get(6)).copy(), true).isEmpty());
								if ((slot0) && (slot1) && (slot2) && (slot3) && (slot4) && (slot5) && (slot6) && (slot7)) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isCrushing() {
		return this.crushTime > 0.0F;
	}

	public float getCrushTime() {
		return this.crushTime;
	}

	public void setCrushTime(float time) {
		this.crushTime = time;
	}

	public int getTotalCrushTime() {
		ItemStack stack0 = this.inventory.getStackInSlot(0);
		ItemStack stack1 = this.inventory.getStackInSlot(1);
		if (stack0.isEmpty() || stack1.isEmpty())
			return 0;

		if (stack0.getItem() == Items.CRUSHER_BIT) {
			ArrayList recipe = Recipes.getCrushResult(stack1);
			if (recipe.size() > 0)
				return (int) recipe.get(7);
		}
		if (stack0.getItem() == Items.HAMMER) {
			ArrayList recipe = Recipes.getHammerResult(stack1);
			if (recipe.size() > 0)
				return (int) recipe.get(7);
		}
		return 0;
	}

	public int getPercentageOfCrushTimeComplete() {
		return (int) Math.round(getFractionOfCrushTimeComplete() * 100.0D);
	}

	public double getFractionOfCrushTimeComplete() {
		return 1 - getFractionOfCrushTimeRemaining() == 1 ? 0 : 1 - getFractionOfCrushTimeRemaining();
	}

	public int getPercentageOfCrushTimeRemaining() {
		return (int) Math.round(getFractionOfCrushTimeRemaining() * 100.0D);
	}

	public double getFractionOfCrushTimeRemaining() {
		if (getCrushTime() > 0.0F && getTotalCrushTime() != 0) {
			return getCrushTime() / getTotalCrushTime();
		}
		return getTotalCrushTime();
	}

	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.DOWN) {
			return SLOTS_BOTTOM;
		} else {
			return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
		}
	}

	public boolean isItemValidProduct(ItemStack stack, int index) {
		boolean returnResult = false;

		ItemStack slot0Stack = inventory.getStackInSlot(0);
		if (slot0Stack.getItem() == Items.CRUSHER_BIT) {
			for (ItemStack result : Recipes.getCrushResults(index - 1)) {
				if ((!returnResult) && (result != null) && (result.getItem() == stack.getItem())) {
					returnResult = true;
				}
			}
		} else if (slot0Stack.getItem() == Items.HAMMER) {
			for (ItemStack result : Recipes.getHammerResults(index - 1)) {
				if ((!returnResult) && (result != null) && (result.getItem() == stack.getItem())) {
					returnResult = true;
				}
			}
		}
		return returnResult;
	}

	public boolean isItemValidTool(ItemStack stack) {
		if ((stack.getItem() == Items.CRUSHER_BIT) || (stack.getItem() == Items.HAMMER)) {
			if (inventory.getStackInSlot(0).isEmpty())
				return true;
		}
		return false;
	}

}