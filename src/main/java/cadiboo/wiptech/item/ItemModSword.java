package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;

public class ItemModSword extends ItemSword implements IItemModMaterial {

	protected final ModMaterials material;

	public ItemModSword(final ModMaterials material) {
		super(material.getToolMaterial());
		ModUtil.setRegistryNames(this, material, "sword");
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
