package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.capability.energy.IEnergyUser;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import cadiboo.wiptech.capability.inventory.IInventoryUser;
import cadiboo.wiptech.capability.inventory.ModItemStackHandler;
import cadiboo.wiptech.init.ModBlocks;
import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * @author Cadiboo
 */
public class TileEntityAssemblyTable extends TileEntity implements IEnergyUser, IInventoryUser, ITileEntityCentral {

	public static final int	WIDTH	= 3;
	public static final int	HEIGHT	= 2;
	public static final int	DEPTH	= 3;

	private final ModEnergyStorage		energy;
	private final ModItemStackHandler	inventory;

	public TileEntityAssemblyTable() {
		this.energy = new ModEnergyStorage(100000);
		this.inventory = new ModItemStackHandler(AttachmentPoints.values().length + 2);
	}

	@Override
	public ModEnergyStorage getEnergy() {
		return this.energy;
	}

	@Override
	public ModItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public BlockPos getPosition() {
		return this.getPos();
	}

	@Override
	public void onLoad() {
		super.onLoad();

		ModBlocks.ASSEMBLY_TABLE.getPeripheralPositions(this.pos).forEach(peripheralPos -> {
			final TileEntity tile = this.world.getTileEntity(peripheralPos);

			if (tile == null) {
				return;
			}

			if (!(tile instanceof TileEntityPeripheral)) {
				return;
			}

			((TileEntityPeripheral) tile).setCentralPos(this.pos);
		});
	}

	@Override
	public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
		return this.getCapability(capability, facing) != null;
	}

	@Override
	public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return (T) this.getEnergy();
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) this.getInventory();
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("energy")) {
			this.getEnergy().setEnergyStored(compound.getInteger("energy"), false);
		}
		if (compound.hasKey("inventory")) {
			this.getInventory().deserializeNBT(compound.getCompoundTag("inventory"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setInteger("energy", this.energy.getEnergyStored());
		compound.setTag("inventory", this.getInventory().serializeNBT());

		return compound;
	}

}
