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

		if (!this.canShootOnPhase(phase)) {
			return false;
		}

		final long check = System.currentTimeMillis() - this.lastShotIncrementedTime;

		if (this.getShotsTaken() >= this.getMaxShots()) {
			return false;
		}

		if (check < this.circuit.getShootInterval()) {
			return false;
		}

		return true;
	}

	public boolean canShootOnPhase(final UsePhases phase) {
		return this.circuit.getUsePhases().contains(phase);
	}

	public CircuitTypes getType() {
		return this.circuit;
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
