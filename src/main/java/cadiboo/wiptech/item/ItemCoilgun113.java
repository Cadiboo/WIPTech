package cadiboo.wiptech.item;

import javax.annotation.Nonnull;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.IWeaponModular;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile113;
import cadiboo.wiptech.handler.EnumHandler.ParamagneticProjectileSizes;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.init.Capabilities;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemCoilgun113 extends ItemGun {

	private static final int	BURST_TIMER		= 2;
	private static final int	SHOOT_ENERGY	= 1000;
	private static final int	ENERGY_CAPACITY	= 10000;
	private int					shotsTaken;
	private int					burstShotsTaken;

	public ItemCoilgun113(String name) {
		super(name);
		shotsTaken = 0;
		burstShotsTaken = 0;
	}

	@Override
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

		entity.motionX += MathHelper.sin(player.rotationYaw * 0.017453292F) * MathHelper.cos(player.rotationPitch * 0.017453292F) * modules.getCoil().getEfficiencyFraction() / 25;
		entity.motionY += MathHelper.sin(player.rotationPitch * 0.017453292F) * modules.getCoil().getEfficiencyFraction() / 25;
		entity.motionZ += -MathHelper.cos(player.rotationYaw * 0.017453292F) * MathHelper.cos(player.rotationPitch * 0.017453292F) * modules.getCoil().getEfficiencyFraction() / 25;

		entity.getEntityWorld().playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

		if (useAmmo) {
			ammo.shrink(1);
			energy.extractEnergy(SHOOT_ENERGY, false);
		}

		shotsTaken++;

	}

	@Override
	protected void handleUsingTick(ItemStack stack, EntityLivingBase entity, int count) {
		IWeaponModular modules = stack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);
		if (!checkModules(modules))
			return;
		switch (modules.getCircuit()) {
			case AUTO:
				handleShoot(stack, entity);
				break;
			case BURST3:
			case BURST5:
			case BURST10:
				handleBurst(modules.getCircuit(), stack, entity);
				break;
			case OVERCLOCKED:
				for (int i = 0; i < 3; i++)
					handleShoot(stack, entity);
				break;
			case MANUAL:
			default:
				return;

		}
	}

	private boolean checkModules(IWeaponModular modules) {
		if (modules == null)
			return false;
		if (modules.getCircuit() == null)
			return false;
		if (modules.getCoil() == null)
			return false;
		if (modules.getCapacitor() == null)
			return false;
		return true;
	}

	private void handleBurst(Circuits circuit, ItemStack stack, EntityLivingBase entity) {
		if ((burstShotsTaken < circuit.getBurstShots()) && Utils.randomBetween(0, 100) < 70 && !entity.getEntityWorld().isRemote) {
			WIPTech.info(shotsTaken);
			burstShotsTaken++;
			handleShoot(stack, entity);
		}
	}

	@Override
	protected void handleStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft) {

		IWeaponModular modules = stack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);
		if (!checkModules(modules))
			return;
		if (modules.getCircuit() == Circuits.MANUAL)
			handleShoot(stack, entity);

		this.burstShotsTaken = 0;

		if (entity instanceof EntityPlayer)
			((EntityPlayer) entity).addStat(StatList.getObjectUseStats(this));
	}

	@Override
	@Nonnull
	protected Item getDefaultAmmo() {
		return Items.TUNGSTEN_MEDIUM;
	}

	@Override
	protected boolean isAmmo(ItemStack stack) {
		return stack.getItem() instanceof ItemParamagneticProjectile113 && ((ItemParamagneticProjectile113) stack.getItem()).getType().getSize() == ParamagneticProjectileSizes.MEDIUM;
	}

	@Override
	public boolean canShoot(ItemStack stack) {
		return stack.getCapability(CapabilityEnergy.ENERGY, null) != null && stack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored() >= SHOOT_ENERGY && checkModules(stack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null));
	}

}
