package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;

public class ItemRail extends ModItem {

	protected final ModMaterials material;

	public ItemRail(ModMaterials materialIn) {
		super(materialIn.getNameLowercase() + "_rail");
		this.material = materialIn;
	}

	public final ModMaterials getModMaterial() {
		return this.material;
	}

}
