package cadiboo.wiptech.block;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.handler.EnumHandler.BlockItems;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockItem extends BlockBase {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	private static final AxisAlignedBB	DEFAULT_AABB	= new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.25D, 0.75D);
	private static final AxisAlignedBB	NS_AABB			= DEFAULT_AABB;
	private static final AxisAlignedBB	EW_AABB			= DEFAULT_AABB;

	private final BlockItems item;

	public BlockItem(String name, Material materialIn, BlockItems itemIn) {
		super(name, materialIn);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setBeaconBase();
		this.setTransparentBlock();
		this.item = itemIn;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		if (this == Blocks.IRON_INGOT)
			drops.add(new ItemStack(Items.IRON_INGOT));
		else if (this == Blocks.GOLD_INGOT)
			drops.add(new ItemStack(Items.GOLD_INGOT));
		else if (this == Blocks.IRON_NUGGET)
			drops.add(new ItemStack(Items.IRON_NUGGET));
		else if (this == Blocks.GOLD_NUGGET)
			drops.add(new ItemStack(Items.GOLD_NUGGET));
		else
			super.getDrops(drops, world, pos, state, fortune);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (item.getName().contains("nugget"))
			return DEFAULT_AABB;
		// switch (state.getValue(FACING)) {
		// case NORTH:
		// case SOUTH:
		// return NS_AABB;
		// case EAST:
		// case WEST:
		// return EW_AABB;
		// default:
		return DEFAULT_AABB;
		// }
	}

	private boolean canPlaceOn(World worldIn, BlockPos pos) {
		return Utils.getBlockFromPos(worldIn, pos) instanceof BlockAnvil && Utils.getBlockFromPos(worldIn, pos.up()).isReplaceable(worldIn, pos.up());
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		WIPTech.logger.info(Utils.getBlockFromPos(worldIn, pos.down()) instanceof BlockAnvil);
		return Utils.getBlockFromPos(worldIn, pos.down()) instanceof BlockAnvil;
	}

	public static Block getBlockToPlace(Item itemIn) {

		if (itemIn == Items.IRON_INGOT)
			return Blocks.IRON_INGOT;
		else if (itemIn == Items.GOLD_INGOT)
			return Blocks.GOLD_INGOT;
		else if (itemIn == Items.IRON_NUGGET)
			return Blocks.IRON_NUGGET;
		else if (itemIn == Items.GOLD_NUGGET)
			return Blocks.GOLD_NUGGET;
		else
			return Block.getBlockFromItem(itemIn);

	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, Utils.getStateFromPos(worldIn, pos.down()).getValue(FACING));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

}
