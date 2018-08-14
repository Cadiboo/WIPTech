package cadiboo.wiptech.entity.item;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import cadiboo.wiptech.capability.ModEnergyStorage;
import cadiboo.wiptech.entity.ModEntity;
import cadiboo.wiptech.entity.projectile.EntitySlug;
import cadiboo.wiptech.util.IEnergyTransferer;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
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
import net.minecraftforge.items.IItemHandler;

public class EntityRailgun extends ModEntity implements IWorldNameable, IEnergyTransferer {

	private ModEnergyStorage energy;
	private IItemHandler inventory;

	public EntityRailgun(World worldIn) {
		super(worldIn);
		this.energy = new ModEnergyStorage(1000000);
		this.inventory = new IItemHandler() {

			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ItemStack getStackInSlot(int slot) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getSlots() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getSlotLimit(int slot) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		this.setSize(3, 2);
	}

	@Override
	public IBlockAccess getWorld() {
		return this.world;
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
	public IItemHandler getInventory() {
		return inventory;
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(TIME_SINCE_HIT, Integer.valueOf(0));
		this.dataManager.register(FORWARD_DIRECTION, Integer.valueOf(1));
		this.dataManager.register(DAMAGE_TAKEN, Float.valueOf(0.0F));

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		// TODO Auto-generated method stub

		if (getPassengers().size() > 0) {

			if (world.isRemote)
				return EnumActionResult.SUCCESS;

			BlockPos blockpos = getPosition();
			EntitySlug slug = new EntitySlug(world, ModMaterials.TUNGSTEN_CARBITE);
			slug.setPosition(blockpos.getX() + 0.5, blockpos.getY() + 1.5, blockpos.getZ() + 0.5);

			slug.motionX += getForward().x;
			slug.motionY += getForward().y;
			slug.motionZ += getForward().z;

			world.spawnEntity(slug);

			return EnumActionResult.SUCCESS;
		}

		return super.applyPlayerInteraction(player, vec, hand);
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {
		if (!this.world.isRemote && !this.isDead) {
			this.getEnergy().receiveEnergy(10000, false);
		}
	}

	private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.<Integer>createKey(EntityBoat.class, DataSerializers.VARINT);
	private static final DataParameter<Float> DAMAGE_TAKEN = EntityDataManager.<Float>createKey(EntityBoat.class, DataSerializers.FLOAT);

	private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.<Integer>createKey(EntityBoat.class, DataSerializers.VARINT);

	private float deltaRotation;
	private int lerpSteps;
	private double lerpX;
	private double lerpY;
	private double lerpZ;
	private double lerpYaw;
	private double lerpPitch;
	private boolean leftInputDown;
	private boolean rightInputDown;
	private boolean forwardInputDown;
	private boolean backInputDown;
	private double waterLevel;

	private float boatGlide;
	private EntityBoat.Status status;
	private EntityBoat.Status previousStatus;
	private double lastYd;

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getEntityBoundingBox();
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	public double getMountedYOffset() {
		return -0.1D;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else if (!this.world.isRemote && !this.isDead) {
			if (source instanceof EntityDamageSourceIndirect && source.getTrueSource() != null && this.isPassenger(source.getTrueSource())) {
				return false;
			} else {
				this.setForwardDirection(-this.getForwardDirection());
				this.setTimeSinceHit(10);
				this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
				this.markVelocityChanged();
				boolean flag = source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer) source.getTrueSource()).capabilities.isCreativeMode;

				if (flag || this.getDamageTaken() > 40.0F) {
					if (!flag && this.world.getGameRules().getBoolean("doEntityDrops")) {
						this.dropItemWithOffset(Items.IRON_AXE, 1, 0.0F);
					}

					this.setDead();
				}

				return true;
			}
		} else {
			return true;
		}
	}

	@Override
	public void applyEntityCollision(Entity entityIn) {
		if (entityIn instanceof EntityBoat) {
			if (entityIn.getEntityBoundingBox().minY < this.getEntityBoundingBox().maxY) {
				super.applyEntityCollision(entityIn);
			}
		} else if (entityIn.getEntityBoundingBox().minY <= this.getEntityBoundingBox().minY) {
			super.applyEntityCollision(entityIn);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void performHurtAnimation() {
		this.setForwardDirection(-this.getForwardDirection());
		this.setTimeSinceHit(10);
		this.setDamageTaken(this.getDamageTaken() * 11.0F);
	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

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

	@Override
	public EnumFacing getAdjustedHorizontalFacing() {
		return this.getHorizontalFacing().rotateY();
	}

	@Override
	public void onUpdate() {
		this.previousStatus = this.status;
		this.status = this.getBoatStatus();

		if (this.getTimeSinceHit() > 0) {
			this.setTimeSinceHit(this.getTimeSinceHit() - 1);
		}

		if (this.getDamageTaken() > 0.0F) {
			this.setDamageTaken(this.getDamageTaken() - 1.0F);
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		super.onUpdate();
		this.tickLerp();

		if (this.canPassengerSteer()) {
			this.updateMotion();

			if (this.world.isRemote) {
				this.controlBoat();
			}

			this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		} else {
			this.motionX = 0.0D;
			this.motionY = 0.0D;
			this.motionZ = 0.0D;
		}

		this.doBlockCollisions();
		List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(0.20000000298023224D, -0.009999999776482582D, 0.20000000298023224D), EntitySelectors
				.getTeamCollisionPredicate(this));

		if (!list.isEmpty()) {
			boolean flag = !this.world.isRemote && !(this.getControllingPassenger() instanceof EntityPlayer);

			for (int j = 0; j < list.size(); ++j) {
				Entity entity = list.get(j);

				if (!entity.isPassenger(this)) {
					if (flag && this.getPassengers().size() < 10 && !entity.isRiding() && entity.width < this.width && entity instanceof EntityLivingBase && !(entity instanceof EntityWaterMob)
							&& !(entity instanceof EntityPlayer)) {
						entity.startRiding(this);
					} else {
						this.applyEntityCollision(entity);
					}
				}
			}
		}
	}

	@Nullable
	protected SoundEvent getPaddleSound() {
		switch (this.getBoatStatus()) {
		case IN_WATER:
		case UNDER_WATER:
		case UNDER_FLOWING_WATER:
			return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
		case ON_LAND:
			return SoundEvents.ENTITY_BOAT_PADDLE_LAND;
		case IN_AIR:
		default:
			return null;
		}
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

	private EntityBoat.Status getBoatStatus() {
		EntityBoat.Status entityboat$status = this.getUnderwaterStatus();

		if (entityboat$status != null) {
			this.waterLevel = this.getEntityBoundingBox().maxY;
			return entityboat$status;
		} else if (this.checkInWater()) {
			return EntityBoat.Status.IN_WATER;
		} else {
			float f = this.getBoatGlide();

			if (f > 0.0F) {
				this.boatGlide = f;
				return EntityBoat.Status.ON_LAND;
			} else {
				return EntityBoat.Status.IN_AIR;
			}
		}
	}

	public float getWaterLevelAbove() {
		AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
		int i = MathHelper.floor(axisalignedbb.minX);
		int j = MathHelper.ceil(axisalignedbb.maxX);
		int k = MathHelper.floor(axisalignedbb.maxY);
		int l = MathHelper.ceil(axisalignedbb.maxY - this.lastYd);
		int i1 = MathHelper.floor(axisalignedbb.minZ);
		int j1 = MathHelper.ceil(axisalignedbb.maxZ);
		BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

		try {
			label108:

			for (int k1 = k; k1 < l; ++k1) {
				float f = 0.0F;
				int l1 = i;

				while (true) {
					if (l1 >= j) {
						if (f < 1.0F) {
							float f2 = blockpos$pooledmutableblockpos.getY() + f;
							return f2;
						}

						break;
					}

					for (int i2 = i1; i2 < j1; ++i2) {
						blockpos$pooledmutableblockpos.setPos(l1, k1, i2);
						IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);

						if (iblockstate.getMaterial() == Material.WATER) {
							f = Math.max(f, BlockLiquid.getBlockLiquidHeight(iblockstate, this.world, blockpos$pooledmutableblockpos));
						}

						if (f >= 1.0F) {
							continue label108;
						}
					}

					++l1;
				}
			}

			float f1 = l + 1;
			return f1;
		} finally {
			blockpos$pooledmutableblockpos.release();
		}
	}

	public float getBoatGlide() {
		AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
		AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY - 0.001D, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		int i = MathHelper.floor(axisalignedbb1.minX) - 1;
		int j = MathHelper.ceil(axisalignedbb1.maxX) + 1;
		int k = MathHelper.floor(axisalignedbb1.minY) - 1;
		int l = MathHelper.ceil(axisalignedbb1.maxY) + 1;
		int i1 = MathHelper.floor(axisalignedbb1.minZ) - 1;
		int j1 = MathHelper.ceil(axisalignedbb1.maxZ) + 1;
		List<AxisAlignedBB> list = Lists.<AxisAlignedBB>newArrayList();
		float f = 0.0F;
		int k1 = 0;
		BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

		try {
			for (int l1 = i; l1 < j; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);

					if (j2 != 2) {
						for (int k2 = k; k2 < l; ++k2) {
							if (j2 <= 0 || k2 != k && k2 != l - 1) {
								blockpos$pooledmutableblockpos.setPos(l1, k2, i2);
								IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);
								iblockstate.addCollisionBoxToList(this.world, blockpos$pooledmutableblockpos, axisalignedbb1, list, this, false);

								if (!list.isEmpty()) {
									f += iblockstate.getBlock().getSlipperiness(iblockstate, this.world, blockpos$pooledmutableblockpos, this);
									++k1;
								}

								list.clear();
							}
						}
					}
				}
			}
		} finally {
			blockpos$pooledmutableblockpos.release();
		}

		return f / k1;
	}

	private boolean checkInWater() {
		AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
		int i = MathHelper.floor(axisalignedbb.minX);
		int j = MathHelper.ceil(axisalignedbb.maxX);
		int k = MathHelper.floor(axisalignedbb.minY);
		int l = MathHelper.ceil(axisalignedbb.minY + 0.001D);
		int i1 = MathHelper.floor(axisalignedbb.minZ);
		int j1 = MathHelper.ceil(axisalignedbb.maxZ);
		boolean flag = false;
		this.waterLevel = Double.MIN_VALUE;
		BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

		try {
			for (int k1 = i; k1 < j; ++k1) {
				for (int l1 = k; l1 < l; ++l1) {
					for (int i2 = i1; i2 < j1; ++i2) {
						blockpos$pooledmutableblockpos.setPos(k1, l1, i2);
						IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);

						if (iblockstate.getMaterial() == Material.WATER) {
							float f = BlockLiquid.getLiquidHeight(iblockstate, this.world, blockpos$pooledmutableblockpos);
							this.waterLevel = Math.max(f, this.waterLevel);
							flag |= axisalignedbb.minY < f;
						}
					}
				}
			}
		} finally {
			blockpos$pooledmutableblockpos.release();
		}

		return flag;
	}

	@Nullable
	private EntityBoat.Status getUnderwaterStatus() {
		AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
		double d0 = axisalignedbb.maxY + 0.001D;
		int i = MathHelper.floor(axisalignedbb.minX);
		int j = MathHelper.ceil(axisalignedbb.maxX);
		int k = MathHelper.floor(axisalignedbb.maxY);
		int l = MathHelper.ceil(d0);
		int i1 = MathHelper.floor(axisalignedbb.minZ);
		int j1 = MathHelper.ceil(axisalignedbb.maxZ);
		boolean flag = false;
		BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

		try {
			for (int k1 = i; k1 < j; ++k1) {
				for (int l1 = k; l1 < l; ++l1) {
					for (int i2 = i1; i2 < j1; ++i2) {
						blockpos$pooledmutableblockpos.setPos(k1, l1, i2);
						IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);

						if (iblockstate.getMaterial() == Material.WATER && d0 < BlockLiquid.getLiquidHeight(iblockstate, this.world, blockpos$pooledmutableblockpos)) {
							if (iblockstate.getValue(BlockLiquid.LEVEL).intValue() != 0) {
								EntityBoat.Status entityboat$status = EntityBoat.Status.UNDER_FLOWING_WATER;
								return entityboat$status;
							}

							flag = true;
						}
					}
				}
			}
		} finally {
			blockpos$pooledmutableblockpos.release();
		}

		return flag ? EntityBoat.Status.UNDER_WATER : null;
	}

	private void updateMotion() {
		double d0 = -0.03999999910593033D;
		double d1 = this.hasNoGravity() ? 0.0D : -0.03999999910593033D;
		double d2 = 0.0D;

		if (this.previousStatus == EntityBoat.Status.IN_AIR && this.status != EntityBoat.Status.IN_AIR && this.status != EntityBoat.Status.ON_LAND) {
			this.waterLevel = this.getEntityBoundingBox().minY + this.height;
			this.setPosition(this.posX, this.getWaterLevelAbove() - this.height + 0.101D, this.posZ);
			this.motionY = 0.0D;
			this.lastYd = 0.0D;
			this.status = EntityBoat.Status.IN_WATER;
		} else {
			if (this.status == EntityBoat.Status.IN_WATER) {
				d2 = (this.waterLevel - this.getEntityBoundingBox().minY) / this.height;
			} else if (this.status == EntityBoat.Status.UNDER_FLOWING_WATER) {
				d1 = -7.0E-4D;
			} else if (this.status == EntityBoat.Status.UNDER_WATER) {
				d2 = 0.009999999776482582D;
			} else if (this.status == EntityBoat.Status.IN_AIR) {
			} else if (this.status == EntityBoat.Status.ON_LAND) {

				if (this.getControllingPassenger() instanceof EntityPlayer) {
					this.boatGlide /= 2.0F;
				}
			}

			this.motionY += d1;

			if (d2 > 0.0D) {
				double d3 = 0.65D;
				this.motionY += d2 * 0.06153846016296973D;
				double d4 = 0.75D;
				this.motionY *= 0.75D;
			}
		}
	}

	private void controlBoat() {
		if (this.isBeingRidden()) {
			float f = 0.0F;

			if (this.leftInputDown) {
				this.deltaRotation += -1.0F;
			}

			if (this.rightInputDown) {
				++this.deltaRotation;
			}

			if (this.rightInputDown != this.leftInputDown && !this.forwardInputDown && !this.backInputDown) {
				f += 0.005F;
			}

			this.rotationYaw += this.deltaRotation;

			if (this.forwardInputDown) {
				f += 0.04F;
			}

			if (this.backInputDown) {
				f -= 0.005F;
			}

			this.motionX += MathHelper.sin(-this.rotationYaw * 0.017453292F) * f;
			this.motionZ += MathHelper.cos(this.rotationYaw * 0.017453292F) * f;
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
				int j = passenger.getEntityId() % 10 == 0 ? 90 : 270;
				passenger.setRenderYawOffset(((EntityAnimal) passenger).renderYawOffset + j);
				passenger.setRotationYawHead(passenger.getRotationYawHead() + j);
			}
		}
	}

	protected void applyYawToEntity(Entity entityToUpdate) {
		entityToUpdate.setRenderYawOffset(this.rotationYaw);
		float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
		float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
		entityToUpdate.prevRotationYaw += f1 - f;
		entityToUpdate.rotationYaw += f1 - f;
		entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void applyOrientationToEntity(Entity entityToUpdate) {
		this.applyYawToEntity(entityToUpdate);
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (player.isSneaking()) {
			return false;
		} else {
			if (!this.world.isRemote) {
				player.startRiding(this);
			}

			return true;
		}
	}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
		this.lastYd = this.motionY;

		if (!this.isRiding()) {
			if (onGroundIn) {
				if (this.fallDistance > 3.0F) {
					if (this.status != EntityBoat.Status.ON_LAND) {
						this.fallDistance = 0.0F;
						return;
					}

					this.fall(this.fallDistance, 1.0F);

					if (!this.world.isRemote && !this.isDead) {
						this.setDead();

						if (this.world.getGameRules().getBoolean("doEntityDrops")) {

							for (int j = 0; j < 2; ++j) {
								this.dropItemWithOffset(Items.STICK, 1, 0.0F);
							}
						}
					}
				}

				this.fallDistance = 0.0F;
			} else if (this.world.getBlockState((new BlockPos(this)).down()).getMaterial() != Material.WATER && y < 0.0D) {
				this.fallDistance = (float) (this.fallDistance - y);
			}
		}
	}

	public void setDamageTaken(float damageTaken) {
		this.dataManager.set(DAMAGE_TAKEN, Float.valueOf(damageTaken));
	}

	public float getDamageTaken() {
		return this.dataManager.get(DAMAGE_TAKEN).floatValue();
	}

	public void setTimeSinceHit(int timeSinceHit) {
		this.dataManager.set(TIME_SINCE_HIT, Integer.valueOf(timeSinceHit));
	}

	public int getTimeSinceHit() {
		return this.dataManager.get(TIME_SINCE_HIT).intValue();
	}

	public void setForwardDirection(int forwardDirection) {
		this.dataManager.set(FORWARD_DIRECTION, Integer.valueOf(forwardDirection));
	}

	public int getForwardDirection() {
		return this.dataManager.get(FORWARD_DIRECTION).intValue();
	}

	@Override
	protected boolean canFitPassenger(Entity passenger) {
		return this.getPassengers().size() < 2;
	}

	@Override
	@Nullable
	public Entity getControllingPassenger() {
		List<Entity> list = this.getPassengers();
		return list.isEmpty() ? null : (Entity) list.get(0);
	}

	@SideOnly(Side.CLIENT)
	public void updateInputs(boolean p_184442_1_, boolean p_184442_2_, boolean p_184442_3_, boolean p_184442_4_) {
		this.leftInputDown = p_184442_1_;
		this.rightInputDown = p_184442_2_;
		this.forwardInputDown = p_184442_3_;
		this.backInputDown = p_184442_4_;
	}

	public static enum Status {
		IN_WATER, UNDER_WATER, UNDER_FLOWING_WATER, ON_LAND, IN_AIR;
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
}
