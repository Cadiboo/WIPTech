package cadiboo.wiptech.client.model;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BakedModelCasedSlug implements IBakedModel {

	private final IBakedModel modelSlug;
	private final IBakedModel modelCasingBack;
	private final IBakedModel modelCasingTop;
	private final IBakedModel modelCasingBottom;

	public BakedModelCasedSlug(final IBakedModel modelSlug, final IBakedModel modelCasingBack, final IBakedModel modelCasingTop, final IBakedModel modelCasingBottom) {
		this.modelSlug = modelSlug;
		this.modelCasingBack = modelCasingBack;
		this.modelCasingTop = modelCasingTop;
		this.modelCasingBottom = modelCasingBottom;
	}

	@Override
	public List<BakedQuad> getQuads(final IBlockState state, final EnumFacing side, final long rand) {

		final List<BakedQuad> quads = new ArrayList<>();
		quads.addAll(this.modelSlug.getQuads(state, side, rand));

		quads.addAll(this.modelCasingBack.getQuads(state, side, rand));
		quads.addAll(this.modelCasingTop.getQuads(state, side, rand));
		quads.addAll(this.modelCasingBottom.getQuads(state, side, rand));

		return quads;

	}

	@Override
	public boolean isAmbientOcclusion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGui3d() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		// TODO Auto-generated method stub
		return this.modelSlug.getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides() {
		// TODO Auto-generated method stub
		return this.modelSlug.getOverrides();
	}

}
