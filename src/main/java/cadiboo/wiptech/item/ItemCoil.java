package cadiboo.wiptech.item;

import cadiboo.wiptech.handler.EnumHandler.ConductiveMetals;

public class ItemCoil extends ItemBase {

	private ConductiveMetals metal;

	public ItemCoil(String name, ConductiveMetals metalIn) {
		super(name);
		this.metal = metalIn;
	}

}
