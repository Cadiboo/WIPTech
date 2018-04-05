package cadiboo.wiptech.block;

import cadiboo.wiptech.util.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
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
		this.setNuggetBlock();
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		drops.add(new ItemStack(this.getItemToDrop()));
	}

	private Item getItemToDrop() {
		return ForgeRegistries.ITEMS.getValue(new ResourceLocation(Reference.ID, this.getItemName()));
	}

	private String getItemName() {
		return this.getUnlocalizedName().replace("tile.", "").replace("_block", "");
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
}
