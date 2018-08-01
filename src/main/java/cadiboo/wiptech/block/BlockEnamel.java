package cadiboo.wiptech.block;

import cadiboo.wiptech.tileentity.TileEntityEnamel;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEnamel extends BlockWire {

	protected static final AxisAlignedBB	CORE_AABB	= BlockWire.CORE_AABB.grow(1d / 16d);
	protected static final AxisAlignedBB	UP_AABB		= BlockWire.UP_AABB.grow(1d / 16d, 0, 1d / 16d);
	protected static final AxisAlignedBB	DOWN_AABB	= BlockWire.DOWN_AABB.grow(1d / 16d, 0, 1d / 16d);
	protected static final AxisAlignedBB	NORTH_AABB	= BlockWire.NORTH_AABB.grow(1d / 16d, 1d / 16d, 0);
	protected static final AxisAlignedBB	SOUTH_AABB	= BlockWire.SOUTH_AABB.grow(1d / 16d, 1d / 16d, 0);
	protected static final AxisAlignedBB	EAST_AABB	= BlockWire.EAST_AABB.grow(0, 1d / 16d, 1d / 16d);
	protected static final AxisAlignedBB	WEST_AABB	= BlockWire.WEST_AABB.grow(0, 1d / 16d, 1d / 16d);

	public BlockEnamel(ModMaterials materialIn) {
		super(materialIn, "enamel");
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		TileEntity tile = source.getTileEntity(pos);
		AxisAlignedBB AABB = CORE_AABB;
		if (tile != null && tile instanceof TileEntityEnamel) {
			TileEntityEnamel enamel = (TileEntityEnamel) tile;

			if (enamel.isConnectedTo(EnumFacing.DOWN))
				AABB = AABB.union(DOWN_AABB);
			if (enamel.isConnectedTo(EnumFacing.UP))
				AABB = AABB.union(UP_AABB);
			if (enamel.isConnectedTo(EnumFacing.NORTH))
				AABB = AABB.union(NORTH_AABB);
			if (enamel.isConnectedTo(EnumFacing.SOUTH))
				AABB = AABB.union(SOUTH_AABB);
			if (enamel.isConnectedTo(EnumFacing.WEST))
				AABB = AABB.union(WEST_AABB);
			if (enamel.isConnectedTo(EnumFacing.EAST))
				AABB = AABB.union(EAST_AABB);
		}
		return AABB;
	}

	@Override
	public TileEntityEnamel createTileEntity(World world, IBlockState state) {
		return new TileEntityEnamel();
	}

}
