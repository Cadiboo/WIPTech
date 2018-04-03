package cadiboo.wiptech.block;

import cadiboo.wiptech.WIPTech;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPeripheralBlock extends BlockBase {

	public BlockPeripheralBlock(String name, Material materialIn) {
		super(name, materialIn);
		this.setHiddenBlock();
		this.setBlockUnbreakable();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		World w = worldIn;
		BlockPos p = pos;
		for(int i=1; i<=5; i++) {
			if(isTurbine(worldIn, pos.down(i)))
				return worldIn.getBlockState(pos.down(i)).getBlock().onBlockActivated(worldIn, pos.down(i), state, playerIn, hand, facing, hitX, hitY, hitZ);	
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		for(int i=1; i<=5; i++) {
			if(isTurbine(worldIn, pos.down(i))) {
				worldIn.getBlockState(pos.down(i)).getBlock().breakBlock(worldIn, pos.down(i), worldIn.getBlockState(pos.down(i)));
				return;
			}
		}
		//super.breakBlock(worldIn, pos, state);
	}

	public static boolean isTurbine(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).getBlock() instanceof BlockTurbine;
	}

	public static boolean isPeripheral(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).getBlock() instanceof BlockPeripheralBlock;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}

}
