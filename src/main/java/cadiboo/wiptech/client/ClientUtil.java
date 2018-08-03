package cadiboo.wiptech.client;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;

public class ClientUtil {

	/**
	 * Rotation algorithm Taken off Max_the_Technomancer from <a href=
	 * "https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/2772267-tesr-getting-darker-and-lighter-as-it-rotates">here</a>
	 * 
	 * @param face the {@link net.minecraft.util.EnumFacing face} to rotate for
	 */
	public static void rotateForFace(EnumFacing face) {
		GlStateManager.rotate(face == EnumFacing.DOWN ? 0 : face == EnumFacing.UP ? 180F : face == EnumFacing.NORTH || face == EnumFacing.EAST ? 90F : -90F,
				face.getAxis() == EnumFacing.Axis.Z ? 1 : 0, 0, face.getAxis() == EnumFacing.Axis.Z ? 0 : 1);
		GlStateManager.rotate(-90, 0, 0, 1);
	}

}
