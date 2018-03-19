package cadiboo.wiptech.entity.projectile;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityProjectileBase extends EntityArrow {

	public static final Predicate<Entity> PROJECTILE_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>()
	{
		public boolean apply(@Nullable Entity targetEntity)
		{
			if(targetEntity instanceof EntityEnderman)
				return true;
			return targetEntity.canBeCollidedWith();
		}
	});

	@Override
	public boolean getIsCritical() {
		return false;
	}

	public static final AxisAlignedBB ON_BLOCK_AABB = new AxisAlignedBB(-0.05D, -0.05D, -0.05D, 0.05D, 0.05D, 0.05D);

	public int xTile;
	public int yTile;
	public int zTile;
	public Block inTile;
	public int inData;
	public boolean inGround;
	public int timeInGround;
	public EntityProjectileBase.PickupStatus pickupStatus;
	public Entity shootingEntity;
	public int ticksInGround;
	public int ticksInAir;
	public double damage;
	public double knockbackStrength;

	public EntityProjectileBase(World worldIn)
	{
		super(worldIn);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.pickupStatus = EntityProjectileBase.PickupStatus.DISALLOWED;
		this.damage = 2.0D;
		this.setSize(0.5F, 0.5F);
	}

	public EntityProjectileBase(World worldIn, double x, double y, double z)
	{
		this(worldIn);
		this.setPosition(x, y, z);
	}

	public EntityProjectileBase(World worldIn, EntityLivingBase shooter)
	{
		this(worldIn, shooter.posX, shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);
		this.shootingEntity = shooter;

		if (shooter instanceof EntityPlayer)
		{
			this.pickupStatus = EntityProjectileBase.PickupStatus.ALLOWED;
		}
	}

	public static enum PickupStatus
	{
		DISALLOWED,
		ALLOWED,
		CREATIVE_ONLY;

		public static EntityProjectileBase.PickupStatus getByOrdinal(int ordinal)
		{
			if (ordinal < 0 || ordinal > values().length)
			{
				ordinal = 0;
			}

			return values()[ordinal];
		}
	}

	/**
	 * Checks if the entity is in range to render.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance)
	{
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 10.0D;

		if (Double.isNaN(d0))
		{
			d0 = 1.0D;
		}

		d0 = d0 * 64.0D * getRenderDistanceWeight();
		return distance < d0 * d0;
	}

	@Override
	public void shoot(Entity shooter, float pitch, float yaw, float iDontDoAnything, float velocity, float inaccuracy)
	{
		float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		float f1 = -MathHelper.sin(pitch * 0.017453292F);
		float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
		this.motionX += shooter.motionX;
		this.motionZ += shooter.motionZ;

		if (!shooter.onGround)
		{
			this.motionY += shooter.motionY;
		}
	}

	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
	 */
	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy)
	{
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / (double)f;
		y = y / (double)f;
		z = z / (double)f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
		x = x * (double)velocity;
		y = y * (double)velocity;
		z = z * (double)velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f1 = MathHelper.sqrt(x * x + z * z);
		this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
		this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		this.ticksInGround = 0;
	}

	/**
	 * Set the position and rotation values directly without any clamping.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
	{
		this.setPosition(x, y, z);
		this.setRotation(yaw, pitch);
	}

	/**
	 * Updates the entity motion clientside, called by packets from the server
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z)
	{
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt(x * x + z * z);
			this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (180D / Math.PI));
			this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		//super.onUpdate();

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
			this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (180D / Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}

		BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
		IBlockState iblockstate = this.world.getBlockState(blockpos);
		Block block = iblockstate.getBlock();

		if (iblockstate.getMaterial() != Material.AIR)
		{
			AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);

			if (axisalignedbb != Block.NULL_AABB && axisalignedbb.grow(0.1F).offset(blockpos).contains(new Vec3d(this.posX, this.posY, this.posZ)))
			{
				this.inGround = true;
			}
		}

		if (this.inGround)
		{
			updateInGround(iblockstate);
		}
		else
		{
			updateInAir();
		}
	}

	private void updateInAir() {
		this.timeInGround = 0;
		++this.ticksInAir;
		Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
		vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (raytraceresult != null)
		{
			vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		Entity entity = this.findEntityOnPath(vec3d1, vec3d);

		if (entity != null)
		{
			raytraceresult = new RayTraceResult(entity);
		}

		if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)raytraceresult.entityHit;

			if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer))
			{
				raytraceresult = null;
			}
		}

		if (raytraceresult != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult))
		{
			this.onHit(raytraceresult);
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
		{
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F)
		{
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
		{
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f1 = 0.99F;
		float f2 = 0.05F;

		if (this.isInWater())
		{
			for (int i = 0; i < 4; ++i)
			{
				float f3 = 0.25F;
				this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
			}

			f1 = 0.6F;
		}

		if (this.isWet())
		{
			this.extinguish();
		}

		this.motionX *= (double)f1;
		this.motionY *= (double)f1;
		this.motionZ *= (double)f1;

		if (!this.hasNoGravity())
		{
			this.motionY -= 0.05000000074505806D;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
		this.doBlockCollisions();
	}

	private void updateInGround(IBlockState state) {
		Block block = state.getBlock();
	    int meta = block.getMetaFromState(state);

	    // check if it's still the same block or if it is already within tolerance of another hitbox
	    // second part prevents it from falling when the block changes but the hitbox does nots
	    if((block == this.inTile && meta == this.inData) || this.getEntityWorld().collidesWithAnyBlock(ON_BLOCK_AABB.offset(this.getPositionVector()))) {
	      ++this.ticksInGround;

	      if(this.ticksInGround >= 1200) {
	        this.setDead();
	      }
	    }
	    else {
	      this.inGround = false;
	      this.motionX *= this.rand.nextFloat() * 0.2F;
	      this.motionY *= this.rand.nextFloat() * 0.2F;
	      this.motionZ *= this.rand.nextFloat() * 0.2F;
	      this.ticksInGround = 0;
	      this.ticksInAir = 0;
	    }

	    ++this.timeInGround;
	}

	/**
	 * Tries to move the entity towards the specified location.
	 */
	@Override
	public void move(MoverType type, double x, double y, double z)
	{
		super.move(type, x, y, z);

		if (this.inGround)
		{
			this.xTile = MathHelper.floor(this.posX);
			this.yTile = MathHelper.floor(this.posY);
			this.zTile = MathHelper.floor(this.posZ);
		}
	}

	@Override
	@Nullable
	protected Entity findEntityOnPath(Vec3d start, Vec3d end)
	{
		Entity entity = null;
		if(this.shootingEntity!=null) {


			List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D), PROJECTILE_TARGETS);
			double d0 = 0.0D;

			for (int i = 0; i < list.size(); ++i)
			{
				Entity entity1 = list.get(i);

				if (entity1 != this.shootingEntity || (this.ticksInAir >=5 && this.arrowShake==0))
				{
					AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
					RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

					if (raytraceresult != null)
					{
						double d1 = start.squareDistanceTo(raytraceresult.hitVec);

						if (d1 < d0 || d0 == 0.0D)
						{
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}


		}
		return entity;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("xTile", this.xTile);
		compound.setInteger("yTile", this.yTile);
		compound.setInteger("zTile", this.zTile);
		compound.setShort("life", (short)this.ticksInGround);
		ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(this.inTile);
		compound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
		compound.setByte("inData", (byte)this.inData);
		compound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
		compound.setByte("pickup", (byte)this.pickupStatus.ordinal());
		compound.setDouble("damage", this.damage);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		this.xTile = compound.getInteger("xTile");
		this.yTile = compound.getInteger("yTile");
		this.zTile = compound.getInteger("zTile");
		this.ticksInGround = compound.getShort("life");

		if (compound.hasKey("inTile", 8))
		{
			this.inTile = Block.getBlockFromName(compound.getString("inTile"));
		}
		else
		{
			this.inTile = Block.getBlockById(compound.getByte("inTile") & 255);
		}

		this.inData = compound.getByte("inData") & 255;
		this.inGround = compound.getByte("inGround") == 1;

		if (compound.hasKey("damage", 99))
		{
			this.damage = compound.getDouble("damage");
		}

		if (compound.hasKey("pickup", 99))
		{
			this.pickupStatus = EntityProjectileBase.PickupStatus.getByOrdinal(compound.getByte("pickup"));
		}
		else if (compound.hasKey("player", 99))
		{
			this.pickupStatus = compound.getBoolean("player") ? EntityProjectileBase.PickupStatus.ALLOWED : EntityProjectileBase.PickupStatus.DISALLOWED;
		}

	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		if (!this.world.isRemote && this.inGround)
		{
			boolean flag = this.pickupStatus == EntityProjectileBase.PickupStatus.ALLOWED || this.pickupStatus == EntityProjectileBase.PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode;

			if (this.pickupStatus == EntityProjectileBase.PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(this.getAmmoStack()))
			{
				flag = false;
			}

			if (flag)
			{
				entityIn.onItemPickup(this, 1);
				this.setDead();
			}
		}
	}


	protected ItemStack getAmmoStack() {
		return ItemStack.EMPTY;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}
	@Override
	public void setDamage(double damageIn)
	{
		this.damage = damageIn;
	}
	@Override
	public double getDamage()
	{
		return this.damage;
	}

	/**
	 * Sets the amount of knockback the arrow applies when it hits a mob.
	 */
	@Override
	public void setKnockbackStrength(int knockbackStrengthIn)
	{
		this.knockbackStrength = knockbackStrengthIn;
	}

	/**
	 * Returns true if it's possible to attack this entity with an item.
	 */
	@Override
	public boolean canBeAttackedWithItem()
	{
		return false;
	}

	@Override
	protected ItemStack getArrowStack() {
		return getAmmoStack();
	}

}
