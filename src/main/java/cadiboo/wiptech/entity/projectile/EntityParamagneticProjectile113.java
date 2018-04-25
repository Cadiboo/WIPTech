package cadiboo.wiptech.entity.projectile;

import cadiboo.wiptech.handler.EnumHandler.ParamagneticProjectiles;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.util.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityParamagneticProjectile113 extends EntityArrow {

	private static final DataParameter<Integer>	TYPE		= EntityDataManager.<Integer>createKey(EntityParamagneticProjectile113.class, DataSerializers.VARINT);
	private static final DataParameter<Integer>	TEMPERATURE	= EntityDataManager.<Integer>createKey(EntityParamagneticProjectile113.class, DataSerializers.VARINT);

	public EntityParamagneticProjectile113(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityParamagneticProjectile113(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntityParamagneticProjectile113(World worldIn) {
		super(worldIn);
	}

	@Override
	public ItemStack getArrowStack() {
		// if (true)
		// return new ItemStack(Items.OSMIUM_NUGGET);
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
		if (Boolean.valueOf(false))
			return new ResourceLocation(Reference.ID, "textures/items/plasma_core.png");
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

}
