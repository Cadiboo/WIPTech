package cadiboo.wiptech.client.render.item.model;

import java.util.ArrayList;
import java.util.Arrays;

import cadiboo.wiptech.client.model.ModelCasedSlug;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class CasedSlugModelLoader implements ICustomModelLoader {

	public static final String CASED_SLUG_MODEL_RESOURCE_LOCATION = "item/cased_slug_model";

	@Override
	public void onResourceManagerReload(final IResourceManager resourceManager) {
	}

	@Override
	public boolean accepts(final ResourceLocation modelLocation) {
		return modelLocation.getResourceDomain().equals(ModReference.MOD_ID) && modelLocation.getResourcePath().startsWith(CASED_SLUG_MODEL_RESOURCE_LOCATION);
	}

	@Override
	public IModel loadModel(final ResourceLocation modelLocation) throws Exception {
		final String resourcePath = modelLocation.getResourcePath();
		if (!resourcePath.equals(CASED_SLUG_MODEL_RESOURCE_LOCATION)) {
			// assert false : "loadModel expected " + CASED_SLUG_MODEL_RESOURCE_LOCATION + " but found " + resourcePath;
			this.getClass();
		}

		try {
			final ArrayList<String> modelName = new ArrayList<>(Arrays.asList(resourcePath.substring(CASED_SLUG_MODEL_RESOURCE_LOCATION.length() + 1).split("_")));
			modelName.remove(modelName.size() - 1);
			modelName.remove(0);
			final String modelMaterial = String.join("_", modelName);

			return new ModelCasedSlug(ModMaterials.valueOf(modelMaterial.toUpperCase()));

		} catch (final Exception e) {
			return ModelLoaderRegistry.getMissingModel();
		}
	}

}
