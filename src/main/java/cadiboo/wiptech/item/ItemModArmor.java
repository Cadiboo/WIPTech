package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemModArmor extends ItemArmor implements IItemModMaterial {

	protected final ModMaterials material;

	public ItemModArmor(final ModMaterials materialIn, final EntityEquipmentSlot slotIn) {
		super(materialIn.getArmorMaterial(), materialIn.getId() + 5, slotIn);
		ModUtil.setRegistryNames(this, materialIn, ModUtil.getSlotGameNameLowercase(slotIn));
		this.material = materialIn;
	}

	@Override
	public boolean hasOverlay(final ItemStack stack) {
		return this.material == ModMaterials.TUNGSTEN_CARBITE;
	}

	@Override
	public ModMaterials getModMaterial() {
		return this.material;
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}
