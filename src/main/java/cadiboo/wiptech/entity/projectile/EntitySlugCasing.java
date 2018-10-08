package cadiboo.wiptech.entity.projectile;

import cadiboo.wiptech.entity.IModEntity;
import cadiboo.wiptech.util.ModEnums.SlugCasingPart;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntitySlugCasing extends Entity implements IModEntity {

	private SlugCasingPart part;

	public EntitySlugCasing(final World world) {
		this(world, SlugCasingPart.BACK);
	}

	public EntitySlugCasing(final World world, final SlugCasingPart part) {
		super(world);
		this.setSize(0.25f, 0.25f);
		this.part = part;
	}

	private SlugCasingPart getPart() {
		return this.part;
	}

	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void readEntityFromNBT(final NBTTagCompound compound) {
		compound.setInteger("part", this.part.getId());
	}

	@Override
	protected void writeEntityToNBT(final NBTTagCompound compound) {
		this.part = SlugCasingPart.byId(compound.getInteger("part"));

	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox() {
		return new AxisAlignedBB(0, 0, 0, this.width, this.height, this.width);
	}

}
