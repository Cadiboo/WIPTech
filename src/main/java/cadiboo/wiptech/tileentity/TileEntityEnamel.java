package cadiboo.wiptech.tileentity;

import net.minecraft.util.EnumFacing;
import scala.util.Random;

public class TileEntityEnamel extends ModTileEntity {

	public boolean isConnectedTo(EnumFacing down) {
		// TODO Auto-generated method stub
		return new Random().nextBoolean();
	}

}
