package cadiboo.wiptech.entity.projectile;

import cadiboo.wiptech.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityPenetratingMissile extends EntityMissile {

	private int timeTillExplode;

	public EntityPenetratingMissile(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityPenetratingMissile(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntityPenetratingMissile(World worldIn) {
		super(worldIn);
	}

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

	@Override
	protected void onHitBlock(RayTraceResult raytraceResultIn) {

		// if (!this.inGround)
		// return;
		// if (this.timeInGround > 0) {
		// return;
		// }

		BlockPos blockpos = raytraceResultIn.getBlockPos();
		this.xTile = blockpos.getX();
		this.yTile = blockpos.getY();
		this.zTile = blockpos.getZ();
		IBlockState iblockstate = this.world.getBlockState(blockpos);

		// if (!this.world.isRemote) {
		// world.createExplosion(this, blockpos.getX(), blockpos.getY(),
		// blockpos.getZ(), 8, true);
		// this.setDead();
		// }
	}

	@Override
	public void updateInGround(IBlockState state) {
		Block block = state.getBlock();
		int meta = block.getMetaFromState(state);
		if ((block == this.inTile && meta == this.inData) || this.getEntityWorld().collidesWithAnyBlock(ON_BLOCK_AABB.offset(this.getPositionVector()))) {
			++this.timeInGround;

			if (this.timeInGround >= 10) {
				world.createExplosion(this, this.posX, this.posY, this.posZ, 8, true);
				this.setDead();
			}
		} else {
			this.inGround = false;
			// this.motionX *= this.rand.nextFloat() * 0.2F;
			// this.motionY *= this.rand.nextFloat() * 0.2F;
			// this.motionZ *= this.rand.nextFloat() * 0.2F;
			// this.timeInGround = 0;
			this.ticksInAir = 0;
		}

		++this.timeInGround;
	}

	@Override
	protected ItemStack getArrowStack() {
		return ItemStack.EMPTY;
	}

	@Override
	protected void onHitEntity(Entity entity) {
		if (!this.world.isRemote && this.ticksExisted > 10) {
			entity.attackEntityFrom(DamageSource.GENERIC, 10);
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

}
