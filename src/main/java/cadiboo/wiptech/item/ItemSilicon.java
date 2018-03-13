package cadiboo.wiptech.item;

import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSilicon
  extends ItemBase
{
  public ItemSilicon(String name)
  {
    super(name);
    setBeaconPayment();
  }
  
  public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
  {
    tooltip.add("Very dark and shiny, I wonder if this could be made wafer-thin and used to make a basic computer?");
    tooltip.add("§6§o*hint*");
  }
}
