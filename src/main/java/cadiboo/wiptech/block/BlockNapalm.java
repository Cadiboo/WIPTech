package cadiboo.wiptech.block;

import java.util.Random;

import javax.annotation.Nullable;

import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNapalm extends Block {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);

	public static final PropertyBool	DOWN	= PropertyBool.create("down");
	public static final PropertyBool	UP		= PropertyBool.create("up");
	public static final PropertyBool	NORTH	= PropertyBool.create("north");
	public static final PropertyBool	SOUTH	= PropertyBool.create("south");
	public static final PropertyBool	WEST	= PropertyBool.create("west");
	public static final PropertyBool	EAST	= PropertyBool.create("east");

	public BlockNapalm(final String name) {
		super(Material.FIRE);
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
	public boolean isOpaqueCube(final IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(final IBlockState state) {
		return false;
	}

	@Override
	public int quantityDropped(final Random random) {
		return 0;
	}

	@Override
	public int tickRate(final World world) {
		return 1;
	}

	@Override
	public void updateTick(final World world, final BlockPos pos, IBlockState state, final Random rand) {
//		world.newExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 10, false, true);
//		world.setBlockToAir(pos);
		if (world.getGameRules().getBoolean("doFireTick")) {
			if (!world.isAreaLoaded(pos, 2)) {
				return; // Forge: prevent loading unloaded chunks when spreading fire
			}
			if (!this.canPlaceBlockAt(world, pos)) {
				world.setBlockToAir(pos);
			}

			final Block block = world.getBlockState(pos.down()).getBlock();
			final boolean flag = block.isFireSource(world, pos.down(), EnumFacing.UP);

			final int i = state.getValue(AGE);

			if (!flag && world.isRaining() && this.canDie(world, pos) && (rand.nextFloat() < (0.2F + (i * 0.03F)))) {
				world.setBlockToAir(pos);
			} else {
				if (i < 15) {
					state = state.withProperty(AGE, i + (rand.nextInt(3) / 2));
					world.setBlockState(pos, state, 4);
				}

				world.scheduleUpdate(pos, this, this.tickRate(world) + rand.nextInt(10));

				if (!flag) {
					if (!this.canNeighborCatchFire(world, pos)) {
						if (!world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP) || (i > 3)) {
							world.setBlockToAir(pos);
						}

						return;
					}

					if (!this.canCatchFire(world, pos.down(), EnumFacing.UP) && (i == 15) && (rand.nextInt(4) == 0)) {
						world.setBlockToAir(pos);
						return;
					}
				}

				final boolean flag1 = world.isBlockinHighHumidity(pos);
				int j = 0;

				if (flag1) {
					j = -50;
				}

				this.tryCatchFire(world, pos.east(), 300 + j, rand, i, EnumFacing.WEST);
				this.tryCatchFire(world, pos.west(), 300 + j, rand, i, EnumFacing.EAST);
				this.tryCatchFire(world, pos.down(), 250 + j, rand, i, EnumFacing.UP);
				this.tryCatchFire(world, pos.up(), 250 + j, rand, i, EnumFacing.DOWN);
				this.tryCatchFire(world, pos.north(), 300 + j, rand, i, EnumFacing.SOUTH);
				this.tryCatchFire(world, pos.south(), 300 + j, rand, i, EnumFacing.NORTH);

				for (int k = -1; k <= 1; ++k) {
					for (int l = -1; l <= 1; ++l) {
						for (int i1 = -1; i1 <= 4; ++i1) {
							if ((k != 0) || (i1 != 0) || (l != 0)) {
								int j1 = 100;

								if (i1 > 1) {
									j1 += (i1 - 1) * 100;
								}

								final BlockPos blockpos = pos.add(k, i1, l);
								final int k1 = this.getNeighborEncouragement(world, blockpos);

								if (k1 > 0) {
									int l1 = (k1 + 40 + (world.getDifficulty().getDifficultyId() * 7)) / (i + 30);

									if (flag1) {
										l1 /= 2;
									}

									if ((l1 > 0) && (rand.nextInt(j1) <= l1) && (!world.isRaining() || !this.canDie(world, blockpos))) {
										int i2 = i + (rand.nextInt(5) / 4);

										if (i2 > 15) {
											i2 = 15;
										}

										world.setBlockState(blockpos, state.withProperty(AGE, i2), 3);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	protected boolean canDie(final World world, final BlockPos pos) {
		return world.isRainingAt(pos) || world.isRainingAt(pos.west()) || world.isRainingAt(pos.east()) || world.isRainingAt(pos.north()) || world.isRainingAt(pos.south());
	}

	private void tryCatchFire(final World world, final BlockPos pos, final int chance, final Random random, final int age, final EnumFacing face) {
		final int i = world.getBlockState(pos).getBlock().getFlammability(world, pos, face);

		if (random.nextInt(chance) < i) {
			final IBlockState iblockstate = world.getBlockState(pos);

			if ((random.nextInt(age + 10) < 5) && !world.isRainingAt(pos)) {
				int j = age + (random.nextInt(5) / 4);

				if (j > 15) {
					j = 15;
				}

				world.setBlockState(pos, this.getDefaultState().withProperty(AGE, j), 3);
			} else {
				world.setBlockToAir(pos);
			}

			if (iblockstate.getBlock() == Blocks.TNT) {
				Blocks.TNT.onBlockDestroyedByPlayer(world, pos, iblockstate.withProperty(BlockTNT.EXPLODE, true));
			}
		}
	}

	private boolean canNeighborCatchFire(final World world, final BlockPos pos) {
		for (final EnumFacing enumfacing : EnumFacing.values()) {
			if (this.canCatchFire(world, pos.offset(enumfacing), enumfacing.getOpposite())) {
				return true;
			}
		}

		return false;
	}

	private boolean canCatchFire(final IBlockAccess world, final BlockPos pos, final EnumFacing facing) {

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
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
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
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess world, final BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(final IBlockState state, final World worldIn, final BlockPos pos) {
		return NULL_AABB;
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

	// TODO: remove in 1.13
	@Override
	public int getMetaFromState(final IBlockState state) {
		return 0;
	}

	// TODO: remove in 1.13
	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return this.getDefaultState();
	}

}
