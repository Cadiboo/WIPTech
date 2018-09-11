package cadiboo.wiptech.block;

import java.util.HashSet;

import net.minecraft.util.math.BlockPos;

public interface IBlockCentral {

	default HashSet<BlockPos> getPeripheralPositions(final BlockPos pos) {
		final int smallX = (int) -(getWidth() / 2f);
		final int smallY = 0;
		final int smallZ = (int) -(getDepth() / 2f);

		final int bigX = (int) (getWidth() / 2f);
		final int bigY = getHeight() - 1;
		final int bigZ = (int) (getDepth() / 2f);

		final HashSet<BlockPos> peripheralPositions = new HashSet<>();

		final BlockPos from = new BlockPos(smallX, smallY, smallZ).add(pos);

		final BlockPos to = new BlockPos(bigX, bigY, bigZ).add(pos);

		BlockPos.getAllInBox(from, to).forEach(peripheralPos -> {
			if (!peripheralPos.equals(pos)) {
				peripheralPositions.add(peripheralPos);
			}
		});

		return peripheralPositions;
	}

	int getWidth();

	int getHeight();

	int getDepth();

}
