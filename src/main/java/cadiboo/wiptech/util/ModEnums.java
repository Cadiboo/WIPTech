package cadiboo.wiptech.util;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import cadiboo.wiptech.block.BlockEnamel;
import cadiboo.wiptech.block.BlockItem;
import cadiboo.wiptech.block.BlockModOre;
import cadiboo.wiptech.block.BlockResource;
import cadiboo.wiptech.block.BlockSpool;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.item.ItemCasedSlug;
import cadiboo.wiptech.item.ItemCircuit;
import cadiboo.wiptech.item.ItemCoil;
import cadiboo.wiptech.item.ItemModArmor;
import cadiboo.wiptech.item.ItemModAxe;
import cadiboo.wiptech.item.ItemModHoe;
import cadiboo.wiptech.item.ItemModHorseArmor;
import cadiboo.wiptech.item.ItemModPickaxe;
import cadiboo.wiptech.item.ItemModShovel;
import cadiboo.wiptech.item.ItemModSword;
import cadiboo.wiptech.item.ItemRail;
import cadiboo.wiptech.item.ItemScope;
import cadiboo.wiptech.item.ItemSlug;
import net.minecraft.block.material.Material;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

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

	/**
	 * MOHS Hardness from <a href= "https://en.wikipedia.org/wiki/Mohs_scale_of_mineral_hardness">Wikipedia</a> and <a href= "http://periodictable.com/Properties/A/MohsHardness.v.html">Periodictable</a>
	 * @author Cadiboo
	 */
	public static enum ModMaterials implements IEnumNameFormattable {

		/* ore block ingot armor tools hard cond */
		URANIUM(0, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 6.00f, 24)),

		TUNGSTEN(1, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 7.50f, 173)),

		TUNGSTEN_CARBITE(2, ModMaterialTypes.METAL, new ModMaterialProperties(false, true, true, true, true, 9.00f, 173)),

		TITANIUM(3, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 6.00f, 23)),

		TIN(4, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 1.50f, 68)),

		THORIUM(5, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 3.00f, 42)),

		SILVER(6, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 2.50f, 407)),

		PLATINUM(7, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 3.50f, 73)),

		PLUTONIUM(8, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 0.01f, 8)),

		OSMIUM(9, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 7.00f, 61)),

		NICKEL(10, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 4.00f, 90)),

		STEEL(11, ModMaterialTypes.METAL, new ModMaterialProperties(false, true, true, true, true, 4.50f, 54)),

		ALUMINIUM(12, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 2.75f, 204)),

		COPPER(13, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 3.00f, 386)),

		GOLD(14, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 2.50f, 315)),

		IRON(15, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 4.00f, 73)),

		LEAD(16, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 1.50f, 35)),

		GALLIUM(17, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 1.50f, 29)),

		BAUXITE(18, ModMaterialTypes.METAL, new ModMaterialProperties(true, false, false, false, false, ModMaterials.ALUMINIUM.getProperties().getHardness(), 0)),

		APATITE(19, ModMaterialTypes.GEM, new ModMaterialProperties(true, true, true, true, true, 4.50f, 0)),

		GLITCH(19, ModMaterialTypes.METAL, new ModMaterialProperties(true, true, true, true, true, 20.0f, 1000));

		private final int					id;
		private final ModMaterialTypes		type;
		private final ModMaterialProperties	properties;
		private final ArmorMaterial			armorMaterial;
		private final ToolMaterial			toolMaterial;
		private final HorseArmorType		horseArmorType;
		private final String				assetsModId;

		private ModMaterials(final int id, final ModMaterialTypes type, final ModMaterialProperties properties) {
			this(id, type, properties, ModReference.MOD_ID);
		}

		ModMaterials(final int id, final ModMaterialTypes type, final ModMaterialProperties properties, final String assetsModId) {
			this.id = id;
			this.type = type;
			this.properties = properties;
			this.armorMaterial = this.generateArmorMaterial();
			this.toolMaterial = this.generateToolMaterial();
			this.horseArmorType = this.generateHorseArmorType();
			this.assetsModId = assetsModId;
		}

		public int getId() {
			return this.id;
		}

		public ModMaterialTypes getType() {
			return this.type;
		}

		public ModMaterialProperties getProperties() {
			return this.properties;
		}

		public String getAssetsModId() {
			return this.assetsModId;
		}

		public ArmorMaterial getArmorMaterial() {
			return this.armorMaterial;
		}

		public ToolMaterial getToolMaterial() {
			return this.toolMaterial;
		}

		public HorseArmorType getHorseArmorType() {
			return this.horseArmorType;
		}

		private ToolMaterial generateToolMaterial() {
			if (!this.getProperties().hasTools()) {
				return null;
			} else {
				final String name = this.getNameUppercase();
				final int harvestLevel = Math.min(3, Math.round(this.getProperties().getHardness() / 3f));
				final int maxUses = (int) Math.ceil(this.getProperties().getHardness() * 150f);
				final float efficiency = this.getProperties().getHardness();
				final float damageVsEntity = this.getProperties().getHardness();
				final int enchantability = (int) Math.ceil(this.getProperties().getConductivity() / 10f);

				final ToolMaterial toolMaterial = EnumHelper.addToolMaterial(name, harvestLevel, maxUses, efficiency, damageVsEntity, enchantability);
				return toolMaterial;
			}
		}

		private ArmorMaterial generateArmorMaterial() {
			if (!this.getProperties().hasArmor()) {
				return null;
			} else {
				final String name = this.getNameUppercase();

				final String textureName = new ModResourceLocation(this.getResouceLocationDomain("helmet", ForgeRegistries.ITEMS), this.getNameLowercase()).toString();

				final int durability = (int) Math.ceil(this.getProperties().getHardness() * ModReference.ARMOR_MATERIAL_HARDNESS_MULTIPLIER);

				final int[] reductionAmounts = new int[4];
				Arrays.fill(reductionAmounts, (int) Math.ceil(this.getProperties().getHardness() / 2f));

				final int enchantability = (int) Math.ceil(this.getProperties().getConductivity() / 10f);

				final SoundEvent soundOnEquip = SoundEvents.ITEM_ARMOR_EQUIP_IRON;

				final float toughness = (int) Math.ceil(this.getProperties().getHardness() / 5f);

				final ArmorMaterial armorMaterial = EnumHelper.addArmorMaterial(name, textureName, durability, reductionAmounts, enchantability, soundOnEquip, toughness);
				// TODO TEST THIS!!
				armorMaterial.setRepairItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ModResourceLocation(this.getResouceLocationDomain("helmet", ForgeRegistries.ITEMS), this.getNameLowercase()))));
				return armorMaterial;

			}
		}

		private HorseArmorType generateHorseArmorType() {
			if (!this.getProperties().hasArmor()) {
				return HorseArmorType.NONE;
			} else {
				final String name = this.getNameUppercase();

				final String textureLocation = new ModResourceLocation(this.getResouceLocationDomain("horse_armor", ForgeRegistries.ITEMS), "textures/entity/horse/armor/horse_armor_" + this.getNameLowercase()).toString() + ".png";

				final int armorStrength = (int) Math.ceil(this.getProperties().getHardness());

				return EnumHelper.addHorseArmor(name, textureLocation, armorStrength);
			}
		}

		public String getResouceLocationDomain(final String nameSuffix, final IForgeRegistry registry) {
			for (final ModContainer mod : Loader.instance().getActiveModList()) {
				if (!mod.getModId().equals(ModReference.MOD_ID)) {
					if (registry.containsKey(new ModResourceLocation(mod.getModId(), this.getVanillaNameLowercase(nameSuffix) + "_" + nameSuffix))) {
						return mod.getModId();
					}
				}
			}
			return ModReference.MOD_ID;
		}

		public String getVanillaNameLowercase(final String suffix) {

			// if (suffix.toLowerCase().equals("horse_armor") && this.getNameLowercase().equals("iron")) {
			// return "metal_horse_armor";
			// }

			switch (suffix.toLowerCase()) {
			case "sword":
			case "shovel":
			case "pickaxe":
			case "axe":
			case "hoe":
			case "helmet":
			case "chestplate":
			case "leggings":
			case "boots":
			case "apple":
			case "carrot":
			case "horse_armor":
				return this.getNameLowercase() + (this.getNameLowercase().contains("gold") ? "en" : "");
			default:
				return this.getNameLowercase();
			}

		}

		public String getVanillaNameUppercase(final String suffix) {
			return this.getVanillaNameLowercase(suffix).toUpperCase();
		}

		public String getVanillaNameFormatted(final String suffix) {
			return StringUtils.capitalize(this.getVanillaNameLowercase(suffix));
		}

		@Nullable
		public BlockModOre getOre() {
			if (!this.getProperties().hasOre()) {
				return null;
			}
			return (BlockModOre) this.getRegistryValue(ForgeRegistries.BLOCKS, "ore");
		}

		@Nullable
		public BlockResource getBlock() {
			if (!this.getProperties().hasBlock()) {
				return null;
			}
			return (BlockResource) this.getRegistryValue(ForgeRegistries.BLOCKS, "block");
		}

		@Nullable
		public BlockItem getResource() {
			if (!this.getProperties().hasResource()) {
				return null;
			}
			return (BlockItem) this.getRegistryValue(ForgeRegistries.BLOCKS, this.getType().getResourceNameSuffix());
		}

		@Nullable
		public BlockItem getResourcePiece() {
			if ((!this.getProperties().hasResource()) || (!this.getType().hasResourcePiece())) {
				return null;
			} else {
				return (BlockItem) this.getRegistryValue(ForgeRegistries.BLOCKS, this.getType().getResourcePieceNameSuffix());
			}
		}

		@Nullable
		public BlockWire getWire() {
			if (!this.getProperties().hasWire()) {
				return null;
			}
			return (BlockWire) this.getRegistryValue(ForgeRegistries.BLOCKS, "wire");
		}

		@Nullable
		public BlockSpool getSpool() {
			if (!this.getProperties().hasWire()) {
				return null;
			}
			return (BlockSpool) this.getRegistryValue(ForgeRegistries.BLOCKS, "spool");
		}

		@Nullable
		public BlockEnamel getEnamel() {
			if (!this.getProperties().hasEnamel()) {
				return null;
			}
			return (BlockEnamel) this.getRegistryValue(ForgeRegistries.BLOCKS, "enamel");
		}

		@Nullable
		public ItemModArmor getHelmet() {
			if (!this.getProperties().hasArmor()) {
				return null;
			}
			return (ItemModArmor) this.getRegistryValue(ForgeRegistries.ITEMS, "helmet");
		}

		@Nullable
		public ItemModArmor getChestplate() {
			if (!this.getProperties().hasArmor()) {
				return null;
			}
			return (ItemModArmor) this.getRegistryValue(ForgeRegistries.ITEMS, "chestplate");
		}

		@Nullable
		public ItemModArmor getLeggings() {
			if (!this.getProperties().hasArmor()) {
				return null;
			}
			return (ItemModArmor) this.getRegistryValue(ForgeRegistries.ITEMS, "leggings");
		}

		@Nullable
		public ItemModArmor getBoots() {
			if (!this.getProperties().hasArmor()) {
				return null;
			}
			return (ItemModArmor) this.getRegistryValue(ForgeRegistries.ITEMS, "boots");
		}

		public ItemModHorseArmor getHorseArmor() {
			if (!this.getProperties().hasArmor()) {
				return null;
			}
			return (ItemModHorseArmor) this.getRegistryValue(ForgeRegistries.ITEMS, "horse_armor");
		}

		@Nullable
		public ItemModPickaxe getPickaxe() {
			if (!this.getProperties().hasTools()) {
				return null;
			}
			return (ItemModPickaxe) this.getRegistryValue(ForgeRegistries.ITEMS, "pickaxe");
		}

		@Nullable
		public ItemModAxe getAxe() {
			if (!this.getProperties().hasTools()) {
				return null;
			}
			return (ItemModAxe) this.getRegistryValue(ForgeRegistries.ITEMS, "axe");
		}

		@Nullable
		public ItemModSword getSword() {
			if (!this.getProperties().hasTools()) {
				return null;
			}
			return (ItemModSword) this.getRegistryValue(ForgeRegistries.ITEMS, "sword");
		}

		@Nullable
		public ItemModShovel getShovel() {
			if (!this.getProperties().hasTools()) {
				return null;
			}
			return (ItemModShovel) this.getRegistryValue(ForgeRegistries.ITEMS, "shovel");
		}

		@Nullable
		public ItemModHoe getHoe() {
			if (!this.getProperties().hasTools()) {
				return null;
			}
			return (ItemModHoe) this.getRegistryValue(ForgeRegistries.ITEMS, "hoe");
		}

		@Nullable
		public ItemCoil getCoil() {
			if (!this.getProperties().hasCoil()) {
				return null;
			}
			return (ItemCoil) this.getRegistryValue(ForgeRegistries.ITEMS, "coil");
		}

		@Nullable
		public ItemRail getRail() {
			if (!this.getProperties().hasRail()) {
				return null;
			}
			return (ItemRail) this.getRegistryValue(ForgeRegistries.ITEMS, "rail");
		}

		@Nullable
		public EntityEntry getSlugEntity() {
			if (!this.getProperties().hasRailgunSlug()) {
				return null;
			}
			return this.getRegistryValue(ForgeRegistries.ENTITIES, "slug");
		}

		@Nullable
		public ItemSlug getSlugItem() {
			if (!this.getProperties().hasRailgunSlug()) {
				return null;
			}
			return (ItemSlug) this.getRegistryValue(ForgeRegistries.ITEMS, "slug");
		}

		@Nullable
		public ItemCasedSlug getCasedSlug() {
			if (!this.getProperties().hasRailgunSlug()) {
				return null;
			}
			return (ItemCasedSlug) ForgeRegistries.ITEMS.getValue(new ModResourceLocation(ModReference.MOD_ID, "cased_" + this.getNameLowercase() + "_" + "slug"));
		}

		@Nullable
		private <T> T getRegistryValue(@Nonnull final IForgeRegistry<? extends IForgeRegistryEntry<T>> registry, @Nonnull final String nameSuffix) {
			return getRegistryValue(registry, this, nameSuffix);
		}

		@Nullable
		public static <T> T getRegistryValue(@Nonnull final IForgeRegistry<? extends IForgeRegistryEntry<T>> registry, @Nonnull final ModMaterials material, @Nonnull String nameSuffix) {
			nameSuffix = nameSuffix.toLowerCase();
			return (T) registry.getValue(new ModResourceLocation(material.getResouceLocationDomain(nameSuffix, registry), material.getVanillaNameLowercase(nameSuffix) + "_" + nameSuffix));
		}

		public static float getHighestHardness() {
			float ret = 0;
			for (final ModMaterials material : ModMaterials.values()) {
				if (material.getProperties().getHardness() > ret) {
					ret = material.getProperties().getHardness();
				}
			}
			return ret;
		}

		public static double getHighestConductivity() {
			float ret = 0;
			for (final ModMaterials material : ModMaterials.values()) {
				if (material == material.GLITCH) {
					continue;
				}
				if (material.getProperties().getConductivity() > ret) {
					ret = material.getProperties().getConductivity();
				}
			}
			return ret;
		}

		public Material getVanillaMaterial() {
			return Material.IRON;
		}

		public static ModMaterials byId(final int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}

	}

	public static enum ModMaterialTypes implements IEnumNameFormattable {

		METAL(0), GEM(1);

		private final int id;

		ModMaterialTypes(final int id) {
			this.id = id;
		}

		public String getResourceNameSuffix() {
			return this.getResourceBlockItemType().getNameLowercase();
		}

		public String getResourcePieceNameSuffix() {
			final BlockItemTypes type = this.getResourcePieceBlockItemType();
			if (type == null) {
				return "";
			} else {
				return this.getResourcePieceBlockItemType().getNameLowercase();
			}
		}

		public int getId() {
			return this.id;
		}

		public static ModMaterialTypes byId(final int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}

		public boolean hasResourcePiece() {
			boolean hasResourcePiece;
			switch (byId(this.getId())) {
			case GEM:
				hasResourcePiece = false;
				break;
			default:
			case METAL:
				hasResourcePiece = true;
				break;
			}
			return hasResourcePiece;
		}

		public BlockItemTypes getResourceBlockItemType() {
			BlockItemTypes resourceBlockItemType;
			switch (byId(this.getId())) {
			case GEM:
				resourceBlockItemType = BlockItemTypes.GEM;
				break;
			default:
			case METAL:
				resourceBlockItemType = BlockItemTypes.INGOT;
				break;
			}
			return resourceBlockItemType;
		}

		public BlockItemTypes getResourcePieceBlockItemType() {
			BlockItemTypes resourcePieceBlockItemType;
			switch (byId(this.getId())) {
			case GEM:
				resourcePieceBlockItemType = null;
				break;
			default:
			case METAL:
				resourcePieceBlockItemType = BlockItemTypes.NUGGET;
				break;
			}
			return resourcePieceBlockItemType;
		}
	}

	public static enum ResourcePieceTypes implements IEnumNameFormattable {

		NUGGET(0), FLAKE(1);

		private final int id;

		ResourcePieceTypes(final int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public static ResourcePieceTypes byId(final int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}
	}

	public static enum BlockItemTypes implements IEnumNameFormattable {

		INGOT(0), NUGGET(1), GEM(2);

		private static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 0.2, 0.8);

		private static final AxisAlignedBB	INGOT_EW_AABB	= new AxisAlignedBB(3d / 16d, 0, 6d / 16d, 1d - (3d / 16d), 3d / 16d, 1d - (6d / 16d));
		private static final AxisAlignedBB	INGOT_NS_AABB	= new AxisAlignedBB(6d / 16d, 0, 3d / 16d, 1d - (6d / 16d), 3d / 16d, 1d - (3d / 16d));

		private static final AxisAlignedBB	NUGGET_EW_AABB	= new AxisAlignedBB(4d / 16d, 0, 5d / 16d, 1d - (4d / 16d), 1d / 16d, 1d - (5d / 16d));
		private static final AxisAlignedBB	NUGGET_NS_AABB	= new AxisAlignedBB(5d / 16d, 0, 4d / 16d, 1d - (5d / 16d), 1d / 16d, 1d - (4d / 16d));

		private final int id;

		BlockItemTypes(final int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public AxisAlignedBB getBoundingBox(final EnumFacing facing) {

			switch (byId(this.getId())) {
			case INGOT:
				switch (facing) {
				case EAST:
				case WEST:
					return INGOT_EW_AABB;
				case NORTH:
				case SOUTH:
					return INGOT_NS_AABB;
				default:
					return DEFAULT_AABB;
				}
			case NUGGET:
				switch (facing) {
				case EAST:
				case WEST:
					return NUGGET_EW_AABB;
				case NORTH:
				case SOUTH:
					return NUGGET_NS_AABB;
				default:
					return DEFAULT_AABB;
				}
			case GEM:
				return DEFAULT_AABB;
			default:
				return DEFAULT_AABB;

			}
		}

		public static BlockItemTypes byId(final int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}

	}

	public static enum SlugCasingParts implements IEnumNameFormattable, IStringSerializable {

		BACK(0),

		TOP(1),

		BOTTOM(2);

		private final int id;

		SlugCasingParts(final int id) {
			this.id = id;
		}

		@Override
		public String getName() {
			return this.getNameFormatted();
		}

		public int getId() {
			return this.id;
		}

		public static SlugCasingParts byId(final int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}

	}

	public static enum AttachmentPoints implements IEnumNameFormattable {
		SCOPE(0), SIDE_LEFT(1), SIDE_RIGHT(2), CIRCUIT(3), RAIL(4), COIL(5), SILENCER(6), UNDER(7);

		final int id;

		private AttachmentPoints(final int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public static AttachmentPoints byId(final int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}

		public String getUnlocalizedName() {
			return this.getNameLowercase();
		}

	}

	public static enum CircuitTypes implements IEnumNameFormattable {

		MANUAL(0, 1), AUTO(1, Integer.MAX_VALUE), BURST3(2, 3), BURST5(3, 5);

		final int				id;
		final int				maxShots;
		final List<UsePhases>	usePhases;

		private CircuitTypes(final int id, final int maxShots, final UsePhases... usePhases) {
			this.id = id;
			this.maxShots = maxShots;
			this.usePhases = Arrays.asList(usePhases);
		}

		public int getId() {
			return this.id;
		}

		public int getMaxShots() {
			return this.maxShots;
		}

		public static CircuitTypes byId(final int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}

		@Nonnull
		public ItemCircuit getItem(final String suffix) {
			return (ItemCircuit) ForgeRegistries.ITEMS.getValue(new ModResourceLocation(ModReference.MOD_ID, this.getNameLowercase() + "_" + suffix));
		}

		public List<UsePhases> getUsePhases() {
			return this.usePhases;
		}

	}

	public static enum ScopeTypes implements IEnumNameFormattable {

		ACOG(0), HOLOGRAPHIC(1), MARS(2), RED_DOT(3), REFLEX(4), SUSAT(5), SNIPER(6), TELESCOPIC(7), THERMAL(8);

		final int id;

		private ScopeTypes(final int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public static ScopeTypes byId(final int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}

		@Nonnull
		public ItemScope getItem(final String suffix) {
			return (ItemScope) ForgeRegistries.ITEMS.getValue(new ModResourceLocation(ModReference.MOD_ID, this.getNameLowercase() + "_" + suffix));
		}

	}

	public static enum UsePhases {
		START, TICK, END
	}

}
