package cadiboo.wiptech.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockBauxiteOre extends BlockBase {

	public BlockBauxiteOre(String name, Material material) {
		super(name, material);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		tooltip.add("\u00A76\u00A7o" + "The ore must first be crushed, chemically processed and then refined using Electrolysis.");
		tooltip.add("By-Products of this ore include Gallium, Iron Oxides, Silica (Silicon Oxide) and Titania (Titanium Oxide).");
	}

}