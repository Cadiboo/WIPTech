package cadiboo.wiptech.init;

import cadiboo.wiptech.Reference;
import cadiboo.wiptech.entity.projectile.EntityCoilgunProjectile;
import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.entity.projectile.EntityRailgunProjectile;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

public class Entities {

	public static int ID = 0;

	public static final EntityEntry NAPALM = EntityEntryBuilder.create()
			.entity(EntityNapalm.class)
			.id(new ResourceLocation(Reference.ID, "napalm"), ID++)
			.name("napalm")
			.tracker(64, 2, true)
			.build();

	public static final EntityEntry RAILGUNPROJECTILE = EntityEntryBuilder.create()
			.entity(EntityRailgunProjectile.class)
			.id(new ResourceLocation(Reference.ID, "railgunprojectile"), ID++)
			.name("railgunprojectile")
			.tracker(64, 2, true)
			.build();
	
	public static final EntityEntry COILGUNPROJECTILE = EntityEntryBuilder.create()
			.entity(EntityCoilgunProjectile.class)
			.id(new ResourceLocation(Reference.ID, "coilgunprojectile"), ID++)
			.name("coilgunprojectile")
			.tracker(64, 2, true)
			.build();



	public static final EntityEntry[] ENTITIES = {
			NAPALM,
			RAILGUNPROJECTILE,
			COILGUNPROJECTILE,
			
	};

}
