package cadiboo.wiptech.capability.attachments.burst;

import cadiboo.wiptech.util.ModEnums.CircuitTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class CircuitBurstShots implements INBTSerializable<NBTTagCompound> {

	private CircuitTypes	circuit;
	private int				shotsTaken;

	public CircuitBurstShots(final CircuitTypes circuit) {

	}

	public boolean canShoot() {
		return this.getShotsTaken() > this.getMaxShots();
	}

	public int getMaxShots() {
		return this.circuit.getBurstShots();
	}

	public int getShotsTaken() {
		return this.shotsTaken;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deserializeNBT(final NBTTagCompound nbt) {
		// TODO Auto-generated method stub

	}

}
