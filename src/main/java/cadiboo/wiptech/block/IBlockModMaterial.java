package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IBlockModMaterial {

	ModMaterials getModMaterial();

	int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos);
	int getLightOpacity(final IBlockState state, final IBlockAccess world, final BlockPos pos);
	boolean isFullCube(final IBlockState state);
	boolean isOpaqueCube(final IBlockState state);
}
