package cadiboo.wiptech.material;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;

public class GemProperties extends ModMaterialProperties {

	public GemProperties(final boolean hasOre, final float MOHS_Hardness, final int thermalConductivityAt20DegreesCelsius, @Nullable final Supplier<Item> getOreDrop, @Nonnull @MethodsReturnNonnullByDefault final BiFunction<Integer, Random, Integer> getQuantityDroppedWithBonus) {
		super(hasOre, true, true, true, true, true, true, true, true, true, true, true, true, hasOre, MOHS_Hardness, thermalConductivityAt20DegreesCelsius, getOreDrop, new BlockRenderLayer[]{BlockRenderLayer.SOLID, BlockRenderLayer.TRANSLUCENT}, getQuantityDroppedWithBonus);
	}

}
