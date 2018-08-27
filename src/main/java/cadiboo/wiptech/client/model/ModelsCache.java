package cadiboo.wiptech.client.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import com.google.common.base.Function;

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
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Information in <a href=
 * "http://www.minecraftforge.net/forum/topic/42318-1102-water-liquid-block-model/?tab=comments#comment-228067">this
 * thread</a>
 * 
 * @author Elix_x (August 2016), modified by Draco18s (October 2016) and then
 *         Cadiboo (2018)
 */
@SideOnly(Side.CLIENT)
public enum ModelsCache implements ISelectiveResourceReloadListener {

	INSTANCE;

	public static final IModelState DEFAULTMODELSTATE = new IModelState() {

		@Override
		public java.util.Optional<TRSRTransformation> apply(java.util.Optional<? extends IModelPart> part) {
			return java.util.Optional.empty();
		}
	};
	public static final VertexFormat DEFAULTVERTEXFORMAT = DefaultVertexFormats.BLOCK;
	public static final Function<ResourceLocation, TextureAtlasSprite> DEFAULTTEXTUREGETTER = new Function<ResourceLocation, TextureAtlasSprite>() {
		@Override
		public TextureAtlasSprite apply(ResourceLocation texture) {
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
		}
	};

	private final Map<ResourceLocation, IModel> modelCache = new HashMap<ResourceLocation, IModel>();
	private final Map<ResourceLocation, IBakedModel> bakedCache = new HashMap<ResourceLocation, IBakedModel>();

	public IModel getModel(ModResourceLocation location) {
		IModel model = modelCache.get(location);
		if (model == null) {
			try {
				model = ModelLoaderRegistry.getModel(location);
			} catch (Exception e) {
				WIPTech.error("Error loading model " + location.toString());
				e.printStackTrace();
				model = ModelLoaderRegistry.getMissingModel();
			}
			modelCache.put(location, model);
		}
		return model;
	}

	public IBakedModel getBakedModel(ModResourceLocation location) {
		return getBakedModel(location, DEFAULTMODELSTATE, DEFAULTVERTEXFORMAT, DEFAULTTEXTUREGETTER);
	}

	public IBakedModel getBakedModel(ModResourceLocation location, IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
		IBakedModel bakedModel = bakedCache.get(location);
		if (bakedModel == null) {
			bakedModel = getModel(location).bake(state, format, textureGetter);
			bakedCache.put(location, bakedModel);
		}
		return bakedModel;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
		modelCache.clear();
		bakedCache.clear();
	}

}
