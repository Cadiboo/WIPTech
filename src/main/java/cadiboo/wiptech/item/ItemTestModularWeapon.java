package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.provider.ModularWeaponProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
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

public class ItemTestModularWeapon extends ItemBase {

	// private final IWeaponModular modules = null;
	private static final ItemStack PROJECTILE_STACK = new ItemStack(Items.PARAMAGNETIC_PROJECILE, 1, 2);

	public ItemTestModularWeapon(String name) {
		super(name);
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if (itemstack.getItem() == Items.TEST_MODULAR_WEAPON) {
			// WIPTech.logger.info(itemstack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY,
			// null).getModules());
			// WIPTech.logger.info(itemstack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY,
			// null).getModuleList());
			// int circuit = itemstack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY,
			// null).getCircuit().getID();
			// circuit++;
			// if(circuit>2)
			// circuit=0;
			// itemstack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY,
			// null).setCircuit(Circuits.byID(circuit));
		}
		playerIn.setActiveHand(handIn);
		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack item, NBTTagCompound nbt) {
		if (item.getItem() == Items.TEST_MODULAR_WEAPON) {
			return new ModularWeaponProvider(item);
		}
		return null;
	}

	@Override
	public void onUsingTick(ItemStack itemStackIn, EntityLivingBase entityLiving, int count) {
		float velocity = 0;
		World worldIn = entityLiving.getEntityWorld();
		EntityPlayer player = (EntityPlayer) entityLiving;

		if (!worldIn.isRemote) {

			EntityParamagneticProjectile projectile = ((ItemParamagneticProjectile) PROJECTILE_STACK.getItem()).createProjectile(worldIn, PROJECTILE_STACK, player, false);
			velocity = EntityParamagneticProjectile.getProjectileVelocity(PROJECTILE_STACK);
			projectile.setDamage(projectile.getDamage());
			projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity, 0.1F);

			projectile.pickupStatus = PickupStatus.CREATIVE_ONLY;

			worldIn.spawnEntity(projectile);
		}

		worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_FIREWORK_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
	}

}
