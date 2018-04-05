package cadiboo.wiptech.tileentity;

import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.util.CustomEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityTurbine extends TileEntityBase implements ITickable {

	public static final int ENERGY_PRODUCTION_BASE = 10;
	public static final int ENERGY_STORAGE = 10000;
	private boolean cachedCanProduce = false;

	public CustomEnergyStorage energy = new CustomEnergyStorage(ENERGY_STORAGE, Integer.MAX_VALUE) {
		@Override
		public boolean canReceive() {
			return false;
		}
	};

	public static int getSlots() {
		return 2;
	}

	public ItemStackHandler inventory = new ItemStackHandler(getSlots()) {
		@Override
		protected void onContentsChanged(int slot) {
			if (!TileEntityTurbine.this.world.isRemote) {
				// TileEntityTurbine.this.lastChangeTime =
				// TileEntityTurbine.this.world.getTotalWorldTime();
				// PacketHandler.NETWORK.sendToAllAround(new
				// PacketUpdateCrusher(TileEntityCrusher.this), new
				// NetworkRegistry.TargetPoint(TileEntityCrusher.this.world.provider.getDimension(),
				// TileEntityCrusher.this.pos.getX(), TileEntityCrusher.this.pos.getY(),
				// TileEntityCrusher.this.pos.getZ(), 64.0D));
			}
		}
	};

	@Override
	public void onLoad() {
		if (this.world.isRemote) {
			// PacketHandler.NETWORK.sendToServer(new PacketRequestUpdateCrusher(this));
		}
	}

	@Override
	public void update() {
		// WIPTech.logger.info(this.energy.getEnergyStored());
		if (!world.isRemote) {

			if (this.canProduce()) {
				this.energy.forceReceive(this.getEnergyProduction(), false);
				// TODO CHANGE THIS
				// WIPTech.logger.info(energy.getEnergyStored());
				this.markDirty();
			}
			if (this.energy.getEnergyStored() > 0) {

				// TODO maybe remove this and 1st item slot entirely //maybe not
				ItemStack stack0 = this.inventory.getStackInSlot(0);
				if (stack0 != null && !stack0.isEmpty()) {
					IEnergyStorage stackEnergy = stack0.getCapability(CapabilityEnergy.ENERGY, null);
					if (stackEnergy != null)
						stackEnergy.extractEnergy(this.energy.forceReceive(stackEnergy.getEnergyStored(), false),
								false);
				}

				ItemStack stack1 = this.inventory.getStackInSlot(getSlots() - 1);
				if (stack1 != null && !stack1.isEmpty()) {
					IEnergyStorage stackEnergy = stack1.getCapability(CapabilityEnergy.ENERGY, null);
					if (stackEnergy != null)
						this.energy.extractEnergy(stackEnergy.receiveEnergy(this.energy.getEnergyStored(), false),
								false);
				}

				pushEnergy(this.world, this.pos, this.energy, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH,
						EnumFacing.WEST, EnumFacing.EAST);
			}
		}
	}

	public int getEnergyProduction() {
		return ENERGY_PRODUCTION_BASE * this.getPos().getY();
	}

	public boolean canProduce() {
		if (this.world.getTotalWorldTime() % 1 == 0)
			cachedCanProduce = this.world.canBlockSeeSky(getPos().up(6));
		return cachedCanProduce;
	}

	public static int pushEnergy(World world, BlockPos pos, IEnergyStorage energy, EnumFacing... sides) {
		if (!world.isRemote && energy.canExtract()) {
			// WIPTech.logger.info("attempting to push");
			for (EnumFacing side : sides) {
				TileEntity tile = world.getTileEntity(pos.offset(side));
				if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite())) {
					// WIPTech.logger.info("Pushing "+energy.getEnergyStored()+" energy");
					IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
					if (storage != null && storage.canReceive()) {
						return energy.extractEnergy(
								storage.receiveEnergy(energy.extractEnergy(Integer.MAX_VALUE, true), false), false);
					}
				}
			}
		}
		return 0;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		if (capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) inventory;
		if (capability == CapabilityEnergy.ENERGY)
			return (T) energy;
		WIPTech.logger.info("shouldnt be here :/");
		return super.getCapability(capability, facing);
	}

	private static final AxisAlignedBB RenderBB = new AxisAlignedBB(-1, 0, -1, 2, 6, 2);

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return RenderBB.offset(pos);
	}

}
