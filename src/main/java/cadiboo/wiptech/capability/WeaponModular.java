package cadiboo.wiptech.capability;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.handler.EnumHandler;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Coils;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Rails;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Scopes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class WeaponModular implements IWeaponModular, INBTSerializable<NBTTagCompound>{

	private Circuits circuit;
	private Coils coil;
	private Scopes scope;
	private Rails rail;
	private int overheatTemperature;
	private int burstShotsTaken;
	private int shotsTaken;
	private boolean overheat;
	private long lastShootTime;
	private int temperature;
	
	public static final int overheatTimer = 5;
	public static final int burstShotsAllowed = 5;
	public static final float shootChance = 1.5F; //0 = never; 1 = always; 2 = half the time; 3 = 1/3rd the time
	public static final float burstShootChance = 2F;
	public static final int circuitOverclockedRepeats = 3;

	public WeaponModular() {
		this.overheatTemperature = 100;
		this.burstShotsTaken = 0;
		this.shotsTaken = 0;
		this.overheat = false;
		this.lastShootTime = 0;
		this.temperature = 25;
	}

	@Override
	public List <? extends EnumHandler.WeaponModules> getModuleList() {
		List modules = new ArrayList();

		if(this.circuit!=null)
			modules.add("circuit: "+this.circuit);
		if(this.coil!=null)
			modules.add("coil: "+this.coil);
		if(this.scope!=null)
			modules.add("scope: "+this.scope);
		if(this.rail!=null)
			modules.add("rail: "+this.rail);

		return modules;
	}

	@Override
	public int getBurstShotsTaken() {
		return this.burstShotsTaken;
	}
	
	@Override
	public void resetBurstShotsTaken() {
		this.burstShotsTaken = 0;
	}

	@Override
	public int getShotsTaken() {
		return this.shotsTaken;
	}

	@Override
	public boolean isOverheated() {
		return getTemperature()>getOverheatTemperature();
	}

	@Override
	public int getTemperature() {
		return this.temperature;
	}

	@Override
	public int getOverheatTemperature() {
		return this.overheatTemperature;
	}
	
	@Override
	public long getLastShootTime() {
		return this.lastShootTime;
	}
	
	@Override
	public void setLastShootTime(long time) {
		this.lastShootTime = time;
	}

	@Override
	public void incrementBurstShotsTaken() {
		this.burstShotsTaken++;
	}

	@Override
	public void incrementShotsTaken() {
		this.shotsTaken++;
	}

	@Override
	public void heat() {
		this.temperature++;
	}

	@Override
	public void cool() {
		if(this.getTemperature() > 0F)
			this.temperature--;
	}

	@Override
	public int getModules() {
		return getModuleList().size(); //TODO make this its own function and therefore less processor intensive 
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

	public WeaponModular setRail(Rails rail) {
		this.rail = rail;
		return this;
	}

	@Override
	public Rails getRail() {
		return this.rail;
	}


	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		
		WIPTech.logger.info("serializeNBT: ");
		WIPTech.logger.info(this.circuit);
		WIPTech.logger.info(this.coil);
		WIPTech.logger.info(this.scope);
		
		if(this.circuit!=null)
			nbt.setInteger("circuit", this.circuit.getID());
		if(this.coil!=null)
			nbt.setInteger("coil", this.coil.getID());
		if(this.scope!=null)
			nbt.setInteger("scope", this.scope.getID());
		if(this.rail!=null)
			nbt.setInteger("rail", this.rail.getID());

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		WIPTech.logger.info("deserializeNBT: "+nbt);
		if(nbt.hasKey("circuit"))
			this.circuit = Circuits.byID(nbt.getInteger("circuit"));
		if(nbt.hasKey("coil"))
			this.coil = Coils.byID(nbt.getInteger("coil"));
		if(nbt.hasKey("scope"))
			this.scope = Scopes.byID(nbt.getInteger("scope"));
		if(nbt.hasKey("rail"))
			this.rail = Rails.byID(nbt.getInteger("rail"));
		/*nbt.getTagTypeName(10).get
		NBTTagList tagList = nbt.getTagList("Modules", Constants.NBT.TAG_COMPOUND);
		WIPTech.logger.info(tagList);
		this.circuit = EnumHandler.WeaponModules.Circuits.byMetadata(tagList.getCompoundTagAt(0).getInteger("circuit"));
		WIPTech.logger.info(this.circuit);
		this.coil = null;
		this.scope = null;*/
	}

}
