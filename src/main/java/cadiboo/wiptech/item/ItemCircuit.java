package cadiboo.wiptech.item;

import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;

public class ItemCircuit extends ItemBase {

	private final Circuits circuit;

	public ItemCircuit(String name, Circuits circuitIn) {
		super(name);
		this.circuit = circuitIn;
	}

	public Circuits getCircuit() {
		return circuit;
	}

}
