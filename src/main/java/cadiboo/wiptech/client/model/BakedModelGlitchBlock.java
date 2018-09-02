package cadiboo.wiptech.client.model;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BakedModelGlitchBlock implements IBakedModel {

	private final IBakedModel modelMissing;
	private final IBakedModel modelInvisible;
	private long rand;

	public BakedModelGlitchBlock(final IBakedModel modelMissing, final IBakedModel modelInvisible) {
		this.modelMissing = modelMissing;
		this.modelInvisible = modelInvisible;
	}

	@Override
	public List<BakedQuad> getQuads(final IBlockState state, final EnumFacing side, final long rand) {
		this.rand = new Random().nextLong();
		if ((this.rand % 2) == 0) {
			return this.modelMissing.getQuads(state, side, rand);
		} else {
			return this.modelInvisible.getQuads(state, side, rand);
		}
	}

	@Override
	public ItemOverrideList getOverrides() {
		if ((this.rand % 2) == 0) {
			return this.modelMissing.getOverrides();
		} else {
			return this.modelInvisible.getOverrides();
		}
	}

	@Override
	public boolean isAmbientOcclusion() {
		if ((this.rand % 2) == 0) {
			return this.modelMissing.isAmbientOcclusion();
		} else {
			return this.modelInvisible.isAmbientOcclusion();
		}
	}

	@Override
	public boolean isGui3d() {
		if ((this.rand % 2) == 0) {
			return this.modelMissing.isGui3d();
		} else {
			return this.modelInvisible.isGui3d();
		}
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		if ((this.rand % 2) == 0) {
			return this.modelMissing.getParticleTexture();
		} else {
			return this.modelInvisible.getParticleTexture();
		}
	}

}
