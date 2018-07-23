package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class BlockWire extends ModBlock {

	protected final ModMaterials material;

	public BlockWire(ModMaterials materialIn) {
		super(materialIn.getProperties().getMaterial(), new ResourceLocation(ModReference.ID, materialIn.getNameLowercase() + "_wire"));
		this.material = materialIn;
	}

	public final ModMaterials getModMaterial() {
		return this.material;
	}
}
