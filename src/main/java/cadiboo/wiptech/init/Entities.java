package cadiboo.wiptech.init;

import cadiboo.wiptech.entity.*;
import cadiboo.wiptech.entity.projectile.*;
import cadiboo.wiptech.entity.ridable.*;
import cadiboo.wiptech.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

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
	
	public static final EntityEntry RAILGUN = EntityEntryBuilder.create()
			.entity(EntityRailgun.class)
			.id(new ResourceLocation(Reference.ID, "railgun"), ID++)
			.name("railgun")
			.tracker(128, 5, false)
			.build();

	public static final EntityEntry[] ENTITIES = {
			NAPALM,
			PARAMAGNETICPROJECTILE,
			RAILGUN,
	};

}
