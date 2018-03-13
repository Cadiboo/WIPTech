package cadiboo.wiptech.item;

import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGallium
  extends ItemBase
{
  public ItemGallium(String name)
  {
    super(name);
    setBeaconPayment();
  }
  
  public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
  {
    tooltip.add("An odd semi-conducter of a metal, it melts at slightly over 25 degrees celcius - making it very fragile");
  }
}
