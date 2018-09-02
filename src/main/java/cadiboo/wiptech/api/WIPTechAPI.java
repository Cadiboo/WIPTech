package cadiboo.wiptech.api;

import cadiboo.wiptech.util.ModEnums.ModMaterialTypes;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModMaterialProperties;
import net.minecraftforge.common.util.EnumHelper;

public final class WIPTechAPI {

	public static ModMaterials addMaterial(final String name, final ModMaterialTypes type, final ModMaterialProperties properties) {
		return EnumHelper.addEnum(ModMaterials.class, name, new Class[]{int.class, ModMaterialTypes.class, ModMaterialProperties.class}, ModMaterials.values().length, type, properties);
	}

}
