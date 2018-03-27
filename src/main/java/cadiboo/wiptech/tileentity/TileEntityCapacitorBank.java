package cadiboo.wiptech.tileentity;

import javax.annotation.Nullable;

import cadiboo.wiptech.block.BlockCapacitorBank;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.util.CustomEnergyStorage;
import cadiboo.wiptech.util.EnergyUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Util;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCapacitorBank extends TileEntityBase implements ITickable {

	public static final int ENERGY_TRANSFER = 1000;
	public static final int ENERGY_STORAGE = 10000;
	
	public static int getSlots() {return 8;}

	public ItemStackHandler inventory = new ItemStackHandler(getSlots())
	{
		protected void onContentsChanged(int slot)
		{
			TileEntityCapacitorBank.this.syncToClients();
		}
	};
	
	private CustomEnergyStorage energy = new CustomEnergyStorage(Integer.MAX_VALUE);

    @Nullable
    @Override
    public IEnergyStorage getEnergy(@Nullable EnumFacing side) {
        return energy;
    }

   /* @Override
    public void onLoad() {
        Block block = world.getBlockState(this.getPos()).getBlock();
        if(block instanceof BlockCapacitorBank){
            int oldEnergy = this.energy.getEnergyStored();
           // BlockBattery.Type batType = ((BlockBattery) block).getBatteryType();
            this.energy = new CustomEnergyStorage(oldEnergy).setTile(this);
        }
    }*/

    @Override
    public void update() {
        this.updateBase();
        if(!world.isRemote){
            if(this.energy.getEnergyStored() > 0)
                EnergyUtils.pushEnergy(this.world, this.pos, energy, EnumFacing.NORTH);
            if(this.world.getTotalWorldTime() % 100 == 0){
                this.syncToClients();
            }
        }
    }

    @Override
    public void onSyncPacket() {
        this.markForRenderUpdate();
    }

   /* public int getChargingState(){
        IEnergyStorage storage = this.normal;
        if(storage != null && storage.getMaxEnergyStored() > 0){
            return (int) (storage.getEnergyStored() / (storage.getMaxEnergyStored() * 1.0F) * 10.0F);
        }
        return 0;
    }

    public EnumFacing getInputSide(){
        return this.world != null ? this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING) : EnumFacing.NORTH;
    }

    public EnumFacing getOutputSide(){
        return this.getInputSide().getOpposite();
    }
    */

}
