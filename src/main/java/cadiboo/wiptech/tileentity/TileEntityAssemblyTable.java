package cadiboo.wiptech.tileentity;

import java.util.ArrayList;
import java.util.Collections;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.attachments.AttachmentList;
import cadiboo.wiptech.capability.attachments.CapabilityAttachmentList;
import cadiboo.wiptech.capability.energy.IEnergyUser;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import cadiboo.wiptech.capability.inventory.IInventoryUser;
import cadiboo.wiptech.capability.inventory.ModItemStackHandler;
import cadiboo.wiptech.event.ModEventFactory;
import cadiboo.wiptech.init.ModBlocks;
import cadiboo.wiptech.item.IItemAttachment;
import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * @author Cadiboo
 */
public class TileEntityAssemblyTable extends TileEntity implements IModTileEntity, ITickable, IEnergyUser, IInventoryUser, ITileEntitySyncable, ITileEntityCentral {

	public static final int	WIDTH	= 3;
	public static final int	HEIGHT	= 2;
	public static final int	DEPTH	= 3;

	public static final String	ASSEMBLY_TIME_TAG		= "assemblyTime";
	public static final String	MAX_ASSEMBLY_TIME_TAG	= "maxAssemblyTime";

	public static final int	ATTACHMENT_SLOTS_SIZE	= (AttachmentPoints.values().length - 1);
	public static final int	ASSEMBLY_SLOT			= (AttachmentPoints.values().length - 1) + 1;

	private static final int ASSEMBLE_ENERGY_TICK = 1000;

	private final ModEnergyStorage		energy;
	private final ModItemStackHandler	inventory;

	private int	assemblyTime;
	private int	maxAssemblyTime;

	public TileEntityAssemblyTable() {
		this.energy = new ModEnergyStorage(100000);
		this.inventory = new ModItemStackHandler(ASSEMBLY_SLOT + 1);
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

	public int getAssemblyTime() {
		return this.assemblyTime;
	}

	public int getMaxAssemblyTime() {
		return this.maxAssemblyTime;
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
	public void update() {
		if (this.world.isRemote) {
			return;
		}

		if (this.assemblyTime > 0) {
			if (this.energy.extractEnergy(ASSEMBLE_ENERGY_TICK, true) == ASSEMBLE_ENERGY_TICK) {
				this.energy.extractEnergy(ASSEMBLE_ENERGY_TICK, false);
				this.assemblyTime--;
			}
		}

		if ((this.assemblyTime == 0) && (this.maxAssemblyTime > 0)) {

			final AttachmentList attachmentList = this.inventory.getStackInSlot(ASSEMBLY_SLOT).getCapability(CapabilityAttachmentList.ATTACHMENT_LIST, null);

			/* should NEVER be null but doesnt hurt to check */
			if (attachmentList != null) {

				for (int slot = 0; slot < ATTACHMENT_SLOTS_SIZE; slot++) {

					final ItemStack add = this.inventory.getStackInSlot(slot).copy();

					if (add.isEmpty()) {
						continue;
					}

					final ItemStack stack = attachmentList.addAttachment(add);
					this.inventory.setStackInSlot(slot, stack);

				}
			}

			WIPTech.info(attachmentList);

			this.maxAssemblyTime = 0;
		}

		this.handleSync();
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
		if (compound.hasKey(ENERGY_TAG)) {
			this.getEnergy().setEnergyStored(compound.getInteger(ENERGY_TAG), false);
		}
		if (compound.hasKey(INVENTORY_TAG)) {
			this.getInventory().deserializeNBT(compound.getCompoundTag(INVENTORY_TAG));
		}
		if (compound.hasKey(ASSEMBLY_TIME_TAG)) {
			this.assemblyTime = compound.getInteger(ASSEMBLY_TIME_TAG);
		}
		if (compound.hasKey(MAX_ASSEMBLY_TIME_TAG)) {
			this.maxAssemblyTime = compound.getInteger(MAX_ASSEMBLY_TIME_TAG);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setInteger(ENERGY_TAG, this.energy.getEnergyStored());
		compound.setTag(INVENTORY_TAG, this.getInventory().serializeNBT());
		compound.setInteger(ASSEMBLY_TIME_TAG, this.assemblyTime);
		compound.setInteger(MAX_ASSEMBLY_TIME_TAG, this.maxAssemblyTime);

		return compound;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(WIDTH, HEIGHT, DEPTH);
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

	public boolean isAssembling() {
		return this.assemblyTime > 0;
	}

	public void startAssembly() {

		if (!this.canAssemble()) {
			return;
		}

		final ItemStack itemStack = this.inventory.getStackInSlot(ASSEMBLY_SLOT);
		final ArrayList<ItemStack> attachments = new ArrayList<>();
		for (int slot = 0; slot < ATTACHMENT_SLOTS_SIZE; slot++) {
			attachments.add(this.inventory.getStackInSlot(slot));
		}

		final int apiAssembleTime = Math.max(1, ModEventFactory.getItemAssembleTime(itemStack, Collections.unmodifiableList(attachments)));

		this.assemblyTime = apiAssembleTime;
		this.maxAssemblyTime = apiAssembleTime;
	}

	public boolean canAssemble() {
		if (this.energy.getEnergyStored() < ASSEMBLE_ENERGY_TICK) {
			return false;
		}

		final ItemStack itemStack = this.inventory.getStackInSlot(ASSEMBLY_SLOT);

		final AttachmentList attachmentList = itemStack.getCapability(CapabilityAttachmentList.ATTACHMENT_LIST, null);

		if (attachmentList == null) {
			return false;
		}

		final ArrayList<ItemStack> attachments = new ArrayList<>();
		for (int slot = 0; slot < ATTACHMENT_SLOTS_SIZE; slot++) {
			final ItemStack attachment = this.inventory.getStackInSlot(slot);
			if (attachment.isEmpty()) {
				continue;
			}
			if (!(attachment.getItem() instanceof IItemAttachment)) {
				return false;
			}
			attachments.add(attachment);
		}

		boolean canAddAnyAttachment = false;

		for (final ItemStack attachment : attachments) {
			if (canAddAnyAttachment) {
				continue;
			}
			final AttachmentPoints attachmentPoint = ((IItemAttachment) attachment.getItem()).getAttachmentPoint();

			if (attachmentList.canAddAttachment(attachment)) {
				canAddAnyAttachment = true;
			}

		}

		if (!canAddAnyAttachment) {
			return false;
		}

		return true;
	}

}
