package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * The same as a block of Iron or a block of Gold but for all materials
 * @author Cadiboo
 */
public class BlockResource extends Block implements IBlockModMaterial {

	protected final ModMaterials material;

	public BlockResource(final ModMaterials material) {
		super(material.getVanillaMaterial());
		ModUtil.setRegistryNames(this, material, "block");
		this.material = material;
	}

	@Override
	public final ModMaterials getModMaterial() {
		return this.material;
	}

	@Override
	public boolean isBeaconBase(final IBlockAccess worldObj, final BlockPos pos, final BlockPos beacon) {
		return true;
	}

	@Override
	public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return ModUtil.getMaterialLightValue(this.getModMaterial());
	}

	@Override
	public int getLightOpacity(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return ModUtil.getMaterialLightOpacity(this.getModMaterial());
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		switch (this.getModMaterial().getType()) {
			case GEM :
				return BlockRenderLayer.TRANSLUCENT;
			default :
			case METAL :
				return super.getBlockLayer();
		}
	}

	@Override
	public boolean isFullCube(final IBlockState state) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(final IBlockState state) {
		/* have to do this because isOpaqueCube is called in Block.<init> (before our material is set) */
		if (this.getModMaterial() == null) {
			return true;
		}
		switch (this.getModMaterial().getType()) {
			case GEM :
				return false;
			default :
			case METAL :
				return true;
		}
	}

}
