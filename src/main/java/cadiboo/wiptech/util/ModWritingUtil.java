package cadiboo.wiptech.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.client.ClientEventSubscriber;
import cadiboo.wiptech.init.ModBlocks;
import cadiboo.wiptech.init.ModItems;
import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
import cadiboo.wiptech.util.ModEnums.CircuitTypes;
import cadiboo.wiptech.util.ModEnums.ModMaterialTypes;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModEnums.ScopeTypes;
import net.minecraft.block.Block;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ExistsForDebugging
@SideOnly(Side.CLIENT)
public class ModWritingUtil {

	private static String	assetDir	= "/Users/" + System.getProperty("user.name") + "/Developer/Modding/WIPTechAlpha/src/main/resources/assets/wiptech/";
	private static String	dataDir		= "/Users/" + System.getProperty("user.name") + "/Developer/Modding/WIPTechAlpha/src/main/resources/data/wiptech/";

	private static final ModResourceLocation	item_generated				= new ModResourceLocation("", "item/generated");
	private static final ModResourceLocation	item_handheld				= new ModResourceLocation("", "item/handheld");
	private static final String					item_default_texture_name	= "layer0";

	@ExistsForDebugging
	@SideOnly(Side.CLIENT)
	public static void writeMod() {

		final boolean recipes = true;
		final boolean lang = true;
		final boolean json = false;

		WIPTech.info("ModWritingUtil.writeMod() with options write recipes: " + recipes + ", write lang: " + lang + ", write json: " + json);

		if (ModReference.Debug.debugAPIMaterials()) {
			WIPTech.info("debugAPIMaterials returned true! to prevent large useless files from being created writeMod() is Aborting!");
			return;
		}

		if (Boolean.valueOf(false).equals(recipes) && Boolean.valueOf(false).equals(recipes) && Boolean.valueOf(false).equals(recipes)) {
			WIPTech.info("All arguments are false for writeMod() - Aborting!");
			return;
		}
		if (recipes) {
			for (final ModMaterials material : ModMaterials.values()) {
				try {
					generateAndWriteRecipes(material);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (lang) {
			try {
				generateAndWriteLang();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		if (json) {
			for (final ModMaterials material : ModMaterials.values()) {
				try {
					generateAndWriteModels(material);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static void generateAndWriteModels(final ModMaterials material) {
		final ModMaterialProperties properties = material.getProperties();

		final HashMap<String, String> blockstates = new HashMap<>();
		final HashMap<String, String> blockModels = new HashMap<>();
		final HashMap<String, String> itemModels = new HashMap<>();

		if (properties.hasOre()) {
			final ModResourceLocation ore = new ModResourceLocation(material.getOre().getRegistryName());

			if (ore.getResourceDomain().equals(ModReference.MOD_ID)) {

				final String path = ore.getResourcePath();
				final ModResourceLocation model = new ModResourceLocation(ModReference.MOD_ID, path);

				blockstates.put(path, generateBlockstateJSON(model));

				final ModResourceLocation parent = new ModResourceLocation(ModReference.MOD_ID, "block/ore");
				final String textureName = "ore";
				final ModResourceLocation textureLocation = getTextureLocation(ore, "block");

				blockModels.put(path, generateModelJSON(parent, textureName, textureLocation));
			}
		}

		if (properties.hasBlock()) {
			final ModResourceLocation block = new ModResourceLocation(material.getBlock().getRegistryName());

			if (block.getResourceDomain().equals(ModReference.MOD_ID)) {

				final String path = block.getResourcePath();
				final ModResourceLocation model = new ModResourceLocation(ModReference.MOD_ID, path);

				blockstates.put(path, generateBlockstateJSON(model));

				final ModResourceLocation parent = new ModResourceLocation("", "block/cube_all");
				final String textureName = "all";
				final ModResourceLocation textureLocation = getTextureLocation(block, "block");

				blockModels.put(path, generateModelJSON(parent, textureName, textureLocation));
			}
		}

		if (properties.hasResource()) {
			final ModResourceLocation resource = new ModResourceLocation(Item.getItemFromBlock(material.getResource()).getRegistryName());

			final String path = resource.getResourcePath();
			final ModResourceLocation model = new ModResourceLocation(ModReference.MOD_ID, path);

			blockstates.put(path, generateBlockstateJSON(model, EnumFacing.HORIZONTALS));

			final String resourceNameSuffix = material.getType().getResourceNameSuffix();

			final ModResourceLocation parent = new ModResourceLocation(ModReference.MOD_ID, "block/" + resourceNameSuffix);
			final String textureName = resourceNameSuffix;
			final ModResourceLocation textureLocation = getTextureLocation(resource, "item");

			blockModels.put(path, generateModelJSON(parent, textureName, textureLocation));

			if (resource.getResourceDomain().equals(ModReference.MOD_ID)) {

				itemModels.put(path, generateModelJSON(item_generated, item_default_texture_name, textureLocation));

			}
		}

		if (properties.hasResource() && material.getType().hasResourcePiece()) {
			ModResourceLocation resourcePiece = new ModResourceLocation(Item.getItemFromBlock(material.getResourcePiece()).getRegistryName());
			final String path = resourcePiece.getResourcePath();

			ModResourceLocation textureLocation = getTextureLocation(resourcePiece, "item");
			if (resourcePiece.getResourceDomain().equals(ModReference.MOD_ID)) {

				itemModels.put(path, generateModelJSON(item_generated, item_default_texture_name, textureLocation));

			}

			// HACK cause im too lazy to make my nuggets models use the nugget textures instead of the currently being used ingot textures
			if (material.getType() == ModMaterialTypes.METAL) {
				resourcePiece = new ModResourceLocation(Item.getItemFromBlock(material.getResource()).getRegistryName());
			}

			String resourcePieceNameSuffix = material.getType().getResourcePieceNameSuffix();
			final ModResourceLocation parent = new ModResourceLocation(ModReference.MOD_ID, "block/" + resourcePieceNameSuffix);
			// HACK cause im too lazy to make my nuggets models use the nugget textures instead of the currently being used ingot textures
			if (material.getType() == ModMaterialTypes.METAL) {
				resourcePieceNameSuffix = "ingot";
			}

			final ModResourceLocation model = new ModResourceLocation(ModReference.MOD_ID, path);

			blockstates.put(path, generateBlockstateJSON(model, true, EnumFacing.HORIZONTALS));

			final String textureName = resourcePieceNameSuffix;
			// HACK cause im too lazy to make my nuggets models use the nugget textures instead of the currently being used ingot textures
			if (material.getType() == ModMaterialTypes.METAL) {
				textureLocation = getTextureLocation(resourcePiece, "item");
			}

			blockModels.put(path, generateModelJSON(parent, textureName, textureLocation));
		}

		if (properties.hasArmor()) {
			for (final Item itemArmor : new Item[] { material.getHelmet(), material.getChestplate(), material.getLeggings(), material.getBoots(), material.getHorseArmor() }) {

				final ModResourceLocation armor = new ModResourceLocation(itemArmor.getRegistryName());

				if (armor.getResourceDomain().equals(ModReference.MOD_ID)) {

					final String path = armor.getResourcePath();
					final ModResourceLocation textureLocation = getTextureLocation(armor, "item");

					itemModels.put(path, generateModelJSON(item_generated, item_default_texture_name, textureLocation));

				}

			}
		}

		if (properties.hasTools()) {
			for (final Item itemTool : new Item[] { material.getPickaxe(), material.getAxe(), material.getSword(), material.getShovel(), material.getHoe() }) {

				final ModResourceLocation tool = new ModResourceLocation(itemTool.getRegistryName());

				if (tool.getResourceDomain().equals(ModReference.MOD_ID)) {

					final String path = tool.getResourcePath();
					final ModResourceLocation textureLocation = getTextureLocation(tool, "item");

					itemModels.put(path, generateModelJSON(item_handheld, item_default_texture_name, textureLocation));

				}

			}
		}

		if (properties.hasWire()) {
			final ModResourceLocation wire = new ModResourceLocation(material.getWire().getRegistryName());

			final String path = wire.getResourcePath();
			final ModResourceLocation model = new ModResourceLocation(ModReference.MOD_ID, "builtin/wire/" + path);

			blockstates.put(path, generateBlockstateJSON(model));

			final ModResourceLocation resource = new ModResourceLocation(Item.getItemFromBlock(material.getResource()).getRegistryName());
			final ModResourceLocation textureLocation = getTextureLocation(resource, "item");
			final String textureName = "resource";

			for (final EnumFacing facing : EnumFacing.VALUES) {

				final String extension_FACING = "extension_" + facing.name().toLowerCase();

				final ModResourceLocation parent = new ModResourceLocation(ModReference.MOD_ID, "block/wire_" + extension_FACING);

				blockModels.put(path + "_" + extension_FACING, generateModelJSON(parent, textureName, textureLocation));
			}

			final ModResourceLocation parent = new ModResourceLocation(ModReference.MOD_ID, "block/wire_core");

			blockModels.put(path + "_core", generateModelJSON(parent, textureName, textureLocation));
		}

		if (properties.hasEnamel()) {
			final ModResourceLocation enamel = new ModResourceLocation(material.getEnamel().getRegistryName());

			final String path = enamel.getResourcePath();
			final ModResourceLocation model = new ModResourceLocation(ModReference.MOD_ID, "builtin/wire/" + path);

			blockstates.put(path, generateBlockstateJSON(model));

			final ModResourceLocation resource = new ModResourceLocation(Item.getItemFromBlock(material.getResource()).getRegistryName());
			final ModResourceLocation textureLocation = getTextureLocation(resource, "item");
			final String textureName = "resource";

			for (final EnumFacing facing : EnumFacing.VALUES) {

				final String extension_FACING = "extension_" + facing.name().toLowerCase();

				final ModResourceLocation parent = new ModResourceLocation(ModReference.MOD_ID, "block/enamel_" + extension_FACING);

				blockModels.put(path + "_" + extension_FACING, generateModelJSON(parent, textureName, textureLocation));
			}

			final ModResourceLocation parent = new ModResourceLocation(ModReference.MOD_ID, "block/enamel_core");

			blockModels.put(path + "_core", generateModelJSON(parent, textureName, textureLocation));
		}

		if (properties.hasCoil()) {

			final ModResourceLocation coil = new ModResourceLocation(material.getCoil().getRegistryName());

			final String path = coil.getResourcePath();
			final ModResourceLocation textureLocation = getTextureLocation(coil, "item");

			itemModels.put(path, generateModelJSON(item_generated, item_default_texture_name, textureLocation));

			final ModResourceLocation resource = new ModResourceLocation(Item.getItemFromBlock(material.getResource()).getRegistryName());

			final ModResourceLocation spool = new ModResourceLocation(material.getSpool().getRegistryName());

			final String spoolPath = spool.getResourcePath();

			final ModResourceLocation model = new ModResourceLocation(ModReference.MOD_ID, spoolPath);
			blockstates.put(spoolPath, generateBlockstateJSON(model));

			final ModResourceLocation spoolTextureLocation = getTextureLocation(resource, "item");
			final ModResourceLocation parent = new ModResourceLocation(ModReference.MOD_ID, "block/spool");
			final String textureName = "resource";

			blockModels.put(spoolPath, generateModelJSON(parent, textureName, spoolTextureLocation));

		}

		if (properties.hasRail()) {

			final ModResourceLocation rail = new ModResourceLocation(material.getRail().getRegistryName());

			final String path = rail.getResourcePath();
			final ModResourceLocation textureLocation = getTextureLocation(rail, "item");

			itemModels.put(path, generateModelJSON(item_generated, item_default_texture_name, textureLocation));

		}

		if (properties.hasRailgunSlug()) {

			final ModResourceLocation slug = new ModResourceLocation(material.getSlugItem().getRegistryName());

			final String path = slug.getResourcePath();

			final ModResourceLocation resource = new ModResourceLocation(Item.getItemFromBlock(material.getResource()).getRegistryName());

			final ModResourceLocation parent = new ModResourceLocation(ModReference.MOD_ID, "item/slug");
			final ModResourceLocation textureLocation = getTextureLocation(resource, "item");

			itemModels.put(path, generateModelJSON(parent, "resource", textureLocation));

		}

		//

		//

		//

		WIPTech.debug("Writing blockstates for " + material);
		blockstates.forEach((name, state) -> {
			final ArrayList data = new ArrayList<>(Arrays.asList(state.split("\n")));
			final Iterator<String> it = data.iterator();
			while (it.hasNext()) {
				if (it.next().equals("")) {
					it.remove();
				}
			}

			final Path file = Paths.get(assetDir + "blockstates/" + name.toLowerCase() + ".json");
			try {
				Files.write(file, data, Charset.forName("UTF-8"));
			} catch (final IOException e) {
				e.printStackTrace();
			}

		});

		WIPTech.debug("Writing blockModels for " + material);
		blockModels.forEach((name, model) -> {
			final ArrayList data = new ArrayList<>(Arrays.asList(model.split("\n")));
			final Iterator<String> it = data.iterator();
			while (it.hasNext()) {
				if (it.next().equals("")) {
					it.remove();
				}
			}

			final Path file = Paths.get(assetDir + "models/block/" + name.toLowerCase() + ".json");
			try {
				Files.write(file, data, Charset.forName("UTF-8"));
			} catch (final IOException e) {
				e.printStackTrace();
			}

		});

		WIPTech.debug("Writing itemModels for " + material);
		itemModels.forEach((name, model) -> {
			final ArrayList data = new ArrayList<>(Arrays.asList(model.split("\n")));
			final Iterator<String> it = data.iterator();
			while (it.hasNext()) {
				if (it.next().equals("")) {
					it.remove();
				}
			}

			final Path file = Paths.get(assetDir + "models/item/" + name.toLowerCase() + ".json");
			try {
				Files.write(file, data, Charset.forName("UTF-8"));
			} catch (final IOException e) {
				e.printStackTrace();
			}

		});

	}

	private static ModResourceLocation getTextureLocation(final ModResourceLocation location, final String prepend) {
		final String domain = location.getResourceDomain();
		final String path = location.getResourcePath();

		final String optionalS = domain.equals("minecraft") && !Loader.MC_VERSION.contains("1.13") ? "s" : "";

		final String texturePath = prepend + optionalS + "/" + path;

		final ModResourceLocation textureLocation = new ModResourceLocation(domain, texturePath);
		return textureLocation;
	}

	private static void generateAndWriteLang() {
		WIPTech.debug("Initialising lang");

		final HashMap<String, String> lang = new HashMap<>();

		for (final ModMaterials material : ModMaterials.values()) {

			if (material.getOre() != null) {
				lang.put(material.getOre().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Ore");
			}

			if (material.getBlock() != null) {
				lang.put(material.getBlock().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Block");
			}

			if (material.getResource() != null) {
				lang.put(material.getResource().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " " + StringUtils.capitalize(material.getType().getResourceNameSuffix()));
			}

			if (material.getResourcePiece() != null) {
				lang.put(material.getResourcePiece().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " " + StringUtils.capitalize(material.getType().getResourcePieceNameSuffix()));
			}

			// armor

			if (material.getHelmet() != null) {
				lang.put(material.getHelmet().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Helmet");
			}

			if (material.getChestplate() != null) {
				lang.put(material.getChestplate().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Chestplate");
			}

			if (material.getLeggings() != null) {
				lang.put(material.getLeggings().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Leggings");
			}

			if (material.getBoots() != null) {
				lang.put(material.getBoots().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Boots");
			}

			if (material.getHorseArmor() != null) {
				lang.put(material.getHorseArmor().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Horse Armor");
			}

			// tools

			if (material.getAxe() != null) {
				lang.put(material.getAxe().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Axe");
			}

			if (material.getPickaxe() != null) {
				lang.put(material.getPickaxe().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Pickaxe");
			}

			if (material.getSword() != null) {
				lang.put(material.getSword().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Sword");
			}

			if (material.getShovel() != null) {
				lang.put(material.getShovel().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Shovel");
			}

			if (material.getHoe() != null) {
				lang.put(material.getHoe().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Hoe");
			}

			//

			if (material.getWire() != null) {
				lang.put(material.getWire().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Wire");
			}

			if (material.getEnamel() != null) {
				lang.put(material.getEnamel().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Enamel");
			}

			if (material.getRail() != null) {
				lang.put(material.getRail().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Rail");
			}

			if (material.getCoil() != null) {
				lang.put(material.getCoil().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Coil");
			}

			if (material.getSpool() != null) {
				lang.put(material.getSpool().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Spool");
			}

			if (material.getSlugItem() != null) {
				lang.put(material.getSlugItem().getUnlocalizedName(), getLocalisedName(material.getNameLowercase()) + " Slug");
				lang.put(material.getCasedSlug().getUnlocalizedName(), "Cased " + getLocalisedName(material.getNameLowercase()) + " Slug");
			}
		}

		for (final CircuitTypes type : CircuitTypes.values()) {
			lang.put(type.getItem("circuit").getUnlocalizedName(), getLocalisedName(type.getNameLowercase()) + " Circuit");
		}

		for (final ScopeTypes type : ScopeTypes.values()) {
			lang.put(type.getItem("scope").getUnlocalizedName(), getLocalisedName(type.getNameLowercase()) + " Scope");
		}

		for (final AttachmentPoints attachmentPoint : AttachmentPoints.values()) {
			lang.put(attachmentPoint.getNameLowercase(), getLocalisedName(attachmentPoint.getNameLowercase()));
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
					lang.put(item.getUnlocalizedName(), getLocalisedName(item.getRegistryName().getResourcePath()));
				}

			} catch (IllegalArgumentException | IllegalAccessException e) {
				WIPTech.error("fucking hell jeff...");
				e.printStackTrace();
			}
		}

		for (final Field field : ModBlocks.class.getFields()) {
			Object value;
			try {
				value = field.get(ModBlocks.class);

				if (!(value instanceof Block)) {
					continue;
				}

				final Block block = (Block) value;

				if ((block != null) && !block.getRegistryName().getResourcePath().equalsIgnoreCase("napalm") && !block.getRegistryName().getResourcePath().equalsIgnoreCase("peripheral")) {
					lang.put(block.getUnlocalizedName(), getLocalisedName(block.getRegistryName().getResourcePath()));
				}

			} catch (IllegalArgumentException | IllegalAccessException e) {
				WIPTech.error("fucking hell derek...");
				e.printStackTrace();
			}
		}

		final List<String> data = new ArrayList<>();

		lang.forEach((unlocalisedName, localisedName) -> {

			if (Loader.MC_VERSION.contains("1.13")) {
				data.add("	\"" + unlocalisedName + ".name" + "\"" + ": " + "\"" + localisedName + "\"" + ",");
			} else {
				data.add(unlocalisedName + ".name" + "=" + localisedName);
			}

		});

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
			finalData.add("\"item.modifiers.horse\": \"When on horse\",");
			finalData.add("\"startassembly\": \"Start Assembly\",");
			finalData.add("\"attachments\": \"Attachments\",");
			finalData.add("\"attachmentpoint\": \"Attachment Point\",");
			finalData.add("\"conductivity\": \"Conductivity\",");
		} else {
			finalData.add("itemGroup." + ModReference.MOD_ID + "=" + ModReference.MOD_NAME);
			finalData.add("item.modifiers.horse=When on horse");
			finalData.add("startassembly=Start Assembly");
			finalData.add("attachments=Attachments");
			finalData.add("attachmentpoint=Attachment Point");
			finalData.add("conductivity=Conductivity");
		}

		finalData.addAll(data);
		if (Loader.MC_VERSION.contains("1.13")) {
			finalData.add("}");
		}

		final Path file = Paths.get(assetDir + "lang/" + getLangFileName() + "." + (Loader.MC_VERSION.contains("1.13") ? "json" : "lang"));
		WIPTech.debug("Writing lang");
		try {
			Files.write(file, finalData, Charset.forName("UTF-8"));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private static String getLangFileName() {
		return "en_us";
	}

	private static void generateAndWriteRecipes(final ModMaterials material) {
		WIPTech.debug("Initialising recipes");

		final HashMap<String, String> recipes = new HashMap<>();

		if ((material.getOre() != null) && (material.getResource() != null) && (material.getType() == ModMaterialTypes.METAL)) {
			GameRegistry.addSmelting(new ItemStack(material.getOre()), new ItemStack(material.getResource()), 1);
		}

		if ((material.getBlock() != null) && (material.getResource() != null) && (material.getResouceLocationDomain("block", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getBlock().getRegistryName().getResourcePath(), "{\n" +
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
						"}");
				/*@formatter:on*/
		}

		if ((material.getResource() != null) && (material.getBlock() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getResource().getRegistryName().getResourcePath() + "_from_block", "{\n" +
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
						"}");
				/*@formatter:on*/
		}
		if ((material.getResource() != null) && (material.getResourcePiece() != null) && (material.getResouceLocationDomain(material.getType().getResourceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
					recipes.put(material.getResource().getRegistryName().getResourcePath() + "_from_"+material.getType().getResourcePieceNameSuffix()+"s", "{\n" +
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
							"}");
					/*@formatter:on*/
		}

		if ((material.getResourcePiece() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(material.getType().getResourcePieceNameSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getResourcePiece().getRegistryName().getResourcePath(), "{\n" +
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
						"}");
				/*@formatter:on*/
		}

		// armor

		if ((material.getHelmet() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(ModUtil.getSlotGameNameLowercase(EntityEquipmentSlot.HEAD), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getHelmet().getRegistryName().getResourcePath(), "{\n" +
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
						"}");
				/*@formatter:on*/
		}

		if ((material.getChestplate() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(ModUtil.getSlotGameNameLowercase(EntityEquipmentSlot.CHEST), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getChestplate().getRegistryName().getResourcePath(), "{\n" +
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
						"}");
				/*@formatter:on*/
		}

		if ((material.getLeggings() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(ModUtil.getSlotGameNameLowercase(EntityEquipmentSlot.LEGS), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getLeggings().getRegistryName().getResourcePath(), "{\n" +
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
						"}");
				/*@formatter:on*/
		}

		if ((material.getBoots() != null) && (material.getResource() != null) && (material.getResouceLocationDomain(ModUtil.getSlotGameNameLowercase(EntityEquipmentSlot.FEET), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getBoots().getRegistryName().getResourcePath(), "{\n" +
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
						"}");
				/*@formatter:on*/
		}

		// tools

		if ((material.getAxe() != null) && (material.getResource() != null) && (material.getResouceLocationDomain("axe", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getAxe().getRegistryName().getResourcePath(), "{\n" +
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
						"}");
				/*@formatter:on*/
		}

		if ((material.getPickaxe() != null) && (material.getResource() != null) && (material.getResouceLocationDomain("pickaxe", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getPickaxe().getRegistryName().getResourcePath(), "{\n" +
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
						"}");
				/*@formatter:on*/
		}

		if ((material.getSword() != null) && (material.getResource() != null) && (material.getResouceLocationDomain("sword", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getSword().getRegistryName().getResourcePath(), "{\n" +
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
						"}");
				/*@formatter:on*/
		}

		if ((material.getShovel() != null) && (material.getResource() != null) && (material.getResouceLocationDomain("shovel", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getShovel().getRegistryName().getResourcePath(), "{\n" +
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
						"}");
				/*@formatter:on*/
		}

		if ((material.getHoe() != null) && (material.getResource() != null) && (material.getResouceLocationDomain("hoe", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getHoe().getRegistryName().getResourcePath(), "{\n" +
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
						"}");
				/*@formatter:on*/
		}

		//

		if ((material.getWire() != null) && (material.getResource() != null)) {
			/*@formatter:off*/
			recipes.put(material.getEnamel().getRegistryName().getResourcePath(), "{\n" +
					"	\"type\": \"wiptech:hammering\",\n" +
					"	\"ingredient\": {\n" +
					"		\"item\": \""+material.getResource().getRegistryName().toString()+"\",\n" +
					"		\"count\": 1\n" +
					"	},\n" +
					"	\"result\": {\n" +
					"		\"item\": \""+material.getWire().getRegistryName().toString()+"\",\n" +
					"		\"count\": 4\n" +
					"	}\n" +
					"}");
			/*@formatter:on*/
		}

		if (material.getEnamel() != null) {
			/*@formatter:off*/
			recipes.put(material.getEnamel().getRegistryName().getResourcePath(), "{\n" +
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
					"}");
			/*@formatter:on*/
		}

		if (material.getRail() != null) {
			/*@formatter:off*/
			recipes.put(material.getRail().getRegistryName().getResourcePath(), "{\n" +
					"	\"type\": \"minecraft:crafting_shaped\",\n" +
					"	\"pattern\": [\n" +
					"		\"XX\"\n" +
					"	],\n" +
					"	\"key\": {\n" +
					"		\"X\": {\n" +
					" 			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
					"		}\n" +
					"	},\n" +
					"	\"result\": {\n" +
					"		\"item\": \""+material.getRail().getRegistryName().toString()+"\",\n" +
					"		\"count\": 1\n" +
					"	}\n" +
					"}");
			/*@formatter:on*/
		}

		if (material.getCoil() != null) {
			/*@formatter:off*/
			recipes.put(material.getCoil().getRegistryName().getResourcePath()+"_from_wire", "{\n" +
					"	\"type\": \"wiptech:coiling\",\n" +
					"	\"group\": \""+material.getCoil().getRegistryName().getResourcePath()+"\",\n" +
					"	\"ingredient\": {\n" +
					"		\"item\": \""+material.getWire().getRegistryName().toString()+"\",\n" +
					"		\"count\": 16\n" +
					"	},\n" +
					"	\"result\": {\n" +
					"		\"item\": \""+material.getCoil().getRegistryName().toString()+"\",\n" +
					"		\"count\": 1\n" +
					"	}\n" +
					"}");
			/*@formatter:on*/
			/*@formatter:off*/
			recipes.put(material.getCoil().getRegistryName().getResourcePath()+"_from_spool", "{\n" +
					"	\"type\": \"wiptech:coiling\",\n" +
					"	\"group\": \""+material.getCoil().getRegistryName().getResourcePath()+"\",\n" +
					"	\"ingredient\": {\n" +
					"		\"item\": \""+material.getSpool().getRegistryName().toString()+"\",\n" +
					"		\"count\": 1\n" +
					"	},\n" +
					"	\"result\": {\n" +
					"		\"item\": \""+material.getCoil().getRegistryName().toString()+"\",\n" +
					"		\"count\": 4\n" +
					"	}\n" +
					"}");
			/*@formatter:on*/
		}

		if (material.getSpool() != null) {
			/*@formatter:off*/
			recipes.put(material.getSpool().getRegistryName().getResourcePath()+"_from_wire", "{\n" +
					"	\"type\": \"wiptech:coiling\",\n" +
					"	\"group\": \""+material.getSpool().getRegistryName().getResourcePath()+"\",\n" +
					"	\"ingredient\": {\n" +
					"		\"item\": \""+material.getWire().getRegistryName().toString()+"\",\n" +
					"		\"count\": 64\n" +
					"	},\n" +
					"	\"result\": {\n" +
					"		\"item\": \""+material.getSpool().getRegistryName().toString()+"\",\n" +
					"		\"count\": 1\n" +
					"	}\n" +
					"}");
			/*@formatter:on*/
			/*@formatter:off*/
			recipes.put(material.getSpool().getRegistryName().getResourcePath()+"_from_coil", "{\n" +
					"	\"type\": \"wiptech:coiling\",\n" +
					"	\"group\": \""+material.getSpool().getRegistryName().getResourcePath()+"\",\n" +
					"	\"ingredient\": {\n" +
					"		\"item\": \""+material.getCoil().getRegistryName().toString()+"\",\n" +
					"		\"count\": 4\n" +
					"	},\n" +
					"	\"result\": {\n" +
					"		\"item\": \""+material.getSpool().getRegistryName().toString()+"\",\n" +
					"		\"count\": 1\n" +
					"	}\n" +
					"}");
			/*@formatter:on*/
		}

		if (material.getSlugItem() != null) {
			/*@formatter:off*/
			recipes.put(material.getCasedSlug().getRegistryName().getResourcePath(), "{\n" +
					"	\"type\": \"minecraft:crafting_shaped\",\n" +
					"	\"group\": \""+material.getCasedSlug().getRegistryName().getResourcePath()+"\",\n" +
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
					" 			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
					"		}\n" +
					"	},\n" +
					"	\"result\": {\n" +
					"		\"item\": \""+material.getCasedSlug().getRegistryName().toString()+"\",\n" +
					"		\"count\": 1\n" +
					"	}\n" +
					"}");
			/*@formatter:on*/
			/*@formatter:off*/
			recipes.put(material.getCasedSlug().getRegistryName().getResourcePath(), "{\n" +
					"	\"type\": \"minecraft:crafting_shaped\",\n" +
					"	\"group\": \""+material.getCasedSlug().getRegistryName().getResourcePath()+"\",\n" +
					"	\"pattern\": [\n" +
					"		\" B \",\n" +
					"		\"AX\",\n" +
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
					"}");
			/*@formatter:on*/
		}

		WIPTech.debug("Writing recipes for " + material);
		recipes.forEach((name, recipe) -> {
			final ArrayList data = new ArrayList<>(Arrays.asList(recipe.split("\n")));
			final Iterator<String> it = data.iterator();
			while (it.hasNext()) {
				if (it.next().equals("")) {
					it.remove();
				}
			}

			// TODO dataDir for 1.13
			final Path file = Paths.get(assetDir + "recipes/" + name.toLowerCase() + ".json");
			try {
				Files.write(file, data, Charset.forName("UTF-8"));
			} catch (final IOException e) {
				e.printStackTrace();
			}

		});
	}

	private static String getLocalisedName(String unlocalised) {
		if (getLangFileName().toLowerCase().equals("en_us")) {
			unlocalised = unlocalised.replace("aluminium", "aluminum");
		}
		return ModUtil.getLocalisedName(unlocalised);
	}

	private static String generateModelJSON(final ModResourceLocation parent, final String textureName, final ModResourceLocation textureLocation) {
		String model = "";
		model += "{" + "\n";
		model += "	" + "\"parent\": \"" + parent.toString() + "\"," + "\n";
		model += "	" + "\"textures\": {" + "\n";
		model += "		" + "\"" + textureName + "\": \"" + textureLocation.toString() + "\"" + "\n";
		model += "	" + "}" + "\n";
		model += "}" + "\n";
		return model;
	}

	private static String generateBlockstateJSON(final ModResourceLocation model, final EnumFacing... facings) {
		return generateBlockstateJSON(model, false, facings);
	}

	private static String generateBlockstateJSON(final ModResourceLocation model, final boolean isNugget, final EnumFacing... facings) {
		String blockstate = "";
		blockstate += "{" + "\n";
		blockstate += "	" + "\"variants\": {" + "\n";
		if (facings.length > 0) {
			for (final EnumFacing facing : facings) {
				if (!Arrays.asList(EnumFacing.HORIZONTALS).contains(facing)) {
					continue;
				}
				blockstate += "		" + "\"facing=" + facing.toString().toLowerCase() + "\": {" + "\n";
				if (isNugget) {
					blockstate += "			" + "\"model\": \"" + model.toString() + "\", \"y\": " + (facing.rotateY().rotateY().rotateY().getHorizontalIndex() * 90) + "\n";
				} else {
					blockstate += "			" + "\"model\": \"" + model.toString() + "\", \"y\": " + (facing.getHorizontalIndex() * 90) + "\n";
				}
				blockstate += "		" + "}," + "\n";
			}
			// remove last comma
			blockstate = StringUtils.reverse(StringUtils.reverse(blockstate).replaceFirst(",", ""));
		} else {
			blockstate += "		" + "\"" + ClientEventSubscriber.DEFAULT_VARIANT + "\": {" + "\n";
			blockstate += "			" + "\"model\": \"" + model.toString() + "\"" + "\n";
			blockstate += "		" + "}" + "\n";
		}
		blockstate += "	" + "}" + "\n";
		blockstate += "}" + "\n";

		return blockstate;
	}

	private static String generateBlockstateJSON(final ModResourceLocation model) {
		return generateBlockstateJSON(model, new EnumFacing[0]);
	}

}
