package cadiboo.wiptech.block;

import java.util.List;

import cadiboo.wiptech.tileentity.TileEntityEnamel;
import cadiboo.wiptech.util.ModEnums.ModMaterial;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
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
public class BlockEnamel extends BlockWire implements IModBlock {

	private static final int	format__0	= 0;
	private static final int	format__1	= 1;

	public static final AxisAlignedBB	CORE_AABB	= new AxisAlignedBB(06d / 16d, 06d / 16d, 06d / 16d, 10d / 16d, 10d / 16d, 10d / 16d);
	public static final AxisAlignedBB	UP_AABB		= new AxisAlignedBB(06d / 16d, 06d / 16d, 06d / 16d, 10d / 16d, format__1, 10d / 16d);
	public static final AxisAlignedBB	DOWN_AABB	= new AxisAlignedBB(06d / 16d, format__0, 06d / 16d, 10d / 16d, 10d / 16d, 10d / 16d);
	public static final AxisAlignedBB	NORTH_AABB	= new AxisAlignedBB(06d / 16d, 06d / 16d, format__0, 10d / 16d, 10d / 16d, 10d / 16d);
	public static final AxisAlignedBB	SOUTH_AABB	= new AxisAlignedBB(06d / 16d, 06d / 16d, 06d / 16d, 10d / 16d, 10d / 16d, format__1);
	public static final AxisAlignedBB	EAST_AABB	= new AxisAlignedBB(06d / 16d, 06d / 16d, 06d / 16d, format__1, 10d / 16d, 10d / 16d);
	public static final AxisAlignedBB	WEST_AABB	= new AxisAlignedBB(format__0, 06d / 16d, 06d / 16d, 10d / 16d, 10d / 16d, 10d / 16d);

	public BlockEnamel(final ModMaterial material) {
		super(material, "enamel");
	}

	@Override
	public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
		final TileEntity tile = source.getTileEntity(pos);
		AxisAlignedBB AABB = this.CORE_AABB;
		if ((tile != null) && (tile instanceof TileEntityEnamel)) {
			final TileEntityEnamel enamel = (TileEntityEnamel) tile;

			if (enamel.isConnectedTo(EnumFacing.DOWN)) {
				AABB = AABB.union(this.DOWN_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.UP)) {
				AABB = AABB.union(this.UP_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.NORTH)) {
				AABB = AABB.union(this.NORTH_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.SOUTH)) {
				AABB = AABB.union(this.SOUTH_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.WEST)) {
				AABB = AABB.union(this.WEST_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.EAST)) {
				AABB = AABB.union(this.EAST_AABB);
			}
		}
		return AABB;
	}

	@Override
	public void addCollisionBoxToList(final IBlockState state, final World world, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, final Entity entity, final boolean isActualState) {
		final TileEntity tile = world.getTileEntity(pos);
		this.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.CORE_AABB);
		if ((tile != null) && (tile instanceof TileEntityEnamel)) {
			final TileEntityEnamel enamel = (TileEntityEnamel) tile;

			if (enamel.isConnectedTo(EnumFacing.DOWN)) {
				this.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.DOWN_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.UP)) {
				this.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.UP_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.NORTH)) {
				this.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.NORTH_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.SOUTH)) {
				this.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.SOUTH_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.WEST)) {
				this.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.WEST_AABB);
			}
			if (enamel.isConnectedTo(EnumFacing.EAST)) {
				this.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.EAST_AABB);
			}
		}

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
