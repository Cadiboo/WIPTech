package cadiboo.wiptech.entity.item;

import java.util.List;

import javax.annotation.Nullable;

import cadiboo.wiptech.capability.ModEnergyStorage;
import cadiboo.wiptech.capability.ModItemStackHandler;
import cadiboo.wiptech.entity.ModEntity;
import cadiboo.wiptech.entity.projectile.EntitySlug;
import cadiboo.wiptech.init.ModItems;
import cadiboo.wiptech.util.IEnergyTransferer;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.block.BlockPlanks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.common.model.animation.AnimationStateMachine;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityRailgun extends ModEntity implements IWorldNameable, IEnergyTransferer {

	private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.<Integer>createKey(EntityRailgun.class, DataSerializers.VARINT);
	/** How much of current speed to retain. Value zero to one. */
	private float momentum;
	private float deltaRotation;
	private int lerpSteps;
	private double lerpX;
	private double lerpY;
	private double lerpZ;
	private double lerpYaw;
	private double lerpPitch;

	private ModEnergyStorage energy;
	private ModItemStackHandler inventory;

	public EntityRailgun(World worldIn) {
		super(worldIn);
		this.preventEntitySpawning = true;
		this.setSize(3, 2);

		this.energy = new ModEnergyStorage(100000);
		this.inventory = new ModItemStackHandler(1) {
			@Override
			public int getSlotLimit(int slot) {
				return 128;
			}
		};
	}

	public EntityRailgun(World worldIn, double x, double y, double z) {
		this(worldIn);
		this.setPosition(x, y, z);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk
	 * on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(FORWARD_DIRECTION, Integer.valueOf(1));
	}

	/**
	 * Returns a boundingBox used to collide the entity with other entities and
	 * blocks. This enables the entity to be pushable on contact, like boats or
	 * minecarts.
	 */
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.getEntityBoundingBox();
	}

	/**
	 * Returns the <b>solid</b> collision bounding box for this entity. Used to make
	 * (e.g.) boats solid. Return null if this entity is not solid.
	 * 
	 * For general purposes, use {@link #width} and {@link #height}.
	 * 
	 * @see getEntityBoundingBox
	 */
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getEntityBoundingBox();
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities when
	 * colliding.
	 */
	@Override
	public boolean canBePushed() {
		return false;
	}

	/**
	 * Returns the Y offset from the entity's position for any entity riding this
	 * one.
	 */
	@Override
	public double getMountedYOffset() {
		return 2.5;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else if (!this.world.isRemote && !this.isDead) {
			if (source instanceof EntityDamageSourceIndirect && source.getTrueSource() != null && this.isPassenger(source.getTrueSource())) {
				return false;
			} else {
				this.markVelocityChanged();
				boolean flag = source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer) source.getTrueSource()).capabilities.isCreativeMode;

				if (!flag && this.world.getGameRules().getBoolean("doEntityDrops")) {
					this.dropItemWithOffset(ModItems.RAILGUN, 1, 0.0F);
				}

				this.setDead();

				return true;
			}
		} else {
			return true;
		}
	}

	/**
	 * Applies a velocity to the entities, to push them away from eachother.
	 */
	@Override
	public void applyEntityCollision(Entity entityIn) {
		if (entityIn instanceof EntityRailgun) {
			if (entityIn.getEntityBoundingBox().minY < this.getEntityBoundingBox().maxY) {
				super.applyEntityCollision(entityIn);
			}
		} else if (entityIn.getEntityBoundingBox().minY <= this.getEntityBoundingBox().minY) {
			super.applyEntityCollision(entityIn);
		}
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this
	 * Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	/**
	 * Set the position and rotation values directly without any clamping.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
		this.lerpX = x;
		this.lerpY = y;
		this.lerpZ = z;
		this.lerpYaw = yaw;
		this.lerpPitch = pitch;
		this.lerpSteps = 10;
	}

	/**
	 * Gets the horizontal facing direction of this Entity, adjusted to take
	 * specially-treated entity types into account.
	 */
	@Override
	public EnumFacing getAdjustedHorizontalFacing() {
		return this.getHorizontalFacing().rotateY();
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		super.onUpdate();
		this.tickLerp();

		if (this.canPassengerSteer()) {
			this.updateMotion();

			if (this.world.isRemote) {
				this.controlRailgun();
			}

			this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		} else {
			this.motionX = 0.0D;
			this.motionY = 0.0D;
			this.motionZ = 0.0D;
		}

		this.doBlockCollisions();
	}

	private void tickLerp() {
		if (this.lerpSteps > 0 && !this.canPassengerSteer()) {
			double d0 = this.posX + (this.lerpX - this.posX) / this.lerpSteps;
			double d1 = this.posY + (this.lerpY - this.posY) / this.lerpSteps;
			double d2 = this.posZ + (this.lerpZ - this.posZ) / this.lerpSteps;
			double d3 = MathHelper.wrapDegrees(this.lerpYaw - this.rotationYaw);
			this.rotationYaw = (float) (this.rotationYaw + d3 / this.lerpSteps);
			this.rotationPitch = (float) (this.rotationPitch + (this.lerpPitch - this.rotationPitch) / this.lerpSteps);
			--this.lerpSteps;
			this.setPosition(d0, d1, d2);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}
	}

	/**
	 * Update the boat's speed, based on momentum.
	 */
	private void updateMotion() {
		double d0 = -0.03999999910593033D;
		double d1 = this.hasNoGravity() ? 0.0D : -0.03999999910593033D;
		double d2 = 0.0D;
		this.momentum = 0.05F;

		this.momentum = 0.9F;

		this.motionX *= this.momentum;
		this.motionZ *= this.momentum;
		this.deltaRotation *= this.momentum;
		this.motionY += d1;

		if (d2 > 0.0D) {
			double d3 = 0.65D;
			this.motionY += d2 * 0.06153846016296973D;
			double d4 = 0.75D;
			this.motionY *= 0.75D;
		}
	}

	private void controlRailgun() {
		if (this.isBeingRidden()) {

			this.rotationPitch = getControllingPassenger().rotationPitch;
			this.rotationYaw = getControllingPassenger().rotationYaw;

//			float f = 0.0F;
//
//			if (this.leftInputDown) {
//				this.deltaRotation += -1.0F;
//			}
//
//			if (this.rightInputDown) {
//				++this.deltaRotation;
//			}
//
//			if (this.rightInputDown != this.leftInputDown && !this.forwardInputDown && !this.backInputDown) {
//				f += 0.005F;
//			}
//
//			this.rotationYaw += this.deltaRotation;
//
//			if (this.forwardInputDown) {
//				f += 0.04F;
//			}
//
//			if (this.backInputDown) {
//				f -= 0.005F;
//			}
//
//			this.motionX += MathHelper.sin(-this.rotationYaw * 0.017453292F) * f;
//			this.motionZ += MathHelper.cos(this.rotationYaw * 0.017453292F) * f;
		}
	}

	@Override
	public void updatePassenger(Entity passenger) {
		if (this.isPassenger(passenger)) {
			float f = 0.0F;
			float f1 = (float) ((this.isDead ? 0.009999999776482582D : this.getMountedYOffset()) + passenger.getYOffset());

			if (this.getPassengers().size() > 1) {
				int i = this.getPassengers().indexOf(passenger);

				if (i == 0) {
					f = 0.2F;
				} else {
					f = -0.6F;
				}

				if (passenger instanceof EntityAnimal) {
					f = (float) (f + 0.2D);
				}
			}

			Vec3d vec3d = (new Vec3d(f, 0.0D, 0.0D)).rotateYaw(-this.rotationYaw * 0.017453292F - ((float) Math.PI / 2F));
			passenger.setPosition(this.posX + vec3d.x, this.posY + f1, this.posZ + vec3d.z);
			passenger.rotationYaw += this.deltaRotation;
			passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotation);
			this.applyYawToEntity(passenger);

			if (passenger instanceof EntityAnimal && this.getPassengers().size() > 1) {
				int j = passenger.getEntityId() % 2 == 0 ? 90 : 270;
				passenger.setRenderYawOffset(((EntityAnimal) passenger).renderYawOffset + j);
				passenger.setRotationYawHead(passenger.getRotationYawHead() + j);
			}
		}
	}

	/**
	 * Applies this boat's yaw to the given entity. Used to update the orientation
	 * of its passenger.
	 */
	protected void applyYawToEntity(Entity entityToUpdate) {
		entityToUpdate.setRenderYawOffset(this.rotationYaw);
		float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
		float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
		entityToUpdate.prevRotationYaw += f1 - f;
		entityToUpdate.rotationYaw += f1 - f;
		entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
	}

	/**
	 * Applies this entity's orientation (pitch/yaw) to another entity. Used to
	 * update passenger orientation.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void applyOrientationToEntity(Entity entityToUpdate) {
		this.applyYawToEntity(entityToUpdate);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (player.isSneaking()) {
			return false;
		} else {
			if (!this.world.isRemote) {
				player.startRiding(this);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {

		if (getControllingPassenger() != null && getControllingPassenger() instanceof EntityPlayer && player == getControllingPassenger()) {

			if (world.isRemote)
				return EnumActionResult.SUCCESS;

			float velocity = 3f;

			Vec3d look = this.getLookVec();

			BlockPos blockpos = getPosition();

			look.addVector(blockpos.getX(), blockpos.getY(), blockpos.getZ());

			EntitySlug slug = new EntitySlug(world, ModMaterials.TUNGSTEN_CARBITE);
			slug.setPosition(blockpos.getX() + 0.5, blockpos.getY() + 13.5, blockpos.getZ() + 0.5);

			slug.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity, 0.0F);

			world.spawnEntity(slug);

			return EnumActionResult.SUCCESS;
		} else {
			return super.applyPlayerInteraction(player, vec, hand);
		}
	}

	/**
	 * Sets the forward direction of the entity.
	 */
	public void setForwardDirection(int forwardDirection) {
		this.dataManager.set(FORWARD_DIRECTION, Integer.valueOf(forwardDirection));
	}

	/**
	 * Gets the forward direction of the entity.
	 */
	public int getForwardDirection() {
		return this.dataManager.get(FORWARD_DIRECTION).intValue();
	}

	@Override
	protected boolean canFitPassenger(Entity passenger) {
		return this.getPassengers().size() < 1;
	}

	/**
	 * For vehicles, the first passenger is generally considered the controller and
	 * "drives" the vehicle. For example, Pigs, Horses, and Boats are generally
	 * "steered" by the controlling passenger.
	 */
	@Override
	@Nullable
	public Entity getControllingPassenger() {
		List<Entity> list = this.getPassengers();
		return list.isEmpty() ? null : (Entity) list.get(0);
	}

	public static enum Type {
		OAK(BlockPlanks.EnumType.OAK.getMetadata(), "oak"), SPRUCE(BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce"), BIRCH(BlockPlanks.EnumType.BIRCH.getMetadata(), "birch"), JUNGLE(
				BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle"), ACACIA(BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia"), DARK_OAK(BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak");

		private final String name;
		private final int metadata;

		private Type(int metadataIn, String nameIn) {
			this.name = nameIn;
			this.metadata = metadataIn;
		}

		public String getName() {
			return this.name;
		}

		public int getMetadata() {
			return this.metadata;
		}

		@Override
		public String toString() {
			return this.name;
		}

		/**
		 * Get a boat type by it's enum ordinal
		 */
		public static EntityRailgun.Type byId(int id) {
			if (id < 0 || id >= values().length) {
				id = 0;
			}

			return values()[id];
		}

		public static EntityRailgun.Type getTypeFromString(String nameIn) {
			for (int i = 0; i < values().length; ++i) {
				if (values()[i].getName().equals(nameIn)) {
					return values()[i];
				}
			}

			return values()[0];
		}
	}

	// Forge: Fix MC-119811 by instantly completing lerp on board
	@Override
	protected void addPassenger(Entity passenger) {
		super.addPassenger(passenger);
		if (this.canPassengerSteer() && this.lerpSteps > 0) {
			this.lerpSteps = 0;
			this.posX = this.lerpX;
			this.posY = this.lerpY;
			this.posZ = this.lerpZ;
			this.rotationYaw = (float) this.lerpYaw;
			this.rotationPitch = (float) this.lerpPitch;
		}
	}

	@Override
	public IBlockAccess getWorld() {
		return world;
	}

	@Override
	public ModEnergyStorage getEnergy() {
		return energy;
	}

	@Override
	public AnimationStateMachine getAnimation() {
		return null;
	}

	@Override
	public ModItemStackHandler getInventory() {
		return inventory;
	}
}
