package cadiboo.wiptech.client.model;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.block.BlockWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BakedModelWire implements IBakedModel {

	private final IBakedModel modelCore;
	private final IBakedModel modelDown;
	private final IBakedModel modelUp;
	private final IBakedModel modelNorth;
	private final IBakedModel modelSouth;
	private final IBakedModel modelWest;
	private final IBakedModel modelEast;

	public BakedModelWire(final IBakedModel modelCoreIn, final IBakedModel modelDownIn, final IBakedModel modelUpIn, final IBakedModel modelNorthIn, final IBakedModel modelSouthIn,
			final IBakedModel modelWestIn, final IBakedModel modelEastIn) {

		modelCore = modelCoreIn;
		modelDown = modelDownIn;
		modelUp = modelUpIn;
		modelNorth = modelNorthIn;
		modelSouth = modelSouthIn;
		modelWest = modelWestIn;
		modelEast = modelEastIn;

	}

	@Override
	public List<BakedQuad> getQuads(final IBlockState state, final EnumFacing side, final long rand) {

		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		quads.addAll(modelCore.getQuads(state, side, rand));

		if (!(state instanceof IExtendedBlockState))
			return quads;

		IExtendedBlockState EBState = (IExtendedBlockState) state;

		if (isConnectedTo(EBState, BlockWire.CONNECTED_DOWN))
			quads.addAll(modelDown.getQuads(EBState, side, rand));
		if (isConnectedTo(EBState, BlockWire.CONNECTED_UP))
			quads.addAll(modelUp.getQuads(EBState, side, rand));
		if (isConnectedTo(EBState, BlockWire.CONNECTED_NORTH))
			quads.addAll(modelNorth.getQuads(EBState, side, rand));
		if (isConnectedTo(EBState, BlockWire.CONNECTED_SOUTH))
			quads.addAll(modelSouth.getQuads(EBState, side, rand));
		if (isConnectedTo(EBState, BlockWire.CONNECTED_WEST))
			quads.addAll(modelWest.getQuads(EBState, side, rand));
		if (isConnectedTo(EBState, BlockWire.CONNECTED_EAST))
			quads.addAll(modelEast.getQuads(EBState, side, rand));

		return quads;

	}

	private boolean isConnectedTo(final IExtendedBlockState state, final IUnlistedProperty<Boolean> side) {
		return state.getValue(side) == true;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(new ArrayList<ItemOverride>(0));
	}

	@Override
	public boolean isAmbientOcclusion() {
		return modelCore.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return modelCore.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return modelCore.getParticleTexture();
	}

}
