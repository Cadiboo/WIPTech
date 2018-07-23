package cadiboo.wiptech.item;

import cadiboo.wiptech.creativetab.ModCreativeTabs;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.item.ItemSpade;
import net.minecraft.util.ResourceLocation;

public class ItemModSword extends ItemSpade {

	public ItemModSword(ModMaterials materialIn) {

		super(materialIn.getToolMaterial());
		ResourceLocation name = new ResourceLocation(materialIn.getResouceLocationDomain(), materialIn.getNameLowercase() + "_sword");
		this.setRegistryName(name);
		this.setUnlocalizedName(name.getResourcePath());
		this.setCreativeTab(ModCreativeTabs.CREATIVE_TAB);

	}

}
