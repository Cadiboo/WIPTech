package cadiboo.wiptech.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModWritingUtil {

	public static String default_variant_name = "normal";
	private static String assetDir = "/Users/" + System.getProperty("user.name") + "/Developer/Modding/WIPTechAlpha/src/main/resources/assets/wiptech/";
	public static boolean debugOres = true;

	public static void writeMod() {

		boolean lang = true;
		boolean json = true;

		try {
			Generator.genVanillaBlockState("/Users/" + System.getProperty("user.name") + "/Developer/Modding/WIPTechAlpha/src/main/resources", ModReference.ID, "testWire", new BlockWire(
					ModMaterials.COPPER));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		WIPTech.info("infoModMaterialsCode with options write lang: " + lang + ", write json: " + json);

		//

		//

		ArrayList<Tuple<String, String>> blockstates = new ArrayList<Tuple<String, String>>();
		ArrayList<Tuple<String, String>> blockModels = new ArrayList<Tuple<String, String>>();
		ArrayList<Tuple<String, String>> itemModels = new ArrayList<Tuple<String, String>>();

		//

		//

		for (ModMaterials material : ModMaterials.values()) {

			if (material.getProperties().hasOre()) {
				String suffixLower = "ore";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				if (material.getResouceLocationDomain("ore", ForgeRegistries.BLOCKS) != "minecraft")
					blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON(nameUpper)));

				if (material.getResouceLocationDomain("ore", ForgeRegistries.BLOCKS) != "minecraft")
					blockModels.add(new Tuple<String, String>(nameUpper, generateBlockModelJSON(ModReference.ID + ":block/ore", suffixLower, nameUpper)));

			}

			//

			if (material.getProperties().hasBlock()) {
				String suffixLower = "block";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				if (material.getResouceLocationDomain("block", ForgeRegistries.BLOCKS) != "minecraft")
					blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON(nameUpper)));

				if (material.getResouceLocationDomain("block", ForgeRegistries.BLOCKS) != "minecraft")
					blockModels.add(new Tuple<String, String>(nameUpper, generateBlockModelJSON("block/cube_all", "all", nameUpper)));
			}

			//

			if (material.getProperties().hasIngotAndNugget()) {
				String suffixLower = "ingot";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				/* @formatter:off */
				blockstates.add(new Tuple<String, String>(nameUpper, "{\n" + "    \"variants\": {\n" + "        \"facing=north\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase()
						+ "\", \"y\": 0 },\n" + "        \"facing=south\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 180 },\n"
						+ "        \"facing=west\":  { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 },\n" + "        \"facing=east\":  { \"model\": \""
						+ ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 90 }\n" + "    }\n" + "}\n"));
				/* @formatter:on */
				blockModels.add(new Tuple<String, String>(nameUpper, generateBlockItemModelJSON(material, suffixLower)));

				if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft")
					itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper)));

				suffixLower = "nugget";
				nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				if (material == ModMaterials.GOLD) /* gold is sideways */
					/* @formatter:off */
					blockstates.add(new Tuple<String, String>(nameUpper, "{\n" + "    \"variants\": {\n" + "        \"facing=north\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase()
							+ "\", \"y\": 180},\n" + "        \"facing=south\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 0 },\n"
							+ "        \"facing=west\":  { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 90 },\n" + "        \"facing=east\":  { \"model\": \""
							+ ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 }\n" + "    }\n" + "}\n"));
				/* @formatter:on */
				else
					/* @formatter:off */
					blockstates.add(new Tuple<String, String>(nameUpper, "{\n" + "    \"variants\": {\n" + "        \"facing=north\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase()
							+ "\", \"y\": 90 },\n" + "        \"facing=south\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 },\n"
							+ "        \"facing=west\":  { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 0 },\n" + "        \"facing=east\":  { \"model\": \""
							+ ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 180 }\n" + "    }\n" + "}\n"));
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
						itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper, "handheld")));
				}
			}

			//

			if (material.getProperties().hasWire()) {
				String suffixLower = "wire";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON("wiremodel/" + nameUpper)));
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

				blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON("wiremodel/" + nameUpper)));
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
						"{\n" + "	\"parent\": \"" + ModReference.ID + ":entity/" + suffixLower + "\",\n" + "	\"textures\": {\n" + "		\"ingot\": \"" + material.getResouceLocationDomain("ingot",
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
//								+ "        \"facing=north\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\" },\n"
//								+ "        \"facing=south\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 180 },\n"
//								+ "        \"facing=west\":  { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 },\n"
//								+ "        \"facing=east\":  { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 90 }\n" 
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

	public static final String generateItemModelJSON(String name, String parent) {

		/* @formatter:off */
		String ret = "{" + "\n\t" + "\"parent\": \"item/" + parent.toLowerCase() + "\"," + "\n\t" + "\"textures\": {" + "\n\t\t" + "\"layer0\": \"" + ModReference.ID + ":item/" + name.toLowerCase()
				+ "\"" + "\n\t" + "}" + "\n" + "}" + "\n";
		/* @formatter:on */
		return ret;

	}

	public static final String generateItemModelJSON(String name) {
		return generateItemModelJSON(name, "generated");
	}

	public static final String generateBlockModelJSON(String fullParentPath, String textureName, String texture) {

		/* @formatter:off */
		String ret = "{" + "\n\t" + "\"parent\": \"" + fullParentPath.toLowerCase() + "\"," + "\n\t" + "\"textures\": {" + "\n\t\t" + "\"" + textureName.toLowerCase() + "\": \"" + ModReference.ID
				+ ":block/" + texture.toLowerCase() + "\"" + "\n\t" + "}" + "\n" + "}\n";
		/* @formatter:on */
		return ret;

	}

	public static final String generateBlockItemModelJSON(ModMaterials material, String suffixLower) {

		/* @formatter:off */
		String ret = "{\n" + "	\"parent\": \"" + ModReference.ID + ":block/" + suffixLower + "\",\n" + "	\"textures\": {\n" + "		\"ingot\": \"" + material.getResouceLocationDomain("ingot",
				ForgeRegistries.ITEMS) + ":item" + (material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS) != "minecraft" || Loader.MC_VERSION.contains("1.13") ? "" : "s") + "/" + material
						.getNameLowercase() + "_ingot\"\n" + "	}\n" + "}\n";
		/* @formatter:on */
		return ret;

	}

	public static final String generateBlockstateJSON(String name) {

		/* @formatter:off */
		String ret = "{" + "\n\t" + "\"variants\": {" + "\n\t\t" + "\"" + default_variant_name + "\": {" + "\n\t\t\t" + "\"model\": \"" + ModReference.ID + ":" + name.toLowerCase() + "\"" + "\n\t\t"
				+ "}" + "\n\t" + "}\n" + "}\n";
		/* @formatter:on */
		return ret;

	}

}
