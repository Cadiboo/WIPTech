package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModEnums.BlockItemTypes;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockItem extends ModMaterialBlock {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	private static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 0.2, 0.8);

	private static final AxisAlignedBB	INGOT_EW_AABB	= new AxisAlignedBB(3d / 16d, 0, 6d / 16d, 1d - 3d / 16d, 3d / 16d, 1d - 6d / 16d);
	private static final AxisAlignedBB	INGOT_NS_AABB	= new AxisAlignedBB(6d / 16d, 0, 3d / 16d, 1d - 6d / 16d, 3d / 16d, 1d - 3d / 16d);

	private static final AxisAlignedBB	NUGGET_EW_AABB	= new AxisAlignedBB(4d / 16d, 0, 5d / 16d, 1d - 4d / 16d, 1d / 16d, 1d - 5d / 16d);
	private static final AxisAlignedBB	NUGGET_NS_AABB	= new AxisAlignedBB(5d / 16d, 0, 4d / 16d, 1d - 5d / 16d, 1d / 16d, 1d - 4d / 16d);

	protected final ModMaterials	material;
	protected final BlockItemTypes	type;

	public BlockItem(ModMaterials materialIn, BlockItemTypes typeIn) {
		super(materialIn, typeIn.getNameLowercase());
		this.material = materialIn;
		this.type = typeIn;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public final ModMaterials getModMaterial() {
		return this.material;
	}

	public final BlockItemTypes getType() {
		return this.type;
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
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();
		return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, enumfacing);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		EnumFacing facing = state.getValue(FACING);
		if (type == BlockItemTypes.NUGGET & material == ModMaterials.GOLD)
			facing = facing.rotateY();

		switch (type) {

			case INGOT:
				switch (state.getValue(FACING)) {

					case EAST:
					case WEST:
						return INGOT_EW_AABB;

					case NORTH:
					case SOUTH:
						return INGOT_NS_AABB;

					default:
						return DEFAULT_AABB;
				}

			case NUGGET:
				switch (facing) {

					case EAST:
					case WEST:
						return NUGGET_EW_AABB;

					case NORTH:
					case SOUTH:
						return NUGGET_NS_AABB;

					default:
						return DEFAULT_AABB;
				}
			default:
				return DEFAULT_AABB;

		}

	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

}
