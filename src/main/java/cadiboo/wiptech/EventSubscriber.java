package cadiboo.wiptech;

import cadiboo.wiptech.block.BlockEnamel;
import cadiboo.wiptech.block.BlockItem;
import cadiboo.wiptech.block.BlockModOre;
import cadiboo.wiptech.block.BlockResource;
import cadiboo.wiptech.block.BlockSpool;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.item.ItemCoil;
import cadiboo.wiptech.item.ItemModArmor;
import cadiboo.wiptech.item.ItemModAxe;
import cadiboo.wiptech.item.ItemModHoe;
import cadiboo.wiptech.item.ItemModPickaxe;
import cadiboo.wiptech.item.ItemModShovel;
import cadiboo.wiptech.item.ItemModSword;
import cadiboo.wiptech.item.ItemRail;
import cadiboo.wiptech.item.ModItemBlock;
import cadiboo.wiptech.tileentity.ModTileEntity;
import cadiboo.wiptech.tileentity.TileEntityEnamel;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.ModEnums.BlockItemTypes;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModWritingUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = ModReference.ID)
public final class EventSubscriber {

	@SubscribeEvent
	public static void onRegisterBlocksEvent(final RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();

		registerTileEntity(TileEntityWire.class);
		registerTileEntity(TileEntityEnamel.class);

		for (ModMaterials material : ModMaterials.values()) {
			if (material.getProperties().hasOre())
				registry.register(new BlockModOre(material));

			if (material.getProperties().hasBlock())
				registry.register(new BlockResource(material));

			if (material.getProperties().hasIngotAndNugget()) {
				registry.register(new BlockItem(material, BlockItemTypes.INGOT));
				registry.register(new BlockItem(material, BlockItemTypes.NUGGET));
			}

			if (material.getProperties().hasWire()) {

				registry.register(new BlockWire(material));
				registry.register(new BlockSpool(material));
			}

			if (material.getProperties().hasEnamel())
				registry.register(new BlockEnamel(material));

		}

//		registry.register(new BlockModOre(ModMaterials.URANIUM));
//		registry.register(new BlockResource(ModMaterials.URANIUM));
//		registry.register(new BlockItem(ModMaterials.URANIUM, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.URANIUM, BlockItemTypes.NUGGET));
//		registry.register(new BlockModOre(ModMaterials.TUNGSTEN));
//		registry.register(new BlockResource(ModMaterials.TUNGSTEN));
//		registry.register(new BlockItem(ModMaterials.TUNGSTEN, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.TUNGSTEN, BlockItemTypes.NUGGET));
//		registry.register(new BlockWire(ModMaterials.TUNGSTEN));
//		registry.register(new BlockEnamel(ModMaterials.TUNGSTEN));
//		registry.register(new BlockSpool(ModMaterials.TUNGSTEN));
//		registry.register(new BlockResource(ModMaterials.TUNGSTEN_CARBITE));
//		registry.register(new BlockItem(ModMaterials.TUNGSTEN_CARBITE, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.TUNGSTEN_CARBITE, BlockItemTypes.NUGGET));
//		registry.register(new BlockWire(ModMaterials.TUNGSTEN_CARBITE));
//		registry.register(new BlockEnamel(ModMaterials.TUNGSTEN_CARBITE));
//		registry.register(new BlockSpool(ModMaterials.TUNGSTEN_CARBITE));
//		registry.register(new BlockModOre(ModMaterials.TITANIUM));
//		registry.register(new BlockResource(ModMaterials.TITANIUM));
//		registry.register(new BlockItem(ModMaterials.TITANIUM, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.TITANIUM, BlockItemTypes.NUGGET));
//		registry.register(new BlockModOre(ModMaterials.TIN));
//		registry.register(new BlockResource(ModMaterials.TIN));
//		registry.register(new BlockItem(ModMaterials.TIN, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.TIN, BlockItemTypes.NUGGET));
//		registry.register(new BlockWire(ModMaterials.TIN));
//		registry.register(new BlockEnamel(ModMaterials.TIN));
//		registry.register(new BlockSpool(ModMaterials.TIN));
//		registry.register(new BlockModOre(ModMaterials.THORIUM));
//		registry.register(new BlockResource(ModMaterials.THORIUM));
//		registry.register(new BlockItem(ModMaterials.THORIUM, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.THORIUM, BlockItemTypes.NUGGET));
//		registry.register(new BlockModOre(ModMaterials.SILVER));
//		registry.register(new BlockResource(ModMaterials.SILVER));
//		registry.register(new BlockItem(ModMaterials.SILVER, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.SILVER, BlockItemTypes.NUGGET));
//		registry.register(new BlockWire(ModMaterials.SILVER));
//		registry.register(new BlockEnamel(ModMaterials.SILVER));
//		registry.register(new BlockSpool(ModMaterials.SILVER));
//		registry.register(new BlockModOre(ModMaterials.PLATINUM));
//		registry.register(new BlockResource(ModMaterials.PLATINUM));
//		registry.register(new BlockItem(ModMaterials.PLATINUM, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.PLATINUM, BlockItemTypes.NUGGET));
//		registry.register(new BlockWire(ModMaterials.PLATINUM));
//		registry.register(new BlockEnamel(ModMaterials.PLATINUM));
//		registry.register(new BlockSpool(ModMaterials.PLATINUM));
//		registry.register(new BlockModOre(ModMaterials.PLUTONIUM));
//		registry.register(new BlockResource(ModMaterials.PLUTONIUM));
//		registry.register(new BlockItem(ModMaterials.PLUTONIUM, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.PLUTONIUM, BlockItemTypes.NUGGET));
//		registry.register(new BlockModOre(ModMaterials.OSMIUM));
//		registry.register(new BlockResource(ModMaterials.OSMIUM));
//		registry.register(new BlockItem(ModMaterials.OSMIUM, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.OSMIUM, BlockItemTypes.NUGGET));
//		registry.register(new BlockWire(ModMaterials.OSMIUM));
//		registry.register(new BlockEnamel(ModMaterials.OSMIUM));
//		registry.register(new BlockSpool(ModMaterials.OSMIUM));
//		registry.register(new BlockModOre(ModMaterials.NICKEL));
//		registry.register(new BlockResource(ModMaterials.NICKEL));
//		registry.register(new BlockItem(ModMaterials.NICKEL, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.NICKEL, BlockItemTypes.NUGGET));
//		registry.register(new BlockWire(ModMaterials.NICKEL));
//		registry.register(new BlockEnamel(ModMaterials.NICKEL));
//		registry.register(new BlockSpool(ModMaterials.NICKEL));
//		registry.register(new BlockResource(ModMaterials.STEEL));
//		registry.register(new BlockItem(ModMaterials.STEEL, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.STEEL, BlockItemTypes.NUGGET));
//		registry.register(new BlockWire(ModMaterials.STEEL));
//		registry.register(new BlockEnamel(ModMaterials.STEEL));
//		registry.register(new BlockSpool(ModMaterials.STEEL));
//		registry.register(new BlockModOre(ModMaterials.ALUMINIUM));
//		registry.register(new BlockResource(ModMaterials.ALUMINIUM));
//		registry.register(new BlockItem(ModMaterials.ALUMINIUM, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.ALUMINIUM, BlockItemTypes.NUGGET));
//		registry.register(new BlockWire(ModMaterials.ALUMINIUM));
//		registry.register(new BlockEnamel(ModMaterials.ALUMINIUM));
//		registry.register(new BlockSpool(ModMaterials.ALUMINIUM));
//		registry.register(new BlockModOre(ModMaterials.COPPER));
//		registry.register(new BlockResource(ModMaterials.COPPER));
//		registry.register(new BlockItem(ModMaterials.COPPER, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.COPPER, BlockItemTypes.NUGGET));
//		registry.register(new BlockWire(ModMaterials.COPPER));
//		registry.register(new BlockEnamel(ModMaterials.COPPER));
//		registry.register(new BlockSpool(ModMaterials.COPPER));
//		registry.register(new BlockModOre(ModMaterials.GOLD));
//		registry.register(new BlockResource(ModMaterials.GOLD));
//		registry.register(new BlockItem(ModMaterials.GOLD, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.GOLD, BlockItemTypes.NUGGET));
//		registry.register(new BlockWire(ModMaterials.GOLD));
//		registry.register(new BlockEnamel(ModMaterials.GOLD));
//		registry.register(new BlockSpool(ModMaterials.GOLD));
//		registry.register(new BlockModOre(ModMaterials.IRON));
//		registry.register(new BlockResource(ModMaterials.IRON));
//		registry.register(new BlockItem(ModMaterials.IRON, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.IRON, BlockItemTypes.NUGGET));
//		registry.register(new BlockWire(ModMaterials.IRON));
//		registry.register(new BlockEnamel(ModMaterials.IRON));
//		registry.register(new BlockSpool(ModMaterials.IRON));
//		registry.register(new BlockModOre(ModMaterials.LEAD));
//		registry.register(new BlockResource(ModMaterials.LEAD));
//		registry.register(new BlockItem(ModMaterials.LEAD, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.LEAD, BlockItemTypes.NUGGET));
//		registry.register(new BlockModOre(ModMaterials.GALLIUM));
//		registry.register(new BlockResource(ModMaterials.GALLIUM));
//		registry.register(new BlockItem(ModMaterials.GALLIUM, BlockItemTypes.INGOT));
//		registry.register(new BlockItem(ModMaterials.GALLIUM, BlockItemTypes.NUGGET));

	}

	@SubscribeEvent
	public static void onRegisterItemsEvent(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		for (ModMaterials material : ModMaterials.values()) {
			if (material.getProperties().hasOre())
				registry.register(new ModItemBlock(material.getOre(), new ResourceLocation(material.getResouceLocationDomain("ore", ForgeRegistries.ITEMS), material.getNameLowercase() + "_ore")));

			if (material.getProperties().hasBlock())
				registry.register(
						new ModItemBlock(material.getBlock(), new ResourceLocation(material.getResouceLocationDomain("block", ForgeRegistries.ITEMS), material.getNameLowercase() + "_block")));

			if (material.getProperties().hasIngotAndNugget()) {
				registry.register(
						new ModItemBlock(material.getIngot(), new ResourceLocation(material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS), material.getNameLowercase() + "_ingot")));
				registry.register(
						new ModItemBlock(material.getNugget(), new ResourceLocation(material.getResouceLocationDomain("nugget", ForgeRegistries.ITEMS), material.getNameLowercase() + "_nugget")));
			}

			if (material.getProperties().hasWire()) {
				registry.register(new ModItemBlock(material.getWire()));
				registry.register(new ModItemBlock(material.getSpool()));
			}

			if (material.getProperties().hasEnamel())
				registry.register(new ModItemBlock(material.getEnamel()));

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

			if (material.getProperties().hasCoil())
				registry.register(new ItemCoil(material));

			if (material.getProperties().hasRail())
				registry.register(new ItemRail(material));

		}

//		registry.register(new ModItemBlock(ModBlocks.URANIUM_ORE));
//		registry.register(new ModItemBlock(ModBlocks.URANIUM_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.URANIUM_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.URANIUM_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.URANIUM, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.URANIUM, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.URANIUM, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.URANIUM, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.URANIUM));
//		registry.register(new ItemModAxe(ModMaterials.URANIUM));
//		registry.register(new ItemModSword(ModMaterials.URANIUM));
//		registry.register(new ItemModShovel(ModMaterials.URANIUM));
//		registry.register(new ItemModHoe(ModMaterials.URANIUM));
//		registry.register(new ModItemBlock(ModBlocks.TUNGSTEN_ORE));
//		registry.register(new ModItemBlock(ModBlocks.TUNGSTEN_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.TUNGSTEN_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.TUNGSTEN_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.TUNGSTEN, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.TUNGSTEN, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.TUNGSTEN, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.TUNGSTEN, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.TUNGSTEN));
//		registry.register(new ItemModAxe(ModMaterials.TUNGSTEN));
//		registry.register(new ItemModSword(ModMaterials.TUNGSTEN));
//		registry.register(new ItemModShovel(ModMaterials.TUNGSTEN));
//		registry.register(new ItemModHoe(ModMaterials.TUNGSTEN));
//		registry.register(new ModItemBlock(ModBlocks.TUNGSTEN_WIRE));
//		registry.register(new ModItemBlock(ModBlocks.TUNGSTEN_ENAMEL));
//		registry.register(new ItemCoil(ModMaterials.TUNGSTEN));
//		registry.register(new ModItemBlock(ModBlocks.TUNGSTEN_SPOOL));
//		registry.register(new ItemRail(ModMaterials.TUNGSTEN));
//		registry.register(new ModItemBlock(ModBlocks.TUNGSTEN_CARBITE_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.TUNGSTEN_CARBITE_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.TUNGSTEN_CARBITE_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.TUNGSTEN_CARBITE, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.TUNGSTEN_CARBITE, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.TUNGSTEN_CARBITE, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.TUNGSTEN_CARBITE, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.TUNGSTEN_CARBITE));
//		registry.register(new ItemModAxe(ModMaterials.TUNGSTEN_CARBITE));
//		registry.register(new ItemModSword(ModMaterials.TUNGSTEN_CARBITE));
//		registry.register(new ItemModShovel(ModMaterials.TUNGSTEN_CARBITE));
//		registry.register(new ItemModHoe(ModMaterials.TUNGSTEN_CARBITE));
//		registry.register(new ModItemBlock(ModBlocks.TUNGSTEN_CARBITE_WIRE));
//		registry.register(new ModItemBlock(ModBlocks.TUNGSTEN_CARBITE_ENAMEL));
//		registry.register(new ItemCoil(ModMaterials.TUNGSTEN_CARBITE));
//		registry.register(new ModItemBlock(ModBlocks.TUNGSTEN_CARBITE_SPOOL));
//		registry.register(new ItemRail(ModMaterials.TUNGSTEN_CARBITE));
//		registry.register(new ModItemBlock(ModBlocks.TITANIUM_ORE));
//		registry.register(new ModItemBlock(ModBlocks.TITANIUM_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.TITANIUM_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.TITANIUM_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.TITANIUM, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.TITANIUM, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.TITANIUM, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.TITANIUM, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.TITANIUM));
//		registry.register(new ItemModAxe(ModMaterials.TITANIUM));
//		registry.register(new ItemModSword(ModMaterials.TITANIUM));
//		registry.register(new ItemModShovel(ModMaterials.TITANIUM));
//		registry.register(new ItemModHoe(ModMaterials.TITANIUM));
//		registry.register(new ModItemBlock(ModBlocks.TIN_ORE));
//		registry.register(new ModItemBlock(ModBlocks.TIN_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.TIN_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.TIN_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.TIN, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.TIN, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.TIN, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.TIN, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.TIN));
//		registry.register(new ItemModAxe(ModMaterials.TIN));
//		registry.register(new ItemModSword(ModMaterials.TIN));
//		registry.register(new ItemModShovel(ModMaterials.TIN));
//		registry.register(new ItemModHoe(ModMaterials.TIN));
//		registry.register(new ModItemBlock(ModBlocks.TIN_WIRE));
//		registry.register(new ModItemBlock(ModBlocks.TIN_ENAMEL));
//		registry.register(new ItemCoil(ModMaterials.TIN));
//		registry.register(new ModItemBlock(ModBlocks.TIN_SPOOL));
//		registry.register(new ItemRail(ModMaterials.TIN));
//		registry.register(new ModItemBlock(ModBlocks.THORIUM_ORE));
//		registry.register(new ModItemBlock(ModBlocks.THORIUM_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.THORIUM_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.THORIUM_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.THORIUM, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.THORIUM, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.THORIUM, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.THORIUM, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.THORIUM));
//		registry.register(new ItemModAxe(ModMaterials.THORIUM));
//		registry.register(new ItemModSword(ModMaterials.THORIUM));
//		registry.register(new ItemModShovel(ModMaterials.THORIUM));
//		registry.register(new ItemModHoe(ModMaterials.THORIUM));
//		registry.register(new ModItemBlock(ModBlocks.SILVER_ORE));
//		registry.register(new ModItemBlock(ModBlocks.SILVER_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.SILVER_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.SILVER_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.SILVER, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.SILVER, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.SILVER, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.SILVER, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.SILVER));
//		registry.register(new ItemModAxe(ModMaterials.SILVER));
//		registry.register(new ItemModSword(ModMaterials.SILVER));
//		registry.register(new ItemModShovel(ModMaterials.SILVER));
//		registry.register(new ItemModHoe(ModMaterials.SILVER));
//		registry.register(new ModItemBlock(ModBlocks.SILVER_WIRE));
//		registry.register(new ModItemBlock(ModBlocks.SILVER_ENAMEL));
//		registry.register(new ItemCoil(ModMaterials.SILVER));
//		registry.register(new ModItemBlock(ModBlocks.SILVER_SPOOL));
//		registry.register(new ItemRail(ModMaterials.SILVER));
//		registry.register(new ModItemBlock(ModBlocks.PLATINUM_ORE));
//		registry.register(new ModItemBlock(ModBlocks.PLATINUM_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.PLATINUM_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.PLATINUM_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.PLATINUM, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.PLATINUM, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.PLATINUM, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.PLATINUM, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.PLATINUM));
//		registry.register(new ItemModAxe(ModMaterials.PLATINUM));
//		registry.register(new ItemModSword(ModMaterials.PLATINUM));
//		registry.register(new ItemModShovel(ModMaterials.PLATINUM));
//		registry.register(new ItemModHoe(ModMaterials.PLATINUM));
//		registry.register(new ModItemBlock(ModBlocks.PLATINUM_WIRE));
//		registry.register(new ModItemBlock(ModBlocks.PLATINUM_ENAMEL));
//		registry.register(new ItemCoil(ModMaterials.PLATINUM));
//		registry.register(new ModItemBlock(ModBlocks.PLATINUM_SPOOL));
//		registry.register(new ItemRail(ModMaterials.PLATINUM));
//		registry.register(new ModItemBlock(ModBlocks.PLUTONIUM_ORE));
//		registry.register(new ModItemBlock(ModBlocks.PLUTONIUM_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.PLUTONIUM_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.PLUTONIUM_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.PLUTONIUM, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.PLUTONIUM, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.PLUTONIUM, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.PLUTONIUM, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.PLUTONIUM));
//		registry.register(new ItemModAxe(ModMaterials.PLUTONIUM));
//		registry.register(new ItemModSword(ModMaterials.PLUTONIUM));
//		registry.register(new ItemModShovel(ModMaterials.PLUTONIUM));
//		registry.register(new ItemModHoe(ModMaterials.PLUTONIUM));
//		registry.register(new ModItemBlock(ModBlocks.OSMIUM_ORE));
//		registry.register(new ModItemBlock(ModBlocks.OSMIUM_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.OSMIUM_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.OSMIUM_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.OSMIUM, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.OSMIUM, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.OSMIUM, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.OSMIUM, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.OSMIUM));
//		registry.register(new ItemModAxe(ModMaterials.OSMIUM));
//		registry.register(new ItemModSword(ModMaterials.OSMIUM));
//		registry.register(new ItemModShovel(ModMaterials.OSMIUM));
//		registry.register(new ItemModHoe(ModMaterials.OSMIUM));
//		registry.register(new ModItemBlock(ModBlocks.OSMIUM_WIRE));
//		registry.register(new ModItemBlock(ModBlocks.OSMIUM_ENAMEL));
//		registry.register(new ItemCoil(ModMaterials.OSMIUM));
//		registry.register(new ModItemBlock(ModBlocks.OSMIUM_SPOOL));
//		registry.register(new ItemRail(ModMaterials.OSMIUM));
//		registry.register(new ModItemBlock(ModBlocks.NICKEL_ORE));
//		registry.register(new ModItemBlock(ModBlocks.NICKEL_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.NICKEL_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.NICKEL_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.NICKEL, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.NICKEL, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.NICKEL, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.NICKEL, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.NICKEL));
//		registry.register(new ItemModAxe(ModMaterials.NICKEL));
//		registry.register(new ItemModSword(ModMaterials.NICKEL));
//		registry.register(new ItemModShovel(ModMaterials.NICKEL));
//		registry.register(new ItemModHoe(ModMaterials.NICKEL));
//		registry.register(new ModItemBlock(ModBlocks.NICKEL_WIRE));
//		registry.register(new ModItemBlock(ModBlocks.NICKEL_ENAMEL));
//		registry.register(new ItemCoil(ModMaterials.NICKEL));
//		registry.register(new ModItemBlock(ModBlocks.NICKEL_SPOOL));
//		registry.register(new ItemRail(ModMaterials.NICKEL));
//		registry.register(new ModItemBlock(ModBlocks.STEEL_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.STEEL_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.STEEL_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.STEEL, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.STEEL, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.STEEL, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.STEEL, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.STEEL));
//		registry.register(new ItemModAxe(ModMaterials.STEEL));
//		registry.register(new ItemModSword(ModMaterials.STEEL));
//		registry.register(new ItemModShovel(ModMaterials.STEEL));
//		registry.register(new ItemModHoe(ModMaterials.STEEL));
//		registry.register(new ModItemBlock(ModBlocks.STEEL_WIRE));
//		registry.register(new ModItemBlock(ModBlocks.STEEL_ENAMEL));
//		registry.register(new ItemCoil(ModMaterials.STEEL));
//		registry.register(new ModItemBlock(ModBlocks.STEEL_SPOOL));
//		registry.register(new ItemRail(ModMaterials.STEEL));
//		registry.register(new ModItemBlock(ModBlocks.ALUMINIUM_ORE));
//		registry.register(new ModItemBlock(ModBlocks.ALUMINIUM_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.ALUMINIUM_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.ALUMINIUM_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.ALUMINIUM, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.ALUMINIUM, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.ALUMINIUM, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.ALUMINIUM, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.ALUMINIUM));
//		registry.register(new ItemModAxe(ModMaterials.ALUMINIUM));
//		registry.register(new ItemModSword(ModMaterials.ALUMINIUM));
//		registry.register(new ItemModShovel(ModMaterials.ALUMINIUM));
//		registry.register(new ItemModHoe(ModMaterials.ALUMINIUM));
//		registry.register(new ModItemBlock(ModBlocks.ALUMINIUM_WIRE));
//		registry.register(new ModItemBlock(ModBlocks.ALUMINIUM_ENAMEL));
//		registry.register(new ItemCoil(ModMaterials.ALUMINIUM));
//		registry.register(new ModItemBlock(ModBlocks.ALUMINIUM_SPOOL));
//		registry.register(new ItemRail(ModMaterials.ALUMINIUM));
//		registry.register(new ModItemBlock(ModBlocks.COPPER_ORE));
//		registry.register(new ModItemBlock(ModBlocks.COPPER_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.COPPER_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.COPPER_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.COPPER, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.COPPER, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.COPPER, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.COPPER, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.COPPER));
//		registry.register(new ItemModAxe(ModMaterials.COPPER));
//		registry.register(new ItemModSword(ModMaterials.COPPER));
//		registry.register(new ItemModShovel(ModMaterials.COPPER));
//		registry.register(new ItemModHoe(ModMaterials.COPPER));
//		registry.register(new ModItemBlock(ModBlocks.COPPER_WIRE));
//		registry.register(new ModItemBlock(ModBlocks.COPPER_ENAMEL));
//		registry.register(new ItemCoil(ModMaterials.COPPER));
//		registry.register(new ModItemBlock(ModBlocks.COPPER_SPOOL));
//		registry.register(new ItemRail(ModMaterials.COPPER));
//		registry.register(new ModItemBlock(ModBlocks.GOLD_ORE, new ResourceLocation("minecraft", "gold_ore")));
//		registry.register(new ModItemBlock(ModBlocks.GOLD_BLOCK, new ResourceLocation("minecraft", "gold_block")));
//		registry.register(new ModItemBlock(ModBlocks.GOLD_INGOT, new ResourceLocation("minecraft", "gold_ingot")));
//		registry.register(new ModItemBlock(ModBlocks.GOLD_NUGGET, new ResourceLocation("minecraft", "gold_nugget")));
//		registry.register(new ItemModArmor(ModMaterials.GOLD, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.GOLD, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.GOLD, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.GOLD, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.GOLD));
//		registry.register(new ItemModAxe(ModMaterials.GOLD));
//		registry.register(new ItemModSword(ModMaterials.GOLD));
//		registry.register(new ItemModShovel(ModMaterials.GOLD));
//		registry.register(new ItemModHoe(ModMaterials.GOLD));
//		registry.register(new ModItemBlock(ModBlocks.GOLD_WIRE));
//		registry.register(new ModItemBlock(ModBlocks.GOLD_ENAMEL));
//		registry.register(new ItemCoil(ModMaterials.GOLD));
//		registry.register(new ModItemBlock(ModBlocks.GOLD_SPOOL));
//		registry.register(new ItemRail(ModMaterials.GOLD));
//		registry.register(new ModItemBlock(ModBlocks.IRON_ORE, new ResourceLocation("minecraft", "iron_ore")));
//		registry.register(new ModItemBlock(ModBlocks.IRON_BLOCK, new ResourceLocation("minecraft", "iron_block")));
//		registry.register(new ModItemBlock(ModBlocks.IRON_INGOT, new ResourceLocation("minecraft", "iron_ingot")));
//		registry.register(new ModItemBlock(ModBlocks.IRON_NUGGET, new ResourceLocation("minecraft", "iron_nugget")));
//		registry.register(new ItemModArmor(ModMaterials.IRON, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.IRON, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.IRON, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.IRON, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.IRON));
//		registry.register(new ItemModAxe(ModMaterials.IRON));
//		registry.register(new ItemModSword(ModMaterials.IRON));
//		registry.register(new ItemModShovel(ModMaterials.IRON));
//		registry.register(new ItemModHoe(ModMaterials.IRON));
//		registry.register(new ModItemBlock(ModBlocks.IRON_WIRE));
//		registry.register(new ModItemBlock(ModBlocks.IRON_ENAMEL));
//		registry.register(new ItemCoil(ModMaterials.IRON));
//		registry.register(new ModItemBlock(ModBlocks.IRON_SPOOL));
//		registry.register(new ItemRail(ModMaterials.IRON));
//		registry.register(new ModItemBlock(ModBlocks.LEAD_ORE));
//		registry.register(new ModItemBlock(ModBlocks.LEAD_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.LEAD_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.LEAD_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.LEAD, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.LEAD, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.LEAD, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.LEAD, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.LEAD));
//		registry.register(new ItemModAxe(ModMaterials.LEAD));
//		registry.register(new ItemModSword(ModMaterials.LEAD));
//		registry.register(new ItemModShovel(ModMaterials.LEAD));
//		registry.register(new ItemModHoe(ModMaterials.LEAD));
//		registry.register(new ModItemBlock(ModBlocks.GALLIUM_ORE));
//		registry.register(new ModItemBlock(ModBlocks.GALLIUM_BLOCK));
//		registry.register(new ModItemBlock(ModBlocks.GALLIUM_INGOT));
//		registry.register(new ModItemBlock(ModBlocks.GALLIUM_NUGGET));
//		registry.register(new ItemModArmor(ModMaterials.GALLIUM, EntityEquipmentSlot.HEAD));
//		registry.register(new ItemModArmor(ModMaterials.GALLIUM, EntityEquipmentSlot.CHEST));
//		registry.register(new ItemModArmor(ModMaterials.GALLIUM, EntityEquipmentSlot.LEGS));
//		registry.register(new ItemModArmor(ModMaterials.GALLIUM, EntityEquipmentSlot.FEET));
//		registry.register(new ItemModPickaxe(ModMaterials.GALLIUM));
//		registry.register(new ItemModAxe(ModMaterials.GALLIUM));
//		registry.register(new ItemModSword(ModMaterials.GALLIUM));
//		registry.register(new ItemModShovel(ModMaterials.GALLIUM));
//		registry.register(new ItemModHoe(ModMaterials.GALLIUM));

	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static final void onRegisterModelsEvent(final ModelRegistryEvent event) {

		for (ModMaterials material : ModMaterials.values()) {
			if (material.getProperties().hasOre())
				if (material.getOre() != null)
					registerItemBlockModel(material.getOre());

			if (material.getProperties().hasBlock())
				if (material.getBlock() != null)
					registerItemBlockModel(material.getBlock());

			if (material.getProperties().hasIngotAndNugget()) {

				if (material.getIngot() != null && material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS) == ModReference.ID)
					registerItemBlockModel(material.getIngot());
				if (material.getNugget() != null && material.getResouceLocationDomain("nugget", ForgeRegistries.ITEMS) == ModReference.ID)
					registerItemBlockModel(material.getNugget());

			}

			if (material.getProperties().hasWire()) {

				if (material.getWire() != null)
					registerItemBlockModel(material.getWire());
				if (material.getSpool() != null)
					registerItemBlockModel(material.getSpool());

			}

			if (material.getProperties().hasEnamel())
				if (material.getEnamel() != null)
					registerItemBlockModel(material.getEnamel());

			if (material.getProperties().hasArmor()) {
				if (material.getHelmet() != null)
					registerItemModel(material.getHelmet());
				if (material.getChestplate() != null)
					registerItemModel(material.getChestplate());
				if (material.getLeggings() != null)
					registerItemModel(material.getLeggings());
				if (material.getBoots() != null)
					registerItemModel(material.getBoots());
			}

			if (material.getProperties().hasTools()) {
				if (material.getPickaxe() != null)
					registerItemModel(material.getPickaxe());
				if (material.getAxe() != null)
					registerItemModel(material.getAxe());
				if (material.getSword() != null)
					registerItemModel(material.getSword());
				if (material.getShovel() != null)
					registerItemModel(material.getShovel());
				if (material.getHoe() != null)
					registerItemModel(material.getHoe());
			}

			if (material.getProperties().hasCoil())
				if (material.getCoil() != null)
					registerItemModel(material.getCoil());

			if (material.getProperties().hasRail())
				if (material.getRail() != null)
					registerItemModel(material.getRail());

		}

//		registerItemBlockModel(ModBlocks.URANIUM_ORE);
//		registerItemBlockModel(ModBlocks.URANIUM_BLOCK);
//		registerItemBlockModel(ModBlocks.URANIUM_INGOT);
//		registerItemBlockModel(ModBlocks.URANIUM_NUGGET);
//		registerItemModel(ModItems.URANIUM_HELMET);
//		registerItemModel(ModItems.URANIUM_CHESTPLATE);
//		registerItemModel(ModItems.URANIUM_LEGGINGS);
//		registerItemModel(ModItems.URANIUM_BOOTS);
//		registerItemModel(ModItems.URANIUM_PICKAXE);
//		registerItemModel(ModItems.URANIUM_AXE);
//		registerItemModel(ModItems.URANIUM_SWORD);
//		registerItemModel(ModItems.URANIUM_SHOVEL);
//		registerItemModel(ModItems.URANIUM_HOE);
//		registerItemBlockModel(ModBlocks.TUNGSTEN_ORE);
//		registerItemBlockModel(ModBlocks.TUNGSTEN_BLOCK);
//		registerItemBlockModel(ModBlocks.TUNGSTEN_INGOT);
//		registerItemBlockModel(ModBlocks.TUNGSTEN_NUGGET);
//		registerItemModel(ModItems.TUNGSTEN_HELMET);
//		registerItemModel(ModItems.TUNGSTEN_CHESTPLATE);
//		registerItemModel(ModItems.TUNGSTEN_LEGGINGS);
//		registerItemModel(ModItems.TUNGSTEN_BOOTS);
//		registerItemModel(ModItems.TUNGSTEN_PICKAXE);
//		registerItemModel(ModItems.TUNGSTEN_AXE);
//		registerItemModel(ModItems.TUNGSTEN_SWORD);
//		registerItemModel(ModItems.TUNGSTEN_SHOVEL);
//		registerItemModel(ModItems.TUNGSTEN_HOE);
//		registerItemBlockModel(ModBlocks.TUNGSTEN_WIRE);
//		registerItemBlockModel(ModBlocks.TUNGSTEN_ENAMEL);
//		registerItemModel(ModItems.TUNGSTEN_COIL);
//		registerItemBlockModel(ModBlocks.TUNGSTEN_SPOOL);
//		registerItemModel(ModItems.TUNGSTEN_RAIL);
//		registerItemBlockModel(ModBlocks.TUNGSTEN_CARBITE_BLOCK);
//		registerItemBlockModel(ModBlocks.TUNGSTEN_CARBITE_INGOT);
//		registerItemBlockModel(ModBlocks.TUNGSTEN_CARBITE_NUGGET);
//		registerItemModel(ModItems.TUNGSTEN_CARBITE_HELMET);
//		registerItemModel(ModItems.TUNGSTEN_CARBITE_CHESTPLATE);
//		registerItemModel(ModItems.TUNGSTEN_CARBITE_LEGGINGS);
//		registerItemModel(ModItems.TUNGSTEN_CARBITE_BOOTS);
//		registerItemModel(ModItems.TUNGSTEN_CARBITE_PICKAXE);
//		registerItemModel(ModItems.TUNGSTEN_CARBITE_AXE);
//		registerItemModel(ModItems.TUNGSTEN_CARBITE_SWORD);
//		registerItemModel(ModItems.TUNGSTEN_CARBITE_SHOVEL);
//		registerItemModel(ModItems.TUNGSTEN_CARBITE_HOE);
//		registerItemBlockModel(ModBlocks.TUNGSTEN_CARBITE_WIRE);
//		registerItemBlockModel(ModBlocks.TUNGSTEN_CARBITE_ENAMEL);
//		registerItemModel(ModItems.TUNGSTEN_CARBITE_COIL);
//		registerItemBlockModel(ModBlocks.TUNGSTEN_CARBITE_SPOOL);
//		registerItemModel(ModItems.TUNGSTEN_CARBITE_RAIL);
//		registerItemBlockModel(ModBlocks.TITANIUM_ORE);
//		registerItemBlockModel(ModBlocks.TITANIUM_BLOCK);
//		registerItemBlockModel(ModBlocks.TITANIUM_INGOT);
//		registerItemBlockModel(ModBlocks.TITANIUM_NUGGET);
//		registerItemModel(ModItems.TITANIUM_HELMET);
//		registerItemModel(ModItems.TITANIUM_CHESTPLATE);
//		registerItemModel(ModItems.TITANIUM_LEGGINGS);
//		registerItemModel(ModItems.TITANIUM_BOOTS);
//		registerItemModel(ModItems.TITANIUM_PICKAXE);
//		registerItemModel(ModItems.TITANIUM_AXE);
//		registerItemModel(ModItems.TITANIUM_SWORD);
//		registerItemModel(ModItems.TITANIUM_SHOVEL);
//		registerItemModel(ModItems.TITANIUM_HOE);
//		registerItemBlockModel(ModBlocks.TIN_ORE);
//		registerItemBlockModel(ModBlocks.TIN_BLOCK);
//		registerItemBlockModel(ModBlocks.TIN_INGOT);
//		registerItemBlockModel(ModBlocks.TIN_NUGGET);
//		registerItemModel(ModItems.TIN_HELMET);
//		registerItemModel(ModItems.TIN_CHESTPLATE);
//		registerItemModel(ModItems.TIN_LEGGINGS);
//		registerItemModel(ModItems.TIN_BOOTS);
//		registerItemModel(ModItems.TIN_PICKAXE);
//		registerItemModel(ModItems.TIN_AXE);
//		registerItemModel(ModItems.TIN_SWORD);
//		registerItemModel(ModItems.TIN_SHOVEL);
//		registerItemModel(ModItems.TIN_HOE);
//		registerItemBlockModel(ModBlocks.TIN_WIRE);
//		registerItemBlockModel(ModBlocks.TIN_ENAMEL);
//		registerItemModel(ModItems.TIN_COIL);
//		registerItemBlockModel(ModBlocks.TIN_SPOOL);
//		registerItemModel(ModItems.TIN_RAIL);
//		registerItemBlockModel(ModBlocks.THORIUM_ORE);
//		registerItemBlockModel(ModBlocks.THORIUM_BLOCK);
//		registerItemBlockModel(ModBlocks.THORIUM_INGOT);
//		registerItemBlockModel(ModBlocks.THORIUM_NUGGET);
//		registerItemModel(ModItems.THORIUM_HELMET);
//		registerItemModel(ModItems.THORIUM_CHESTPLATE);
//		registerItemModel(ModItems.THORIUM_LEGGINGS);
//		registerItemModel(ModItems.THORIUM_BOOTS);
//		registerItemModel(ModItems.THORIUM_PICKAXE);
//		registerItemModel(ModItems.THORIUM_AXE);
//		registerItemModel(ModItems.THORIUM_SWORD);
//		registerItemModel(ModItems.THORIUM_SHOVEL);
//		registerItemModel(ModItems.THORIUM_HOE);
//		registerItemBlockModel(ModBlocks.SILVER_ORE);
//		registerItemBlockModel(ModBlocks.SILVER_BLOCK);
//		registerItemBlockModel(ModBlocks.SILVER_INGOT);
//		registerItemBlockModel(ModBlocks.SILVER_NUGGET);
//		registerItemModel(ModItems.SILVER_HELMET);
//		registerItemModel(ModItems.SILVER_CHESTPLATE);
//		registerItemModel(ModItems.SILVER_LEGGINGS);
//		registerItemModel(ModItems.SILVER_BOOTS);
//		registerItemModel(ModItems.SILVER_PICKAXE);
//		registerItemModel(ModItems.SILVER_AXE);
//		registerItemModel(ModItems.SILVER_SWORD);
//		registerItemModel(ModItems.SILVER_SHOVEL);
//		registerItemModel(ModItems.SILVER_HOE);
//		registerItemBlockModel(ModBlocks.SILVER_WIRE);
//		registerItemBlockModel(ModBlocks.SILVER_ENAMEL);
//		registerItemModel(ModItems.SILVER_COIL);
//		registerItemBlockModel(ModBlocks.SILVER_SPOOL);
//		registerItemModel(ModItems.SILVER_RAIL);
//		registerItemBlockModel(ModBlocks.PLATINUM_ORE);
//		registerItemBlockModel(ModBlocks.PLATINUM_BLOCK);
//		registerItemBlockModel(ModBlocks.PLATINUM_INGOT);
//		registerItemBlockModel(ModBlocks.PLATINUM_NUGGET);
//		registerItemModel(ModItems.PLATINUM_HELMET);
//		registerItemModel(ModItems.PLATINUM_CHESTPLATE);
//		registerItemModel(ModItems.PLATINUM_LEGGINGS);
//		registerItemModel(ModItems.PLATINUM_BOOTS);
//		registerItemModel(ModItems.PLATINUM_PICKAXE);
//		registerItemModel(ModItems.PLATINUM_AXE);
//		registerItemModel(ModItems.PLATINUM_SWORD);
//		registerItemModel(ModItems.PLATINUM_SHOVEL);
//		registerItemModel(ModItems.PLATINUM_HOE);
//		registerItemBlockModel(ModBlocks.PLATINUM_WIRE);
//		registerItemBlockModel(ModBlocks.PLATINUM_ENAMEL);
//		registerItemModel(ModItems.PLATINUM_COIL);
//		registerItemBlockModel(ModBlocks.PLATINUM_SPOOL);
//		registerItemModel(ModItems.PLATINUM_RAIL);
//		registerItemBlockModel(ModBlocks.PLUTONIUM_ORE);
//		registerItemBlockModel(ModBlocks.PLUTONIUM_BLOCK);
//		registerItemBlockModel(ModBlocks.PLUTONIUM_INGOT);
//		registerItemBlockModel(ModBlocks.PLUTONIUM_NUGGET);
//		registerItemModel(ModItems.PLUTONIUM_HELMET);
//		registerItemModel(ModItems.PLUTONIUM_CHESTPLATE);
//		registerItemModel(ModItems.PLUTONIUM_LEGGINGS);
//		registerItemModel(ModItems.PLUTONIUM_BOOTS);
//		registerItemModel(ModItems.PLUTONIUM_PICKAXE);
//		registerItemModel(ModItems.PLUTONIUM_AXE);
//		registerItemModel(ModItems.PLUTONIUM_SWORD);
//		registerItemModel(ModItems.PLUTONIUM_SHOVEL);
//		registerItemModel(ModItems.PLUTONIUM_HOE);
//		registerItemBlockModel(ModBlocks.OSMIUM_ORE);
//		registerItemBlockModel(ModBlocks.OSMIUM_BLOCK);
//		registerItemBlockModel(ModBlocks.OSMIUM_INGOT);
//		registerItemBlockModel(ModBlocks.OSMIUM_NUGGET);
//		registerItemModel(ModItems.OSMIUM_HELMET);
//		registerItemModel(ModItems.OSMIUM_CHESTPLATE);
//		registerItemModel(ModItems.OSMIUM_LEGGINGS);
//		registerItemModel(ModItems.OSMIUM_BOOTS);
//		registerItemModel(ModItems.OSMIUM_PICKAXE);
//		registerItemModel(ModItems.OSMIUM_AXE);
//		registerItemModel(ModItems.OSMIUM_SWORD);
//		registerItemModel(ModItems.OSMIUM_SHOVEL);
//		registerItemModel(ModItems.OSMIUM_HOE);
//		registerItemBlockModel(ModBlocks.OSMIUM_WIRE);
//		registerItemBlockModel(ModBlocks.OSMIUM_ENAMEL);
//		registerItemModel(ModItems.OSMIUM_COIL);
//		registerItemBlockModel(ModBlocks.OSMIUM_SPOOL);
//		registerItemModel(ModItems.OSMIUM_RAIL);
//		registerItemBlockModel(ModBlocks.NICKEL_ORE);
//		registerItemBlockModel(ModBlocks.NICKEL_BLOCK);
//		registerItemBlockModel(ModBlocks.NICKEL_INGOT);
//		registerItemBlockModel(ModBlocks.NICKEL_NUGGET);
//		registerItemModel(ModItems.NICKEL_HELMET);
//		registerItemModel(ModItems.NICKEL_CHESTPLATE);
//		registerItemModel(ModItems.NICKEL_LEGGINGS);
//		registerItemModel(ModItems.NICKEL_BOOTS);
//		registerItemModel(ModItems.NICKEL_PICKAXE);
//		registerItemModel(ModItems.NICKEL_AXE);
//		registerItemModel(ModItems.NICKEL_SWORD);
//		registerItemModel(ModItems.NICKEL_SHOVEL);
//		registerItemModel(ModItems.NICKEL_HOE);
//		registerItemBlockModel(ModBlocks.NICKEL_WIRE);
//		registerItemBlockModel(ModBlocks.NICKEL_ENAMEL);
//		registerItemModel(ModItems.NICKEL_COIL);
//		registerItemBlockModel(ModBlocks.NICKEL_SPOOL);
//		registerItemModel(ModItems.NICKEL_RAIL);
//		registerItemBlockModel(ModBlocks.STEEL_BLOCK);
//		registerItemBlockModel(ModBlocks.STEEL_INGOT);
//		registerItemBlockModel(ModBlocks.STEEL_NUGGET);
//		registerItemModel(ModItems.STEEL_HELMET);
//		registerItemModel(ModItems.STEEL_CHESTPLATE);
//		registerItemModel(ModItems.STEEL_LEGGINGS);
//		registerItemModel(ModItems.STEEL_BOOTS);
//		registerItemModel(ModItems.STEEL_PICKAXE);
//		registerItemModel(ModItems.STEEL_AXE);
//		registerItemModel(ModItems.STEEL_SWORD);
//		registerItemModel(ModItems.STEEL_SHOVEL);
//		registerItemModel(ModItems.STEEL_HOE);
//		registerItemBlockModel(ModBlocks.STEEL_WIRE);
//		registerItemBlockModel(ModBlocks.STEEL_ENAMEL);
//		registerItemModel(ModItems.STEEL_COIL);
//		registerItemBlockModel(ModBlocks.STEEL_SPOOL);
//		registerItemModel(ModItems.STEEL_RAIL);
//		registerItemBlockModel(ModBlocks.ALUMINIUM_ORE);
//		registerItemBlockModel(ModBlocks.ALUMINIUM_BLOCK);
//		registerItemBlockModel(ModBlocks.ALUMINIUM_INGOT);
//		registerItemBlockModel(ModBlocks.ALUMINIUM_NUGGET);
//		registerItemModel(ModItems.ALUMINIUM_HELMET);
//		registerItemModel(ModItems.ALUMINIUM_CHESTPLATE);
//		registerItemModel(ModItems.ALUMINIUM_LEGGINGS);
//		registerItemModel(ModItems.ALUMINIUM_BOOTS);
//		registerItemModel(ModItems.ALUMINIUM_PICKAXE);
//		registerItemModel(ModItems.ALUMINIUM_AXE);
//		registerItemModel(ModItems.ALUMINIUM_SWORD);
//		registerItemModel(ModItems.ALUMINIUM_SHOVEL);
//		registerItemModel(ModItems.ALUMINIUM_HOE);
//		registerItemBlockModel(ModBlocks.ALUMINIUM_WIRE);
//		registerItemBlockModel(ModBlocks.ALUMINIUM_ENAMEL);
//		registerItemModel(ModItems.ALUMINIUM_COIL);
//		registerItemBlockModel(ModBlocks.ALUMINIUM_SPOOL);
//		registerItemModel(ModItems.ALUMINIUM_RAIL);
//		registerItemBlockModel(ModBlocks.COPPER_ORE);
//		registerItemBlockModel(ModBlocks.COPPER_BLOCK);
//		registerItemBlockModel(ModBlocks.COPPER_INGOT);
//		registerItemBlockModel(ModBlocks.COPPER_NUGGET);
//		registerItemModel(ModItems.COPPER_HELMET);
//		registerItemModel(ModItems.COPPER_CHESTPLATE);
//		registerItemModel(ModItems.COPPER_LEGGINGS);
//		registerItemModel(ModItems.COPPER_BOOTS);
//		registerItemModel(ModItems.COPPER_PICKAXE);
//		registerItemModel(ModItems.COPPER_AXE);
//		registerItemModel(ModItems.COPPER_SWORD);
//		registerItemModel(ModItems.COPPER_SHOVEL);
//		registerItemModel(ModItems.COPPER_HOE);
//		registerItemBlockModel(ModBlocks.COPPER_WIRE);
//		registerItemBlockModel(ModBlocks.COPPER_ENAMEL);
//		registerItemModel(ModItems.COPPER_COIL);
//		registerItemBlockModel(ModBlocks.COPPER_SPOOL);
//		registerItemModel(ModItems.COPPER_RAIL);
//		registerItemBlockModel(ModBlocks.GOLD_WIRE);
//		registerItemBlockModel(ModBlocks.GOLD_ENAMEL);
//		registerItemModel(ModItems.GOLD_COIL);
//		registerItemBlockModel(ModBlocks.GOLD_SPOOL);
//		registerItemModel(ModItems.GOLD_RAIL);
//		registerItemBlockModel(ModBlocks.IRON_WIRE);
//		registerItemBlockModel(ModBlocks.IRON_ENAMEL);
//		registerItemModel(ModItems.IRON_COIL);
//		registerItemBlockModel(ModBlocks.IRON_SPOOL);
//		registerItemModel(ModItems.IRON_RAIL);
//		registerItemBlockModel(ModBlocks.LEAD_ORE);
//		registerItemBlockModel(ModBlocks.LEAD_BLOCK);
//		registerItemBlockModel(ModBlocks.LEAD_INGOT);
//		registerItemBlockModel(ModBlocks.LEAD_NUGGET);
//		registerItemModel(ModItems.LEAD_HELMET);
//		registerItemModel(ModItems.LEAD_CHESTPLATE);
//		registerItemModel(ModItems.LEAD_LEGGINGS);
//		registerItemModel(ModItems.LEAD_BOOTS);
//		registerItemModel(ModItems.LEAD_PICKAXE);
//		registerItemModel(ModItems.LEAD_AXE);
//		registerItemModel(ModItems.LEAD_SWORD);
//		registerItemModel(ModItems.LEAD_SHOVEL);
//		registerItemModel(ModItems.LEAD_HOE);
//		registerItemBlockModel(ModBlocks.GALLIUM_ORE);
//		registerItemBlockModel(ModBlocks.GALLIUM_BLOCK);
//		registerItemBlockModel(ModBlocks.GALLIUM_INGOT);
//		registerItemBlockModel(ModBlocks.GALLIUM_NUGGET);
//		registerItemModel(ModItems.GALLIUM_HELMET);
//		registerItemModel(ModItems.GALLIUM_CHESTPLATE);
//		registerItemModel(ModItems.GALLIUM_LEGGINGS);
//		registerItemModel(ModItems.GALLIUM_BOOTS);
//		registerItemModel(ModItems.GALLIUM_PICKAXE);
//		registerItemModel(ModItems.GALLIUM_AXE);
//		registerItemModel(ModItems.GALLIUM_SWORD);
//		registerItemModel(ModItems.GALLIUM_SHOVEL);
//		registerItemModel(ModItems.GALLIUM_HOE);

	}

	private static void registerTileEntity(Class<? extends ModTileEntity> clazz) {
		GameRegistry.registerTileEntity(clazz, new ResourceLocation(ModReference.ID, clazz.getName().replace("TileEntity", "").toLowerCase()));
	}

	@SideOnly(Side.CLIENT)
	protected static final void registerItemModel(final Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "normal"));
	}

	@SideOnly(Side.CLIENT)
	protected static final void registerItemBlockModel(final Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "normal"));
	}

	@SideOnly(Side.CLIENT)
	protected static final void registerBlockItemModel(final Block block) {
		registerItemBlockModel(block);
	}

	@SideOnly(Side.CLIENT)
	protected static final void registerBlockItemItemOverrideModel(final Block block) {
		ModelLoader.setCustomModelResourceLocation(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", block.getRegistryName().getResourcePath())), 0,
				new ModelResourceLocation(block.getRegistryName(), "normal"));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static final void writeMod(final ModelBakeEvent event) {

		ModWritingUtil.writeMod();

	}

}