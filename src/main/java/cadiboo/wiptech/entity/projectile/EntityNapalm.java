package cadiboo.wiptech.entity.projectile;

import java.util.Random;

import cadiboo.wiptech.entity.IModEntity;
import cadiboo.wiptech.init.ModBlocks;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class EntityNapalm extends EntityThrowable implements IModEntity {

	public EntityNapalm(final World world) {
		super(world);
	}

	public EntityNapalm(final World world, final EntityLivingBase thrower) {
		super(world, thrower);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.spawnParticles(this.posX, this.posY, this.posZ);

		if (this.ticksExisted <= 2) {
			return;
		}

		if (this.world.isRemote) {
			return;
		}

		this.igniteBlocks(this.posX, this.posY, this.posZ);

	}

	public void spawnParticles(final double posX, final double posY, final double posZ) {

		final Random rand = new Random();

		for (int i = 0; i < Math.min(50, this.ticksExisted); i++) {
			final double randXMotion = rand.nextGaussian() * 0.0025 * this.ticksExisted;
			final double randYMotion = rand.nextDouble() * 0.025 * Math.min(25, this.ticksExisted);
			final double randZMotion = rand.nextGaussian() * 0.0025 * this.ticksExisted;

			this.world.spawnParticle(EnumParticleTypes.FLAME, true, posX, posY, posZ, randXMotion, randYMotion, randZMotion);
		}
	}

	@Override
	protected void onImpact(final RayTraceResult result) {
		if (result == null) {
			return;
		}

		if (this.world.isRemote) {
			return;
		}

		if (result.entityHit != null) {
			if ((result.entityHit != this.getThrower()) || (this.ticksExisted > 5)) {
				result.entityHit.setFire(100);
			}
		}

		if (ModReference.Debug.insanity()) {
			this.world.newExplosion(this, this.posX, this.posY, this.posZ, 30, true, true);
		}

		if (result.typeOfHit != Type.BLOCK) {
			return;
		}

		final double posX = result.hitVec.x - Math.max(-0.51, Math.min(0.51, this.motionX));
		final double posY = result.hitVec.y - Math.max(-0.51, Math.min(0.51, this.motionY));
		final double posZ = result.hitVec.z - Math.max(-0.51, Math.min(0.51, this.motionZ));

		this.igniteBlocks(posX, posY, posZ);
		this.setDead();
	}

	protected void igniteBlocks(final double posX, final double posY, final double posZ) {
		if (this.world.isRemote) {
			return;
		}
		BlockPos pos = new BlockPos(posX, posY, posZ);
		if ((this.world.getBlockState(pos) != Blocks.AIR.getDefaultState()) && (this.world.getBlockState(pos) != Blocks.SNOW_LAYER.getDefaultState())) {
			return;
		}

		final int radius = Math.min(3, Math.round(this.ticksExisted / 50f));

		for (int x = -(radius); x <= radius; x++) {
			for (int y = -(radius); y <= radius; y++) {
				for (int z = -(radius); z <= radius; z++) {
					pos = new BlockPos(posX + x, posY + y, posZ + z);

					boolean place = false;

					for (final EnumFacing facing : EnumFacing.VALUES) {
						if (place) {
							continue;
						}
						if (this.world.getBlockState(pos.offset(facing)).isSideSolid(this.world, pos, facing.getOpposite())) {
							place = true;
						}
						if (place) {
							continue;
						}
						if (this.world.getBlockState(pos.offset(facing)).getBlock().isFlammable(this.world, pos, facing.getOpposite())) {
							place = true;
						}
					}

					if ((this.world.getBlockState(pos) == Blocks.AIR.getDefaultState()) && place) {
						this.world.setBlockState(pos, ModBlocks.NAPALM.getDefaultState(), 11);
					} else if (this.world.getBlockState(pos) == Blocks.SNOW_LAYER.getDefaultState()) {
						this.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
					}
				}
			}
		}

	}

}
