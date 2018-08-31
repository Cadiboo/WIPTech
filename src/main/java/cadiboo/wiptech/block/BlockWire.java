package cadiboo.wiptech.block;

import java.util.List;

import cadiboo.wiptech.capability.energy.network.CapabilityEnergyNetworkList;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

public class BlockWire extends Block implements IBlockModMaterial {

	protected static final AxisAlignedBB CORE_AABB = new AxisAlignedBB(7d / 16d, 7d / 16d, 7d / 16d, 9d / 16d, 9d / 16d, 9d / 16d);
	protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(7d / 16d, 7d / 16d, 7d / 16d, 9d / 16d, 1, 9d / 16d);
	protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(7d / 16d, 0, 7d / 16d, 9d / 16d, 9d / 16d, 9d / 16d);
	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(7d / 16d, 7d / 16d, 0, 9d / 16d, 9d / 16d, 9d / 16d);
	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(7d / 16d, 7d / 16d, 7d / 16d, 9d / 16d, 9d / 16d, 1);
	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(7d / 16d, 7d / 16d, 7d / 16d, 1, 9d / 16d, 9d / 16d);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0, 7d / 16d, 7d / 16d, 9d / 16d, 9d / 16d, 9d / 16d);

	public static final IUnlistedProperty<Boolean> CONNECTED_DOWN = new Properties.PropertyAdapter<>(PropertyBool.create("connected_down"));
	public static final IUnlistedProperty<Boolean> CONNECTED_UP = new Properties.PropertyAdapter<>(PropertyBool.create("connected_up"));
	public static final IUnlistedProperty<Boolean> CONNECTED_NORTH = new Properties.PropertyAdapter<>(PropertyBool.create("connected_north"));
	public static final IUnlistedProperty<Boolean> CONNECTED_SOUTH = new Properties.PropertyAdapter<>(PropertyBool.create("connected_south"));
	public static final IUnlistedProperty<Boolean> CONNECTED_WEST = new Properties.PropertyAdapter<>(PropertyBool.create("connected_west"));
	public static final IUnlistedProperty<Boolean> CONNECTED_EAST = new Properties.PropertyAdapter<>(PropertyBool.create("connected_east"));

	protected final ModMaterials material;

	public BlockWire(final ModMaterials materialIn) {
		this(materialIn, "wire");
	}

	protected BlockWire(final ModMaterials materialIn, final String nameSuffix) {
		super(materialIn.getVanillaMaterial());
		ModUtil.setRegistryNames(this, materialIn, nameSuffix);
		this.material = materialIn;
	}

	@Override
	public final ModMaterials getModMaterial() {
		return this.material;
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
	public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
		final TileEntity tile = source.getTileEntity(pos);
		AxisAlignedBB AABB = CORE_AABB;
		if ((tile != null) && (tile instanceof TileEntityWire)) {
			final TileEntityWire wire = (TileEntityWire) tile;

			if (wire.isConnectedTo(EnumFacing.DOWN)) {
				AABB = AABB.union(DOWN_AABB);
			}
			if (wire.isConnectedTo(EnumFacing.UP)) {
				AABB = AABB.union(UP_AABB);
			}
			if (wire.isConnectedTo(EnumFacing.NORTH)) {
				AABB = AABB.union(NORTH_AABB);
			}
			if (wire.isConnectedTo(EnumFacing.SOUTH)) {
				AABB = AABB.union(SOUTH_AABB);
			}
			if (wire.isConnectedTo(EnumFacing.WEST)) {
				AABB = AABB.union(WEST_AABB);
			}
			if (wire.isConnectedTo(EnumFacing.EAST)) {
				AABB = AABB.union(EAST_AABB);
			}
		}
		return AABB;
	}

	@Override
	public EnumBlockRenderType getRenderType(final IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean hasTileEntity(final IBlockState state) {
		return true;
	}

	@Override
	public TileEntityWire createTileEntity(final World world, final IBlockState state) {
		return new TileEntityWire();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		final IProperty[] listedProperties = new IProperty[0]; /* no listed properties */
		final IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{CONNECTED_UP, CONNECTED_DOWN, CONNECTED_EAST, CONNECTED_WEST, CONNECTED_NORTH, CONNECTED_SOUTH};
		return new ExtendedBlockState(this, listedProperties, unlistedProperties);
	}

	@Override
	public IBlockState getExtendedState(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		if (!(state instanceof IExtendedBlockState) || (world.getTileEntity(pos) == null) || !(world.getTileEntity(pos) instanceof TileEntityWire)) {
			return state;
		}

		final TileEntityWire tile = (TileEntityWire) world.getTileEntity(pos);

		IExtendedBlockState retval = (IExtendedBlockState) state;

		retval = retval.withProperty(CONNECTED_DOWN, tile.isConnectedTo(EnumFacing.DOWN));
		retval = retval.withProperty(CONNECTED_UP, tile.isConnectedTo(EnumFacing.UP));
		retval = retval.withProperty(CONNECTED_NORTH, tile.isConnectedTo(EnumFacing.NORTH));
		retval = retval.withProperty(CONNECTED_SOUTH, tile.isConnectedTo(EnumFacing.SOUTH));
		retval = retval.withProperty(CONNECTED_WEST, tile.isConnectedTo(EnumFacing.WEST));
		retval = retval.withProperty(CONNECTED_EAST, tile.isConnectedTo(EnumFacing.EAST));

		return retval;
	}

	@Override
	public void addCollisionBoxToList(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, final Entity entityIn, final boolean isActualState) {
		final TileEntity tile = worldIn.getTileEntity(pos);
		this.addCollisionBoxToList(pos, entityBox, collidingBoxes, CORE_AABB);
		if ((tile != null) && (tile instanceof TileEntityWire)) {
			final TileEntityWire wire = (TileEntityWire) tile;

			if (wire.isConnectedTo(EnumFacing.DOWN)) {
				this.addCollisionBoxToList(pos, entityBox, collidingBoxes, DOWN_AABB);
			}
			if (wire.isConnectedTo(EnumFacing.UP)) {
				this.addCollisionBoxToList(pos, entityBox, collidingBoxes, UP_AABB);
			}
			if (wire.isConnectedTo(EnumFacing.NORTH)) {
				this.addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
			}
			if (wire.isConnectedTo(EnumFacing.SOUTH)) {
				this.addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
			}
			if (wire.isConnectedTo(EnumFacing.WEST)) {
				this.addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
			}
			if (wire.isConnectedTo(EnumFacing.EAST)) {
				this.addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
			}
		}

	}

	@Override
	public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
		if (worldIn != null) {
			final TileEntity tile = worldIn.getTileEntity(pos);
			if (tile != null) {
				if (tile instanceof TileEntityWire) {
					final TileEntityWire wire = (TileEntityWire) tile;
					worldIn.getCapability(CapabilityEnergyNetworkList.NETWORK_LIST, null).removeConnection(tile.getPos());
				}
			}
		}

		super.breakBlock(worldIn, pos, state);
	}
}
