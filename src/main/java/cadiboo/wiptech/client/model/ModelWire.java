package cadiboo.wiptech.client.model;

import java.util.ArrayList;
import java.util.function.Function;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelWire implements IModel {

	protected final ModMaterials material;

	public ModelWire(final ModMaterials material) {
		this.material = material;
	}

	@Override
	public IBakedModel bake(final IModelState state, final VertexFormat format, final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		try {

			final ArrayList<IBakedModel> bakedModels = new ArrayList<>();

			final IBakedModel bakedModelCore = ModelLoaderRegistry.getModel(this.getModelLocation("core")).bake(state, format, bakedTextureGetter);

			for (final EnumFacing connection : EnumFacing.VALUES) {
				bakedModels.add(ModelLoaderRegistry.getModel(this.getModelLocation(connection.name().toLowerCase())).bake(state, format, bakedTextureGetter));
			}

			return new BakedModelWire(bakedModelCore, bakedModels.get(EnumFacing.DOWN.getIndex()), bakedModels.get(EnumFacing.UP.getIndex()), bakedModels.get(EnumFacing.NORTH.getIndex()), bakedModels.get(EnumFacing.SOUTH.getIndex()), bakedModels.get(EnumFacing.WEST.getIndex()), bakedModels.get(EnumFacing.EAST.getIndex()));
		} catch (final Exception exception) {
			WIPTech.error(this.getClass().getName() + ".bake() failed due to exception:" + exception);
			return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
		}
	}

	protected ResourceLocation getModelLocation(final String string) {
		if (string == "core") {
			return new ResourceLocation(ModReference.MOD_ID, "block/" + this.material.getNameLowercase() + "_wire_core");
		}
		return new ResourceLocation(ModReference.MOD_ID, "block/" + this.material.getNameLowercase() + "_wire_extension_" + string);
	}

}
