package cadiboo.wiptech.api;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModMaterialProperties;
import net.minecraftforge.common.util.EnumHelper;

public class WIPTechAPI {

	public static final ModMaterials addMaterial(String name, ModMaterialProperties properties) {
		return EnumHelper.addEnum(ModMaterials.class, name, new Class[] { int.class, ModMaterialProperties.class }, ModMaterials.values().length, properties);
	}

}
