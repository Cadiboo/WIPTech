package cadiboo.wiptech.util;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ModUtil {

	public static void infoModMaterialsCode() {

		ArrayList<String> modBlocks = new ArrayList<String>();
		ArrayList<String> registerBlocks = new ArrayList<String>();

		ArrayList<String> modItems = new ArrayList<String>();
		ArrayList<String> registerItems = new ArrayList<String>();

		ArrayList<String> registerModels = new ArrayList<String>();

		for (ModMaterials material : ModMaterials.values()) {
			if (material.getProperties().hasOre()) {
				String name = material.getNameUppercase() + "_ORE";
				if (material.getResouceLocationDomain() == "minecraft")
					modBlocks.add("@ObjectHolder(\"minecraft:" + name.toLowerCase() + "\")");
				modBlocks.add("public static final BlockModOre " + name + " = null;");
				registerBlocks.add("registry.register(new BlockModOre(ModMaterials." + material.getNameUppercase() + "));");
				registerItems.add("registry.register(new ItemBlock(ModBlocks." + name + ").setRegistryName(ModBlocks." + name + ".getRegistryName()));");
				registerModels.add("registerItemBlockModel(ModBlocks." + name + ");");
			}

			if (material.getProperties().hasBlock()) {
				String name = material.getNameUppercase() + "_BLOCK";
				if (material.getResouceLocationDomain() == "minecraft")
					modBlocks.add("@ObjectHolder(\"minecraft:" + name.toLowerCase() + "\")");
				modBlocks.add("public static final BlockResource " + name + " = null;");
				registerBlocks.add("registry.register(new BlockResource(ModMaterials." + material.getNameUppercase() + "));");
				registerItems.add("registry.register(new ItemBlock(ModBlocks." + name + ").setRegistryName(ModBlocks." + name + ".getRegistryName()));");
				registerModels.add("registerItemBlockModel(ModBlocks." + name + ");");
			}

			if (material.getProperties().hasIngotAndNugget()) {
				String name = material.getNameUppercase() + "_INGOT";
				modBlocks.add("public static final BlockItem " + name + " = null;");
				registerBlocks.add("registry.register(new BlockItem(ModMaterials." + material.getNameUppercase() + ", BlockItemTypes.INGOT));");
				registerItems.add("registry.register(new ItemBlock(ModBlocks." + name + ").setRegistryName(ModBlocks." + name + ".getRegistryName()));");
				registerModels.add("registerItemBlockModel(ModBlocks." + name + ");");

				name = material.getNameUppercase() + "_NUGGET";
				modBlocks.add("public static final BlockItem " + name + " = null;");
				registerBlocks.add("registry.register(new BlockItem(ModMaterials." + material.getNameUppercase() + ", BlockItemTypes.NUGGET));");
				registerItems.add("registry.register(new ItemBlock(ModBlocks." + name + ").setRegistryName(ModBlocks." + name + ".getRegistryName()));");
				registerModels.add("registerItemBlockModel(ModBlocks." + name + ");");
			}

			if (material.getProperties().hasArmor()) {
				for (String nameSuffix : new String[] { "HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS" }) {

					String name = material.getNameUppercase() + "_" + nameSuffix;
					String slotName;
					switch (nameSuffix) {
						case "HELMET":
							slotName = "HEAD";
						break;
						case "CHESTPLATE":
							slotName = "CHEST";
						break;
						case "LEGGINGS":
							slotName = "LEGS";
						break;
						default:
						case "BOOTS":
							slotName = "FEET";
						break;
					}
					if (material.getResouceLocationDomain() == "minecraft")
						modItems.add("@ObjectHolder(\"minecraft:" + name.toLowerCase() + "\")");
					modItems.add("public static final ItemModArmor" + " " + name + " = null;");
					registerItems.add("registry.register(new ItemModArmor(ModMaterials." + material.getNameUppercase() + ", EntityEquipmentSlot. " + slotName + "));");
					registerModels.add("registerItemModel(ModItems." + name + ");");
				}
			}

			if (material.getProperties().hasTools()) {
				for (String nameSuffix : new String[] { "PICKAXE", "AXE", "SWORD", "SHOVEL" }) {

					String name = material.getNameUppercase() + "_" + nameSuffix;
					String itemType = "Mod" + StringUtils.capitalize(nameSuffix.replace("_", "").toLowerCase());

					if (material.getResouceLocationDomain() == "minecraft")
						modItems.add("@ObjectHolder(\"minecraft:" + name.toLowerCase() + "\")");
					modItems.add("public static final Item" + itemType + " " + name + " = null;");
					registerItems.add("registry.register(new Item" + itemType + "(ModMaterials." + material.getNameUppercase() + "));");
					registerModels.add("registerItemModel(ModItems." + name + ");");
				}
			}

			if (material.getProperties().hasWire()) {
				String name = material.getNameUppercase() + "_WIRE";
				modBlocks.add("public static final BlockWire " + name + " = null;");
				registerBlocks.add("registry.register(new BlockWire(ModMaterials." + material.getNameUppercase() + "));");
				registerModels.add("registerItemBlockModel(ModBlocks." + name + ");");
			}

			if (material.getProperties().hasEnamel()) {
				String name = material.getNameUppercase() + "_ENAMEL";
				modBlocks.add("public static final BlockEnamel " + name + " = null;");
				registerBlocks.add("registry.register(new BlockEnamel(ModMaterials." + material.getNameUppercase() + "));");
				registerModels.add("registerItemBlockModel(ModBlocks." + name + ");");
			}

			if (material.getProperties().hasCoil()) {
				String name = material.getNameUppercase() + "_COIL";
				modItems.add("public static final ItemCoil" + " " + name + " = null;");
				registerItems.add("registry.register(new ItemCoil(ModMaterials." + material.getNameUppercase() + "));");
				registerModels.add("registerItemModel(ModItems." + name + ");");

				name = material.getNameUppercase() + "_SPOOL";
				modBlocks.add("public static final BlockSpool " + name + " = null;");
				registerBlocks.add("registry.register(new BlockSpool(ModMaterials." + material.getNameUppercase() + "));");
				registerItems.add("registry.register(new ItemBlock(ModBlocks." + name + ").setRegistryName(ModBlocks." + name + ".getRegistryName()));");
				registerModels.add("registerItemBlockModel(ModBlocks." + name + ");");
			}

			if (material.getProperties().hasRail()) {
				String name = material.getNameUppercase() + "_RAIL";
				modItems.add("public static final ItemRail" + " " + name + " = null;");
				registerItems.add("registry.register(new ItemRail(ModMaterials." + material.getNameUppercase() + "));");
				registerModels.add("registerItemModel(ModItems." + name + ");");
			}

		}

		WIPTech.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~init.ModBlocks:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		WIPTech.info(modBlocks.toArray());
		WIPTech.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~init.ModItems:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		WIPTech.info(modItems.toArray());

		WIPTech.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~EventSubscriber#onRegisterBlocksEvent~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		WIPTech.info(registerBlocks.toArray());
		WIPTech.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~EventSubscriber#onRegisterItemsEvent:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		WIPTech.info(registerItems.toArray());
		WIPTech.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~EventSubscriber#onRegisterModelsEvent:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		WIPTech.info(registerModels.toArray());

	}

	/**
	 * gets the game name in uppercase
	 */
	public static final String getSlotGameNameUppercase(final EntityEquipmentSlot slotIn) {
		switch (slotIn) {
			case CHEST:
				return "CHESTPLATE";
			case FEET:
				return "BOOTS";
			case HEAD:
				return "HELMET";
			case LEGS:
				return "LEGGINGS";
			default:
				return slotIn.name();
		}
	}

	/**
	 * Converts the game name to lowercase as per
	 * {@link java.lang.String#toLowerCase() String.toLowerCase}.
	 */
	public static final String getSlotGameNameLowercase(final EntityEquipmentSlot slotIn) {
		return getSlotGameNameUppercase(slotIn).toLowerCase();
	}

	/**
	 * Capitalizes the game name as per
	 * {@link org.apache.commons.lang3.StringUtils#capitalize(String)
	 * StringUtils.capitalize}.
	 */
	public static final String getSlotGameNameFormatted(final EntityEquipmentSlot slotIn) {
		return StringUtils.capitalize(getSlotGameNameLowercase(slotIn));
	}

}
