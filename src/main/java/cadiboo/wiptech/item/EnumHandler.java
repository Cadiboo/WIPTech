package cadiboo.wiptech.item;

import cadiboo.wiptech.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnumHandler
{
	public static ResourceLocation[] getResourceLocations(int i) {
		ResourceLocation[] string;
		if(i == 1) {
			string = new ResourceLocation[3];
			for(int j = 0; j < MagneticMetalRods.values().length; j++) {
				string[j] = new ResourceLocation(Reference.ID, MagneticMetalRods.values()[j].getName());
			}
			return string;
		}
		else {
			string = new ResourceLocation[3];
			for(int j = 0; j < MagneticMetalRods.values().length; j++) {
				string[j] = new ResourceLocation(Reference.ID, MagneticMetalRods.values()[j].getName());
			}
			return string;
		}
	}

	public static enum MagneticMetalRods implements IStringSerializable {
		IRON(0, "iron", TextFormatting.WHITE),
		OSMIUM(1, "osmium", TextFormatting.DARK_BLUE),
		TUNGSTEN(2,"tungsten", TextFormatting.GRAY);
		
		private int ID;
		private String name;
		private TextFormatting chatColor;

		private MagneticMetalRods(int ID, String name, TextFormatting chatColorIn) {
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

		public static MagneticMetalRods byMetadata(int i) {
			return MagneticMetalRods.values()[i];
		}

		public int getMetadata() {
			return getID();
		}
	}

}