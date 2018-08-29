package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;

public class ItemCasedSlug extends ModItem implements IItemModMaterial {

	protected final ModMaterials material;

	public ItemCasedSlug(ModMaterials materialIn) {
		super("cased_" + materialIn.getNameLowercase() + "_slug");
		this.material = materialIn;
	}

	@Override
	public ModMaterials getModMaterial() {
		return material;
	}

}
