package cadiboo.wiptech.block;

import cadiboo.wiptech.tileentity.TileEntityEnamel;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * The same as {@link cadiboo.wiptech.block.BlockWire BlockWire} except with a bigger bounding box and a {@link cadiboo.wiptech.tileentity.TileEntityEnamel TileEntityEnamel} instead of a {@link cadiboo.wiptech.tileentity.TileEntityWire TileEntityWire}
 * @author Cadiboo
 */
public class BlockEnamel extends BlockWire {

	protected static final AxisAlignedBB	CORE_AABB	= BlockWire.CORE_AABB.grow(1d / 16d);
	protected static final AxisAlignedBB	UP_AABB		= BlockWire.UP_AABB.grow(1d / 16d, 0, 1d / 16d);
	protected static final AxisAlignedBB	DOWN_AABB	= BlockWire.DOWN_AABB.grow(1d / 16d, 0, 1d / 16d);
	protected static final AxisAlignedBB	NORTH_AABB	= BlockWire.NORTH_AABB.grow(1d / 16d, 1d / 16d, 0);
	protected static final AxisAlignedBB	SOUTH_AABB	= BlockWire.SOUTH_AABB.grow(1d / 16d, 1d / 16d, 0);
	protected static final AxisAlignedBB	EAST_AABB	= BlockWire.EAST_AABB.grow(0, 1d / 16d, 1d / 16d);
	protected static final AxisAlignedBB	WEST_AABB	= BlockWire.WEST_AABB.grow(0, 1d / 16d, 1d / 16d);

	public BlockEnamel(final ModMaterials material) {
		super(material, "enamel");
	}

	@Override
	public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
		final TileEntity tile = source.getTileEntity(pos);
		AxisAlignedBB AABB = CORE_AABB;
		if ((tile != null) && (tile instanceof TileEntityEnamel)) {
			final TileEntityEnamel enamel = (TileEntityEnamel) tile;

			if (enamel.isConnectedTo(EnumFacing.DOWN)) {
				AABB = AABB.union(DOWN_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.UP)) {
				AABB = AABB.union(UP_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.NORTH)) {
				AABB = AABB.union(NORTH_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.SOUTH)) {
				AABB = AABB.union(SOUTH_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.WEST)) {
				AABB = AABB.union(WEST_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.EAST)) {
				AABB = AABB.union(EAST_AABB);
			}
		}
		return AABB;
	}

	@Override
	public TileEntityEnamel createTileEntity(final World world, final IBlockState state) {
		return new TileEntityEnamel();
	}

	/**
	 * Because enamel is covered, only have it emit at maximum a small amount of light
	 */
	@Override
	public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return Math.min(2, ModUtil.getMaterialLightValue(this.getModMaterial()));
	}

}
