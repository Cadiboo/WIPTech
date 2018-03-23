package cadiboo.wiptech.capability.fail;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.handler.EnumHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModularData implements INBTSerializable<NBTBase>{

	private EntityLivingBase entityLiving;

	protected EnumHandler.WeaponModules.Circuits circuit;
	protected EnumHandler.WeaponModules.Coils coil;
	protected EnumHandler.WeaponModules.Scopes scope;

	public ModularData() {
		this.circuit = null;
		this.coil = null;
		this.scope = null;
	}

	public EntityLivingBase getEntity() { 
		return entityLiving; 
	}

	public void setPlayer(EntityLivingBase entity){
		this.entityLiving = entity;
	}

	public static ModularData get(EntityLivingBase living)
	{
		return living.getCapability(ModularDataCapability.CAPABILITY, null);
	}

	@Override
	public NBTBase serializeNBT()
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
	public void deserializeNBT(NBTBase nbt)
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