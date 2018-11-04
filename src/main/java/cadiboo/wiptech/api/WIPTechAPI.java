package cadiboo.wiptech.api;

import cadiboo.wiptech.material.ModMaterial;
import cadiboo.wiptech.material.ModMaterialProperties;
import cadiboo.wiptech.util.ModEnums.CircuitType;
import cadiboo.wiptech.util.ModEnums.UsePhase;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;

/**
 * API (Application Programming Interface) to allow other Mods to add other {@link cadiboo.wiptech.util.ModEnums.ModMaterial materials} to be created by our mod
 * 
 * @author Cadiboo
 */
public final class WIPTechAPI {

	/**
	 * Adds a {@link cadiboo.wiptech.util.ModEnums.ModMaterial material}
	 * 
	 * @param name        the name for the new material
	 * @param properties  the {@link cadiboo.wiptech.material.ModMaterialProperties properties} of the material
	 * @param assetsModId the Mod Id that assets (textures, models) will be loaded from.
	 * @return the new material
	 */
	public static ModMaterial addMaterial(final String name, final ModMaterialProperties properties, final String assetsModId) {

		final Class<?>[] parameterTypes = new Class[] { int.class, ModMaterialProperties.class, String.class };
		final int enumId = ModMaterial.values().length;

		return EnumHelper.addEnum(ModMaterial.class, name, parameterTypes, enumId, properties, assetsModId);
	}

	/**
	 * Adds a {@link cadiboo.wiptech.util.ModEnums.ModMaterial material} with the active Mod's Id as the assetsModId
	 * 
	 * @param name       the name for the new material
	 * @param properties the {@link cadiboo.wiptech.material.ModMaterialProperties properties} of the material
	 * @return the new material
	 */
	public static ModMaterial addMaterial(final String name, final ModMaterialProperties properties) {
		final String assetsModId = Loader.instance().activeModContainer().getModId();
		return addMaterial(name, properties, assetsModId);
	}

	/**
	 * Adds a {@link cadiboo.wiptech.util.ModEnums.CircuitType circuit type} to WIPTech that will automatically have an item created for it
	 * 
	 * @param name                          the name for the new {@link CircuitType circuit type}
	 * @param maxShots                      the maximum amount of shots that can be taken (useful for burst circuits)
	 * @param shootIntervalTimeMilliseconds how long between shots
	 * @param usePhases                     when the circuit can shoot
	 * @param assetsModId                   the Mod Id that assets (textures, models) will be loaded from.
	 * @return the new circuit type
	 */
	public static CircuitType addCircuit(final String name, final int maxShots, final int shootIntervalTimeMilliseconds, final UsePhase[] usePhases, final String assetsModId) {
		final Class<?>[] parameterTypes = new Class[] { int.class, int.class, int.class, usePhases.getClass() };
		final int enumId = ModMaterial.values().length;

		return EnumHelper.addEnum(CircuitType.class, name, parameterTypes, enumId, maxShots, shootIntervalTimeMilliseconds, usePhases);
	}

	/**
	 * Adds a {@link cadiboo.wiptech.util.ModEnums.CircuitType circuitType} to WIPTech that will automatically have an item created for it with the active Mod's Id as the assetsModId
	 * 
	 * @param name                          the name for the new {@link CircuitType circuit type}
	 * @param maxShots                      the maximum amount of shots that can be taken (useful for burst circuits)
	 * @param shootIntervalTimeMilliseconds how long between shots
	 * @param usePhases                     when the circuit can shoot
	 * @return the new circuit type
	 */
	public static CircuitType addCircuit(final String name, final int maxShots, final int shootIntervalTimeMilliseconds, final UsePhase[] usePhases) {
		final String assetsModId = Loader.instance().activeModContainer().getModId();
		return addCircuit(name, maxShots, shootIntervalTimeMilliseconds, usePhases, assetsModId);
	}

}
