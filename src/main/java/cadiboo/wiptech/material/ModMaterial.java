package cadiboo.wiptech.material;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import cadiboo.wiptech.EventSubscriber;
import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockEnamel;
import cadiboo.wiptech.block.BlockItem;
import cadiboo.wiptech.block.BlockModOre;
import cadiboo.wiptech.block.BlockResource;
import cadiboo.wiptech.block.BlockSpool;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.block.IBlockModMaterial;
import cadiboo.wiptech.client.ClientEventSubscriber;
import cadiboo.wiptech.client.render.block.model.GlitchModelLoader;
import cadiboo.wiptech.client.render.block.model.WireModelLoader;
import cadiboo.wiptech.entity.projectile.EntitySlug;
import cadiboo.wiptech.init.ModItems;
import cadiboo.wiptech.item.IItemModMaterial;
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
import cadiboo.wiptech.item.ModItemBlock;
import cadiboo.wiptech.util.ModEnums.BlockItemType;
import cadiboo.wiptech.util.ModEnums.IEnumNameFormattable;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocation;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocationDomain;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocationPath;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

	BAUXITE(18, new ModMaterialProperties(true, false, false, null, false, null, false, false, false, false, false, false, false, false, false, false, ModMaterial.ALUMINIUM.getProperties().getHardness(), 0, null, new BlockRenderLayer[]{BlockRenderLayer.SOLID}, null, null)),

	APATITE(19, new GemProperties(true, 4.50f, 0, () -> ModItems.APATITE_RESOURCE, (final Integer fortune, final Random random) -> {
		return 64;
	})),

	GLITCH(19, new MetalProperties(true, 20.0f, 1000));

	private final int id;
	private final ModMaterialProperties properties;
	private final ArmorMaterial armorMaterial;
	private final ToolMaterial toolMaterial;
	private final HorseArmorType horseArmorType;
	private final ModResourceLocationDomain modId;

	private BlockModOre ore;
	private BlockResource block;
	private BlockItem resource;
	private BlockItem resourcePiece;
	private BlockWire wire;
	private BlockSpool spool;
	private BlockEnamel enamel;

	private ModItemBlock itemBlockOre;
	private ModItemBlock itemBlockBlock;
	private ModItemBlock itemBlockResource;
	private ModItemBlock itemBlockResourcePiece;
	private ModItemBlock itemBlockWire;
	private ModItemBlock itemBlockSpool;
	private ModItemBlock itemBlockEnamel;

	private ItemModArmor helmet;
	private ItemModArmor chestplate;
	private ItemModArmor leggings;
	private ItemModArmor boots;
	private ItemModHorseArmor horseArmor;
	private ItemModPickaxe pickaxe;
	private ItemModAxe axe;
	private ItemModSword sword;
	private ItemModShovel shovel;
	private ItemModHoe hoe;
	private ItemCoil coil;
	private ItemRail rail;
	private ItemSlug slugItem;
	private ItemCasedSlug casedSlug;

	private EntityEntry slugEntity;

	private ModMaterial(final int id, final ModMaterialProperties properties) {
		this(id, properties, ModReference.MOD_ID);
	}

	private ModMaterial(final int id, final ModMaterialProperties properties, final String modId) {
		this.id = id;
		this.properties = properties;
		this.modId = new ModResourceLocationDomain(modId);
		this.armorMaterial = this.generateArmorMaterial();
		this.toolMaterial = this.generateToolMaterial();
		this.horseArmorType = this.generateHorseArmorType();
	}

	public int getId() {
		return this.id;
	}

	public ModMaterialProperties getProperties() {
		return this.properties;
	}

	public ModResourceLocationDomain getModId() {
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

			String nameSuffix = null;
			if ((nameSuffix == null) && this.getProperties().hasHelmet()) {
				nameSuffix = "helmet";
			}
			if ((nameSuffix == null) && this.getProperties().hasChestplate()) {
				nameSuffix = "chestplate";
			}
			if ((nameSuffix == null) && this.getProperties().hasLeggings()) {
				nameSuffix = "leggings";
			}
			if ((nameSuffix == null) && this.getProperties().hasBoots()) {
				nameSuffix = "boots";
			}

			final String textureName = new ModResourceLocation(this.getResouceLocationDomainWithOverrides(nameSuffix, ForgeRegistries.ITEMS), new ModResourceLocationPath(this.getNameLowercase())).toString();

			final int durability = (int) Math.ceil(this.getProperties().getHardness() * ModReference.ARMOR_MATERIAL_HARDNESS_MULTIPLIER);

			final int[] reductionAmounts = new int[4];
			Arrays.fill(reductionAmounts, (int) Math.ceil(this.getProperties().getHardness() / 2f));

			final int enchantability = (int) Math.ceil(this.getProperties().getConductivity() / 10f);

			final SoundEvent soundOnEquip = SoundEvents.ITEM_ARMOR_EQUIP_IRON;

			final float toughness = (int) Math.ceil(this.getProperties().getHardness() / 5f);

			final ArmorMaterial armorMaterial = EnumHelper.addArmorMaterial(name, textureName, durability, reductionAmounts, enchantability, soundOnEquip, toughness);
			// TODO TEST THIS!!
			armorMaterial.setRepairItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ModResourceLocation(this.getResouceLocationDomainWithOverrides(nameSuffix, ForgeRegistries.ITEMS), new ModResourceLocationPath(this.getNameLowercase() + (this.getProperties().getResourceSuffix().length() > 0 ? "_" + this.getProperties().getResourceSuffix() : ""))))));
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

			final String textureLocation = new ModResourceLocation(this.getResouceLocationDomainWithOverrides("horse_armor", ForgeRegistries.ITEMS), new ModResourceLocationPath("textures/entity/horse/armor/horse_armor_" + this.getNameLowercase())).toString() + ".png";

			final int armorStrength = (int) Math.ceil(this.getProperties().getHardness());

			return EnumHelper.addHorseArmor(name, textureLocation, armorStrength);
		}
	}

	public ModResourceLocationDomain getResouceLocationDomainWithOverrides(final String nameSuffix, final IForgeRegistry registry) {
		for (final ModContainer mod : Loader.instance().getActiveModList()) {
			if (!mod.getModId().equals(ModReference.MOD_ID)) {
				if (registry.containsKey(new ModResourceLocation(mod.getModId(), this.getVanillaNameLowercase(nameSuffix) + (nameSuffix.length() > 0 ? "_" + nameSuffix : "")))) {
					return new ModResourceLocationDomain(mod.getModId());
				}
			}
		}
		return new ModResourceLocationDomain(ModReference.MOD_ID);
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

	/** tungsten_carbite used to turn into Tungsten_carbite. Now it turns into Tungsten Carbite */
	@Override
	public String getNameFormatted() {
		return String.join(" ", Arrays.asList(this.getNameLowercase().split("_")).stream().map(String::toUpperCase).collect(Collectors.toList()));
	}

	public BlockModOre getOre() {
		return this.ore;
	}

	public BlockResource getBlock() {
		return this.block;
	}

	public BlockItem getResource() {
		return this.resource;
	}

	public BlockItem getResourcePiece() {
		return this.resourcePiece;
	}

	public BlockWire getWire() {
		return this.wire;
	}

	public BlockSpool getSpool() {
		return this.spool;
	}

	public BlockEnamel getEnamel() {
		return this.enamel;
	}

	public ModItemBlock getItemBlockOre() {
		return this.itemBlockOre;
	}

	public ModItemBlock getItemBlockBlock() {
		return this.itemBlockBlock;
	}

	public ModItemBlock getItemBlockResource() {
		return this.itemBlockResource;
	}

	public ModItemBlock getItemBlockResourcePiece() {
		return this.itemBlockResourcePiece;
	}

	public ModItemBlock getItemBlockWire() {
		return this.itemBlockWire;
	}

	public ModItemBlock getItemBlockSpool() {
		return this.itemBlockSpool;
	}

	public ModItemBlock getItemBlockEnamel() {
		return this.itemBlockEnamel;
	}

	public ItemModArmor getHelmet() {
		return this.helmet;
	}

	public ItemModArmor getChestplate() {
		return this.chestplate;
	}

	public ItemModArmor getLeggings() {
		return this.leggings;
	}

	public ItemModArmor getBoots() {
		return this.boots;
	}

	public ItemModHorseArmor getHorseArmor() {
		return this.horseArmor;
	}

	public ItemModPickaxe getPickaxe() {
		return this.pickaxe;
	}

	public ItemModAxe getAxe() {
		return this.axe;
	}

	public ItemModSword getSword() {
		return this.sword;
	}

	public ItemModShovel getShovel() {
		return this.shovel;
	}

	public ItemModHoe getHoe() {
		return this.hoe;
	}

	public ItemCoil getCoil() {
		return this.coil;
	}

	public ItemRail getRail() {
		return this.rail;
	}

	public ItemSlug getSlugItem() {
		return this.slugItem;
	}

	public ItemCasedSlug getCasedSlug() {
		return this.casedSlug;
	}

	public EntityEntry getSlugEntity() {
		return this.slugEntity;
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

	public static ModMaterial byId(final int id) {
		return values()[Math.min(Math.abs(id), values().length)];
	}

	public class MaterialEventSubscriber {

		@SubscribeEvent
		public void onRegisterBlocksEvent(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();

			if (ModMaterial.this.getProperties().hasOre()) {
				ModMaterial.this.ore = new BlockModOre(ModMaterial.this);
				registry.register(ModMaterial.this.ore);
			}

			if (ModMaterial.this.getProperties().hasBlock()) {
				ModMaterial.this.block = new BlockResource(ModMaterial.this);
				registry.register(ModMaterial.this.block);
			}

			if (ModMaterial.this.getProperties().hasResource()) {
				ModMaterial.this.resource = new BlockItem(ModMaterial.this, BlockItemType.RESOURCE);
				registry.register(ModMaterial.this.resource);
			}

			if (ModMaterial.this.getProperties().hasResourcePiece()) {
				ModMaterial.this.resourcePiece = new BlockItem(ModMaterial.this, BlockItemType.RESOURCE_PIECE);
				registry.register(ModMaterial.this.resourcePiece);
			}

			if (ModMaterial.this.getProperties().hasWire()) {
				ModMaterial.this.wire = new BlockWire(ModMaterial.this);
				registry.register(ModMaterial.this.wire);
				ModMaterial.this.spool = new BlockSpool(ModMaterial.this);
				registry.register(ModMaterial.this.spool);
			}

			if (ModMaterial.this.getProperties().hasEnamel()) {
				ModMaterial.this.enamel = new BlockEnamel(ModMaterial.this);
				registry.register(ModMaterial.this.enamel);
			}

			WIPTech.debug("Registered blocks for " + ModMaterial.this.getNameFormatted());
		}

		@SubscribeEvent
		public void onRegisterItemsEvent(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();

			if (ModMaterial.this.getProperties().hasOre()) {
				ModMaterial.this.itemBlockOre = new ModItemBlock(ModMaterial.this.getOre(), new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", ForgeRegistries.BLOCKS), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_ore")));
				registry.register(ModMaterial.this.itemBlockOre);
			}

			if (ModMaterial.this.getProperties().hasBlock()) {
				ModMaterial.this.itemBlockBlock = new ModItemBlock(ModMaterial.this.getBlock(), new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("block", ForgeRegistries.BLOCKS), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_block")));
				registry.register(ModMaterial.this.itemBlockBlock);
			}

			if (ModMaterial.this.getProperties().hasResource()) {
				final String suffix = ModMaterial.this.getProperties().getResourceSuffix();
				ModMaterial.this.itemBlockResource = new ModItemBlock(ModMaterial.this.getResource(), new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides(ModMaterial.this.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + (suffix.length() > 0 ? "_" + suffix : ""))));
				registry.register(ModMaterial.this.itemBlockResource);
			}
			if (ModMaterial.this.getProperties().hasResourcePiece()) {
				final String suffix = ModMaterial.this.getProperties().getResourcePieceSuffix();
				ModMaterial.this.itemBlockResourcePiece = new ModItemBlock(ModMaterial.this.getResourcePiece(), new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides(ModMaterial.this.getProperties().getResourcePieceSuffix(), ForgeRegistries.ITEMS), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + (suffix.length() > 0 ? "_" + suffix : ""))));
				registry.register(ModMaterial.this.itemBlockResourcePiece);
			}

			if (ModMaterial.this.getProperties().hasWire()) {
				ModMaterial.this.itemBlockWire = new ModItemBlock(ModMaterial.this.getWire());
				registry.register(ModMaterial.this.itemBlockWire);

				ModMaterial.this.itemBlockSpool = new ModItemBlock(ModMaterial.this.getSpool());
				registry.register(ModMaterial.this.itemBlockSpool);
			}

			if (ModMaterial.this.getProperties().hasEnamel()) {
				ModMaterial.this.itemBlockEnamel = new ModItemBlock(ModMaterial.this.getEnamel());
				registry.register(ModMaterial.this.itemBlockEnamel);
			}

			//

			if (ModMaterial.this.getProperties().hasHelmet()) {
				ModMaterial.this.helmet = new ItemModArmor(ModMaterial.this, EntityEquipmentSlot.HEAD);
				registry.register(ModMaterial.this.helmet);
			}
			if (ModMaterial.this.getProperties().hasChestplate()) {
				ModMaterial.this.chestplate = new ItemModArmor(ModMaterial.this, EntityEquipmentSlot.CHEST);
				registry.register(ModMaterial.this.chestplate);
			}
			if (ModMaterial.this.getProperties().hasLeggings()) {
				ModMaterial.this.leggings = new ItemModArmor(ModMaterial.this, EntityEquipmentSlot.LEGS);
				registry.register(ModMaterial.this.leggings);
			}
			if (ModMaterial.this.getProperties().hasBoots()) {
				ModMaterial.this.boots = new ItemModArmor(ModMaterial.this, EntityEquipmentSlot.FEET);
				registry.register(ModMaterial.this.boots);
			}
			if (ModMaterial.this.getProperties().hasHorseArmor()) {
				ModMaterial.this.horseArmor = new ItemModHorseArmor(ModMaterial.this);
				registry.register(ModMaterial.this.horseArmor);
			}

			if (ModMaterial.this.getProperties().hasPickaxe()) {
				ModMaterial.this.pickaxe = new ItemModPickaxe(ModMaterial.this);
				registry.register(ModMaterial.this.pickaxe);
			}
			if (ModMaterial.this.getProperties().hasAxe()) {
				ModMaterial.this.axe = new ItemModAxe(ModMaterial.this);
				registry.register(ModMaterial.this.axe);
			}
			if (ModMaterial.this.getProperties().hasSword()) {
				ModMaterial.this.sword = new ItemModSword(ModMaterial.this);
				registry.register(ModMaterial.this.sword);
			}
			if (ModMaterial.this.getProperties().hasShovel()) {
				ModMaterial.this.shovel = new ItemModShovel(ModMaterial.this);
				registry.register(ModMaterial.this.shovel);
			}
			if (ModMaterial.this.getProperties().hasHoe()) {
				ModMaterial.this.hoe = new ItemModHoe(ModMaterial.this);
				registry.register(ModMaterial.this.hoe);
			}

			if (ModMaterial.this.getProperties().hasCoil()) {
				ModMaterial.this.coil = new ItemCoil(ModMaterial.this);
				registry.register(ModMaterial.this.coil);
			}

			if (ModMaterial.this.getProperties().hasRail()) {
				ModMaterial.this.rail = new ItemRail(ModMaterial.this);
				registry.register(ModMaterial.this.rail);
			}

			if (ModMaterial.this.getProperties().hasRailgunSlug()) {
				ModMaterial.this.slugItem = new ItemSlug(ModMaterial.this);
				registry.register(ModMaterial.this.slugItem);

				ModMaterial.this.casedSlug = new ItemCasedSlug(ModMaterial.this);
				registry.register(ModMaterial.this.casedSlug);
			}

			WIPTech.debug("Registered items for " + ModMaterial.this.getNameFormatted());

		}

		@SubscribeEvent
		public void onRegisterEntitiesEvent(final RegistryEvent.Register<EntityEntry> event) {
			final IForgeRegistry<EntityEntry> registry = event.getRegistry();

			// TODO AdditionalSpawnData maybe?
			if (ModMaterial.this.getProperties().hasRailgunSlug()) {
				final ModResourceLocation registryName = new ModResourceLocation(ModReference.MOD_ID, ModMaterial.this.getNameLowercase() + "_slug");
				final boolean hasEgg = false;
				final int range = 128;
				final int updateFrequency = 2;
				final boolean sendVelocityUpdates = true;

				EntityEntryBuilder<Entity> builder = EntityEntryBuilder.create();
				builder = builder.entity(EntitySlug.class);
				builder = builder.id(registryName, EventSubscriber.entityId++);
				builder = builder.name(registryName.getResourcePath());
				builder = builder.tracker(range, updateFrequency, sendVelocityUpdates);

				if (hasEgg) {
					builder = builder.egg(0xFFFFFF, 0xAAAAAA);
				}

				ModMaterial.this.slugEntity = builder.build();
				registry.register(ModMaterial.this.slugEntity);
			}
			WIPTech.debug("Registered entities for " + ModMaterial.this.getNameFormatted());
		}

		/** CLIENT ONLY */
		@SideOnly(Side.CLIENT)
		public void onRegisterModelsEvent(final ModelRegistryEvent event) {

			if (ModMaterial.this.getProperties().hasWire()) {
				ModelLoader.setCustomStateMapper(ModMaterial.this.getWire(), new StateMapperBase() {
					@Override
					protected ModelResourceLocation getModelResourceLocation(final IBlockState iBlockState) {
						return new ModelResourceLocation(new ModResourceLocation(ModMaterial.this.getModId().toString(), ModMaterial.this.getNameLowercase() + "_wire"), ClientEventSubscriber.DEFAULT_VARIANT);
					}
				});
			}

			if (ModMaterial.this.getProperties().hasEnamel()) {
				ModelLoader.setCustomStateMapper(ModMaterial.this.getEnamel(), new StateMapperBase() {

					@Override
					protected ModelResourceLocation getModelResourceLocation(final IBlockState iBlockState) {
						return new ModelResourceLocation(new ModResourceLocation(ModMaterial.this.getModId().toString(), ModMaterial.this.getNameLowercase() + "_enamel"), ClientEventSubscriber.DEFAULT_VARIANT);
					}
				});
			}

			ModelLoaderRegistry.registerLoader(new WireModelLoader());
			WIPTech.debug("Registered custom State Mappers for wires and enamels with the Model Loader");

			if (ModMaterial.GLITCH.getProperties().hasBlock() || ModMaterial.GLITCH.getProperties().hasOre()) {
				if (ModMaterial.GLITCH.getBlock() != null) {
					ModelLoader.setCustomStateMapper(ModMaterial.GLITCH.getBlock(), new StateMapperBase() {
						@Override
						protected ModelResourceLocation getModelResourceLocation(final IBlockState iBlockState) {
							return new ModelResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "glitch_block"), ClientEventSubscriber.DEFAULT_VARIANT);
						}
					});
				}
				if (ModMaterial.GLITCH.getOre() != null) {
					ModelLoader.setCustomStateMapper(ModMaterial.GLITCH.getOre(), new StateMapperBase() {
						@Override
						protected ModelResourceLocation getModelResourceLocation(final IBlockState iBlockState) {
							return new ModelResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "glitch_ore"), ClientEventSubscriber.DEFAULT_VARIANT);
						}
					});
				}
				if (ModMaterial.GLITCH.getSpool() != null) {
					ModelLoader.setCustomStateMapper(ModMaterial.GLITCH.getSpool(), new StateMapperBase() {
						@Override
						protected ModelResourceLocation getModelResourceLocation(final IBlockState iBlockState) {
							return new ModelResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "glitch_spool"), ClientEventSubscriber.DEFAULT_VARIANT);
						}
					});
				}
			}
			ModelLoaderRegistry.registerLoader(new GlitchModelLoader());
			WIPTech.debug("Registered custom State Mapper(s) for glitch block and ore with the Model Loader");

			if (ModMaterial.this.getProperties().hasRailgunSlug()) {
				// FIXME TODO re-enable this & make it work
				// ModelLoader.setCustomMeshDefinition(getCasedSlug(), stack -> new ModelResourceLocation(new ModResourceLocation(getAssetsModId(), "cased_" + getNameLowercase() + "_slug"), DEFAULT_VARIANT));
			}

			// ModelLoaderRegistry.registerLoader(new CasedSlugModelLoader());
			// WIPTech.debug("Registered custom Mesh Definitions for cased slugs with the Model Loader");

			if (ModMaterial.this.getProperties().hasOre()) {
				this.registerBlockModMaterialItemBlockModel(ModMaterial.this.getOre());
			}

			if (ModMaterial.this.getProperties().hasBlock()) {
				this.registerBlockModMaterialItemBlockModel(ModMaterial.this.getBlock());
			}

			if (ModMaterial.this.getProperties().hasResource()) {
				if (ModMaterial.this.getResouceLocationDomainWithOverrides(ModMaterial.this.getProperties().getResourceSuffix().toLowerCase(), ForgeRegistries.ITEMS).equals(ModMaterial.this.getModId())) {
					this.registerBlockModMaterialItemBlockModel(ModMaterial.this.getResource());
				}
				if (ModMaterial.this.getResouceLocationDomainWithOverrides(ModMaterial.this.getProperties().getResourcePieceSuffix().toLowerCase(), ForgeRegistries.ITEMS).equals(ModMaterial.this.getModId())) {
					this.registerBlockModMaterialItemBlockModel(ModMaterial.this.getResourcePiece());
				}
			}

			if (ModMaterial.this.getProperties().hasWire()) {
				this.registerBlockModMaterialItemBlockModel(ModMaterial.this.getWire());

				this.registerBlockModMaterialItemBlockModel(ModMaterial.this.getSpool());
			}

			if (ModMaterial.this.getProperties().hasEnamel()) {
				this.registerBlockModMaterialItemBlockModel(ModMaterial.this.getEnamel());
			}

			if (ModMaterial.this.getProperties().hasHelmet()) {
				this.registerItemModMaterialModel(ModMaterial.this.getHelmet());
			}
			if (ModMaterial.this.getProperties().hasChestplate()) {
				this.registerItemModMaterialModel(ModMaterial.this.getChestplate());
			}
			if (ModMaterial.this.getProperties().hasLeggings()) {
				this.registerItemModMaterialModel(ModMaterial.this.getLeggings());
			}
			if (ModMaterial.this.getProperties().hasBoots()) {
				this.registerItemModMaterialModel(ModMaterial.this.getBoots());
			}
			if (ModMaterial.this.getProperties().hasHorseArmor()) {
				this.registerItemModMaterialModel(ModMaterial.this.getHorseArmor());
			}

			if (ModMaterial.this.getProperties().hasPickaxe()) {
				this.registerItemModMaterialModel(ModMaterial.this.getPickaxe());
			}
			if (ModMaterial.this.getProperties().hasAxe()) {
				this.registerItemModMaterialModel(ModMaterial.this.getAxe());
			}
			if (ModMaterial.this.getProperties().hasSword()) {
				this.registerItemModMaterialModel(ModMaterial.this.getSword());
			}
			if (ModMaterial.this.getProperties().hasShovel()) {
				this.registerItemModMaterialModel(ModMaterial.this.getShovel());
			}
			if (ModMaterial.this.getProperties().hasHoe()) {
				this.registerItemModMaterialModel(ModMaterial.this.getHoe());
			}

			if (ModMaterial.this.getProperties().hasCoil()) {
				this.registerItemModMaterialModel(ModMaterial.this.getCoil());
			}

			if (ModMaterial.this.getProperties().hasRail()) {
				this.registerItemModMaterialModel(ModMaterial.this.getRail());
			}

			if (ModMaterial.this.getProperties().hasRailgunSlug()) {
				this.registerItemModMaterialModel(ModMaterial.this.getSlugItem());

				// registerItemModMaterialModel(getCasedSlug());
			}

			WIPTech.debug("Registered models for materials");
		}

		/** CLIENT ONLY */
		@SideOnly(Side.CLIENT)
		private <T extends Item & IItemModMaterial> void registerItemModMaterialModel(final T item) {
			final boolean isVanilla = item.getRegistryName().getResourceDomain().equals("minecraft");
			final String registryNameResourceDomain = isVanilla ? "minecraft" : item.getModMaterial().getModId().toString();
			final String registryNameResourcePath = item.getRegistryName().getResourcePath();

			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ModResourceLocation(registryNameResourceDomain, registryNameResourcePath), ClientEventSubscriber.DEFAULT_VARIANT));
		}

		/** CLIENT ONLY */
		@SideOnly(Side.CLIENT)
		private <T extends Block & IBlockModMaterial> void registerBlockModMaterialItemBlockModel(final T block) {
			final boolean isVanilla = block.getRegistryName().getResourceDomain().equals("minecraft");
			final String registryNameResourceDomain = isVanilla ? "minecraft" : block.getModMaterial().getModId().toString();
			final String registryNameResourcePath = block.getRegistryName().getResourcePath();

			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ModResourceLocation(registryNameResourceDomain, registryNameResourcePath), ClientEventSubscriber.DEFAULT_VARIANT));
		}

		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void onRegistryEventRegister(final RegistryEvent.Register event) {
			final IForgeRegistry registry = event.getRegistry();

			if (registry == ForgeRegistries.BLOCKS) {
				if (ModMaterial.this.getProperties().hasOre()) {
					ModMaterial.this.ore = (BlockModOre) this.getRegistryValue(registry, "ore");
				}
				if (ModMaterial.this.getProperties().hasBlock()) {
					ModMaterial.this.block = (BlockResource) this.getRegistryValue(registry, "block");
				}
				if (ModMaterial.this.getProperties().hasResource()) {
					ModMaterial.this.resource = (BlockItem) this.getRegistryValue(registry, ModMaterial.this.getProperties().getResourceSuffix());
				}
				if (ModMaterial.this.getProperties().hasResourcePiece()) {
					ModMaterial.this.resourcePiece = (BlockItem) this.getRegistryValue(registry, ModMaterial.this.getProperties().getResourcePieceSuffix());
				}
				if (ModMaterial.this.getProperties().hasWire()) {
					ModMaterial.this.wire = (BlockWire) this.getRegistryValue(registry, "wire");

					ModMaterial.this.spool = (BlockSpool) this.getRegistryValue(registry, "spool");
				}
				if (ModMaterial.this.getProperties().hasEnamel()) {
					ModMaterial.this.enamel = (BlockEnamel) this.getRegistryValue(registry, "enamel");
				}
			}

			if (registry == ForgeRegistries.ITEMS) {

				if (ModMaterial.this.getProperties().hasOre()) {
					ModMaterial.this.itemBlockOre = (ModItemBlock) this.getRegistryValue(registry, "ore");
				}
				if (ModMaterial.this.getProperties().hasBlock()) {
					ModMaterial.this.itemBlockBlock = (ModItemBlock) this.getRegistryValue(registry, "block");
				}
				if (ModMaterial.this.getProperties().hasResource()) {
					ModMaterial.this.itemBlockResource = (ModItemBlock) this.getRegistryValue(registry, ModMaterial.this.getProperties().getResourceSuffix());
				}
				if (ModMaterial.this.getProperties().hasResourcePiece()) {
					ModMaterial.this.itemBlockResourcePiece = (ModItemBlock) this.getRegistryValue(registry, ModMaterial.this.getProperties().getResourcePieceSuffix());
				}
				if (ModMaterial.this.getProperties().hasWire()) {
					ModMaterial.this.itemBlockWire = (ModItemBlock) this.getRegistryValue(registry, "wire");

					ModMaterial.this.itemBlockSpool = (ModItemBlock) this.getRegistryValue(registry, "spool");
				}
				if (ModMaterial.this.getProperties().hasEnamel()) {
					ModMaterial.this.itemBlockEnamel = (ModItemBlock) this.getRegistryValue(registry, "enamel");
				}

				//

				if (ModMaterial.this.getProperties().hasHelmet()) {
					ModMaterial.this.helmet = (ItemModArmor) this.getRegistryValue(registry, "helmet");
				}
				if (ModMaterial.this.getProperties().hasChestplate()) {
					ModMaterial.this.chestplate = (ItemModArmor) this.getRegistryValue(registry, "chestplate");
				}
				if (ModMaterial.this.getProperties().hasLeggings()) {
					ModMaterial.this.leggings = (ItemModArmor) this.getRegistryValue(registry, "leggings");
				}
				if (ModMaterial.this.getProperties().hasBoots()) {
					ModMaterial.this.boots = (ItemModArmor) this.getRegistryValue(registry, "boots");
				}
				if (ModMaterial.this.getProperties().hasHorseArmor()) {
					ModMaterial.this.horseArmor = (ItemModHorseArmor) this.getRegistryValue(registry, "horseArmor");
				}
				if (ModMaterial.this.getProperties().hasPickaxe()) {
					ModMaterial.this.pickaxe = (ItemModPickaxe) this.getRegistryValue(registry, "pickaxe");
				}
				if (ModMaterial.this.getProperties().hasAxe()) {
					ModMaterial.this.axe = (ItemModAxe) this.getRegistryValue(registry, "axe");
				}
				if (ModMaterial.this.getProperties().hasSword()) {
					ModMaterial.this.sword = (ItemModSword) this.getRegistryValue(registry, "sword");
				}
				if (ModMaterial.this.getProperties().hasShovel()) {
					ModMaterial.this.shovel = (ItemModShovel) this.getRegistryValue(registry, "shovel");
				}
				if (ModMaterial.this.getProperties().hasHoe()) {
					ModMaterial.this.hoe = (ItemModHoe) this.getRegistryValue(registry, "hoe");
				}
				if (ModMaterial.this.getProperties().hasCoil()) {
					ModMaterial.this.coil = (ItemCoil) this.getRegistryValue(registry, "coil");
				}
				if (ModMaterial.this.getProperties().hasRail()) {
					ModMaterial.this.rail = (ItemRail) this.getRegistryValue(registry, "rail");
				}
				if (ModMaterial.this.getProperties().hasRailgunSlug()) {
					ModMaterial.this.slugItem = (ItemSlug) this.getRegistryValue(registry, "slug");
				}
				if (ModMaterial.this.getProperties().hasRailgunSlug()) {
					ModMaterial.this.casedSlug = (ItemCasedSlug) this.getRegistryValue(registry, "cased_slug");
				}
			}

			if (registry == ForgeRegistries.ENTITIES) {
				if (ModMaterial.this.getProperties().hasRailgunSlug()) {
					ModMaterial.this.slugEntity = (EntityEntry) this.getRegistryValue(registry, "slug");
				}
			}

		}

		@Nullable
		public <T> T getRegistryValue(@Nonnull final IForgeRegistry<? extends IForgeRegistryEntry<T>> registry, @Nonnull String nameSuffix) {
			nameSuffix = nameSuffix.toLowerCase();
			return (T) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides(nameSuffix, registry), new ModResourceLocationPath(ModMaterial.this.getVanillaNameLowercase(nameSuffix) + (nameSuffix.length() > 0 ? "_" + nameSuffix : ""))));
		}

		// TODO: texturestich?

		@Override
		public String toString() {
			return super.toString() + " - " + ModMaterial.this.getNameFormatted();
		}
	}

	private final MaterialEventSubscriber eventSubscriber = new MaterialEventSubscriber();
	public MaterialEventSubscriber getEventSubscriber() {
		WIPTech.info(this.eventSubscriber.toString());
		return this.eventSubscriber;
	}

}
