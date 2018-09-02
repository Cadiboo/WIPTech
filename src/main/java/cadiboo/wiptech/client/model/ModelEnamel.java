package cadiboo.wiptech.client.model;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelEnamel extends ModelWire implements IModel {

	public ModelEnamel(final ModMaterials material) {
		super(material);
	}

	@Override
	protected ResourceLocation getModelLocation(final String string) {
		if (string == "core") {
			return new ResourceLocation(ModReference.MOD_ID, "block/" + this.material.getNameLowercase() + "_enamel_core");
		}
		return new ResourceLocation(ModReference.MOD_ID, "block/" + this.material.getNameLowercase() + "_enamel_extension_" + string);
	}

}
