package cadiboo.wiptech.capability;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.handler.EnumHandler;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Coils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

public class WeaponModular implements IWeaponModules, INBTSerializable<NBTTagCompound>{

	private EnumHandler.WeaponModules.Circuits circuit;
	private EnumHandler.WeaponModules.Coils coil;
	private EnumHandler.WeaponModules.Scopes scope;
	
	@Override
	public int getModules() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCoil(Coils coil) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Coils getCoil() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCircuit(Circuits circuit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Circuits getCircuit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagList nbtTagList = new NBTTagList();
		NBTTagCompound compund = new NBTTagCompound();

		compund.setInteger("circuit", this.circuit.getID());
		compund.setInteger("coil", this.coil.getID());
		compund.setInteger("scope", this.scope.getID());

		nbtTagList.appendTag(compund);

		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Modules", nbtTagList);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		WIPTech.logger.info(nbt);
		/*nbt.getTagTypeName(10).get
		NBTTagList tagList = nbt.getTagList("Modules", Constants.NBT.TAG_COMPOUND);
		WIPTech.logger.info(tagList);
		this.circuit = EnumHandler.WeaponModules.Circuits.byMetadata(tagList.getCompoundTagAt(0).getInteger("circuit"));
		WIPTech.logger.info(this.circuit);
		this.coil = null;
		this.scope = null;*/
	}

}
