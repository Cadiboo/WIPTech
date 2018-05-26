package cadiboo.wiptech.item;

import javax.annotation.Nonnull;

import cadiboo.wiptech.entity.projectile.EntityMissile;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile113;
import cadiboo.wiptech.handler.EnumHandler.ParamagneticProjectileSizes;
import cadiboo.wiptech.init.Items;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemMissileLauncher extends ItemGun {

	public ItemMissileLauncher(String name) {
		super(name);
	}

	@Override
	protected void handleShoot(ItemStack stack, EntityLivingBase entity) {
		if (!(entity instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) entity;

		boolean useAmmo = !player.capabilities.isCreativeMode;

		ItemStack ammo = this.findAmmo(player);

		if (ammo.isEmpty())
			if (!useAmmo)
				ammo = new ItemStack(this.getDefaultAmmo());
			else
				return;

		float velocity = 3;// (float) ((ItemParamagneticProjectile113)
							// ammo.getItem()).getType().getVelocity();

		if (velocity < 0.1D)
			return;

		if (!entity.getEntityWorld().isRemote) {
			EntityMissile projectile = new EntityMissile(player.getEntityWorld(), player);
			projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity, 0.0F);

			if (player.isCreative()) {
				projectile.pickupStatus = EntityParamagneticProjectile113.PickupStatus.CREATIVE_ONLY;
			}

			entity.getEntityWorld().spawnEntity(projectile);
		}

		entity.getEntityWorld().playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

		if (useAmmo) {
			ammo.shrink(1);
		}

	}

	@Override
	protected void handleUsingTick(ItemStack stack, EntityLivingBase entity, int count) {
		handleShoot(stack, entity);
	}

	@Override
	protected void handleStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft) {

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
		return true;
	}

}
