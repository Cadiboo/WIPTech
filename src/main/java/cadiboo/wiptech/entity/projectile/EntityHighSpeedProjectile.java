package cadiboo.wiptech.entity.projectile;

import java.math.BigDecimal;
import java.util.List;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityHighSpeedProjectile extends EntityThrowable {

	private BigDecimal	motionX	= new BigDecimal("0");
	private BigDecimal	motionY	= new BigDecimal("0");
	private BigDecimal	motionZ	= new BigDecimal("0");

	public EntityHighSpeedProjectile(final World world) {
		super(world);
	}

	public EntityHighSpeedProjectile(final World world, final double x, final double y, final double z) {
		super(world, x, y, z);
	}

	public EntityHighSpeedProjectile(final World world, final EntityLivingBase thrower) {
		super(world, thrower);
	}

	// START MOTION
	public BigDecimal getMotionX() {
		return this.motionX;
	}

	public void setMotionX(final BigDecimal motionX) {
		this.motionX = motionX;
	}

	public void setMotionX(final double motionX) {
		this.setMotionX(new BigDecimal(motionX));
	}

	public BigDecimal getMotionY() {
		return this.motionY;
	}

	public void setMotionY(final BigDecimal motionY) {
		this.motionY = motionY;
	}

	public void setMotionY(final double motionY) {
		this.setMotionY(new BigDecimal(motionY));
	}

	public BigDecimal getMotionZ() {
		return this.motionZ;
	}

	public void setMotionZ(final BigDecimal motionZ) {
		this.motionZ = motionZ;
	}

	public void setMotionZ(final double motionZ) {
		this.setMotionZ(new BigDecimal(motionZ));
	}

	public void setMotionToZero() {
		this.setMotionX(new BigDecimal("0"));
		this.setMotionY(new BigDecimal("0"));
		this.setMotionZ(new BigDecimal("0"));
	}

	// END MOTION

	@Override
	protected void preparePlayerToSpawn() {
		if (this.world != null) {
			while ((this.posY > 0.0D) && (this.posY < 256.0D)) {
				this.setPosition(this.posX, this.posY, this.posZ);

				if (this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
					break;
				}

				++this.posY;
			}

			this.setMotionToZero();
			this.rotationPitch = 0.0F;
		}
	}

	@Override
	public void move(final MoverType type, final double x, final double y, final double z) {
		if (!this.noClip && this.isInWeb) {
			this.setMotionToZero();
		}
		super.move(type, x, y, z);
	}

	@Override
	public void moveRelative(float strafe, float up, float forward, final float friction) {
		float f = (strafe * strafe) + (up * up) + (forward * forward);

		if (f >= 1.0E-4F) {
			f = MathHelper.sqrt(f);

			if (f < 1.0F) {
				f = 1.0F;
			}

			f = friction / f;
			strafe = strafe * f;
			up = up * f;
			forward = forward * f;
			final float f1 = MathHelper.sin(this.rotationYaw * 0.017453292F);
			final float f2 = MathHelper.cos(this.rotationYaw * 0.017453292F);

			final double motionXd = (strafe * f2) - (forward * f1);
			final double motionYd = up;
			final double motionZd = (forward * f2) + (strafe * f1);

			final BigDecimal motionX = this.getMotionX().add(new BigDecimal(motionXd));
			this.setMotionX(motionX);

			final BigDecimal motionY = this.getMotionY().add(new BigDecimal(motionYd));
			this.setMotionY(motionY);

			final BigDecimal motionZ = this.getMotionZ().add(new BigDecimal(motionZd));
			this.setMotionZ(motionZ);
		}
		super.moveRelative(strafe, up, forward, friction);
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		try {
			compound.setTag("BigDecimalMotion", this.newBigDecimalNBTList(this.getMotionX(), this.getMotionY(), this.getMotionZ()));
		} catch (final Throwable throwable) {
			final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
			final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
			this.addEntityCrashInfo(crashreportcategory);
			throw new ReportedException(crashreport);
		}
		return super.writeToNBT(compound);
	}

	public NBTTagList newBigDecimalNBTList(final BigDecimal... numbers) {
		final NBTTagList nbttaglist = new NBTTagList();

		for (final BigDecimal decimal : numbers) {
			nbttaglist.appendTag(new NBTTagString(decimal.toString()));
		}

		return nbttaglist;
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		try {

//			for (int i = 0; i < 20; i++) {
//				WIPTech.info(i, compound.getTagList("BigDecimalMotion", i));
//			}

			final NBTTagList motion = compound.getTagList("BigDecimalMotion", 8);
			final String motionXs = motion.getStringTagAt(0);
			final String motionYs = motion.getStringTagAt(1);
			final String motionZs = motion.getStringTagAt(2);

			this.setMotionX(new BigDecimal(motionXs));
			this.setMotionY(new BigDecimal(motionYs));
			this.setMotionZ(new BigDecimal(motionZs));

		} catch (final Throwable throwable) {
			final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
			final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being loaded");
			this.addEntityCrashInfo(crashreportcategory);
			throw new ReportedException(crashreport);
		}
		super.readFromNBT(compound);
	}

	@Override
	public void updateRidden() {
		final Entity entity = this.getRidingEntity();

		if (!this.isRiding() || !entity.isDead) {
			this.setMotionToZero();
		}
		super.updateRidden();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(final double x, final double y, final double z) {
		this.setMotionX(x);
		this.setMotionY(y);
		this.setMotionZ(z);
		super.setVelocity(x, y, z);
	}

	@Override
	protected boolean pushOutOfBlocks(final double x, final double y, final double z) {
		final BlockPos blockpos = new BlockPos(x, y, z);
		final double d0 = x - blockpos.getX();
		final double d1 = y - blockpos.getY();
		final double d2 = z - blockpos.getZ();

		if (this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {

			EnumFacing enumfacing = EnumFacing.UP;
			double d3 = Double.MAX_VALUE;

			if (!this.world.isBlockFullCube(blockpos.west()) && (d0 < d3)) {
				d3 = d0;
				enumfacing = EnumFacing.WEST;
			}

			if (!this.world.isBlockFullCube(blockpos.east()) && ((1.0D - d0) < d3)) {
				d3 = 1.0D - d0;
				enumfacing = EnumFacing.EAST;
			}

			if (!this.world.isBlockFullCube(blockpos.north()) && (d2 < d3)) {
				d3 = d2;
				enumfacing = EnumFacing.NORTH;
			}

			if (!this.world.isBlockFullCube(blockpos.south()) && ((1.0D - d2) < d3)) {
				d3 = 1.0D - d2;
				enumfacing = EnumFacing.SOUTH;
			}

			if (!this.world.isBlockFullCube(blockpos.up()) && ((1.0D - d1) < d3)) {
				d3 = 1.0D - d1;
				enumfacing = EnumFacing.UP;
			}

			final float f = (this.rand.nextFloat() * 0.2F) + 0.1F;
			final float f1 = enumfacing.getAxisDirection().getOffset();

			if (enumfacing.getAxis() == EnumFacing.Axis.X) {
				this.setMotionX(f1 * f);
				this.setMotionY(this.getMotionY().multiply(new BigDecimal(0.75)));
				this.setMotionZ(this.getMotionZ().multiply(new BigDecimal(0.75)));
			} else if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
				this.setMotionX(this.getMotionX().multiply(new BigDecimal(0.75)));
				this.setMotionY(f1 * f);
				this.setMotionZ(this.getMotionZ().multiply(new BigDecimal(0.75)));
			} else if (enumfacing.getAxis() == EnumFacing.Axis.Z) {
				this.setMotionX(this.getMotionX().multiply(new BigDecimal(0.75)));
				this.setMotionY(this.getMotionY().multiply(new BigDecimal(0.75)));
				this.setMotionZ(f1 * f);
			}
		}

		return super.pushOutOfBlocks(x, y, z);
	}

	@Override
	public void shoot(final Entity entityThrower, final float rotationPitch, final float rotationYaw, final float pitchOffset, final float velocity, final float inaccuracy) {
		final BigDecimal motionX = this.getMotionX().add(new BigDecimal(entityThrower.motionX));
		this.setMotionX(motionX);

		final BigDecimal motionZ = this.getMotionZ().add(new BigDecimal(entityThrower.motionZ));
		this.setMotionZ(motionZ);

		if (!entityThrower.onGround) {
			final BigDecimal motionY = this.getMotionY().add(new BigDecimal(entityThrower.motionY));
			this.setMotionY(motionY);
		}

		super.shoot(entityThrower, rotationPitch, rotationYaw, pitchOffset, velocity, inaccuracy);

	}

	@Override
	public void shoot(double x, double y, double z, final float velocity, final float inaccuracy) {
		final float f = MathHelper.sqrt((x * x) + (y * y) + (z * z));
		x = x / f;
		y = y / f;
		z = z / f;
		x = x + (this.rand.nextGaussian() * 0.0075 * inaccuracy);
		y = y + (this.rand.nextGaussian() * 0.0075 * inaccuracy);
		z = z + (this.rand.nextGaussian() * 0.0075 * inaccuracy);
		x = x * velocity;
		y = y * velocity;
		z = z * velocity;
		this.setMotionX(x);
		this.setMotionY(y);
		this.setMotionZ(z);

		super.shoot(x, y, z, velocity, inaccuracy);
	}

	@Override
	public void onUpdate() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		super.onUpdate();

		if (this.throwableShake > 0) {
			--this.throwableShake;
		}

		if (this.inGround) {
//			if (this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
//				++this.ticksInGround;
//
//				if (this.ticksInGround == 1200) {
//					this.setDead();
//				}
//
//				return;
//			}

			this.inGround = false;
			this.setMotionX(this.getMotionX().multiply(new BigDecimal(this.rand.nextFloat() * 0.2F)));
			this.setMotionY(this.getMotionY().multiply(new BigDecimal(this.rand.nextFloat() * 0.2F)));
			this.setMotionZ(this.getMotionZ().multiply(new BigDecimal(this.rand.nextFloat() * 0.2F)));
//			this.ticksInGround = 0;
//			this.ticksInAir = 0;
//		} else {
//			++this.ticksInAir;
		}

		Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d1 = new Vec3d(this.posX + this.getMotionX().doubleValue(), this.posY + this.getMotionY().doubleValue(), this.posZ + this.getMotionZ().doubleValue());
		RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1);
		vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d1 = new Vec3d(this.posX + this.getMotionX().doubleValue(), this.posY + this.getMotionY().doubleValue(), this.posZ + this.getMotionZ().doubleValue());

		if (raytraceresult != null) {
			vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		Entity entity = null;
		final List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.getMotionX().doubleValue(), this.getMotionY().doubleValue(), this.getMotionZ().doubleValue()).grow(1.0D));
		double d0 = 0.0D;
		boolean flag = false;

		for (int i = 0; i < list.size(); ++i) {
			final Entity entity1 = list.get(i);

			if (entity1.canBeCollidedWith()) {
				if (entity1 == this.ignoreEntity) {
					flag = true;
				} else if ((this.thrower != null) && (this.ticksExisted < 2) && (this.ignoreEntity == null)) {
					this.ignoreEntity = entity1;
					flag = true;
				} else {
					flag = false;
					final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.3);
					final RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

					if (raytraceresult1 != null) {
						final double d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);

						if ((d1 < d0) || (d0 == 0.0D)) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}
		}

		if (this.ignoreEntity != null) {
//			if (flag) {
//				this.ignoreTime = 2;
//			} else if (this.ignoreTime-- <= 0) {
//				this.ignoreEntity = null;
//			}
		}

		if (entity != null) {
			raytraceresult = new RayTraceResult(entity);
		}

		if (raytraceresult != null) {
			if ((raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) && (this.world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL)) {
				this.setPortal(raytraceresult.getBlockPos());
			} else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
				this.onImpact(raytraceresult);
			}
		}

		this.posX += this.getMotionX().doubleValue();
		this.posY += this.getMotionY().doubleValue();
		this.posZ += this.getMotionZ().doubleValue();
		final float f = MathHelper.sqrt((this.getMotionX().doubleValue() * this.getMotionX().doubleValue()) + (this.getMotionZ().doubleValue() * this.getMotionZ().doubleValue()));
		this.rotationYaw = (float) (MathHelper.atan2(this.getMotionX().doubleValue(), this.getMotionZ().doubleValue()) * (180D / Math.PI));

		for (this.rotationPitch = (float) (MathHelper.atan2(this.getMotionY().doubleValue(), f) * (180D / Math.PI)); (this.rotationPitch - this.prevRotationPitch) < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while ((this.rotationPitch - this.prevRotationPitch) >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while ((this.rotationYaw - this.prevRotationYaw) < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while ((this.rotationYaw - this.prevRotationYaw) >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + ((this.rotationPitch - this.prevRotationPitch) * 0.2F);
		this.rotationYaw = this.prevRotationYaw + ((this.rotationYaw - this.prevRotationYaw) * 0.2F);
		float f1 = 0.99F;
		final float f2 = this.getGravityVelocity();

		if (this.isInWater()) {
			for (int j = 0; j < 4; ++j) {
				final float f3 = 0.25F;
				this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - (this.getMotionX().doubleValue() * 0.25D), this.posY - (this.getMotionY().doubleValue() * 0.25D), this.posZ - (this.getMotionZ().doubleValue() * 0.25D), this.getMotionX().doubleValue(), this.getMotionY().doubleValue(), this.getMotionZ().doubleValue());
			}

			f1 = 0.8F;
		}

		this.setMotionX(this.getMotionX().multiply(new BigDecimal(f1)));
		this.setMotionY(this.getMotionY().multiply(new BigDecimal(f1)));
		this.setMotionZ(this.getMotionZ().multiply(new BigDecimal(f1)));

		if (!this.hasNoGravity()) {
			this.setMotionY(this.getMotionY().subtract(new BigDecimal(f2)));
		}

		this.setPosition(this.posX, this.posY, this.posZ);
	}

}
