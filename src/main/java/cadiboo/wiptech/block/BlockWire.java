package cadiboo.wiptech.block;

import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
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

public class BlockWire extends ModMaterialBlock {

	protected static final AxisAlignedBB	CORE_AABB	= new AxisAlignedBB(7d / 16d, 7d / 16d, 7d / 16d, 9d / 16d, 9d / 16d, 9d / 16d);
	protected static final AxisAlignedBB	UP_AABB		= new AxisAlignedBB(7d / 16d, 7d / 16d, 7d / 16d, 9d / 16d, 1, 9d / 16d);
	protected static final AxisAlignedBB	DOWN_AABB	= new AxisAlignedBB(7d / 16d, 0, 7d / 16d, 9d / 16d, 9d / 16d, 9d / 16d);
	protected static final AxisAlignedBB	NORTH_AABB	= new AxisAlignedBB(7d / 16d, 7d / 16d, 0, 9d / 16d, 9d / 16d, 9d / 16d);
	protected static final AxisAlignedBB	SOUTH_AABB	= new AxisAlignedBB(7d / 16d, 7d / 16d, 7d / 16d, 9d / 16d, 9d / 16d, 1);
	protected static final AxisAlignedBB	EAST_AABB	= new AxisAlignedBB(7d / 16d, 7d / 16d, 7d / 16d, 1, 9d / 16d, 9d / 16d);
	protected static final AxisAlignedBB	WEST_AABB	= new AxisAlignedBB(0, 7d / 16d, 7d / 16d, 9d / 16d, 9d / 16d, 9d / 16d);

	public static final IUnlistedProperty<Boolean>	CONNECTION_DOWN		= new Properties.PropertyAdapter<Boolean>(PropertyBool.create("connection_down"));
	public static final IUnlistedProperty<Boolean>	CONNECTION_UP		= new Properties.PropertyAdapter<Boolean>(PropertyBool.create("connection_up"));
	public static final IUnlistedProperty<Boolean>	CONNECTION_NORTH	= new Properties.PropertyAdapter<Boolean>(PropertyBool.create("connection_north"));
	public static final IUnlistedProperty<Boolean>	CONNECTION_SOUTH	= new Properties.PropertyAdapter<Boolean>(PropertyBool.create("connection_south"));
	public static final IUnlistedProperty<Boolean>	CONNECTION_WEST		= new Properties.PropertyAdapter<Boolean>(PropertyBool.create("connection_west"));
	public static final IUnlistedProperty<Boolean>	CONNECTION_EAST		= new Properties.PropertyAdapter<Boolean>(PropertyBool.create("connection_east"));

	protected final ModMaterials material;

	public BlockWire(ModMaterials materialIn) {
		this(materialIn, "wire");
	}

	protected BlockWire(ModMaterials materialIn, String nameSuffix) {
		super(materialIn, nameSuffix);
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
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntityWire createTileEntity(World world, IBlockState state) {
		return new TileEntityWire();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		IProperty[] listedProperties = new IProperty[0]; // no listed properties
		IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { CONNECTION_UP, CONNECTION_DOWN, CONNECTION_EAST, CONNECTION_WEST, CONNECTION_NORTH, CONNECTION_SOUTH };
		return new ExtendedBlockState(this, listedProperties, unlistedProperties);
	}

	// this method uses the block state and BlockPos to update the unlisted
	// CONNECTION
	// properties in IExtendedBlockState based
	// on non-metadata information. This is then conveyed to the
	// IBakedModel#getQuads during rendering.
	// In this case, we look around the block to see which faces are next to either
	// a solid block or another web block:
	// The web node forms a strand of web to any adjacent solid blocks or web nodes
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (!(state instanceof IExtendedBlockState) || world.getTileEntity(pos) == null || !(world.getTileEntity(pos) instanceof TileEntityWire))
			return state;

		TileEntityWire tile = (TileEntityWire) world.getTileEntity(pos);

		IExtendedBlockState retval = (IExtendedBlockState) state;

		boolean linkdown = tile.isConnectedTo(EnumFacing.DOWN);
		retval = retval.withProperty(CONNECTION_DOWN, linkdown);

		boolean linkup = tile.isConnectedTo(EnumFacing.UP);
		retval = retval.withProperty(CONNECTION_UP, linkup);

		boolean linknorth = tile.isConnectedTo(EnumFacing.NORTH);
		retval = retval.withProperty(CONNECTION_NORTH, linknorth);

		boolean linksouth = tile.isConnectedTo(EnumFacing.SOUTH);
		retval = retval.withProperty(CONNECTION_SOUTH, linksouth);

		boolean linkwest = tile.isConnectedTo(EnumFacing.WEST);
		retval = retval.withProperty(CONNECTION_WEST, linkwest);

		boolean linkeast = tile.isConnectedTo(EnumFacing.EAST);
		retval = retval.withProperty(CONNECTION_EAST, linkeast);

		return retval;
	}

	protected boolean canConnectTo(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	// the LINK properties are used to communicate to the ISmartBlockModel which of
	// the links should be drawn

}
