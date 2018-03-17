package cadiboo.wiptech.block.crusher;

import java.util.ArrayList;

import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.init.Recipes;
import cadiboo.wiptech.network.PacketRequestUpdateCrusher;
import cadiboo.wiptech.network.PacketUpdateCrusher;
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

	public float crushTime;
	public long lastChangeTime;
	
	public static int getSlots() {return 8;}
	
	public ItemStackHandler inventory = new ItemStackHandler(getSlots())
	{
		protected void onContentsChanged(int slot)
		{
			if (!TileEntityCrusher.this.world.isRemote)
			{
				TileEntityCrusher.this.lastChangeTime = TileEntityCrusher.this.world.getTotalWorldTime();
				WIPTech.network.sendToAllAround(new PacketUpdateCrusher(TileEntityCrusher.this), new NetworkRegistry.TargetPoint(TileEntityCrusher.this.world.provider.getDimension(), TileEntityCrusher.this.pos.getX(), TileEntityCrusher.this.pos.getY(), TileEntityCrusher.this.pos.getZ(), 64.0D));
			}
		}
	};

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setTag("inventory", this.inventory.serializeNBT());
		compound.setLong("lastChangeTime", this.lastChangeTime);
		compound.setFloat("crushTime", this.crushTime);
		return super.writeToNBT(compound);
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		this.lastChangeTime = compound.getLong("lastChangeTime");
		this.crushTime = compound.getFloat("crushTime");
		super.readFromNBT(compound);
	}

	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || (super.hasCapability(capability, facing));
	}

	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
	}

	public void onLoad()
	{
		if (this.world.isRemote) {
			WIPTech.network.sendToServer(new PacketRequestUpdateCrusher(this));
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
	}

	public void update()
	{
		if (this.world.getBlockState(this.pos).getBlock() != getBlockType()) {
			return;
		}

		if (this.crushTime > 0.0F)
		{
			this.crushTime--;

			if(canCrush()) {
				if (this.crushTime <= 0.0F) {
					crushItem();
				}
			}
		} else if (canCrush()){
			ItemStack stack = this.inventory.getStackInSlot(0);
			if (stack.getItem() == Items.CRUSHER_BIT) {
				setCrushTime(((Integer)Recipes.getCrushResult(this.inventory.getStackInSlot(1)).get(7)).intValue() * 1.0F);
			} else if (stack.getItem() == Items.HAMMER) {
				setCrushTime(((Integer)Recipes.getHammerResult(this.inventory.getStackInSlot(1)).get(7)).intValue() * 1.0F);
			}
		}
		else
		{
			this.crushTime = 0.0F;
		}
	}

	private void crushItem()
	{
		if ((this.inventory.getStackInSlot(0) != null) && (!this.inventory.getStackInSlot(1).isEmpty()))
		{
			ArrayList resultList = null;
			ItemStack stack = this.inventory.getStackInSlot(0);
			if (stack.getItem() == Items.CRUSHER_BIT) {
				resultList = Recipes.getCrushResult(this.inventory.getStackInSlot(1));
			} else if (stack.getItem() == Items.HAMMER) {
				resultList = Recipes.getHammerResult(this.inventory.getStackInSlot(1));
			}
			if (resultList != null)
			{
				this.inventory.insertItem(2, ((ItemStack)resultList.get(1)).copy(), false);
				this.inventory.insertItem(3, ((ItemStack)resultList.get(2)).copy(), false);
				this.inventory.insertItem(4, ((ItemStack)resultList.get(3)).copy(), false);
				this.inventory.insertItem(5, ((ItemStack)resultList.get(4)).copy(), false);
				this.inventory.insertItem(6, ((ItemStack)resultList.get(5)).copy(), false);
				this.inventory.insertItem(7, ((ItemStack)resultList.get(6)).copy(), false);
				this.inventory.extractItem(1, 1, false);
			}
			else
			{
				WIPTech.logger.info("ERROR resultList =null so could NOT CRUSH ITEM");
			}
			return;
		}
		WIPTech.logger.info("ERROR COULD NOT CRUSH ITEM");
		this.crushTime = 0.0F;
	}

	private boolean canCrush()
	{
		if ((this.inventory.getStackInSlot(0) != null) && (!this.inventory.getStackInSlot(0).isEmpty()))
		{
			ArrayList resultsList = null;
			ItemStack stack = this.inventory.getStackInSlot(0);
			if (stack.getItem() == Items.CRUSHER_BIT) {
				resultsList = Recipes.getCrushResult(this.inventory.getStackInSlot(1));
			} else if (stack.getItem() == Items.HAMMER) {
				resultsList = Recipes.getHammerResult(this.inventory.getStackInSlot(1));
			}
			boolean recipeExists = resultsList != null;
			if (recipeExists)
			{
				boolean slot0 = !this.inventory.getStackInSlot(0).isEmpty();
				boolean slot1 = !this.inventory.getStackInSlot(1).isEmpty();
				boolean slot2 = this.inventory.getStackInSlot(2).getCount() < this.inventory.getStackInSlot(2).getMaxStackSize();
				boolean slot3 = this.inventory.getStackInSlot(3).getCount() < this.inventory.getStackInSlot(3).getMaxStackSize();
				boolean slot4 = this.inventory.getStackInSlot(4).getCount() < this.inventory.getStackInSlot(4).getMaxStackSize();
				boolean slot5 = this.inventory.getStackInSlot(5).getCount() < this.inventory.getStackInSlot(5).getMaxStackSize();
				boolean slot6 = this.inventory.getStackInSlot(6).getCount() < this.inventory.getStackInSlot(6).getMaxStackSize();
				boolean slot7 = this.inventory.getStackInSlot(7).getCount() < this.inventory.getStackInSlot(7).getMaxStackSize();
				if ((slot0) && (slot1) && (slot2) && (slot3) && (slot4) && (slot5) && (slot6) && (slot7))
				{
					recipeExists = (recipeExists) && (resultsList.size() > 0);
					if (recipeExists)
					{
						recipeExists = (recipeExists) && (resultsList.get(0) != null);
						if (recipeExists)
						{
							slot2 = (slot2) && ((ItemStack)resultsList.get(1) != null);
							slot3 = (slot3) && ((ItemStack)resultsList.get(2) != null);
							slot4 = (slot4) && ((ItemStack)resultsList.get(3) != null);
							slot5 = (slot5) && ((ItemStack)resultsList.get(4) != null);
							slot6 = (slot6) && ((ItemStack)resultsList.get(5) != null);
							slot7 = (slot7) && ((ItemStack)resultsList.get(6) != null);
							if ((slot0) && (slot1) && (slot2) && (slot3) && (slot4) && (slot5) && (slot6) && (slot7))
							{
								slot2 = (slot2) && (this.inventory.insertItem(2, ((ItemStack)resultsList.get(1)).copy(), true).isEmpty());
								slot3 = (slot3) && (this.inventory.insertItem(3, ((ItemStack)resultsList.get(2)).copy(), true).isEmpty());
								slot4 = (slot4) && (this.inventory.insertItem(4, ((ItemStack)resultsList.get(3)).copy(), true).isEmpty());
								slot5 = (slot5) && (this.inventory.insertItem(5, ((ItemStack)resultsList.get(4)).copy(), true).isEmpty());
								slot6 = (slot6) && (this.inventory.insertItem(6, ((ItemStack)resultsList.get(5)).copy(), true).isEmpty());
								slot7 = (slot7) && (this.inventory.insertItem(7, ((ItemStack)resultsList.get(6)).copy(), true).isEmpty());
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

	public static boolean isCrushing(TileEntityCrusher tileEntity)
	{
		return tileEntity.isCrushing();
	}

	private boolean isCrushing()
	{
		return this.crushTime > 0.0F;
	}

	public static float getCrushTime(TileEntityCrusher tileEntity)
	{
		return tileEntity.getCrushTime();
	}

	private float getCrushTime()
	{
		return this.crushTime;
	}

	public void setCrushTime(TileEntityCrusher tileEntity, float time)
	{
		tileEntity.setCrushTime(time);
	}

	private void setCrushTime(float time)
	{
		this.crushTime = time;
	}

	public static int getTotalCrushTime(TileEntityCrusher tileEntity)
	{
		return tileEntity.getTotalCrushTime();
	}

	private int getTotalCrushTime()
	{
		ItemStack stack = this.inventory.getStackInSlot(0);
		if (stack.getItem() == Items.CRUSHER_BIT) {
			return ((Integer)(!this.inventory.getStackInSlot(1).isEmpty() ? Recipes.getCrushResult(this.inventory.getStackInSlot(1)).get(7) : Integer.valueOf(0))).intValue();
		}
		if (stack.getItem() == Items.HAMMER) {
			return ((Integer)(!this.inventory.getStackInSlot(1).isEmpty() ? Recipes.getHammerResult(this.inventory.getStackInSlot(1)).get(7) : Integer.valueOf(0))).intValue();
		}
		return 0;
	}

	public static int getPercentageOfCrushTimeComplete(TileEntityCrusher tileEntity)
	{
		return tileEntity.getPercentageOfCrushTimeComplete();
	}

	private int getPercentageOfCrushTimeComplete()
	{
		return (int)Math.round(getFractionOfCrushTimeComplete() * 100.0D);
	}

	public static double getFractionOfCrushTimeComplete(TileEntityCrusher tileEntity)
	{
		return tileEntity.getFractionOfCrushTimeComplete();
	}

	private double getFractionOfCrushTimeComplete()
	{
		if (getCrushTime() > 0.0F) {
			return (getTotalCrushTime() - getCrushTime()) / getTotalCrushTime();
		}
		return 0.0D;
	}

	public static int getPercentageOfCrushTimeRemaining(TileEntityCrusher tileEntity)
	{
		return tileEntity.getPercentageOfCrushTimeRemaining();
	}

	private int getPercentageOfCrushTimeRemaining()
	{
		return (int)Math.round(getFractionOfCrushTimeRemaining() * 100.0D);
	}

	public static double getFractionOfCrushTimeRemaining(TileEntityCrusher tileEntity)
	{
		return tileEntity.getFractionOfCrushTimeRemaining();
	}

	private double getFractionOfCrushTimeRemaining()
	{
		if (getCrushTime() > 0.0F) {
			return getCrushTime() / getTotalCrushTime();
		}
		return getTotalCrushTime();
	}
}