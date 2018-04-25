package cadiboo.wiptech.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockStrongPistonExtension extends BlockDirectional {

	public static final PropertyEnum<BlockStrongPistonExtension.EnumPistonType>	TYPE	= PropertyEnum.<BlockStrongPistonExtension.EnumPistonType>create("type",
			BlockStrongPistonExtension.EnumPistonType.class);
	public static final PropertyBool											SHORT	= PropertyBool.create("short");

	protected static final AxisAlignedBB	PISTON_EXTENSION_EAST_AABB	= new AxisAlignedBB(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB	PISTON_EXTENSION_WEST_AABB	= new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.25D, 1.0D, 1.0D);
	protected static final AxisAlignedBB	PISTON_EXTENSION_SOUTH_AABB	= new AxisAlignedBB(0.0D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB	PISTON_EXTENSION_NORTH_AABB	= new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.25D);
	protected static final AxisAlignedBB	PISTON_EXTENSION_UP_AABB	= new AxisAlignedBB(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB	PISTON_EXTENSION_DOWN_AABB	= new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
	protected static final AxisAlignedBB	UP_ARM_AABB					= new AxisAlignedBB(0.375D, -0.25D, 0.375D, 0.625D, 0.75D, 0.625D);
	protected static final AxisAlignedBB	DOWN_ARM_AABB				= new AxisAlignedBB(0.375D, 0.25D, 0.375D, 0.625D, 1.25D, 0.625D);
	protected static final AxisAlignedBB	SOUTH_ARM_AABB				= new AxisAlignedBB(0.375D, 0.375D, -0.25D, 0.625D, 0.625D, 0.75D);
	protected static final AxisAlignedBB	NORTH_ARM_AABB				= new AxisAlignedBB(0.375D, 0.375D, 0.25D, 0.625D, 0.625D, 1.25D);
	protected static final AxisAlignedBB	EAST_ARM_AABB				= new AxisAlignedBB(-0.25D, 0.375D, 0.375D, 0.75D, 0.625D, 0.625D);
	protected static final AxisAlignedBB	WEST_ARM_AABB				= new AxisAlignedBB(0.25D, 0.375D, 0.375D, 1.25D, 0.625D, 0.625D);
	protected static final AxisAlignedBB	SHORT_UP_ARM_AABB			= new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.75D, 0.625D);
	protected static final AxisAlignedBB	SHORT_DOWN_ARM_AABB			= new AxisAlignedBB(0.375D, 0.25D, 0.375D, 0.625D, 1.0D, 0.625D);
	protected static final AxisAlignedBB	SHORT_SOUTH_ARM_AABB		= new AxisAlignedBB(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 0.75D);
	protected static final AxisAlignedBB	SHORT_NORTH_ARM_AABB		= new AxisAlignedBB(0.375D, 0.375D, 0.25D, 0.625D, 0.625D, 1.0D);
	protected static final AxisAlignedBB	SHORT_EAST_ARM_AABB			= new AxisAlignedBB(0.0D, 0.375D, 0.375D, 0.75D, 0.625D, 0.625D);
	protected static final AxisAlignedBB	SHORT_WEST_ARM_AABB			= new AxisAlignedBB(0.25D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D);

	public BlockStrongPistonExtension(String name) {
		super(Material.PISTON);
		this.setRegistryName(new ResourceLocation(Reference.ID, name));
		this.setUnlocalizedName(name);
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, BlockStrongPistonExtension.EnumPistonType.NORMAL).withProperty(SHORT, Boolean.valueOf(false)));
		this.setSoundType(SoundType.STONE);
		this.setHardness(0.5F);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(FACING)) {
			case DOWN:
			default:
				return PISTON_EXTENSION_DOWN_AABB;
			case UP:
				return PISTON_EXTENSION_UP_AABB;
			case NORTH:
				return PISTON_EXTENSION_NORTH_AABB;
			case SOUTH:
				return PISTON_EXTENSION_SOUTH_AABB;
			case WEST:
				return PISTON_EXTENSION_WEST_AABB;
			case EAST:
				return PISTON_EXTENSION_EAST_AABB;
		}
	}

	@Override
	public void addCollisionBoxToList(IBlockState a, World b, BlockPos c, AxisAlignedBB d, List<AxisAlignedBB> e, @Nullable Entity f, boolean g) {
		super.addCollisionBoxToList(a, b, c, d, e, f, g);
	}

	private AxisAlignedBB getArmShape(IBlockState state) {
		boolean flag = state.getValue(SHORT).booleanValue();

		switch (state.getValue(FACING)) {
			case DOWN:
			default:
				return flag ? SHORT_DOWN_ARM_AABB : DOWN_ARM_AABB;
			case UP:
				return flag ? SHORT_UP_ARM_AABB : UP_ARM_AABB;
			case NORTH:
				return flag ? SHORT_NORTH_ARM_AABB : NORTH_ARM_AABB;
			case SOUTH:
				return flag ? SHORT_SOUTH_ARM_AABB : SOUTH_ARM_AABB;
			case WEST:
				return flag ? SHORT_WEST_ARM_AABB : WEST_ARM_AABB;
			case EAST:
				return flag ? SHORT_EAST_ARM_AABB : EAST_ARM_AABB;
		}
	}

	/**
	 * Determines if the block is solid enough on the top side to support other
	 * blocks, like redstone components.
	 */
	@Override
	public boolean isTopSolid(IBlockState state) {
		return state.getValue(FACING) == EnumFacing.UP;
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (player.capabilities.isCreativeMode) {
			BlockPos blockpos = pos.offset(state.getValue(FACING).getOpposite());
			Block block = worldIn.getBlockState(blockpos).getBlock();

			if (block == Blocks.STRONG_PISTON || block == Blocks.STRONG_PISTON_STICKY) {
				worldIn.setBlockToAir(blockpos);
			}
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	/**
	 * Called serverside after this block is replaced with another in Chunk, but
	 * before the Tile Entity is updated
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		EnumFacing enumfacing = state.getValue(FACING).getOpposite();
		pos = pos.offset(enumfacing);
		IBlockState iblockstate = worldIn.getBlockState(pos);

		if ((iblockstate.getBlock() == Blocks.STRONG_PISTON || iblockstate.getBlock() == Blocks.STRONG_PISTON_STICKY) && iblockstate.getValue(BlockStrongPistonBase.EXTENDED).booleanValue()) {
			iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
			worldIn.setBlockToAir(pos);
		}
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for
	 * render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	/**
	 * Checks if this block can be placed exactly at the given position.
	 */
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return false;
	}

	/**
	 * Check whether this Block can be placed at pos, while aiming at the specified
	 * side of an adjacent block
	 */
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return false;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should
	 * perform any checks during a neighbor change. Cases may include when redstone
	 * power is updated, cactus blocks popping off due to a neighboring solid block,
	 * etc.
	 */
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		EnumFacing enumfacing = state.getValue(FACING);
		BlockPos blockpos = pos.offset(enumfacing.getOpposite());
		IBlockState iblockstate = worldIn.getBlockState(blockpos);

		if (iblockstate.getBlock() != Blocks.STRONG_PISTON && iblockstate.getBlock() != Blocks.STRONG_PISTON_STICKY) {
			worldIn.setBlockToAir(pos);
		} else {
			iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Nullable
	public static EnumFacing getFacing(int meta) {
		int i = meta & 7;
		return i > 5 ? null : EnumFacing.getFront(i);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(state.getValue(TYPE) == BlockStrongPistonExtension.EnumPistonType.STICKY ? Blocks.STRONG_PISTON_STICKY : Blocks.STRONG_PISTON);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(TYPE,
				(meta & 8) > 0 ? BlockStrongPistonExtension.EnumPistonType.STICKY : BlockStrongPistonExtension.EnumPistonType.NORMAL);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | state.getValue(FACING).getIndex();

		if (state.getValue(TYPE) == BlockStrongPistonExtension.EnumPistonType.STICKY) {
			i |= 8;
		}

		return i;
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, BlockStrongPistonExtension.TYPE, BlockStrongPistonExtension.SHORT });
	}

	/**
	 * Get the geometry of the queried face at the given position and state. This is
	 * used to decide whether things like buttons are allowed to be placed on the
	 * face, or how glass panes connect to the face, among other things.
	 * <p>
	 * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED},
	 * which represents something that does not fit the other descriptions and will
	 * generally cause other things not to connect to the face.
	 * 
	 * @return an approximation of the form of the given face
	 */
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return face == state.getValue(FACING) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	public static enum EnumPistonType implements IStringSerializable {
		NORMAL("normal"), STICKY("sticky");

		private final String VARIANT;

		private EnumPistonType(String name) {
			this.VARIANT = name;
		}

		@Override
		public String toString() {
			return this.VARIANT;
		}

		@Override
		public String getName() {
			return this.VARIANT;
		}
	}

}
