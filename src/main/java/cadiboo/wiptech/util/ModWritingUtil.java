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
import cadiboo.wiptech.material.GemProperties;
import cadiboo.wiptech.material.MetalProperties;
import cadiboo.wiptech.material.ModMaterial;
import cadiboo.wiptech.material.ModMaterialProperties;
import cadiboo.wiptech.util.ModEnums.AttachmentPoint;
import cadiboo.wiptech.util.ModEnums.CircuitType;
import cadiboo.wiptech.util.ModEnums.ScopeType;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocation;
import net.minecraft.block.Block;
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

	private static String assetDir = "/Users/" + System.getProperty("user.name") + "/Developer/Modding/WIPTechAlpha/src/main/resources/assets/wiptech/";
	private static String dataDir = "/Users/" + System.getProperty("user.name") + "/Developer/Modding/WIPTechAlpha/src/main/resources/data/wiptech/";

	private static final ModResourceLocation ITEM_GENERATED = new ModResourceLocation("", "item/generated");
	private static final ModResourceLocation ITEM_HANDHELD = new ModResourceLocation("", "item/handheld");
	private static final String ITEM_DEFAULT_TEXTURE_NAME = "layer0";

	@ExistsForDebugging
	@SideOnly(Side.CLIENT)
	public static void writeMod() {

		final boolean recipes = true;
		final boolean lang = true;
		final boolean json = true;

		WIPTech.info("ModWritingUtil.writeMod() with options write recipes: " + recipes + ", write lang: " + lang + ", write json: " + json);

		if (ModReference.Debug.debugAPIMaterials()) {
			WIPTech.info("debugAPIMaterials returned true! to prevent large useless files from being created writeMod() is Aborting!");
			return;
		}

		if (Boolean.valueOf(false).equals(recipes) && Boolean.valueOf(false).equals(lang) && Boolean.valueOf(false).equals(json)) {
			WIPTech.info("All arguments are false for writeMod() - Aborting!");
			return;
		}

		if (recipes) {
			new Thread(() -> {
				for (final ModMaterial material : ModMaterial.values()) {
					try {
						generateAndWriteRecipes(material);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}, "Recipe Writer Thread").start();
		}

		if (lang) {
			new Thread(() -> {
				try {
					generateAndWriteLang();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}, "Lang Writer Thread").start();
		}

		if (json) {
			new Thread(() -> {
				for (final ModMaterial material : ModMaterial.values()) {
					try {
						generateAndWriteModels(material);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}, "JSON Writer Thread").start();
		}

	}

	private static void generateAndWriteModels(final ModMaterial material) {

		final ModMaterialProperties properties = material.getProperties();

		final HashMap<String, String> blockstates = new HashMap<>();
		final HashMap<String, String> blockModels = new HashMap<>();
		final HashMap<String, String> itemModels = new HashMap<>();

		if (properties.hasOre()) {
			final ModResourceLocation ore = new ModResourceLocation(material.getOre().getRegistryName());

			if (ore.getNamespace().equals(ModReference.MOD_ID)) {

				final String path = ore.getPath();
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

			if (block.getNamespace().equals(ModReference.MOD_ID)) {

				final String path = block.getPath();
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

			final String path = resource.getPath();
			final ModResourceLocation model = new ModResourceLocation(ModReference.MOD_ID, path);

			blockstates.put(path, generateBlockstateJSON(model, EnumFacing.HORIZONTALS));

			String resourceNameSuffix = material.getProperties().getResourceSuffix();
			if (material.getProperties() instanceof GemProperties) {
				resourceNameSuffix = "gem";
			}

			final ModResourceLocation parent = new ModResourceLocation(ModReference.MOD_ID, "block/" + resourceNameSuffix);
			final String textureName = resourceNameSuffix;
			final ModResourceLocation textureLocation = getTextureLocation(resource, "item");

			blockModels.put(path, generateModelJSON(parent, textureName, textureLocation));

			if (resource.getNamespace().equals(ModReference.MOD_ID)) {

				itemModels.put(path, generateModelJSON(ITEM_GENERATED, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));

			}
		}

		if (properties.hasResource() && material.getProperties().hasResourcePiece()) {
			ModResourceLocation resourcePiece = new ModResourceLocation(Item.getItemFromBlock(material.getResourcePiece()).getRegistryName());
			final String path = resourcePiece.getPath();

			ModResourceLocation textureLocation = getTextureLocation(resourcePiece, "item");
			if (resourcePiece.getNamespace().equals(ModReference.MOD_ID)) {

				itemModels.put(path, generateModelJSON(ITEM_GENERATED, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));

			}

			// TODO:
			// HACK cause im too lazy to make my nuggets models use the nugget textures instead of the currently being used ingot textures
			if (material.getProperties() instanceof MetalProperties) {
				resourcePiece = new ModResourceLocation(Item.getItemFromBlock(material.getResource()).getRegistryName());
			}

			String resourcePieceNameSuffix = material.getProperties().getResourcePieceSuffix();
			final ModResourceLocation parent = new ModResourceLocation(ModReference.MOD_ID, "block/" + resourcePieceNameSuffix);
			// TODO:
			// HACK cause im too lazy to make my nuggets models use the nugget textures instead of the currently being used ingot textures
			if (material.getProperties() instanceof MetalProperties) {
				resourcePieceNameSuffix = "ingot";
			}

			final ModResourceLocation model = new ModResourceLocation(ModReference.MOD_ID, path);

			blockstates.put(path, generateBlockstateJSON(model, true, EnumFacing.HORIZONTALS));

			final String textureName = resourcePieceNameSuffix;
			// TODO:
			// HACK cause im too lazy to make my nuggets models use the nugget textures instead of the currently being used ingot textures
			if (material.getProperties() instanceof MetalProperties) {
				textureLocation = getTextureLocation(resourcePiece, "item");
			}

			blockModels.put(path, generateModelJSON(parent, textureName, textureLocation));
		}

		if (properties.hasHelmet()) {

			final ModResourceLocation armor = new ModResourceLocation(material.getHelmet().getRegistryName());

			if (armor.getNamespace().equals(ModReference.MOD_ID)) {

				final String path = armor.getPath();
				final ModResourceLocation textureLocation = getTextureLocation(armor, "item");

				itemModels.put(path, generateModelJSON(ITEM_GENERATED, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));

			}
		}

		if (properties.hasChestplate()) {

			final ModResourceLocation armor = new ModResourceLocation(material.getChestplate().getRegistryName());

			if (armor.getNamespace().equals(ModReference.MOD_ID)) {

				final String path = armor.getPath();
				final ModResourceLocation textureLocation = getTextureLocation(armor, "item");

				itemModels.put(path, generateModelJSON(ITEM_GENERATED, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));

			}

		}

		if (properties.hasLeggings()) {

			final ModResourceLocation armor = new ModResourceLocation(material.getLeggings().getRegistryName());

			if (armor.getNamespace().equals(ModReference.MOD_ID)) {

				final String path = armor.getPath();
				final ModResourceLocation textureLocation = getTextureLocation(armor, "item");

				itemModels.put(path, generateModelJSON(ITEM_GENERATED, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));

			}

		}

		if (properties.hasBoots()) {
			final ModResourceLocation armor = new ModResourceLocation(material.getBoots().getRegistryName());

			if (armor.getNamespace().equals(ModReference.MOD_ID)) {

				final String path = armor.getPath();
				final ModResourceLocation textureLocation = getTextureLocation(armor, "item");

				itemModels.put(path, generateModelJSON(ITEM_GENERATED, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));

			}
		}

		if (properties.hasHorseArmor()) {
			final ModResourceLocation armor = new ModResourceLocation(material.getHorseArmor().getRegistryName());

			if (armor.getNamespace().equals(ModReference.MOD_ID)) {

				final String path = armor.getPath();
				final ModResourceLocation textureLocation = getTextureLocation(armor, "item");

				itemModels.put(path, generateModelJSON(ITEM_GENERATED, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));

			}
		}

		if (properties.hasPickaxe()) {

			final ModResourceLocation pickaxe = new ModResourceLocation(material.getPickaxe().getRegistryName());

			if (pickaxe.getNamespace().equals(ModReference.MOD_ID)) {

				final String path = pickaxe.getPath();
				final ModResourceLocation textureLocation = getTextureLocation(pickaxe, "item");

				itemModels.put(path, generateModelJSON(ITEM_HANDHELD, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));
			}
		}

		if (properties.hasAxe()) {

			final ModResourceLocation axe = new ModResourceLocation(material.getAxe().getRegistryName());

			if (axe.getNamespace().equals(ModReference.MOD_ID)) {

				final String path = axe.getPath();
				final ModResourceLocation textureLocation = getTextureLocation(axe, "item");

				itemModels.put(path, generateModelJSON(ITEM_HANDHELD, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));

			}

		}

		if (properties.hasSword()) {

			final ModResourceLocation tool = new ModResourceLocation(material.getSword().getRegistryName());

			if (tool.getNamespace().equals(ModReference.MOD_ID)) {

				final String path = tool.getPath();
				final ModResourceLocation textureLocation = getTextureLocation(tool, "item");

				itemModels.put(path, generateModelJSON(ITEM_HANDHELD, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));

			}

		}

		if (properties.hasShovel()) {

			final ModResourceLocation tool = new ModResourceLocation(material.getShovel().getRegistryName());

			if (tool.getNamespace().equals(ModReference.MOD_ID)) {

				final String path = tool.getPath();
				final ModResourceLocation textureLocation = getTextureLocation(tool, "item");

				itemModels.put(path, generateModelJSON(ITEM_HANDHELD, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));

			}

		}

		if (properties.hasHoe()) {

			final ModResourceLocation tool = new ModResourceLocation(material.getHoe().getRegistryName());

			if (tool.getNamespace().equals(ModReference.MOD_ID)) {

				final String path = tool.getPath();
				final ModResourceLocation textureLocation = getTextureLocation(tool, "item");

				itemModels.put(path, generateModelJSON(ITEM_HANDHELD, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));

			}

		}

		if (properties.hasWire()) {
			final ModResourceLocation wire = new ModResourceLocation(material.getWire().getRegistryName());

			final String path = wire.getPath();
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

			final String path = enamel.getPath();
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

			final String path = coil.getPath();
			final ModResourceLocation textureLocation = getTextureLocation(coil, "item");

			itemModels.put(path, generateModelJSON(ITEM_GENERATED, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));

			final ModResourceLocation resource = new ModResourceLocation(Item.getItemFromBlock(material.getResource()).getRegistryName());

			final ModResourceLocation spool = new ModResourceLocation(material.getSpool().getRegistryName());

			final String spoolPath = spool.getPath();

			final ModResourceLocation model = new ModResourceLocation(ModReference.MOD_ID, spoolPath);
			blockstates.put(spoolPath, generateBlockstateJSON(model));

			final ModResourceLocation spoolTextureLocation = getTextureLocation(resource, "item");
			final ModResourceLocation parent = new ModResourceLocation(ModReference.MOD_ID, "block/spool");
			final String textureName = "resource";

			blockModels.put(spoolPath, generateModelJSON(parent, textureName, spoolTextureLocation));

		}

		if (properties.hasRail()) {

			final ModResourceLocation rail = new ModResourceLocation(material.getRail().getRegistryName());

			final String path = rail.getPath();
			final ModResourceLocation textureLocation = getTextureLocation(rail, "item");

			itemModels.put(path, generateModelJSON(ITEM_GENERATED, ITEM_DEFAULT_TEXTURE_NAME, textureLocation));

		}

		if (properties.hasRailgunSlug()) {

			final ModResourceLocation slug = new ModResourceLocation(material.getSlugItem().getRegistryName());

			final String path = slug.getPath();

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
				WIPTech.info("Writing Blockstate " + name.toLowerCase() + ".json");
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
				WIPTech.info("Writing Block Model " + name.toLowerCase() + ".json");
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
				WIPTech.info("Writing Item Model " + name.toLowerCase() + ".json");
				Files.write(file, data, Charset.forName("UTF-8"));
			} catch (final IOException e) {
				e.printStackTrace();
			}

		});

	}

	private static ModResourceLocation getTextureLocation(final ModResourceLocation location, final String prepend) {
		final String domain = location.getNamespace();
		final String path = location.getPath();

		final String optionalS = domain.equals("minecraft") && !Loader.MC_VERSION.contains("1.13") ? "s" : "";

		final String texturePath = prepend + optionalS + "/" + path;

		final ModResourceLocation textureLocation = new ModResourceLocation(domain, texturePath);
		return textureLocation;
	}

	private static void generateAndWriteLang() {
		WIPTech.debug("Initialising lang");

		final HashMap<String, String> lang = new HashMap<>();

		for (final ModMaterial material : ModMaterial.values()) {

			if (material.getOre() != null) {
				lang.put(material.getOre().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Ore");
			}

			if (material.getBlock() != null) {
				lang.put(material.getBlock().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Block");
			}

			if (material.getResource() != null) {
				lang.put(material.getResource().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + (material.getProperties() instanceof GemProperties ? "" : " Ingot"));
			}

			if (material.getResourcePiece() != null) {
				lang.put(material.getResourcePiece().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " " + (material.getProperties() instanceof GemProperties ? "Shard" : "Nugget"));
			}

			// armor

			if (material.getHelmet() != null) {
				lang.put(material.getHelmet().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Helmet");
			}

			if (material.getChestplate() != null) {
				lang.put(material.getChestplate().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Chestplate");
			}

			if (material.getLeggings() != null) {
				lang.put(material.getLeggings().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Leggings");
			}

			if (material.getBoots() != null) {
				lang.put(material.getBoots().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Boots");
			}

			if (material.getHorseArmor() != null) {
				lang.put(material.getHorseArmor().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Horse Armor");
			}

			// tools

			if (material.getAxe() != null) {
				lang.put(material.getAxe().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Axe");
			}

			if (material.getPickaxe() != null) {
				lang.put(material.getPickaxe().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Pickaxe");
			}

			if (material.getSword() != null) {
				lang.put(material.getSword().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Sword");
			}

			if (material.getShovel() != null) {
				lang.put(material.getShovel().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Shovel");
			}

			if (material.getHoe() != null) {
				lang.put(material.getHoe().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Hoe");
			}

			//

			if (material.getWire() != null) {
				lang.put(material.getWire().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Wire");
			}

			if (material.getEnamel() != null) {
				lang.put(material.getEnamel().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Enamel");
			}

			if (material.getRail() != null) {
				lang.put(material.getRail().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Rail");
			}

			if (material.getCoil() != null) {
				lang.put(material.getCoil().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Coil");
			}

			if (material.getSpool() != null) {
				lang.put(material.getSpool().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Spool");
			}

			if (material.getSlugItem() != null) {
				lang.put(material.getSlugItem().getTranslationKey(), getTranslatedTranslationKey(material.getNameLowercase()) + " Slug");
				lang.put(material.getCasedSlug().getTranslationKey(), "Cased " + getTranslatedTranslationKey(material.getNameLowercase()) + " Slug");
			}
		}

		for (final CircuitType type : CircuitType.values()) {
			lang.put(type.getItem("circuit").getTranslationKey(), getTranslatedTranslationKey(type.getNameLowercase()) + " Circuit");
		}

		for (final ScopeType type : ScopeType.values()) {
			lang.put(type.getItem("scope").getTranslationKey(), getTranslatedTranslationKey(type.getNameLowercase()) + " Scope");
		}

		for (final AttachmentPoint attachmentPoint : AttachmentPoint.values()) {
			lang.put(attachmentPoint.getNameLowercase(), getTranslatedTranslationKey(attachmentPoint.getNameLowercase()));
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
					lang.put(item.getTranslationKey(), getTranslatedTranslationKey(item.getRegistryName().getPath()));
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

				if ((block != null) && !block.getRegistryName().getPath().equalsIgnoreCase("napalm") && !block.getRegistryName().getPath().equalsIgnoreCase("peripheral")) {
					lang.put(block.getTranslationKey(), getTranslatedTranslationKey(block.getRegistryName().getPath()));
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

		for (final String langEntry : finalData) {
			WIPTech.info("Lang Entry " + langEntry);
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

	private static void generateAndWriteRecipes(final ModMaterial material) {
		WIPTech.debug("Initialising recipes");

		final HashMap<String, String> recipes = new HashMap<>();

		if ((material.getOre() != null) && (material.getResource() != null)) {
			GameRegistry.addSmelting(new ItemStack(material.getOre()), new ItemStack(material.getResource()), 1);
		}

		if ((material.getBlock() != null) && (material.getResource() != null) && (material.getResouceLocationDomainWithOverrides("block", ForgeRegistries.BLOCKS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getBlock().getRegistryName().getPath(), "{\n" +
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

		if ((material.getResource() != null) && (material.getBlock() != null) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides("block", ForgeRegistries.BLOCKS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getResource().getRegistryName().getPath() + "_from_block", "{\n" +
						"	\"type\": \"minecraft:crafting_shaped\",\n" +
						"	\"group\": \""+material.getResource().getRegistryName().getPath()+"\",\n" +
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
		if ((material.getResource() != null) && (material.getResourcePiece() != null) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourcePieceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
					recipes.put(material.getResource().getRegistryName().getPath() + "_from_"+material.getProperties().getResourcePieceSuffix()+"s", "{\n" +
							"	\"type\": \"minecraft:crafting_shaped\",\n" +
							"	\"group\": \""+material.getResource().getRegistryName().getPath()+"\",\n" +
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

		if ((material.getResourcePiece() != null) && (material.getResource() != null) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourcePieceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getResourcePiece().getRegistryName().getPath(), "{\n" +
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

		if ((material.getHelmet() != null) && (material.getResource() != null) && (material.getResouceLocationDomainWithOverrides("helmet", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getHelmet().getRegistryName().getPath(), "{\n" +
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

		if ((material.getChestplate() != null) && (material.getResource() != null) && (material.getResouceLocationDomainWithOverrides("chesplate", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getChestplate().getRegistryName().getPath(), "{\n" +
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

		if ((material.getLeggings() != null) && (material.getResource() != null) && (material.getResouceLocationDomainWithOverrides("leggings", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getLeggings().getRegistryName().getPath(), "{\n" +
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

		if ((material.getBoots() != null) && (material.getResource() != null) && (material.getResouceLocationDomainWithOverrides("boots", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getBoots().getRegistryName().getPath(), "{\n" +
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

		// if ((material.getHorseArmor() != null) && (material.getResource() != null) && (material.getResouceLocationDomainWithOverrides("boots", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
//			/*@formatter:off*/
//				recipes.put(material.getBoots().getRegistryName().getResourcePath(), "{\n" +
//						"	\"type\": \"minecraft:crafting_shaped\",\n" +
//						"	\"pattern\": [\n" +
//						"		\" XX\",\n" +
//						"		\"XX \"\n" +
//						"	],\n" +
//						"	\"key\": {\n" +
//						"		\"X\": {\n" +
//						" 			\"item\": \""+material.getResource().getRegistryName().toString()+"\"\n" +
//						"		}\n" +
//						"	},\n" +
//						"	\"result\": {\n" +
//						"		\"item\": \""+material.getHorseArmor().getRegistryName().toString()+"\",\n" +
//						"		\"count\": 1\n" +
//						"	}\n" +
//						"}");
//				/*@formatter:on*/
		// }

		// tools

		if ((material.getAxe() != null) && (material.getResource() != null) && (material.getResouceLocationDomainWithOverrides("axe", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getAxe().getRegistryName().getPath(), "{\n" +
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

		if ((material.getPickaxe() != null) && (material.getResource() != null) && (material.getResouceLocationDomainWithOverrides("pickaxe", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getPickaxe().getRegistryName().getPath(), "{\n" +
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

		if ((material.getSword() != null) && (material.getResource() != null) && (material.getResouceLocationDomainWithOverrides("sword", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getSword().getRegistryName().getPath(), "{\n" +
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

		if ((material.getShovel() != null) && (material.getResource() != null) && (material.getResouceLocationDomainWithOverrides("shovel", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getShovel().getRegistryName().getPath(), "{\n" +
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

		if ((material.getHoe() != null) && (material.getResource() != null) && (material.getResouceLocationDomainWithOverrides("hoe", ForgeRegistries.ITEMS).equals(ModReference.MOD_ID)) && (material.getResouceLocationDomainWithOverrides(material.getProperties().getResourceSuffix(), ForgeRegistries.ITEMS).equals(ModReference.MOD_ID))) {
			/*@formatter:off*/
				recipes.put(material.getHoe().getRegistryName().getPath(), "{\n" +
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

		// if ((material.getWire() != null) && (material.getResource() != null)) {
//			/*@formatter:off*/
//			recipes.put(material.getEnamel().getRegistryName().getResourcePath(), "{\n" +
//					"	\"type\": \"wiptech:hammering\",\n" +
//					"	\"ingredient\": {\n" +
//					"		\"item\": \""+material.getResource().getRegistryName().toString()+"\",\n" +
//					"		\"count\": 1\n" +
//					"	},\n" +
//					"	\"result\": {\n" +
//					"		\"item\": \""+material.getWire().getRegistryName().toString()+"\",\n" +
//					"		\"count\": 4\n" +
//					"	}\n" +
//					"}");
//			/*@formatter:on*/
		// }

		if (material.getEnamel() != null) {
			/*@formatter:off*/
			recipes.put(material.getEnamel().getRegistryName().getPath(), "{\n" +
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
			recipes.put(material.getRail().getRegistryName().getPath(), "{\n" +
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

		// if (material.getCoil() != null) {
//			/*@formatter:off*/
//			recipes.put(material.getCoil().getRegistryName().getResourcePath()+"_from_wire", "{\n" +
//					"	\"type\": \"wiptech:coiling\",\n" +
//					"	\"group\": \""+material.getCoil().getRegistryName().getResourcePath()+"\",\n" +
//					"	\"ingredient\": {\n" +
//					"		\"item\": \""+material.getWire().getRegistryName().toString()+"\",\n" +
//					"		\"count\": 16\n" +
//					"	},\n" +
//					"	\"result\": {\n" +
//					"		\"item\": \""+material.getCoil().getRegistryName().toString()+"\",\n" +
//					"		\"count\": 1\n" +
//					"	}\n" +
//					"}");
//			/*@formatter:on*/
//			/*@formatter:off*/
//			recipes.put(material.getCoil().getRegistryName().getResourcePath()+"_from_spool", "{\n" +
//					"	\"type\": \"wiptech:coiling\",\n" +
//					"	\"group\": \""+material.getCoil().getRegistryName().getResourcePath()+"\",\n" +
//					"	\"ingredient\": {\n" +
//					"		\"item\": \""+material.getSpool().getRegistryName().toString()+"\",\n" +
//					"		\"count\": 1\n" +
//					"	},\n" +
//					"	\"result\": {\n" +
//					"		\"item\": \""+material.getCoil().getRegistryName().toString()+"\",\n" +
//					"		\"count\": 4\n" +
//					"	}\n" +
//					"}");
//			/*@formatter:on*/
		// }
		//
		// if (material.getSpool() != null) {
//			/*@formatter:off*/
//			recipes.put(material.getSpool().getRegistryName().getResourcePath()+"_from_wire", "{\n" +
//					"	\"type\": \"wiptech:coiling\",\n" +
//					"	\"group\": \""+material.getSpool().getRegistryName().getResourcePath()+"\",\n" +
//					"	\"ingredient\": {\n" +
//					"		\"item\": \""+material.getWire().getRegistryName().toString()+"\",\n" +
//					"		\"count\": 64\n" +
//					"	},\n" +
//					"	\"result\": {\n" +
//					"		\"item\": \""+material.getSpool().getRegistryName().toString()+"\",\n" +
//					"		\"count\": 1\n" +
//					"	}\n" +
//					"}");
//			/*@formatter:on*/
//			/*@formatter:off*/
//			recipes.put(material.getSpool().getRegistryName().getResourcePath()+"_from_coil", "{\n" +
//					"	\"type\": \"wiptech:coiling\",\n" +
//					"	\"group\": \""+material.getSpool().getRegistryName().getResourcePath()+"\",\n" +
//					"	\"ingredient\": {\n" +
//					"		\"item\": \""+material.getCoil().getRegistryName().toString()+"\",\n" +
//					"		\"count\": 4\n" +
//					"	},\n" +
//					"	\"result\": {\n" +
//					"		\"item\": \""+material.getSpool().getRegistryName().toString()+"\",\n" +
//					"		\"count\": 1\n" +
//					"	}\n" +
//					"}");
//			/*@formatter:on*/
		// }

		if (material.getSlugItem() != null) {
			/*@formatter:off*/
			recipes.put(material.getCasedSlug().getRegistryName().getPath(), "{\n" +
					"	\"type\": \"minecraft:crafting_shaped\",\n" +
					"	\"group\": \""+material.getCasedSlug().getRegistryName().getPath()+"\",\n" +
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
			recipes.put(material.getCasedSlug().getRegistryName().getPath(), "{\n" +
					"	\"type\": \"minecraft:crafting_shaped\",\n" +
					"	\"group\": \""+material.getCasedSlug().getRegistryName().getPath()+"\",\n" +
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
				WIPTech.info("Writing Recipe " + name.toLowerCase() + ".json");
				Files.write(file, data, Charset.forName("UTF-8"));
			} catch (final IOException e) {
				e.printStackTrace();
			}

		});
	}

	private static String getTranslatedTranslationKey(String unlocalised) {
		if (getLangFileName().toLowerCase().equals("en_us")) {
			unlocalised = unlocalised.replace("aluminium", "aluminum");
		}
		return ModUtil.registryNameToTranslationKey(unlocalised);
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
