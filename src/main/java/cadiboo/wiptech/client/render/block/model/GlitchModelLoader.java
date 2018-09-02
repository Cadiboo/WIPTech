package cadiboo.wiptech.client.render.block.model;

import cadiboo.wiptech.client.model.ModelGlitchBlock;
import cadiboo.wiptech.client.model.ModelGlitchOre;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GlitchModelLoader implements ICustomModelLoader {

	public static final String GLITCH_BLOCK_MODEL_RESOURCE_LOCATION = "models/block/glitch_block";
	public static final String GLITCH_ORE_MODEL_RESOURCE_LOCATION = "models/block/glitch_ore";

	@Override
	public void onResourceManagerReload(final IResourceManager resourceManager) {
	}

	@Override
	public boolean accepts(final ResourceLocation modelLocation) {
		return modelLocation.getResourceDomain().equals(ModReference.MOD_ID) && (modelLocation.getResourcePath().startsWith(GLITCH_BLOCK_MODEL_RESOURCE_LOCATION) || modelLocation.getResourcePath().startsWith(GLITCH_ORE_MODEL_RESOURCE_LOCATION));
	}

	@Override
	public IModel loadModel(final ResourceLocation modelLocation) throws Exception {
		final String resourcePath = modelLocation.getResourcePath();
		if (!resourcePath.equals(GLITCH_BLOCK_MODEL_RESOURCE_LOCATION) || !resourcePath.equals(GLITCH_ORE_MODEL_RESOURCE_LOCATION)) {
			assert false : "loadModel expected " + GLITCH_BLOCK_MODEL_RESOURCE_LOCATION + " OR " + GLITCH_ORE_MODEL_RESOURCE_LOCATION + " but found " + resourcePath;
		}

		try {
			if (resourcePath.equals(GLITCH_BLOCK_MODEL_RESOURCE_LOCATION)) {
				return new ModelGlitchBlock();
			} else if (resourcePath.equals(GLITCH_ORE_MODEL_RESOURCE_LOCATION)) {
				return new ModelGlitchOre();
			} else {
				throw new IllegalArgumentException();
			}
		} catch (final Exception e) {
			return ModelLoaderRegistry.getMissingModel();
		}
	}

}
