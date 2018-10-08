package cadiboo.wiptech.material;

import net.minecraft.util.BlockRenderLayer;

public class MetalProperties extends ModMaterialProperties {

	public MetalProperties(final boolean hasOre, final float MOHS_Hardness, final int thermalConductivityAt20DegreesCelsius) {
		super(hasOre, true, true, true, true, true, true, true, true, true, true, true, true, hasOre, MOHS_Hardness, thermalConductivityAt20DegreesCelsius, null, new BlockRenderLayer[]{BlockRenderLayer.SOLID}, (fortune, random) -> {
			return 1;
		});
	}

}
