package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemCoil extends Item implements IItemModMaterial {

	private final ModMaterials material;

	public ItemCoil(final ModMaterials material) {
		ModUtil.setRegistryNames(this, material, "coil");
		this.material = material;
	}

	@Override
	public final ModMaterials getModMaterial() {
		return this.material;
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}
