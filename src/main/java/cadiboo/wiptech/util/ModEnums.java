package cadiboo.wiptech.util;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import cadiboo.wiptech.item.ItemCircuit;
import cadiboo.wiptech.item.ItemScope;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocation;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Holds all enums and enum-related stuff for this mod
 * @author Cadiboo
 */
public final class ModEnums {

	/**
	 * provides some default methods for formatting enum names
	 * @author Cadiboo
	 */
	public interface IEnumNameFormattable {

		/**
		 * Converts the name to lowercase as per {@link java.lang.String#toLowerCase() String.toLowerCase}.
		 */
		default String getNameLowercase() {
			return this.name().toLowerCase();
		}

		/**
		 * Converts the name to uppercase as per {@link java.lang.String#toUpperCase() String.toUpperCase}.
		 */
		default String getNameUppercase() {
			return this.getNameLowercase().toUpperCase();
		}

		/**
		 * Capitalizes the name of the material as per {@link org.apache.commons.lang3.StringUtils#capitalize(String) StringUtils.capitalize}.
		 */
		default String getNameFormatted() {
			return StringUtils.capitalize(this.getNameLowercase());
		}

		// HACK
		String name(); /* not exactly hacky, but this method is provided by enum */

	}

	public static enum BlockItemType implements IEnumNameFormattable {

		RESOURCE(0), RESOURCE_PIECE(1);

		private final int id;

		BlockItemType(final int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public static BlockItemType byId(final int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}

	}

	public static enum SlugCasingPart implements IEnumNameFormattable, IStringSerializable {

		BACK(0),

		TOP(1),

		BOTTOM(2);

		private final int id;

		SlugCasingPart(final int id) {
			this.id = id;
		}

		@Override
		public String getName() {
			return this.getNameFormatted();
		}

		public int getId() {
			return this.id;
		}

		public static SlugCasingPart byId(final int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}

	}

	public static enum AttachmentPoint implements IEnumNameFormattable {
		SCOPE(0), SIDE_LEFT(1), SIDE_RIGHT(2), CIRCUIT(3), RAIL(4), COIL(5), SILENCER(6), UNDER(7);

		final int id;

		private AttachmentPoint(final int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public static AttachmentPoint byId(final int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}

		public String getUnlocalizedName() {
			return this.getNameLowercase();
		}

	}

	public static enum CircuitType implements IEnumNameFormattable {

		SEMI_AUTO(0, 1, 0, new UsePhase[]{UsePhase.END}),

		AUTO(1, Integer.MAX_VALUE, 50, new UsePhase[]{UsePhase.TICK, UsePhase.END}),

		BURST3(2, 3, 100, new UsePhase[]{UsePhase.TICK, UsePhase.END}),

		BURST5(3, 5, 75, new UsePhase[]{UsePhase.TICK, UsePhase.END}),

		BURST10(3, 10, 50, new UsePhase[]{UsePhase.TICK, UsePhase.END});

		private final int id;
		private final int maxShots;
		private final int shootInterval;
		private final List<UsePhase> usePhases;
		private final String assetsModId;

		private CircuitType(final int id, final int maxShots, final int shootIntervalTimeMilliseconds, final UsePhase[] usePhases) {
			this(id, maxShots, shootIntervalTimeMilliseconds, usePhases, ModReference.MOD_ID);
		}

		private CircuitType(final int id, final int maxShots, final int shootIntervalTimeMilliseconds, final UsePhase[] usePhases, final String assetsModId) {
			this.id = id;
			this.maxShots = maxShots;
			this.shootInterval = shootIntervalTimeMilliseconds;
			this.usePhases = Arrays.asList(usePhases);
			this.assetsModId = assetsModId;
		}

		public int getId() {
			return this.id;
		}

		public int getMaxShots() {
			return this.maxShots;
		}

		public int getShootInterval() {
			return this.shootInterval;
		}

		public List<UsePhase> getUsePhases() {
			return this.usePhases;
		}

		public String getAssetsModId() {
			return this.assetsModId;
		}

		public static CircuitType byId(final int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}

		@Nonnull
		public ItemCircuit getItem(final String suffix) {
			return (ItemCircuit) ForgeRegistries.ITEMS.getValue(new ModResourceLocation(ModReference.MOD_ID, this.getNameLowercase() + "_" + suffix));
		}

	}

	public static enum ScopeType implements IEnumNameFormattable {

		ACOG(0), HOLOGRAPHIC(1), MARS(2), RED_DOT(3), REFLEX(4), SUSAT(5), SNIPER(6), TELESCOPIC(7), THERMAL(8);

		final int id;

		private ScopeType(final int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public static ScopeType byId(final int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}

		@Nonnull
		public ItemScope getItem(final String suffix) {
			return (ItemScope) ForgeRegistries.ITEMS.getValue(new ModResourceLocation(ModReference.MOD_ID, this.getNameLowercase() + "_" + suffix));
		}

	}

	public static enum UsePhase {
		START, TICK, END
	}

}
