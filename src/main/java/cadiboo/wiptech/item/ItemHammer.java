package cadiboo.wiptech.item;

import java.util.Set;

import com.google.common.collect.Sets;

import cadiboo.wiptech.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.util.EnumHelper;

public class ItemHammer extends ItemTool {
	public static Item.ToolMaterial WIPTECHHAMMER = EnumHelper.addToolMaterial("WIPTECHHAMMER", 3, 250, 6.0F, 2.0F, 14);
	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.getIngredientBlocks());

	public ItemHammer(String name, float attackDamage, Item.ToolMaterial material, Set effectiveBlocks) {
		super(attackDamage, attackDamage, material, effectiveBlocks);
		setRegistryName("wiptech", name);
		setUnlocalizedName(name);
	}

	public ItemHammer(String name, Item.ToolMaterial material, Set effectiveBlocks) {
		this(name, 0.0F, material, effectiveBlocks);
	}

	public ItemHammer(String name) {
		this(name, Item.ToolMaterial.IRON, EFFECTIVE_ON);
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		Block block = blockIn.getBlock();
		if (EFFECTIVE_ON.contains(block)) {
			return true;
		}
		return false;
	}
}
