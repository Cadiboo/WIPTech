package cadiboo.wiptech.item;

import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSiliconWafer
  extends ItemBase
{
  public ItemSiliconWafer(String name)
  {
    super(name);
    setBeaconPayment();
  }
  
  public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
  {
    tooltip.add("Etch it to lay the basics for a basic chip");
  }
}
