package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModEnums.BlockItemTypes;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
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

public class BlockItem extends Block implements IBlockModMaterial {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	private static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 0.2, 0.8);

	private static final AxisAlignedBB INGOT_EW_AABB = new AxisAlignedBB(3d / 16d, 0, 6d / 16d, 1d - (3d / 16d), 3d / 16d, 1d - (6d / 16d));
	private static final AxisAlignedBB INGOT_NS_AABB = new AxisAlignedBB(6d / 16d, 0, 3d / 16d, 1d - (6d / 16d), 3d / 16d, 1d - (3d / 16d));

	private static final AxisAlignedBB NUGGET_EW_AABB = new AxisAlignedBB(4d / 16d, 0, 5d / 16d, 1d - (4d / 16d), 1d / 16d, 1d - (5d / 16d));
	private static final AxisAlignedBB NUGGET_NS_AABB = new AxisAlignedBB(5d / 16d, 0, 4d / 16d, 1d - (5d / 16d), 1d / 16d, 1d - (4d / 16d));

	protected final ModMaterials material;
	protected final BlockItemTypes type;

	public BlockItem(final ModMaterials materialIn, final BlockItemTypes typeIn) {
		super(materialIn.getVanillaMaterial());
		ModUtil.setRegistryNames(this, materialIn, typeIn.getNameLowercase());
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
	public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return ModUtil.getMaterialLightValue(this.getModMaterial());
	}

	@Override
	public int getLightOpacity(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return ModUtil.getMaterialLightOpacity(this.material);
	}

	@Override
	public boolean isFullCube(final IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(final IBlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
		final EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();
		return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, enumfacing);
	}

	@Override
	public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {

		EnumFacing facing = state.getValue(FACING);
		if ((this.type == BlockItemTypes.NUGGET) & (this.material == ModMaterials.GOLD)) {
			facing = facing.rotateY();
		}

		switch (this.type) {

			case INGOT :
				switch (state.getValue(FACING)) {

					case EAST :
					case WEST :
						return INGOT_EW_AABB;

					case NORTH :
					case SOUTH :
						return INGOT_NS_AABB;

					default :
						return DEFAULT_AABB;
				}

			case NUGGET :
				switch (facing) {

					case EAST :
					case WEST :
						return NUGGET_EW_AABB;

					case NORTH :
					case SOUTH :
						return NUGGET_NS_AABB;

					default :
						return DEFAULT_AABB;
				}
			default :
				return DEFAULT_AABB;

		}

	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[]{FACING});
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

}
