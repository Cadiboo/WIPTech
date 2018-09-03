package cadiboo.wiptechApiTester;

import java.math.BigDecimal;
import java.math.BigInteger;

import cadiboo.wiptech.api.WIPTechAPI;
import cadiboo.wiptech.util.ModEnums.ModMaterialTypes;
import cadiboo.wiptech.util.ModMaterialProperties;
import cadiboo.wiptech.util.ModReference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author Cadiboo
 */
@Mod(modid = "wiptech_api_tester", name = "WIPTech Api Tester")
/*@formatter:on*/
public class WIPTechApiTester {

	@Instance("wiptech_api_tester")
	public static WIPTechApiTester instance;

	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		if (ModReference.Debug.debugAPIMaterials()) {
			for (final ModMaterialTypes type : ModMaterialTypes.values()) {
				final int n = 5;
				BigInteger bi = BigInteger.ZERO;
				final BigDecimal rows = new BigDecimal(Math.pow(2, n));
				while (bi.compareTo(rows.toBigInteger()) < 0) {
					String bin = bi.toString(2);// Integer.toBinaryString(i);
					while (bin.length() < n) {
						bin = "0" + bin;
					}
					final char[] chars = bin.toCharArray();
					final boolean[] boolArray = new boolean[n];
					final String[] tfArray = new String[n];
					for (int j = 0; j < chars.length; j++) {
						boolArray[j] = chars[j] == '0' ? true : false;
						tfArray[j] = chars[j] == '0' ? "T" : "F";
					}

					final String name = "TEST_MATERIAL_" + type.getNameUppercase() + "_" + String.join("", tfArray);
					final ModMaterialProperties properties = new ModMaterialProperties(boolArray[0], boolArray[1], boolArray[2], boolArray[3], boolArray[4], 4.00f, 73);

					event.getModLog().info("Adding material with name: " + name + ", type: " + type + ", properties: " + properties.toString());

					WIPTechAPI.addMaterial(name, type, properties);

					bi = bi.add(BigInteger.ONE);
				}
			}
		}

	}

}
