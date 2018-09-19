package cadiboo.wiptech.capability.attachments.circuitdata;

import cadiboo.wiptech.util.ModEnums.CircuitTypes;
import cadiboo.wiptech.util.ModEnums.UsePhases;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class CircuitData implements INBTSerializable<NBTTagCompound> {

	public static final String SHOTS_TAKEN_TAG = "shotsTaken";

	private final CircuitTypes	circuit;
	private int					shotsTaken;
	private long				lastShotIncrementedTime;

	public CircuitData(final CircuitTypes circuit) {
		this.circuit = circuit;
	}

	public boolean canShoot(final UsePhases phase) {
		if (!this.circuit.getUsePhases().contains(phase)) {
			return false;
		}
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
		return this.circuit.getMaxShots();
	}

	public int getShotsTaken() {
		return this.shotsTaken;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger(SHOTS_TAKEN_TAG, this.shotsTaken);
		return compound;
	}

	@Override
	public void deserializeNBT(final NBTTagCompound compound) {
		if (compound.hasKey(SHOTS_TAKEN_TAG)) {
			this.shotsTaken = compound.getInteger(SHOTS_TAKEN_TAG);
		}
	}

}
