package cadiboo.wiptech.capability;

import cadiboo.wiptech.handler.network.CSyncModular;
import cadiboo.wiptech.handler.network.PacketHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModularData {

	private EntityLivingBase entityLiving;

	private boolean flag = false;
	private int timer = 0;
	
	public ModularData(){
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

	public NBTBase writeData(){
		NBTTagCompound tag = new NBTTagCompound();
		
		return tag;
	}

	public void readData(NBTBase nbt){

	}

	public void initFreezeData() {
		if(entityLiving.world.isRemote)
			return;
		
		startToFreeze();
		
		entityLiving.prevLimbSwingAmount = entityLiving.limbSwingAmount;
		entityLiving.prevCameraPitch = entityLiving.cameraPitch;
		entityLiving.prevPosX = entityLiving.posX;
		entityLiving.prevPosY = entityLiving.posY;
		entityLiving.prevPosZ = entityLiving.posZ;
		entityLiving.prevSwingProgress = entityLiving.swingProgressInt;
		entityLiving.prevRenderYawOffset = entityLiving.renderYawOffset;
		entityLiving.prevRotationPitch = entityLiving.rotationPitch;
		entityLiving.prevRotationYaw = entityLiving.rotationYaw;
		entityLiving.prevRotationYawHead = entityLiving.rotationYawHead;
		
//		WorldDataHandler.get(entityLiving.world).addEntry(entityLiving.getUniqueID());
		
		//PacketHandler.NETWORK.sendToAll(new CSyncModular(true, entityLiving.getEntityId()));//, new TargetPoint(entityLiving.dimension, (int)entityLiving.posX, (int)entityLiving.posY, (int)entityLiving.posZ, 256));
	}

	public int getTimer(){
		return timer;
	}
	
	public void countDownTimer(){
		timer--;

		if(timer <= 0)
		{
			stopFreeze();
			//PacketHandler.NETWORK.sendToAll(new CSyncModular(false, entityLiving.getEntityId()));//, new TargetPoint(entityLiving.dimension, (int)entityLiving.posX, (int)entityLiving.posY, (int)entityLiving.posZ, 256));
		}
	}

	public void stopFreeze(){
		timer = 0;
		flag = false;
		entityLiving.updateBlocked = false;
	}
	
	public void startToFreeze(){
		flag = true;
		entityLiving.updateBlocked = true;
		timer = maxTimer();
	}
	
	public boolean shouldApplyFreeze() {
		return flag || getTimer() > 0;
	}
	
	public int maxTimer(){
		return 20*20;
	}
	
	@SideOnly(Side.CLIENT)
	/**method used in client packets to sync the flag boolean for entity renders.*/
	public void updateClientFlag(boolean flag){
		this.flag = flag;
	}
	
}