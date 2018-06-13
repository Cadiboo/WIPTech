package cadiboo.wiptech.block;

import cadiboo.wiptech.tileentity.TileEntityPeripheral;
import cadiboo.wiptech.util.Utils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPeripheralBlock extends BlockTileEntity<TileEntityPeripheral> {

	private static final AxisAlignedBB	TURBINE_BB			= new AxisAlignedBB(0, 0, 0, 1, 6, 1);
	private static final AxisAlignedBB	ASSEMBLY_TABLE_BB	= new AxisAlignedBB(-1, 0, -1, 2, 2, 2);

	public BlockPeripheralBlock(String name, Material materialIn) {
		super(name, materialIn);
		this.setHiddenBlock();
		this.setTransparentBlock();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		World w = worldIn;
		BlockPos p = pos;
		for (int i = 1; i <= 5; i++) {
			if (isTurbine(worldIn, pos.down(i)))
				return worldIn.getBlockState(pos.down(i)).getBlock().onBlockActivated(worldIn, pos.down(i), state, playerIn, hand, facing, hitX, hitY, hitZ);
		}
		for (int x = -1; x < 2; x++)
			for (int z = -1; z < 2; z++)
				for (int y = 0; y < 2; y++)
					if (isAssemblyTable(worldIn, pos.down(y).south(z).east(x)))
						return worldIn.getBlockState(pos.down(y).south(z).east(x)).getBlock().onBlockActivated(worldIn, pos.down(y).south(z).east(x), state, playerIn, hand, facing, hitX, hitY, hitZ);
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.getBlockState(this.getTileEntityPos(worldIn, pos)).getBlock().breakBlock(worldIn, this.getTileEntityPos(worldIn, pos), worldIn.getBlockState(this.getTileEntityPos(worldIn, pos)));
		return;
		// super.breakBlock(worldIn, pos, state);
	}

	public static boolean isTurbine(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).getBlock() instanceof BlockTurbine;
	}

	public static boolean isAssemblyTable(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).getBlock() instanceof BlockAssemblyTable;
	}

	public static boolean isPeripheral(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).getBlock() instanceof BlockPeripheralBlock;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	public BlockPos getTileEntityPos(World worldIn, BlockPos pos) {
		for (int i = 1; i <= 5; i++) {
			if (isTurbine(worldIn, pos.down(i)))
				return pos.down(i);
		}
		for (int x = -1; x < 2; x++)
			for (int z = -1; z < 2; z++)
				for (int y = 0; y < 2; y++)
					if (isAssemblyTable(worldIn, pos.down(y).south(z).east(x)))
						return pos.down(y).south(z).east(x);

		return new BlockPos(-1, -1, -1);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		BlockPos blockPos = getTileEntityPos(world, pos);
		if (blockPos != null)
			return Utils.getBlockFromPos(world, blockPos).getPickBlock(state, target, world, blockPos, player);
		return super.getPickBlock(state, target, world, pos, player);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		BlockPos pos2 = getTileEntityPos(worldIn, pos);
		if (isTurbine(worldIn, pos2))
			return TURBINE_BB.offset(pos2);
		else if (isAssemblyTable(worldIn, pos2))
			return ASSEMBLY_TABLE_BB.offset(pos2);
		return super.getSelectedBoundingBox(state, worldIn, pos);
	}

	@Override
	public TileEntityPeripheral createTileEntity(World world, IBlockState state) {
		return new TileEntityPeripheral();
	}

}
