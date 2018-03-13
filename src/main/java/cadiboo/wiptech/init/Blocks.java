package cadiboo.wiptech.init;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.block.BlockBase;
import cadiboo.wiptech.block.BlockCopperOre;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class Blocks {
	
	public static final BlockCopperOre COPPER_ORE = new BlockCopperOre("copper_ore", Material.ROCK);
	
	public static final Block[] BLOCKS = {
		
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
