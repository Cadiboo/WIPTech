package cadiboo.wiptech.client.model;

import java.util.function.Function;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.util.ModReference;
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
public class ModelGlitchBlock implements IModel {

	public ModelGlitchBlock() {
	}

	@Override
	public IBakedModel bake(final IModelState state, final VertexFormat format, final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		try {
			final IBakedModel bakedModelMissing = ModelLoaderRegistry.getModel(new ModResourceLocation(ModReference.MOD_ID, "block/missing_block")).bake(state, format, bakedTextureGetter);
			final IBakedModel bakedModelInvisible = ModelLoaderRegistry.getModel(new ModResourceLocation(ModReference.MOD_ID, "block/invisible_block")).bake(state, format, bakedTextureGetter);

			return new BakedModelGlitchBlock(bakedModelMissing, bakedModelInvisible);
		} catch (final Exception exception) {
			WIPTech.error(this.getClass().getName() + ".bake() failed due to exception:" + exception);
			return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
		}
	}

}
