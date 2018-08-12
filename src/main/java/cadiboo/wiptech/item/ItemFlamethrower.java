package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.projectile.EntityNapalm;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemFlamethrower extends ModItem {

    public ItemFlamethrower(String name) {
	super(name);
	setMaxStackSize(0);
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
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
	EntityLivingBase entityLiving = player;
	World worldIn = player.world;
	if ((entityLiving instanceof EntityPlayer)) {
	    float velocity = 1F;
	    EntityPlayer entityplayer = (EntityPlayer) entityLiving;
	    if (!worldIn.isRemote) {
		EntityNapalm entitynapalm = new EntityNapalm(worldIn, player);
		entitynapalm.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity, 5f);
		worldIn.spawnEntity(entitynapalm);
	    }
	    worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ,
		    SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F,
		    1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
	}
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
	ItemStack itemstack = playerIn.getHeldItem(handIn);
	playerIn.setActiveHand(handIn);
	return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }

}
