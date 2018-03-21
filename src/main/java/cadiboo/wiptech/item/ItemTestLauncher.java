package cadiboo.wiptech.item;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile;
import cadiboo.wiptech.entity.projectile.EntityProjectileBase;
import cadiboo.wiptech.handler.GuiHandler;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.provider.TestLauncherProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemTestLauncher extends ItemBase {
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

				float velocity = EntityFerromagneticProjectile.getProjectileVelocity(stack);

				if ((double)velocity >= 0.1D)
				{
					boolean flag1 = entityplayer.capabilities.isCreativeMode/* || (itemstack.getItem() instanceof ItemMagneticMetalRod && ((ItemMagneticMetalRod) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer))*/;

					if (!worldIn.isRemote)
					{
						ItemFerromagneticProjectile itemprojectile = (ItemFerromagneticProjectile)(itemstack.getItem() instanceof ItemFerromagneticProjectile ? itemstack.getItem() : Items.FERROMAGNETIC_PROJECILE);
						EntityFerromagneticProjectile projectile = itemprojectile.createProjectile(worldIn, itemstack, entityplayer, false);
						projectile.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, velocity, 0.1F);
						
						//if(this.railgun.overheat) entityarrow.setFire(100);

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

		if (!worldIn.isRemote && playerIn.isSneaking()){
			//playerIn.openGui(WIPTech.instance, GuiHandler.TEST_LAUNCHER, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
			playerIn.openGui(WIPTech.instance, GuiHandler.TEST_LAUNCHER, worldIn, 0, 0, 0);
			//LogHelper.info("Succesfully opened GUI");
		}
		
		playerIn.setActiveHand(handIn);
		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}

	public int getItemEnchantability()
	{
		return 0;
	}
	
	@Override
	public ICapabilityProvider initCapabilities( ItemStack item, NBTTagCompound nbt ) {
		if( item.getItem() == Items.TEST_LAUNCHER ) {
			return new TestLauncherProvider();
		}
		return null;
	}
	
}
