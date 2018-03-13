package cadiboo.wiptech.block.crusher;

import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.network.PacketRequestUpdateCrusher;
import cadiboo.wiptech.network.PacketUpdateCrusher;
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

	//protected ItemStackHandler inputSlot;
	//protected ItemStackHandler outputSlot;
	//private ItemStackHandler outputSlotWrapper;
	public float crushTime;
	//private int activeSlot = -1;
	public long lastChangeTime;

	public ItemStackHandler inventory = new ItemStackHandler(8) {
		@Override
		protected void onContentsChanged(int slot) {
			if (!world.isRemote) {
				lastChangeTime = world.getTotalWorldTime();
				WIPTech.network.sendToAllAround(new PacketUpdateCrusher(TileEntityCrusher.this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
			}
		};
	};

	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		compound.setLong("lastChangeTime", lastChangeTime);
		compound.setFloat("crushTime", crushTime);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		lastChangeTime = compound.getLong("lastChangeTime");
		crushTime = compound.getFloat("crushTime");
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
			WIPTech.network.sendToServer(new PacketRequestUpdateCrusher(this));
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
	}

	@Override
	public void update() {
		if(world.getBlockState(pos).getBlock() != this.getBlockType()) return;
		if(crushTime > 0) {
			--crushTime;
		}
		else {
			crushTime = 100;
		}
	}

	private boolean canCrush(int slot) {
		/*if(inputSlot.getStackInSlot(slot) == null) return false;
		ItemStack result = Processing.getCrushResult(inputSlot.getStackInSlot(slot), true);
		if(result == null) return false;
		if(outputSlot.insertItem(0, result, true) != null) return false;
		 */
		return true;
	}

	public static boolean isCrushing(TileEntityCrusher tileEntity) {
		//return ((BlockCrusher) tileEntity.getWorld().getBlockState(tileEntity.getPos()).getBlock()).isCrushing();
		return tileEntity.isCrushing();
	}
	private boolean isCrushing() {
		return crushTime > 0;
	}
	
	public static float getCrushTime(TileEntityCrusher tileEntity) {
		//return (tileEntity.getWorld().getBlockState(tileEntity.getPos()).getBlock());
		return tileEntity.getCrushTime();
	}
	private float getCrushTime() {
		return crushTime;
	}
	
	public void setCrushTime(TileEntityCrusher tileEntity, float time) {
		tileEntity.setCrushTime(time);
	}
	private void setCrushTime(float time) {
		crushTime = time;
	}

	public static int getTotalCrushTime(TileEntityCrusher tileEntity) {
		return tileEntity.getTotalCrushTime();
	}
	private int getTotalCrushTime() {
		return 100;
	}

	public static int getPercentageOfCrushTimeComplete(TileEntityCrusher tileEntity) {
		return tileEntity.getPercentageOfCrushTimeComplete();
	}
	private int getPercentageOfCrushTimeComplete() {
		return (int) Math.round(getFractionOfCrushTimeComplete()*100);
	}
	
	public static double getFractionOfCrushTimeComplete(TileEntityCrusher tileEntity) {
		return tileEntity.getFractionOfCrushTimeComplete();
	}
	private double getFractionOfCrushTimeComplete() {
		//Utils.getLogger().info("getFractionOfCrushTimeComplete: "+(getTotalCrushTime() - getCrushTime())/getTotalCrushTime());
		return (getTotalCrushTime() - getCrushTime())/getTotalCrushTime();
	}
	
	public static int getPercentageOfCrushTimeRemaining(TileEntityCrusher tileEntity) {
		return tileEntity.getPercentageOfCrushTimeRemaining();
	}
	private int getPercentageOfCrushTimeRemaining() {
		return (int) Math.round(getFractionOfCrushTimeRemaining()*100);
	}
	
	public static double getFractionOfCrushTimeRemaining(TileEntityCrusher tileEntity) {
		return tileEntity.getFractionOfCrushTimeRemaining();
	}
	private double getFractionOfCrushTimeRemaining() {
		//Utils.getLogger().info("getFractionOfCrushTimeRemaining: "+getCrushTime() / getTotalCrushTime());
		return getCrushTime() / getTotalCrushTime();
	}
}