package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSpade;

public class ItemModShovel extends ItemSpade implements IItemModMaterial {

	protected final ModMaterials material;

	public ItemModShovel(final ModMaterials material) {
		super(material.getToolMaterial());
		ModUtil.setRegistryNames(this, material, "shovel");
		this.material = material;
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
