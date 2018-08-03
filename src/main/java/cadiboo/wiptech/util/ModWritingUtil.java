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
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModWritingUtil {

	public static String	default_variant_name	= "normal";
	private static String	assetDir				= "/Users/" + System.getProperty("user.name") + "/Developer/Modding/WIPTechAlpha/src/main/resources/assets/wiptech/";

	public static void writeMod() {

		boolean code = true;
		boolean json = true;

//		try {
//			Generator.genVanillaBlockState("/Users/" + System.getProperty("user.name") + "/Developer/Modding/WIPTechAlpha/src/main/resources", ModReference.ID, "testWire", ModBlocks.COPPER_WIRE);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		WIPTech.info("infoModMaterialsCode with options write code: " + code + ", write json: " + json);

		//

		//

		ArrayList<String> modBlocks = new ArrayList<String>();
		ArrayList<String> registerBlocks = new ArrayList<String>();

		ArrayList<String> modItems = new ArrayList<String>();
		ArrayList<String> registerItems = new ArrayList<String>();

		ArrayList<String> registerModels = new ArrayList<String>();

		ArrayList<Tuple<String, String>> blockstates = new ArrayList<Tuple<String, String>>();
		ArrayList<Tuple<String, String>> blockModels = new ArrayList<Tuple<String, String>>();
		ArrayList<Tuple<String, String>> itemModels = new ArrayList<Tuple<String, String>>();

		//

		//

		for (ModMaterials material : ModMaterials.values()) {

			if (material.getProperties().hasOre()) {
				String suffixLower = "ore";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				modBlocks.add("@ObjectHolder(\"" + material.getResouceLocationDomain(suffixLower, ForgeRegistries.BLOCKS) + ":" + nameUpper.toLowerCase() + "\")");
				modBlocks.add("public static final BlockModOre " + nameUpper + " = null;");

				registerBlocks.add("registry.register(new BlockModOre(ModMaterials." + material.getNameUppercase() + "));");
				registerItems.add("registry.register(new ModItemBlock(ModBlocks." + nameUpper
						+ (material.getResouceLocationDomain("ore", ForgeRegistries.BLOCKS) != ModReference.ID
								? ", new ResourceLocation(\"" + material.getResouceLocationDomain("ore", ForgeRegistries.BLOCKS) + "\", \"" + nameUpper.toLowerCase() + "\")"
								: "")
						+ "));");
				if (material.getResouceLocationDomain("ore", ForgeRegistries.BLOCKS) != "minecraft")
					registerModels.add("registerItemBlockModel(ModBlocks." + nameUpper + ");");

				if (material.getResouceLocationDomain("ore", ForgeRegistries.BLOCKS) != "minecraft")
					blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON(nameUpper)));

				if (material.getResouceLocationDomain("ore", ForgeRegistries.BLOCKS) != "minecraft")
					blockModels.add(new Tuple<String, String>(nameUpper, generateBlockModelJSON(ModReference.ID + ":block/ore", suffixLower, nameUpper)));

			}

			//

			if (material.getProperties().hasBlock()) {
				String suffixLower = "block";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				modBlocks.add("@ObjectHolder(\"" + material.getResouceLocationDomain(suffixLower, ForgeRegistries.BLOCKS) + ":" + nameUpper.toLowerCase() + "\")");
				modBlocks.add("public static final BlockResource " + nameUpper + " = null;");

				registerBlocks.add("registry.register(new BlockResource(ModMaterials." + material.getNameUppercase() + "));");
				registerItems.add("registry.register(new ModItemBlock(ModBlocks." + nameUpper
						+ (material.getResouceLocationDomain("block", ForgeRegistries.BLOCKS) != ModReference.ID
								? ", new ResourceLocation(\"" + material.getResouceLocationDomain("block", ForgeRegistries.BLOCKS) + "\", \"" + nameUpper.toLowerCase() + "\")"
								: "")
						+ "));");

				if (material.getResouceLocationDomain("block", ForgeRegistries.BLOCKS) != "minecraft")
					registerModels.add("registerItemBlockModel(ModBlocks." + nameUpper + ");");

				if (material.getResouceLocationDomain("block", ForgeRegistries.BLOCKS) != "minecraft")
					blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON(nameUpper)));

				if (material.getResouceLocationDomain("block", ForgeRegistries.BLOCKS) != "minecraft")
					blockModels.add(new Tuple<String, String>(nameUpper, generateBlockModelJSON("block/cube_all", "all", nameUpper)));
			}

			//

			if (material.getProperties().hasIngotAndNugget()) {
				String suffixLower = "ingot";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				modBlocks.add("@ObjectHolder(\"" + material.getResouceLocationDomain(suffixLower, ForgeRegistries.BLOCKS) + ":" + nameUpper.toLowerCase() + "\")");
				modBlocks.add("public static final BlockItem " + nameUpper + " = null;");

				registerBlocks.add("registry.register(new BlockItem(ModMaterials." + material.getNameUppercase() + ", BlockItemTypes.INGOT));");
				registerItems.add("registry.register(new ModItemBlock(ModBlocks." + nameUpper
						+ (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != ModReference.ID
								? ", new ResourceLocation(\"" + material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) + "\", \"" + nameUpper.toLowerCase() + "\")"
								: "")
						+ "));");

				if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft")
					registerModels.add("registerItemBlockModel(ModBlocks." + nameUpper + ");");

				/* @formatter:off */
				blockstates.add(new Tuple<String, String>(nameUpper,
						"{\n" + "    \"variants\": {\n" 
								+ "        \"facing=north\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 0 },\n"
								+ "        \"facing=south\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 180 },\n"
								+ "        \"facing=west\":  { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 },\n"
								+ "        \"facing=east\":  { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 90 }\n" 
						+ "    }\n" + "}\n"));
				/* @formatter:on */
				blockModels.add(new Tuple<String, String>(nameUpper, generateBlockItemModelJSON(material, suffixLower)));

				if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft")
					itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper)));

				suffixLower = "nugget";
				nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				modBlocks.add("@ObjectHolder(\"" + material.getResouceLocationDomain(suffixLower, ForgeRegistries.BLOCKS) + ":" + nameUpper.toLowerCase() + "\")");
				modBlocks.add("public static final BlockItem " + nameUpper + " = null;");

				registerBlocks.add("registry.register(new BlockItem(ModMaterials." + material.getNameUppercase() + ", BlockItemTypes.NUGGET));");
				registerItems.add("registry.register(new ModItemBlock(ModBlocks." + nameUpper
						+ (material.getResouceLocationDomain("nugget", ForgeRegistries.ITEMS) != ModReference.ID
								? ", new ResourceLocation(\"" + material.getResouceLocationDomain("nugget", ForgeRegistries.ITEMS) + "\", \"" + nameUpper.toLowerCase() + "\")"
								: "")
						+ "));");

				if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft")
					registerModels.add("registerItemBlockModel(ModBlocks." + nameUpper + ");");

				if (material == ModMaterials.GOLD) /* gold is sideways */
					/* @formatter:off */
					blockstates.add(new Tuple<String, String>(nameUpper,
							"{\n" + "    \"variants\": {\n" 
									+ "        \"facing=north\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 180},\n"
									+ "        \"facing=south\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 0 },\n"
									+ "        \"facing=west\":  { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 90 },\n"
									+ "        \"facing=east\":  { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 }\n" 
							+ "    }\n" + "}\n"));
					/* @formatter:on */
				else
					/* @formatter:off */
					blockstates.add(new Tuple<String, String>(nameUpper,
							"{\n" + "    \"variants\": {\n" 
									+ "        \"facing=north\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 90 },\n"
									+ "        \"facing=south\": { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 270 },\n"
									+ "        \"facing=west\":  { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 0 },\n"
									+ "        \"facing=east\":  { \"model\": \"" + ModReference.ID + ":" + nameUpper.toLowerCase() + "\", \"y\": 180 }\n" 
							+ "    }\n" + "}\n"));
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

					modItems.add("@ObjectHolder(\"" + material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) + ":" + material.getVanillaNameLowercase(suffixLower) + "_" + suffixLower
							+ "\")");
					modItems.add("public static final ItemModArmor" + " " + nameUpper + " = null;");

					registerItems.add("registry.register(new ItemModArmor(ModMaterials." + material.getNameUppercase() + ", EntityEquipmentSlot. " + slotName + "));");

					if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft")
						registerModels.add("registerItemModel(ModItems." + nameUpper + ");");

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

					modItems.add("@ObjectHolder(\"" + material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) + ":" + material.getVanillaNameLowercase(suffixLower) + "_" + suffixLower
							+ "\")");
					modItems.add("public static final Item" + itemType + " " + nameUpper + " = null;");

					registerItems.add("registry.register(new Item" + itemType + "(ModMaterials." + material.getNameUppercase() + "));");

					if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft")
						registerModels.add("registerItemModel(ModItems." + nameUpper + ");");

					if (material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) != "minecraft")
						itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper, "handheld")));
				}
			}

			//

			if (material.getProperties().hasWire()) {
				String suffixLower = "wire";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				modBlocks.add("@ObjectHolder(\"" + material.getResouceLocationDomain(suffixLower, ForgeRegistries.BLOCKS) + ":" + nameUpper.toLowerCase() + "\")");
				modBlocks.add("public static final BlockWire " + nameUpper + " = null;");

				registerBlocks.add("registry.register(new BlockWire(ModMaterials." + material.getNameUppercase() + "));");
				registerItems.add("registry.register(new ModItemBlock(ModBlocks." + nameUpper + "));");

				registerModels.add("registerItemBlockModel(ModBlocks." + nameUpper + ");");

				blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON(nameUpper)));
				blockModels.add(new Tuple<String, String>(nameUpper, generateBlockItemModelJSON(material, suffixLower)));

				suffixLower += "_extension";
				nameUpper += "_extension".toUpperCase();

				blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON(nameUpper)));
				blockModels.add(new Tuple<String, String>(nameUpper, generateBlockItemModelJSON(material, suffixLower)));
			}

			//

			if (material.getProperties().hasEnamel()) {
				String suffixLower = "enamel";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				modBlocks.add("@ObjectHolder(\"" + material.getResouceLocationDomain(suffixLower, ForgeRegistries.BLOCKS) + ":" + nameUpper.toLowerCase() + "\")");
				modBlocks.add("public static final BlockEnamel " + nameUpper + " = null;");

				registerBlocks.add("registry.register(new BlockEnamel(ModMaterials." + material.getNameUppercase() + "));");
				registerItems.add("registry.register(new ModItemBlock(ModBlocks." + nameUpper + "));");

				registerModels.add("registerItemBlockModel(ModBlocks." + nameUpper + ");");

				blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON(nameUpper)));
				blockModels.add(new Tuple<String, String>(nameUpper, generateBlockItemModelJSON(material, suffixLower)));

				suffixLower += "_extension";
				nameUpper += "_extension".toUpperCase();

				blockstates.add(new Tuple<String, String>(nameUpper, generateBlockstateJSON(nameUpper)));
				blockModels.add(new Tuple<String, String>(nameUpper, generateBlockItemModelJSON(material, suffixLower)));
			}

			//

			if (material.getProperties().hasCoil()) {
				String suffixLower = "coil";
				String nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				modItems.add("@ObjectHolder(\"" + material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) + ":" + nameUpper.toLowerCase() + "\")");
				modItems.add("public static final ItemCoil" + " " + nameUpper + " = null;");

				registerItems.add("registry.register(new ItemCoil(ModMaterials." + material.getNameUppercase() + "));");
				registerModels.add("registerItemModel(ModItems." + nameUpper + ");");

				itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper)));

				suffixLower = "spool";
				nameUpper = material.getNameUppercase() + "_" + suffixLower.toUpperCase();

				modBlocks.add("@ObjectHolder(\"" + material.getResouceLocationDomain(suffixLower, ForgeRegistries.BLOCKS) + ":" + nameUpper.toLowerCase() + "\")");
				modBlocks.add("public static final BlockSpool " + nameUpper + " = null;");

				registerBlocks.add("registry.register(new BlockSpool(ModMaterials." + material.getNameUppercase() + "));");
				registerItems.add("registry.register(new ModItemBlock(ModBlocks." + nameUpper + "));");

				registerModels.add("registerItemBlockModel(ModBlocks." + nameUpper + ");");

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

				modItems.add("@ObjectHolder(\"" + material.getResouceLocationDomain(suffixLower, ForgeRegistries.ITEMS) + ":" + nameUpper.toLowerCase() + "\")");
				modItems.add("public static final ItemRail" + " " + nameUpper + " = null;");
				registerItems.add("registry.register(new ItemRail(ModMaterials." + material.getNameUppercase() + "));");
				registerModels.add("registerItemModel(ModItems." + nameUpper + ");");
				itemModels.add(new Tuple<String, String>(nameUpper, generateItemModelJSON(nameUpper)));
			}

		}

		if (code) {

			WIPTech.info("Writing ModBlocks_autogenerated");
			Path file = Paths.get("/Users/" + System.getProperty("user.name") + "/Desktop/ModBlocks_autogenerated.txt");
			try {
				List<String> data = new ArrayList<String>();
				data.add("public class ModBlocks {\n");
				for (String block : modBlocks)
					data.add("	" + block);
				data.add("\n}");
				Files.write(file, data, Charset.forName("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			WIPTech.info("Writing ModItems_autogenerated");
			file = Paths.get("/Users/" + System.getProperty("user.name") + "/Desktop/ModItems_autogenerated.txt");
			try {
				List<String> data = new ArrayList<String>();
				data.add("public class ModItems {\n");
				for (String item : modItems)
					data.add("	" + item);
				data.add("\n}");
				Files.write(file, data, Charset.forName("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			WIPTech.info("Writing RegisterBlocks_autogenerated");
			file = Paths.get("/Users/" + System.getProperty("user.name") + "/Desktop/RegisterBlocks_autogenerated.txt");
			try {
				List<String> data = new ArrayList<String>();
				data.add("	" + "@SubscribeEvent");
				data.add("	" + "public static void onRegisterBlocksEvent(final RegistryEvent.Register<Block> event) {");
				data.add("		" + "final IForgeRegistry<Block> registry = event.getRegistry();");
				for (String block : registerBlocks)
					data.add("		" + block);
				data.add("	" + "}");
				data.add("registerTileEntity(TileEntityWire.class);");
				data.add("registerTileEntity(TileEntityEnamel.class);");
				Files.write(file, data, Charset.forName("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			WIPTech.info("Writing RegisterItems_autogenerated");
			file = Paths.get("/Users/" + System.getProperty("user.name") + "/Desktop/RegisterItems_autogenerated.txt");
			try {
				List<String> data = new ArrayList<String>();
				data.add("	" + "@SubscribeEvent");
				data.add("	" + "public static void onRegisterItemsEvent(final RegistryEvent.Register<Item> event) {");
				data.add("		" + "final IForgeRegistry<Item> registry = event.getRegistry();");
				for (String item : registerItems)
					data.add("		" + item);
				data.add("	" + "}");
				Files.write(file, data, Charset.forName("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			WIPTech.info("Writing RegisterModels_autogenerated");
			file = Paths.get("/Users/" + System.getProperty("user.name") + "/Desktop/RegisterModels_autogenerated.txt");
			try {
				List<String> data = new ArrayList<String>();
				data.add("	@SubscribeEvent\n" + "	@SideOnly(Side.CLIENT)\n" + "	public static final void onRegisterModelsEvent(final ModelRegistryEvent event) {\n");

				data.add("\n	}");
				data.add("	" + "@SubscribeEvent");
				data.add("	" + "@SideOnly(Side.CLIENT)");
				data.add("	" + "public static final void onRegisterModelsEvent(final ModelRegistryEvent event) {");
				for (String model : registerModels)
					data.add("		" + model);
				data.add("	" + "}");
				Files.write(file, data, Charset.forName("UTF-8"));
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

	public static final String generateItemModelJSON(String name, String parent) {

		/*@formatter:off*/
		String ret = "{" + "\n\t" + 
					"\"parent\": \"item/" + parent.toLowerCase() + "\"," + "\n\t" + 
					"\"textures\": {" + "\n\t\t" + 
						"\"layer0\": \"" + ModReference.ID + ":item/" + name.toLowerCase() + "\"" + "\n\t" + 
					"}" + "\n" +
				"}" + "\n";
		/*@formatter:on*/
		return ret;

	}

	public static final String generateItemModelJSON(String name) {
		return generateItemModelJSON(name, "generated");
	}

	public static final String generateBlockModelJSON(String fullParentPath, String textureName, String texture) {

		/*@formatter:off*/
		String ret = "{" + "\n\t" + 
					"\"parent\": \"" + fullParentPath.toLowerCase() + "\"," + "\n\t" + 
					"\"textures\": {" + "\n\t\t" + 
							"\""+textureName.toLowerCase()+"\": \"" + ModReference.ID + ":block/" + texture.toLowerCase() + "\"" + "\n\t" + 
					"}" + "\n" +
				"}\n";
		/*@formatter:on*/
		return ret;

	}

	public static final String generateBlockItemModelJSON(ModMaterials material, String suffixLower) {

		/*@formatter:off*/
		String ret = "{\n" + 
				"	\"parent\": \""+ModReference.ID+":block/"+suffixLower+"\",\n" + 
				"	\"textures\": {\n" + 
				"		\"ingot\": \""+material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS)+":item"+(material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS)!="minecraft" || Loader.MC_VERSION.contains("1.13")?"":"s")+"/"+material.getNameLowercase()+"_ingot\"\n" + 
				"	}\n" + 
				"}\n";
		/*@formatter:on*/
		return ret;

	}

	public static final String generateBlockstateJSON(String name) {

		/*@formatter:off*/
		String ret = "{" + "\n\t" + 
					"\"variants\": {" + "\n\t\t" + 
						"\""+default_variant_name+"\": {" + "\n\t\t\t" + 
								"\"model\": \"" + ModReference.ID + ":" + name.toLowerCase() + "\"" +  "\n\t\t" +
							"}" + "\n\t" +
					"}\n" + 
				"}\n";
		/*@formatter:on*/
		return ret;

	}

}
