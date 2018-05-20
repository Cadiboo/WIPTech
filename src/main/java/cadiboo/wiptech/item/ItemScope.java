package cadiboo.wiptech.item;

import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Scopes;

public class ItemScope extends ItemBase {

	private final Scopes scope;

	public ItemScope(String name, Scopes scopeIn) {
		super(name);
		this.scope = scopeIn;
	}

	public Scopes getScope() {
		return scope;
	}

}
