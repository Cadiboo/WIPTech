package cadiboo.wiptech.capability;

import java.util.List;

import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Capacitors;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Coils;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Rails;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Scopes;

public interface IWeaponModular {

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

	WeaponModular setCapacitor(Capacitors capacitor);

	Capacitors getCapacitor();

}
