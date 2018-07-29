package cadiboo.wiptech.block;

import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWire extends ModMaterialBlock implements ModTileEntityBlock {

	private static final AxisAlignedBB	CORE_AABB	= new AxisAlignedBB(7d / 16d, 7d / 16d, 7d / 16d, 9d / 16d, 9d / 16d, 9d / 16d);
	private static final AxisAlignedBB	UP_AABB		= new AxisAlignedBB(7d / 16d, 7d / 16d, 7d / 16d, 9d / 16d, 1, 9d / 16d);
	private static final AxisAlignedBB	DOWN_AABB	= new AxisAlignedBB(7d / 16d, 0, 7d / 16d, 9d / 16d, 9d / 16d, 9d / 16d);
	private static final AxisAlignedBB	NORTH_AABB	= new AxisAlignedBB(7d / 16d, 7d / 16d, 0, 9d / 16d, 9d / 16d, 9d / 16d);
	private static final AxisAlignedBB	SOUTH_AABB	= new AxisAlignedBB(7d / 16d, 7d / 16d, 7d / 16d, 9d / 16d, 9d / 16d, 1);
	private static final AxisAlignedBB	EAST_AABB	= new AxisAlignedBB(7d / 16d, 7d / 16d, 7d / 16d, 1, 9d / 16d, 9d / 16d);
	private static final AxisAlignedBB	WEST_AABB	= new AxisAlignedBB(0, 7d / 16d, 7d / 16d, 9d / 16d, 9d / 16d, 9d / 16d);

	protected final ModMaterials material;

	public BlockWire(ModMaterials materialIn) {
		super(materialIn, "wire");
		this.material = materialIn;
	}

	@Override
	public final ModMaterials getModMaterial() {
		return this.material;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		TileEntity tile = source.getTileEntity(pos);
		AxisAlignedBB AABB = CORE_AABB;
		if (tile != null && tile instanceof TileEntityWire) {
			TileEntityWire wire = (TileEntityWire) tile;
			if (wire.isConnectedTo(EnumFacing.DOWN))
				AABB = AABB.union(DOWN_AABB);
			if (wire.isConnectedTo(EnumFacing.UP))
				AABB = AABB.union(UP_AABB);
			if (wire.isConnectedTo(EnumFacing.NORTH))
				AABB = AABB.union(NORTH_AABB);
			if (wire.isConnectedTo(EnumFacing.SOUTH))
				AABB = AABB.union(SOUTH_AABB);
			if (wire.isConnectedTo(EnumFacing.WEST))
				AABB = AABB.union(WEST_AABB);
			if (wire.isConnectedTo(EnumFacing.EAST))
				AABB = AABB.union(EAST_AABB);
		}
		return AABB;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public TileEntityWire createTileEntity(World world, IBlockState state) {
		return new TileEntityWire();
	}

}
