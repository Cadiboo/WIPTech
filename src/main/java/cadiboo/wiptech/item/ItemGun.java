package cadiboo.wiptech.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class ItemGun extends ItemEnergy {

	protected int reloadTimeRemaining;

	public ItemGun(String name, int capacity) {
		super(name, capacity);
		this.setMaxDamage(100);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack item, NBTTagCompound nbt) {

		return new ICapabilityProvider() {
			@Override
			public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
				return capability == CapabilityEnergy.ENERGY;
			}

			@Nullable
			@Override
			public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

				return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(new EnergyStorage(getMaxEnergy(), getMaxTransfer(), getMaxTransfer(), item.getTagCompound() != null ? item.getTagCompound().getInteger("Energy") : 0) {
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
						NBTTagCompound nbt = item.getTagCompound() != null ? item.getTagCompound() : new NBTTagCompound();
						nbt.setInteger("Energy", energy);
						item.setTagCompound(nbt);
					}
				}) : null;
			}
		};

		// ICapabilityProvider provider = new ICapabilityProvider
		// return super.initCapabilities(item, nbt);
		// ICapabilityProvider modules = new ModularWeaponProvider113(item, this);
		// return modules;
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
		if (reloadTimeRemaining > 0)
			reloadTimeRemaining--;
		stack.setItemDamage(reloadTimeRemaining);
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		this.handleUsingTick(stack, player, count);
		super.onUsingTick(stack, player, count);
	}

	protected abstract void handleUsingTick(ItemStack stack, EntityLivingBase player, int count);

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		this.handleStoppedUsing(stack, worldIn, entityLiving, timeLeft);
	}

	protected abstract void handleStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft);

	@Nonnull
	protected abstract Item getDefaultAmmo();

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if (reloadTimeRemaining > 0)
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);

		boolean hasAmmo = !this.findAmmo(playerIn).isEmpty();

		if (!playerIn.isCreative() && !hasAmmo)
			return new ActionResult(EnumActionResult.FAIL, itemstack);

		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.getHeldItem(hand).getCount() == 1) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, facing)) {
				if (!world.isRemote) {
					IEnergyStorage energy = tile.getCapability(CapabilityEnergy.ENERGY, facing);
					if (energy != null && energy.canExtract()) {
						WIPTech.info(player.getHeldItem(hand).getCapability(CapabilityEnergy.ENERGY, null) != null);
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
	public NBTTagCompound getNBTShareTag(ItemStack stack) {
		NBTTagCompound nbt = super.getNBTShareTag(stack);
		if (stack.getCapability(CapabilityEnergy.ENERGY, null) != null)
			nbt.setInteger("Energy", stack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored());
		return nbt;
	}

}
