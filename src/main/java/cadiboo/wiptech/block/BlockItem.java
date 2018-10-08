package cadiboo.wiptech.block;

import cadiboo.wiptech.material.ModMaterial;
import cadiboo.wiptech.util.ModEnums.BlockItemType;
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

/**
 * A block to allow placement of ingots, nuggets, gems etc.
 * @author Cadiboo
 */
public class BlockItem extends Block implements IModBlock, IBlockModMaterial {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	protected final ModMaterial material;
	protected final BlockItemType type;

	public BlockItem(final ModMaterial material, final BlockItemType type) {
		super(material.getVanillaMaterial());
		ModUtil.setRegistryNames(this, material, type.getNameLowercase());
		this.material = material;
		this.type = type;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public final ModMaterial getModMaterial() {
		return this.material;
	}

	public final BlockItemType getType() {
		return this.type;
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
	public BlockFaceShape getBlockFaceShape(final IBlockAccess world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
		EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();
		if ((this.getType() == BlockItemType.NUGGET) & (this.material == ModMaterial.GOLD)) {
			enumfacing = enumfacing.rotateY();
		}
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, enumfacing);
	}

	@Override
	public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
		final EnumFacing facing = state.getValue(FACING);
		return this.getType().getBoundingBox(facing);
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