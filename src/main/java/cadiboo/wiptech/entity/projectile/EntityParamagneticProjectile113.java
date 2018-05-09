package cadiboo.wiptech.entity.projectile;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import cadiboo.wiptech.config.Configuration;
import cadiboo.wiptech.handler.EnumHandler.ParamagneticProjectiles;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.util.Reference;
import cadiboo.wiptech.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
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

public class EntityParamagneticProjectile113 extends EntityArrow {

	private static final Predicate<Entity> ARROW_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
		@Override
		public boolean apply(@Nullable Entity targetEntity) {
			if (targetEntity.hurtResistantTime > 0) {
				targetEntity.hurtResistantTime = 0;
				return false;
			}
			if (targetEntity instanceof EntityEnderman) {
				return true;
			}
			return targetEntity.canBeCollidedWith();
		}
	});

	private static final DataParameter<Integer>	TYPE		= EntityDataManager.<Integer>createKey(EntityParamagneticProjectile113.class, DataSerializers.VARINT);
	private static final DataParameter<Integer>	TEMPERATURE	= EntityDataManager.<Integer>createKey(EntityParamagneticProjectile113.class, DataSerializers.VARINT);

	public int		xTile;
	public int		yTile;
	public int		zTile;
	public Block	inTile;
	private int		ticksInAir;
	public int		inData;
	public int		ticksInGround;
	public double	knockbackStrength;

	public static final AxisAlignedBB ON_BLOCK_AABB = new AxisAlignedBB(-0.05D, -0.05D, -0.05D, 0.05D, 0.05D, 0.05D);

	public EntityParamagneticProjectile113(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
		this.onUpdate();
	}

	public EntityParamagneticProjectile113(World worldIn, EntityLivingBase shooter) {
		this(worldIn, shooter.posX, shooter.posY + shooter.getEyeHeight() - 0.20000000149011612D * 2, shooter.posZ);
		this.shootingEntity = shooter;

		if (shooter instanceof EntityPlayer) {
			this.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
		}
		this.onUpdate();
	}

	public EntityParamagneticProjectile113(World worldIn) {
		super(worldIn);
		this.onUpdate();
	}

	@Override
	public void shoot(Entity shooter, float pitch, float yaw, float unused, float velocity, float inaccuracy) {
		float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		float f1 = -MathHelper.sin(pitch * 0.017453292F);
		float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		this.shoot(f, f1, f2, velocity, inaccuracy);
		this.motionX += shooter.motionX;
		this.motionZ += shooter.motionZ;
	}

	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		super.shoot(x, y, z, velocity, inaccuracy);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
		super.setPositionAndRotationDirect(x, y, z, yaw, pitch, posRotationIncrements, teleport);
	}

	/**
	 * Updates the entity motion clientside, called by packets from the server
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		super.setVelocity(x, y, z);
	}

	/**
	 * Called to update the entity's position/logic.
	 * 
	 * @return
	 */
	@Override
	public void onUpdate() {
		onEntityUpdate();

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f) * (180D / Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}

		BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
		IBlockState state = Utils.getStateFromPos(this.world, blockpos);
		Block block = Utils.getBlockFromPos(this.world, blockpos);

		if (state.getMaterial() != Material.AIR) {
			AxisAlignedBB aabb = state.getCollisionBoundingBox(this.world, blockpos);
			if (aabb != Block.NULL_AABB && aabb.grow(0.1F).offset(blockpos).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
				this.inGround = true;
			}
		}

		if (this.arrowShake > 0)
			this.arrowShake--;

		if (this.inGround) {
			updateInGround(state);
		} else {
			updateInAir();
		}
	}

	private void updateInAir() {
		this.timeInGround = 0;
		++this.ticksInAir;
		if (this.ticksInAir > this.getAirLifespan()) {
			this.setDead();
			return;
		}

		Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
		// vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		// vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY,
		// this.posZ + this.motionZ);

		if (raytraceresult != null) {
			vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		Entity entity = this.findEntityOnPath(vec3d1, vec3d);

		if (entity != null) {
			raytraceresult = new RayTraceResult(entity);
		}

		if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;

			if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
				raytraceresult = null;
			}
		}

		if (raytraceresult != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
			this.onHit(raytraceresult);
		}

		BlockPos blockpos = this.getPosition();
		IBlockState iblockstate = this.world.getBlockState(blockpos);

		if (!this.world.isRemote) {
			if (this.canIgnite()) {
				if ((iblockstate.getBlock() == Blocks.AIR)
						&& ((world.getBlockState(blockpos.down()).getBlock().isFlammable(world, blockpos.down(), EnumFacing.UP)) || (world.getBlockState(blockpos.up()).getBlock().isFlammable(world, blockpos.up(), EnumFacing.DOWN))
								|| (world.getBlockState(blockpos.north()).getBlock().isFlammable(world, blockpos.north(), EnumFacing.SOUTH)) || (world.getBlockState(blockpos.east()).getBlock().isFlammable(world, blockpos.east(), EnumFacing.WEST))
								|| (world.getBlockState(blockpos.south()).getBlock().isFlammable(world, blockpos.south(), EnumFacing.NORTH)) || (world.getBlockState(blockpos.west()).getBlock().isFlammable(world, blockpos.west(), EnumFacing.EAST))))
					this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState(), 2);
				if (iblockstate.getBlock() == Blocks.WATER)
					this.world.setBlockToAir(blockpos);
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

		for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f4) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f1 = 0.99F;
		float f2 = 0.05F;

		if (this.isInWater()) {
			for (int i = 0; i < 4; ++i) {
				float f3 = 0.25F;
				this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
			}

			f1 = 0.6F;
		}

		if (this.isWet()) {
			this.extinguish();
		}

		this.motionX *= f1;
		this.motionY *= f1;
		this.motionZ *= f1;

		if (!this.hasNoGravity())
			this.motionY -= this.isPlasma() ? 0.00500000074505806D : 0.05000000074505806D;

		this.setPosition(this.posX, this.posY, this.posZ);
		this.doBlockCollisions();
	}

	public void updateInGround(IBlockState state) {
		Block block = state.getBlock();
		int meta = block.getMetaFromState(state);

		// check if it's still the same block or if it is already within tolerance of
		// another hitbox
		// second part prevents it from falling when the block changes but the hitbox
		// does nots
		if ((block == this.inTile && meta == this.inData) || this.getEntityWorld().collidesWithAnyBlock(ON_BLOCK_AABB.offset(this.getPositionVector()))) {
			++this.ticksInGround;

			if (this.ticksInGround >= this.getGroundLifespan()) {
				this.setDead();
			}
		} else {
			this.inGround = false;
			this.motionX *= this.rand.nextFloat() * 0.2F;
			this.motionY *= this.rand.nextFloat() * 0.2F;
			this.motionZ *= this.rand.nextFloat() * 0.2F;
			this.ticksInGround = 0;
			this.ticksInAir = 0;
		}

		++this.timeInGround;
	}

	public int getAirLifespan() {
		return this.isPlasma() ? 100 : 300;
	}

	public int getGroundLifespan() {
		return this.isPlasma() ? 0 : 1200;
	}

	private boolean canIgnite() {
		// TODO Maybe make other thing able to ignite? probs nah
		return this.isPlasma();
	}

	@Override
	public ItemStack getArrowStack() {
		switch (this.getType()) {
			case IRON_SMALL:
				return new ItemStack(Items.IRON_SMALL);
			case OSMIUM_SMALL:
				return new ItemStack(Items.OSMIUM_SMALL);
			case TUNGSTEN_SMALL:
				return new ItemStack(Items.TUNGSTEN_SMALL);
			case IRON_MEDIUM:
				return new ItemStack(Items.IRON_MEDIUM);
			case OSMIUM_MEDIUM:
				return new ItemStack(Items.OSMIUM_MEDIUM);
			case TUNGSTEN_MEDIUM:
				return new ItemStack(Items.TUNGSTEN_MEDIUM);
			case IRON_LARGE:
				return new ItemStack(Items.IRON_LARGE);
			case OSMIUM_LARGE:
				return new ItemStack(Items.OSMIUM_LARGE);
			case TUNGSTEN_LARGE:
				return new ItemStack(Items.TUNGSTEN_LARGE);
			default:
				return new ItemStack(Items.IRON_LARGE);
		}
	}

	public ResourceLocation getTexture() {
		return new ResourceLocation(Reference.ID, "textures/items/" + this.getType().getName() + ".png");
	}

	@Override
	public void entityInit() {
		super.entityInit();
		this.dataManager.register(TYPE, 0);
		this.dataManager.register(TEMPERATURE, 25);
	}

	public void setType(ParamagneticProjectiles type) {
		this.dataManager.set(TYPE, type.getID());
	}

	public ParamagneticProjectiles getType() {
		return ParamagneticProjectiles.byID(this.dataManager.get(TYPE));
	}

	public void setTemperature(int temp) {
		this.dataManager.set(TEMPERATURE, temp);
	}

	public int getTemperature() {
		return this.dataManager.get(TEMPERATURE);
	}

	public int getOverheatTemperature() {
		switch (this.getType().getSize()) {
			case LARGE:
				return 200;
			case MEDIUM:
				return 100;
			case SMALL:
				return 50;
			default:
			case NANO:
				return 500;
		}
	}

	@Override
	public void onHit(RayTraceResult raytraceResultIn) {
		Entity entity = raytraceResultIn.entityHit;

		if (entity != null) {
			onHitEntity(entity);
		} else {
			onHitBlock(raytraceResultIn);
		}
	}

	@Override
	public boolean getIsCritical() {
		return false;
	}

	private void onHitBlock(RayTraceResult raytraceResultIn) {

		BlockPos blockpos = raytraceResultIn.getBlockPos();
		this.xTile = blockpos.getX();
		this.yTile = blockpos.getY();
		this.zTile = blockpos.getZ();
		IBlockState iblockstate = this.world.getBlockState(blockpos);

		if (!this.world.isRemote) {

			if (this.canIgnite()) {
				if (iblockstate.getBlock() instanceof BlockTNT) {
					this.world.setBlockToAir(blockpos);
					world.createExplosion(this, blockpos.getX(), blockpos.getY(), blockpos.getZ(), 8, true);
				}
				if (iblockstate.getMaterial() == Material.WATER) {
					this.world.setBlockToAir(blockpos);
				}
			}
			if (this.canFreeze()) {
				if (iblockstate.getMaterial() == Material.ICE) {
					this.world.setBlockState(blockpos, Blocks.PACKED_ICE.getDefaultState());
				}
				if (iblockstate.getMaterial() == Material.WATER) {
					this.world.setBlockState(blockpos, Blocks.ICE.getDefaultState());
				}
			}
			if (this.canMelt()) {
				if (iblockstate.getMaterial() == Material.ICE || iblockstate.getMaterial() == Material.PACKED_ICE) {
					this.world.setBlockState(blockpos, Blocks.WATER.getDefaultState());
				}
				if (iblockstate.getMaterial() == Material.SNOW || iblockstate.getMaterial() == Material.CRAFTED_SNOW) {
					this.world.setBlockToAir(blockpos);
				}
			}
		}

		if (this.isPlasma()) {
			this.setDead();
			return;
		}

		this.inTile = iblockstate.getBlock();
		this.inData = this.inTile.getMetaFromState(iblockstate);

		if (iblockstate.getMaterial() != Material.AIR) {
			this.inTile.onEntityCollidedWithBlock(this.world, blockpos, iblockstate, this);
		}

		this.motionX = ((float) (raytraceResultIn.hitVec.x - this.posX));
		this.motionY = ((float) (raytraceResultIn.hitVec.y - this.posY));
		this.motionZ = ((float) (raytraceResultIn.hitVec.z - this.posZ));
		float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
		this.posX -= this.motionX / f2 * 0.05000000074505806D;
		this.posY -= this.motionY / f2 * 0.05000000074505806D;
		this.posZ -= this.motionZ / f2 * 0.05000000074505806D;
		this.playSound(SoundEvents.BLOCK_STONE_BREAK, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
		this.inGround = true;
		this.arrowShake = 5;
	}

	private boolean canMelt() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean canFreeze() {
		// TODO Auto-generated method stub
		return false;
	}

	private void onHitEntity(Entity entity) {

		DamageSource damagesource;
		boolean shootingEntityNull = this.shootingEntity == null;

		switch (this.getType().getSize()) {
			case LARGE:
				damagesource = shootingEntityNull ? cadiboo.wiptech.util.DamageSource.causeRailgunProjectileDamage113(this, this) : cadiboo.wiptech.util.DamageSource.causeRailgunProjectileDamage113(this, this.shootingEntity);
				break;
			default:
			case MEDIUM:
				damagesource = shootingEntityNull ? cadiboo.wiptech.util.DamageSource.causeRailgunProjectileDamage113(this, this) : cadiboo.wiptech.util.DamageSource.causeRailgunProjectileDamage113(this, this.shootingEntity);
				break;
			case SMALL:
				damagesource = shootingEntityNull ? cadiboo.wiptech.util.DamageSource.causeCoilgunProjectileDamage113(this, this) : cadiboo.wiptech.util.DamageSource.causeCoilgunProjectileDamage113(this, this.shootingEntity);
				break;
			case NANO:
				damagesource = shootingEntityNull ? cadiboo.wiptech.util.DamageSource.causePlasmaProjectileDamage113(this, this) : cadiboo.wiptech.util.DamageSource.causePlasmaProjectileDamage113(this, this.shootingEntity);
				break;
		}

		// if (this.isOverheated())
		// entity.setFire(5);

		if (this.isPlasma())
			entity.setFire(2);

		final Configuration.Projectile PC = Configuration.projectile;

		boolean finished = false;
		if ((entity instanceof EntityEnderman && PC.HurtEnderman) || (entity instanceof EntityWither && PC.HurtWither)) {
			finished = entity.attackEntityFrom(DamageSource.causeThornsDamage(this), (float) this.getDamage());
		}

		if (!finished && entity.attackEntityFrom(damagesource, (float) this.getDamage())) {
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase = (EntityLivingBase) entity;

				if (this.knockbackStrength > 0) {
					float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

					if (f1 > 0.0F) {
						entitylivingbase.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579D / f1, 0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D / f1);
					}
				}

				if (this.shootingEntity instanceof EntityLivingBase) {
					EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
					EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity, entitylivingbase);
				}
			}

			switch (this.getType().getSize()) {
				default:
				case LARGE:
				case MEDIUM:
					this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					break;
				case SMALL:
					this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					break;
				case NANO:
					this.playSound(SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					break;
			}

			if (this.isPlasma())
				this.setDead();
		}
		this.onUpdate();
	}

	public boolean isPlasma() {
		return this.getType() == ParamagneticProjectiles.PLASMA;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("type", this.getType().getID());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setType(ParamagneticProjectiles.byID(compound.getInteger("type")));
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn) {
		super.onCollideWithPlayer(entityIn);
	}

	public float getStuckDepth() {
		switch (this.getType().getSize()) {
			case LARGE:
				return 0.4F;
			case MEDIUM:
				return 0.2F;
			case SMALL:
				return 0.1F;
			default:
				return 0;
		}
	}

	@Override
	@Nullable
	protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
		Entity entity = null;
		if (this.shootingEntity != null) {

			List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D), ARROW_TARGETS);
			double d0 = 0.0D;

			for (int i = 0; i < list.size(); ++i) {
				Entity entity1 = list.get(i);

				if (entity1 != this.shootingEntity || (this.ticksInAir >= 5 && this.arrowShake <= 0)) {
					AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
					RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

					if (raytraceresult != null) {
						double d1 = start.squareDistanceTo(raytraceresult.hitVec);

						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

		}
		return entity;
	}

	@Override
	public void setKnockbackStrength(int knockbackStrengthIn) {
		setKnockbackStrength((double) knockbackStrengthIn);
	}

	public void setKnockbackStrength(double knockback) {
		this.knockbackStrength = knockback;
	}
}