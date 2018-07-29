package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.util.BlockRenderLayer;

public class BlockModOre extends ModMaterialBlock {

	protected final ModMaterials material;

	public BlockModOre(ModMaterials materialIn) {
		super(materialIn, "ore");
		this.material = materialIn;
	}

	@Override
	public final ModMaterials getModMaterial() {
		return this.material;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

}
