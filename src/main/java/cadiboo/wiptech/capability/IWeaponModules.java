package cadiboo.wiptech.capability;

import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Coils;

public interface IWeaponModules {

	/**
	 * Returns the number of modules on weapon
	 * @return 
	 *
	 * @return The number of modules on weapon
	 **/
	int getModules();

	void setCoil(Coils coil);
	Coils getCoil();

	void setCircuit(Circuits circuit);
	Circuits getCircuit();
	
}
