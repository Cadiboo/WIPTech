package cadiboo.wiptech.item;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile2;
import cadiboo.wiptech.entity.projectile.EntityProjectileBase2;
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

public class ItemTestLauncher extends ItemBase
{
	public ItemTestLauncher(String name)
	{
		super(name);
		this.maxStackSize = 1;
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.COMBAT);
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase player, int timeLeft) {
		if (player instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)player;
			boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
			flag = true;
			ItemStack itemstack = ItemStack.EMPTY;

			int i = this.getMaxItemUseDuration(stack) - timeLeft;

			if (i <= 0) return;

			if (!itemstack.isEmpty() || flag)
			{
				if (itemstack.isEmpty())
				{
					itemstack = new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, 5); //TUNGSTEN MEDIUM
				}

				float velocity = EntityFerromagneticProjectile2.getProjectileVelocity(stack);

				if ((double)velocity >= 0.1D)
				{
					boolean flag1 = entityplayer.capabilities.isCreativeMode/* || (itemstack.getItem() instanceof ItemMagneticMetalRod && ((ItemMagneticMetalRod) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer))*/;

					if (!worldIn.isRemote)
					{
						EntityFerromagneticProjectile2 projectile = new EntityFerromagneticProjectile2(worldIn, entityplayer);
						projectile.setAmmoId(stack.getMetadata());
						WIPTech.logger.info(projectile);
						WIPTech.logger.info(projectile.getAmmoId());
						
						projectile.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, velocity, 0.0F);

						projectile.setDamage(EntityFerromagneticProjectile2.getProjectileDamage(itemstack));

						projectile.setKnockbackStrength(EntityFerromagneticProjectile2.getProjectileKnockback(itemstack));
						//if(this.railgun.overheat) entityarrow.setFire(100);

						if (flag1 || entityplayer.capabilities.isCreativeMode)
						{
							projectile.pickupStatus = EntityProjectileBase2.PickupStatus.CREATIVE_ONLY;
						}

						worldIn.spawnEntity(projectile);
					}

					worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_FIREWORK_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

					if (!flag1 && !entityplayer.capabilities.isCreativeMode)
					{
						itemstack.shrink(1);

						if (itemstack.isEmpty())
						{
							entityplayer.inventory.deleteStack(itemstack);
						}
					}
				}
			}
		}
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}

	public int getItemEnchantability()
	{
		return 0;
	}
}
