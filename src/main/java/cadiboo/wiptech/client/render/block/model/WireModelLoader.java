package cadiboo.wiptech.client.render.block.model;

import java.util.ArrayList;
import java.util.Arrays;

import cadiboo.wiptech.client.model.ModelEnamel;
import cadiboo.wiptech.client.model.ModelWire;
import cadiboo.wiptech.material.ModMaterial;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WireModelLoader implements ICustomModelLoader {

	public static final String WIRE_MODEL_RESOURCE_LOCATION = "models/block/builtin/wire";

	@Override
	public void onResourceManagerReload(final IResourceManager resourceManager) {
	}

	@Override
	public boolean accepts(final ResourceLocation modelLocation) {
//		WIPTech.info(modelLocation);
		return modelLocation.getNamespace().equals(ModReference.MOD_ID) && modelLocation.getPath().startsWith(WIRE_MODEL_RESOURCE_LOCATION);
	}

	@Override
	public IModel loadModel(final ResourceLocation modelLocation) throws Exception {
		final String resourcePath = modelLocation.getPath();
		if (!resourcePath.equals(WIRE_MODEL_RESOURCE_LOCATION)) {
			assert false : "loadModel expected " + WIRE_MODEL_RESOURCE_LOCATION + " but found " + resourcePath;
		}

		try {
			final ArrayList<String> modelName = new ArrayList<>(Arrays.asList(resourcePath.substring(WIRE_MODEL_RESOURCE_LOCATION.length() + 1).split("_")));
			final String modelType = modelName.get(modelName.size() - 1);
			modelName.remove(modelName.size() - 1);
			final String modelMaterial = String.join("_", modelName);

			if (modelType.toLowerCase().equals("wire")) {
				return new ModelWire(ModMaterial.valueOf(modelMaterial.toUpperCase()));
			} else if (modelType.toLowerCase().equals("enamel")) {
				return new ModelEnamel(ModMaterial.valueOf(modelMaterial.toUpperCase()));
			} else {
				return ModelLoaderRegistry.getMissingModel();
			}
		} catch (final Exception e) {
			return ModelLoaderRegistry.getMissingModel();
		}
	}

}
