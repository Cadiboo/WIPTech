package cadiboo.wiptech.client.model;

import java.util.ArrayList;
import java.util.function.Function;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModEnums.SlugCasingParts;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModResourceLocation;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

public class ModelCasedSlug implements IModel {

	protected final ModMaterials material;

	public ModelCasedSlug(final ModMaterials material) {
		this.material = material;
	}

	@Override
	public IBakedModel bake(final IModelState state, final VertexFormat format, final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		try {

			final ArrayList<IBakedModel> bakedCasingModels = new ArrayList<>();

			for (final SlugCasingParts part : SlugCasingParts.values()) {
				bakedCasingModels.add(part.getId(), ModelLoaderRegistry.getModel(new ModelResourceLocation(new ModResourceLocation(ModReference.MOD_ID, "item/" + "slug_casing_" + part.getNameLowercase()).toString())).bake(state, format, bakedTextureGetter));
			}

			final IBakedModel bakedModelSlug = ModelLoaderRegistry.getModel(new ModelResourceLocation(ModReference.MOD_ID, "item/" + this.material.getNameLowercase() + "_slug")).bake(state, format, bakedTextureGetter);

			return new BakedModelCasedSlug(bakedModelSlug, bakedCasingModels.get(SlugCasingParts.BACK.getId()), bakedCasingModels.get(SlugCasingParts.TOP.getId()), bakedCasingModels.get(SlugCasingParts.BOTTOM.getId()));
		} catch (final Exception exception) {
			WIPTech.error(this.getClass().getName() + ".bake() failed due to exception:" + exception);
			return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
		}
	}

}
