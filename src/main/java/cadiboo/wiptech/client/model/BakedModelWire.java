package cadiboo.wiptech.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import cadiboo.wiptech.block.BlockWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BakedModelWire implements IBakedModel {

	private final IBakedModel modelCore;
	private final IBakedModel modelDown;
	private final IBakedModel modelUp;
	private final IBakedModel modelNorth;
	private final IBakedModel modelSouth;
	private final IBakedModel modelWest;
	private final IBakedModel modelEast;

	public BakedModelWire(final IBakedModel modelCore, final IBakedModel modelDown, final IBakedModel modelUp, final IBakedModel modelNorth, final IBakedModel modelSouth, final IBakedModel modelWest, final IBakedModel modelEast) {
		this.modelCore = modelCore;
		this.modelDown = modelDown;
		this.modelUp = modelUp;
		this.modelNorth = modelNorth;
		this.modelSouth = modelSouth;
		this.modelWest = modelWest;
		this.modelEast = modelEast;
	}

	@Override
	public List<BakedQuad> getQuads(final IBlockState state, final EnumFacing side, final long rand) {

		final List<BakedQuad> quads = new ArrayList<>();
		quads.addAll(this.modelCore.getQuads(state, side, rand));

		if (!(state instanceof IExtendedBlockState)) {
			return quads;
		}

		final IExtendedBlockState EBState = (IExtendedBlockState) state;

		if (this.isConnectedTo(EBState, BlockWire.CONNECTED_DOWN)) {
			quads.addAll(this.modelDown.getQuads(EBState, side, rand));
		}
		if (this.isConnectedTo(EBState, BlockWire.CONNECTED_UP)) {
			quads.addAll(this.modelUp.getQuads(EBState, side, rand));
		}
		if (this.isConnectedTo(EBState, BlockWire.CONNECTED_NORTH)) {
			quads.addAll(this.modelNorth.getQuads(EBState, side, rand));
		}
		if (this.isConnectedTo(EBState, BlockWire.CONNECTED_SOUTH)) {
			quads.addAll(this.modelSouth.getQuads(EBState, side, rand));
		}
		if (this.isConnectedTo(EBState, BlockWire.CONNECTED_WEST)) {
			quads.addAll(this.modelWest.getQuads(EBState, side, rand));
		}
		if (this.isConnectedTo(EBState, BlockWire.CONNECTED_EAST)) {
			quads.addAll(this.modelEast.getQuads(EBState, side, rand));
		}

		return quads;

	}

	private boolean isConnectedTo(final IExtendedBlockState state, final IUnlistedProperty<Boolean> side) {
		if ((state == null) || (side == null) || (state.getValue(side) == null)) {
			return false;
		}
		return state.getValue(side) == true;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(new ArrayList<ItemOverride>(0));
	}

	@Override
	public boolean isAmbientOcclusion() {
		return this.modelCore.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.modelCore.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return this.modelCore.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return this.modelCore.getItemCameraTransforms();
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(final TransformType cameraTransformType) {
		return this.modelCore.handlePerspective(cameraTransformType);
	}

}
