package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemModArmor extends ItemArmor {

	protected final ModMaterials material;

	public ItemModArmor(ModMaterials materialIn, EntityEquipmentSlot slotIn) {

		super(materialIn.getArmorMaterial(), materialIn.getId() + 5, slotIn);
		this.material = materialIn;
		ModUtil.setNameForMaterialItem(this, materialIn, ModUtil.getSlotGameNameLowercase(slotIn));
		ModUtil.setCreativeTab(this);

	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

	@Override
	public boolean hasOverlay(ItemStack stack) {
		return material == ModMaterials.TUNGSTEN_CARBITE;
	}

}
