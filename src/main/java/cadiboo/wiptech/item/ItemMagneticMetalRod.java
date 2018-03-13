package cadiboo.wiptech.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemMagneticMetalRod
  extends ItemBase
{
  public static final String[] itemNames = { "iron_rod", "tungsten_rod", "osmium_rod" };
  
  public ItemMagneticMetalRod(String name)
  {
    super(name);
    setHasSubtypes(true);
    setMaxDamage(0);
  }
  
  public String getUnlocalizedName(ItemStack stack)
  {
    int i = stack.getMetadata();
    return super.getUnlocalizedName() + "." + EnumHandler.MagneticMetalRods.byMetadata(i).getUnlocalizedName();
  }
  
  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
  {
    if (isInCreativeTab(tab)) {
      for (int i = 0; i < 3; i++) {
        items.add(new ItemStack(this, 1, i));
      }
    }
  }
}
