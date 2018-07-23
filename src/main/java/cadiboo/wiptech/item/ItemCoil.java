package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;

public class ItemCoil extends ModItem {

	protected final ModMaterials material;

	public ItemCoil(ModMaterials materialIn) {
		super(materialIn.getNameLowercase() + "_coil");
		this.material = materialIn;
	}

	public final ModMaterials getModMaterial() {
		return this.material;
	}

}
