package cadiboo.wiptech.item;

import javax.annotation.Nonnull;

import cadiboo.wiptech.handler.EnumHandler.ParamagneticProjectileSizes;
import cadiboo.wiptech.init.Items;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemRailgun113 extends ItemGun {

	public ItemRailgun113(String name) {
		super(name);
	}

	@Override
	protected boolean isAmmo(ItemStack stack) {
		return stack.getItem() instanceof ItemParamagneticProjectile113 && ((ItemParamagneticProjectile113) stack.getItem()).getType().getSize() == ParamagneticProjectileSizes.MEDIUM;
	}

	@Override
	protected void handleUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (!(entityLiving instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) entityLiving;

		boolean useAmmo = !player.capabilities.isCreativeMode;

		// if !shoots plasma

		ItemStack ammo = this.findAmmo(player);

		if (ammo.isEmpty() && !useAmmo)
			ammo = new ItemStack(this.getDefaultAmmo());

		float velocity = ((ItemParamagneticProjectile113) ammo.getItem()).getVelocity();

		if (velocity < 0.1D)
			return;

		if (!worldIn.isRemote) {
			ItemParamagneticProjectile113 item = (ItemParamagneticProjectile113) ammo.getItem();
			EntityArrow projectile = item.createProjectile(worldIn, stack, player);
			projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity, 0.0F);

			if (player.isCreative()) {
				projectile.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
			}

			worldIn.spawnEntity(projectile);
		}

		worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

		if (useAmmo) {
			ammo.shrink(1);
		}

		// reloadTimeRemaining = 20;

		player.addStat(StatList.getObjectUseStats(this));
	}

	@Override
	@Nonnull
	protected Item getDefaultAmmo() {
		return Items.TUNGSTEN_MEDIUM;
	}

}
