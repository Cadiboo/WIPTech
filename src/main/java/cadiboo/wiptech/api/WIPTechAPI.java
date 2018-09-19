package cadiboo.wiptech.api;

import cadiboo.wiptech.util.ModEnums.CircuitTypes;
import cadiboo.wiptech.util.ModEnums.ModMaterialTypes;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModEnums.UsePhases;
import cadiboo.wiptech.util.ModMaterialProperties;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;

/**
 * API (Application Programming Interface) to allow other Mods to add other {@link cadiboo.wiptech.util.ModEnums.ModMaterials materials} to be created by our mod
 * @author Cadiboo
 */
public final class WIPTechAPI {

	/**
	 * Adds a {@link cadiboo.wiptech.util.ModEnums.ModMaterials material}
	 * @param name        the name for the new material
	 * @param type        the {@link cadiboo.wiptech.util.ModEnums.ModMaterialTypes type} of the material
	 * @param properties  the {@link cadiboo.wiptech.util.ModMaterialProperties properties} of the material
	 * @param assetsModId the Mod Id that assets (textures, models) will be loaded from.
	 * @return the new material
	 */
	public static ModMaterials addMaterial(final String name, final ModMaterialTypes type, final ModMaterialProperties properties, final String assetsModId) {

		final Class[] parameterTypes = new Class[] { int.class, ModMaterialTypes.class, ModMaterialProperties.class, String.class };
		final int enumId = ModMaterials.values().length;

		return EnumHelper.addEnum(ModMaterials.class, name, parameterTypes, enumId, type, properties, assetsModId);
	}

	/**
	 * Adds a {@link cadiboo.wiptech.util.ModEnums.ModMaterials material} with the active Mod's Id as the assetsModId
	 * @param name       the name for the new material
	 * @param type       the {@link cadiboo.wiptech.util.ModEnums.ModMaterialTypes type} of the material
	 * @param properties the {@link cadiboo.wiptech.util.ModMaterialProperties properties} of the material
	 * @return the new material
	 */
	public static ModMaterials addMaterial(final String name, final ModMaterialTypes type, final ModMaterialProperties properties) {
		final String assetsModId = Loader.instance().activeModContainer().getModId();
		return addMaterial(name, type, properties, assetsModId);
	}

	/**
	 * Adds a {@link cadiboo.wiptech.util.ModEnums.CircuitTypes circuit type} to WIPTech that will automatically have an item created for it
	 * @param name                          the name for the new {@link CircuitTypes circuit type}
	 * @param maxShots                      the maximum amount of shots that can be taken (useful for burst circuits)
	 * @param shootIntervalTimeMilliseconds how long between shots
	 * @param usePhases                     when the circuit can shoot
	 * @param assetsModId                   the Mod Id that assets (textures, models) will be loaded from.
	 * @return the new circuit type
	 */
	public static CircuitTypes addCircuit(final String name, final int maxShots, final int shootIntervalTimeMilliseconds, final UsePhases[] usePhases, final String assetsModId) {
		final Class[] parameterTypes = new Class[] { int.class, int.class, int.class, usePhases.getClass() };
		final int enumId = ModMaterials.values().length;

		return EnumHelper.addEnum(CircuitTypes.class, name, parameterTypes, enumId, maxShots, shootIntervalTimeMilliseconds, usePhases);
	}

	/**
	 * Adds a {@link cadiboo.wiptech.util.ModEnums.CircuitTypes circuitType} to WIPTech that will automatically have an item created for it with the active Mod's Id as the assetsModId
	 * @param name                          the name for the new {@link CircuitTypes circuit type}
	 * @param maxShots                      the maximum amount of shots that can be taken (useful for burst circuits)
	 * @param shootIntervalTimeMilliseconds how long between shots
	 * @param usePhases                     when the circuit can shoot
	 * @return the new circuit type
	 */
	public static CircuitTypes addCircuit(final String name, final int maxShots, final int shootIntervalTimeMilliseconds, final UsePhases[] usePhases) {
		final String assetsModId = Loader.instance().activeModContainer().getModId();
		return addCircuit(name, maxShots, shootIntervalTimeMilliseconds, usePhases, assetsModId);
	}

}
