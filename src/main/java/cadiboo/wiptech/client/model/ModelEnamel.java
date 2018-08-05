package cadiboo.wiptech.client.model;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;

public class ModelEnamel extends ModelWire implements IModel {

	public ModelEnamel(ModMaterials materialIn) {
		super(materialIn);
	}

	@Override
	protected ResourceLocation getModelLocation(String string) {
		if (string == "core")
			return new ResourceLocation(ModReference.ID, "block/enamel_core");
		return new ResourceLocation(ModReference.ID, "block/enamel_extension_" + string);
	}

}
