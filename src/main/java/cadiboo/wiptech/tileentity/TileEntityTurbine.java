package cadiboo.wiptech.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.handler.network.PacketHandler;
import cadiboo.wiptech.handler.network.PacketRequestUpdateCrusher;
import cadiboo.wiptech.util.CustomEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityTurbine extends TileEntityBase implements ITickable {

	public static int ENERGY_PRODUCTION = 8;

	public CustomEnergyStorage energy = new CustomEnergyStorage(ENERGY_PRODUCTION){
        @Override
        public boolean canReceive() {
            return false;
        }
    };

	@Override
	public void update() {
		if(!world.isRemote){

			if(this.canProduce()){
				energy.forceReceive(ENERGY_PRODUCTION, false);
				this.markDirty();
			}
			if(this.energy.getEnergyStored() > 0){
                pushEnergy(this.world, this.pos, this.energy, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST);
            }
		}
	}

	private boolean canProduce() {
		return true;
	}

	public static int pushEnergy(World world, BlockPos pos, IEnergyStorage energy, EnumFacing... sides){
		if(!world.isRemote && energy.canExtract()){
			for(EnumFacing side : sides){
				TileEntity tile = world.getTileEntity(pos.offset(side));
				if(tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite())){
					IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
					if(storage != null && storage.canReceive()){
						return energy.extractEnergy(storage.receiveEnergy(energy.extractEnergy(Integer.MAX_VALUE, true), false), false);
					}
				}
			}
		}
		return 0;
	}
}
