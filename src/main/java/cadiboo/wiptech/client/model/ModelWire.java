package cadiboo.wiptech.client.model;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Optional;
import java.util.function.Function;

import com.google.common.collect.ImmutableMap;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.animation.IClip;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModelWire implements IModel {

	protected ModMaterials material;

	public ModelWire(ModMaterials materialIn) {
		this.material = materialIn;
	}

	// Bake the subcomponents into a CompositeModel
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		try {
			IdentityHashMap<String, String> tempTextures = new IdentityHashMap<>();

			ResourceLocation materialIngotLocation = new ResourceLocation(material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS),
					"item" + (material.getResouceLocationDomain("ingot", ForgeRegistries.ITEMS) != "minecraft" || Loader.MC_VERSION.contains("1.13") ? "" : "s") + "/"
							+ material.getVanillaNameLowercase("ingot") + "_ingot");

			tempTextures.put("ingot", materialIngotLocation.toString());
			tempTextures.put("particle", materialIngotLocation.toString());

			ImmutableMap<String, String> textures = ImmutableMap.copyOf(tempTextures);

			IModel subComponent = ModelLoaderRegistry.getModel(getModelLocation("core")).retexture(textures);
			IBakedModel bakedModelCore = subComponent.bake(state, format, bakedTextureGetter);

			subComponent = ModelLoaderRegistry.getModel(getModelLocation("up")).retexture(textures);
			IBakedModel bakedModelUp = subComponent.bake(state, format, bakedTextureGetter);

			subComponent = ModelLoaderRegistry.getModel(getModelLocation("down")).retexture(textures);
			IBakedModel bakedModelDown = subComponent.bake(state, format, bakedTextureGetter);

			subComponent = ModelLoaderRegistry.getModel(getModelLocation("west")).retexture(textures);
			IBakedModel bakedModelWest = subComponent.bake(state, format, bakedTextureGetter);

			subComponent = ModelLoaderRegistry.getModel(getModelLocation("east")).retexture(textures);
			IBakedModel bakedModelEast = subComponent.bake(state, format, bakedTextureGetter);

			subComponent = ModelLoaderRegistry.getModel(getModelLocation("north")).retexture(textures);
			IBakedModel bakedModelNorth = subComponent.bake(state, format, bakedTextureGetter);

			subComponent = ModelLoaderRegistry.getModel(getModelLocation("south")).retexture(textures);
			IBakedModel bakedModelSouth = subComponent.bake(state, format, bakedTextureGetter);

			return new CompositeModel(bakedModelCore, bakedModelUp, bakedModelDown, bakedModelWest, bakedModelEast, bakedModelNorth, bakedModelSouth) {
				@Override
				public TextureAtlasSprite getParticleTexture() {
					TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(materialIngotLocation.toString());
					return textureAtlasSprite;
				}
			};
		} catch (Exception exception) {
			System.err.println(this.getClass().getName() + ".bake() failed due to exception:" + exception);
			return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
		}
	}

	protected ResourceLocation getModelLocation(String string) {
		if (string == "core")
			return new ResourceLocation(ModReference.ID, "block/wire_core");
		return new ResourceLocation(ModReference.ID, "block/wire_extension_" + string);
	}

	@Override
	public IModelState getDefaultState() {
		return IModel.super.getDefaultState();
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return IModel.super.getDependencies();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return IModel.super.getTextures();
	}

	@Override
	public Optional<? extends IClip> getClip(String name) {
		return IModel.super.getClip(name);
	}

	@Override
	public IModel gui3d(boolean value) {
		return IModel.super.gui3d(value);
	}

	@Override
	public IModel process(ImmutableMap<String, String> customData) {
		return IModel.super.process(customData);
	}

	@Override
	public IModel retexture(ImmutableMap<String, String> textures) {
		return IModel.super.retexture(textures);
	}

	@Override
	public IModel smoothLighting(boolean value) {
		return IModel.super.smoothLighting(value);
	}

	@Override
	public IModel uvlock(boolean value) {
		return IModel.super.uvlock(value);
	}

}
