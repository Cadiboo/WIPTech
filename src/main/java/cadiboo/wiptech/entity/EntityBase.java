package cadiboo.wiptech.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityBase
  extends Entity
{
  public EntityBase(World worldIn)
  {
    super(worldIn);
  }
  
  protected void entityInit() {}
  
  protected void readEntityFromNBT(NBTTagCompound compound) {}
  
  protected void writeEntityToNBT(NBTTagCompound compound) {}
}
