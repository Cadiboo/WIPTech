package cadiboo.wiptech.init;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.block.BlockAluminumOre;
import cadiboo.wiptech.block.BlockBase;
import cadiboo.wiptech.block.BlockBauxiteOre;
import cadiboo.wiptech.block.BlockCopperBlock;
import cadiboo.wiptech.block.BlockCopperOre;
import cadiboo.wiptech.block.BlockCopperSpool;
import cadiboo.wiptech.block.BlockGoldSpool;
import cadiboo.wiptech.block.BlockOsmiumOre;
import cadiboo.wiptech.block.BlockStrongPistonBase;
import cadiboo.wiptech.block.BlockTungstenOre;
import cadiboo.wiptech.block.coiler.BlockCoiler;
import cadiboo.wiptech.block.crusher.BlockCrusher;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class Blocks {
	
	//public static final Block-Ore		_ORE			= new BlockOre("_ore", Material.ROCK);
			//public static final BlockTinBlock	TIN_BLOCK		= new BlockTinBlock("tin_block", Material.IRON);
			//public static final BlockTinOre		TIN_ORE			= new BlockTinOre("tin_ore", Material.ROCK);
			
	public static final BlockCopperBlock		COPPER_BLOCK		= new BlockCopperBlock("copper_block", Material.IRON);
	public static final BlockCopperOre		COPPER_ORE		= new BlockCopperOre("copper_ore", Material.ROCK);
	public static final BlockCopperSpool		COPPER_SPOOL		= new BlockCopperSpool("copper_spool", Material.IRON);
	public static final BlockGoldSpool 		GOLD_SPOOL		= new BlockGoldSpool("gold_spool", Material.IRON);
	public static final BlockBauxiteOre		BAUXITE_ORE		= new BlockBauxiteOre("bauxite_ore", Material.ROCK);
	public static final BlockAluminumOre		ALUMINUM_ORE		= new BlockAluminumOre("aluminum_ore", Material.ROCK);
	public static final BlockCrusher			CRUSHER			= new BlockCrusher("crusher", Material.IRON);
	public static final BlockCoiler			COILER			= new BlockCoiler("coiler", Material.IRON);
	public static final BlockStrongPistonBase STRONG_PISTON	= new BlockStrongPistonBase("strong_piston", Material.PISTON);
	public static final BlockTungstenOre		TUNGSTEN_ORE		= new BlockTungstenOre("tungsten_ore", Material.ROCK);
	public static final BlockOsmiumOre		OSMIUM_ORE		= new BlockOsmiumOre("osmium_ore", Material.ROCK);
	
	public static final Block[] BLOCKS = {
			COPPER_BLOCK,
			
			COPPER_ORE,
			BAUXITE_ORE,
			ALUMINUM_ORE,
			TUNGSTEN_ORE,
			OSMIUM_ORE,
			
			COPPER_SPOOL,
			GOLD_SPOOL,
			
			CRUSHER,
			COILER,
			
			STRONG_PISTON,
	};

	private static List<Block> Ores = new ArrayList<Block>();
	private static Boolean OresIterated = false;

	public static List<Block> getOres() {
		if(!OresIterated) {
			for (Block block : BLOCKS) {
				if(!Ores.contains(block)) {
					if(block instanceof BlockBase) {
						if(((BlockBase) block).isOreBlock()) {
							Ores.add(block);
						}
					}
				} else {
					OresIterated = true;
				}
			}
		}
		return Ores;
	}

	private static List<Block> TileEntities = new ArrayList<Block>();
	private static Boolean TileEntitiesIterated = false;

	public static List<Block> getTileEntities() {
		if(!TileEntitiesIterated) {
			for (Block block : BLOCKS) {
				if(!TileEntities.contains(block)) {
					if(((BlockBase) block).isTileEntity()) {
						TileEntities.add(block);
					}
				} else {
					TileEntitiesIterated = true;
				}
			}
		}
		return TileEntities;
	}

}
