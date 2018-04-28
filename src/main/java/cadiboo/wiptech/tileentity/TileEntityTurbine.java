package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.capability.ItemEnergyItemHandler;
import cadiboo.wiptech.config.Configuration;
import cadiboo.wiptech.util.CustomEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class TileEntityTurbine extends TileEntityBase implements ITickable {

	protected boolean				cachedCanProduce	= false;
	protected ItemEnergyItemHandler	leftSlot;
	protected ItemEnergyItemHandler	rightSlot;

	private static final AxisAlignedBB RENDER_AABB = new AxisAlignedBB(-1, 0, -1, 2, 6, 2);

	public TileEntityTurbine() {
		leftSlot = new ItemEnergyItemHandler(1);
		rightSlot = new ItemEnergyItemHandler(1);
	}

	private CustomEnergyStorage energy = new CustomEnergyStorage(10000, Integer.MAX_VALUE) {
		@Override
		public boolean canReceive() {
			return false;
		}
	};

	@Override
	public void update() {
		if (this.canProduce()) {
			this.energy.forceReceive(Configuration.energy.TurbineProduction * this.getPos().getY(), false);
		}
		if (this.energy.getEnergyStored() > 0) {
			ItemStack left = this.leftSlot.getStackInSlot(0);
			if (!left.isEmpty()) {
				IEnergyStorage storage = left.getCapability(CapabilityEnergy.ENERGY, null);
				if (storage != null) {
					this.energy.extractEnergy(storage.receiveEnergy(this.energy.getEnergyStored(), false), false);
				}
			}
			ItemStack right = this.rightSlot.getStackInSlot(0);
			if (right.isEmpty() && !left.isEmpty()) {
				IEnergyStorage storage = left.getCapability(CapabilityEnergy.ENERGY, null);
				if (storage != null && storage.getEnergyStored() == storage.getMaxEnergyStored()) {
					if (!leftSlot.extractItem(0, 1, true).isEmpty())
						rightSlot.insertItem(0, leftSlot.extractItem(0, 1, false), false);
				}
			}

			pushEnergy(this.world, this.pos, this.energy, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST);
		}
		this.updateBase();
		syncToClients();
	}

	public static int pushEnergy(World world, BlockPos pos, IEnergyStorage energy, EnumFacing... sides) {
		if (!world.isRemote && energy.canExtract()) {
			for (EnumFacing side : sides) {
				TileEntity tile = world.getTileEntity(pos.offset(side));
				if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite())) {
					IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
					if (storage != null && storage.canReceive()) {
						return energy.extractEnergy(storage.receiveEnergy(energy.extractEnergy(Integer.MAX_VALUE, true), false), false);
					}
				}
			}
		}
		return 0;
	}

	public boolean canProduce() {
		if (this.world.getTotalWorldTime() % 1 == 0)
			cachedCanProduce = this.world.canBlockSeeSky(getPos().up(6));
		return cachedCanProduce;
	}

	// private void sendUpdates() {
	// world.markBlockRangeForRenderUpdate(pos, pos);
	// world.notifyBlockUpdate(pos, getState(), getState(), 3);
	// world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
	// markDirty();
	// }

	private IBlockState getState() {
		return world.getBlockState(pos);
	}

	// @Override
	// @Nullable
	// public SPacketUpdateTileEntity getUpdatePacket() {
	// return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
	// }
	//
	// @Override
	// public NBTTagCompound getUpdateTag() {
	// // NBTTagCompound compound = new NBTTagCompound();
	// // compound.setInteger("Energy", this.getCapability(CapabilityEnergy.ENERGY,
	// // null).getEnergyStored());
	// // return this.writeToNBT(compound);
	// return super.getUpdateTag();
	// }
	//
	// @Override
	// public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
	// super.onDataPacket(net, pkt);
	// handleUpdateTag(pkt.getNbtCompound());
	// }

	@Override
	public IItemHandler getInventory(EnumFacing side) {
		if (side == EnumFacing.UP) {
			return leftSlot;
		}
		if (side == EnumFacing.DOWN) {
			return rightSlot;
		}
		return new CombinedInvWrapper(leftSlot, rightSlot);
	}

	@Override
	public IEnergyStorage getEnergy(EnumFacing side) {
		return energy;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return RENDER_AABB.offset(pos);
	}

}
