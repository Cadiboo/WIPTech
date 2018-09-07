package cadiboo.wiptech.client.model;

import java.util.List;
import java.util.Random;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BakedModelGlitch implements IBakedModel {

	private final IBakedModel modelMissing;
	private final IBakedModel modelInvisible;

	public BakedModelGlitch(final IBakedModel modelMissing, final IBakedModel modelInvisible) {
		this.modelMissing = modelMissing;
		this.modelInvisible = modelInvisible;
	}

	@Override
	public List<BakedQuad> getQuads(final IBlockState state, final EnumFacing side, final long rand) {
		return this.getModel(rand).getQuads(state, side, rand);
	}

	private IBakedModel getModel(final Long rand) {
		if (new Random(System.currentTimeMillis() + rand).nextInt(2) == 0) {
			return this.modelMissing;
		} else {
			return this.modelInvisible;
		}
	}

	private IBakedModel getModel() {
		return this.getModel(0l);
	}

	@Override
	public ItemOverrideList getOverrides() {
		return this.getModel().getOverrides();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return this.getModel().isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.getModel().isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return this.getModel().getParticleTexture();
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(final TransformType cameraTransformType) {
		return this.getModel().handlePerspective(cameraTransformType);
	}

}
