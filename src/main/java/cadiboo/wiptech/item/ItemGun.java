package cadiboo.wiptech.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.capability.WeaponModular;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Capacitors;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Coils;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Rails;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Scopes;
import cadiboo.wiptech.init.Capabilities;
import cadiboo.wiptech.util.Utils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class ItemGun extends ItemBase {

	public ItemGun(String name) {
		super(name);
		this.setMaxStackSize(1);
		this.setMaxDamage(100);
	}

	@Override
	public final ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new ICapabilityProvider() {
			@Override
			public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
				return capability == CapabilityEnergy.ENERGY || capability == Capabilities.MODULAR_WEAPON_CAPABILITY;
			}

			@Nullable
			@Override
			public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
				if (capability == CapabilityEnergy.ENERGY) {
					// int capacity = stack.getTagCompound() != null &&
					// stack.getTagCompound().hasKey("capacitor") ?
					// Capacitors.byID(stack.getTagCompound().getInteger("capacitor")).getStorage()
					// : 0;
					int capacity = stack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null) != null && stack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null).getCapacitor() != null
							? stack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null).getCapacitor().getStorage()
							: 0;
					return CapabilityEnergy.ENERGY.cast(new EnergyStorage(capacity, capacity, capacity, stack.getTagCompound() != null ? stack.getTagCompound().getInteger("Energy") : 0) {

						@Override
						public int receiveEnergy(int maxReceive, boolean simulate) {
							int i = super.receiveEnergy(maxReceive, simulate);
							setEnergy(energy);
							return i;
						}

						@Override
						public int extractEnergy(int maxExtract, boolean simulate) {
							int i = super.extractEnergy(maxExtract, simulate);
							setEnergy(energy);
							return i;
						}

						private void setEnergy(int energy) {
							NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
							nbt.setInteger("Energy", energy);
							stack.setItemDamage(100 - Utils.getEnergyPercentage(energy, this.capacity));
							stack.setTagCompound(nbt);
						}

					});
				} else if (capability == Capabilities.MODULAR_WEAPON_CAPABILITY) {
					return Capabilities.MODULAR_WEAPON_CAPABILITY.cast(new WeaponModular(stack.getTagCompound()) {
						@Override
						public WeaponModular setRail(Rails rail) {
							setNBT();
							return super.setRail(rail);
						}

						@Override
						public WeaponModular setCapacitor(Capacitors capacitor) {
							setNBT();
							return super.setCapacitor(capacitor);
						}

						@Override
						public WeaponModular setCircuit(Circuits circuit) {
							setNBT();
							return super.setCircuit(circuit);
						}

						@Override
						public WeaponModular setCoil(Coils coil) {
							setNBT();
							return super.setCoil(coil);
						}

						@Override
						public WeaponModular setScope(Scopes scope) {
							setNBT();
							return super.setScope(scope);
						}

						private void setNBT() {
							NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
							nbt.merge(this.serializeNBT());
							stack.setTagCompound(nbt);
						}
					});
				}
				return null;
			}
		};
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	protected ItemStack findAmmo(EntityPlayer player) {
		if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND)))
			return player.getHeldItem(EnumHand.OFF_HAND);
		else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND)))
			return player.getHeldItem(EnumHand.MAIN_HAND);
		else {
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				ItemStack itemstack = player.inventory.getStackInSlot(i);

				if (this.isAmmo(itemstack)) {
					return itemstack;
				}
			}
			return ItemStack.EMPTY;
		}
	}

	protected abstract void handleShoot(ItemStack stack, EntityLivingBase entity);

	protected abstract boolean isAmmo(ItemStack stack);

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if (!worldIn.isRemote)
			if (worldIn.getTotalWorldTime() % 10 == 0 && entityIn instanceof EntityPlayerMP)
				((EntityPlayer) entityIn).inventoryContainer.detectAndSendChanges();
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		super.onUsingTick(stack, player, count);
		if (!this.canShoot(stack))
			player.stopActiveHand();
		this.handleUsingTick(stack, player, count);
	}

	protected abstract void handleUsingTick(ItemStack stack, EntityLivingBase player, int count);

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
		this.handleStoppedUsing(stack, worldIn, entityLiving, timeLeft);
	}

	protected abstract void handleStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft);

	@Nonnull
	protected abstract Item getDefaultAmmo();

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		boolean hasAmmo = !this.findAmmo(playerIn).isEmpty();

		if (!playerIn.isCreative() && !hasAmmo && !canShoot(itemstack))
			return new ActionResult(EnumActionResult.FAIL, itemstack);

		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

	public abstract boolean canShoot(ItemStack stack);

	@Nonnull
	@Override
	public final EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.getHeldItem(hand).getCount() == 1) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, facing)) {
				if (!world.isRemote) {
					IEnergyStorage energy = tile.getCapability(CapabilityEnergy.ENERGY, facing);
					if (energy != null && energy.canExtract()) {
						IEnergyStorage storage = player.getHeldItem(hand).getCapability(CapabilityEnergy.ENERGY, null);
						if (storage != null && storage.canReceive()) {
							storage.receiveEnergy(energy.extractEnergy(storage.receiveEnergy(Integer.MAX_VALUE, true), false), false);
						}
					}
				}
				return EnumActionResult.SUCCESS;
			}
		}
		return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		// TODO Auto-generated method stub
		return super.getDurabilityForDisplay(stack);
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		// TODO Auto-generated method stub
		return super.getRGBDurabilityForDisplay(stack);
	}

	@Override
	public final void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		super.getSubItems(tab, items);
		if (this.isInCreativeTab(tab)) {
			ItemStack full = new ItemStack(this);
			if (full.hasCapability(CapabilityEnergy.ENERGY, null)) {
				IEnergyStorage energy = full.getCapability(CapabilityEnergy.ENERGY, null);
				if (energy != null) {
					this.setEnergy(full, energy.getMaxEnergyStored());
					items.add(full);
				}
			}

			ItemStack empty = new ItemStack(this);
			this.setEnergy(empty, 0);
			items.add(empty);
		}
	}

	private void setEnergy(ItemStack stack, int energy) {
		NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
		nbt.setInteger("Energy", energy);
	}

	@Override
	public NBTTagCompound getNBTShareTag(ItemStack stack) {
		// TODO Auto-generated method stub
		// NBTTagCompound nbt = super.getNBTShareTag(stack);
		// nbt.merge(((WeaponModular)
		// stack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY,
		// null)).serializeNBT());
		// return nbt;
		return super.getNBTShareTag(stack);
	}

}
