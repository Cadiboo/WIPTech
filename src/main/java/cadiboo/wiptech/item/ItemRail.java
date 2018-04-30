package cadiboo.wiptech.item;

import cadiboo.wiptech.handler.EnumHandler.ConductiveMetals;

public class ItemRail extends ItemBase {

	private final ConductiveMetals metal;

	public ItemRail(String name, ConductiveMetals metalIn) {
		super(name);
		this.metal = metalIn;
	}

	public ConductiveMetals getMetal() {
		return metal;
	}

}
