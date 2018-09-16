package cadiboo.wiptech;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import cadiboo.wiptech.block.BlockAssemblyTable;
import cadiboo.wiptech.block.BlockEnamel;
import cadiboo.wiptech.block.BlockItem;
import cadiboo.wiptech.block.BlockModFurnace;
import cadiboo.wiptech.block.BlockModOre;
import cadiboo.wiptech.block.BlockPeripheral;
import cadiboo.wiptech.block.BlockResource;
import cadiboo.wiptech.block.BlockSpool;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.block.IBlockModMaterial;
import cadiboo.wiptech.capability.energy.network.CapabilityEnergyNetworkList;
import cadiboo.wiptech.capability.energy.network.EnergyNetwork;
import cadiboo.wiptech.capability.energy.network.EnergyNetworkList;
import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.client.model.ModelsCache;
import cadiboo.wiptech.client.render.block.model.GlitchModelLoader;
import cadiboo.wiptech.client.render.block.model.WireModelLoader;
import cadiboo.wiptech.client.render.entity.EntityNapalmRenderer;
import cadiboo.wiptech.client.render.entity.EntityPortableGeneratorRenderer;
import cadiboo.wiptech.client.render.entity.EntityRailgunRenderer;
import cadiboo.wiptech.client.render.entity.EntitySlugCasingRenderer;
import cadiboo.wiptech.client.render.entity.EntitySlugRenderer;
import cadiboo.wiptech.client.render.tileentity.TileEntityAssemblyTableRenderer;
import cadiboo.wiptech.client.render.tileentity.TileEntityEnamelRenderer;
import cadiboo.wiptech.client.render.tileentity.TileEntityModFurnaceRenderer;
import cadiboo.wiptech.client.render.tileentity.TileEntityWireRenderer;
import cadiboo.wiptech.entity.IModEntity;
import cadiboo.wiptech.entity.item.EntityPortableGenerator;
import cadiboo.wiptech.entity.item.EntityRailgun;
import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.entity.projectile.EntitySlug;
import cadiboo.wiptech.entity.projectile.EntitySlugCasing;
import cadiboo.wiptech.event.ModFurnaceItemSmeltTimeEvent;
import cadiboo.wiptech.init.ModBlocks;
import cadiboo.wiptech.init.ModItems;
import cadiboo.wiptech.item.IItemModMaterial;
import cadiboo.wiptech.item.ItemCasedSlug;
import cadiboo.wiptech.item.ItemCircuit;
import cadiboo.wiptech.item.ItemCoil;
import cadiboo.wiptech.item.ItemFlamethrower;
import cadiboo.wiptech.item.ItemGrenadeLauncher;
import cadiboo.wiptech.item.ItemHandheldCoilgun;
import cadiboo.wiptech.item.ItemHandheldCoilgunPistol;
import cadiboo.wiptech.item.ItemHandheldPlasmagun;
import cadiboo.wiptech.item.ItemHandheldRailgun;
import cadiboo.wiptech.item.ItemHeartbeatSensor;
import cadiboo.wiptech.item.ItemLaser;
import cadiboo.wiptech.item.ItemModArmor;
import cadiboo.wiptech.item.ItemModAxe;
import cadiboo.wiptech.item.ItemModHoe;
import cadiboo.wiptech.item.ItemModHorseArmor;
import cadiboo.wiptech.item.ItemModPickaxe;
import cadiboo.wiptech.item.ItemModShovel;
import cadiboo.wiptech.item.ItemModSword;
import cadiboo.wiptech.item.ItemPortableGenerator;
import cadiboo.wiptech.item.ItemRail;
import cadiboo.wiptech.item.ItemRailgun;
import cadiboo.wiptech.item.ItemScope;
import cadiboo.wiptech.item.ItemShotgun;
import cadiboo.wiptech.item.ItemSlug;
import cadiboo.wiptech.item.ItemSlugCasing;
import cadiboo.wiptech.item.ModItemBlock;
import cadiboo.wiptech.network.ModNetworkManager;
import cadiboo.wiptech.network.play.server.SPacketSyncEnergyNetworkList;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.tileentity.TileEntityEnamel;
import cadiboo.wiptech.tileentity.TileEntityModFurnace;
import cadiboo.wiptech.tileentity.TileEntityPeripheral;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.ExistsForDebugging;
import cadiboo.wiptech.util.ModEnums.CircuitTypes;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModEnums.ScopeTypes;
import cadiboo.wiptech.util.ModEnums.SlugCasingParts;
import cadiboo.wiptech.util.ModMaterialProperties;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModResourceLocation;
import cadiboo.wiptech.util.ModUtil;
import cadiboo.wiptech.util.ModWritingUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
public final class EventSubscriber {

	private static int entityId = 0;

	/* register blocks */
	@SubscribeEvent
	public static void onRegisterBlocksEvent(final RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();

		registerBlocksForMaterials(registry);

		registry.register(new BlockModFurnace("mod_furnace"));

		registry.register(new BlockAssemblyTable("assembly_table"));

		registry.register(new BlockPeripheral("peripheral"));

		WIPTech.info("Registered blocks");

		registerTileEntities();

		WIPTech.debug("Registered tile entities");

	}

	private static void registerBlocksForMaterials(final IForgeRegistry<Block> registry) {
		for (final ModMaterials material : ModMaterials.values()) {
			if (material.getProperties().hasOre()) {
				registry.register(new BlockModOre(material));
			}

			if (material.getProperties().hasBlock()) {
				registry.register(new BlockResource(material));
			}

			if (material.getProperties().hasResource()) {
				registry.register(new BlockItem(material, material.getType().getResourceBlockItemType()));
				if (material.getType().hasResourcePiece()) {
					registry.register(new BlockItem(material, material.getType().getResourcePieceBlockItemType()));
				}
			}

			if (material.getProperties().hasWire()) {
				registry.register(new BlockWire(material));
				registry.register(new BlockSpool(material));
			}

			if (material.getProperties().hasEnamel()) {
				registry.register(new BlockEnamel(material));
			}

		}
		WIPTech.debug("Registered blocks for materials");
	}

	private static void registerTileEntities() {
		registerTileEntity(TileEntityWire.class);
		registerTileEntity(TileEntityEnamel.class);
		registerTileEntity(TileEntityModFurnace.class);
		registerTileEntity(TileEntityAssemblyTable.class);
		registerTileEntity(TileEntityPeripheral.class);
	}

	private static void registerTileEntity(final Class<? extends TileEntity> clazz) {
		try {
			GameRegistry.registerTileEntity(clazz, new ModResourceLocation(ModReference.MOD_ID, ModUtil.getRegistryNameForClass(clazz, "TileEntity")));
		} catch (final Exception e) {
			WIPTech.error("Error registering Tile Entity " + clazz.getSimpleName());
			e.printStackTrace();
		}
	}

	/* register items */
	@SubscribeEvent
	public static void onRegisterItemsEvent(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(new ModItemBlock(ModBlocks.MOD_FURNACE));

		registry.register(new ModItemBlock(ModBlocks.ASSEMBLY_TABLE));

//		registry.register(new ModItemBlock(ModBlocks.PERIPHERAL));

		//

		registerItemsForMaterials(registry);
		registerItemsForAttachments(registry);

		registry.register(new ItemPortableGenerator("portable_generator"));

		registry.register(new ItemFlamethrower("flamethrower"));

		registry.register(new ItemRailgun("railgun"));

		registry.register(new ItemSlugCasing("slug_casing_back", SlugCasingParts.BACK));
		registry.register(new ItemSlugCasing("slug_casing_top", SlugCasingParts.TOP));
		registry.register(new ItemSlugCasing("slug_casing_bottom", SlugCasingParts.BOTTOM));

		registry.register(new ItemHandheldRailgun("handheld_railgun"));
		registry.register(new ItemHandheldCoilgun("handheld_coilgun"));
		registry.register(new ItemHandheldCoilgunPistol("handheld_coilgun_pistol"));
		registry.register(new ItemHandheldPlasmagun("handheld_plasmagun"));

		WIPTech.info("Registered items");

	}

	private static void registerItemsForMaterials(final IForgeRegistry<Item> registry) {
		for (final ModMaterials material : ModMaterials.values()) {
			if (material.getProperties().hasOre()) {
				registry.register(new ModItemBlock(material.getOre(), new ModResourceLocation(material.getResouceLocationDomain("ore", ForgeRegistries.ITEMS), material.getNameLowercase() + "_ore")));
			}

			if (material.getProperties().hasBlock()) {
				registry.register(new ModItemBlock(material.getBlock(), new ModResourceLocation(material.getResouceLocationDomain("block", ForgeRegistries.ITEMS), material.getNameLowercase() + "_block")));
			}

			if (material.getProperties().hasResource()) {
				registry.register(new ModItemBlock(material.getResource(), new ModResourceLocation(material.getResouceLocationDomain(material.getType().getResourceNameSuffix().toLowerCase(), ForgeRegistries.ITEMS), material.getNameLowercase() + "_" + material.getType().getResourceNameSuffix().toLowerCase())));
				if (material.getType().hasResourcePiece()) {
					registry.register(new ModItemBlock(material.getResourcePiece(), new ModResourceLocation(material.getResouceLocationDomain(material.getType().getResourcePieceNameSuffix().toLowerCase(), ForgeRegistries.ITEMS), material.getNameLowercase() + "_" + material.getType().getResourcePieceNameSuffix().toLowerCase())));
				}
			}

			if (material.getProperties().hasWire()) {
				registry.register(new ModItemBlock(material.getWire()));
				registry.register(new ModItemBlock(material.getSpool()));
			}

			if (material.getProperties().hasEnamel()) {
				registry.register(new ModItemBlock(material.getEnamel()));
			}

			if (material.getProperties().hasArmor()) {
				registry.register(new ItemModArmor(material, EntityEquipmentSlot.HEAD));
				registry.register(new ItemModArmor(material, EntityEquipmentSlot.CHEST));
				registry.register(new ItemModArmor(material, EntityEquipmentSlot.LEGS));
				registry.register(new ItemModArmor(material, EntityEquipmentSlot.FEET));
				registry.register(new ItemModHorseArmor(material));
			}

			if (material.getProperties().hasTools()) {
				registry.register(new ItemModPickaxe(material));
				registry.register(new ItemModAxe(material));
				registry.register(new ItemModSword(material));
				registry.register(new ItemModShovel(material));
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

		}

		WIPTech.debug("Registered items for materials");

	}

	private static void registerItemsForAttachments(final IForgeRegistry<Item> registry) {

		for (final CircuitTypes type : CircuitTypes.values()) {
			registry.register(new ItemCircuit("circuit", type));
		}

		for (final ScopeTypes type : ScopeTypes.values()) {
			registry.register(new ItemScope("scope", type));
		}

		registry.register(new ItemShotgun("shotgun"));
		registry.register(new ItemGrenadeLauncher("grenade_launcher"));

		registry.register(new ItemHeartbeatSensor("heartbeat_sensor"));
		registry.register(new ItemLaser("laser"));

	}

	/* register entities */
	@SubscribeEvent
	public static void onRegisterEntitiesEvent(final RegistryEvent.Register<EntityEntry> event) {
		final IForgeRegistry<EntityEntry> registry = event.getRegistry();

		registerEntitiesForMaterials(registry);

		event.getRegistry().register(buildEntityEntryFromClass(EntityPortableGenerator.class, false, 64, 2, true));

		event.getRegistry().register(buildEntityEntryFromClass(EntityRailgun.class, false, 64, 2, true));

		event.getRegistry().register(buildEntityEntryFromClass(EntitySlugCasing.class, false, 128, 2, true));

		event.getRegistry().register(buildEntityEntryFromClass(EntityNapalm.class, false, 128, 2, true));

		// TODO: register shotgun bullet;

		// TODO: register grenade bullet;

		WIPTech.info("Registered entities");

	}

	private static void registerEntitiesForMaterials(final IForgeRegistry<EntityEntry> registry) {
		// TODO AdditionalSpawnData maybe?
		for (final ModMaterials material : ModMaterials.values()) {
			if (material.getProperties().hasRailgunSlug()) {
				registry.register(buildEntityEntryFromClassWithName(EntitySlug.class, new ModResourceLocation(ModReference.MOD_ID, material.getNameLowercase() + "_slug"), false, 128, 2, true));
			}
		}
		WIPTech.debug("Registered entities for materials");
	}

	private static <T extends Entity & IModEntity> EntityEntry buildEntityEntryFromClass(final Class<T> clazz, final boolean hasEgg, final int range, final int updateFrequency, final boolean sendVelocityUpdates) {
		return buildEntityEntryFromClassWithName(clazz, new ModResourceLocation(ModReference.MOD_ID, ModUtil.getRegistryNameForClass(clazz, "Entity")), hasEgg, range, updateFrequency, sendVelocityUpdates);
	}

	private static <T extends Entity & IModEntity> EntityEntry buildEntityEntryFromClassWithName(final Class<T> clazz, final ModResourceLocation registryName, final boolean hasEgg, final int range, final int updateFrequency, final boolean sendVelocityUpdates) {
		EntityEntryBuilder<Entity> builder = EntityEntryBuilder.create();
		builder = builder.entity(clazz);
		builder = builder.id(registryName, entityId++);
		builder = builder.name(registryName.getResourcePath());
		builder = builder.tracker(range, updateFrequency, sendVelocityUpdates);

		if (hasEgg) {
			builder = builder.egg(0xFFFFFF, 0xAAAAAA);
		}

		return builder.build();
	}

	/* register models */
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRegisterModelsEvent(final ModelRegistryEvent event) {

		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(ModelsCache.INSTANCE);
		WIPTech.info("Registered resource manager reload listener for " + ModelsCache.class.getSimpleName());

		registerTileEntitySpecialRenderers();
		WIPTech.info("Registered tile entity special renderers");

		registerEntityRenderers();
		WIPTech.info("Registered entity renderers");

		registerModelsForMaterials();
		registerModelsForAttachments();

		/* item blocks */
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.MOD_FURNACE), 0, new ModelResourceLocation(ModBlocks.MOD_FURNACE.getRegistryName(), "facing=north"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.ASSEMBLY_TABLE), 0, new ModelResourceLocation(ModBlocks.ASSEMBLY_TABLE.getRegistryName(), ModWritingUtil.default_variant_name));

		/* items */
		registerNormalItemModel(ModItems.PORTABLE_GENERATOR);
		registerNormalItemModel(ModItems.FLAMETHROWER);
		registerNormalItemModel(ModItems.RAILGUN);
		registerNormalItemModel(ModItems.SLUG_CASING_BACK);
		registerNormalItemModel(ModItems.SLUG_CASING_TOP);
		registerNormalItemModel(ModItems.SLUG_CASING_BOTTOM);

		registerNormalItemModel(ModItems.HANDHELD_RAILGUN);
		registerNormalItemModel(ModItems.HANDHELD_COILGUN);
		registerNormalItemModel(ModItems.HANDHELD_COILGUN_PISTOL);
		registerNormalItemModel(ModItems.HANDHELD_PLASMAGUN);

		WIPTech.info("Registered models");

	}

	@SideOnly(Side.CLIENT)
	private static void registerTileEntitySpecialRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWire.class, new TileEntityWireRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnamel.class, new TileEntityEnamelRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityModFurnace.class, new TileEntityModFurnaceRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAssemblyTable.class, new TileEntityAssemblyTableRenderer());
	}

	@SideOnly(Side.CLIENT)
	private static void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityPortableGenerator.class, renderManager -> new EntityPortableGeneratorRenderer(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntitySlugCasing.class, renderManager -> new EntitySlugCasingRenderer(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntitySlug.class, renderManager -> new EntitySlugRenderer(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityRailgun.class, renderManager -> new EntityRailgunRenderer(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityNapalm.class, renderManager -> new EntityNapalmRenderer(renderManager));
	}

	@SideOnly(Side.CLIENT)
	private static void registerModelsForMaterials() {
		for (final ModMaterials material : ModMaterials.values()) {

			if (material.getProperties().hasWire()) {
				ModelLoader.setCustomStateMapper(material.getWire(), new StateMapperBase() {
					@Override
					protected ModelResourceLocation getModelResourceLocation(final IBlockState iBlockState) {
						return new ModelResourceLocation(new ModResourceLocation(material.getAssetsModId(), material.getNameLowercase() + "_wire"), ModWritingUtil.default_variant_name);
					}
				});
			}

			if (material.getProperties().hasEnamel()) {
				ModelLoader.setCustomStateMapper(material.getEnamel(), new StateMapperBase() {

					@Override
					protected ModelResourceLocation getModelResourceLocation(final IBlockState iBlockState) {
						return new ModelResourceLocation(new ModResourceLocation(material.getAssetsModId(), material.getNameLowercase() + "_enamel"), ModWritingUtil.default_variant_name);
					}
				});
			}

		}
		ModelLoaderRegistry.registerLoader(new WireModelLoader());
		WIPTech.debug("Registered custom State Mappers for wires and enamels with the Model Loader");

		if (ModMaterials.GLITCH.getProperties().hasBlock() || ModMaterials.GLITCH.getProperties().hasOre()) {
			if (ModMaterials.GLITCH.getBlock() != null) {
				ModelLoader.setCustomStateMapper(ModMaterials.GLITCH.getBlock(), new StateMapperBase() {
					@Override
					protected ModelResourceLocation getModelResourceLocation(final IBlockState iBlockState) {
						return new ModelResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "glitch_block"), ModWritingUtil.default_variant_name);
					}
				});
			}
			if (ModMaterials.GLITCH.getOre() != null)

			{
				ModelLoader.setCustomStateMapper(ModMaterials.GLITCH.getOre(), new StateMapperBase() {
					@Override
					protected ModelResourceLocation getModelResourceLocation(final IBlockState iBlockState) {
						return new ModelResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "glitch_ore"), ModWritingUtil.default_variant_name);
					}
				});
			}
		}
		ModelLoaderRegistry.registerLoader(new GlitchModelLoader());
		WIPTech.debug("Registered custom State Mapper(s) for glitch block and ore with the Model Loader");

		for (final ModMaterials material : ModMaterials.values()) {

			if (material.getProperties().hasRailgunSlug()) {
				// FIXME TODO re-enable this & make it work
//				ModelLoader.setCustomMeshDefinition(material.getCasedSlug(), stack -> new ModelResourceLocation(new ModResourceLocation(material.getAssetsModId(), "cased_" + material.getNameLowercase() + "_slug"), ModWritingUtil.default_variant_name));
			}

		}

//		ModelLoaderRegistry.registerLoader(new CasedSlugModelLoader());
//		WIPTech.debug("Registered custom Mesh Definitions for cased slugs with the Model Loader");

		for (final ModMaterials material : ModMaterials.values()) {
			if (material.getProperties().hasOre()) {
				if (material.getOre() != null) {
					registerBlockModMaterialItemBlockModel(material.getOre());
				}
			}

			if (material.getProperties().hasBlock()) {
				if (material.getBlock() != null) {
					registerBlockModMaterialItemBlockModel(material.getBlock());
				}
			}

			if (material.getProperties().hasResource()) {
				if ((material.getResource() != null) && material.getResouceLocationDomain(material.getType().getResourceNameSuffix().toLowerCase(), ForgeRegistries.ITEMS).equals(material.getAssetsModId())) {
					registerBlockModMaterialItemBlockModel(material.getResource());
				}
				if ((material.getResourcePiece() != null) && material.getResouceLocationDomain(material.getType().getResourcePieceNameSuffix().toLowerCase(), ForgeRegistries.ITEMS).equals(material.getAssetsModId())) {
					registerBlockModMaterialItemBlockModel(material.getResourcePiece());
				}
			}

			if (material.getProperties().hasWire()) {
				if (material.getWire() != null) {
					registerBlockModMaterialItemBlockModel(material.getWire());
				}
				if (material.getSpool() != null) {
					registerBlockModMaterialItemBlockModel(material.getSpool());
				}
			}

			if (material.getProperties().hasEnamel()) {
				if (material.getEnamel() != null) {
					registerBlockModMaterialItemBlockModel(material.getEnamel());
				}
			}

			if (material.getProperties().hasArmor()) {
				if (material.getHelmet() != null) {
					registerItemModMaterialModel(material.getHelmet());
				}
				if (material.getChestplate() != null) {
					registerItemModMaterialModel(material.getChestplate());
				}
				if (material.getLeggings() != null) {
					registerItemModMaterialModel(material.getLeggings());
				}
				if (material.getBoots() != null) {
					registerItemModMaterialModel(material.getBoots());
				}
				if (material.getHorseArmor() != null) {
					registerItemModMaterialModel(material.getHorseArmor());
				}
			}

			if (material.getProperties().hasTools()) {
				if (material.getPickaxe() != null) {
					registerItemModMaterialModel(material.getPickaxe());
				}
				if (material.getAxe() != null) {
					registerItemModMaterialModel(material.getAxe());
				}
				if (material.getSword() != null) {
					registerItemModMaterialModel(material.getSword());
				}
				if (material.getShovel() != null) {
					registerItemModMaterialModel(material.getShovel());
				}
				if (material.getHoe() != null) {
					registerItemModMaterialModel(material.getHoe());
				}
			}

			if (material.getProperties().hasCoil()) {
				if (material.getCoil() != null) {
					registerItemModMaterialModel(material.getCoil());
				}
			}

			if (material.getProperties().hasRail()) {
				if (material.getRail() != null) {
					registerItemModMaterialModel(material.getRail());
				}
			}

			if (material.getProperties().hasRailgunSlug()) {
				if (material.getSlugItem() != null) {
					registerItemModMaterialModel(material.getSlugItem());
				}
//				if (material.getCasedSlug() != null) {
//					registerItemModMaterialModel(material.getCasedSlug());
//				}
			}

		}
		WIPTech.debug("Registered models for materials");
	}

	private static void registerModelsForAttachments() {

		for (final CircuitTypes type : CircuitTypes.values()) {
			registerNormalItemModel(type.getItem("circuit"));
		}

		for (final ScopeTypes type : ScopeTypes.values()) {
			registerNormalItemModel(type.getItem("scope"));
		}

		registerNormalItemModel(ModItems.SHOTGUN);
		registerNormalItemModel(ModItems.GRENADE_LAUNCHER);

		registerNormalItemModel(ModItems.HEARTBEAT_SENSOR);
		registerNormalItemModel(ModItems.LASER);

	}

	@SideOnly(Side.CLIENT)
	private static <T extends Item & IItemModMaterial> void registerItemModMaterialModel(final T item) {
		final boolean isVanilla = item.getRegistryName().getResourceDomain().equals("minecraft");
		final String registryNameResourceDomain = isVanilla ? "minecraft" : item.getModMaterial().getAssetsModId();
		final String registryNameResourcePath = item.getRegistryName().getResourcePath();

		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ModResourceLocation(registryNameResourceDomain, registryNameResourcePath), ModWritingUtil.default_variant_name));
	}

	@SideOnly(Side.CLIENT)
	private static <T extends Block & IBlockModMaterial> void registerBlockModMaterialItemBlockModel(final T block) {
		final boolean isVanilla = block.getRegistryName().getResourceDomain().equals("minecraft");
		final String registryNameResourceDomain = isVanilla ? "minecraft" : block.getModMaterial().getAssetsModId();
		final String registryNameResourcePath = block.getRegistryName().getResourcePath();

		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ModResourceLocation(registryNameResourceDomain, registryNameResourcePath), ModWritingUtil.default_variant_name));
	}

	@SideOnly(Side.CLIENT)
	private static void registerNormalItemModel(final Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), ModWritingUtil.default_variant_name));
	}

	@SideOnly(Side.CLIENT)
	private static void registerNormalBlockModel(final Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), ModWritingUtil.default_variant_name));
	}

	/* injected textures */
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onTextureStichEvent(final TextureStitchEvent event) {

		final TextureMap map = event.getMap();

		injectTextures(map);

	}

	@SideOnly(Side.CLIENT)
	private static void injectTextures(final TextureMap map) {
		final HashSet<ModResourceLocation> modelLocations = new HashSet<>();

		if (ModMaterials.GLITCH != null) {
			final ModMaterialProperties properties = ModMaterials.GLITCH.getProperties();
			if (properties.hasOre()) {
				modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "block/missing_ore"));
				modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_ore"));
			}
			if (properties.hasBlock()) {
				modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "block/missing_block"));
				modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_block"));
			}

			if (properties.hasResource()) {
				final String resourceSuffix = ModMaterials.GLITCH.getType().getResourceNameSuffix();
				modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "item/missing_" + resourceSuffix));
				modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "item/invisible_" + resourceSuffix));

				if (ModMaterials.GLITCH.getType().hasResourcePiece()) {
					final String resourcePieceSuffix = ModMaterials.GLITCH.getType().getResourcePieceNameSuffix();
					modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "item/missing_" + resourcePieceSuffix));
					modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "item/invisible_" + resourcePieceSuffix));
				}
			}

			if (properties.hasArmor()) {
				for (final String suffix : new String[] { "helmet", "chestplate", "leggings", "boots" }) {
					modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "item/missing_" + suffix));
					modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "item/invisible_" + suffix));
				}
				// TODO: horse armor
			}

			if (properties.hasTools()) {
				for (final String suffix : new String[] { "pickaxe", "axe", "sword", "shovel", "hoe" }) {
					modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "item/missing_" + suffix));
					modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "item/invisible_" + suffix));
				}
			}

			if (properties.hasRailgunSlug()) {
				// FIXME TODO: make it work
			}

			if (properties.hasWire()) {
				// idk what to do here
			}

			if (properties.hasEnamel()) {
				// idk what to do here
			}

			if (properties.hasCoil()) {
				modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "item/missing_" + "coil"));
				modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "item/invisible_" + "coil"));

				modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "block/missing_spool"));
				modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_spool"));
			}

			if (properties.hasRail()) {
				modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "item/missing_" + "rail"));
				modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "item/invisible_" + "rail"));
			}

		}

		for (final ModResourceLocation modelLocation : modelLocations) {
			final IModel model = ModelsCache.INSTANCE.getModel(modelLocation);

			for (final ResourceLocation textureLocation : model.getTextures()) {
				map.registerSprite(textureLocation);
			}
		}

	}

	@SideOnly(Side.CLIENT)
	public static void onHorseArmorTexture() {
		// TODO: submit a pull to forge for this
	}

	/* injected models */
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelBakeEvent(final ModelBakeEvent event) {
		final IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();

		injectModels(registry);
		WIPTech.info("Injected models");

	}

	@SideOnly(Side.CLIENT)
	private static void injectModels(final IRegistry<ModelResourceLocation, IBakedModel> registry) {
		final HashSet<ModResourceLocation> modelLocations = new HashSet<>();

		modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "entity/portable_generator_handle"));
		modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "entity/portable_generator_wheel"));
		modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "entity/railgun_base"));
		modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "entity/railgun_turret"));
		modelLocations.add(new ModResourceLocation(ModReference.MOD_ID, "entity/railgun_gun"));
		// models.add(new ModResourceLocation(ModReference.MOD_ID, "entity/slug_casing_back"));
		// models.add(new ModResourceLocation(ModReference.MOD_ID, "entity/slug_casing_top"));
		// models.add(new ModResourceLocation(ModReference.MOD_ID, "entity/slug_casing_bottom"));

		for (final ModResourceLocation modelLocation : modelLocations) {
			try {
				/* modified from code made by Draco18s */
				final ModelResourceLocation location = new ModelResourceLocation(modelLocation.toString());

				final IBakedModel bakedModel = ModelsCache.INSTANCE.getBakedModel(modelLocation);

				registry.putObject(location, bakedModel);
				WIPTech.debug("Sucessfully injected " + modelLocation.toString() + " into Model Registry");
			} catch (final Exception e) {
				WIPTech.error("Error injecting model " + modelLocation.toString() + " into Model Registry");
			}
		}
	}

	/* world rendering */
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRenderWorldLast(final RenderWorldLastEvent event) {

		final World world = Minecraft.getMinecraft().world;
		if (world == null) {
			return;
		}

		renderSelectedBoundingBoxes(event);

		renderEnergyNetworks(world, event.getPartialTicks());

	}

	private static void renderSelectedBoundingBoxes(final RenderWorldLastEvent event) {

		if (Boolean.valueOf(true)) {
			return;
		}

		final World world = Minecraft.getMinecraft().world;
		if (world == null) {
			return;
		}

		final RayTraceResult rayTraceResult = Minecraft.getMinecraft().objectMouseOver;
		if (rayTraceResult == null) {
			return;
		}

		if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) {
			return;
		}

		final BlockPos pos = rayTraceResult.getBlockPos();

		final IBlockState blockState = world.getBlockState(pos);

		final Block block = blockState.getBlock();

		if (!(block instanceof BlockWire)) {
			return;
		}

		final List<AxisAlignedBB> AABBs = new ArrayList<>();

		blockState.addCollisionBoxToList(world, pos, new AxisAlignedBB(pos), AABBs, Minecraft.getMinecraft().player, true);

		final float partialTicks = event.getPartialTicks();

		// Usually the player
		final Entity entity = Minecraft.getMinecraft().getRenderViewEntity();

		// Interpolating everything back to 0,0,0. These are transforms you can find at RenderEntity class
		final double d0 = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partialTicks);
		final double d1 = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks);
		final double d2 = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partialTicks);

		// Apply 0-our transforms to set everything back to 0,0,0
		Tessellator.getInstance().getBuffer().setTranslation(-d0, -d1, -d2);

		// FIXME TODO: remove the intersecting lines
		for (final AxisAlignedBB AABB : AABBs) {
			if (AABB.equals(BlockWire.CORE_AABB.offset(pos))) {
				continue;
			}
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.glLineWidth(2.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);

			if ((blockState.getMaterial() != Material.AIR) && world.getWorldBorder().contains(pos)) {
				RenderGlobal.drawSelectionBoundingBox(AABB.grow(0.0020000000949949026D), 0.0F, 0.0F, 0.0F, 0.4F);
			}

			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}
		// When you are done rendering all your boxes reset the offsets. We do not want everything that renders next to still be at 0,0,0 :)
		Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);

	}

	@SideOnly(Side.CLIENT)
	private static void renderEnergyNetworks(final World world, final float partialTicks) {

		if (!ModReference.Debug.debugEnergyNetworks()) {
//			return;
		}

		final ItemStack check = Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

		if (check.isEmpty()) {
			return;
		}

		final Block block = Block.getBlockFromItem(check.getItem());

		if (!(block instanceof BlockWire)) {
			return;
		}

		if (world == null) {
			return;
		}

		final EnergyNetworkList list = world.getCapability(CapabilityEnergyNetworkList.NETWORK_LIST, null);
		if (list == null) {
			return;
		}
		GlStateManager.enableBlend();

		// Usually the player
		final Entity entity = Minecraft.getMinecraft().getRenderViewEntity();

		// Interpolating everything back to 0,0,0. These are transforms you can find at RenderEntity class
		final double d0 = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partialTicks);
		final double d1 = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks);
		final double d2 = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partialTicks);

		// Apply 0-our transforms to set everything back to 0,0,0
		Tessellator.getInstance().getBuffer().setTranslation(-d0, -d1, -d2);

		for (final EnergyNetwork network : list.getNetworks()) {
			final Random rand = new Random(network.hashCode());

			GlStateManager.color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.6f);
			for (final BlockPos pos : network.getConnections()) {

				// our positions
				final int sX = pos.getX();
				final int sY = pos.getY();
				final int sZ = pos.getZ();

				// bind our texture
				Minecraft.getMinecraft().getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/misc/circuits.png"));

				if (pos.distanceSq(d0, d1, d2) > Math.pow(5, 2)) {
					Minecraft.getMinecraft().getTextureManager().getTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/misc/circuits.png")).setBlurMipmap(true, false);
				} else {
					Minecraft.getMinecraft().getTextureManager().getTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/misc/circuits.png")).setBlurMipmap(false, false);
				}

				// actual render function
				ClientUtil.drawCuboidAt(sX + 0.5, sY + 0.5, sZ + 0.5, 0, 1, 0, 1, 0.5, 0.5, 0.5, 1);

			}

		}
		// When you are done rendering all your boxes reset the offsets. We do not want everything that renders next to still be at 0,0,0 :)
		Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);

		GlStateManager.disableBlend();

	}

	@SubscribeEvent
	public static void onRenderGameOverlay(final RenderGameOverlayEvent.Post event) {
		if ((event.getType() != RenderGameOverlayEvent.ElementType.ALL) || (Minecraft.getMinecraft().currentScreen != null)) {
			return;
		}

		IEnergyStorage energy = null;

		final Minecraft mc = Minecraft.getMinecraft();
		final RayTraceResult rayTraceResult = mc.objectMouseOver;
		mc.entityRenderer.getMouseOver(event.getPartialTicks());

		if ((energy == null) && (mc.getRenderViewEntity().getRidingEntity() != null)) {
			if (mc.getRenderViewEntity().getRidingEntity().hasCapability(CapabilityEnergy.ENERGY, null)) {
				energy = mc.getRenderViewEntity().getRidingEntity().getCapability(CapabilityEnergy.ENERGY, null);
			}
		}
		if ((energy == null) && (rayTraceResult != null) && (rayTraceResult.getBlockPos() != null)) {
			final TileEntity tileHit = mc.world.getTileEntity(rayTraceResult.getBlockPos());
			if (tileHit != null) {
				if (tileHit.hasCapability(CapabilityEnergy.ENERGY, rayTraceResult.sideHit)) {
					energy = tileHit.getCapability(CapabilityEnergy.ENERGY, rayTraceResult.sideHit);
				}
			}
		}
		if ((energy == null) && (rayTraceResult != null) && (rayTraceResult.entityHit != null)) {
			if (rayTraceResult.entityHit.hasCapability(CapabilityEnergy.ENERGY, null)) {
				energy = rayTraceResult.entityHit.getCapability(CapabilityEnergy.ENERGY, null);
			}
		}

		if (energy == null) {
			return;
		}

		final double power = (double) energy.getEnergyStored() / (double) energy.getMaxEnergyStored();
		final int scaled_height = (int) Math.round((1 - power) * 52D);
		final ScaledResolution Scaled = new ScaledResolution(Minecraft.getMinecraft());
		final int Width = Scaled.getScaledWidth() - 10;
		final int Height = Scaled.getScaledHeight() - 54;

		mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/gui/energy.png"));

		ClientUtil.drawNonStandardTexturedRect(Width, Height, 0, 0, 10, 54, 256, 256);
		ClientUtil.drawNonStandardTexturedRect(Width + 1, Height + 1 + scaled_height, 10, 0, 8, 52 - scaled_height, 256, 256);
		final int percent = (int) Math.round(power * 100);
		mc.fontRenderer.drawStringWithShadow(percent + "%", Width - 7 - (String.valueOf(percent).length() * 6), Height + 35, 0xFFFFFF);
		final String outOf = energy.getEnergyStored() + "/" + energy.getMaxEnergyStored();
		mc.fontRenderer.drawStringWithShadow(outOf, Width - 1 - (outOf.length() * 6), Height + 45, 0xFFFFFF);

	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void drawBlockHighlightEvent(final DrawBlockHighlightEvent event) {
		try {
			final EntityPlayer player = event.getPlayer();
			if (player == null) {
				return;
			}

			final RayTraceResult rayTraceResult = event.getTarget();
			if ((rayTraceResult == null) || (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK)) {
				return;
			}

			final World world = player.world;
			if (world == null) {
				return;
			}

			final float partialTicks = event.getPartialTicks();
			final BlockPos pos = rayTraceResult.getBlockPos();
			final IBlockState blockState = world.getBlockState(pos);
			if ((blockState.getMaterial() == Material.AIR) || !world.getWorldBorder().contains(pos)) {
				return;
			}

			if (!(blockState.getBlock() instanceof BlockWire) && !ModReference.Debug.debugBoundingBoxes() && !ModReference.Debug.debugCollisionBoxes()) {
				return;
			}

			event.setCanceled(true);

			final AxisAlignedBB oldSelectedBox = blockState.getSelectedBoundingBox(world, pos);

			final List<AxisAlignedBB> boxes = new ArrayList<>();

			blockState.addCollisionBoxToList(world, pos, new AxisAlignedBB(pos), boxes, player, false);

			if (boxes.size() <= 1) {
				boxes.clear();
				boxes.add(oldSelectedBox);
			}

			final double renderX = player.lastTickPosX + ((player.posX - player.lastTickPosX) * partialTicks);
			final double renderY = player.lastTickPosY + ((player.posY - player.lastTickPosY) * partialTicks);
			final double renderZ = player.lastTickPosZ + ((player.posZ - player.lastTickPosZ) * partialTicks);

			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.glLineWidth(2.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);

			for (AxisAlignedBB box : boxes) {
				if (box.equals(BlockWire.CORE_AABB.offset(pos)) && !ModReference.Debug.debugCollisionBoxes()) {
					continue;
				}

				if (!ModReference.Debug.debugCollisionBoxes()) {
					if (box.maxY > (box.minY + 1)) {
						box = box.setMaxY(oldSelectedBox.maxY);
					}
				}

				final AxisAlignedBB renderBox = box.grow(0.0020000000949949026D).offset(-renderX, -renderY, -renderZ);

				if (ModReference.Debug.debugCollisionBoxes()) {
					event.getContext().drawSelectionBoundingBox(renderBox, 1.0F, 0.0F, 0.0F, 0.4F);
					continue;
				}

				if (ModReference.Debug.debugBoundingBoxes()) {
					event.getContext().drawSelectionBoundingBox(renderBox, 0.0F, 1.0F, 1.0F, 0.4F);
					continue;
				}
				event.getContext().drawSelectionBoundingBox(renderBox, 0.0F, 0.0F, 0.0F, 0.4F);

			}

			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		} catch (final Exception e) {
			event.setCanceled(false);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@SideOnly(Side.CLIENT)
	public static void onTooltipEvent(final ItemTooltipEvent event) {

		final Item item = event.getItemStack().getItem();

		if (item.getHorseArmorType(event.getItemStack()) != HorseArmorType.NONE) {
			final HorseArmorType armorType = item.getHorseArmorType(event.getItemStack());

			setTooltip(event, TextFormatting.RESET + "");

			setTooltip(event, WIPTech.proxy.localize("item.modifiers.horse") + ": ");

			final int protection = armorType.getProtection();

			if (protection > 0) {
				setTooltip(event, TextFormatting.BLUE + " " + WIPTech.proxy.localizeAndFormat("attribute.modifier.plus.0", protection, WIPTech.proxy.localize("enchantment.protect.all")));
			} else if (protection < 0) {
				setTooltip(event, TextFormatting.RED + " " + WIPTech.proxy.localizeAndFormat("attribute.modifier.take.0", protection, WIPTech.proxy.localize("enchantment.protect.all")));
			}

		}

		if (!item.getRegistryName().getResourceDomain().equals(ModReference.MOD_ID)) {
			return;
		}

		if (item instanceof ItemCoil) {
			setTooltip(event, WIPTech.proxy.localize("conductivity") + ": " + ((ItemCoil) item).getModMaterial().getProperties().getConductivity() + "");
		}

		if (item instanceof ItemRail) {
			setTooltip(event, WIPTech.proxy.localize("conductivity") + ": " + ((ItemRail) item).getModMaterial().getProperties().getConductivity() + "");
		}

		if ((Block.getBlockFromItem(item) instanceof BlockWire) && !(Block.getBlockFromItem(item) instanceof BlockEnamel)) {
			setTooltip(event, WIPTech.proxy.localize("conductivity") + ": " + ((BlockWire) Block.getBlockFromItem(item)).getModMaterial().getProperties().getConductivity() + "");
			setTooltip(event, WIPTech.proxy.localize("Ouch! Put some insulation around it"));
		}

		if (Block.getBlockFromItem(item) instanceof BlockEnamel) {
			setTooltip(event, WIPTech.proxy.localize("conductivity") + ": " + ((BlockEnamel) Block.getBlockFromItem(item)).getModMaterial().getProperties().getConductivity() + "");
		}

	}

	@SideOnly(Side.CLIENT)
	private static void setTooltip(final ItemTooltipEvent event, final String tooltip) {

		for (int index = 0; index < event.getToolTip().size(); index++) {

			final String line = event.getToolTip().get(index);
			final String check = net.minecraft.util.StringUtils.stripControlCodes(line);
			final String registryName = event.getItemStack().getItem().getRegistryName().toString();

			if (check.equals(registryName)) { // TODO why? and what does this do???
				event.getToolTip().add(index, tooltip);
				return;
			}
		}
		event.getToolTip().add(tooltip);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerInteract(final PlayerInteractEvent event) {
		if ((event.getEntityPlayer() == null) || (event.getEntityPlayer().getRidingEntity() == null) || !(event.getEntityPlayer().getRidingEntity() instanceof EntityRailgun)) {
			return;
		}

		((EntityRailgun) event.getEntityPlayer().getRidingEntity()).shoot();

		return;
	}

	@SubscribeEvent
	public static void onAttachWorldCapabilities(final AttachCapabilitiesEvent<World> event) {
		event.addCapability(new ModResourceLocation(ModReference.MOD_ID, ModUtil.getRegistryNameForClass(CapabilityEnergyNetworkList.class, "Capability")), new ICapabilityProvider() {

			private final EnergyNetworkList energyNetworkList = new EnergyNetworkList(event.getObject());

			@Override
			public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
				return capability == CapabilityEnergyNetworkList.NETWORK_LIST;
			}

			@Override
			public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
				if (capability == CapabilityEnergyNetworkList.NETWORK_LIST) {
					return (T) this.energyNetworkList;
				}
				return null;
			}
		});
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(final PlayerLoggedInEvent event) {
		if (!(event.player instanceof EntityPlayerMP)) {
			return;
		}
		final EntityPlayerMP player = (EntityPlayerMP) event.player;

		final World world = player.world;
		if (world == null) {
			return;
		}

		final EnergyNetworkList list = world.getCapability(CapabilityEnergyNetworkList.NETWORK_LIST, null);
		if (list == null) {
			return;
		}

		final NBTTagList syncTag = (NBTTagList) CapabilityEnergyNetworkList.NETWORK_LIST.writeNBT(list, null);

		ModNetworkManager.NETWORK.sendTo(new SPacketSyncEnergyNetworkList(syncTag), player);

	}

	@SubscribeEvent
	public static void onPlayerChangedDimension(final PlayerChangedDimensionEvent event) {
		WIPTech.info("Player Changed Dimension");
		WIPTech.info(event.player.world.loadedTileEntityList);
		WIPTech.info("New Dimension ID: " + event.player.dimension);
	}

	@SubscribeEvent
	public static void onPlayerRespawn(final PlayerRespawnEvent event) {
		WIPTech.info(event.player.world.loadedTileEntityList);
		WIPTech.info("Player Respawned");
	}

	@SubscribeEvent
	public static void onWorldTick(final WorldTickEvent event) {
		if (event.phase != Phase.END) {
			return;
		}

		final EnergyNetworkList list = event.world.getCapability(CapabilityEnergyNetworkList.NETWORK_LIST, null);
		if (list == null) {
			return;
		}
		list.update();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	@SideOnly(Side.CLIENT)
	@ExistsForDebugging
	public static void writeMod(final ModelBakeEvent event) {

		ModWritingUtil.writeMod();

	}

	@SubscribeEvent
	@ExistsForDebugging
	public static void getModFurnaceItemSmeltTime(final ModFurnaceItemSmeltTimeEvent event) {
		event.setSmeltTime(20);
	}

}