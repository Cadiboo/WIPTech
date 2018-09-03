package cadiboo.wiptech;

import java.util.Random;

import cadiboo.wiptech.block.BlockEnamel;
import cadiboo.wiptech.block.BlockItem;
import cadiboo.wiptech.block.BlockModOre;
import cadiboo.wiptech.block.BlockResource;
import cadiboo.wiptech.block.BlockSpool;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.block.IBlockModMaterial;
import cadiboo.wiptech.capability.energy.network.CapabilityEnergyNetworkList;
import cadiboo.wiptech.capability.energy.network.EnergyNetwork;
import cadiboo.wiptech.capability.energy.network.EnergyNetworkList;
import cadiboo.wiptech.capability.energy.network.IEnergyNetworkList;
import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.client.model.ModelsCache;
import cadiboo.wiptech.client.render.block.model.GlitchModelLoader;
import cadiboo.wiptech.client.render.block.model.WireModelLoader;
import cadiboo.wiptech.client.render.entity.EntityNapalmRenderer;
import cadiboo.wiptech.client.render.entity.EntityPortableGeneratorRenderer;
import cadiboo.wiptech.client.render.entity.EntityRailgunRenderer;
import cadiboo.wiptech.client.render.entity.EntitySlugCasingRenderer;
import cadiboo.wiptech.client.render.entity.EntitySlugRenderer;
import cadiboo.wiptech.client.render.item.model.CasedSlugModelLoader;
import cadiboo.wiptech.client.render.tileentity.TileEntityEnamelRenderer;
import cadiboo.wiptech.client.render.tileentity.TileEntityWireRenderer;
import cadiboo.wiptech.entity.item.EntityPortableGenerator;
import cadiboo.wiptech.entity.item.EntityRailgun;
import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.entity.projectile.EntitySlug;
import cadiboo.wiptech.entity.projectile.EntitySlugCasing;
import cadiboo.wiptech.init.ModItems;
import cadiboo.wiptech.item.IItemModMaterial;
import cadiboo.wiptech.item.ItemCasedSlug;
import cadiboo.wiptech.item.ItemCoil;
import cadiboo.wiptech.item.ItemFlamethrower;
import cadiboo.wiptech.item.ItemModArmor;
import cadiboo.wiptech.item.ItemModAxe;
import cadiboo.wiptech.item.ItemModHoe;
import cadiboo.wiptech.item.ItemModPickaxe;
import cadiboo.wiptech.item.ItemModShovel;
import cadiboo.wiptech.item.ItemModSword;
import cadiboo.wiptech.item.ItemPortableGenerator;
import cadiboo.wiptech.item.ItemRail;
import cadiboo.wiptech.item.ItemRailgun;
import cadiboo.wiptech.item.ItemSlug;
import cadiboo.wiptech.item.ItemSlugCasing;
import cadiboo.wiptech.item.ModItemBlock;
import cadiboo.wiptech.network.ModNetworkManager;
import cadiboo.wiptech.network.play.server.SPacketSyncEnergyNetworkList;
import cadiboo.wiptech.tileentity.TileEntityEnamel;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.ExistsForDebugging;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModEnums.SlugCasingParts;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModResourceLocation;
import cadiboo.wiptech.util.ModUtil;
import cadiboo.wiptech.util.ModWritingUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
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

	@SubscribeEvent
	public static void onRegisterBlocksEvent(final RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();

		registerBlocksForMaterials(registry);

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
	}

	private static void registerTileEntity(final Class<? extends TileEntity> clazz) {
		try {
			GameRegistry.registerTileEntity(clazz, new ModResourceLocation(ModReference.MOD_ID, ModUtil.getRegistryNameForClass(clazz, "TileEntity")));
		} catch (final Exception e) {
			WIPTech.error("Error registering Tile Entity " + clazz.getSimpleName());
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public static void onRegisterItemsEvent(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		registerItemsForMaterials(registry);

		registry.register(new ItemPortableGenerator("portable_generator"));

		registry.register(new ItemFlamethrower("flamethrower"));

		registry.register(new ItemRailgun("railgun"));

		registry.register(new ItemSlugCasing("slug_casing_back", SlugCasingParts.BACK));
		registry.register(new ItemSlugCasing("slug_casing_top", SlugCasingParts.TOP));
		registry.register(new ItemSlugCasing("slug_casing_bottom", SlugCasingParts.BOTTOM));

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

	@SubscribeEvent
	public static void onRegisterEntitiesEvent(final RegistryEvent.Register<EntityEntry> event) {
		final IForgeRegistry<EntityEntry> registry = event.getRegistry();

		registerEntitiesForMaterials(registry);

		event.getRegistry().register(buildEntityEntryFromClass(EntityPortableGenerator.class, false, 64, 2, true));

		event.getRegistry().register(buildEntityEntryFromClass(EntityRailgun.class, false, 64, 2, true));

		event.getRegistry().register(buildEntityEntryFromClass(EntitySlugCasing.class, false, 128, 2, true));

		event.getRegistry().register(buildEntityEntryFromClass(EntityNapalm.class, false, 128, 2, true));

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

	private static EntityEntry buildEntityEntryFromClass(final Class<? extends Entity> clazz, final boolean hasEgg, final int range, final int updateFrequency, final boolean sendVelocityUpdates) {
		return buildEntityEntryFromClassWithName(clazz, new ModResourceLocation(ModReference.MOD_ID, ModUtil.getRegistryNameForClass(clazz, "Entity")), hasEgg, range, updateFrequency, sendVelocityUpdates);
	}

	private static EntityEntry buildEntityEntryFromClassWithName(final Class<? extends Entity> clazz, final ModResourceLocation registryName, final boolean hasEgg, final int range, final int updateFrequency, final boolean sendVelocityUpdates) {
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

		registerNormalItemModel(ModItems.PORTABLE_GENERATOR);
		registerNormalItemModel(ModItems.FLAMETHROWER);
		registerNormalItemModel(ModItems.RAILGUN);
		registerNormalItemModel(ModItems.SLUG_CASING_BACK);
		registerNormalItemModel(ModItems.SLUG_CASING_TOP);
		registerNormalItemModel(ModItems.SLUG_CASING_BOTTOM);

		WIPTech.info("Registered models");

	}

	@SideOnly(Side.CLIENT)
	private static void registerTileEntitySpecialRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWire.class, new TileEntityWireRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnamel.class, new TileEntityEnamelRenderer());
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
			if (ModMaterials.GLITCH.getOre() != null) {
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
				// FIXME TODO
				ModelLoader.setCustomMeshDefinition(material.getCasedSlug(), stack -> new ModelResourceLocation(new ModResourceLocation(material.getAssetsModId(), "cased_" + material.getNameLowercase() + "_slug"), ModWritingUtil.default_variant_name));
			}

		}

		ModelLoaderRegistry.registerLoader(new CasedSlugModelLoader());
		WIPTech.debug("Registered custom Mesh Definitions for cased slugs with the Model Loader");

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
				if ((material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix().toLowerCase(), ForgeRegistries.ITEMS).equals(material.getAssetsModId()))) {
					registerBlockModMaterialItemBlockModel(material.getResource());
				}
				if ((material.getResourcePiece() != null) && (material.getResouceLocationDomain(material.getType().getResourcePieceNameSuffix().toLowerCase(), ForgeRegistries.ITEMS).equals(material.getAssetsModId()))) {
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
				if (material.getCasedSlug() != null) {
					registerItemModMaterialModel(material.getCasedSlug());
				}
			}

		}
		WIPTech.debug("Registered models for materials");
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

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelBakeEvent(final ModelBakeEvent event) {
		final IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();

		injectModels(registry);
		WIPTech.info("Injected models");

	}

	@SideOnly(Side.CLIENT)
	private static void injectModels(final IRegistry<ModelResourceLocation, IBakedModel> registry) {
		final ModResourceLocation[] models = {

				new ModResourceLocation(ModReference.MOD_ID, "entity/portable_generator_handle"),

				new ModResourceLocation(ModReference.MOD_ID, "entity/portable_generator_wheel"),

				new ModResourceLocation(ModReference.MOD_ID, "entity/railgun_base"),

				new ModResourceLocation(ModReference.MOD_ID, "entity/railgun_turret"),

				new ModResourceLocation(ModReference.MOD_ID, "entity/railgun_gun"),

				// new ModResourceLocation(ModReference.MOD_ID, "entity/slug_casing_back"),
				//
				// new ModResourceLocation(ModReference.MOD_ID, "entity/slug_casing_top"),
				//
				// new ModResourceLocation(ModReference.MOD_ID, "entity/slug_casing_bottom"),

		};

		for (final ModResourceLocation model : models) {
			try {
				/* modified from code made by Draco18s */
				final ModelResourceLocation location = new ModelResourceLocation(model.toString());

				final IBakedModel bakedModel = ModelsCache.INSTANCE.getBakedModel(model);

				registry.putObject(location, bakedModel);
				WIPTech.debug("Sucessfully injected " + model.toString() + " into Model Registry");
			} catch (final Exception e) {
				WIPTech.error("Error injecting model " + model.toString() + " into Model Registry");
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRenderWorldLast(final RenderWorldLastEvent event) {

		final World world = Minecraft.getMinecraft().world;
		if (world == null) {
			return;
		}
		final IEnergyNetworkList list = world.getCapability(CapabilityEnergyNetworkList.NETWORK_LIST, null);
		if (list == null) {
			return;
		}
		if (!(list instanceof EnergyNetworkList)) {
			return;
		}

		for (final EnergyNetwork network : ((EnergyNetworkList) list).getNetworks()) {
			for (final BlockPos pos : network.getConnections()) {

				// our positions
				final int sX = pos.getX();
				final int sY = pos.getY();
				final int sZ = pos.getZ();

				// Usually the player
				final Entity entity = Minecraft.getMinecraft().getRenderViewEntity();

				// Interpolating everything back to 0,0,0. These are transforms you can find at RenderEntity class
				final double d0 = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * event.getPartialTicks());
				final double d1 = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * event.getPartialTicks());
				final double d2 = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * event.getPartialTicks());

				// Apply 0-our transforms to set everything back to 0,0,0
				Tessellator.getInstance().getBuffer().setTranslation(-d0, -d1, -d2);

				// bind our texture
				Minecraft.getMinecraft().getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/misc/circuits.png"));

				if (pos.distanceSq(d0, d1, d2) > Math.pow(5, 2)) {
					Minecraft.getMinecraft().getTextureManager().getTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/misc/circuits.png")).setBlurMipmap(true, false);
				} else {
					Minecraft.getMinecraft().getTextureManager().getTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/misc/circuits.png")).setBlurMipmap(false, false);
				}

				final Random rand = new Random(network.hashCode());

				GlStateManager.color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());

				// actual render function
//				ClientUtil.drawCuboidAt(sX + 0.5, sY + 0.5, sZ + 0.5, 0, 1, 0, 1, 0.5, 0.5, 0.5, 1);

				// When you are done rendering all your boxes reset the offsets. We do not want everything that renders next to still be at 0,0,0 :)
				Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);
			}
		}

	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
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

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@SideOnly(Side.CLIENT)
	public static void onTooltipEvent(final ItemTooltipEvent event) {

		final Item item = event.getItemStack().getItem();

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
		for (int i = 0; i < event.getToolTip().size(); i++) {
			if (net.minecraft.util.StringUtils.stripControlCodes(event.getToolTip().get(i)).equals(event.getItemStack().getItem().getRegistryName().toString())) { // TODO why? and what does this do???
				event.getToolTip().add(i, tooltip);
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
	public static void onAttachCapabilities(final AttachCapabilitiesEvent<World> event) {
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

		final IEnergyNetworkList list = world.getCapability(CapabilityEnergyNetworkList.NETWORK_LIST, null);
		if (list == null) {
			return;
		}

		final NBTTagList syncTag = (NBTTagList) CapabilityEnergyNetworkList.NETWORK_LIST.writeNBT(list, null);

		ModNetworkManager.NETWORK.sendTo(new SPacketSyncEnergyNetworkList(syncTag), player);

	}

	@SubscribeEvent
	public void onChangeDimension(final PlayerChangedDimensionEvent event) {
		WIPTech.info("Player Changed Dimension");
		WIPTech.info(event.player.world.loadedTileEntityList);
		WIPTech.info("New Dimension ID: " + event.player.dimension);
	}

	@SubscribeEvent
	public void onPlayerRespawn(final PlayerRespawnEvent event) {
		WIPTech.info(event.player.world.loadedTileEntityList);
		WIPTech.info("Player Respawned");
	}

	@SubscribeEvent
	public static void onWorldTick(final WorldTickEvent event) {
		if (event.phase != Phase.END) {
			return;
		}

		final IEnergyNetworkList list = event.world.getCapability(CapabilityEnergyNetworkList.NETWORK_LIST, null);
		if (list == null) {
			return;
		}
		list.update();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@SideOnly(Side.CLIENT)
	@ExistsForDebugging
	public static void writeMod(final ModelBakeEvent event) {

		ModWritingUtil.writeMod();

	}

}