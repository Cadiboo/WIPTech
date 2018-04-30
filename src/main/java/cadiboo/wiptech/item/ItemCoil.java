package cadiboo.wiptech.item;

import cadiboo.wiptech.handler.EnumHandler.ConductiveMetals;

public class ItemCoil extends ItemBase {

	private final ConductiveMetals metal;

	public ItemCoil(String name, ConductiveMetals metalIn) {
		super(name);
		this.metal = metalIn;
	}

	public ConductiveMetals getMetal() {
		return metal;
	}

}
