package cadiboo.wiptech.client.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.util.ModResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * modified from code made by Draco18s. Information in <a href= "http://www.minecraftforge.net/forum/topic/42318-1102-water-liquid-block-model/?tab=comments#comment-228067">this thread</a>
 * @author Elix_x (August 2016), modified by Draco18s (October 2016) and then Cadiboo (August 2018)
 */
@SideOnly(Side.CLIENT)
public class ModelsCache implements ISelectiveResourceReloadListener {

	public static final ModelsCache INSTANCE = new ModelsCache();

	private ModelsCache() {
	}

	public static final IModelState										DEFAULTMODELSTATE		= part -> java.util.Optional.empty();
	public static final VertexFormat									DEFAULTVERTEXFORMAT		= DefaultVertexFormats.BLOCK;
	public static final Function<ResourceLocation, TextureAtlasSprite>	DEFAULTTEXTUREGETTER	= texture -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());

	private final Map<ResourceLocation, IModel>			modelCache	= new HashMap<>();
	private final Map<ResourceLocation, IBakedModel>	bakedCache	= new HashMap<>();

	public IModel getModel(final ModResourceLocation location) {
		IModel model = this.modelCache.get(location);
		if (model == null) {
			try {
				model = ModelLoaderRegistry.getModel(location);
			} catch (final Exception e) {
				WIPTech.error("Error loading model " + location.toString());
				e.printStackTrace();
				model = ModelLoaderRegistry.getMissingModel();
			}
			this.modelCache.put(location, model);
		}
		return model;
	}

	public IBakedModel getBakedModel(final ModResourceLocation location) {
		return this.getBakedModel(location, DEFAULTMODELSTATE, DEFAULTVERTEXFORMAT, DEFAULTTEXTUREGETTER);
	}

	public IBakedModel getBakedModel(final ModResourceLocation location, final IModelState state, final VertexFormat format, final Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
		IBakedModel bakedModel = this.bakedCache.get(location);
		if (bakedModel == null) {
			bakedModel = this.getModel(location).bake(state, format, textureGetter);
			this.bakedCache.put(location, bakedModel);
		}
		return bakedModel;
	}

	@Override
	public void onResourceManagerReload(final IResourceManager resourceManager, final Predicate<IResourceType> resourcePredicate) {
		this.modelCache.clear();
		this.bakedCache.clear();
	}

}
