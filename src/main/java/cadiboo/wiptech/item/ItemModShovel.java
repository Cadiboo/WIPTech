package cadiboo.wiptech.item;

import cadiboo.wiptech.creativetab.ModCreativeTabs;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;

public class ItemModShovel extends ItemSword {

	public ItemModShovel(ModMaterials materialIn) {

		super(materialIn.getToolMaterial());
		ResourceLocation name = new ResourceLocation(materialIn.getResouceLocationDomain(), materialIn.getNameLowercase() + "_shovel");
		this.setRegistryName(name);
		this.setUnlocalizedName(name.getResourcePath());
		this.setCreativeTab(ModCreativeTabs.CREATIVE_TAB);

	}

}
