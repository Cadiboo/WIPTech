package cadiboo.wiptech.entity.projectile;

import java.util.Random;

import cadiboo.wiptech.util.ModReference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityNapalm extends EntityThrowable {

	public EntityNapalm(final World world) {
		super(world);
	}

	public EntityNapalm(final World world, final EntityLivingBase thrower) {
		super(world, thrower);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.igniteBlocks();

		if (this.ticksExisted <= 2) {
			return;
		}

		for (int i = 0; i < Math.min(50, this.ticksExisted); i++) {
			final double randX = new Random().nextGaussian() * 0.0025 * this.ticksExisted;
			final double randY = new Random().nextDouble() * 0.025 * Math.min(25, this.ticksExisted);
			final double randZ = new Random().nextGaussian() * 0.0025 * this.ticksExisted;

			this.world.spawnParticle(EnumParticleTypes.FLAME, true, this.posX, this.posY, this.posZ, randX, randY, randZ);
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

		this.igniteBlocks();
		this.setDead();
	}

	protected void igniteBlocks() {
		if (this.world.isRemote) {
			return;
		}
		BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);
		if ((this.world.getBlockState(pos) != Blocks.AIR.getDefaultState()) && (this.world.getBlockState(pos) != Blocks.SNOW_LAYER.getDefaultState())) {
			return;
		}

		final int radius = Math.min(3, Math.round(this.ticksExisted / 50f));
		for (int x = -(radius); x <= radius; x++) {
			for (int y = -(radius); y <= radius; y++) {
				for (int z = -(radius); z <= radius; z++) {
					pos = new BlockPos(this.posX + x, this.posY + y, this.posZ + z);
					if (this.world.getBlockState(pos) == Blocks.AIR.getDefaultState()) {
						this.world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
					} else if (this.world.getBlockState(pos) == Blocks.SNOW_LAYER.getDefaultState()) {
						this.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
					}
				}
			}
		}

	}

}
