package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.capability.energy.IEnergyUser;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author Cadiboo
 */
public class TileEntityPeripheral extends TileEntity implements IEnergyUser, ITileEntitySyncable {

	private BlockPos centralPos;

	@Override
	public ModEnergyStorage getEnergy() {
		if (this.getCentral() == null) {
			return null;
		}
		return this.getCentral().getEnergy();
	}

	@Override
	public BlockPos getPosition() {
		return this.getPos();
	}

	/**
	 * @return
	 * @return
	 */
	public <T extends TileEntity & IEnergyUser> T getCentral() {
		if (!this.hasWorld()) {
			return null;
		}

		final World world = this.getWorld();

		if (world == null) {
			return null;
		}

		if (this.getCentralPos() == null) {
			return null;
		}

		final TileEntity tile = world.getTileEntity(this.getCentralPos());

		if (tile == null) {
			return null;
		}

		if (!(tile instanceof IEnergyUser)) {
			return null;
		}

		return (T) tile;
	}

	public BlockPos getCentralPos() {
		if (this.centralPos == null) {
			return BlockPos.ORIGIN;
		}
		return this.centralPos;
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("centralPos")) {
			this.centralPos = BlockPos.fromLong(compound.getLong("centralPos"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		if ((this.centralPos != null) && !this.centralPos.equals(BlockPos.ORIGIN)) {
			compound.setLong("centralPos", this.centralPos.toLong());
		}
		return compound;
	}

	public void setCentralPos(final BlockPos pos) {
		this.centralPos = pos;
		this.syncToClients();
	}

	@Override
	public void onLoad() {
		this.syncToClients();
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> TileEntityPeripheral.this.syncToClients());
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
		return super.getCapability(capability, facing);
	}

	@Override
	public void readNBT(final NBTTagCompound syncTag) {
		this.readFromNBT(syncTag);
	}

	@Override
	public void writeNBT(final NBTTagCompound syncTag) {
		this.writeToNBT(syncTag);
	}

}
