package cadiboo.wiptech.tileentity;

import java.util.Random;

import net.minecraft.util.EnumFacing;

public class TileEntityEnamel extends TileEntityWire {

	@Override
	public boolean isConnectedTo(EnumFacing face) {
//		return true;
		// TODO Auto-generated method stub
		return new Random().nextBoolean();
	}

}
