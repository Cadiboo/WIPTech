package cadiboo.wiptech.capability;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.handler.EnumHandler;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Capacitors;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Coils;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Rails;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Scopes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class WeaponModular implements IWeaponModular, INBTSerializable<NBTTagCompound> {

	private Circuits	circuit;
	private Coils		coil;
	private Scopes		scope;
	private Rails		rail;
	private Capacitors	capacitor;

	public WeaponModular() {
		this.circuit = Circuits.MANUAL;
		this.capacitor = Capacitors.TIN;
	}

	public WeaponModular(NBTTagCompound nbt) {
		this();
		deserializeNBT(nbt);
	}

	@Override
	public List<? extends EnumHandler.WeaponModules> getModuleList() {
		List modules = new ArrayList();

		if (this.circuit != null)
			modules.add("circuit: " + this.circuit);
		if (this.coil != null)
			modules.add("coil: " + this.coil);
		if (this.scope != null)
			modules.add("scope: " + this.scope);
		if (this.rail != null)
			modules.add("rail: " + this.rail);
		if (this.capacitor != null)
			modules.add("capacitor: " + this.capacitor);

		return modules;
	}

	@Override
	public int getModules() {
		return getModuleList().size();
	}

	@Override
	public WeaponModular setCircuit(Circuits circuit) {
		this.circuit = circuit;
		return this;
	}

	@Override
	public Circuits getCircuit() {
		return this.circuit;
	}

	@Override
	public WeaponModular setCoil(Coils coil) {
		this.coil = coil;
		return this;
	}

	@Override
	public Coils getCoil() {
		return this.coil;
	}

	@Override
	public WeaponModular setScope(Scopes scope) {
		this.scope = scope;
		return this;
	}

	@Override
	public Scopes getScope() {
		return this.scope;
	}

	@Override
	public WeaponModular setRail(Rails rail) {
		this.rail = rail;
		return this;
	}

	@Override
	public Rails getRail() {
		return this.rail;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		if (this.circuit != null)
			nbt.setInteger("circuit", this.circuit.getID());
		if (this.coil != null)
			nbt.setInteger("coil", this.coil.getID());
		if (this.scope != null)
			nbt.setInteger("scope", this.scope.getID());
		if (this.rail != null)
			nbt.setInteger("rail", this.rail.getID());
		if (this.capacitor != null)
			nbt.setInteger("capacitor", this.capacitor.getID());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt == null)
			return;
		if (nbt.hasKey("circuit"))
			this.circuit = Circuits.byID(nbt.getInteger("circuit"));
		if (nbt.hasKey("coil"))
			this.coil = Coils.byID(nbt.getInteger("coil"));
		if (nbt.hasKey("scope"))
			this.scope = Scopes.byID(nbt.getInteger("scope"));
		if (nbt.hasKey("rail"))
			this.rail = Rails.byID(nbt.getInteger("rail"));
		if (nbt.hasKey("capacitor"))
			this.capacitor = Capacitors.byID(nbt.getInteger("capacitor"));
	}

	@Override
	public WeaponModular setCapacitor(Capacitors capacitor) {
		this.capacitor = capacitor;
		return this;
	}

	@Override
	public Capacitors getCapacitor() {
		return capacitor;
	}

}
