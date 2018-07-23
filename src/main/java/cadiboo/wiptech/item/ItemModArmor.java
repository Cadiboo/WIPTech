package cadiboo.wiptech.item;

import cadiboo.wiptech.creativetab.ModCreativeTabs;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;

public class ItemModArmor extends ItemArmor {

	public ItemModArmor(ModMaterials materialIn, EntityEquipmentSlot slotIn) {

		super(materialIn.getArmorMaterial(), 4, slotIn);
		ResourceLocation name = new ResourceLocation(materialIn.getResouceLocationDomain(), materialIn.getNameLowercase() + "_" + ModUtil.getSlotGameNameLowercase(slotIn));
		this.setRegistryName(name);
		this.setUnlocalizedName(name.getResourcePath());
		this.setCreativeTab(ModCreativeTabs.CREATIVE_TAB);

	}

}
