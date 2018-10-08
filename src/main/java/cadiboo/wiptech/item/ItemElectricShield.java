package cadiboo.wiptech.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.capability.attachments.AttachmentList;
import cadiboo.wiptech.capability.attachments.CapabilityAttachmentList;
import cadiboo.wiptech.capability.attachments.IAttachmentUser;
import cadiboo.wiptech.capability.energy.IEnergyUser;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import cadiboo.wiptech.util.ModEnums.AttachmentPoint;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemElectricShield extends Item {

	public static final AttachmentPoint[] ATTACHMENT_POINTS = new AttachmentPoint[] {

			AttachmentPoint.CIRCUIT,

			AttachmentPoint.COIL,

			AttachmentPoint.SCOPE,

			AttachmentPoint.SIDE_LEFT,

			AttachmentPoint.SIDE_RIGHT,

			AttachmentPoint.UNDER

	};

	public ItemElectricShield(final String name) {
		ModUtil.setRegistryNames(this, name);
	}

	@Override
	public boolean isShield(final ItemStack stack, final EntityLivingBase entity) {

		final IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);

		if (energy == null) {
			return false;
		}

		return energy.getEnergyStored() > this.getBlockEnergyCost();
	}

	public int getBlockEnergyCost() {
		return 100;
	}

	@Override
	public final ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final NBTTagCompound nbt) {
		return new CapabilityProvider(stack, nbt);
	}

	public class CapabilityProvider implements ICapabilitySerializable<NBTTagCompound>, IEnergyUser, IAttachmentUser {

		final ItemStack			itemStack;
		final ModEnergyStorage	energy		= new ModEnergyStorage(1000);
		final AttachmentList	attachments	= new AttachmentList(ATTACHMENT_POINTS);

		public CapabilityProvider(final ItemStack stack, final NBTTagCompound nbt) {
			this.itemStack = stack;
		}

		@Override
		public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing facing) {
			return this.getCapability(capability, facing) != null;
		}

		@Nullable
		@Override
		public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
			if (capability == CapabilityEnergy.ENERGY) {
				return (T) this.getEnergy();
			}

			if (capability == CapabilityAttachmentList.ATTACHMENT_LIST) {
				return (T) this.getAttachments();
			}

			return null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			final NBTTagCompound compound = new NBTTagCompound();
			compound.merge(IEnergyUser.super.serializeNBT());
			compound.merge(IAttachmentUser.super.serializeNBT());
			return compound;
		}

		@Override
		public void deserializeNBT(final NBTTagCompound compound) {
			IEnergyUser.super.deserializeNBT(compound);
			IAttachmentUser.super.deserializeNBT(compound);
		}

		@Override
		public AttachmentList getAttachments() {
			return this.attachments;
		}

		@Override
		public ModEnergyStorage getEnergy() {
			return this.energy;
		}

	}

}
