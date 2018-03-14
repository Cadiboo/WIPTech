package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.entity.projectile.EntityRailgunProjectile;
import cadiboo.wiptech.item.EnumHandler.MagneticMetalRods;

import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import cadiboo.wiptech.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemRailgun
extends Item
{
	public ItemRailgun(String name)
	{
		setRegistryName("wiptech", name);
		setUnlocalizedName(name);
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

	private ItemStack findAmmo(EntityPlayer player)
	{
		if (this.isRod(player.getHeldItem(EnumHand.OFF_HAND)))
		{
			return player.getHeldItem(EnumHand.OFF_HAND);
		}
		else if (this.isRod(player.getHeldItem(EnumHand.MAIN_HAND)))
		{
			return player.getHeldItem(EnumHand.MAIN_HAND);
		}
		else
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
			{
				ItemStack itemstack = player.inventory.getStackInSlot(i);

				if (this.isRod(itemstack))
				{
					return itemstack;
				}
			}

			return ItemStack.EMPTY;
		}
	}

	protected boolean isRod(ItemStack stack)
	{
		return stack.getItem() instanceof ItemMagneticMetalRod;
	}

	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)entityLiving;
			boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
			ItemStack itemstack = this.findAmmo(entityplayer);

			int i = this.getMaxItemUseDuration(stack) - timeLeft;

			if (i <= 0) return;

			if (!itemstack.isEmpty() || flag)
			{
				if (itemstack.isEmpty())
				{
					itemstack = new ItemStack(Items.MAGNETIC_METAL_ROD, 1, 2); //TUNGSTEN
				}

				float velocity = getRailgunProjectileVelocity(stack);

				if ((double)velocity >= 0.1D)
				{
					boolean flag1 = entityplayer.capabilities.isCreativeMode/* || (itemstack.getItem() instanceof ItemMagneticMetalRod && ((ItemMagneticMetalRod) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer))*/;

					if (!worldIn.isRemote)
					{
						ItemMagneticMetalRod itemrod = (ItemMagneticMetalRod)(itemstack.getItem() instanceof ItemMagneticMetalRod ? itemstack.getItem() : Items.MAGNETIC_METAL_ROD);

						//EntityRailgunProjectile railgunProjectile = new EntityRailgunProjectile(worldIn, entityplayer);
						EntityRailgunProjectile railgunProjectile = itemrod.createRailgunProjectile(worldIn, itemstack, entityplayer);
						railgunProjectile.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, velocity, 0.0F);

						railgunProjectile.setDamage(getRailgunProjectileDamage(itemstack));

						railgunProjectile.setKnockbackStrength(getRailgunProjectileKnockback(itemstack));
						//entityarrow.setFire(100);

						if (flag1 || entityplayer.capabilities.isCreativeMode)
						{
							railgunProjectile.pickupStatus = EntityRailgunProjectile.PickupStatus.CREATIVE_ONLY;
						}

						worldIn.spawnEntity(railgunProjectile);
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



	private float getRailgunProjectileVelocity(ItemStack stack) {
		float velocity = 0;
		switch(stack.getMetadata()) {
		case 0:
			velocity = 3.5F;break;
		case 1:
			velocity = 3F; break;
		default:
			velocity = 3.25F; break;
		}
		return velocity;
	}
	
	private float getRailgunProjectileDamage(ItemStack stack) {
		float damage = 0;
		switch(stack.getMetadata()) {
		case 0:
			damage = 5F;break;
		case 1:
			damage = 8F; break;
		default:
			damage = 10F; break;
		}
		return damage;
		//return damage*2;
	}
	
	private int getRailgunProjectileKnockback(ItemStack stack) {
		int knockback = 0;
		switch(stack.getMetadata()) {
		case 0:
			knockback = 1;break;
		case 1:
			knockback = 3; break;
		default:
			knockback = 5; break;
		}
		return knockback;
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
