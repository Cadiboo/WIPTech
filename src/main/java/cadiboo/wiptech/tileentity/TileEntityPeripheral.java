package cadiboo.wiptech.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author Cadiboo
 */
public class TileEntityPeripheral extends TileEntity implements ITileEntitySyncable {

	private BlockPos centralPos;

	@Override
	public BlockPos getPosition() {
		return this.getPos();
	}

	public <T extends TileEntity & ITileEntityCentral> T getCentral() {
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

		if (!(tile instanceof ITileEntityCentral)) {
			return null;
		}

		return (T) tile;
	}

	/**
	 * @return the central pos or (0, 0, 0) if it is null
	 */
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
		if (this.getCentral() != null) {
			return this.getCentral().getCapability(capability, facing);

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
