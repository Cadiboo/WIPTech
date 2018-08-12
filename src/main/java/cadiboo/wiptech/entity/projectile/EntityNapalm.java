package cadiboo.wiptech.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityNapalm extends EntityThrowable {

    public EntityNapalm(World worldIn) {
	super(worldIn);
    }

    public EntityNapalm(World worldIn, EntityLivingBase throwerIn) {
	super(worldIn, throwerIn);
	this.posY -= 0.3;
    }

    @Override
    public void onUpdate() {
	super.onUpdate();
	this.igniteBlocks();
    }

    @Override
    protected void onImpact(RayTraceResult result) {
	if (result == null)
	    return;

	if (this.world.isRemote)
	    return;

	if (result.entityHit != null)
	    if ((result.entityHit != getThrower()) || (this.ticksExisted > 5))
		result.entityHit.setFire(100);

	this.igniteBlocks();
	this.setDead();
    }

    protected void igniteBlocks() {
	if (this.world.isRemote)
	    return;
	BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);
	if (world.getBlockState(pos) != Blocks.AIR.getDefaultState())
	    return;

	for (EnumFacing facing : EnumFacing.VALUES) {
//	    if (world.getBlockState(pos.offset(facing)).getBlock().isFlammable(world, pos, facing.getOpposite()))
	    if (world.getBlockState(pos.offset(facing)) != Blocks.AIR.getDefaultState()
		    && world.getBlockState(pos.offset(facing)) != Blocks.FIRE.getDefaultState())
		world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 2);
	}
    }

}
