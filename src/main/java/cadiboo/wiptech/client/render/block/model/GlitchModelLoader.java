package cadiboo.wiptech.client.render.block.model;

import cadiboo.wiptech.client.model.ModelGlitch;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModResourceLocation;
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

	public static final String GLITCH_MODEL_RESOURCE_LOCATION_BASE = "models/block/builtin/glitch_";

	public static final String	GLITCH_BLOCK_MODEL_RESOURCE_LOCATION	= GLITCH_MODEL_RESOURCE_LOCATION_BASE + "block";
	public static final String	GLITCH_ORE_MODEL_RESOURCE_LOCATION		= GLITCH_MODEL_RESOURCE_LOCATION_BASE + "ore";
	public static final String	GLITCH_SPOOL_MODEL_RESOURCE_LOCATION	= GLITCH_MODEL_RESOURCE_LOCATION_BASE + "spool";

	@Override
	public void onResourceManagerReload(final IResourceManager resourceManager) {
	}

	@Override
	public boolean accepts(final ResourceLocation modelLocation) {
		if (modelLocation.toString().contains("glitch")) {
//			WIPTech.info("glitch");
		}
		return modelLocation.getResourceDomain().equals(ModReference.MOD_ID) && (modelLocation.getResourcePath().startsWith(GLITCH_MODEL_RESOURCE_LOCATION_BASE));
	}

	@Override
	public IModel loadModel(final ResourceLocation modelLocation) throws Exception {
		final String resourcePath = modelLocation.getResourcePath();
		if (!resourcePath.equals(GLITCH_BLOCK_MODEL_RESOURCE_LOCATION) && !resourcePath.equals(GLITCH_ORE_MODEL_RESOURCE_LOCATION) && !resourcePath.equals(GLITCH_SPOOL_MODEL_RESOURCE_LOCATION)) {
			assert false : "loadModel expected " + GLITCH_BLOCK_MODEL_RESOURCE_LOCATION + " OR " + GLITCH_ORE_MODEL_RESOURCE_LOCATION + " OR " + GLITCH_SPOOL_MODEL_RESOURCE_LOCATION + " but found " + resourcePath;
		}

		try {
			ModResourceLocation missingModel = new ModResourceLocation(TextureMap.LOCATION_MISSING_TEXTURE);
			ModResourceLocation invisibleModel = new ModResourceLocation(TextureMap.LOCATION_MISSING_TEXTURE);
			if (resourcePath.equals(GLITCH_BLOCK_MODEL_RESOURCE_LOCATION)) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_block"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_block"));
			} else if (resourcePath.equals(GLITCH_ORE_MODEL_RESOURCE_LOCATION)) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_ore"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_ore"));
			} else if (resourcePath.equals(GLITCH_SPOOL_MODEL_RESOURCE_LOCATION)) {
				missingModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/missing_spool"));
				invisibleModel = new ModResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_spool"));
			} else {
				new IllegalArgumentException("how did we get here...??????").printStackTrace();
			}
			return new ModelGlitch(missingModel, invisibleModel);
		} catch (final Exception e) {
			return ModelLoaderRegistry.getMissingModel();
		}
	}

}
