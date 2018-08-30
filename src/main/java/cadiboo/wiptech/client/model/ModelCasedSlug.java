package cadiboo.wiptech.client.model;

import java.util.ArrayList;
import java.util.function.Function;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModEnums.SlugCasingParts;
import cadiboo.wiptech.util.ModReference;
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

	public ModelCasedSlug(ModMaterials materialIn) {
		this.material = materialIn;
	}
//	
//	slug_casing_bottom.json
//	slug_casing_top.json
//	slug_casing_back.json

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		try {

			ArrayList<IBakedModel> bakedCasingModels = new ArrayList<IBakedModel>();

			for (SlugCasingParts part : SlugCasingParts.values()) {
				bakedCasingModels.add(part.getId(), ModelLoaderRegistry.getModel(new ModelResourceLocation(ModReference.Version.getModId(), "item/" + "slug_casing_" + part.getNameLowercase())).bake(
						state, format, bakedTextureGetter));
			}

			IBakedModel bakedModelSlug = ModelLoaderRegistry.getModel(new ModelResourceLocation(ModReference.Version.getModId(), "item/" + material.getNameLowercase() + "_slug")).bake(state, format,
					bakedTextureGetter);

			return new BakedModelCasedSlug(bakedModelSlug, bakedCasingModels.get(SlugCasingParts.BACK.getId()), bakedCasingModels.get(SlugCasingParts.TOP.getId()), bakedCasingModels.get(
					SlugCasingParts.BOTTOM.getId()));
		} catch (Exception exception) {
			WIPTech.error(this.getClass().getName() + ".bake() failed due to exception:" + exception);
			return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
		}
	}

}
