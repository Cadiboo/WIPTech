package cadiboo.wiptech.item;

import cadiboo.wiptech.init.Items;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemGun extends ItemBase {

	private int reloadTimeRemaining;

	public ItemGun(String name) {
		super(name);
		this.maxStackSize = 1;
		this.setMaxDamage(100);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	private ItemStack findAmmo(EntityPlayer player) {
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
		return stack.getItem() instanceof ItemParamagneticProjectile113;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (reloadTimeRemaining > 0)
			reloadTimeRemaining--;
		stack.setItemDamage(reloadTimeRemaining);
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		this.onPlayerStoppedUsing(stack, player.getEntityWorld(), player, 10);
		super.onUsingTick(stack, player, count);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

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

		worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F,
				1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

		if (useAmmo) {
			ammo.shrink(1);
		}

		// reloadTimeRemaining = 20;

		player.addStat(StatList.getObjectUseStats(this));
	}

	private ItemParamagneticProjectile113 getDefaultAmmo() {
		return Items.TUNGSTEN_LARGE;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		// if (reloadTimeRemaining > 0)
		// return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);

		boolean hasAmmo = !this.findAmmo(playerIn).isEmpty();

		if (!playerIn.isCreative() && !hasAmmo)
			return new ActionResult(EnumActionResult.FAIL, itemstack);

		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

}
