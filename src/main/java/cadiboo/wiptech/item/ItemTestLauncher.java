package cadiboo.wiptech.item;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.IWeaponModular;
import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile;
import cadiboo.wiptech.entity.projectile.EntityProjectileBase;
import cadiboo.wiptech.handler.GuiHandler;
import cadiboo.wiptech.init.Capabilities;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.provider.TestLauncherProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemTestLauncher extends ItemBase {

	private boolean overheat;
	private long lastShootTime;
	private long secondlastShootTime;
	private long thirdlastShootTime;
	private long fourthlastShootTime;

	public ItemTestLauncher(String name)
	{
		super(name);
		this.maxStackSize = 1;
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.COMBAT);
		this.overheat = false;
		this.lastShootTime = 0;
		this.secondlastShootTime = lastShootTime;
		this.thirdlastShootTime = secondlastShootTime;
		this.fourthlastShootTime = thirdlastShootTime;
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
	public void onPlayerStoppedUsing(ItemStack itemStackIn, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLiving;
			ItemStack ammoStack = ItemStack.EMPTY;

			if (this.getMaxItemUseDuration(itemStackIn) - timeLeft <= 0) return;

			if (!ammoStack.isEmpty() || player.capabilities.isCreativeMode)
			{
				if (ammoStack.isEmpty())
				{
					ammoStack = new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, 5); //TUNGSTEN MEDIUM
				}

				float velocity = 0;

				IWeaponModular modules = itemStackIn.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);

				boolean flag = player.capabilities.isCreativeMode || (ammoStack.getItem() instanceof ItemFerromagneticProjectile && ((ItemFerromagneticProjectile) ammoStack.getItem()).isInfinite(ammoStack, itemStackIn, player));

				this.fourthlastShootTime = this.thirdlastShootTime;
				this.thirdlastShootTime = this.secondlastShootTime;
				this.secondlastShootTime = this.lastShootTime;
				this.lastShootTime = worldIn.getTotalWorldTime();

				if((this.lastShootTime - this.secondlastShootTime + this.thirdlastShootTime - fourthlastShootTime)<5) {
					this.overheat = true;
				} else {
					this.overheat = false;
				}


				if (!worldIn.isRemote)
				{

					ItemFerromagneticProjectile itemprojectile = (ItemFerromagneticProjectile)(ammoStack.getItem() instanceof ItemFerromagneticProjectile ? ammoStack.getItem() : Items.FERROMAGNETIC_PROJECILE);
					EntityFerromagneticProjectile projectile = itemprojectile.createProjectile(worldIn, ammoStack, player, false);
					velocity = (EntityFerromagneticProjectile.getProjectileVelocity(ammoStack))*(modules.getCoil().getEfficiencyFraction());
					WIPTech.logger.info(velocity);
					WIPTech.logger.info(EntityFerromagneticProjectile.getProjectileVelocity(ammoStack));
					WIPTech.logger.info(modules.getCoil().getEfficiencyFraction());
					projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity, 0.1F);

					if(this.overheat) {
						projectile.setTemperature(projectile.getTemperature() + 25F);
					}

					if (flag)
					{
						projectile.pickupStatus = EntityProjectileBase.PickupStatus.CREATIVE_ONLY;
					}

					worldIn.spawnEntity(projectile);
				}

				worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_FIREWORK_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

				if (!flag)
				{
					ammoStack.shrink(1);

					if (ammoStack.isEmpty())
					{
						player.inventory.deleteStack(ammoStack);
					}
				}
			}
		}
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if( itemstack.getItem() == Items.TEST_LAUNCHER ) {
			WIPTech.logger.info(itemstack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null).getModuleList());

			if (!worldIn.isRemote && playerIn.isSneaking()){
				playerIn.openGui(WIPTech.instance, GuiHandler.TEST_LAUNCHER, worldIn, 0, 0, 0);
			}
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
