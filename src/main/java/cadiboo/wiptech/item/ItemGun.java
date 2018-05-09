package cadiboo.wiptech.item;

import javax.annotation.Nonnull;

import cadiboo.wiptech.provider.ModularWeaponProvider;
import cadiboo.wiptech.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.EnumHelper;

public abstract class ItemGun extends ItemBase {

	protected int				reloadTimeRemaining;
	public static EnumAction	ZOOM	= EnumHelper.addAction(Reference.ID + "zoom");

	public ItemGun(String name) {
		super(name);
		this.maxStackSize = 1;
		this.setMaxDamage(100);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack item, NBTTagCompound nbt) {
		ICapabilityProvider modules = new ModularWeaponProvider();
		return modules;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	protected ItemStack findAmmo(EntityPlayer player) {
		if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND)))
			return player.getHeldItem(EnumHand.OFF_HAND);
		else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND)))
			return player.getHeldItem(EnumHand.MAIN_HAND);
		else {
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				ItemStack itemstack = player.inventory.getStackInSlot(i);

				if (this.isAmmo(itemstack)) {
					return itemstack;
				}
			}
			return ItemStack.EMPTY;
		}
	}

	abstract protected boolean isAmmo(ItemStack stack);
	// {
	// return stack.getItem() instanceof ItemParamagneticProjectile113;
	// }

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (reloadTimeRemaining > 0)
			reloadTimeRemaining--;
		stack.setItemDamage(reloadTimeRemaining);
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		this.handleUsingTick(stack, player, count);
		super.onUsingTick(stack, player, count);
	}

	protected abstract void handleUsingTick(ItemStack stack, EntityLivingBase player, int count);

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		this.handleStoppedUsing(stack, worldIn, entityLiving, timeLeft);
	}

	protected abstract void handleStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft);

	@Nonnull
	protected abstract Item getDefaultAmmo();

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if (reloadTimeRemaining > 0)
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);

		boolean hasAmmo = !this.findAmmo(playerIn).isEmpty();

		if (!playerIn.isCreative() && !hasAmmo)
			return new ActionResult(EnumActionResult.FAIL, itemstack);

		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

}
