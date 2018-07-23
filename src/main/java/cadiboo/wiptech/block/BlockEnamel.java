package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class BlockEnamel extends ModBlock {

	protected final ModMaterials material;

	public BlockEnamel(ModMaterials materialIn) {
		super(materialIn.getProperties().getMaterial(), new ResourceLocation(ModReference.ID, materialIn.getNameLowercase() + "_enamel"));
		this.material = materialIn;
	}

	public final ModMaterials getModMaterial() {
		return this.material;
	}
}
