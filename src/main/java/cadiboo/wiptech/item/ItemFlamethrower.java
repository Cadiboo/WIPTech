package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.projectile.EntityNapalm;
import java.util.Random;
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

public class ItemFlamethrower
  extends Item
{
  public ItemFlamethrower(String name)
  {
    setRegistryName("wiptech", name);
    setUnlocalizedName(name);
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
  
  public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
  {
    EntityLivingBase entityLiving = player;
    World worldIn = player.world;
    if ((entityLiving instanceof EntityPlayer))
    {
      float velocity = 0.75F;
      EntityPlayer entityplayer = (EntityPlayer)entityLiving;
      if (!worldIn.isRemote)
      {
        EntityNapalm entitynapalm = new EntityNapalm(worldIn, player);
        entitynapalm.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity, 1.0F);
        worldIn.spawnEntity(entitynapalm);
      }
      worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
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
