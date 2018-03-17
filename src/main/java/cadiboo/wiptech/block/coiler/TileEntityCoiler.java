package cadiboo.wiptech.block.coiler;

import java.util.ArrayList;

import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.coiler.TileEntityCoiler;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.init.Recipes;
import cadiboo.wiptech.network.PacketRequestUpdateCoiler;
import cadiboo.wiptech.network.PacketUpdateCoiler;
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

public class TileEntityCoiler extends TileEntity implements ITickable {

	public float windTime;
	//private int activeSlot = -1;
	public long lastChangeTime;
	
	public static int getSlots() {
		return 10;
	}

	public ItemStackHandler inventory = new ItemStackHandler(10) {
		@Override
		protected void onContentsChanged(int slot) {
			if (!world.isRemote) {
				lastChangeTime = world.getTotalWorldTime();
				WIPTech.network.sendToAllAround(new PacketUpdateCoiler(TileEntityCoiler.this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
			}
		};
	};


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		compound.setLong("lastChangeTime", lastChangeTime);
		compound.setFloat("windTime", windTime);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		lastChangeTime = compound.getLong("lastChangeTime");
		windTime = compound.getFloat("windTime");
		super.readFromNBT(compound);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
	}

	@Override
	public void onLoad() {
		if (world.isRemote) {
			WIPTech.network.sendToServer(new PacketRequestUpdateCoiler(this));
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
	}

	@Override
	public void update() {
		if(world.getBlockState(pos).getBlock() != this.getBlockType()) return;
		if(windTime > 0) {
			--windTime;
			if(!canCoil()) {
				windTime = 0;
			}
			else if (windTime <= 0) {
				windItem();
			}
		}
		else {
			if(canCoil())
			{
				this.setWindTime((int) Recipes.getCoilResult(inventory.getStackInSlot(1)).get(7) * 1.0F);
			} else {
				windTime = 0;
			}
		}
	}

	private void windItem()
	{
		if(inventory.getStackInSlot(0) != null && !inventory.getStackInSlot(1).isEmpty()){

			/*ItemStack stack = inventory.getStackInSlot(0);
			ArrayList resultList = (ArrayList) Recipes.getCoilResult(inventory.getStackInSlot(1));
			if(resultList !=null) {
				inventory.insertItem(2, ((ItemStack) resultList.get(1)).copy(), false);
				inventory.insertItem(3, ((ItemStack) resultList.get(2)).copy(), false);
				inventory.insertItem(4, ((ItemStack) resultList.get(3)).copy(), false);
				inventory.insertItem(5, ((ItemStack) resultList.get(4)).copy(), false);
				inventory.insertItem(6, ((ItemStack) resultList.get(5)).copy(), false);
				inventory.insertItem(7, ((ItemStack) resultList.get(6)).copy(), false);
				inventory.extractItem(1, 1, false);
			} else {
				WIPTech.logger.info("ERROR resultList =null so could NOT Coil ITEM");
			}
			return;*/
		} else {
			WIPTech.logger.info("ERROR COULD NOT Coil ITEM");
			windTime = 0;
			return;
		}
	}

	private boolean canCoil() {
		if(inventory.getStackInSlot(0) != null && !inventory.getStackInSlot(0).isEmpty())
		{
			
			/*ItemStack stack = inventory.getStackInSlot(0);
			ArrayList resultsList = (ArrayList) Recipes.getCoilResult(inventory.getStackInSlot(1));

			boolean recipeExists = resultsList!=null;
			if(recipeExists) {
				boolean slot0 = !inventory.getStackInSlot(0).isEmpty();
				boolean slot1 = !inventory.getStackInSlot(1).isEmpty();
				boolean slot2 = inventory.getStackInSlot(2).getCount() < inventory.getStackInSlot(2).getMaxStackSize();
				boolean slot3 = inventory.getStackInSlot(3).getCount() < inventory.getStackInSlot(3).getMaxStackSize();
				boolean slot4 = inventory.getStackInSlot(4).getCount() < inventory.getStackInSlot(4).getMaxStackSize();
				boolean slot5 = inventory.getStackInSlot(5).getCount() < inventory.getStackInSlot(5).getMaxStackSize();
				boolean slot6 = inventory.getStackInSlot(6).getCount() < inventory.getStackInSlot(6).getMaxStackSize();
				boolean slot7 = inventory.getStackInSlot(7).getCount() < inventory.getStackInSlot(7).getMaxStackSize();

				if(slot0 && slot1 && slot2 && slot3 && slot4 && slot5 && slot6 && slot7) {
					recipeExists = recipeExists && resultsList.size()>0;
					if(recipeExists) {
						recipeExists = recipeExists && resultsList.get(0)!=null;
						if(recipeExists) {
							slot2 = slot2&& (ItemStack) resultsList.get(1)!=null;
							slot3 = slot3&& (ItemStack) resultsList.get(2)!=null;
							slot4 = slot4&& (ItemStack) resultsList.get(3)!=null;
							slot5 = slot5&& (ItemStack) resultsList.get(4)!=null;
							slot6 = slot6&& (ItemStack) resultsList.get(5)!=null;
							slot7 = slot7&& (ItemStack) resultsList.get(6)!=null;
							if(slot0 && slot1 && slot2 && slot3 && slot4 && slot5 && slot6 && slot7) {
								slot2 = slot2&& inventory.insertItem(2, ((ItemStack) resultsList.get(1)).copy(), true).isEmpty();
								slot3 = slot3&& inventory.insertItem(3, ((ItemStack) resultsList.get(2)).copy(), true).isEmpty();
								slot4 = slot4&& inventory.insertItem(4, ((ItemStack) resultsList.get(3)).copy(), true).isEmpty();
								slot5 = slot5&& inventory.insertItem(5, ((ItemStack) resultsList.get(4)).copy(), true).isEmpty();
								slot6 = slot6&& inventory.insertItem(6, ((ItemStack) resultsList.get(5)).copy(), true).isEmpty();
								slot7 = slot7&& inventory.insertItem(7, ((ItemStack) resultsList.get(6)).copy(), true).isEmpty();
								if(slot0 && slot1 && slot2 && slot3 && slot4 && slot5 && slot6 && slot7) {
									return true;
								}
							}
						}
					}
				}
			}*/

		}
		return false;
	}

	public static boolean isWinding(TileEntityCoiler tileEntity) {
		//return ((BlockCoiler) tileEntity.getWorld().getBlockState(tileEntity.getPos()).getBlock()).isCoiling();
		return tileEntity.isWinding();
	}
	private boolean isWinding() {
		return windTime > 0;
	}

	public static float getWindTime(TileEntityCoiler tileEntity) {
		//return (tileEntity.getWorld().getBlockState(tileEntity.getPos()).getBlock());
		return tileEntity.getWindTime();
	}
	private float getWindTime() {
		return this.windTime;
	}

	public void setWindTime(TileEntityCoiler tileEntity, float time) {
		tileEntity.setWindTime(time);
	}
	private void setWindTime(float windTime) {
		this.windTime = windTime;
	}

	public static int getTotalWindTime(TileEntityCoiler tileEntity) {
		return tileEntity.getTotalWindTime();
	}
	private int getTotalWindTime() {
		return (int) (!inventory.getStackInSlot(1).isEmpty()?Recipes.getCoilResult(inventory.getStackInSlot(1)).get(7):0);
	}

	public static int getPercentageOfWindTimeComplete(TileEntityCoiler tileEntity) {
		return tileEntity.getPercentageOfWindTimeComplete();
	}
	private int getPercentageOfWindTimeComplete() {
		return (int) Math.round(getFractionOfWindTimeComplete()*100);
	}

	public static double getFractionOfWindTimeComplete(TileEntityCoiler tileEntity) {
		return tileEntity.getFractionOfWindTimeComplete();
	}
	private double getFractionOfWindTimeComplete() {
		if(getWindTime()>0)
			return (getTotalWindTime() - getWindTime())/getTotalWindTime();
		else
			return 0;
	}

	public static int getPercentageOfWindTimeRemaining(TileEntityCoiler tileEntity) {
		return tileEntity.getPercentageOfWindTimeRemaining();
	}
	private int getPercentageOfWindTimeRemaining() {
		return (int) Math.round(getFractionOfWindTimeRemaining()*100);
	}

	public static double getFractionOfWindTimeRemaining(TileEntityCoiler tileEntity) {
		return tileEntity.getFractionOfWindTimeRemaining();
	}
	private double getFractionOfWindTimeRemaining() {
		if(getWindTime()>0)
			return getWindTime() / getTotalWindTime();
		else
			return getTotalWindTime();
	}

}