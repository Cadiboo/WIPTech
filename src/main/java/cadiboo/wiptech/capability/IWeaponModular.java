package cadiboo.wiptech.capability;

import java.util.List;

import org.apache.logging.log4j.message.Message;

import cadiboo.wiptech.handler.EnumHandler.WeaponModules.*;

public interface IWeaponModular {

	/**
	 * Returns the number of modules on weapon
	 * @return 
	 *
	 * @return The number of modules on weapon
	 **/
	int getModules();

	WeaponModular setCoil(Coils coil);
	Coils getCoil();
	
	WeaponModular setRail(Rails rail);
	Rails getRail();

	WeaponModular setCircuit(Circuits circuit);
	Circuits getCircuit();

	WeaponModular setScope(Scopes scope);
	Scopes getScope();

	List getModuleList();
	
}
