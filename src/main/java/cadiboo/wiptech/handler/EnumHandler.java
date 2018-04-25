package cadiboo.wiptech.handler;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

public class EnumHandler {

	public static enum BlockItems implements IStringSerializable {

		COPPER_INGOT(0, "copper_ingot"),

		TIN_INGOT(1, "tin_ingot"),

		ALUMINIUM_INGOT(2, "aluminium_ingot"),

		SILVER_INGOT(3, "silver_ingot"),

		GOLD_INGOT(4, "osmium_ingot"),

		IRON_INGOT(5, "tungsten_ingot"),

		COPPER_NUGGET(6, "copper_nugget"),

		TIN_NUGGET(7, "tin_nugget"),

		ALUMINIUM_NUGGET(8, "aluminium_nugget"),

		SILVER_NUGGET(9, "silver_nugget"),

		GOLD_NUGGET(10, "osmium_nugget"),

		IRON_NUGGET(11, "tungsten_nugget");

		private int		ID;
		private String	name;

		private BlockItems(int id, String name) {
			this.ID = id;
			this.name = name;
		}

		public static BlockItems byID(int i) {
			return BlockItems.values()[i];
		}

		@Override
		public String getName() {
			return this.name;
		}

		public int getID() {
			return this.ID;
		}

	}

	public static enum ResourceBlocks implements IStringSerializable {

		COPPER(0, "copper"),

		TIN(1, "tin"),

		ALUMINIUM(2, "aluminium"),

		SILVER(3, "silver"),

		OSMIUM(4, "osmium"),

		TUNGSTEN(5, "tungsten"),

		TITANIUM(6, "titanium");

		private int		ID;
		private String	name;

		private ResourceBlocks(int id, String name) {
			this.ID = id;
			this.name = name;
		}

		public static ResourceBlocks byID(int i) {
			return ResourceBlocks.values()[i];
		}

		@Override
		public String getName() {
			return this.name;
		}

		public int getID() {
			return this.ID;
		}

	}

	public static enum Ores implements IStringSerializable {

		COPPER(0, "copper"),

		TIN(1, "tin"),

		ALUMINIUM(2, "aluminium"),

		SILVER(3, "silver"),

		OSMIUM(4, "osmium"),

		TUNGSTEN(5, "tungsten"),

		BAUXITE(6, "bauxite");

		private int		ID;
		private String	name;

		private Ores(int id, String name) {
			this.ID = id;
			this.name = name;
		}

		public static Ores byID(int i) {
			return Ores.values()[i];
		}

		@Override
		public String getName() {
			return this.name;
		}

		public int getID() {
			return this.ID;
		}

	}

	public static enum IngredientBlocks implements IStringSerializable {

		COPPER(0, "copper"),

		TIN(1, "tin"),

		ALUMINIUM(2, "aluminium"),

		SILVER(3, "silver"),

		GOLD(4, "osmium"),

		IRON(5, "tungsten");

		private int		ID;
		private String	name;

		private IngredientBlocks(int id, String name) {
			this.ID = id;
			this.name = name;
		}

		public static IngredientBlocks byID(int i) {
			return IngredientBlocks.values()[i];
		}

		@Override
		public String getName() {
			return this.name;
		}

		public int getID() {
			return this.ID;
		}

	}

	public static enum ConductiveMetals implements IStringSerializable {

		// Material IACS (International Annealed Copper Standard)
		// Ranking Metal % Conductivity*
		// 1 Silver (Pure) 105%
		// 2 Copper 100%
		// 3 Gold (Pure) 70%
		// 4 Aluminum 61%
		// 5 Brass 28%
		// 6 Zinc 27%
		// 7 Nickel 22%
		// 8 Iron (Pure) 17%
		// 9 Tin 15%
		// 10 Phosphor Bronze 15%
		// 11 Steel (Stainless included) 3-15%
		// 12 Lead (Pure) 7%
		// 13 Nickel Aluminum Bronze 7%
		//
		// TIN (0, "tin", 15, TextFormatting.WHITE),
		// IRON (1, "iron", 17, TextFormatting.GRAY),
		// ALUMINIUM (2, "aluminium", 61, TextFormatting.GRAY),
		// GOLD (3, "gold", 70, TextFormatting.GOLD),
		// COPPER (4, "copper", 100, TextFormatting.RED),
		// SILVER (5, "silver", 105, TextFormatting.WHITE);

		TIN(0, "tin", 15, TextFormatting.WHITE),

		IRON(1, "iron", 17, TextFormatting.GRAY),

		ALUMINIUM(2, "aluminium", 61, TextFormatting.GRAY),

		GOLD(3, "gold", 70, TextFormatting.GOLD),

		COPPER(4, "copper", 100, TextFormatting.RED),

		SILVER(5, "silver", 105, TextFormatting.WHITE);

		private int				ID;
		private String			name;
		private TextFormatting	chatColor;
		private int				conductivity;

		private ConductiveMetals(int ID, String name, int conductivity, TextFormatting chatColorIn) {
			this.ID = ID;
			this.name = name;
			this.conductivity = conductivity;
			this.chatColor = chatColorIn;
		}

		@Override
		public String getName() {
			return this.name;
		}

		public int getID() {
			return this.ID;
		}

		public static ConductiveMetals byID(int i) {
			return ConductiveMetals.values()[i];
		}

		public float getConductivityFraction() {
			return getConductivityPercentage() / 100F;
		}

		public int getConductivityPercentage() {
			return this.conductivity;
		}
	}

	public static final class WeaponModules {

		public static enum Rails implements IStringSerializable, IWeaponModule {

			/*
			 * Material IACS (International Annealed Copper Standard) Ranking Metal %
			 * Conductivity* 1 Silver (Pure) 105% 2 Copper 100% 3 Gold (Pure) 70% 4 Aluminum
			 * 61% 5 Brass 28% 6 Zinc 27% 7 Nickel 22% 8 Iron (Pure) 17% 9 Tin 15% 10
			 * Phosphor Bronze 15% 11 Steel (Stainless included) 3-15% 12 Lead (Pure) 7% 13
			 * Nickel Aluminum Bronze 7%
			 */

			TIN(0, "tin", 15, TextFormatting.WHITE), IRON(1, "iron", 17, TextFormatting.GRAY), ALUMINIUM(2, "aluminium", 61, TextFormatting.GRAY), GOLD(3, "gold", 70, TextFormatting.GOLD), COPPER(4,
					"copper", 100, TextFormatting.RED), SILVER(5, "silver", 105, TextFormatting.WHITE);

			private int				ID;
			private String			name;
			private TextFormatting	chatColor;
			private int				efficiency;

			private Rails(int ID, String name, int efficiency, TextFormatting chatColorIn) {
				this.ID = ID;
				this.name = name;
				this.efficiency = efficiency;
				this.chatColor = chatColorIn;
			}

			@Override
			public String getName() {
				return this.name;
			}

			@Override
			public int getID() {
				return ID;
			}

			public static Rails byID(int i) {
				return Rails.values()[i];
			}

			public float getEfficiencyFraction() {
				return getEfficiencyPercentage() / 100F;
			}

			public int getEfficiencyPercentage() {
				return this.efficiency;
			}
		}

		public static enum Circuits implements IStringSerializable, IWeaponModule {
			MANUAL(0, "manual", TextFormatting.WHITE), BURST3(1, "burst3", TextFormatting.RED), BURST5(2, "burst5", TextFormatting.RED), BURST10(3, "burst10", TextFormatting.RED), AUTO(4, "auto",
					TextFormatting.DARK_RED), OVERCLOCKED(5, "overclocked", TextFormatting.AQUA);

			private int				ID;
			private String			name;
			private TextFormatting	chatColor;

			private Circuits(int ID, String name, TextFormatting chatColorIn) {
				this.ID = ID;
				this.name = name;
				this.chatColor = chatColorIn;
			}

			@Override
			public String getName() {
				return this.name;
			}

			@Override
			public int getID() {
				return ID;
			}

			public static Circuits byID(int i) {
				return Circuits.values()[i];
			}
		}

		public static enum Scopes implements IStringSerializable, IWeaponModule {
			ACOG(0, "acog", TextFormatting.WHITE), ZOOM(1, "zoom", TextFormatting.GRAY), LASER(2, "laser", TextFormatting.RED);

			private int				ID;
			private String			name;
			private TextFormatting	chatColor;

			private Scopes(int ID, String name, TextFormatting chatColorIn) {
				this.ID = ID;
				this.name = name;
				this.chatColor = chatColorIn;
			}

			@Override
			public String getName() {
				return this.name;
			}

			@Override
			public int getID() {
				return ID;
			}

			public static Scopes byID(int i) {
				return Scopes.values()[i];
			}
		}

		public static enum Coils implements IStringSerializable, IWeaponModule {

			/*
			 * Material IACS (International Annealed Copper Standard) Ranking Metal %
			 * Conductivity* 1 Silver (Pure) 105% 2 Copper 100% 3 Gold (Pure) 70% 4 Aluminum
			 * 61% 5 Brass 28% 6 Zinc 27% 7 Nickel 22% 8 Iron (Pure) 17% 9 Tin 15% 10
			 * Phosphor Bronze 15% 11 Steel (Stainless included) 3-15% 12 Lead (Pure) 7% 13
			 * Nickel Aluminum Bronze 7%
			 */

			TIN(0, "tin", 15, TextFormatting.WHITE), IRON(1, "iron", 17, TextFormatting.GRAY), ALUMINIUM(2, "aluminium", 61, TextFormatting.GRAY), GOLD(3, "gold", 70, TextFormatting.GOLD), COPPER(4,
					"copper", 100, TextFormatting.RED), SILVER(5, "silver", 105, TextFormatting.WHITE);

			private int				ID;
			private String			name;
			private TextFormatting	chatColor;
			private int				efficiency;

			private Coils(int ID, String name, int efficiency, TextFormatting chatColorIn) {
				this.ID = ID;
				this.name = name;
				this.efficiency = efficiency;
				this.chatColor = chatColorIn;
			}

			@Override
			public String getName() {
				return this.name;
			}

			@Override
			public int getID() {
				return ID;
			}

			public static Coils byID(int i) {
				Object coils = Coils.values();
				return Coils.values()[i];
			}

			public float getEfficiencyFraction() {
				return getEfficiencyPercentage() / 100F;
			}

			public int getEfficiencyPercentage() {
				return this.efficiency;
			}

		}

	}

	public static enum ToolTypes implements IStringSerializable {

		SHOVEL(0, "shovel"),

		PICKAXE(1, "pickaxe"),

		SWORD(2, "sword"),

		HOE(3, "hoe"),

		AXE(4, "axe");

		private int		ID;
		private String	name;

		private ToolTypes(int id, String name) {
			this.ID = id;
			this.name = name;
		}

		public static ToolTypes byID(int i) {
			return ToolTypes.values()[i];
		}

		@Override
		public String getName() {
			return this.name;
		}

		public int getID() {
			return this.ID;
		}

	}

	public static enum ParamagneticProjectiles implements IStringSerializable {
		IRON_LARGE(0, "iron_large", ParamagneticProjectileSizes.LARGE, 1, 1, 1, TextFormatting.WHITE),

		OSMIUM_LARGE(1, "osmium_large", ParamagneticProjectileSizes.LARGE, 1, 1, 1, TextFormatting.DARK_BLUE),

		TUNGSTEN_LARGE(2, "tungsten_large", ParamagneticProjectileSizes.LARGE, 1, 1, 1, TextFormatting.GRAY),

		IRON_MEDIUM(3, "iron_medium", ParamagneticProjectileSizes.MEDIUM, 1, 1, 1, TextFormatting.WHITE),

		OSMIUM_MEDIUM(4, "osmium_medium", ParamagneticProjectileSizes.MEDIUM, 1, 1, 1, TextFormatting.DARK_BLUE),

		TUNGSTEN_MEDIUM(5, "tungsten_medium", ParamagneticProjectileSizes.MEDIUM, 1, 1, 1, TextFormatting.GRAY),

		IRON_SMALL(6, "iron_small", ParamagneticProjectileSizes.SMALL, 1, 1, 1, TextFormatting.WHITE),

		OSMIUM_SMALL(7, "osmium_small", ParamagneticProjectileSizes.SMALL, 1, 1, 1, TextFormatting.DARK_BLUE),

		TUNGSTEN_SMALL(8, "tungsten_small", ParamagneticProjectileSizes.SMALL, 1, 1, 1, TextFormatting.GRAY),

		PLASMA(9, "plasma", ParamagneticProjectileSizes.NANO, 1, 1, 1, TextFormatting.GOLD);

		private int							ID;
		private String						name;
		private ParamagneticProjectileSizes	size;
		private float						damage;
		private float						velocity;
		private float						knockback;
		private TextFormatting				chatColor;

		private ParamagneticProjectiles(int ID, String name, ParamagneticProjectileSizes size, float damage, float velocity, float knockback, TextFormatting chatColorIn) {
			this.ID = ID;
			this.name = name;
			this.size = size;
			this.damage = damage;
			this.velocity = velocity;
			this.knockback = knockback;
			this.chatColor = chatColorIn;
		}

		@Override
		public String getName() {
			return this.name;
		}

		public int getID() {
			return this.ID;
		}

		public static ParamagneticProjectiles byID(int i) {
			return ParamagneticProjectiles.values()[i];
		}

		public float getVelocity() {
			return this.velocity;
		}

		public float getDamage() {
			return this.damage;
		}

		public float getKnockback() {
			return this.knockback;
		}

		public ParamagneticProjectileSizes getSize() {
			return this.size;
		}
	}

	public static enum ParamagneticProjectileSizes implements IStringSerializable {
		LARGE(0, "large"), MEDIUM(0, "medium"), SMALL(0, "small"), NANO(0, "nano");

		private int		ID;
		private String	name;

		private ParamagneticProjectileSizes(int ID, String name) {
			this.ID = ID;
			this.name = name;
		}

		public int getID() {
			return this.ID;
		}

		public static ParamagneticProjectiles byID(int i) {
			return ParamagneticProjectiles.values()[i];
		}

		@Override
		public String getName() {
			return this.getName();
		}
	}

}