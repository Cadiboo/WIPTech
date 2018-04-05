package cadiboo.wiptech.block;

import cadiboo.wiptech.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockNuggetBase extends BlockBase {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	private static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.25D, 0.75D);
	private static final AxisAlignedBB NS_AABB = DEFAULT_AABB;
	private static final AxisAlignedBB EW_AABB = DEFAULT_AABB;

	public BlockNuggetBase(String name) {
		super(name, Material.IRON);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setNuggetBlock();
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		drops.add(new ItemStack(getItemToDrop(this)));
	}

	public static Item getItemToDrop(Block block) {
		return ForgeRegistries.ITEMS.getValue(new ResourceLocation(Reference.ID, getItemName(block)));
	}

	public static String getItemName(Block block) {
		return block.getUnlocalizedName().replace("tile.", "").replace("_block", "");
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(FACING)) {
		case NORTH:
		case SOUTH:
			return NS_AABB;
		case EAST:
		case WEST:
			return EW_AABB;
		default:
			return DEFAULT_AABB;
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}
}
