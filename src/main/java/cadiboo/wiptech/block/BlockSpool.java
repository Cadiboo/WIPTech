package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class BlockSpool extends ModBlock {

	protected final ModMaterials material;

	public BlockSpool(ModMaterials materialIn) {
		super(materialIn.getProperties().getMaterial(), new ResourceLocation(ModReference.ID, materialIn.getNameLowercase() + "_spool"));
		this.material = materialIn;
	}

	public final ModMaterials getModMaterial() {
		return this.material;
	}
}
