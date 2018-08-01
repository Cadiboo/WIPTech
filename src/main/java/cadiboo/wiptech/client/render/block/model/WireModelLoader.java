package cadiboo.wiptech.client.render.block.model;

import cadiboo.wiptech.client.model.ModelWire;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class WireModelLoader implements ICustomModelLoader {

	private static final String SMART_MODEL_RESOURCE_LOCATION = "models/block/wire";

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return modelLocation.getResourceDomain().equals(ModReference.ID) && modelLocation.getResourcePath().equals(SMART_MODEL_RESOURCE_LOCATION);
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		String resourcePath = modelLocation.getResourcePath();
		if (!resourcePath.equals(SMART_MODEL_RESOURCE_LOCATION)) {
			assert false : "loadModel expected " + SMART_MODEL_RESOURCE_LOCATION + " but found " + resourcePath;
		}
		String modelName = resourcePath.substring(SMART_MODEL_RESOURCE_LOCATION.length());

		if (modelName.equals("webmodel")) {
			return new ModelWire();
		} else {
			return ModelLoaderRegistry.getMissingModel();
		}
	}

}
