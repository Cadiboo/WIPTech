package cadiboo.wiptech.item;

import javax.annotation.Nullable;

import cadiboo.wiptech.util.CustomEnergyStorage;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemEnergy extends ItemBase {

	private final int maxEnergy;

	public int getMaxEnergy() {
		return maxEnergy;
	}

	private final int transfer;

	public int getMaxTransfer() {
		return transfer;
	}

	public ItemEnergy(String name, int maxEnergy, int transfer) {
		super(name);
		this.maxEnergy = maxEnergy;
		this.transfer = transfer;

		// this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

	public ItemEnergy(String name, int maxEnergy) {
		this(name, maxEnergy, maxEnergy);
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return false; // return this.isOverpowered
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		super.getSubItems(tab, items);
		if (this.isInCreativeTab(tab)) {
			ItemStack stack = new ItemStack(this);
			if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
				IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
				if (energy != null) {
					this.setEnergy(stack, energy.getMaxEnergyStored());
					items.add(stack);
				}
			}

			ItemStack stack2 = new ItemStack(this);
			this.setEnergy(stack2, 0);
			items.add(stack2);
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack itemStack) {
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (energy != null) {
				double maxAmount = energy.getMaxEnergyStored();
				double energyDif = maxAmount - energy.getEnergyStored();
				return energyDif / maxAmount;
			}
		}
		return super.getDurabilityForDisplay(stack);
	}

	public void setEnergy(ItemStack stack, int energyRecieved) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (energy instanceof CustomEnergyStorage) {
				((CustomEnergyStorage) energy).setEnergyStored(energyRecieved);
			}
		}
	}

	public int receiveEnergyInternal(ItemStack stack, int maxReceive, boolean simulate) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (energy instanceof CustomEnergyStorage) {
				((CustomEnergyStorage) energy).receiveEnergyInternal(maxReceive, simulate);
			}
		}
		return 0;
	}

	public int extractEnergyInternal(ItemStack stack, int maxExtract, boolean simulate) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (energy instanceof CustomEnergyStorage) {
				((CustomEnergyStorage) energy).extractEnergyInternal(maxExtract, simulate);
			}
		}
		return 0;
	}

	public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (energy != null) {
				return energy.receiveEnergy(maxReceive, simulate);
			}
		}
		return 0;
	}

	public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (energy != null) {
				return energy.extractEnergy(maxExtract, simulate);
			}
		}
		return 0;
	}

	public int getEnergyStored(ItemStack stack) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (energy != null) {
				return energy.getEnergyStored();
			}
		}
		return 0;
	}

	public int getMaxEnergyStored(ItemStack stack) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
			if (energy != null) {
				return energy.getMaxEnergyStored();
			}
		}
		return 0;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new EnergyCapabilityProvider(stack, this);
	}

	private static class EnergyCapabilityProvider implements ICapabilityProvider {

		public final CustomEnergyStorage energy;

		public EnergyCapabilityProvider(final ItemStack stack, ItemEnergy item) {
			this.energy = new CustomEnergyStorage(item.getMaxEnergy(), item.transfer, item.transfer) {
				@Override
				public int getEnergyStored() {
					if (stack.hasTagCompound()) {
						return stack.getTagCompound().getInteger("Energy");
					} else {
						return 0;
					}
				}

				@Override
				public void setEnergyStored(int energy) {
					if (!stack.hasTagCompound()) {
						stack.setTagCompound(new NBTTagCompound());
					}

					stack.getTagCompound().setInteger("Energy", energy);
				}
			};
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return this.getCapability(capability, facing) != null;
		}

		@Nullable
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability == CapabilityEnergy.ENERGY) {
				return CapabilityEnergy.ENERGY.cast(this.energy);
			}
			return null;
		}
	}

}
