package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.util.ResourceLocation;

public class BlockResource extends ModBlock {

	protected final ModMaterials material;

	public BlockResource(ModMaterials materialIn) {
		super(materialIn.getProperties().getMaterial(), new ResourceLocation(materialIn.getResouceLocationDomain(), materialIn.getNameLowercase() + "_block"));
		this.material = materialIn;
	}

	public final ModMaterials getModMaterial() {
		return this.material;
	}

}
