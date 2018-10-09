package cadiboo.wiptechApiTester;

import java.math.BigDecimal;
import java.math.BigInteger;

import cadiboo.wiptech.api.WIPTechAPI;
import cadiboo.wiptech.material.ModMaterialProperties;
import cadiboo.wiptech.util.ModEnums.CircuitType;
import cadiboo.wiptech.util.ModEnums.UsePhase;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import scala.util.Random;

/**
 * @author Cadiboo
 */
@Mod(modid = WIPTechApiTester.MOD_ID, name = "WIPTech Api Tester", version = "0.0")
/*@formatter:on*/
public class WIPTechApiTester {

	public static final String MOD_ID = "wiptech_api_tester";

	@Instance("wiptech_api_tester")
	public static WIPTechApiTester instance;

	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {

		if (ModReference.Debug.debugAPICircuits()) {
			final Random rand = new Random(0xBADC0DE);
			final CircuitType SEMI_AUTO = WIPTechAPI.addCircuit(rand.nextString(7), rand.nextInt(1000), rand.nextInt(100), new UsePhase[]{UsePhase.TICK, UsePhase.END});
		}

		if (ModReference.Debug.debugAPIMaterials()) {
			final int n = 14;
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

				// true, false, false, false, false, false, false, false, false, false, false, false, false, false, ModMaterial.ALUMINIUM.getProperties().getHardness(), 0, null, new BlockRenderLayer[]{BlockRenderLayer.SOLID}, null)

				final String name = "TEST_MATERIAL_" + String.join("", tfArray);
				// final ModMaterialProperties properties = new ModMaterialProperties(boolArray[0], boolArray[1], boolArray[2], boolArray[3], boolArray[4], boolArray[5], boolArray[6], boolArray[7], boolArray[8], boolArray[9], boolArray[10], boolArray[11], boolArray[12], boolArray[13], 4.00f, 73);
				final ModMaterialProperties properties = new ModMaterialProperties(boolArray[0], boolArray[1], boolArray[2], boolArray[2] ? "tr1" : "tr2", boolArray[3], boolArray[3] ? "tr1" : "tr2", boolArray[4], boolArray[5], boolArray[6], boolArray[7], boolArray[8], boolArray[9], boolArray[10], boolArray[11], boolArray[12], boolArray[13], 4.00f, 73, null, new BlockRenderLayer[]{BlockRenderLayer.TRANSLUCENT}, (final Integer i, final java.util.Random r) -> {
					return 1 + i + r.nextInt();
				}, null);

				event.getModLog().info("Adding material with name: " + name + ", properties: " + properties.toString());

				WIPTechAPI.addMaterial(name, properties);

				bi = bi.add(BigInteger.ONE);
			}
		}

	}

	@Mod.EventBusSubscriber
	public static class EventSubscriber {

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();

		}

	}

}
