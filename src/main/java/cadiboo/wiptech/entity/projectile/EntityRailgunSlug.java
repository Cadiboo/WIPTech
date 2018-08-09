package cadiboo.wiptech.entity.projectile;

import cadiboo.wiptech.capability.ModEnergyStorage;
import cadiboo.wiptech.entity.ModEntity;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.model.animation.AnimationStateMachine;
import net.minecraftforge.items.IItemHandler;

public class EntityRailgunSlug extends ModEntity {

	private ModMaterials material;

	public EntityRailgunSlug(World worldIn) {
		this(worldIn, ModMaterials.IRON);
	}

	public EntityRailgunSlug(World worldIn, ModMaterials materialIn) {
		super(worldIn);
		this.material = materialIn;
		this.setSize(0.25f, 0.25f);
	}

	public ModMaterials getMaterial() {
		return material;
	}

	@Override
	public ModEnergyStorage getEnergy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnimationStateMachine getAnimation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IItemHandler getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		compound.setInteger("material", material.getId());
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		this.material = ModMaterials.byId(compound.getInteger("material"));

	}/* extends EntityArrow { */

//	public EntityRailgunSlug(World worldIn, double x, double y, double z) {
//		super(worldIn, x, y, z);
//		// TODO Auto-generated constructor stub
//	}

//	@Override
//	protected ItemStack getArrowStack() {
//		// TODO Auto-generated method stub
//		return ItemStack.EMPTY;
//	}

}
