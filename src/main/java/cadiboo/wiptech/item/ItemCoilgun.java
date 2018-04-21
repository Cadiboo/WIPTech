package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile;
import cadiboo.wiptech.entity.projectile.EntityProjectileBase;
import cadiboo.wiptech.init.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemCoilgun extends ItemBase {

	public ItemCoilgun(String name) {
		super(name);
		this.maxStackSize = 1;
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.COMBAT);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		/*
		 * if(this.getComponents.contains(rollingCircuit||machinegunCircuit) {
		 */
		if (player instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) player;
			World worldIn = entityplayer.getEntityWorld();
			boolean flag = entityplayer.capabilities.isCreativeMode
					|| EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
			flag = true;
			ItemStack itemstack = ItemStack.EMPTY;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.PARAMAGNETIC_PROJECILE, 1, 8); // TUNGSTEN SMALL
				}

				float velocity = EntityParamagneticProjectile.getProjectileVelocity(stack);

				if (velocity >= 0.1D) {
					boolean flag1 = entityplayer.capabilities.isCreativeMode/*
																			 * || (itemstack.getItem() instanceof
																			 * ItemMagneticMetalRod &&
																			 * ((ItemMagneticMetalRod)
																			 * itemstack.getItem()).isInfinite(
																			 * itemstack, stack, entityplayer))
																			 */;

					if (!worldIn.isRemote) {
						ItemParamagneticProjectile itemprojectile = (ItemParamagneticProjectile) (itemstack
								.getItem() instanceof ItemParamagneticProjectile ? itemstack.getItem()
										: Items.PARAMAGNETIC_PROJECILE);
						EntityParamagneticProjectile projectile = itemprojectile.createProjectile(worldIn, itemstack,
								entityplayer, false);
						projectile.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F,
								velocity, 0.1F);

						// if(this.coilGold) damage & knockback * 1.5

						// if(this.coilgun.overheat) entityarrow.setFire(100);

						if (flag1 || entityplayer.capabilities.isCreativeMode) {
							projectile.pickupStatus = EntityProjectileBase.PickupStatus.CREATIVE_ONLY;
						}

						worldIn.spawnEntity(projectile);
					}

					worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ,
							SoundEvents.ENTITY_FIREWORK_SHOOT, SoundCategory.PLAYERS, 1.0F,
							1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

					if (!flag1 && !entityplayer.capabilities.isCreativeMode) {
						itemstack.shrink(1);

						if (itemstack.isEmpty()) {
							entityplayer.inventory.deleteStack(itemstack);
						}
					}
				}
			}
		}
	}

	protected boolean isAmmo(ItemStack stack) {
		return stack.getItem() instanceof ItemParamagneticProjectile && 6 >= stack.getMetadata()
				&& stack.getMetadata() <= 8;
	}

	private ItemStack findAmmo(EntityPlayer player) {
		if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND))) {
			return player.getHeldItem(EnumHand.OFF_HAND);
		} else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND))) {
			return player.getHeldItem(EnumHand.MAIN_HAND);
		} else {
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				ItemStack itemstack = player.inventory.getStackInSlot(i);

				if (this.isAmmo(itemstack)) {
					return itemstack;
				}
			}

			return ItemStack.EMPTY;
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		// this.burstCounter.reset()
		/*
		 * if(!this.getComponents.contains(rollingCircuit||machinegunCircuit) {
		 */
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			boolean flag = entityplayer.capabilities.isCreativeMode
					|| EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
			ItemStack itemstack = this.findAmmo(entityplayer);

			int i = this.getMaxItemUseDuration(stack) - timeLeft;

			if (i <= 0)
				return;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.PARAMAGNETIC_PROJECILE, 1, 8); // TUNGSTEN SMALL
				}

				float velocity = EntityParamagneticProjectile.getProjectileVelocity(stack);
				// TODO make it *by coil

				if (velocity >= 0.1D) {
					boolean flag1 = entityplayer.capabilities.isCreativeMode
					/*
					 * || (itemstack.getItem() instanceof ItemMagneticMetalRod &&
					 * ((ItemMagneticMetalRod) itemstack.getItem()).isInfinite( itemstack, stack,
					 * entityplayer))
					 */;

					if (!worldIn.isRemote) {
						ItemParamagneticProjectile itemprojectile = (ItemParamagneticProjectile) (itemstack
								.getItem() instanceof ItemParamagneticProjectile ? itemstack.getItem()
										: Items.PARAMAGNETIC_PROJECILE);
						EntityParamagneticProjectile projectile = itemprojectile.createProjectile(worldIn, itemstack,
								entityplayer, false);
						projectile.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F,
								velocity, 0.1F);

						// if(this.coilGold) damage & knockback * 1.5

						// if(this.coilgun.overheat) entityarrow.setFire(100);

						if (flag1 || entityplayer.capabilities.isCreativeMode) {
							projectile.pickupStatus = EntityProjectileBase.PickupStatus.CREATIVE_ONLY;
						}

						worldIn.spawnEntity(projectile);
					}

					worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ,
							SoundEvents.ENTITY_FIREWORK_SHOOT, SoundCategory.PLAYERS, 1.0F,
							1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

					if (!flag1 && !entityplayer.capabilities.isCreativeMode) {
						itemstack.shrink(1);

						if (itemstack.isEmpty()) {
							entityplayer.inventory.deleteStack(itemstack);
						}
					}
				}
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}

	@Override
	public int getItemEnchantability() {
		return 0;
	}
}
