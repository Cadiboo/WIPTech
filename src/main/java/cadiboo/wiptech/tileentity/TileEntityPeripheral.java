package cadiboo.wiptech.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author Cadiboo
 */
public class TileEntityPeripheral extends TileEntity implements IModTileEntity, ITileEntitySyncable, ITickable {

	public static final String		CENTRAL_POS_TAG					= "centralPos";
	public static final BlockPos	NULL_CENTRAL_POS_REPLACEMENT	= new BlockPos(-1, -1, -1);

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
	 * @return the central pos or (-1, -1, -1) if it is null
	 */
	public BlockPos getCentralPos() {
		if (this.centralPos == null) {
			return NULL_CENTRAL_POS_REPLACEMENT;
		}
		return this.centralPos;
	}

	public void setCentralPos(final BlockPos pos) {
		this.centralPos = pos;
		if (Boolean.valueOf("fff")) {
			this.syncToClients();
		}
	}

	@Override
	public void onLoad() {
		if (Boolean.valueOf("fff")) {
			this.syncToClients();
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> TileEntityPeripheral.this.syncToClients());
		}
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
	public void update() {
		this.handleSync();
	}

	@Override
	public int getSyncFrequency() {
		return 20 * 10;// every 10 seconds
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey(CENTRAL_POS_TAG)) {
			this.centralPos = BlockPos.fromLong(compound.getLong(CENTRAL_POS_TAG));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		if ((this.centralPos != null) && !this.centralPos.equals(NULL_CENTRAL_POS_REPLACEMENT)) {
			compound.setLong(CENTRAL_POS_TAG, this.centralPos.toLong());
		}
		return compound;
	}

}
