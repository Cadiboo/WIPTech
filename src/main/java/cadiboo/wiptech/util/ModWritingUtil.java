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
import cadiboo.wiptech.block.BlockItem;
import cadiboo.wiptech.init.ModBlocks;
import cadiboo.wiptech.init.ModItems;
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

	@ExistsForDebugging
	@SideOnly(Side.CLIENT)
	public static void writeMod() {

		boolean recipes = true;
		boolean lang = true;
		boolean json = true;

		WIPTech.info("infoModMaterialsCode with options write recipes: " + recipes + ", write lang: " + lang + ", write json: " + json);

		ArrayList<Tuple<String, String>> blockstates = new ArrayList<Tuple<String, String>>();
		ArrayList<Tuple<String, String>> blockModels = new ArrayList<Tuple<String, String>>();
		ArrayList<Tuple<String, String>> itemModels = new ArrayList<Tuple<String, String>>();

		for (ModMaterials material : ModMaterials.values()) {

			if (material.getProperties().hasOre()) {
				String suffixLower = "ore";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				if (material.getResouceLocationDomain("ore", ForgeRegistries.BLOCKS) != "minecraft")
					blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON(nameUpper)));

				if (material.getResouceLocationDomain("ore", ForgeRegistries.BLOCKS) != "minecraft")
					blockModels.add(new Tuple<String, String>(nameUpper, generateBlockModelJSON(new ModResourceLocation(ModReference.Version.getModId(), "block/ore"), suffixLower, nameUpper)));

			}

			//

			if (material.getProperties().hasBlock()) {
				String suffixLower = "block";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				if (material.getResouceLocationDomain("block", ForgeRegistries.BLOCKS) != "minecraft")
					blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON(nameUpper)));

				if (material.getResouceLocationDomain("block", ForgeRegistries.BLOCKS) != "minecraft")
					blockModels.add(new Tuple<String, String>(nameUpper, generateBlockModelJSON(new ModResourceLocation("", "block/cube_all"), "all", nameUpper)));
			}

			//

			if (material.getProperties().hasIngotAndNugget()) {
				String suffixLower = "ingot";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				/* @formatter:off */
				blockstates.add(new Tuple<String, String>(nameUpper, "{\n" + "    \"variants\": {\n" + "        \"facing=north\": { \"model\": \"" + ModReference.Version.getModId() + ":" + nameUpper.toLowerCase()
						+ "\", \"y\": 0 },\n" + "        \"facing=south\": { \"model\": \"" + ModReference.Version.getModId() + ":" + nameUpper.toLowerCase() + "\", \"y\": 180 },\n"
						+ "        \"facing=west\":  { \"model\": \"" + ModReference.Version.getModId() + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 },\n" + "        \"facing=east\":  { \"model\": \""
						+ ModReference.Version.getModId() + ":" + nameUpper.toLowerCase() + "\", \"y\": 90 }\n" + "    }\n" + "}\n"));
				/* @formatter:on */
				blockModels.add(new Tuple<String, String>(nameUpper, generateBlockItemModelJSON(material, suffixLower)));

				if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft")
					itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper)));

				suffixLower = "nugget";
				nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				if (material == ModMaterials.GOLD) /* gold is sideways */
					/* @formatter:off */
					blockstates.add(new Tuple<String, String>(nameUpper, "{\n" + "    \"variants\": {\n" + "        \"facing=north\": { \"model\": \"" + ModReference.Version.getModId() + ":" + nameUpper.toLowerCase()
							+ "\", \"y\": 180},\n" + "        \"facing=south\": { \"model\": \"" + ModReference.Version.getModId() + ":" + nameUpper.toLowerCase() + "\", \"y\": 0 },\n"
							+ "        \"facing=west\":  { \"model\": \"" + ModReference.Version.getModId() + ":" + nameUpper.toLowerCase() + "\", \"y\": 90 },\n" + "        \"facing=east\":  { \"model\": \""
							+ ModReference.Version.getModId() + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 }\n" + "    }\n" + "}\n"));
				/* @formatter:on */
				else
					/* @formatter:off */
					blockstates.add(new Tuple<String, String>(nameUpper, "{\n" + "    \"variants\": {\n" + "        \"facing=north\": { \"model\": \"" + ModReference.Version.getModId() + ":" + nameUpper.toLowerCase()
							+ "\", \"y\": 90 },\n" + "        \"facing=south\": { \"model\": \"" + ModReference.Version.getModId() + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 },\n"
							+ "        \"facing=west\":  { \"model\": \"" + ModReference.Version.getModId() + ":" + nameUpper.toLowerCase() + "\", \"y\": 0 },\n" + "        \"facing=east\":  { \"model\": \""
							+ ModReference.Version.getModId() + ":" + nameUpper.toLowerCase() + "\", \"y\": 180 }\n" + "    }\n" + "}\n"));
				/* @formatter:on */
				blockModels.add(new Tuple<String, String>(nameUpper, generateBlockItemModelJSON(material, suffixLower)));

				if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft")
					itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper)));
			}

			//

			if (material.getProperties().hasArmor()) {
				for (String suffixUpper : new String[] { "HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS" }) {

					String suffixLower = suffixUpper.toLowerCase();
					String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

					String slotName;
					switch (suffixLower.toUpperCase()) {
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

					if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft")
						itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper)));
				}
			}

			//

			if (material.getProperties().hasTools()) {
				for (String suffixUpper : new String[] { "PICKAXE", "AXE", "SWORD", "SHOVEL", "HOE" }) {

					String suffixLower = suffixUpper.toLowerCase();
					String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

					String itemType = "Mod" + StringUtils.capitalize(suffixLower);

					if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft")
						itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper, new ModResourceLocation("", "item/handheld"))));
				}
			}

			//

			if (material.getProperties().hasWire()) {
				String suffixLower = "wire";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper, new ModResourceLocation(ModReference.Version.getModId(), suffixLower))));

				blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON("wiremodel/" + nameUpper)));
//				blockModels.add(new Tuple<String, String>(nameUpper, "{\n" + "	\"parent\": \"wiptech:block/" + material.getNameLowercase() + "_" + suffixLower + "_core\",\n" + "}\n" + ""));
				blockModels.add(new Tuple<String, String>(nameUpper + "_core", generateBlockItemModelJSON(material, suffixLower + "_core")));

				for (EnumFacing facing : EnumFacing.VALUES) {
//					String name = (nameUpper + "_" + facing.name()).toLowerCase();

					blockModels.add(new Tuple<String, String>(nameUpper + "_extension_" + facing.name().toLowerCase(), generateBlockItemModelJSON(material, suffixLower + "_extension_" + facing.name()
							.toLowerCase())));
				}
			}

			//

			if (material.getProperties().hasEnamel()) {
				String suffixLower = "enamel";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper, new ModResourceLocation(ModReference.Version.getModId(), suffixLower))));

				blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON("wiremodel/" + nameUpper)));
//				blockModels.add(new Tuple<String, String>(nameUpper, "{\n" + "	\"parent\": \"wiptech:block/" + material.getNameLowercase() + "_" + suffixLower + "_core\",\n" + "}\n" + ""));
				blockModels.add(new Tuple<String, String>(nameUpper + "_core", generateBlockItemModelJSON(material, suffixLower + "_core")));

				for (EnumFacing facing : EnumFacing.VALUES) {
//					String name = (nameUpper + "_" + facing.name()).toLowerCase();

					blockModels.add(new Tuple<String, String>(nameUpper + "_extension_" + facing.name().toLowerCase(), generateBlockItemModelJSON(material, suffixLower + "_extension_" + facing.name()
							.toLowerCase())));
				}

			}

			if (material.getProperties().hasRailgunSlug()) {
				String suffixLower = "slug";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();
				itemModels.add(new Tuple<String, String>(nameUpper,

				/* @formatter:off */
						"{\n" + "	\"parent\": \"" + ModReference.Version.getModId() + ":entity/" + suffixLower + "\",\n" + "	\"textures\": {\n" + "		\"ingot\": \"" + material.getResouceLocationDomain("ingot",
								ForgeRegistries.ITEMS) + ":item" + (material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS) != "minecraft" || Loader.MC_VERSION.contains("1.13") ? "" : "s") + "/" + material
										.getNameLowercase() + "_ingot\"\n" + "	}\n" + "}\n"
						/* @formatter:on */

				));
			}

			//

			if (material.getProperties().hasCoil()) {
				String suffixLower = "coil";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper)));

				suffixLower = "spool";
				nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

//				/* @formatter:off */
//				blockstates.add(new Tuple<String, String>(nameUpper,
//						"{\n" + "    \"variants\": {\n" 
//								+ "        \"facing=north\": { \"model\": \"" + ModReference.Version.getModId() + ":" + nameUpper.toLowerCase() + "\" },\n"
//								+ "        \"facing=south\": { \"model\": \"" + ModReference.Version.getModId() + ":" + nameUpper.toLowerCase() + "\", \"y\": 180 },\n"
//								+ "        \"facing=west\":  { \"model\": \"" + ModReference.Version.getModId() + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 },\n"
//								+ "        \"facing=east\":  { \"model\": \"" + ModReference.Version.getModId() + ":" + nameUpper.toLowerCase() + "\", \"y\": 90 }\n" 
//						+ "    }\n" + "}\n"));
//				/* @formatter:on */
				blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON(nameUpper)));
				blockModels.add(new Tuple<String, String>(nameUpper, generateBlockItemModelJSON(material, suffixLower)));
			}

			//

			if (material.getProperties().hasRail()) {
				String suffixLower = "rail";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper)));
			}

		}

		if (lang) {

			WIPTech.info("Initialising recipes");

			ArrayList<Tuple<String, String>> recipesObj = new ArrayList<Tuple<String, String>>();

			for (ModMaterials material : ModMaterials.values()) {

				if (material.getOre() != null)
					GameRegistry.addSmelting(new ItemStack(material.getOre()), new ItemStack(material.getIngot()), 1);

				if (material.getBlock() != null && !material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS).equals("minecraft"))
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getBlock().getRegistryName().getResourcePath(), "{\n" + 
							"	\"type\": \"minecraft:crafting_shaped\",\n" + 
							"	\"pattern\": [\n" + 
							"		\"###\",\n" + 
							"		\"###\",\n" + 
							"		\"###\"\n" + 
							"	],\n" + 
							"	\"key\": {\n" + 
							"		\"#\": {\n" + 
							"			\"item\": \""+material.getIngot().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getBlock().getRegistryName().toString()+"\"\n" + 
							"	}\n" + 
							"}"));
					/*@formatter:on*/

				BlockItem ingot = material.getIngot();

				if (material.getIngot() != null && !material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS).equals("minecraft")) {
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getIngot().getRegistryName().getResourcePath() + "_from_block", "{\n" + 
							"	\"type\": \"minecraft:crafting_shaped\",\n" + 
							"	\"group\": \""+material.getIngot().getRegistryName().getResourcePath()+"\",\n" + 
							"	\"pattern\": [\n" + 
							"		\"#\"\n" + 
							"	],\n" + 
							"	\"key\": {\n" + 
							"		\"#\": {\n" + 
							"			\"item\": \""+material.getBlock().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getIngot().getRegistryName().toString()+"\",\n" + 
							"		\"count\": 9\n" + 
							"	}\n" + 
							"}"));
					/*@formatter:on*/
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getIngot().getRegistryName().getResourcePath() + "_from_nuggets", "{\n" + 
							"	\"type\": \"minecraft:crafting_shaped\",\n" +	
							"	\"group\": \""+material.getIngot().getRegistryName().getResourcePath()+"\",\n" + 
							"	\"pattern\": [\n" + 
							"		\"###\",\n" + 
							"		\"###\",\n" + 
							"		\"###\"\n" + 
							"	],\n" + 
							"	\"key\": {\n" + 
							"		\"#\": {\n" + 
							"			\"item\": \""+material.getNugget().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getIngot().getRegistryName().toString()+"\",\n" + 
							"		\"count\": 1\n" +
							"	}\n" + 
							"}"));
					/*@formatter:on*/
				}

				if (material.getNugget() != null && !material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS).equals("minecraft"))
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getNugget().getRegistryName().getResourcePath(), "{\n" + 
							"	\"type\": \"minecraft:crafting_shaped\",\n" +	
							"	\"pattern\": [\n" + 
							"		\"#\"\n" + 
							"	],\n" + 
							"	\"key\": {\n" + 
							"		\"#\": {\n" + 
							" 			\"item\": \""+material.getIngot().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getNugget().getRegistryName().toString()+"\",\n" + 
							"		\"count\": 9\n" +
							"	}\n" + 
							"}"));
					/*@formatter:on*/

				// armor

				if (material.getHelmet() != null && !material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS).equals("minecraft"))
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getHelmet().getRegistryName().getResourcePath(), "{\n" + 
							"	\"type\": \"minecraft:crafting_shaped\",\n" +	
							"	\"pattern\": [\n" + 
							"		\"XXX\",\n" + 
							"		\"X X\"\n" + 
							"	],\n" + 
							"	\"key\": {\n" + 
							"		\"X\": {\n" + 
							" 			\"item\": \""+material.getIngot().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getHelmet().getRegistryName().toString()+"\",\n" + 
							"		\"count\": 1\n" +
							"	}\n" + 
							"}"));
					/*@formatter:on*/

				if (material.getChestplate() != null && !material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS).equals("minecraft"))
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getChestplate().getRegistryName().getResourcePath(), "{\n" + 
							"	\"type\": \"minecraft:crafting_shaped\",\n" +	
							"	\"pattern\": [\n" + 
							"		\"X X\",\n" + 
							"		\"XXX\",\n" + 
							"		\"XXX\"\n" + 
							"	],\n" + 
							"	\"key\": {\n" + 
							"		\"X\": {\n" + 
							" 			\"item\": \""+material.getIngot().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getChestplate().getRegistryName().toString()+"\",\n" + 
							"		\"count\": 1\n" +
							"	}\n" + 
							"}"));
					/*@formatter:on*/

				if (material.getLeggings() != null && !material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS).equals("minecraft"))
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getLeggings().getRegistryName().getResourcePath(), "{\n" + 
							"	\"type\": \"minecraft:crafting_shaped\",\n" +	
							"	\"pattern\": [\n" + 
							"		\"XXX\",\n" + 
							"		\"X X\",\n" + 
							"		\"X X\"\n" + 
							"	],\n" + 
							"	\"key\": {\n" + 
							"		\"X\": {\n" + 
							" 			\"item\": \""+material.getIngot().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getLeggings().getRegistryName().toString()+"\",\n" + 
							"		\"count\": 1\n" +
							"	}\n" + 
							"}"));
					/*@formatter:on*/

				if (material.getBoots() != null && !material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS).equals("minecraft"))
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getBoots().getRegistryName().getResourcePath(), "{\n" + 
							"	\"type\": \"minecraft:crafting_shaped\",\n" +	
							"	\"pattern\": [\n" + 
							"		\"X X\",\n" + 
							"		\"X X\"\n" + 
							"	],\n" + 
							"	\"key\": {\n" + 
							"		\"X\": {\n" + 
							" 			\"item\": \""+material.getIngot().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getBoots().getRegistryName().toString()+"\",\n" + 
							"		\"count\": 1\n" +
							"	}\n" + 
							"}"));
					/*@formatter:on*/

				// tools

				if (material.getAxe() != null && !material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS).equals("minecraft"))
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getAxe().getRegistryName().getResourcePath(), "{\n" + 
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
							" 			\"item\": \""+material.getIngot().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getAxe().getRegistryName().toString()+"\",\n" + 
							"		\"count\": 1\n" +
							"	}\n" + 
							"}"));
					/*@formatter:on*/

				if (material.getPickaxe() != null && !material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS).equals("minecraft"))
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getPickaxe().getRegistryName().getResourcePath(), "{\n" + 
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
							" 			\"item\": \""+material.getIngot().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getPickaxe().getRegistryName().toString()+"\",\n" + 
							"		\"count\": 1\n" +
							"	}\n" + 
							"}"));
					/*@formatter:on*/

				if (material.getSword() != null && !material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS).equals("minecraft"))
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getSword().getRegistryName().getResourcePath(), "{\n" + 
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
							" 			\"item\": \""+material.getIngot().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getSword().getRegistryName().toString()+"\",\n" + 
							"		\"count\": 1\n" +
							"	}\n" + 
							"}"));
					/*@formatter:on*/

				if (material.getShovel() != null && !material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS).equals("minecraft"))
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getShovel().getRegistryName().getResourcePath(), "{\n" + 
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
							" 			\"item\": \""+material.getIngot().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getShovel().getRegistryName().toString()+"\",\n" + 
							"		\"count\": 1\n" +
							"	}\n" + 
							"}"));
					/*@formatter:on*/

				if (material.getHoe() != null && !material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS).equals("minecraft"))
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getHoe().getRegistryName().getResourcePath(), "{\n" + 
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
							" 			\"item\": \""+material.getIngot().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getHoe().getRegistryName().toString()+"\",\n" + 
							"		\"count\": 1\n" +
							"	}\n" + 
							"}"));
					/*@formatter:on*/

				//

//				if (material.getWire() != null)
//					/*@formatter:off*/
//					recipesObj.add(new Tuple<String, String>(material.getWire().getRegistryName().getResourcePath(), "{\n" + 
//							"	\"type\": \"minecraft:crafting_shaped\",\n" + 
//							"	\"pattern\": [\n" + 
//							"		\"##\"\n" + 
//							"	],\n" + 
//							"	\"key\": {\n" + 
//							"		\"#\": {\n" + 
//							"			\"item\": \""+material.getIngot().getRegistryName().toString()+"\"\n" + 
//							"		}\n" + 
//							"	},\n" + 
//							"	\"result\": {\n" + 
//							"		\"item\": \""+material.getWire().getRegistryName().toString()+"\"\n" + 
//							"	}\n" + 
//							"}"));
//					/*@formatter:on*/

				if (material.getEnamel() != null)
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getEnamel().getRegistryName().getResourcePath(), "{\n" + 
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

				if (material.getRail() != null)
					;

				if (material.getCoil() != null)
					;

				if (material.getSpool() != null)
					;

				if (material.getSlugItem() != null)
					/*@formatter:off*/
					recipesObj.add(new Tuple<String, String>(material.getSlugItem().getRegistryName().getResourcePath(), "{\n" + 
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
							" 			\"item\": \""+material.getIngot().getRegistryName().toString()+"\"\n" + 
							"		}\n" + 
							"	},\n" + 
							"	\"result\": {\n" + 
							"		\"item\": \""+material.getSlugItem().getRegistryName().toString()+"\",\n" + 
							"		\"count\": 1\n" +
							"	}\n" + 
							"}"));
					/*@formatter:on*/

			}

			WIPTech.info("Writing recipes");
			for (Tuple<String, String> recipe : recipesObj) {
				String name = recipe.getFirst();
				List<String> data = new ArrayList<String>();
				data.add(recipe.getSecond());
				// TODO dataDir for 1.13
				Path file = Paths.get(assetDir + "recipes/" + name.toLowerCase() + ".json");
				try {
					Files.write(file, data, Charset.forName("UTF-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		if (lang) {

			WIPTech.info("Initialising lang");

			ArrayList<Tuple<String, String>> langObj = new ArrayList<Tuple<String, String>>();

			for (ModMaterials material : ModMaterials.values()) {

				if (material.getOre() != null)
					langObj.add(new Tuple<String, String>(material.getOre().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Ore"));

				if (material.getBlock() != null)
					langObj.add(new Tuple<String, String>(material.getBlock().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Block"));

				if (material.getIngot() != null)
					langObj.add(new Tuple<String, String>(material.getIngot().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Ingot"));

				if (material.getNugget() != null)
					langObj.add(new Tuple<String, String>(material.getNugget().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Nugget"));

				// armor

				if (material.getHelmet() != null)
					langObj.add(new Tuple<String, String>(material.getHelmet().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Helmet"));

				if (material.getChestplate() != null)
					langObj.add(new Tuple<String, String>(material.getChestplate().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Chestplate"));

				if (material.getLeggings() != null)
					langObj.add(new Tuple<String, String>(material.getLeggings().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Leggings"));

				if (material.getBoots() != null)
					langObj.add(new Tuple<String, String>(material.getBoots().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Boots"));

				// tools

				if (material.getAxe() != null)
					langObj.add(new Tuple<String, String>(material.getAxe().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Axe"));

				if (material.getPickaxe() != null)
					langObj.add(new Tuple<String, String>(material.getPickaxe().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Pickaxe"));

				if (material.getSword() != null)
					langObj.add(new Tuple<String, String>(material.getSword().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Sword"));

				if (material.getShovel() != null)
					langObj.add(new Tuple<String, String>(material.getShovel().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Shovel"));

				if (material.getHoe() != null)
					langObj.add(new Tuple<String, String>(material.getHoe().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Hoe"));

				//

				if (material.getWire() != null)
					langObj.add(new Tuple<String, String>(material.getWire().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Wire"));

				if (material.getEnamel() != null)
					langObj.add(new Tuple<String, String>(material.getEnamel().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Enamel"));

				if (material.getRail() != null)
					langObj.add(new Tuple<String, String>(material.getRail().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Rail"));

				if (material.getCoil() != null)
					langObj.add(new Tuple<String, String>(material.getCoil().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Coil"));

				if (material.getSpool() != null)
					langObj.add(new Tuple<String, String>(material.getSpool().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Spool"));

				if (material.getSlugItem() != null)
					langObj.add(new Tuple<String, String>(material.getSlugItem().getUnlocalizedName(), ModUtil.getLocalisedName(material.getNameLowercase()) + " Slug"));

			}

			for (Field field : ModItems.class.getFields()) {
				Object value;
				try {
					value = field.get(ModItems.class);

					if (!(value instanceof Item))
						continue;

					Item item = (Item) value;

					if (item != null)
						langObj.add(new Tuple<String, String>(item.getUnlocalizedName(), ModUtil.getLocalisedName(item.getRegistryName().getResourcePath())));

				} catch (IllegalArgumentException | IllegalAccessException e) {
					WIPTech.error("fucking hell jeff...");
					e.printStackTrace();
				}
			}

			for (Field field : ModBlocks.class.getFields()) {
				Object value;
				try {
					value = field.get(ModItems.class);

					if (!(value instanceof Block))
						continue;

					Block block = (Block) value;

					if (block != null)
						langObj.add(new Tuple<String, String>(block.getUnlocalizedName(), ModUtil.getLocalisedName(block.getRegistryName().getResourcePath())));

				} catch (IllegalArgumentException | IllegalAccessException e) {
					WIPTech.error("fucking hell derek...");
					e.printStackTrace();
				}
			}

			List<String> data = new ArrayList<String>();

			for (Tuple<String, String> langEntry : langObj) {
				String unlocalised = langEntry.getFirst();
				String name = langEntry.getSecond();

				if (Loader.MC_VERSION.contains("1.13"))
					data.add("\"" + unlocalised + ".name" + "\"" + ": " + "\"" + name + "\"" + ",");
				else
					data.add(unlocalised + ".name" + "=" + name);
			}

			if (Loader.MC_VERSION.contains("1.13")) {
				String removeComma = data.get(data.size() - 1);
				removeComma = removeComma.substring(0, removeComma.length() - 2);
				data.set(data.size() - 1, removeComma);
			}

			ArrayList<String> finalData = new ArrayList<String>();
			if (Loader.MC_VERSION.contains("1.13"))
				finalData.add("{");

			if (Loader.MC_VERSION.contains("1.13"))
				finalData.add("\"itemGroup." + ModReference.Version.getModId() + "\"" + ": " + "\"" + ModReference.NAME + "\"" + ",");
			else
				finalData.add("itemGroup." + ModReference.Version.getModId() + "=" + ModReference.NAME);

			finalData.addAll(data);
			if (Loader.MC_VERSION.contains("1.13"))
				finalData.add("}");

			Path file = Paths.get(assetDir + "lang/en_us." + (Loader.MC_VERSION.contains("1.13") ? "json" : "lang"));
			WIPTech.info("Writing lang");
			try {
				Files.write(file, finalData, Charset.forName("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		if (json) {

			WIPTech.info("Writing blockstates");
			for (Tuple<String, String> state : blockstates) {
				String name = state.getFirst();
				List<String> data = new ArrayList<String>();
				data.add(state.getSecond());
				Path file = Paths.get(assetDir + "blockstates/" + name.toLowerCase() + ".json");
				try {
					Files.write(file, data, Charset.forName("UTF-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			WIPTech.info("Writing blockModels");
			for (Tuple<String, String> blockModel : blockModels) {
				String name = blockModel.getFirst();
				List<String> data = new ArrayList<String>();
				data.add(blockModel.getSecond());
				Path file = Paths.get(assetDir + "models/block/" + name.toLowerCase() + ".json");
				try {
					Files.write(file, data, Charset.forName("UTF-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			WIPTech.info("Writing itemModels");
			for (Tuple<String, String> itemModel : itemModels) {
				String name = itemModel.getFirst();
				List<String> data = new ArrayList<String>();
				data.add(itemModel.getSecond());
				Path file = Paths.get(assetDir + "models/item/" + name.toLowerCase() + ".json");
				try {
					Files.write(file, data, Charset.forName("UTF-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	private static final String generateItemModelJSON(String name, ModResourceLocation parent) {

		/* @formatter:off */
		String ret = "{" + "\n\t" + "\"parent\": \"" + parent.toString() + "\"," + "\n\t" + "\"textures\": {" + "\n\t\t" + "\"layer0\": \"" + ModReference.Version.getModId() + ":item/" + name.toLowerCase()
				+ "\"" + "\n\t" + "}" + "\n" + "}" + "\n";
		/* @formatter:on */
		return ret;

	}

	private static final String generateItemModelJSON(String name) {
		return generateItemModelJSON(name, new ModResourceLocation("", "item/generated"));
	}

	private static final String generateBlockModelJSON(ModResourceLocation fullParentPath, String textureName, String texture) {

		/* @formatter:off */
		String ret = "{" + "\n\t" + "\"parent\": \"" + fullParentPath.toString() + "\"," + "\n\t" + "\"textures\": {" + "\n\t\t" + "\"" + textureName.toLowerCase() + "\": \"" + ModReference.Version.getModId()
				+ ":block/" + texture.toLowerCase() + "\"" + "\n\t" + "}" + "\n" + "}\n";
		/* @formatter:on */
		return ret;

	}

	private static final String generateBlockItemModelJSON(ModMaterials material, String suffixLower) {

		/* @formatter:off */
		String ret = "{\n" + "	\"parent\": \"" + ModReference.Version.getModId() + ":block/" + suffixLower + "\",\n" + "	\"textures\": {\n" + "		\"ingot\": \"" + material.getResouceLocationDomain("ingot",
				ForgeRegistries.ITEMS) + ":item" + (material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS) != "minecraft" || Loader.MC_VERSION.contains("1.13") ? "" : "s") + "/" + material
						.getNameLowercase() + "_ingot\"\n" + "	}\n" + "}\n";
		/* @formatter:on */
		return ret;

	}

	private static final String generateBlockstateJSON(String name) {

		/* @formatter:off */
		String ret = "{" + "\n\t" + "\"variants\": {" + "\n\t\t" + "\"" + default_variant_name + "\": {" + "\n\t\t\t" + "\"model\": \"" + ModReference.Version.getModId() + ":" + name.toLowerCase() + "\"" + "\n\t\t"
				+ "}" + "\n\t" + "}\n" + "}\n";
		/* @formatter:on */
		return ret;

	}

}
