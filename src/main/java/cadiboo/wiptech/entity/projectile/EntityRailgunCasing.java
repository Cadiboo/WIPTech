package cadiboo.wiptech.entity.projectile;

import cadiboo.wiptech.capability.ModEnergyStorage;
import cadiboo.wiptech.entity.ModEntity;
import cadiboo.wiptech.util.ModEnums.IEnumNameFormattable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.model.animation.AnimationStateMachine;
import net.minecraftforge.items.IItemHandler;

public class EntityRailgunCasing extends ModEntity {

    public enum CasingParts implements IEnumNameFormattable {

	BACK(0), TOP(1), BOTTOM(2);

	private int id;

	CasingParts(int idIn) {
	    this.id = idIn;
	}

	public int getId() {
	    return id;
	}

	public static CasingParts byId(int id) {
	    return values()[Math.min(Math.abs(id), values().length)];
	}

    }

    private CasingParts part;

    public EntityRailgunCasing(World worldIn) {
	this(worldIn, CasingParts.BACK);
    }

    public EntityRailgunCasing(World worldIn, CasingParts partIn) {
	super(worldIn);
	this.setSize(0.25f, 0.25f);
	this.part = partIn;
    }

    private CasingParts getPart() {
	return this.part;
    }

    @Override
    public ModEnergyStorage getEnergy() {
	return null;
    }

    @Override
    public AnimationStateMachine getAnimation() {
	return null;
    }

    @Override
    public IItemHandler getInventory() {
	return null;
    }

    @Override
    protected void entityInit() {
	// TODO Auto-generated method stub

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
	compound.setInteger("part", part.id);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
	this.part = CasingParts.byId(compound.getInteger("part"));

    }

    @Override
    public boolean canBeCollidedWith() {
	return !this.isDead;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
	return new AxisAlignedBB(0, 0, 0, width, height, width);
    }

}
