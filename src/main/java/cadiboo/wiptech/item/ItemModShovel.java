package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSpade;

public class ItemModShovel extends ItemSpade implements IItemModMaterial {

	protected final ModMaterials material;

	public ItemModShovel(final ModMaterials materialIn) {
		super(materialIn.getToolMaterial());
		ModUtil.setRegistryNames(this, materialIn, "shovel");
		this.material = materialIn;
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
