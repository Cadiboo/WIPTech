package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.util.CustomEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityTurbine extends TileEntityBase implements ITickable {

	public static final int ENERGY_PRODUCTION = 8;
	public static final int ENERGY_STORAGE = 10000;

	public CustomEnergyStorage energy = new CustomEnergyStorage(ENERGY_STORAGE){
		@Override
		public boolean canReceive() {
			return false;
		}
	};

	@Override
	public void update() {
		if(!world.isRemote){

			if(this.canProduce()){
				this.energy.receiveEnergyInternal(ENERGY_PRODUCTION, false);
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
			WIPTech.logger.info("attempting to push");
			for(EnumFacing side : sides){
				TileEntity tile = world.getTileEntity(pos.offset(side));
				if(tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite())){
					WIPTech.logger.info("Pushing "+energy.getEnergyStored()+" energy");
					IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
					if(storage != null && storage.canReceive()){
						return energy.extractEnergy(storage.receiveEnergy(energy.extractEnergy(Integer.MAX_VALUE, true), false), false);
					}
				}
			}
		}
		return 0;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1, 3, 1));
	}
}
