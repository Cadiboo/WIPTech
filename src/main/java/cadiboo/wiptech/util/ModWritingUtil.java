package cadiboo.wiptech.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.annotations.VisibleForTesting;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.init.ModBlocks;
import cadiboo.wiptech.init.ModItems;
import cadiboo.wiptech.util.ModEnums.ModMaterialTypes;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ExistsForDebugging
@SideOnly(Side.CLIENT)
public class ModWritingUtil {

	@VisibleForTesting
	public static String default_variant_name = "normal";
	private static String assetDir = "/Users/" + System.getProperty("user.name") + "/Developer/Modding/WIPTechAlpha/src/main/resources/assets/wiptech/";
	private static String dataDir = "/Users/" + System.getProperty("user.name") + "/Developer/Modding/WIPTechAlpha/src/main/resources/data/wiptech/";

	private static String langFileName = "en_us";

	@ExistsForDebugging
	@SideOnly(Side.CLIENT)
	public static void writeMod() {

		final boolean recipes = true;
		final boolean lang = true;
		final boolean json = true;

		WIPTech.info("ModWritingUtil.writeMod() with options write recipes: " + recipes + ", write lang: " + lang + ", write json: " + json);

		final ArrayList<Tuple<String, String>> blockstates = new ArrayList<>();
		final ArrayList<Tuple<String, String>> blockModels = new ArrayList<>();
		final ArrayList<Tuple<String, String>> itemModels = new ArrayList<>();

		for (final ModMaterials material : ModMaterials.values()) {

			if (material.getProperties().hasOre()) {
				final String suffixLower = "ore";
				final String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				if (material.getResouceLocationDomain("ore", ForgeRegistries.BLOCKS) != "minecraft") {
					blockstates.add(new Tuple<>(nameUpper, generateBlockstateJSON(nameUpper)));
				}

				if (material.getResouceLocationDomain("ore", ForgeRegistries.BLOCKS) != "minecraft") {
					blockModels.add(new Tuple<>(nameUpper, generateBlockModelJSON(new ModResourceLocation(ModReference.MOD_ID, "block/ore"), suffixLower, nameUpper)));
				}

			}

			//

			if (material.getProperties().hasBlock()) {
				final String suffixLower = "block";
				final String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				if (material.getResouceLocationDomain("block", ForgeRegistries.BLOCKS) != "minecraft") {
					blockstates.add(new Tuple<>(nameUpper, generateBlockstateJSON(nameUpper)));
				}

				if (material.getResouceLocationDomain("block", ForgeRegistries.BLOCKS) != "minecraft") {
					blockModels.add(new Tuple<>(nameUpper, generateBlockModelJSON(new ModResourceLocation("", "block/cube_all"), "all", nameUpper)));
				}
			}

			//

			if (material.getProperties().hasResource()) {

				String suffixLower = material.getType().getResourceNameSuffix().toLowerCase();
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				/* @formatter:off */
				blockstates.add(new Tuple<>(nameUpper, "{\n" + "    \"variants\": {\n" + "        \"facing=north\": { \"model\": \"" + ModReference.MOD_ID + ":" + nameUpper.toLowerCase()
						+ "\", \"y\": 0 },\n" + "        \"facing=south\": { \"model\": \"" + ModReference.MOD_ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 180 },\n"
						+ "        \"facing=west\":  { \"model\": \"" + ModReference.MOD_ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 },\n" + "        \"facing=east\":  { \"model\": \""
						+ ModReference.MOD_ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 90 }\n" + "    }\n" + "}\n"));
				/* @formatter:on */
				blockModels.add(new Tuple<>(nameUpper, generateBlockItemModelJSON(material, suffixLower)));

				if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft") {
					itemModels.add(new Tuple<>(nameUpper, generateItemModelJSON(nameUpper)));
				}

				if (material.getType() != ModMaterialTypes.GEM) {
					suffixLower = material.getType().getResourcePieceNameSuffix().toLowerCase();
					nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

					if (material == ModMaterials.GOLD) {
					/* @formatter:off */
					blockstates.add(new Tuple<>(nameUpper, "{\n" + "    \"variants\": {\n" + "        \"facing=north\": { \"model\": \"" + ModReference.MOD_ID + ":" + nameUpper.toLowerCase()
							+ "\", \"y\": 180},\n" + "        \"facing=south\": { \"model\": \"" + ModReference.MOD_ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 0 },\n"
							+ "        \"facing=west\":  { \"model\": \"" + ModReference.MOD_ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 90 },\n" + "        \"facing=east\":  { \"model\": \""
							+ ModReference.MOD_ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 }\n" + "    }\n" + "}\n"));
				/* @formatter:on */
					} else {
					/* @formatter:off */
					blockstates.add(new Tuple<>(nameUpper, "{\n" + "    \"variants\": {\n" + "        \"facing=north\": { \"model\": \"" + ModReference.MOD_ID + ":" + nameUpper.toLowerCase()
							+ "\", \"y\": 90 },\n" + "        \"facing=south\": { \"model\": \"" + ModReference.MOD_ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 },\n"
							+ "        \"facing=west\":  { \"model\": \"" + ModReference.MOD_ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 0 },\n" + "        \"facing=east\":  { \"model\": \""
							+ ModReference.MOD_ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 180 }\n" + "    }\n" + "}\n"));
				}
				/* @formatter:on */
					blockModels.add(new Tuple<>(nameUpper, generateBlockItemModelJSON(material, suffixLower)));

					if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft") {
						itemModels.add(new Tuple<>(nameUpper, generateItemModelJSON(nameUpper)));
					}
				}
			}

			//

			if (material.getProperties().hasArmor()) {
				for (final String suffixUpper : new String[]{"HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS"}) {

					final String suffixLower = suffixUpper.toLowerCase();
					final String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

					String slotName;
					switch (suffixLower.toUpperCase()) {
						case "HELMET" :
							slotName = "HEAD";
							break;
						case "CHESTPLATE" :
							slotName = "CHEST";
							break;
						case "LEGGINGS" :
							slotName = "LEGS";
							break;
						default :
						case "BOOTS" :
							slotName = "FEET";
							break;
					}

					if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft") {
						itemModels.add(new Tuple<>(nameUpper, generateItemModelJSON(nameUpper)));
					}
				}
			}

			//

			if (material.getProperties().hasTools()) {
				for (final String suffixUpper : new String[]{"PICKAXE", "AXE", "SWORD", "SHOVEL", "HOE"}) {

					final String suffixLower = suffixUpper.toLowerCase();
					final String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

					final String itemType = "Mod" + StringUtils.capitalize(suffixLower);

					if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft") {
						itemModels.add(new Tuple<>(nameUpper, generateItemModelJSON(nameUpper, new ModResourceLocation("", "item/handheld"))));
					}
				}
			}

			//

			if (material.getProperties().hasWire()) {
				final String suffixLower = "wire";
				final String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				itemModels.add(new Tuple<>(nameUpper, generateItemModelJSON(nameUpper, new ModResourceLocation(ModReference.MOD_ID, suffixLower))));

				blockstates.add(new Tuple<>(nameUpper, generateBlockstateJSON("wire_model/" + nameUpper)));
				// blockModels.add(new Tuple<String, String>(nameUpper, "{\n" + " \"parent\": \"wiptech:block/" + material.getNameLowercase() + "_" + suffixLower + "_core\",\n" + "}\n" + ""));
				blockModels.add(new Tuple<>(nameUpper + "_core", generateBlockItemModelJSON(material, suffixLower + "_core")));

				for (final EnumFacing facing : EnumFacing.VALUES) {
					// String name = (nameUpper + "_" + facing.name()).toLowerCase();

					blockModels.add(new Tuple<>(nameUpper + "_extension_" + facing.name().toLowerCase(), generateBlockItemModelJSON(material, suffixLower + "_extension_" + facing.name().toLowerCase())));
				}
			}

			//

			if (material.getProperties().hasEnamel()) {
				final String suffixLower = "enamel";
				final String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				itemModels.add(new Tuple<>(nameUpper, generateItemModelJSON(nameUpper, new ModResourceLocation(ModReference.MOD_ID, suffixLower))));

				blockstates.add(new Tuple<>(nameUpper, generateBlockstateJSON("wire_model/" + nameUpper)));
				// blockModels.add(new Tuple<String, String>(nameUpper, "{\n" + " \"parent\": \"wiptech:block/" + material.getNameLowercase() + "_" + suffixLower + "_core\",\n" + "}\n" + ""));
				blockModels.add(new Tuple<>(nameUpper + "_core", generateBlockItemModelJSON(material, suffixLower + "_core")));

				for (final EnumFacing facing : EnumFacing.VALUES) {
					// String name = (nameUpper + "_" + facing.name()).toLowerCase();

					blockModels.add(new Tuple<>(nameUpper + "_extension_" + facing.name().toLowerCase(), generateBlockItemModelJSON(material, suffixLower + "_extension_" + facing.name().toLowerCase())));
				}

			}

			if (material.getProperties().hasRailgunSlug()) {
				final String suffixLower = "slug";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();
				itemModels.add(new Tuple<>(nameUpper,

				/* @formatter:off */
						"{\n" + "	\"parent\": \"" + ModReference.MOD_ID + ":entity/" + suffixLower + "\",\n" + "	\"textures\": {\n" + "		\"ingot\": \"" + material.getResouceLocationDomain("ingot",
								ForgeRegistries.ITEMS) + ":item" + ((material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS) != "minecraft") || Loader.MC_VERSION.contains("1.13") ? "" : "s") + "/" + material
										.getNameLowercase() + "_ingot\"\n" + "	}\n" + "}\n"
						/* @formatter:on */

				));

				final String prefixLower = "cased";
				nameUpper = prefixLower.toUpperCase() + "_" + material.getNameUppercase() + "_" + suffixLower.toUpperCase();
				itemModels.add(new Tuple<>(nameUpper, "{\n" + "	\"parent\": \"wiptech:item/cased_slug_model\"\n" + "}\n"));
			}

			//

			if (material.getProperties().hasCoil()) {
				String suffixLower = "coil";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				itemModels.add(new Tuple<>(nameUpper, generateItemModelJSON(nameUpper)));

				suffixLower = "spool";
				nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

//				/* @formatter:off */
//				blockstates.add(new Tuple<String, String>(nameUpper,
//						"{\n" + "    \"variants\": {\n"
//								+ "        \"facing=north\": { \"model\": \"" + ModReference.MOD_ID + ":" + nameUpper.toLowerCase() + "\" },\n"
//								+ "        \"facing=south\": { \"model\": \"" + ModReference.MOD_ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 180 },\n"
//								+ "        \"facing=west\":  { \"model\": \"" + ModReference.MOD_ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 },\n"
//								+ "        \"facing=east\":  { \"model\": \"" + ModReference.MOD_ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 90 }\n"
//						+ "    }\n" + "}\n"));
//				/* @formatter:on */
				blockstates.add(new Tuple<>(nameUpper, generateBlockstateJSON(nameUpper)));
				blockModels.add(new Tuple<>(nameUpper, generateBlockItemModelJSON(material, suffixLower)));
			}

			//

			if (material.getProperties().hasRail()) {
				final String suffixLower = "rail";
				final String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				itemModels.add(new Tuple<>(nameUpper, generateItemModelJSON(nameUpper)));
			}

		}

		if (lang) {

			WIPTech.info("Initialising recipes");

			final ArrayList<Tuple<String, String>> recipesObj = new ArrayList<>();

			for (final ModMaterials material : ModMaterials.values()) {

				if ((material.getOre() != null) && (material.getResource() != null) && (material.getType() == ModMaterialTypes.METAL)) {
					GameRegistry.addSmelting(new ItemStack(material.getOre()), new ItemStack(material.getResource()), 1);
				}

				if ((material.getBlock() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<>(material.getBlock().getRegistryName().getResourcePath(), "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"pattern\": [\n" +
							"		\"###\",\n" +
							"		\"###\",\n" +
							"		\"###\"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"#\": {\n" +
							"			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getBlock().getRegistryName().toString()+"\"\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
				}

				if ((material.getResource() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<>(material.getResource().getRegistryName().getResourcePath() + "_from_block", "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"group\": \""+material.getResource().getRegistryName().getResourcePath()+"\",\n" +
							"	\"pattern\": [\n" +
							"		\"#\"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"#\": {\n" +
							"			\"item\": \""+material.getBlock().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getResource().getRegistryName().toString()+"\",\n" +
							"		\"count\": 9\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
					if (material.getResourcePiece() != null) {
						/*@formatter:off*/
						recipesObj.add(new Tuple<>(material.getResource().getRegistryName().getResourcePath() + "_from_"+material.getType().getResourcePieceNameSuffix()+"s", "{\n" +
								"	\"type\": \"minecraft:crafting_shaped\",\n" +
								"	\"group\": \""+material.getResource().getRegistryName().getResourcePath()+"\",\n" +
								"	\"pattern\": [\n" +
								"		\"###\",\n" +
								"		\"###\",\n" +
								"		\"###\"\n" +
								"	],\n" +
								"	\"key\": {\n" +
								"		\"#\": {\n" +
								"			\"item\": \""+material.getResourcePiece().getRegistryName().toString()+"\"\n" +
								"		}\n" +
								"	},\n" +
								"	\"result\": {\n" +
								"		\"item\": \""+material.getResource().getRegistryName().toString()+"\",\n" +
								"		\"count\": 1\n" +
								"	}\n" +
								"}"));
						/*@formatter:on*/
					}
				}

				if ((material.getResourcePiece() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourcePieceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
					/*@formatter:off*/
					recipesObj.add(
							new Tuple<>(
									material
									.getResourcePiece()
									.getRegistryName()
									.getResourcePath()
									, "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"pattern\": [\n" +
							"		\"#\"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"#\": {\n" +
							" 			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getResourcePiece().getRegistryName().toString()+"\",\n" +
							"		\"count\": 9\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
				}

				// armor

				if ((material.getHelmet() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<>(material.getHelmet().getRegistryName().getResourcePath(), "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"pattern\": [\n" +
							"		\"XXX\",\n" +
							"		\"X X\"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"X\": {\n" +
							" 			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getHelmet().getRegistryName().toString()+"\",\n" +
							"		\"count\": 1\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
				}

				if ((material.getChestplate() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<>(material.getChestplate().getRegistryName().getResourcePath(), "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"pattern\": [\n" +
							"		\"X X\",\n" +
							"		\"XXX\",\n" +
							"		\"XXX\"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"X\": {\n" +
							" 			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getChestplate().getRegistryName().toString()+"\",\n" +
							"		\"count\": 1\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
				}

				if ((material.getLeggings() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<>(material.getLeggings().getRegistryName().getResourcePath(), "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"pattern\": [\n" +
							"		\"XXX\",\n" +
							"		\"X X\",\n" +
							"		\"X X\"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"X\": {\n" +
							" 			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getLeggings().getRegistryName().toString()+"\",\n" +
							"		\"count\": 1\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
				}

				if ((material.getBoots() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<>(material.getBoots().getRegistryName().getResourcePath(), "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"pattern\": [\n" +
							"		\"X X\",\n" +
							"		\"X X\"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"X\": {\n" +
							" 			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getBoots().getRegistryName().toString()+"\",\n" +
							"		\"count\": 1\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
				}

				// tools

				if ((material.getAxe() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<>(material.getAxe().getRegistryName().getResourcePath(), "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"pattern\": [\n" +
							"		\"XX\",\n" +
							"		\"X#\",\n" +
							"		\" #\"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"#\": {\n" +
							"			\"item\": \"minecraft:stick\"\n" +
							"		}," +
							"		\"X\": {\n" +
							" 			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getAxe().getRegistryName().toString()+"\",\n" +
							"		\"count\": 1\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
				}

				if ((material.getPickaxe() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<>(material.getPickaxe().getRegistryName().getResourcePath(), "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"pattern\": [\n" +
							"		\"XXX\",\n" +
							"		\" # \",\n" +
							"		\" # \"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"#\": {\n" +
							"			\"item\": \"minecraft:stick\"\n" +
							"		}," +
							"		\"X\": {\n" +
							" 			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getPickaxe().getRegistryName().toString()+"\",\n" +
							"		\"count\": 1\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
				}

				if ((material.getSword() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<>(material.getSword().getRegistryName().getResourcePath(), "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"pattern\": [\n" +
							"		\"X\",\n" +
							"		\"X\",\n" +
							"		\"#\"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"#\": {\n" +
							"			\"item\": \"minecraft:stick\"\n" +
							"		}," +
							"		\"X\": {\n" +
							" 			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getSword().getRegistryName().toString()+"\",\n" +
							"		\"count\": 1\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
				}

				if ((material.getShovel() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<>(material.getShovel().getRegistryName().getResourcePath(), "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"pattern\": [\n" +
							"		\"X\",\n" +
							"		\"#\",\n" +
							"		\"#\"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"#\": {\n" +
							"			\"item\": \"minecraft:stick\"\n" +
							"		}," +
							"		\"X\": {\n" +
							" 			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getShovel().getRegistryName().toString()+"\",\n" +
							"		\"count\": 1\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
				}

				if ((material.getHoe() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<>(material.getHoe().getRegistryName().getResourcePath(), "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"pattern\": [\n" +
							"		\"XX\",\n" +
							"		\" #\",\n" +
							"		\" #\"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"#\": {\n" +
							"			\"item\": \"minecraft:stick\"\n" +
							"		},\n" +
							"		\"X\": {\n" +
							" 			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getHoe().getRegistryName().toString()+"\",\n" +
							"		\"count\": 1\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
				}

				//

				// if (material.getWire() != null&& (material.getResource()!=null) )
//					/*@formatter:off*/
//					recipesObj.add(new Tuple<String, String>(material.getWire().getRegistryName().getResourcePath(), "{\n" +
//							"	\"type\": \"minecraft:crafting_shaped\",\n" +
//							"	\"pattern\": [\n" +
//							"		\"##\"\n" +
//							"	],\n" +
//							"	\"key\": {\n" +
//							"		\"#\": {\n" +
//							"			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
//							"		}\n" +
//							"	},\n" +
//							"	\"result\": {\n" +
//							"		\"item\": \""+material.getWire().getRegistryName().toString()+"\"\n" +
//							"	}\n" +
//							"}"));
//					/*@formatter:on*/

				if (material.getEnamel() != null) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<>(material.getEnamel().getRegistryName().getResourcePath(), "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"pattern\": [\n" +
							"		\"XXX\",\n" +
							"		\"X#X\",\n" +
							"		\"XXX\"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"#\": {\n" +
							"			\"item\": \"minecraft:concrete\",\n" +
							"			\"data\": 32767\n" +
							"		},\n" +
							"		\"X\": {\n" +
							" 			\"item\": \""+material.getWire().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getEnamel().getRegistryName().toString()+"\",\n" +
							"		\"count\": 8\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
				}

				if (material.getRail() != null) {
					;
				}

				if (material.getCoil() != null) {
					;
				}

				if (material.getSpool() != null) {
					;
				}

				if (material.getSlugItem() != null) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<>(material.getCasedSlug().getRegistryName().getResourcePath(), "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"pattern\": [\n" +
							"		\" B \",\n" +
							"		\"AXX\",\n" +
							"		\" C \"\n" +
							"	],\n" +
							"	\"key\": {\n" +
							"		\"A\": {\n" +
							" 			\"item\": \""+ModItems.SLUG_CASING_BACK.getRegistryName().toString()+"\"\n" +
							"		}," +
							"		\"B\": {\n" +
							" 			\"item\": \""+ModItems.SLUG_CASING_TOP.getRegistryName().toString()+"\"\n" +
							"		}," +
							"		\"C\": {\n" +
							" 			\"item\": \""+ModItems.SLUG_CASING_BOTTOM.getRegistryName().toString()+"\"\n" +
							"		}," +
							"		\"X\": {\n" +
							" 			\"item\": \""+material.getSlugItem().getRegistryName().toString()+"\"\n" +
							"		}\n" +
							"	},\n" +
							"	\"result\": {\n" +
							"		\"item\": \""+material.getCasedSlug().getRegistryName().toString()+"\",\n" +
							"		\"count\": 1\n" +
							"	}\n" +
							"}"));
					/*@formatter:on*/
				}

			}

			WIPTech.info("Writing recipes");
			for (final Tuple<String, String> recipe : recipesObj) {
				final String name = recipe.getFirst();
				final List<String> data = new ArrayList<>();
				data.add(recipe.getSecond());
				// TODO dataDir for 1.13
				final Path file = Paths.get(assetDir + "recipes/" + name.toLowerCase() + ".json");
				try {
					Files.write(file, data, Charset.forName("UTF-8"));
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

		}

		if (lang) {

			WIPTech.info("Initialising lang");

			final ArrayList<Tuple<String, String>> langObj = new ArrayList<>();

			for (final ModMaterials material : ModMaterials.values()) {

				if (material.getOre() != null) {
					langObj.add(new Tuple<>(material.getOre().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Ore"));
				}

				if (material.getBlock() != null) {
					langObj.add(new Tuple<>(material.getBlock().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Block"));
				}

				if (material.getResource() != null) {
					langObj.add(new Tuple<>(material.getResource().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " " + StringUtils.capitalize(material.getType().getResourceNameSuffix())));
				}

				if (material.getResourcePiece() != null) {
					langObj.add(new Tuple<>(material.getResourcePiece().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " " + StringUtils.capitalize(material.getType().getResourcePieceNameSuffix())));
				}

				// armor

				if (material.getHelmet() != null) {
					langObj.add(new Tuple<>(material.getHelmet().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Helmet"));
				}

				if (material.getChestplate() != null) {
					langObj.add(new Tuple<>(material.getChestplate().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Chestplate"));
				}

				if (material.getLeggings() != null) {
					langObj.add(new Tuple<>(material.getLeggings().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Leggings"));
				}

				if (material.getBoots() != null) {
					langObj.add(new Tuple<>(material.getBoots().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Boots"));
				}

				// tools

				if (material.getAxe() != null) {
					langObj.add(new Tuple<>(material.getAxe().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Axe"));
				}

				if (material.getPickaxe() != null) {
					langObj.add(new Tuple<>(material.getPickaxe().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Pickaxe"));
				}

				if (material.getSword() != null) {
					langObj.add(new Tuple<>(material.getSword().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Sword"));
				}

				if (material.getShovel() != null) {
					langObj.add(new Tuple<>(material.getShovel().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Shovel"));
				}

				if (material.getHoe() != null) {
					langObj.add(new Tuple<>(material.getHoe().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Hoe"));
				}

				//

				if (material.getWire() != null) {
					langObj.add(new Tuple<>(material.getWire().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Wire"));
				}

				if (material.getEnamel() != null) {
					langObj.add(new Tuple<>(material.getEnamel().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Enamel"));
				}

				if (material.getRail() != null) {
					langObj.add(new Tuple<>(material.getRail().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Rail"));
				}

				if (material.getCoil() != null) {
					langObj.add(new Tuple<>(material.getCoil().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Coil"));
				}

				if (material.getSpool() != null) {
					langObj.add(new Tuple<>(material.getSpool().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Spool"));
				}

				if (material.getSlugItem() != null) {
					langObj.add(new Tuple<>(material.getSlugItem().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Slug"));
					langObj.add(new Tuple<>(material.getCasedSlug().getUnlocalizedName(), "Cased " + getLocalisedName(material.getNameLowercase()) + " Slug"));
				}
			}

			for (final Field field : ModItems.class.getFields()) {
				Object value;
				try {
					value = field.get(ModItems.class);

					if (!(value instanceof Item)) {
						continue;
					}

					final Item item = (Item) value;

					if (item != null) {
						langObj.add(new Tuple<>(item.getUnlocalizedName(), getLocalisedName(item.getRegistryName().getResourcePath())));
					}

				} catch (IllegalArgumentException | IllegalAccessException e) {
					WIPTech.error("fucking hell jeff...");
					e.printStackTrace();
				}
			}

			for (final Field field : ModBlocks.class.getFields()) {
				Object value;
				try {
					value = field.get(ModItems.class);

					if (!(value instanceof Block)) {
						continue;
					}

					final Block block = (Block) value;

					if (block != null) {
						langObj.add(new Tuple<>(block.getUnlocalizedName(), getLocalisedName(block.getRegistryName().getResourcePath())));
					}

				} catch (IllegalArgumentException | IllegalAccessException e) {
					WIPTech.error("fucking hell derek...");
					e.printStackTrace();
				}
			}

			final List<String> data = new ArrayList<>();

			for (final Tuple<String, String> langEntry : langObj) {
				final String unlocalised = langEntry.getFirst();
				final String name = langEntry.getSecond();

				if (Loader.MC_VERSION.contains("1.13")) {
					data.add("\"" + unlocalised + ".name" + "\"" + ": " + "\"" + name + "\"" + ",");
				} else {
					data.add(unlocalised + ".name" + "=" + name);
				}
			}

			if (Loader.MC_VERSION.contains("1.13")) {
				String removeComma = data.get(data.size() - 1);
				removeComma = removeComma.substring(0, removeComma.length() - 2);
				data.set(data.size() - 1, removeComma);
			}

			final ArrayList<String> finalData = new ArrayList<>();
			if (Loader.MC_VERSION.contains("1.13")) {
				finalData.add("{");
			}

			if (Loader.MC_VERSION.contains("1.13")) {
				finalData.add("\"itemGroup." + ModReference.MOD_ID + "\"" + ": " + "\"" + ModReference.MOD_NAME + "\"" + ",");
			} else {
				finalData.add("itemGroup." + ModReference.MOD_ID + "=" + ModReference.MOD_NAME);
			}

			finalData.addAll(data);
			if (Loader.MC_VERSION.contains("1.13")) {
				finalData.add("}");
			}

			final Path file = Paths.get(assetDir + "lang/en_us." + (Loader.MC_VERSION.contains("1.13") ? "json" : "lang"));
			WIPTech.info("Writing lang");
			try {
				Files.write(file, finalData, Charset.forName("UTF-8"));
			} catch (final IOException e) {
				e.printStackTrace();
			}

		}

		if (json) {

			WIPTech.info("Writing blockstates");
			for (final Tuple<String, String> state : blockstates) {
				final String name = state.getFirst();
				final List<String> data = new ArrayList<>();
				data.add(state.getSecond());
				final Path file = Paths.get(assetDir + "blockstates/" + name.toLowerCase() + ".json");
				try {
					Files.write(file, data, Charset.forName("UTF-8"));
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

			WIPTech.info("Writing blockModels");
			for (final Tuple<String, String> blockModel : blockModels) {
				final String name = blockModel.getFirst();
				final List<String> data = new ArrayList<>();
				data.add(blockModel.getSecond());
				final Path file = Paths.get(assetDir + "models/block/" + name.toLowerCase() + ".json");
				try {
					Files.write(file, data, Charset.forName("UTF-8"));
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

			WIPTech.info("Writing itemModels");
			for (final Tuple<String, String> itemModel : itemModels) {
				final String name = itemModel.getFirst();
				final List<String> data = new ArrayList<>();
				data.add(itemModel.getSecond());
				final Path file = Paths.get(assetDir + "models/item/" + name.toLowerCase() + ".json");
				try {
					Files.write(file, data, Charset.forName("UTF-8"));
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	private static String getLocalisedName(String unlocalised) {
		if (langFileName.toLowerCase().equals("en_us")) {
			unlocalised = unlocalised.replace("aluminium", "aluminum");
		}
		return ModUtil.getLocalisedName(unlocalised);
	}

	private static final String generateItemModelJSON(final String name, final ModResourceLocation parent) {

		/* @formatter:off */
		final String ret = "{" + "\n\t" + "\"parent\": \"" + parent.toString() + "\"," + "\n\t" + "\"textures\": {" + "\n\t\t" + "\"layer0\": \"" + ModReference.MOD_ID + ":item/" + name.toLowerCase()
				+ "\"" + "\n\t" + "}" + "\n" + "}" + "\n";
		/* @formatter:on */
		return ret;

	}

	private static final String generateItemModelJSON(final String name) {
		return generateItemModelJSON(name, new ModResourceLocation("", "item/generated"));
	}

	private static final String generateBlockModelJSON(final ModResourceLocation fullParentPath, final String textureName, final String texture) {

		/* @formatter:off */
		final String ret = "{" + "\n\t" + "\"parent\": \"" + fullParentPath.toString() + "\"," + "\n\t" + "\"textures\": {" + "\n\t\t" + "\"" + textureName.toLowerCase() + "\": \"" + ModReference.MOD_ID
				+ ":block/" + texture.toLowerCase() + "\"" + "\n\t" + "}" + "\n" + "}\n";
		/* @formatter:on */
		return ret;

	}

	private static final String generateBlockItemModelJSON(final ModMaterials material, final String suffixLower) {

		/* @formatter:off */
		final String ret = "{\n" + "	\"parent\": \"" + ModReference.MOD_ID + ":block/" + suffixLower + "\",\n" + "	\"textures\": {\n" + "		\"ingot\": \"" + material.getResouceLocationDomain("ingot",
				ForgeRegistries.ITEMS) + ":item" + ((material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS) != "minecraft") || Loader.MC_VERSION.contains("1.13") ? "" : "s") + "/" + material
						.getNameLowercase() + "_ingot\"\n" + "	}\n" + "}\n";
		/* @formatter:on */
		return ret;

	}

	private static final String generateBlockstateJSON(final String name) {

		/* @formatter:off */
		final String ret = "{" + "\n\t" + "\"variants\": {" + "\n\t\t" + "\"" + default_variant_name + "\": {" + "\n\t\t\t" + "\"model\": \"" + ModReference.MOD_ID + ":" + name.toLowerCase() + "\"" + "\n\t\t"
				+ "}" + "\n\t" + "}\n" + "}\n";
		/* @formatter:on */
		return ret;

	}

}
