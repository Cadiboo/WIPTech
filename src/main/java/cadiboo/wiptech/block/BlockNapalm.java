package cadiboo.wiptech.block;

import java.util.Random;

import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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

	/**
	 * Side sensitive version that calls the block function.
	 * @param world The current world
	 * @param pos   Block position
	 * @param face  The side the fire is coming from
	 * @return True if the face can catch fire.
	 */
	@Override
	public boolean canCatchFire(final IBlockAccess world, final BlockPos pos, final EnumFacing facing) {
		boolean canCatchFire = false;
		canCatchFire = canCatchFire | world.getBlockState(pos).isSideSolid(world, pos, facing);
		canCatchFire = canCatchFire | world.getBlockState(pos).getBlock().isFlammable(world, pos, facing);
		return canCatchFire;
	}

	/**
	 * returns if it is raining at or around the specified blockpos
	 */
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

	/**
	 * do logic like kill old fires & spawn new ones
	 */
	@Override
	public void updateTick(final World world, final BlockPos pos, IBlockState state, final Random rand) {

		// TODO add a gamerule to kill all napalm - napalmIsEnabled
		if (Boolean.valueOf("false")) {
			world.setBlockToAir(pos);
			return;
		}

		if (world.getGameRules().getBoolean("doFireTick")) {
			if (!world.isAreaLoaded(pos, 2)) {
				return; // Forge: prevent loading unloaded chunks when spreading fire
			}
			if (!this.canPlaceBlockAt(world, pos)) {
				world.setBlockToAir(pos);
			}

			final Block blockDown = world.getBlockState(pos.down()).getBlock();
			final boolean burnsForever = blockDown.isFireSource(world, pos.down(), EnumFacing.UP);

			final int age = state.getValue(AGE);

			if (!burnsForever && world.isRaining() && this.canDie(world, pos) && (rand.nextFloat() < (0.2F + (age * 0.03F)))) {
				world.setBlockToAir(pos);
			} else {
				if (age < 15) {
					if (rand.nextInt(5) == 0) {
						state = state.withProperty(AGE, age + 1);
						world.setBlockState(pos, state, 6);
					}
				} else {
					world.setBlockToAir(pos);
					return;
				}

				world.scheduleUpdate(pos, this, this.tickRate(world) + rand.nextInt(10));

				if (!burnsForever) {
					if (!this.canAnyNeighborCatchFire(world, pos) /* || (rand.nextInt(100) == 0) */) {
						if (age > 3) {
							world.setBlockToAir(pos);
							return;
						}
					}
				}

				final boolean isInHighHumidity = world.isBlockinHighHumidity(pos);
				final int spawnChance = isInHighHumidity ? -50 : 0;

				for (final EnumFacing facing : EnumFacing.VALUES) {
					final int spawnChanceBase = (facing == EnumFacing.DOWN) || (facing == EnumFacing.UP) ? 250 : 300;
					this.tryCatchFire(world, pos.offset(facing), spawnChanceBase + spawnChance, rand, age, facing.getOpposite());
				}

				for (int x = -2; x <= 2; ++x) {
					for (int z = -2; z <= 2; ++z) {
						for (int y = -1; y <= 4; ++y) {

							if ((x == 0) || (y == 0) || (z == 0)) {
								continue;
							}

							final int heightRandSeed = Math.max(100, (y - 1) * 100);

							final BlockPos blockpos = pos.add(x, y, z);
							final int encouragement = this.getNeighborEncouragement(world, blockpos);

							if (encouragement > 0) {
								int encouragementChance = (encouragement + 40 + (world.getDifficulty().getDifficultyId() * 7)) / (age + 30);

								if (isInHighHumidity) {
									encouragementChance /= 2;
								}

//								encouragementChance *= 10;

								if ((encouragementChance > 0) && (rand.nextInt(heightRandSeed) <= encouragementChance) && (!world.isRaining() || !this.canDie(world, blockpos))) {

									world.setBlockState(blockpos, state.withProperty(AGE, 0), 3);
								}

							}
						}
					}
				}
			}
		}
	}

	public void tryCatchFire(final World world, final BlockPos pos, final int chance, final Random random, final int age, final EnumFacing face) {

		final int flamability = world.getBlockState(pos).getBlock().getFlammability(world, pos, face);

		if ((random.nextInt(chance) < flamability)) {
			final IBlockState iblockstate = world.getBlockState(pos);

			if ((random.nextInt(age + 10) < 5) && !world.isRainingAt(pos)) {
				int newAge = age + (random.nextInt(5) / 4);

				if (newAge > 15) {
					newAge = 15;
				}

				world.setBlockState(pos, this.getDefaultState().withProperty(AGE, newAge), 3);
			} else {
//				world.setBlockToAir(pos);
			}

			if (iblockstate.getBlock() == Blocks.TNT) {
				Blocks.TNT.onBlockDestroyedByPlayer(world, pos, iblockstate.withProperty(BlockTNT.EXPLODE, true));
			}
		}
	}

	public boolean canAnyNeighborCatchFire(final World world, final BlockPos pos) {
		for (final EnumFacing enumfacing : EnumFacing.values()) {
			if (this.canCatchFire(world, pos.offset(enumfacing), enumfacing.getOpposite())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param world The current world
	 * @param pos   Block position in world
	 * @return the encouragement values of all the block's neighbours
	 */
	public int getNeighborEncouragement(final World world, final BlockPos pos) {
		if (!world.isAirBlock(pos)) {
			return 0;
		} else {
			int encouragement = 0;
			for (final EnumFacing enumfacing : EnumFacing.values()) {
				final int fireSpread = world.getBlockState(pos.offset(enumfacing)).getBlock().getFireSpreadSpeed(world, pos.offset(enumfacing), enumfacing.getOpposite());
				encouragement += fireSpread;
			}
			return encouragement;
		}
	}

	/**
	 * Returns if this block is collidable. Only used by fire, although stairs return that of the block that the stair is made of (though nobody's going to make fire stairs, right?)
	 */
	@Override
	public boolean isCollidable() {
		return true;
	}

	/**
	 * Checks if this block can be placed exactly at the given position.
	 */
	@Override
	public boolean canPlaceBlockAt(final World world, final BlockPos pos) {
		return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid block, etc.
	 */
	@Override
	public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
		if (!this.canAnyNeighborCatchFire(world, pos)) {
			world.setBlockToAir(pos);
		}
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile Entity is set
	 */
	@Override
	public void onBlockAdded(final World world, final BlockPos pos, final IBlockState state) {
		if ((world.provider.getDimensionType().getId() > 0) || !Blocks.PORTAL.trySpawnPortal(world, pos)) {
			if (!this.canAnyNeighborCatchFire(world, pos)) {
				world.setBlockToAir(pos);
			} else {
				world.scheduleUpdate(pos, this, this.tickRate(world) + world.rand.nextInt(10));
			}
		}
	}

	/**
	 * Location aware and overrideable version of the lightOpacity array, return the number to subtract from the light value when it passes through this block. This is not guaranteed to have the tile entity in place before this is called, so it is recommended that you have your tile entity call relight after being placed if you rely on it for light info.
	 * @param state The Block state
	 * @param world The current world
	 * @param pos   Block position in world
	 * @return The amount of light to block, 0 for air, 255 for fully opaque.
	 */
	@Override
	public int getLightOpacity(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return 0;
	}

	/**
	 * Get a light value for this block, taking into account the given state and coordinates, normal ranges are between 0 and 15
	 * @param state Block state
	 * @param world The current world
	 * @param pos   Block position in world
	 * @return The light value
	 */
	@Override
	public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return 15;
	}

	/**
	 * spawn fire particles
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(final IBlockState state, final World world, final BlockPos pos, final Random rand) {
		if (rand.nextInt(24) == 0) {
			world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), (rand.nextFloat() * 0.7F) + 0.3F, false);
		}

		for (final EnumFacing facing : EnumFacing.VALUES) {
			if (this.canCatchFire(world, pos, facing)) {
				for (int j = 0; j < 2; ++j) {
					final double x = pos.getX() + (rand.nextDouble() * 0.10000000149011612D);
					final double y = pos.getY() + rand.nextDouble();
					final double z = pos.getZ() + rand.nextDouble();
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y, z, 0.0D, 0.0D, 0.0D);
				}
			}
		}

		for (int i = 0; i < 3; ++i) {
			final double x = pos.getX() + rand.nextDouble();
			final double y = pos.getY() + (rand.nextDouble() * 0.5D) + 0.5D;
			final double z = pos.getZ() + rand.nextDouble();
			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y, z, 0.0D, 0.0D, 0.0D);
		}

	}

	/**
	 * Get the geometry of the queried face at the given position and state. This is used to decide whether things like buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
	 * <p>
	 * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED}, which represents something that does not fit the other descriptions and will generally cause other things not to connect to the face.
	 * @return an approximation of the form of the given face
	 */
	@Override
	public BlockFaceShape getBlockFaceShape(final IBlockAccess world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	/**
	 * Determines if this block should set fire and deal fire damage to entities coming into contact with it.
	 * @param world The current world
	 * @param pos   Block position in world
	 * @return True if the block should deal damage
	 */
	@Override
	public boolean isBurning(final IBlockAccess world, final BlockPos pos) {
		return true;
	}

}
