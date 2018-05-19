package cadiboo.wiptech.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.IWeaponModular;
import cadiboo.wiptech.capability.WeaponModular;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile113;
import cadiboo.wiptech.handler.EnumHandler.ParamagneticProjectileSizes;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Coils;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Rails;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Scopes;
import cadiboo.wiptech.init.Capabilities;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemStandaloneGun extends ItemBase {

	private int	SHOOT_ENERGY	= 100;
	private int	CAPACITY		= 10000;

	public ItemStandaloneGun(String name) {
		super(name);
		this.setMaxDamage(100);
		this.setMaxStackSize(1);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new ICapabilityProvider() {
			@Override
			public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
				return capability == CapabilityEnergy.ENERGY || capability == Capabilities.MODULAR_WEAPON_CAPABILITY;
			}

			@Nullable
			@Override
			public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
				int maxTransfer = Math.round(CAPACITY / 50.0F);

				// WIPTech.info(stack.getTagCompound() == null ? "" :
				// stack.getTagCompound().getInteger("Energy"));

				return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(new EnergyStorage(CAPACITY, maxTransfer, maxTransfer, stack.getTagCompound() != null ? stack.getTagCompound().getInteger("Energy") : 0) {
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
						stack.setItemDamage(100 - Utils.getEnergyPercentage(stack.getCapability(CapabilityEnergy.ENERGY, null)));
						stack.setTagCompound(nbt);
					}
				}) : capability == Capabilities.MODULAR_WEAPON_CAPABILITY ? Capabilities.MODULAR_WEAPON_CAPABILITY.cast(new WeaponModular(nbt)) : null;
			}
		};
	}

	@Override
	public boolean updateItemStackNBT(NBTTagCompound nbt) {
		return super.updateItemStackNBT(nbt);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote) {
			if (worldIn.getTotalWorldTime() % 10 == 0 && entityIn instanceof EntityPlayerMP)
				if (itemSlot < 9)
					((EntityPlayer) entityIn).inventoryContainer.detectAndSendChanges(); // currently doesnt work if your holding it in your offHand, TODO use reflection
																							// to do stuff with detectAndSendChanges > this.listeners
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	protected void handleShoot(ItemStack stack, EntityLivingBase entity) {

		IWeaponModular modules = stack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);
		if (!checkModules(modules))
			return;

		IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (energy == null)
			return;

		if (!(entity instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) entity;

		boolean useAmmo = !player.capabilities.isCreativeMode;

		// WIPTech.info(energy, energy.getEnergyStored(),
		// !entity.getEntityWorld().isRemote);

		if (energy.extractEnergy(SHOOT_ENERGY, true) != SHOOT_ENERGY && useAmmo)
			return;

		ItemStack ammo = this.findAmmo(player);

		if (ammo.isEmpty())
			if (!useAmmo)
				ammo = new ItemStack(this.getDefaultAmmo());
			else
				return;

		float velocity = (float) ((ItemParamagneticProjectile113) ammo.getItem()).getType().getVelocity();

		if (velocity < 0.1D)
			return;

		if (!entity.getEntityWorld().isRemote) {
			ItemParamagneticProjectile113 item = (ItemParamagneticProjectile113) ammo.getItem();
			EntityParamagneticProjectile113 projectile = item.createProjectile(entity.getEntityWorld(), stack, player);
			projectile.setDamage(item.getType().getDamage());
			projectile.setKnockbackStrength(item.getType().getKnockback());
			projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity, 0.0F);

			if (player.isCreative()) {
				projectile.pickupStatus = EntityParamagneticProjectile113.PickupStatus.CREATIVE_ONLY;
			}

			entity.getEntityWorld().spawnEntity(projectile);
		}

		entity.motionX += MathHelper.sin(player.rotationYaw * 0.017453292F) * MathHelper.cos(player.rotationPitch * 0.017453292F) * modules.getRail().getEfficiencyFraction() / 25;
		entity.motionY += MathHelper.sin(player.rotationPitch * 0.017453292F) * modules.getRail().getEfficiencyFraction() / 25;
		entity.motionZ += -MathHelper.cos(player.rotationYaw * 0.017453292F) * MathHelper.cos(player.rotationPitch * 0.017453292F) * modules.getRail().getEfficiencyFraction() / 25;

		entity.getEntityWorld().playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

		if (useAmmo) {
			ammo.shrink(1);
			energy.extractEnergy(SHOOT_ENERGY, false);
		}

		// shotsTaken++;

	}

	@Nonnull
	protected Item getDefaultAmmo() {
		return Items.TUNGSTEN_SMALL;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase entity, int count) {
		// IWeaponModular modules =
		// stack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);
		// if (!checkModules(modules))
		// return;
		// switch (modules.getCircuit()) {
		// case AUTO:
		// handleShoot(stack, entity);
		// break;
		// case BURST3:
		// case BURST5:
		// case BURST10:
		// handleBurst(modules.getCircuit(), stack, entity);
		// break;
		// case OVERCLOCKED:
		// for (int i = 0; i < 3; i++)
		handleShoot(stack, entity);
		// break;
		// case MANUAL:
		// default:
		// return;
		//
		// }
		super.onUsingTick(stack, entity, count);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft) {
		IWeaponModular modules = stack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);
		// if (!checkModules(modules))
		// return;
		// if (modules.getCircuit() == Circuits.MANUAL)
		handleShoot(stack, entity);

		// this.burstShotsTaken = 0;

		if (entity instanceof EntityPlayer)
			((EntityPlayer) entity).addStat(StatList.getObjectUseStats(this));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		boolean hasAmmo = !this.findAmmo(playerIn).isEmpty();

		if (!playerIn.isCreative() && !hasAmmo)
			return new ActionResult(EnumActionResult.FAIL, itemstack);

		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

	@Nonnull
	@Override
	public final EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
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

	protected boolean isAmmo(ItemStack stack) {
		return stack.getItem() instanceof ItemParamagneticProjectile113 && ((ItemParamagneticProjectile113) stack.getItem()).getType().getSize() == ParamagneticProjectileSizes.SMALL;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	private boolean checkModules(IWeaponModular modules) {
		if (modules == null)
			// return false;
			modules = new WeaponModular();
		modules.setCircuit(Circuits.AUTO);
		modules.setCoil(Coils.SILVER);
		modules.setRail(Rails.SILVER);
		modules.setScope(Scopes.ZOOM);
		if (modules.getCircuit() == null)
			return false;
		if (modules.getCoil() == null)
			return false;
		if (modules.getRail() == null)
			return false;
		if (modules.getScope() == null)
			return false;
		return true;
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
}
