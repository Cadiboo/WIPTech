package cadiboo.wiptech.util;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Utils {

	public static Block getBlockFromPos(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).getBlock();
	}

	public static IBlockState getStateFromPos(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos);
	}

	public static double randomBetween(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}

}
