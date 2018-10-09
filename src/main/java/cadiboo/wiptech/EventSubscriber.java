package cadiboo.wiptech;

import cadiboo.wiptech.block.BlockAssemblyTable;
import cadiboo.wiptech.block.BlockEnamel;
import cadiboo.wiptech.block.BlockItem;
import cadiboo.wiptech.block.BlockModFurnace;
import cadiboo.wiptech.block.BlockModOre;
import cadiboo.wiptech.block.BlockNapalm;
import cadiboo.wiptech.block.BlockPeripheral;
import cadiboo.wiptech.block.BlockResource;
import cadiboo.wiptech.block.BlockSpool;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.capability.energy.network.CapabilityEnergyNetworkList;
import cadiboo.wiptech.capability.energy.network.EnergyNetworkList;
import cadiboo.wiptech.entity.IModEntity;
import cadiboo.wiptech.entity.item.EntityPortableGenerator;
import cadiboo.wiptech.entity.item.EntityRailgun;
import cadiboo.wiptech.entity.projectile.EntityCoilgunBullet;
import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.entity.projectile.EntitySlug;
import cadiboo.wiptech.entity.projectile.EntitySlugCasing;
import cadiboo.wiptech.event.ModFurnaceItemSmeltTimeEvent;
import cadiboo.wiptech.init.ModBlocks;
import cadiboo.wiptech.item.ItemCasedSlug;
import cadiboo.wiptech.item.ItemCircuit;
import cadiboo.wiptech.item.ItemCoil;
import cadiboo.wiptech.item.ItemElectricShield;
import cadiboo.wiptech.item.ItemFlamethrower;
import cadiboo.wiptech.item.ItemGrenadeLauncher;
import cadiboo.wiptech.item.ItemHandheldCoilgun;
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
import cadiboo.wiptech.material.ModMaterial;
import cadiboo.wiptech.network.ModNetworkManager;
import cadiboo.wiptech.network.play.server.SPacketSyncEnergyNetworkList;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.tileentity.TileEntityEnamel;
import cadiboo.wiptech.tileentity.TileEntityModFurnace;
import cadiboo.wiptech.tileentity.TileEntityPeripheral;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.ExistsForDebugging;
import cadiboo.wiptech.util.ModEnums.BlockItemType;
import cadiboo.wiptech.util.ModEnums.CircuitType;
import cadiboo.wiptech.util.ModEnums.ScopeType;
import cadiboo.wiptech.util.ModEnums.SlugCasingPart;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModUtil;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocation;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocationPath;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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

		registry.register(new BlockNapalm("napalm"));

		WIPTech.info("Registered blocks");

		registerTileEntities();

		WIPTech.debug("Registered tile entities");

	}

	private static void registerBlocksForMaterials(final IForgeRegistry<Block> registry) {

		// debug code, gotta love it
		final ModMaterial debug1 = ModMaterial.ALUMINIUM;
		final ModMaterial debug2 = ModMaterial.LEAD;
		final ModMaterial debug3 = ModMaterial.BAUXITE;
		final ModMaterial debug4 = ModMaterial.APATITE;

		final ModMaterial[] values = ModMaterial.values();

		for (final ModMaterial material : ModMaterial.values()) {
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

		// registry.register(new ModItemBlock(ModBlocks.PERIPHERAL));

		//

		registerItemsForMaterials(registry);
		registerItemsForAttachments(registry);

		registry.register(new ItemPortableGenerator("portable_generator"));

		registry.register(new ItemFlamethrower("flamethrower"));

		registry.register(new ItemRailgun("railgun"));

		registry.register(new ItemSlugCasing("slug_casing_back", SlugCasingPart.BACK));
		registry.register(new ItemSlugCasing("slug_casing_top", SlugCasingPart.TOP));
		registry.register(new ItemSlugCasing("slug_casing_bottom", SlugCasingPart.BOTTOM));

		registry.register(new ItemHandheldRailgun("handheld_railgun"));
		registry.register(new ItemHandheldCoilgun("handheld_coilgun"));
		registry.register(new ItemHandheldPlasmagun("handheld_plasmagun"));

		registry.register(new ItemElectricShield("electric_shield"));

		WIPTech.info("Registered items");

	}

	private static void registerItemsForMaterials(final IForgeRegistry<Item> registry) {
		for (final ModMaterial material : ModMaterial.values()) {
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

		}

		WIPTech.debug("Registered items for materials");

	}

	private static void registerItemsForAttachments(final IForgeRegistry<Item> registry) {

		for (final CircuitType type : CircuitType.values()) {
			registry.register(new ItemCircuit("circuit", type));
		}

		for (final ScopeType type : ScopeType.values()) {
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

		event.getRegistry().register(buildEntityEntryFromClass(EntityCoilgunBullet.class, false, 128, 1, true));

		// TODO: register shotgun bullet;

		// TODO: register grenade bullet;

		WIPTech.info("Registered entities");

	}

	private static void registerEntitiesForMaterials(final IForgeRegistry<EntityEntry> registry) {
		// TODO AdditionalSpawnData maybe?
		for (final ModMaterial material : ModMaterial.values()) {
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

	@SubscribeEvent
	@ExistsForDebugging
	public static void getModFurnaceItemSmeltTime(final ModFurnaceItemSmeltTimeEvent event) {
		event.setSmeltTime(20);
	}

}