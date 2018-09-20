package cadiboo.wiptech.block;

import java.util.Random;

import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.BlockFire;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNapalm extends BlockFire {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);

	public static final PropertyBool	DOWN	= PropertyBool.create("down");
	public static final PropertyBool	UP		= PropertyBool.create("up");
	public static final PropertyBool	NORTH	= PropertyBool.create("north");
	public static final PropertyBool	SOUTH	= PropertyBool.create("south");
	public static final PropertyBool	WEST	= PropertyBool.create("west");
	public static final PropertyBool	EAST	= PropertyBool.create("east");

	public BlockNapalm(final String name) {
		super();
		ModUtil.setRegistryNames(this, name);

		IBlockState defaultState = this.blockState.getBaseState().withProperty(AGE, 0);

		defaultState = defaultState.withProperty(DOWN, false);
		defaultState = defaultState.withProperty(UP, false);
		defaultState = defaultState.withProperty(NORTH, false);
		defaultState = defaultState.withProperty(SOUTH, false);
		defaultState = defaultState.withProperty(WEST, false);
		defaultState = defaultState.withProperty(EAST, false);

		this.setDefaultState(defaultState);
		this.setTickRandomly(true);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { AGE, DOWN, UP, NORTH, SOUTH, WEST, EAST });
	}

	@Override
	public IBlockState getActualState(final IBlockState state, final IBlockAccess world, final BlockPos pos) {

		IBlockState actualState = state;

		actualState = actualState.withProperty(DOWN, this.canCatchFire(world, pos.down(), EnumFacing.DOWN.getOpposite()));
		actualState = actualState.withProperty(UP, this.canCatchFire(world, pos.up(), EnumFacing.UP.getOpposite()));
		actualState = actualState.withProperty(NORTH, this.canCatchFire(world, pos.north(), EnumFacing.NORTH.getOpposite()));
		actualState = actualState.withProperty(SOUTH, this.canCatchFire(world, pos.south(), EnumFacing.SOUTH.getOpposite()));
		actualState = actualState.withProperty(WEST, this.canCatchFire(world, pos.west(), EnumFacing.WEST.getOpposite()));
		actualState = actualState.withProperty(EAST, this.canCatchFire(world, pos.east(), EnumFacing.EAST.getOpposite()));

		return actualState;
	}

	@Override
	protected boolean canDie(final World world, final BlockPos pos) {

		if (world.isRainingAt(pos)) {
			return true;
		}

		for (final EnumFacing facing : EnumFacing.values()) {
			if (world.isRainingAt(pos.offset(facing))) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canCatchFire(final IBlockAccess world, final BlockPos pos, final EnumFacing facing) {

		boolean canCatchFire = false;

		canCatchFire = canCatchFire | world.getBlockState(pos).isSideSolid(world, pos, facing);
		canCatchFire = canCatchFire | world.getBlockState(pos).getBlock().isFlammable(world, pos, facing);

		return canCatchFire;
	}

	private int getNeighborEncouragement(final World world, final BlockPos pos) {
		if (!world.isAirBlock(pos)) {
			return 0;
		} else {
			int i = 0;

			for (final EnumFacing enumfacing : EnumFacing.values()) {
				i += Math.max(world.getBlockState(pos.offset(enumfacing)).getBlock().getFireSpreadSpeed(world, pos.offset(enumfacing), enumfacing.getOpposite()), i);
			}

			return i;
		}
	}

	@Override
	public int getLightOpacity(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return 0;
	}

	@Override
	public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return 15;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(final IBlockState state, final World world, final BlockPos pos, final Random rand) {
		if (rand.nextInt(24) == 0) {
			world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), (rand.nextFloat() * 0.7F) + 0.3F, false);
		}

		for (final EnumFacing facing : EnumFacing.VALUES) {
			if (this.canCatchFire(world, pos, facing)) {
				for (int j = 0; j < 2; ++j) {
					final double d3 = pos.getX() + (rand.nextDouble() * 0.10000000149011612D);
					final double d8 = pos.getY() + rand.nextDouble();
					final double d13 = pos.getZ() + rand.nextDouble();
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d8, d13, 0.0D, 0.0D, 0.0D);
				}
			}
		}

		for (int i = 0; i < 3; ++i) {
			final double d0 = pos.getX() + rand.nextDouble();
			final double d1 = pos.getY() + (rand.nextDouble() * 0.5D) + 0.5D;
			final double d2 = pos.getZ() + rand.nextDouble();
			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}

	}

	@Override
	public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean isBurning(final IBlockAccess world, final BlockPos pos) {
		return true;
	}

}
