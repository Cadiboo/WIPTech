package cadiboo.wiptech.handler;

import cadiboo.wiptech.Reference;
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
		ResourceLocation[] string = new ResourceLocation[ItemFerromagneticProjectile.subTypesAmmount+1];
		for(int j = 0; j < FerromagneticProjectiles.values().length; j++) {
			string[j] = new ResourceLocation(Reference.ID, FerromagneticProjectiles.values()[j].getName());
		}
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
			return FerromagneticProjectiles.values()[i];
		}

		public int getMetadata() {
			return getID();
		}
	}
	
	public static final class WeaponModules {
		
		public static enum Circuits implements IStringSerializable {
			MANUAL(0, "manual", TextFormatting.WHITE),
			BURST(1, "burst", TextFormatting.RED),
			AUTO(2,"auto", TextFormatting.DARK_RED);
			
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

			public static Circuits byMetadata(int i) {
				return Circuits.values()[i];
			}

			public int getMetadata() {
				return getID();
			}
		}
		
		public static enum Scopes implements IStringSerializable {
			MANUAL(0, "manual", TextFormatting.WHITE),
			BURST(1, "burst", TextFormatting.RED),
			AUTO(2,"auto", TextFormatting.DARK_RED);
			
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

			public static Scopes byMetadata(int i) {
				return Scopes.values()[i];
			}

			public int getMetadata() {
				return getID();
			}
		}
		
		public static enum Coils implements IStringSerializable {
			MANUAL(0, "manual", TextFormatting.WHITE),
			BURST(1, "burst", TextFormatting.RED),
			AUTO(2,"auto", TextFormatting.DARK_RED);
			
			private int ID;
			private String name;
			private TextFormatting chatColor;

			private Coils(int ID, String name, TextFormatting chatColorIn) {
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

			public static Coils byMetadata(int i) {
				return Coils.values()[i];
			}

			public int getMetadata() {
				return getID();
			}
		}
		
		
	}

}