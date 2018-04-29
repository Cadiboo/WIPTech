package cadiboo.wiptech.block;

import cadiboo.wiptech.util.Utils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPeripheralBlock extends BlockBase {

	public BlockPeripheralBlock(String name, Material materialIn) {
		super(name, materialIn);
		this.setHiddenBlock();
		this.setTransparentBlock();
		this.setBlockUnbreakable();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		World w = worldIn;
		BlockPos p = pos;
		for (int i = 1; i <= 5; i++) {
			if (isTurbine(worldIn, pos.down(i)))
				return worldIn.getBlockState(pos.down(i)).getBlock().onBlockActivated(worldIn, pos.down(i), state, playerIn, hand, facing, hitX, hitY, hitZ);
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		for (int i = 1; i <= 5; i++) {
			if (isTurbine(worldIn, pos.down(i))) {
				worldIn.getBlockState(pos.down(i)).getBlock().breakBlock(worldIn, pos.down(i), worldIn.getBlockState(pos.down(i)));
				return;
			}
		}
		// super.breakBlock(worldIn, pos, state);
	}

	public static boolean isTurbine(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).getBlock() instanceof BlockTurbine;
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
		return null;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		BlockPos blockPos = getTileEntityPos(world, pos);
		if (blockPos != null)
			return Utils.getBlockFromPos(world, blockPos).getPickBlock(state, target, world, blockPos, player);
		return super.getPickBlock(state, target, world, pos, player);
	}

}
