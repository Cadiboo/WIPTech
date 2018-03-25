package cadiboo.wiptech.item;

import java.util.List;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.IWeaponModular;
import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile;
import cadiboo.wiptech.entity.projectile.EntityProjectileBase;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.*;
import cadiboo.wiptech.init.Capabilities;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.provider.ModularWeaponProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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

public class ItemPlasmagun extends ItemBase {

	private static final long shootTimeAdd = 5;
	private static final int burstShotsAllowed = 5;
	private static final double overheatTemperature = 10;
	private static final int circuitAutoRepeats = 3;
	private static ItemStack plasmaStack = new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, 9); //Plasma

	private int burstShotsTaken;
	private boolean overheat;
	private long lastShootTime;
	private double temperature;

	public ItemPlasmagun(String name)
	{
		super(name);
		this.maxStackSize = 1;
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.COMBAT);
		this.burstShotsTaken = 0;
		this.overheat = false;
		this.lastShootTime = 0;
		this.temperature = 0;
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

			if (this.getMaxItemUseDuration(itemStackIn) - timeLeft <= 0) return;

			IWeaponModular modules = itemStackIn.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);

			if(modules!=null) {
				Circuits circuit = modules.getCircuit();
				if(circuit!=null) {
					if(circuit!=Circuits.MANUAL)
						return;
					handleShoot(itemStackIn, worldIn, (EntityPlayer) entityLiving, modules);
				}

			}
		}
	}

	@Override
	public void onUsingTick(ItemStack itemStackIn, EntityLivingBase entityLiving, int count) {
		if (entityLiving instanceof EntityPlayer)
		{
			IWeaponModular modules = itemStackIn.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);

			if(modules!=null) {
				Circuits circuit = modules.getCircuit();
				if(circuit!=null) {
					if(circuit==Circuits.MANUAL)
						return;
					if(circuit==Circuits.BURST) {
						if(this.burstShotsTaken <= burstShotsAllowed) {
							this.burstShotsTaken++;
						} else {
							return;
						}
					}
					handleShoot(itemStackIn, ((EntityPlayer) entityLiving).getEntityWorld(), (EntityPlayer) entityLiving, modules);
					if(circuit==Circuits.AUTO) {
						for(int i = 0; i<circuitAutoRepeats; i++) {
							handleShoot(itemStackIn, ((EntityPlayer) entityLiving).getEntityWorld(), (EntityPlayer) entityLiving, modules);
						}
					}
				}

			}
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(this.temperature>0)
			this.temperature--;
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	private void handleShoot(ItemStack itemStackIn, World worldIn, EntityPlayer player, IWeaponModular modules) {

		boolean flag = player.capabilities.isCreativeMode;

		float velocity = 0;

		if(this.lastShootTime>0) {

			this.temperature += this.lastShootTime - worldIn.getTotalWorldTime() + shootTimeAdd;

			this.overheat = this.temperature>overheatTemperature;

			this.lastShootTime = worldIn.getTotalWorldTime();
		}

		if (!worldIn.isRemote)
		{
			EntityFerromagneticProjectile projectile = ((ItemFerromagneticProjectile)plasmaStack.getItem()).createProjectile(worldIn, plasmaStack, player, true);
			velocity = EntityFerromagneticProjectile.getProjectileVelocity(plasmaStack)*modules.getCoil().getEfficiencyFraction()*modules.getRail().getEfficiencyFraction();
			projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity, 0.1F);

			if(this.overheat) {
				projectile.setOverheat(true);
				projectile.setFire(EntityFerromagneticProjectile.overheatFireTime);
			}

			if (flag)
			{
				projectile.pickupStatus = EntityProjectileBase.PickupStatus.CREATIVE_ONLY;
			}

			worldIn.spawnEntity(projectile);
		}

		worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_FIREWORK_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if( itemstack.getItem() == Items.PLASMA_GUN ) {
			this.burstShotsTaken = 0;
			WIPTech.logger.info(itemstack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null).getModuleList());
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
		if( item.getItem() == Items.PLASMA_GUN ) {
			return new ModularWeaponProvider();
		}
		return null;
	}

	//doesnt work when giving it to yourself through creative/commands
	/*@Override
	public void onCreated(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		IWeaponModular modules = itemStackIn.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);
		WIPTech.logger.info(modules);
		if(modules!=null) {
			modules.setCircuit(Circuits.AUTO);
			modules.setCoil(Coils.SILVER);
			modules.setRail(Rails.SILVER);
			modules.setScope(Scopes.LASER);
			WIPTech.logger.info(modules.getModuleList());
		}
		super.onCreated(itemStackIn, worldIn, playerIn);
	}*/
	
	@Override
	public void addInformation(ItemStack itemStackIn, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		IWeaponModular modules = itemStackIn.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);

		if(modules!=null && modules.getModules()>0) {
			tooltip.add("Installed Modules:");
			tooltip.addAll(modules.getModuleList());
		}
		
		super.addInformation(itemStackIn, worldIn, tooltip, flagIn);
	}

}
