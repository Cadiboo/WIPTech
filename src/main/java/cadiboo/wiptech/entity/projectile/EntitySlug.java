package cadiboo.wiptech.entity.projectile;

import javax.annotation.Nullable;

import cadiboo.wiptech.capability.ModEnergyStorage;
import cadiboo.wiptech.entity.ModEntity;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.model.animation.AnimationStateMachine;
import net.minecraftforge.items.IItemHandler;

public class EntitySlug extends ModEntity {

	// FIXME do all this with capabilities or IEEPs. THe issue is that the material
	// isnt synced between client & server
	private ModMaterials material;
	private static final DataParameter<Integer> MATERIAL = EntityDataManager.<Integer>createKey(EntitySlug.class, DataSerializers.VARINT);

	public EntitySlug(World worldIn) {
		this(worldIn, ModMaterials.IRON);
	}

	public EntitySlug(World worldIn, ModMaterials materialIn) {
		super(worldIn);
//		this.material = materialIn;
		if (!world.isRemote)
			this.dataManager.set(MATERIAL, materialIn.getId());
		this.setSize(0.25f, 0.25f);
	}

	public ModMaterials getMaterial() {
//		return material;
		return ModMaterials.byId(this.dataManager.get(MATERIAL).intValue());

	}

	@Override
	public ModEnergyStorage getEnergy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnimationStateMachine getAnimation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IItemHandler getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void entityInit() {
//		this.dataManager.register(MATERIAL, material.getId());
		this.dataManager.register(MATERIAL, 0);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
//		if(compound.hasKey("material"))
//			this.material = ModMaterials.byId(compound.getInteger("material"));
//		getMaterial();

		if (compound.hasKey("material"))
			this.dataManager.set(MATERIAL, compound.getInteger("material"));

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
//		compound.setInteger("material", material.getId());
//		getMaterial();

		compound.setInteger("material", this.dataManager.get(MATERIAL).intValue());

	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub
		super.onUpdate();
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		this.motionX *= 0.95;
		this.motionY *= 0.95;
		this.motionZ *= 0.95;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getEntityBoundingBox();
	}

}
