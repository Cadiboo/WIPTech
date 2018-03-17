package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile;
import cadiboo.wiptech.entity.projectile.EntityProjectileBase;
import cadiboo.wiptech.init.Items;
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

public class ItemCoilgun extends ItemBase {
	private static final Item IRON_NUGGET = net.minecraft.item.Item.REGISTRY.getObject(new ResourceLocation("minecraft", "iron_nugget"));
	//private static enum CIRCUIT_TYPE;

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
				EntityFerromagneticProjectile entitycoilgunprojectile = new EntityFerromagneticProjectile(worldIn, player);
				entitycoilgunprojectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity, 1.0F);
				worldIn.spawnEntity(entitycoilgunprojectile);
			}
			worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
		}
	}
	
	protected boolean isAmmo(ItemStack stack)
	{
		return stack.getItem() instanceof ItemFerromagneticProjectile;
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
					itemstack = new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, 5); //TUNGSTEN
				}

				float velocity = EntityFerromagneticProjectile.getProjectileVelocity(stack);

				if ((double)velocity >= 0.1D)
				{
					boolean flag1 = entityplayer.capabilities.isCreativeMode/* || (itemstack.getItem() instanceof ItemMagneticMetalRod && ((ItemMagneticMetalRod) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer))*/;

					if (!worldIn.isRemote)
					{
						EntityFerromagneticProjectile projectile = new EntityFerromagneticProjectile(worldIn, entityplayer);
						projectile.setAmmoId(EntityFerromagneticProjectile.getAmmoTier(stack.getMetadata()));
						
						projectile.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, velocity, 0.0F);

						projectile.setDamage(EntityFerromagneticProjectile.getProjectileDamage(itemstack));

						projectile.setKnockbackStrength(EntityFerromagneticProjectile.getProjectileKnockback(itemstack));
						//if(this.coilgun.overheat) entityarrow.setFire(100);

						if (flag1 || entityplayer.capabilities.isCreativeMode)
						{
							projectile.pickupStatus = EntityProjectileBase.PickupStatus.CREATIVE_ONLY;
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
