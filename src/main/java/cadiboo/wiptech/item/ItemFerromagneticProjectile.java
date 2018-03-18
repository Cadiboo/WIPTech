package cadiboo.wiptech.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemFerromagneticProjectile extends ItemBase {

	public static final String[] itemNames = {
			"iron_rod_large",
			"tungsten_rod_large",
			"osmium_rod_large",

			"iron_rod_medium",
			"tungsten_rod_medium",
			"osmium_rod_medium",

			"iron_rod_small",
			"tungsten_rod_small",
			"osmium_rod_small",

			//"plasma",

	};
	public static final int subTypesAmmount = itemNames.length;

	public ItemFerromagneticProjectile(String name)
	{
		super(name);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		int i = stack.getMetadata();
		if(i>subTypesAmmount)
			i=0;
		return super.getUnlocalizedName() + "." + EnumHandler.FerromagneticProjectiles.byMetadata(i).getUnlocalizedName();
	}

	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (isInCreativeTab(tab)) {
			for (int i = 0; i < subTypesAmmount; i++) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}

}
