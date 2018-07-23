package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModEnums.BlockItemTypes;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class BlockItem extends ModBlock {

	protected final ModMaterials	material;
	protected final BlockItemTypes	type;

	public BlockItem(ModMaterials materialIn, BlockItemTypes typeIn) {
		super(materialIn.getProperties().getMaterial(), new ResourceLocation(ModReference.ID, materialIn.getNameLowercase() + "_" + typeIn.getNameLowercase()));
		this.material = materialIn;
		this.type = typeIn;
	}

	public final ModMaterials getModMaterial() {
		return this.material;
	}

	public final BlockItemTypes getType() {
		return this.type;
	}

}
