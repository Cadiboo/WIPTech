package cadiboo.wiptech.item;

import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGalliumIngot
  extends ItemBase
{
  public ItemGalliumIngot(String name)
  {
    super(name);
    setBeaconPayment();
  }
  
  public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
  {
    tooltip.add("Your attempt to turn it into a more familiar form didn't really succeed...");
  }
}
