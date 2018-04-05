package cadiboo.wiptech.capability;

import java.util.List;

import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Coils;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Rails;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Scopes;

public interface IWeaponModular {

	/**
	 * Returns the number of modules on weapon
	 * 
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

	int getBurstShotsTaken();

	void incrementBurstShotsTaken();

	int getShotsTaken();

	void incrementShotsTaken();

	boolean isOverheated();

	int getTemperature();

	void setTemperature(int temperature);

	void heat();

	void cool();

	int getOverheatTemperature();

	long getLastShootTime();

	void resetBurstShotsTaken();

	void setLastShootTime(long totalWorldTime);

}
