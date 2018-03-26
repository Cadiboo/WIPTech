package cadiboo.wiptech.handler;

import cadiboo.wiptech.Reference;
import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.item.ItemFerromagneticProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnumHandler
{
	public static ResourceLocation[] getResourceLocations(int i) {
		ResourceLocation[] string = new ResourceLocation[ItemFerromagneticProjectile.subTypesAmmount];
		for(int j = 0; j < FerromagneticProjectiles.values().length; j++) {
			string[j] = new ResourceLocation(Reference.ID, FerromagneticProjectiles.values()[j].getName());
		}
		//TODO TOFINDOUT this is never called, why did I make it?

		return string;
	}

	public static enum FerromagneticProjectiles implements IStringSerializable {
		IRON_LARGE(0, "iron_large", TextFormatting.WHITE),
		OSMIUM_LARGE(1, "osmium_large", TextFormatting.DARK_BLUE),
		TUNGSTEN_LARGE(2,"tungsten_large", TextFormatting.GRAY),
		IRON_MEDIUM(3, "iron_medium", TextFormatting.WHITE),
		OSMIUM_MEDIUM(4, "osmium_medium", TextFormatting.DARK_BLUE),
		TUNGSTEN_MEDIUM(5,"tungsten_medium", TextFormatting.GRAY),
		IRON_SMALL(6, "iron_small", TextFormatting.WHITE),
		OSMIUM_SMALL(7, "osmium_small", TextFormatting.DARK_BLUE),
		TUNGSTEN_SMALL(8,"tungsten_small", TextFormatting.GRAY);//,
		//PLASMA(9,"plasma", TextFormatting.GOLD); //nano

		private int ID;
		private String name;
		private TextFormatting chatColor;

		private FerromagneticProjectiles(int ID, String name, TextFormatting chatColorIn) {
			this.ID = ID;
			this.name = name;
			this.chatColor = chatColorIn;
		}

		@Override
		public String getName() {
			return this.name;
		}

		public String getUnlocalizedName() {
			return this.getName();
		}

		public int getID() {
			return ID;
		}

		@Override
		public String toString() {
			return getName();
		}

		public static FerromagneticProjectiles byMetadata(int i) {
			if(i>ItemFerromagneticProjectile.subTypesAmmountZI) {
				return FerromagneticProjectiles.values()[0];
			}
			return FerromagneticProjectiles.values()[i];
		}

		public int getMetadata() {
			return getID();
		}
	}

	public static final class WeaponModules {

		public static enum Rails implements IStringSerializable, IWeaponModule {

			/*
			Material IACS (International Annealed Copper Standard)
			Ranking	Metal	% Conductivity*
			1	Silver (Pure)	105%
			2	Copper	100%
			3	Gold (Pure)	70%
			4	Aluminum	61%
			5	Brass	28%
			6	Zinc	27%
			7	Nickel	22%
			8	Iron (Pure)	17%
			9	Tin	15%
			10	Phosphor Bronze	15%
			11	Steel (Stainless included)	3-15%
			12	Lead (Pure)	7%
			13	Nickel Aluminum Bronze	7%
			 */

			SILVER		(5, "silver",	105,	TextFormatting.WHITE),
			COPPER		(4, "copper",	100,	TextFormatting.RED),
			GOLD			(3, "gold",		70, TextFormatting.GOLD),
			ALUMINIUM	(2, "aluminium",	61, TextFormatting.GRAY),
			IRON			(1, "iron",		17, TextFormatting.GRAY),
			TIN			(0, "tin",		15, TextFormatting.WHITE);

			private int ID;
			private String name;
			private TextFormatting chatColor;
			private int efficiency;

			private Rails(int ID, String name, int efficiency, TextFormatting chatColorIn) {
				this.ID = ID;
				this.name = name;
				this.efficiency = efficiency;
				this.chatColor = chatColorIn;
			}

			@Override
			public String getName() {
				return this.name;
			}

			public int getID() {
				return ID;
			}

			public static Rails byID(int i) {
				return Rails.values()[i];
			}

			public float getEfficiencyFraction() {
				return (float)getEfficiencyPercentage()/100F;
			}

			public int getEfficiencyPercentage() {
				return this.efficiency;
			}
		}

		public static enum Circuits implements IStringSerializable, IWeaponModule {
			MANUAL(0, "manual", TextFormatting.WHITE),
			BURST3(1, "burst3", TextFormatting.RED),
			BURST5(2, "burst5", TextFormatting.RED),
			BURST10(3, "burst10", TextFormatting.RED),
			AUTO(4,"auto", TextFormatting.DARK_RED),
			OVERCLOCKED(5,"overclocked", TextFormatting.AQUA);

			private int ID;
			private String name;
			private TextFormatting chatColor;

			private Circuits(int ID, String name, TextFormatting chatColorIn) {
				this.ID = ID;
				this.name = name;
				this.chatColor = chatColorIn;
			}

			@Override
			public String getName() {
				return this.name;
			}

			public int getID() {
				return ID;
			}

			public static Circuits byID(int i) {
				return Circuits.values()[i];
			}
		}

		public static enum Scopes implements IStringSerializable, IWeaponModule {
			ACOG(0, "acog", TextFormatting.WHITE),
			ZOOM(1, "zoom", TextFormatting.GRAY),
			LASER(2,"laser", TextFormatting.RED);

			private int ID;
			private String name;
			private TextFormatting chatColor;

			private Scopes(int ID, String name, TextFormatting chatColorIn) {
				this.ID = ID;
				this.name = name;
				this.chatColor = chatColorIn;
			}

			@Override
			public String getName() {
				return this.name;
			}

			public int getID() {
				return ID;
			}

			public static Scopes byID(int i) {
				return Scopes.values()[i];
			}
		}

		public static enum Coils implements IStringSerializable, IWeaponModule {

			/*
			Material IACS (International Annealed Copper Standard)
			Ranking	Metal	% Conductivity*
			1	Silver (Pure)	105%
			2	Copper	100%
			3	Gold (Pure)	70%
			4	Aluminum	61%
			5	Brass	28%
			6	Zinc	27%
			7	Nickel	22%
			8	Iron (Pure)	17%
			9	Tin	15%
			10	Phosphor Bronze	15%
			11	Steel (Stainless included)	3-15%
			12	Lead (Pure)	7%
			13	Nickel Aluminum Bronze	7%
			 */

			SILVER		(5, "silver",	105,	TextFormatting.WHITE),
			COPPER		(4, "copper",	100,	TextFormatting.RED),
			GOLD			(3, "gold",		70, TextFormatting.GOLD),
			ALUMINIUM	(2, "aluminium",	61, TextFormatting.GRAY),
			IRON			(1, "iron",		17, TextFormatting.GRAY),
			TIN			(0, "tin",		15, TextFormatting.WHITE);

			private int ID;
			private String name;
			private TextFormatting chatColor;
			private int efficiency;

			private Coils(int ID, String name, int efficiency, TextFormatting chatColorIn) {
				this.ID = ID;
				this.name = name;
				this.efficiency = efficiency;
				this.chatColor = chatColorIn;
			}

			@Override
			public String getName() {
				return this.name;
			}

			public int getID() {
				return ID;
			}

			public static Coils byID(int i) {
				return Coils.values()[i];
			}

			public float getEfficiencyFraction() {
				return (float)getEfficiencyPercentage()/100F;
			}

			public int getEfficiencyPercentage() {
				return this.efficiency;
			}

		}

	}

}