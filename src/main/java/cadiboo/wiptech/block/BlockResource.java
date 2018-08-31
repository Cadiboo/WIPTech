package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockResource extends Block implements IBlockModMaterial {

	protected final ModMaterials material;

	public BlockResource(final ModMaterials materialIn) {
		super(materialIn.getVanillaMaterial());
		ModUtil.setRegistryNames(this, materialIn, "block");
		this.material = materialIn;
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
		return ModUtil.getMaterialLightOpacity(this.material);
	}

	@Override
	public boolean isFullCube(final IBlockState state) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(final IBlockState state) {
		return true;
	}

}
