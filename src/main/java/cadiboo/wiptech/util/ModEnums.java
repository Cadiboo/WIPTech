package cadiboo.wiptech.util;

import java.util.Arrays;

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
import cadiboo.wiptech.item.ItemCoil;
import cadiboo.wiptech.item.ItemModArmor;
import cadiboo.wiptech.item.ItemModAxe;
import cadiboo.wiptech.item.ItemModHoe;
import cadiboo.wiptech.item.ItemModPickaxe;
import cadiboo.wiptech.item.ItemModShovel;
import cadiboo.wiptech.item.ItemModSword;
import cadiboo.wiptech.item.ItemRail;
import cadiboo.wiptech.item.ItemSlug;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * 
 * @author Cadiboo
 *
 */

public class ModEnums {

	public interface IEnumNameFormattable {

		/**
		 * Converts the name to lowercase as per {@link java.lang.String#toLowerCase()
		 * String.toLowerCase}.
		 */
		public default String getNameLowercase() {
			return this.name().toLowerCase();
		}

		/**
		 * Converts the name to uppercase as per {@link java.lang.String#toUpperCase()
		 * String.toUpperCase}.
		 */
		public default String getNameUppercase() {
			return this.getNameLowercase().toUpperCase();
		}

		/**
		 * Capitalizes the name of the material as per
		 * {@link org.apache.commons.lang3.StringUtils#capitalize(String)
		 * StringUtils.capitalize}.
		 */
		public default String getNameFormatted() {
			return StringUtils.capitalize(this.getNameLowercase());
		}

		public String name(); /* not exactly hacky, but this method is provided by enum */

	}

	/**
	 * 
	 * 
	 * MOHS Hardness from <a href=
	 * "https://en.wikipedia.org/wiki/Mohs_scale_of_mineral_hardness">Wikipedia</a>
	 * and <a href=
	 * "http://periodictable.com/Properties/A/MohsHardness.v.html">Periodictable</a>
	 * 
	 * @author Cadiboo
	 *
	 */
	public static enum ModMaterials implements IEnumNameFormattable {

		/* ore block ingot armor tools hard cond */
		URANIUM(0, new ModMaterialProperties(true, true, true, true, true, 6.00f, 24)),

		TUNGSTEN(1, new ModMaterialProperties(true, true, true, true, true, 7.50f, 173)),

		TUNGSTEN_CARBITE(2, new ModMaterialProperties(false, true, true, true, true, 9.00f, 173)),

		TITANIUM(3, new ModMaterialProperties(true, true, true, true, true, 6.00f, 23)),

		TIN(4, new ModMaterialProperties(true, true, true, true, true, 1.50f, 68)),

		THORIUM(5, new ModMaterialProperties(true, true, true, true, true, 3.00f, 42)),

		SILVER(6, new ModMaterialProperties(true, true, true, true, true, 2.50f, 407)),

		PLATINUM(7, new ModMaterialProperties(true, true, true, true, true, 3.50f, 73)),

		PLUTONIUM(8, new ModMaterialProperties(true, true, true, true, true, 0.01f, 8)),

		OSMIUM(9, new ModMaterialProperties(true, true, true, true, true, 7.00f, 61)),

		NICKEL(10, new ModMaterialProperties(true, true, true, true, true, 4.00f, 90)),

		STEEL(11, new ModMaterialProperties(false, true, true, true, true, 4.50f, 54)),

		ALUMINIUM(12, new ModMaterialProperties(true, true, true, true, true, 2.75f, 204)),

		COPPER(13, new ModMaterialProperties(true, true, true, true, true, 3.00f, 386)),

		GOLD(14, new ModMaterialProperties(true, true, true, true, true, 2.50f, 315)),

		IRON(15, new ModMaterialProperties(true, true, true, true, true, 4.00f, 73)),

		LEAD(16, new ModMaterialProperties(true, true, true, true, true, 1.50f, 35)),

		GALLIUM(17, new ModMaterialProperties(true, true, true, true, true, 1.50f, 29)),

		BAUXITE(18, new ModMaterialProperties(true, false, false, false, false, ModMaterials.ALUMINIUM.getProperties().getHardness(), 0));

		private final int id;
		private final ModMaterialProperties properties;
		private final ArmorMaterial armorMaterial;
		private final ToolMaterial toolMaterial;

		ModMaterials(final int idIn, final ModMaterialProperties propertiesIn) {
			this.id = idIn;
			this.properties = propertiesIn;

			if (this.getProperties().hasArmor()) {
				String name = this.getNameUppercase();

				String textureName = new ResourceLocation(this.getResouceLocationDomain("helmet", ForgeRegistries.ITEMS), getNameLowercase()).toString();

				int durability = Math.round(this.getProperties().getHardness() * ModReference.ARMOR_MATERIAL_HARDNESS_MULTIPLIER);

				int[] reductionAmounts = new int[4];
				Arrays.fill(reductionAmounts, Math.round(this.getProperties().getHardness() / 2f));

				int enchantability = Math.round(this.getProperties().getConductivity() / 10f);

				SoundEvent soundOnEquip = SoundEvents.ITEM_ARMOR_EQUIP_IRON;

				float toughness = Math.round(this.getProperties().getHardness() / 5f);

				this.armorMaterial = EnumHelper.addArmorMaterial(name, textureName, durability, reductionAmounts, enchantability, soundOnEquip, toughness);
				this.armorMaterial.setRepairItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.getResouceLocationDomain("helmet", ForgeRegistries.ITEMS),
						getNameLowercase()))));
			} else
				this.armorMaterial = null;

			if (this.getProperties().hasTools()) {

				String name = this.getNameUppercase();
				int harvestLevel = Math.min(3, Math.round(this.getProperties().getHardness() / 3f));
				int maxUses = Math.round(this.getProperties().getHardness() * 150f);
				float efficiency = this.getProperties().getHardness();
				float damageVsEntity = this.getProperties().getHardness();
				int enchantability = Math.round(this.getProperties().getConductivity() / 10f);

				this.toolMaterial = EnumHelper.addToolMaterial(name, harvestLevel, maxUses, efficiency, damageVsEntity, enchantability);
			} else
				this.toolMaterial = null;
		}

		public final int getId() {
			return this.id;
		}

		public final ModMaterialProperties getProperties() {
			return properties;
		}

		public final ArmorMaterial getArmorMaterial() {
			return this.armorMaterial;
		}

		public final ToolMaterial getToolMaterial() {
			return this.toolMaterial;
		}

		public final String getResouceLocationDomain(String nameSuffix, IForgeRegistry registry) {
			for (ModContainer mod : Loader.instance().getActiveModList()) {
				if (!mod.getModId().equals(ModReference.MOD_ID))
					if (registry.containsKey(new ResourceLocation(mod.getModId(), getVanillaNameLowercase(nameSuffix) + "_" + nameSuffix)))
						return mod.getModId();
			}
			return ModReference.MOD_ID;
		}

		public String getVanillaNameLowercase(String suffix) {
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
				return getNameLowercase() + (getNameLowercase().contains("gold") ? "en" : "");
			default:
				return getNameLowercase();
			}

		}

		public String getVanillaNameUppercase(String suffix) {
			return getVanillaNameLowercase(suffix).toUpperCase();
		}

		public String getVanillaNameFormatted(String suffix) {
			return StringUtils.capitalize(getVanillaNameLowercase(suffix));
		}

		@Nullable
		public final BlockModOre getOre() {
			if (!this.getProperties().hasOre())
				return null;
			return (BlockModOre) getRegistryValue(ForgeRegistries.BLOCKS, "ore");
		}

		@Nullable
		public final BlockResource getBlock() {
			if (!this.getProperties().hasBlock())
				return null;
			return (BlockResource) getRegistryValue(ForgeRegistries.BLOCKS, "block");
		}

		@Nullable
		public final BlockItem getIngot() {
			if (!this.getProperties().hasIngotAndNugget())
				return null;
			return (BlockItem) getRegistryValue(ForgeRegistries.BLOCKS, "ingot");
		}

		@Nullable
		public final BlockItem getNugget() {
			if (!this.getProperties().hasIngotAndNugget())
				return null;
			return (BlockItem) getRegistryValue(ForgeRegistries.BLOCKS, "nugget");
		}

		@Nullable
		public BlockWire getWire() {
			if (!this.getProperties().hasWire())
				return null;
			return (BlockWire) getRegistryValue(ForgeRegistries.BLOCKS, "wire");
		}

		@Nullable
		public BlockSpool getSpool() {
			if (!this.getProperties().hasWire())
				return null;
			return (BlockSpool) getRegistryValue(ForgeRegistries.BLOCKS, "spool");
		}

		@Nullable
		public BlockEnamel getEnamel() {
			if (!this.getProperties().hasEnamel())
				return null;
			return (BlockEnamel) getRegistryValue(ForgeRegistries.BLOCKS, "enamel");
		}

		@Nullable
		public ItemModArmor getHelmet() {
			if (!this.getProperties().hasArmor())
				return null;
			return (ItemModArmor) getRegistryValue(ForgeRegistries.ITEMS, "helmet");
		}

		@Nullable
		public ItemModArmor getChestplate() {
			if (!this.getProperties().hasArmor())
				return null;
			return (ItemModArmor) getRegistryValue(ForgeRegistries.ITEMS, "chestplate");
		}

		@Nullable
		public ItemModArmor getLeggings() {
			if (!this.getProperties().hasArmor())
				return null;
			return (ItemModArmor) getRegistryValue(ForgeRegistries.ITEMS, "leggings");
		}

		@Nullable
		public ItemModArmor getBoots() {
			if (!this.getProperties().hasArmor())
				return null;
			return (ItemModArmor) getRegistryValue(ForgeRegistries.ITEMS, "boots");
		}

		@Nullable
		public ItemModPickaxe getPickaxe() {
			if (!this.getProperties().hasTools())
				return null;
			return (ItemModPickaxe) getRegistryValue(ForgeRegistries.ITEMS, "pickaxe");
		}

		@Nullable
		public ItemModAxe getAxe() {
			if (!this.getProperties().hasTools())
				return null;
			return (ItemModAxe) getRegistryValue(ForgeRegistries.ITEMS, "axe");
		}

		@Nullable
		public ItemModSword getSword() {
			if (!this.getProperties().hasTools())
				return null;
			return (ItemModSword) getRegistryValue(ForgeRegistries.ITEMS, "sword");
		}

		@Nullable
		public ItemModShovel getShovel() {
			if (!this.getProperties().hasTools())
				return null;
			return (ItemModShovel) getRegistryValue(ForgeRegistries.ITEMS, "shovel");
		}

		@Nullable
		public ItemModHoe getHoe() {
			if (!this.getProperties().hasTools())
				return null;
			return (ItemModHoe) getRegistryValue(ForgeRegistries.ITEMS, "hoe");
		}

		@Nullable
		public ItemCoil getCoil() {
			if (!this.getProperties().hasCoil())
				return null;
			return (ItemCoil) getRegistryValue(ForgeRegistries.ITEMS, "coil");
		}

		@Nullable
		public ItemRail getRail() {
			if (!this.getProperties().hasRail())
				return null;
			return (ItemRail) getRegistryValue(ForgeRegistries.ITEMS, "rail");
		}

		@Nullable
		public EntityEntry getSlugEntity() {
			if (!this.getProperties().hasRailgunSlug())
				return null;
			return getRegistryValue(ForgeRegistries.ENTITIES, "slug");
		}

		@Nullable
		public ItemSlug getSlugItem() {
			if (!this.getProperties().hasRailgunSlug())
				return null;
			return (ItemSlug) getRegistryValue(ForgeRegistries.ITEMS, "slug");
		}

		@Nullable
		public ItemCasedSlug getCasedSlug() {
			if (!this.getProperties().hasRailgunSlug())
				return null;
			return (ItemCasedSlug) ForgeRegistries.ITEMS.getValue(new ResourceLocation(ModReference.MOD_ID, "cased_" + this.getNameLowercase() + "_" + "slug"));
		}

		@Nullable
		private <T> T getRegistryValue(@Nonnull IForgeRegistry<? extends IForgeRegistryEntry<T>> registry, @Nonnull String nameSuffix) {
			nameSuffix = nameSuffix.toLowerCase();
			return (T) registry.getValue(new ResourceLocation(this.getResouceLocationDomain(nameSuffix, registry), this.getNameLowercase() + "_" + nameSuffix));
		}

		public static ModMaterials byId(int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}

		public static final float getHighestHardness() {
			float ret = 0;
			for (ModMaterials material : ModMaterials.values())
				if (material.getProperties().getHardness() > ret)
					ret = material.getProperties().getHardness();
			return ret;
		}

		public static double getHighestConductivity() {
			float ret = 0;
			for (ModMaterials material : ModMaterials.values())
				if (material.getProperties().getConductivity() > ret)
					ret = material.getProperties().getConductivity();
			return ret;
		}

	}

	public static enum BlockItemTypes implements IEnumNameFormattable {

		/* @formatter:off */
		NUGGET(0), INGOT(1);
		/* @formatter:on */

		private final int id;

		BlockItemTypes(int idIn) {
			this.id = idIn;
		}

		public int getId() {
			return this.id;
		}

		public static BlockItemTypes byId(int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}
	}

	public static enum SlugCasingParts implements IEnumNameFormattable, IStringSerializable {

		BACK(0),

		TOP(1),

		BOTTOM(2);

		private final int id;

		SlugCasingParts(int idIn) {
			this.id = idIn;
		}

		@Override
		public String getName() {
			return getNameFormatted();
		}

		public int getId() {
			return this.id;
		}

		public static SlugCasingParts byId(int id) {
			return values()[Math.min(Math.abs(id), values().length)];
		}
	}

}
