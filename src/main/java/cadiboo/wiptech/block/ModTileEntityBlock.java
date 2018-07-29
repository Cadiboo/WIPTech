package cadiboo.wiptech.block;

import javax.annotation.Nullable;

import cadiboo.wiptech.tileentity.ModTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface ModTileEntityBlock<TE extends ModTileEntity> {

	public default TE getTileEntity(IBlockAccess world, BlockPos pos) {
		return (TE) world.getTileEntity(pos);
	}

	public default boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	public TE createTileEntity(World world, IBlockState state);

}
