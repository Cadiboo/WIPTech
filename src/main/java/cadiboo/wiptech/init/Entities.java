package cadiboo.wiptech.init;

import cadiboo.wiptech.entity.projectile.EntityMissile;
import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile113;
import cadiboo.wiptech.entity.ridable.EntityRailgun;
import cadiboo.wiptech.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

//@formatter:off
public class Entities {

	public static int ID = 0;

	public static final EntityEntry NAPALM = EntityEntryBuilder.create()
			.entity(EntityNapalm.class)
			.id(new ResourceLocation(Reference.ID, "napalm"), ID++)
			.name("napalm")
			.tracker(64, 5, true)
			.build();
	
	public static final EntityEntry PARAMAGNETICPROJECTILE = EntityEntryBuilder.create()
			.entity(EntityParamagneticProjectile.class)
			.id(new ResourceLocation(Reference.ID, "paramagneticprojectile"), ID++)
			.name("paramagneticprojectile")
			.tracker(128, 1, true)
			.build();
	
	public static final EntityEntry IRON_SMALL = EntityEntryBuilder.create()
			.entity(EntityParamagneticProjectile113.class)
			.id(new ResourceLocation(Reference.ID, "iron_small"), ID++)
			.name("iron_small")
			.tracker(128, 1, true)
			.build();
	
	public static final EntityEntry OSMIUM_SMALL = EntityEntryBuilder.create()
			.entity(EntityParamagneticProjectile113.class)
			.id(new ResourceLocation(Reference.ID, "osmium_small"), ID++)
			.name("osmium_small")
			.tracker(128, 1, true)
			.build();
	
	public static final EntityEntry TUNGSTEN_SMALL = EntityEntryBuilder.create()
			.entity(EntityParamagneticProjectile113.class)
			.id(new ResourceLocation(Reference.ID, "tungsten_small"), ID++)
			.name("tungsten_small")
			.tracker(128, 1, true)
			.build();
	
	public static final EntityEntry IRON_MEDIUM = EntityEntryBuilder.create()
			.entity(EntityParamagneticProjectile113.class)
			.id(new ResourceLocation(Reference.ID, "iron_medium"), ID++)
			.name("iron_medium")
			.tracker(128, 1, true)
			.build();
	
	public static final EntityEntry OSMIUM_MEDIUM = EntityEntryBuilder.create()
			.entity(EntityParamagneticProjectile113.class)
			.id(new ResourceLocation(Reference.ID, "osmium_medium"), ID++)
			.name("osmium_medium")
			.tracker(128, 1, true)
			.build();
	
	public static final EntityEntry TUNGSTEN_MEDIUM = EntityEntryBuilder.create()
			.entity(EntityParamagneticProjectile113.class)
			.id(new ResourceLocation(Reference.ID, "tungsten_medium"), ID++)
			.name("tungsten_medium")
			.tracker(128, 1, true)
			.build();
	
	public static final EntityEntry IRON_LARGE = EntityEntryBuilder.create()
			.entity(EntityParamagneticProjectile113.class)
			.id(new ResourceLocation(Reference.ID, "iron_large"), ID++)
			.name("iron_large")
			.tracker(128, 1, true)
			.build();
	
	public static final EntityEntry OSMIUM_LARGE = EntityEntryBuilder.create()
			.entity(EntityParamagneticProjectile113.class)
			.id(new ResourceLocation(Reference.ID, "osmium_large"), ID++)
			.name("osmium_large")
			.tracker(128, 1, true)
			.build();
	
	public static final EntityEntry TUNGSTEN_LARGE = EntityEntryBuilder.create()
			.entity(EntityParamagneticProjectile113.class)
			.id(new ResourceLocation(Reference.ID, "tungsten_large"), ID++)
			.name("tungsten_large")
			.tracker(128, 1, true)
			.build();
	
	public static final EntityEntry RAILGUN = EntityEntryBuilder.create()
			.entity(EntityRailgun.class)
			.id(new ResourceLocation(Reference.ID, "railgun"), ID++)
			.name("railgun")
			.tracker(128, 5, false)
			.build();
	
	public static final EntityEntry MISSILE = EntityEntryBuilder.create()
			.entity(EntityMissile.class)
			.id(new ResourceLocation(Reference.ID, "missile"), ID++)
			.name("missile")
			.tracker(128, 5, false)
			.build();

	public static final EntityEntry[] ENTITIES = {
			//@formatter:on
			NAPALM, PARAMAGNETICPROJECTILE, RAILGUN,

			IRON_SMALL, IRON_MEDIUM, IRON_LARGE,

			OSMIUM_SMALL, OSMIUM_MEDIUM, OSMIUM_LARGE,

			TUNGSTEN_SMALL, TUNGSTEN_MEDIUM, TUNGSTEN_LARGE,

			MISSILE };

}
