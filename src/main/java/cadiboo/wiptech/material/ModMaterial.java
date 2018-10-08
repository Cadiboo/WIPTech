package cadiboo.wiptech.material;

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
import cadiboo.wiptech.item.ItemModHorseArmor;
import cadiboo.wiptech.item.ItemModPickaxe;
import cadiboo.wiptech.item.ItemModShovel;
import cadiboo.wiptech.item.ItemModSword;
import cadiboo.wiptech.item.ItemRail;
import cadiboo.wiptech.item.ItemSlug;
import cadiboo.wiptech.util.ModEnums.IEnumNameFormattable;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModResourceLocation;
import net.minecraft.block.material.Material;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * MOHS Hardness from <a href= "https://en.wikipedia.org/wiki/Mohs_scale_of_mineral_hardness">Wikipedia</a> and <a href= "http://periodictable.com/Properties/A/MohsHardness.v.html">Periodictable</a>
 * @author Cadiboo
 */
public enum ModMaterial implements IEnumNameFormattable {

	/* ore block ingot armor tools hard cond */
	URANIUM(0, new MetalProperties(true, 6.00f, 24)),

	TUNGSTEN(1, new MetalProperties(true, 7.50f, 173)),

	TUNGSTEN_CARBITE(2, new MetalProperties(false, 9.00f, 173)),

	TITANIUM(3, new MetalProperties(true, 6.00f, 23)),

	TIN(4, new MetalProperties(true, 1.50f, 68)),

	THORIUM(5, new MetalProperties(true, 3.00f, 42)),

	SILVER(6, new MetalProperties(true, 2.50f, 407)),

	PLATINUM(7, new MetalProperties(true, 3.50f, 73)),

	PLUTONIUM(8, new MetalProperties(true, 0.01f, 8)),

	OSMIUM(9, new MetalProperties(true, 7.00f, 61)),

	NICKEL(10, new MetalProperties(true, 4.00f, 90)),

	STEEL(11, new MetalProperties(false, 4.50f, 54)),

	ALUMINIUM(12, new MetalProperties(true, 2.75f, 204)),

	COPPER(13, new MetalProperties(true, 3.00f, 386)),

	GOLD(14, new MetalProperties(true, 2.50f, 315)),

	IRON(15, new MetalProperties(true, 4.00f, 73)),

	LEAD(16, new MetalProperties(true, 1.50f, 35)),

	GALLIUM(17, new MetalProperties(true, 1.50f, 29)),

	BAUXITE(18, new ModMaterialProperties(true, false, false, false, false, false, false, false, false, false, false, false, false, false, ModMaterial.ALUMINIUM.getProperties().getHardness(), 0, null)),

	APATITE(19, new MetalProperties(true, 4.50f, 0)),

	GLITCH(19, new MetalProperties(true, 20.0f, 1000));

	private final int id;
	private final ModMaterialProperties properties;
	private final ArmorMaterial armorMaterial;
	private final ToolMaterial toolMaterial;
	private final HorseArmorType horseArmorType;
	private final String modId;

	private ModMaterial(final int id, final ModMaterialProperties properties) {
		this(id, properties, ModReference.MOD_ID);
	}

	ModMaterial(final int id, final ModMaterialProperties properties, final String modId) {
		this.id = id;
		this.properties = properties;
		this.armorMaterial = this.generateArmorMaterial();
		this.toolMaterial = this.generateToolMaterial();
		this.horseArmorType = this.generateHorseArmorType();
		this.modId = modId;
	}

	public int getId() {
		return this.id;
	}

	public ModMaterialProperties getProperties() {
		return this.properties;
	}

	public String getAssetsModId() {
		return this.modId;
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
		boolean hasTools = false;
		hasTools |= this.getProperties().hasPickaxe();
		hasTools |= this.getProperties().hasAxe();
		hasTools |= this.getProperties().hasSword();
		hasTools |= this.getProperties().hasShovel();
		hasTools |= this.getProperties().hasHoe();

		if (!hasTools) {
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
		boolean hasArmor = false;
		hasArmor |= this.getProperties().hasHelmet();
		hasArmor |= this.getProperties().hasChestplate();
		hasArmor |= this.getProperties().hasLeggings();
		hasArmor |= this.getProperties().hasBoots();

		if (!hasArmor) {
			return null;
		} else {
			final String name = this.getNameUppercase();

			final String textureName = new ModResourceLocation(this.getResouceLocationDomain(), this.getNameLowercase()).toString();

			final int durability = (int) Math.ceil(this.getProperties().getHardness() * ModReference.ARMOR_MATERIAL_HARDNESS_MULTIPLIER);

			final int[] reductionAmounts = new int[4];
			Arrays.fill(reductionAmounts, (int) Math.ceil(this.getProperties().getHardness() / 2f));

			final int enchantability = (int) Math.ceil(this.getProperties().getConductivity() / 10f);

			final SoundEvent soundOnEquip = SoundEvents.ITEM_ARMOR_EQUIP_IRON;

			final float toughness = (int) Math.ceil(this.getProperties().getHardness() / 5f);

			final ArmorMaterial armorMaterial = EnumHelper.addArmorMaterial(name, textureName, durability, reductionAmounts, enchantability, soundOnEquip, toughness);
			// TODO TEST THIS!!
			armorMaterial.setRepairItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ModResourceLocation(this.getResouceLocationDomain(), this.getNameLowercase()))));
			return armorMaterial;

		}
	}

	private HorseArmorType generateHorseArmorType() {
		boolean hasArmor = false;
		hasArmor |= this.getProperties().hasHelmet();
		hasArmor |= this.getProperties().hasChestplate();
		hasArmor |= this.getProperties().hasLeggings();
		hasArmor |= this.getProperties().hasBoots();

		if (!hasArmor) {
			return HorseArmorType.NONE;
		} else {
			final String name = this.getNameUppercase();

			final String textureLocation = new ModResourceLocation(this.getResouceLocationDomain(), "textures/entity/horse/armor/horse_armor_" + this.getNameLowercase()).toString() + ".png";

			final int armorStrength = (int) Math.ceil(this.getProperties().getHardness());

			return EnumHelper.addHorseArmor(name, textureLocation, armorStrength);
		}
	}

	public String getResouceLocationDomain() {
		return this.modId;
	}

	public String getVanillaNameLowercase(final String suffix) {

		// if (suffix.toLowerCase().equals("horse_armor") && this.getNameLowercase().equals("iron")) {
		// return "metal_horse_armor";
		// }

		switch (suffix.toLowerCase()) {
			case "sword" :
			case "shovel" :
			case "pickaxe" :
			case "axe" :
			case "hoe" :
			case "helmet" :
			case "chestplate" :
			case "leggings" :
			case "boots" :
			case "apple" :
			case "carrot" :
			case "horse_armor" :
				return this.getNameLowercase() + (this.getNameLowercase().contains("gold") ? "en" : "");
			default :
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
		return (BlockItem) this.getRegistryValue(ForgeRegistries.BLOCKS, "resource");
	}

	private String getResourcePieceType() {
		return null;
	}

	@Nullable
	public BlockItem getResourcePiece() {
		if (!this.getProperties().hasResourcePiece()) {
			return null;
		}
		return (BlockItem) this.getRegistryValue(ForgeRegistries.BLOCKS, "resource_piece");
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
		if (!this.getProperties().hasHelmet()) {
			return null;
		}
		return (ItemModArmor) this.getRegistryValue(ForgeRegistries.ITEMS, "helmet");
	}

	@Nullable
	public ItemModArmor getChestplate() {
		if (!this.getProperties().hasChestplate()) {
			return null;
		}
		return (ItemModArmor) this.getRegistryValue(ForgeRegistries.ITEMS, "chestplate");
	}

	@Nullable
	public ItemModArmor getLeggings() {
		if (!this.getProperties().hasLeggings()) {
			return null;
		}
		return (ItemModArmor) this.getRegistryValue(ForgeRegistries.ITEMS, "leggings");
	}

	@Nullable
	public ItemModArmor getBoots() {
		if (!this.getProperties().hasBoots()) {
			return null;
		}
		return (ItemModArmor) this.getRegistryValue(ForgeRegistries.ITEMS, "boots");
	}

	public ItemModHorseArmor getHorseArmor() {
		if (!this.getProperties().hasHorseArmor()) {
			return null;
		}
		return (ItemModHorseArmor) this.getRegistryValue(ForgeRegistries.ITEMS, "horse_armor");
	}

	@Nullable
	public ItemModPickaxe getPickaxe() {
		if (!this.getProperties().hasPickaxe()) {
			return null;
		}
		return (ItemModPickaxe) this.getRegistryValue(ForgeRegistries.ITEMS, "pickaxe");
	}

	@Nullable
	public ItemModAxe getAxe() {
		if (!this.getProperties().hasAxe()) {
			return null;
		}
		return (ItemModAxe) this.getRegistryValue(ForgeRegistries.ITEMS, "axe");
	}

	@Nullable
	public ItemModSword getSword() {
		if (!this.getProperties().hasSword()) {
			return null;
		}
		return (ItemModSword) this.getRegistryValue(ForgeRegistries.ITEMS, "sword");
	}

	@Nullable
	public ItemModShovel getShovel() {
		if (!this.getProperties().hasShovel()) {
			return null;
		}
		return (ItemModShovel) this.getRegistryValue(ForgeRegistries.ITEMS, "shovel");
	}

	@Nullable
	public ItemModHoe getHoe() {
		if (!this.getProperties().hasHoe()) {
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
	public static <T> T getRegistryValue(@Nonnull final IForgeRegistry<? extends IForgeRegistryEntry<T>> registry, @Nonnull final ModMaterial material, @Nonnull String nameSuffix) {
		nameSuffix = nameSuffix.toLowerCase();
		return (T) registry.getValue(new ModResourceLocation(material.getResouceLocationDomain(), material.getVanillaNameLowercase(nameSuffix) + "_" + nameSuffix));
	}

	public static float getHighestHardness() {
		float ret = 0;
		for (final ModMaterial material : ModMaterial.values()) {
			if (material.getProperties().getHardness() > ret) {
				ret = material.getProperties().getHardness();
			}
		}
		return ret;
	}

	public static double getHighestConductivity() {
		float ret = 0;
		for (final ModMaterial material : ModMaterial.values()) {
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

	public static ModMaterial byId(final int id) {
		return values()[Math.min(Math.abs(id), values().length)];
	}

}
