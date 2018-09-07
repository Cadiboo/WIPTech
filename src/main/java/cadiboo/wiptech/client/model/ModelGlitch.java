package cadiboo.wiptech.client.model;

import java.util.function.Function;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.util.ModResourceLocation;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelGlitch implements IModel {

	private final ModResourceLocation	missingModel;
	private final ModResourceLocation	invisibleModel;

	public ModelGlitch(final ModResourceLocation missingModel, final ModResourceLocation invisibleModel) {
		this.missingModel = missingModel;
		this.invisibleModel = invisibleModel;
	}

	@Override
	public IBakedModel bake(final IModelState state, final VertexFormat format, final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		try {
			final IBakedModel bakedModelMissing = ModelsCache.INSTANCE.getBakedModel(this.missingModel, state, format, bakedTextureGetter);
			final IBakedModel bakedModelInvisible = ModelsCache.INSTANCE.getBakedModel(this.invisibleModel, state, format, bakedTextureGetter);

			return new BakedModelGlitch(bakedModelMissing, bakedModelInvisible);
		} catch (final Exception exception) {
			WIPTech.error(this.getClass().getName() + ".bake() failed due to exception:" + exception);
			return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
		}
	}

}
