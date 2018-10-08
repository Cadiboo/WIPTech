package cadiboo.wiptech.entity.projectile;

import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.entity.IModEntity;
import cadiboo.wiptech.util.ModEnums.ModMaterial;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntitySlug extends EntityThrowable implements IModEntity, IEntityAdditionalSpawnData {

	// FIXME do all this with capabilities or IEEPs. THe issue is that the material
	// isnt synced between client & server
	private ModMaterial						material;
	private static final DataParameter<Integer>	MATERIAL	= EntityDataManager.<Integer>createKey(EntitySlug.class, DataSerializers.VARINT);

	public EntitySlug(final World world) {
		this(world, ModMaterial.IRON);
	}

	public EntitySlug(final World world, final ModMaterial material) {
		super(world);
		// this.material = material;
		if (!world.isRemote) {
			this.dataManager.set(MATERIAL, material.getId());
		}
		this.setSize(0.25f, 0.25f);
	}

	public ModMaterial getMaterial() {
		// return material;
		return ModMaterial.byId(this.dataManager.get(MATERIAL).intValue());

	}

	@Override
	protected void entityInit() {
		// this.dataManager.register(MATERIAL, material.getId());
		this.dataManager.register(MATERIAL, 0);
	}

	@Override
	public void readEntityFromNBT(final NBTTagCompound compound) {
		// if(compound.hasKey("material"))
		// this.material = ModMaterials.byId(compound.getInteger("material"));
		// getMaterial();

		if (compound.hasKey("material")) {
			this.dataManager.set(MATERIAL, compound.getInteger("material"));
		}

	}

	@Override
	public void writeEntityToNBT(final NBTTagCompound compound) {
		// compound.setInteger("material", material.getId());
		// getMaterial();

		compound.setInteger("material", this.dataManager.get(MATERIAL).intValue());

	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub
		if (!this.hasNoGravity() && (this.ticksExisted < 20)) {
			this.motionY += this.getGravityVelocity() / 1.1f;
		}
		super.onUpdate();
	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	@Override
	public void onCollideWithPlayer(final EntityPlayer entity) {
		entity.attackEntityFrom(DamageSource.ANVIL, 1);
		super.onCollideWithPlayer(entity);
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getEntityBoundingBox();
	}

	@Override
	public void writeSpawnData(final ByteBuf buffer) {
		WIPTech.info("writeSpawnData", buffer);
	}

	@Override
	public void readSpawnData(final ByteBuf additionalData) {
		WIPTech.info("readSpawnData", additionalData);
	}

	@Override
	protected void onImpact(final RayTraceResult result) {
		if (this.ticksExisted > 5) {
			if (result.entityHit != this.getThrower()) {
				this.motionX = 0;
				this.motionY = 0;
				this.motionZ = 0;
			}
		}
	}

	public void setThrower(final EntityLivingBase entityThrower) {
		this.thrower = entityThrower;
	}

	@Override
	public boolean isInRangeToRenderDist(final double distance) {
		if (distance < 256) {
			return true;
		}
		return super.isInRangeToRenderDist(distance);
	}

	@Override
	public boolean isInRangeToRender3d(final double x, final double y, final double z) {
		// TODO Auto-generated method stub
		return super.isInRangeToRender3d(x, y, z);
	}

}
