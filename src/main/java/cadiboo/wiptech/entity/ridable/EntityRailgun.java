package cadiboo.wiptech.entity.ridable;

import cadiboo.wiptech.entity.EntityBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;

public class EntityRailgun extends EntityBase implements IWorldNameable {

	private double railgunX;
	private double railgunY;
	private double railgunZ;
	private double railgunYaw;

	public EntityRailgun(World worldIn)
	{
		super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(1.8F, 0.4F);
    }

	protected boolean canTriggerWalking()
	{	
		return false;
	}
	
	public boolean canBeRidden()
    {
        return true;
    }

}
