package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemFlamethrower extends Item implements IModItem {

	public ItemFlamethrower(final String name) {
		ModUtil.setRegistryNames(this, name);
		this.setMaxStackSize(0);
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(final ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public void onUsingTick(final ItemStack stack, final EntityLivingBase player, final int count) {
		if (!(player instanceof EntityPlayer)) {
			return;
		}

		final float velocity = 1.25F;
		if (!player.world.isRemote) {
			final EntityNapalm entitynapalm = new EntityNapalm(player.world, player);
			entitynapalm.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity, 5.0F);
			player.world.spawnEntity(entitynapalm);
		}
		player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F, (1.0F / ((itemRand.nextFloat() * 0.4F) + 1.2F)) + (velocity * 0.5F));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
		final ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}
