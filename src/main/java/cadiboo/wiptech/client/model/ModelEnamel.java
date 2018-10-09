package cadiboo.wiptech.client.model;

import cadiboo.wiptech.material.ModMaterial;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelEnamel extends ModelWire implements IModel {

	public ModelEnamel(final ModMaterial material) {
		super(material);
	}

	@Override
	protected ModResourceLocation getModelLocation(final String string) {
		if (string == "core") {
			return new ModResourceLocation(ModReference.MOD_ID, "block/" + this.material.getNameLowercase() + "_enamel_core");
		}
		return new ModResourceLocation(ModReference.MOD_ID, "block/" + this.material.getNameLowercase() + "_enamel_extension_" + string);
	}

}
