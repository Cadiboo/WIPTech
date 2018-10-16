package cadiboo.wiptech;

import cadiboo.wiptech.block.BlockAssemblyTable;
import cadiboo.wiptech.block.BlockModFurnace;
import cadiboo.wiptech.block.BlockNapalm;
import cadiboo.wiptech.block.BlockPeripheral;
import cadiboo.wiptech.capability.energy.network.CapabilityEnergyNetworkList;
import cadiboo.wiptech.capability.energy.network.EnergyNetworkList;
import cadiboo.wiptech.entity.IModEntity;
import cadiboo.wiptech.entity.item.EntityPortableGenerator;
import cadiboo.wiptech.entity.item.EntityRailgun;
import cadiboo.wiptech.entity.projectile.EntityCoilgunBullet;
import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.entity.projectile.EntitySlugCasing;
import cadiboo.wiptech.event.ModFurnaceItemSmeltTimeEvent;
import cadiboo.wiptech.init.ModBlocks;
import cadiboo.wiptech.item.ItemCircuit;
import cadiboo.wiptech.item.ItemElectricShield;
import cadiboo.wiptech.item.ItemFlamethrower;
import cadiboo.wiptech.item.ItemGrenadeLauncher;
import cadiboo.wiptech.item.ItemHandheldCoilgun;
import cadiboo.wiptech.item.ItemHandheldPlasmagun;
import cadiboo.wiptech.item.ItemHandheldRailgun;
import cadiboo.wiptech.item.ItemHeartbeatSensor;
import cadiboo.wiptech.item.ItemLaser;
import cadiboo.wiptech.item.ItemPortableGenerator;
import cadiboo.wiptech.item.ItemRailgun;
import cadiboo.wiptech.item.ItemScope;
import cadiboo.wiptech.item.ItemShotgun;
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
import cadiboo.wiptech.util.ModEnums.CircuitType;
import cadiboo.wiptech.util.ModEnums.ScopeType;
import cadiboo.wiptech.util.ModEnums.SlugCasingPart;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModUtil;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
public final class EventSubscriber {

	public static int entityId = 0;

	/* register blocks */
	@SubscribeEvent
	public static void onRegisterBlocksEvent(final RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();

		registry.register(new BlockModFurnace("mod_furnace"));

		registry.register(new BlockAssemblyTable("assembly_table"));

		registry.register(new BlockPeripheral("peripheral"));

		registry.register(new BlockNapalm("napalm"));

		WIPTech.info("Registered blocks");

		registerTileEntities();

		WIPTech.debug("Registered tile entities");

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

		event.getRegistry().register(buildEntityEntryFromClass(EntityPortableGenerator.class, false, 64, 2, true));

		event.getRegistry().register(buildEntityEntryFromClass(EntityRailgun.class, false, 64, 2, true));

		event.getRegistry().register(buildEntityEntryFromClass(EntitySlugCasing.class, false, 128, 2, true));

		event.getRegistry().register(buildEntityEntryFromClass(EntityNapalm.class, false, 128, 2, true));

		event.getRegistry().register(buildEntityEntryFromClass(EntityCoilgunBullet.class, false, 128, 1, true));

		// TODO: register shotgun bullet;

		// TODO: register grenade bullet;

		WIPTech.info("Registered entities");

	}

	private static <T extends Entity & IModEntity> EntityEntry buildEntityEntryFromClass(final Class<T> clazz, final boolean hasEgg, final int range, final int updateFrequency, final boolean sendVelocityUpdates) {
		return buildEntityEntryFromClassWithName(clazz, new ModResourceLocation(ModReference.MOD_ID, ModUtil.getRegistryNameForClass(clazz, "Entity")), hasEgg, range, updateFrequency, sendVelocityUpdates);
	}

	private static <T extends Entity & IModEntity> EntityEntry buildEntityEntryFromClassWithName(final Class<T> clazz, final ModResourceLocation registryName, final boolean hasEgg, final int range, final int updateFrequency, final boolean sendVelocityUpdates) {
		EntityEntryBuilder<Entity> builder = EntityEntryBuilder.create();
		builder = builder.entity(clazz);
		builder = builder.id(registryName, entityId++);
		builder = builder.name(registryName.getPath());
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