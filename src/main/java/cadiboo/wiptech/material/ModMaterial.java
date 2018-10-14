package cadiboo.wiptech.material;

import java.util.Arrays;
import java.util.Random;

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
				if (registry.containsKey(new ModResourceLocation(mod.getModId(), this.getVanillaNameLowercase(nameSuffix) + "_" + nameSuffix))) {
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

			final ModMaterial material = ModMaterial.this;

			if (material.getProperties().hasOre()) {
				registry.register(new BlockModOre(material));
			}

			if (material.getProperties().hasBlock()) {
				registry.register(new BlockResource(material));
			}

			if (material.getProperties().hasResource()) {
				registry.register(new BlockItem(material, BlockItemType.RESOURCE));
			}

			if (material.getProperties().hasResourcePiece()) {
				registry.register(new BlockItem(material, BlockItemType.RESOURCE_PIECE));
			}

			if (material.getProperties().hasWire()) {
				registry.register(new BlockWire(material));
				registry.register(new BlockSpool(material));
			}

			if (material.getProperties().hasEnamel()) {
				registry.register(new BlockEnamel(material));
			}

			WIPTech.debug("Registered blocks for " + ModMaterial.this.getNameFormatted());
		}

		@SubscribeEvent
		public void onRegisterItemsEvent(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();

			final ModMaterial material = ModMaterial.this;

			if (material.getProperties().hasOre()) {
				registry.register(new ModItemBlock(material.getOre(), new ModResourceLocation(material.getResouceLocationDomainWithOverrides("ore", ForgeRegistries.BLOCKS), new ModResourceLocationPath(material.getNameLowercase() + "_ore"))));
			}

			if (material.getProperties().hasBlock()) {
				registry.register(new ModItemBlock(material.getBlock(), new ModResourceLocation(material.getResouceLocationDomainWithOverrides("block", ForgeRegistries.BLOCKS), new ModResourceLocationPath(material.getNameLowercase() + "_block"))));
			}

			if (material.getProperties().hasResource()) {
				final String suffix = material.getProperties().getResourceSuffix();
				registry.register(new ModItemBlock(material.getResource(), new ModResourceLocation(material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS), new ModResourceLocationPath(material.getNameLowercase() + (suffix.length() > 0 ? "_" + suffix : "")))));
			}
			if (material.getProperties().hasResourcePiece()) {
				final String suffix = material.getProperties().getResourcePieceSuffix();
				registry.register(new ModItemBlock(material.getResourcePiece(), new ModResourceLocation(material.getResouceLocationDomainWithOverrides(material.getProperties().getResourcePieceSuffix(), ForgeRegistries.ITEMS), new ModResourceLocationPath(material.getNameLowercase() + (suffix.length() > 0 ? "_" + suffix : "")))));
			}

			if (material.getProperties().hasWire()) {
				registry.register(new ModItemBlock(material.getWire()));
				registry.register(new ModItemBlock(material.getSpool()));
			}

			if (material.getProperties().hasEnamel()) {
				registry.register(new ModItemBlock(material.getEnamel()));
			}

			if (material.getProperties().hasHelmet()) {
				registry.register(new ItemModArmor(material, EntityEquipmentSlot.HEAD));
			}
			if (material.getProperties().hasChestplate()) {
				registry.register(new ItemModArmor(material, EntityEquipmentSlot.CHEST));
			}
			if (material.getProperties().hasLeggings()) {
				registry.register(new ItemModArmor(material, EntityEquipmentSlot.LEGS));
			}
			if (material.getProperties().hasBoots()) {
				registry.register(new ItemModArmor(material, EntityEquipmentSlot.FEET));
			}
			if (material.getProperties().hasHorseArmor()) {
				registry.register(new ItemModHorseArmor(material));
			}

			if (material.getProperties().hasPickaxe()) {
				registry.register(new ItemModPickaxe(material));
			}
			if (material.getProperties().hasAxe()) {
				registry.register(new ItemModAxe(material));
			}
			if (material.getProperties().hasSword()) {
				registry.register(new ItemModSword(material));
			}
			if (material.getProperties().hasShovel()) {
				registry.register(new ItemModShovel(material));
			}
			if (material.getProperties().hasHoe()) {
				registry.register(new ItemModHoe(material));
			}

			if (material.getProperties().hasCoil()) {
				registry.register(new ItemCoil(material));
			}

			if (material.getProperties().hasRail()) {
				registry.register(new ItemRail(material));
			}

			if (material.getProperties().hasRailgunSlug()) {
				registry.register(new ItemSlug(material));
				registry.register(new ItemCasedSlug(material));
			}

			WIPTech.debug("Registered items for " + ModMaterial.this.getNameFormatted());

		}

		@SubscribeEvent
		public void onRegisterEntitiesEvent(final RegistryEvent.Register<EntityEntry> event) {
			final IForgeRegistry<EntityEntry> registry = event.getRegistry();

			final ModMaterial material = ModMaterial.this;

			// TODO AdditionalSpawnData maybe?
			if (material.getProperties().hasRailgunSlug()) {

				final ModResourceLocation registryName = new ModResourceLocation(ModReference.MOD_ID, material.getNameLowercase() + "_slug");
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

				final EntityEntry entry = builder.build();
				registry.register(entry);
			}
			WIPTech.debug("Registered entities for " + ModMaterial.this.getNameFormatted());
		}

		@SideOnly(Side.CLIENT)
		public void onRegisterModelsEvent(final ModelRegistryEvent event) {

			final ModMaterial material = ModMaterial.this;

			if (material.getProperties().hasWire()) {
				ModelLoader.setCustomStateMapper(material.getWire(), new StateMapperBase() {
					@Override
					protected ModelResourceLocation getModelResourceLocation(final IBlockState iBlockState) {
						return new ModelResourceLocation(new ModResourceLocation(material.getModId().toString(), material.getNameLowercase() + "_wire"), ClientEventSubscriber.DEFAULT_VARIANT);
					}
				});
			}

			if (material.getProperties().hasEnamel()) {
				ModelLoader.setCustomStateMapper(material.getEnamel(), new StateMapperBase() {

					@Override
					protected ModelResourceLocation getModelResourceLocation(final IBlockState iBlockState) {
						return new ModelResourceLocation(new ModResourceLocation(material.getModId().toString(), material.getNameLowercase() + "_enamel"), ClientEventSubscriber.DEFAULT_VARIANT);
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

			if (material.getProperties().hasRailgunSlug()) {
				// FIXME TODO re-enable this & make it work
				// ModelLoader.setCustomMeshDefinition(material.getCasedSlug(), stack -> new ModelResourceLocation(new ModResourceLocation(material.getAssetsModId(), "cased_" + material.getNameLowercase() + "_slug"), DEFAULT_VARIANT));
			}

			// ModelLoaderRegistry.registerLoader(new CasedSlugModelLoader());
			// WIPTech.debug("Registered custom Mesh Definitions for cased slugs with the Model Loader");

			if (material.getProperties().hasOre()) {
				if (material.getOre() != null) {
					this.registerBlockModMaterialItemBlockModel(material.getOre());
				}
			}

			if (material.getProperties().hasBlock()) {
				if (material.getBlock() != null) {
					this.registerBlockModMaterialItemBlockModel(material.getBlock());
				}
			}

			if (material.getProperties().hasResource()) {
				if ((material.getResource() != null) && material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix().toLowerCase(), ForgeRegistries.ITEMS).equals(material.getModId())) {
					this.registerBlockModMaterialItemBlockModel(material.getResource());
				}
				if ((material.getResourcePiece() != null) && material.getResouceLocationDomainWithOverrides(material.getProperties().getResourcePieceSuffix().toLowerCase(), ForgeRegistries.ITEMS).equals(material.getModId())) {
					this.registerBlockModMaterialItemBlockModel(material.getResourcePiece());
				}
			}

			if (material.getProperties().hasWire()) {
				if (material.getWire() != null) {
					this.registerBlockModMaterialItemBlockModel(material.getWire());
				}
				if (material.getSpool() != null) {
					this.registerBlockModMaterialItemBlockModel(material.getSpool());
				}
			}

			if (material.getProperties().hasEnamel()) {
				if (material.getEnamel() != null) {
					this.registerBlockModMaterialItemBlockModel(material.getEnamel());
				}
			}

			if (material.getProperties().hasHelmet()) {
				if (material.getHelmet() != null) {
					this.registerItemModMaterialModel(material.getHelmet());
				}
			}
			if (material.getProperties().hasChestplate()) {
				if (material.getChestplate() != null) {
					this.registerItemModMaterialModel(material.getChestplate());
				}
			}
			if (material.getProperties().hasLeggings()) {
				if (material.getLeggings() != null) {
					this.registerItemModMaterialModel(material.getLeggings());
				}
			}
			if (material.getProperties().hasBoots()) {
				if (material.getBoots() != null) {
					this.registerItemModMaterialModel(material.getBoots());
				}
			}
			if (material.getProperties().hasHorseArmor()) {
				if (material.getHorseArmor() != null) {
					this.registerItemModMaterialModel(material.getHorseArmor());
				}
			}

			if (material.getProperties().hasPickaxe()) {
				if (material.getPickaxe() != null) {
					this.registerItemModMaterialModel(material.getPickaxe());
				}
			}
			if (material.getProperties().hasAxe()) {
				if (material.getAxe() != null) {
					this.registerItemModMaterialModel(material.getAxe());
				}
			}
			if (material.getProperties().hasSword()) {
				if (material.getSword() != null) {
					this.registerItemModMaterialModel(material.getSword());
				}
			}
			if (material.getProperties().hasShovel()) {
				if (material.getShovel() != null) {
					this.registerItemModMaterialModel(material.getShovel());
				}
			}
			if (material.getProperties().hasHoe()) {
				if (material.getHoe() != null) {
					this.registerItemModMaterialModel(material.getHoe());
				}
			}

			if (material.getProperties().hasCoil()) {
				if (material.getCoil() != null) {
					this.registerItemModMaterialModel(material.getCoil());
				}
			}

			if (material.getProperties().hasRail()) {
				if (material.getRail() != null) {
					this.registerItemModMaterialModel(material.getRail());
				}
			}

			if (material.getProperties().hasRailgunSlug()) {
				if (material.getSlugItem() != null) {
					this.registerItemModMaterialModel(material.getSlugItem());
				}
				// if (material.getCasedSlug() != null) {
				// registerItemModMaterialModel(material.getCasedSlug());
				// }
			}

			WIPTech.debug("Registered models for materials");
		}

		@SideOnly(Side.CLIENT)
		private <T extends Item & IItemModMaterial> void registerItemModMaterialModel(final T item) {
			final boolean isVanilla = item.getRegistryName().getResourceDomain().equals("minecraft");
			final String registryNameResourceDomain = isVanilla ? "minecraft" : item.getModMaterial().getModId().toString();
			final String registryNameResourcePath = item.getRegistryName().getResourcePath();

			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ModResourceLocation(registryNameResourceDomain, registryNameResourcePath), ClientEventSubscriber.DEFAULT_VARIANT));
		}

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
				ModMaterial.this.ore = (BlockModOre) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "ore")));
				ModMaterial.this.block = (BlockResource) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "block")));
				ModMaterial.this.resource = (BlockItem) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "resource")));
				ModMaterial.this.resourcePiece = (BlockItem) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "resourcePiece")));
				ModMaterial.this.wire = (BlockWire) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "wire")));
				ModMaterial.this.spool = (BlockSpool) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "spool")));
				ModMaterial.this.enamel = (BlockEnamel) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "enamel")));
			}

			if (registry == ForgeRegistries.ITEMS) {
				ModMaterial.this.itemBlockOre = (ModItemBlock) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "ore")));
				ModMaterial.this.itemBlockBlock = (ModItemBlock) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "block")));
				ModMaterial.this.itemBlockResource = (ModItemBlock) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "resource")));
				ModMaterial.this.itemBlockResourcePiece = (ModItemBlock) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "resourcePiece")));
				ModMaterial.this.itemBlockWire = (ModItemBlock) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "wire")));
				ModMaterial.this.itemBlockSpool = (ModItemBlock) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "spool")));
				ModMaterial.this.itemBlockEnamel = (ModItemBlock) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "enamel")));

				ModMaterial.this.helmet = (ItemModArmor) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "helmet")));
				ModMaterial.this.chestplate = (ItemModArmor) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "chestplate")));
				ModMaterial.this.leggings = (ItemModArmor) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "leggings")));
				ModMaterial.this.boots = (ItemModArmor) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "boots")));
				ModMaterial.this.horseArmor = (ItemModHorseArmor) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "horseArmor")));
				ModMaterial.this.pickaxe = (ItemModPickaxe) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "pickaxe")));
				ModMaterial.this.axe = (ItemModAxe) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "axe")));
				ModMaterial.this.sword = (ItemModSword) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "sword")));
				ModMaterial.this.shovel = (ItemModShovel) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "shovel")));
				ModMaterial.this.hoe = (ItemModHoe) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "hoe")));
				ModMaterial.this.coil = (ItemCoil) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "coil")));
				ModMaterial.this.rail = (ItemRail) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "rail")));
				ModMaterial.this.slugItem = (ItemSlug) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "slug")));
				ModMaterial.this.casedSlug = (ItemCasedSlug) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath("cased" + "_" + ModMaterial.this.getNameLowercase() + "_" + "slug")));
			}

			if (registry == ForgeRegistries.ENTITIES) {
				ModMaterial.this.slugEntity = (EntityEntry) registry.getValue(new ModResourceLocation(ModMaterial.this.getResouceLocationDomainWithOverrides("ore", registry), new ModResourceLocationPath(ModMaterial.this.getNameLowercase() + "_" + "slug")));
			}

		}

		// TODO: texturestich?
	}

	private final MaterialEventSubscriber eventSubscriber = new MaterialEventSubscriber();
	public MaterialEventSubscriber getEventSubscriber() {
		return this.eventSubscriber;
	}

}
