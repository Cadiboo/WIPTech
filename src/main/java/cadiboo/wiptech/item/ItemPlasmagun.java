package cadiboo.wiptech.item;

import java.util.List;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.IWeaponModular;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.init.Capabilities;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.provider.ModularWeaponProvider;
import cadiboo.wiptech.util.CustomEnergyStorage;
import cadiboo.wiptech.util.EnergyUtils;
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
import net.minecraftforge.energy.CapabilityEnergy;

public class ItemPlasmagun extends ItemBase {

	private static final ItemStack PLASMA_STACK = new ItemStack(Items.PARAMAGNETIC_PROJECILE, 1, 9); // Plasma

	public ItemPlasmagun(String name) {
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
	public void onPlayerStoppedUsing(ItemStack itemStackIn, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;

			if (this.getMaxItemUseDuration(itemStackIn) - timeLeft <= 0)
				return;

			IWeaponModular modules = itemStackIn.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);

			if (modules != null) {
				// modules.incrementShotsTaken();
				// modules.resetBurstShotsTaken();
				Circuits circuit = modules.getCircuit();
				if (circuit != null) {
					if (circuit != Circuits.MANUAL)
						return;
					handleShoot(itemStackIn, worldIn, (EntityPlayer) entityLiving, modules, (CustomEnergyStorage) itemStackIn.getCapability(CapabilityEnergy.ENERGY, null));
				}

			}
		}
	}

	@Override
	public void onUsingTick(ItemStack itemStackIn, EntityLivingBase entityLiving, int count) {
		if (entityLiving instanceof EntityPlayer) {
			IWeaponModular modules = itemStackIn.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);

			// if (modules != null) {
			// modules.incrementShotsTaken();
			// Circuits circuit = modules.getCircuit();
			// if (circuit != null) {
			// switch (circuit) {
			// case AUTO:
			// if (Math.floor(modules.getShotsTaken() % WeaponModular.shootChance) != 0)
			// return;
			// break;
			// case BURST3:
			// if (modules.getBurstShotsTaken() < 3 && Math.floor(modules.getShotsTaken() %
			// WeaponModular.burstShootChance) != 0) {
			// modules.incrementBurstShotsTaken();
			// } else {
			// return;
			// }
			// break;
			// case BURST5:
			// if (modules.getBurstShotsTaken() < 5 && Math.floor(modules.getShotsTaken() %
			// WeaponModular.burstShootChance) != 0) {
			// modules.incrementBurstShotsTaken();
			// } else {
			// return;
			// }
			// break;
			// case BURST10:
			// if (modules.getBurstShotsTaken() < 10 && Math.floor(modules.getShotsTaken() %
			// WeaponModular.burstShootChance) != 0) {
			// modules.incrementBurstShotsTaken();
			// } else {
			// return;
			// }
			// break;
			// case OVERCLOCKED:
			// for (int i = 0; i < WeaponModular.circuitOverclockedRepeats; i++) {
			// handleShoot(itemStackIn, ((EntityPlayer) entityLiving).getEntityWorld(),
			// (EntityPlayer) entityLiving, modules, (CustomEnergyStorage)
			// itemStackIn.getCapability(CapabilityEnergy.ENERGY, null));
			// }
			// return;
			//
			// default:
			// return;
			//
			// }
			// handleShoot(itemStackIn, ((EntityPlayer) entityLiving).getEntityWorld(),
			// (EntityPlayer) entityLiving, modules, (CustomEnergyStorage)
			// itemStackIn.getCapability(CapabilityEnergy.ENERGY, null));
			// }
			//
			// }
		}
	}

	@Override
	public void onUpdate(ItemStack itemStackIn, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		IWeaponModular modules = itemStackIn.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);

		if (modules != null) {
			// modules.cool();
		}
		super.onUpdate(itemStackIn, worldIn, entityIn, itemSlot, isSelected);
	}

	private void handleShoot(ItemStack itemStackIn, World worldIn, EntityPlayer player, IWeaponModular modules, CustomEnergyStorage energy) {

		boolean flag = player.capabilities.isCreativeMode;

		// if (energy.getEnergyStored() < WeaponModular.energyCost)
		// return;
		//
		// if (modules.getCircuit() == null)
		// return;
		// if (modules.getCoil() == null) {
		// modules.setTemperature(modules.getOverheatTemperature() + 20);
		// return;
		// }
		if (modules.getRail() == null)
			return;

		float velocity = 0;

		if (!worldIn.isRemote) {
			// if (modules.getLastShootTime() > 0) {
			//
			// // WIPTech.logger.info("temperature: "+this.temperature+", lastShootTime:
			// // "+this.lastShootTime+", getTotalWorldTime:
			// "+worldIn.getTotalWorldTime()+",
			// // shootTimeAdd: "+shootTimeAdd);
			// if (modules.getLastShootTime() > worldIn.getTotalWorldTime() -
			// WeaponModular.overheatTimer)
			//
			// modules.setLastShootTime(worldIn.getTotalWorldTime());
			// }
			// modules.setLastShootTime(worldIn.getTotalWorldTime());
			//
			// EntityParamagneticProjectile projectile = ((ItemParamagneticProjectile)
			// PLASMA_STACK.getItem()).createProjectile(worldIn, PLASMA_STACK, player,
			// true);
			// velocity = EntityParamagneticProjectile.getProjectileVelocity(PLASMA_STACK) *
			// modules.getCoil().getEfficiencyFraction();
			// projectile.setDamage(projectile.getDamage() *
			// modules.getRail().getEfficiencyFraction());
			// projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F,
			// velocity, 0.1F);
			// projectile.setTemperature(1185F);
			//
			// if (modules.isOverheated()) {
			// projectile.setTemperature(projectile.getTemperature() + 25F);
			// }

			// worldIn.spawnEntity(projectile);
		}

		// energy.extractEnergy(WeaponModular.energyCost, false);

		worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_FIREWORK_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		WIPTech.logger.info(itemstack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored());

		if (itemstack.getItem() == Items.PLASMA_GUN) {
			IWeaponModular modules = itemstack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);

			if (modules != null) {
				// modules.resetBurstShotsTaken();
			}
		}
		playerIn.setActiveHand(handIn);
		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}

	@Override
	public int getItemEnchantability() {
		return 0;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack item, NBTTagCompound nbt) {
		if (item.getItem() == Items.PLASMA_GUN) {
			return new ModularWeaponProvider();
		}
		return null;
	}

	@Override
	public void addInformation(ItemStack itemStackIn, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		IWeaponModular modules = itemStackIn.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);

		if (modules != null && modules.getModules() > 0) {
			tooltip.add("Installed Modules:");
			tooltip.addAll(modules.getModuleList());
		}

		WIPTech.logger.info(modules.getModuleList());

		CustomEnergyStorage energy = (CustomEnergyStorage) itemStackIn.getCapability(CapabilityEnergy.ENERGY, null);
		if (energy != null) {
			WIPTech.logger.info(energy);
			// energy.setEnergyStored(5);
			WIPTech.logger.info(energy.getEnergyStored());
			tooltip.add(EnergyUtils.formatEnergy(energy));
		}
		super.addInformation(itemStackIn, worldIn, tooltip, flagIn);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		// return 1D-(getEnergy(stack)/getMaxEnergy(stack));
		return 0.5D;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		// return MathHelper.hsvToRGB(Math.max(0.0F,
		// (float)(1-getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
		return 123;
	}

}
