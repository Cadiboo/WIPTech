package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemRail extends Item implements IItemModMaterial {

	protected final ModMaterials material;

	public ItemRail(final ModMaterials materialIn) {
		ModUtil.setRegistryNames(this, materialIn, "rail");
		this.material = materialIn;
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
