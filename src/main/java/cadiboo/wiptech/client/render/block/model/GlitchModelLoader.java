package cadiboo.wiptech.client.render.block.model;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.client.model.ModelGlitch;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GlitchModelLoader implements ICustomModelLoader {

	public static final String GLITCH_MODEL_RESOURCE_LOCATION = "models/block/builtin/glitch/";

	@Override
	public void onResourceManagerReload(final IResourceManager resourceManager) {
	}

	@Override
	public boolean accepts(final ResourceLocation modelLocation) {
		if (modelLocation.toString().contains("glitch")) {
			WIPTech.info("glitch");
			WIPTech.info(modelLocation);
		}
		return modelLocation.getNamespace().equals(ModReference.MOD_ID) && (modelLocation.getPath().startsWith(GLITCH_MODEL_RESOURCE_LOCATION));
	}

	@Override
	public IModel loadModel(final ResourceLocation modelLocation) throws Exception {
		final String resourcePath = modelLocation.getPath().replace(GLITCH_MODEL_RESOURCE_LOCATION, "");

		try {
			ModResourceLocation missingModel = new ModResourceLocation(TextureMap.LOCATION_MISSING_TEXTURE);
			ModResourceLocation invisibleModel = new ModResourceLocation(TextureMap.LOCATION_MISSING_TEXTURE);
			if (resourcePath.equals("glitch_block")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_block"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_block"));
			} else if (resourcePath.equals("glitch_ore")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_ore"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_ore"));
			} else if (resourcePath.equals("glitch_spool")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_spool"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_spool"));
			} else if (resourcePath.equals("glitch_axe")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_axe"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_axe"));
			} else if (resourcePath.equals("glitch_boots")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "boots/missing_boots"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "boots/invisible_boots"));
			} else if (resourcePath.equals("glitch_cased_slug")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "cased_slug/missing_cased_slug"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "cased_slug/invisible_cased_slug"));
			} else if (resourcePath.equals("glitch_chestplate")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "chestplate/missing_chestplate"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "chestplate/invisible_chestplate"));
			} else if (resourcePath.equals("glitch_coil")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "coil/missing_coil"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "coil/invisible_coil"));
			} else if (resourcePath.equals("glitch_enamel")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "enamel/missing_enamel"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "enamel/invisible_enamel"));
			} else if (resourcePath.equals("glitch_helmet")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_helmet"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_helmet"));
			} else if (resourcePath.equals("glitch_hoe")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_hoe"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_hoe"));
			} else if (resourcePath.equals("glitch_horse_armor")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_horse_armor"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_horse_armor"));
			} else if (resourcePath.equals("glitch_ingot")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_ingot"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_ingot"));
			} else if (resourcePath.equals("glitch_leggings")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_leggings"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_leggings"));
			} else if (resourcePath.equals("glitch_nugget")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_nugget"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_nugget"));
			} else if (resourcePath.equals("glitch_rail")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_rail"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_rail"));
			} else if (resourcePath.equals("glitch_pickaxe")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_pickaxe"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_pickaxe"));
			} else if (resourcePath.equals("glitch_shovel")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_shovel"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_shovel"));
			} else if (resourcePath.equals("glitch_slug")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_slug"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_slug"));
			} else if (resourcePath.equals("glitch_sword")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_sword"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_sword"));
			} else if (resourcePath.equals("glitch_wire")) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_wire"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_wire"));
			} else {
				new IllegalArgumentException("how did we get here...??????").printStackTrace();
			}
			return new ModelGlitch(missingModel, invisibleModel);
		} catch (final Exception e) {
			return ModelLoaderRegistry.getMissingModel();
		}
	}

}
