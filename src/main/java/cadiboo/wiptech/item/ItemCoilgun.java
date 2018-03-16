package cadiboo.wiptech.item;

import cadiboo.wiptech.Reference;
import cadiboo.wiptech.entity.projectile.EntityCoilgunProjectile;
import cadiboo.wiptech.entity.projectile.EntityRailgunProjectile;
import cadiboo.wiptech.init.Items;

import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemCoilgun
extends ItemBase
{
	private static final Item IRON_NUGGET = net.minecraft.item.Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_nugget"));

	public ItemCoilgun(String name)
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
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		/*
		if(this.getComponents.contains(rollingCircuit||machinegunCircuit) {
		 */
		World worldIn = player.world;
		if ((player instanceof EntityPlayer))
		{
			float velocity = 0.75F;
			if (!worldIn.isRemote)
			{
				EntityCoilgunProjectile entitycoilgunprojectile = new EntityCoilgunProjectile(worldIn, player);
				entitycoilgunprojectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity, 1.0F);
				worldIn.spawnEntity(entitycoilgunprojectile);
			}
			worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
		}
	}
	
	protected boolean isAmmo(ItemStack stack)
	{
		return stack.getItem() == IRON_NUGGET || stack.getItem() instanceof ItemOsmiumNugget || stack.getItem() instanceof ItemTungstenNugget;
	}
	
	private ItemStack findAmmo(EntityPlayer player)
	{
		if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND)))
		{
			return player.getHeldItem(EnumHand.OFF_HAND);
		}
		else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND)))
		{
			return player.getHeldItem(EnumHand.MAIN_HAND);
		}
		else
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
			{
				ItemStack itemstack = player.inventory.getStackInSlot(i);

				if (this.isAmmo(itemstack))
				{
					return itemstack;
				}
			}

			return ItemStack.EMPTY;
		}
	}
	
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		/*
		if(!this.getComponents.contains(rollingCircuit||machinegunCircuit) {
		 */
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
					itemstack = new ItemStack(Items.TUNGSTEN_NUGGET, 1, 2); //TUNGSTEN
				}

				float velocity = getCoilgunProjectileVelocity(stack);

				if ((double)velocity >= 0.1D)
				{
					boolean flag1 = entityplayer.capabilities.isCreativeMode/* || (itemstack.getItem() instanceof ItemMagneticMetalRod && ((ItemMagneticMetalRod) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer))*/;

					if (!worldIn.isRemote)
					{
						Item itemnugget = ((stack.getItem() == IRON_NUGGET || stack.getItem() instanceof ItemOsmiumNugget || stack.getItem() instanceof ItemTungstenNugget) ? itemstack.getItem() : Items.MAGNETIC_METAL_ROD);

						//EntityRailgunProjectile railgunProjectile = new EntityRailgunProjectile(worldIn, entityplayer);
						
						EntityCoilgunProjectile coilgunProjectile = new EntityCoilgunProjectile(worldIn, entityplayer);
						coilgunProjectile.setNuggetId(this.getAmmoLevel(stack));
						//return railgunProjectile;
						
						
						coilgunProjectile.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, velocity, 0.0F);

						coilgunProjectile.setDamage(getCoilgunProjectileDamage(itemstack));

						coilgunProjectile.setKnockbackStrength(getCoilgunProjectileKnockback(itemstack));
						//if(this.railgun.overheat) entityarrow.setFire(100);

						if (flag1 || entityplayer.capabilities.isCreativeMode)
						{
							coilgunProjectile.pickupStatus = EntityCoilgunProjectile.PickupStatus.CREATIVE_ONLY;
						}

						worldIn.spawnEntity(coilgunProjectile);
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
	
	private int getAmmoLevel(ItemStack stack) {
		int level = 0;
		if(stack.getItem() instanceof ItemOsmiumNugget)
			level = 1;
		if(stack.getItem() instanceof ItemTungstenNugget)
			level = 2;
		return level;
	}
	
	private float getCoilgunProjectileVelocity(ItemStack stack) {
		float velocity = 0;
		switch(getAmmoLevel(stack)) {
		case 0:
			velocity = 3.5F;break;
		case 1:
			velocity = 3F; break;
		default:
			velocity = 3.25F; break;
		}
		return velocity;
	}

	private float getCoilgunProjectileDamage(ItemStack stack) {
		float damage = 0;
		switch(getAmmoLevel(stack)) {
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
	
	private int getCoilgunProjectileKnockback(ItemStack stack) {
		int knockback = 0;
		switch(getAmmoLevel(stack)) {
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
