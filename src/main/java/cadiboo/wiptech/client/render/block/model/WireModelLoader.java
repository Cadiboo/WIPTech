package cadiboo.wiptech.client.render.block.model;

import java.util.ArrayList;
import java.util.Arrays;

import cadiboo.wiptech.client.model.ModelEnamel;
import cadiboo.wiptech.client.model.ModelWire;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
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

	public static final String WIRE_MODEL_RESOURCE_LOCATION = "models/block/wiremodel";

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {

		return modelLocation.getResourceDomain().equals(ModReference.Version.getModId()) && modelLocation.getResourcePath().startsWith(WIRE_MODEL_RESOURCE_LOCATION);
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		String resourcePath = modelLocation.getResourcePath();
		if (!resourcePath.equals(WIRE_MODEL_RESOURCE_LOCATION)) {
			assert false : "loadModel expected " + WIRE_MODEL_RESOURCE_LOCATION + " but found " + resourcePath;
		}

		ArrayList<String> modelName = new ArrayList<String>(Arrays.asList(resourcePath.substring(WIRE_MODEL_RESOURCE_LOCATION.length() + 1).split("_")));
		String modelType = modelName.get(modelName.size() - 1);
		modelName.remove(modelName.size() - 1);
		String modelMaterial = String.join("_", modelName);

		if (modelType.toLowerCase().equals("wire")) {
			return new ModelWire(ModMaterials.valueOf(modelMaterial.toUpperCase()));
		} else if (modelType.toLowerCase().equals("enamel")) {
			return new ModelEnamel(ModMaterials.valueOf(modelMaterial.toUpperCase()));
		} else {
			return ModelLoaderRegistry.getMissingModel();
		}
	}

}
