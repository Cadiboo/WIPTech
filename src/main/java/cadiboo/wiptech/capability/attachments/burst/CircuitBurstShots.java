package cadiboo.wiptech.capability.attachments.burst;

import cadiboo.wiptech.util.ModEnums.CircuitTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class CircuitBurstShots implements INBTSerializable<NBTTagCompound> {

	private final CircuitTypes	circuit;
	private int					shotsTaken;
	private long				lastShotIncrementedTime;

	public CircuitBurstShots(final CircuitTypes circuit) {
		this.circuit = circuit;
	}

	public boolean canShoot() {
		final long check = System.currentTimeMillis() - this.lastShotIncrementedTime;
		return (this.getShotsTaken() < this.getMaxShots()) && (check > 75);
	}

	public void incrementShotsTaken() {
		this.shotsTaken++;
		this.lastShotIncrementedTime = System.currentTimeMillis();
	}

	public void resetShotsTaken() {
		this.shotsTaken = 0;
	}

	public int getMaxShots() {
		return this.circuit.getBurstShots();
	}

	public int getShotsTaken() {
		return this.shotsTaken;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound compound = new NBTTagCompound();

		return compound;
	}

	@Override
	public void deserializeNBT(final NBTTagCompound nbt) {
		// TODO Auto-generated method stub

	}

}
