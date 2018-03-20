package cadiboo.wiptech.init;

import cadiboo.wiptech.Reference;
import cadiboo.wiptech.entity.*;
import cadiboo.wiptech.entity.projectile.*;
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
	
	public static final EntityEntry FERROMAGNETICPROJECTILE = EntityEntryBuilder.create()
			.entity(EntityFerromagneticProjectile.class)
			.id(new ResourceLocation(Reference.ID, "ferromagneticprojectile"), ID++)
			.name("ferromagneticprojectile")
			.tracker(64, 2, true)
			.build();



	public static final EntityEntry[] ENTITIES = {
			NAPALM,
			FERROMAGNETICPROJECTILE,
	};

}
