package cadiboo.wiptech.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import cadiboo.wiptech.block.BlockBase;
import cadiboo.wiptech.block.BlockCapacitorBank;
import cadiboo.wiptech.block.BlockCoiler;
import cadiboo.wiptech.block.BlockCrusher;
import cadiboo.wiptech.block.BlockItem;
import cadiboo.wiptech.block.BlockOre;
import cadiboo.wiptech.block.BlockPeripheralBlock;
import cadiboo.wiptech.block.BlockResourceBlock;
import cadiboo.wiptech.block.BlockSpool;
import cadiboo.wiptech.block.BlockStrongPistonBase;
import cadiboo.wiptech.block.BlockStrongPistonExtension;
import cadiboo.wiptech.block.BlockTurbine;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.handler.EnumHandler.BlockItems;
import cadiboo.wiptech.handler.EnumHandler.ConductiveMetals;
import cadiboo.wiptech.handler.EnumHandler.Ores;
import cadiboo.wiptech.handler.EnumHandler.ResourceBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class Blocks {

	// 1.13
	public static final BlockOre	COPPER_ORE		= new BlockOre("copper_ore", Material.ROCK, Ores.COPPER);
	public static final BlockOre	TIN_ORE			= new BlockOre("tin_ore", Material.ROCK, Ores.TIN);
	public static final BlockOre	ALUMINIUM_ORE	= new BlockOre("aluminium_ore", Material.ROCK, Ores.ALUMINIUM);
	public static final BlockOre	SILVER_ORE		= new BlockOre("silver_ore", Material.ROCK, Ores.SILVER);
	public static final BlockOre	BAUXITE_ORE		= new BlockOre("bauxite_ore", Material.ROCK, Ores.BAUXITE);
	public static final BlockOre	OSMIUM_ORE		= new BlockOre("osmium_ore", Material.ROCK, Ores.OSMIUM);
	public static final BlockOre	TUNGSTEN_ORE	= new BlockOre("tungsten_ore", Material.ROCK, Ores.TUNGSTEN);

	public static final BlockResourceBlock	COPPER_BLOCK	= new BlockResourceBlock("copper_block", Material.IRON, ResourceBlocks.COPPER);
	public static final BlockResourceBlock	TIN_BLOCK		= new BlockResourceBlock("tin_block", Material.IRON, ResourceBlocks.TIN);
	public static final BlockResourceBlock	ALUMINIUM_BLOCK	= new BlockResourceBlock("aluminium_block", Material.IRON, ResourceBlocks.ALUMINIUM);
	public static final BlockResourceBlock	SILVER_BLOCK	= new BlockResourceBlock("silver_block", Material.IRON, ResourceBlocks.SILVER);
	public static final BlockResourceBlock	OSMIUM_BLOCK	= new BlockResourceBlock("osmium_block", Material.IRON, ResourceBlocks.OSMIUM);
	public static final BlockResourceBlock	TUNGSTEN_BLOCK	= new BlockResourceBlock("tungsten_block", Material.IRON, ResourceBlocks.TUNGSTEN);
	public static final BlockResourceBlock	TITANIUM_BLOCK	= new BlockResourceBlock("titanium_block", Material.IRON, ResourceBlocks.TITANIUM);

	public static final BlockSpool	COPPER_SPOOL	= new BlockSpool("copper_spool", Material.IRON, ConductiveMetals.COPPER);
	public static final BlockSpool	TIN_SPOOL		= new BlockSpool("tin_spool", Material.IRON, ConductiveMetals.TIN);
	public static final BlockSpool	ALUMINIUM_SPOOL	= new BlockSpool("aluminium_spool", Material.IRON, ConductiveMetals.ALUMINIUM);
	public static final BlockSpool	SILVER_SPOOL	= new BlockSpool("silver_spool", Material.IRON, ConductiveMetals.SILVER);
	public static final BlockSpool	IRON_SPOOL		= new BlockSpool("iron_spool", Material.IRON, ConductiveMetals.IRON);
	public static final BlockSpool	GOLD_SPOOL		= new BlockSpool("gold_spool", Material.IRON, ConductiveMetals.GOLD);

	public static final BlockItem	COPPER_INGOT	= new BlockItem("copper_ingot", Material.IRON, BlockItems.COPPER_INGOT);
	public static final BlockItem	TIN_INGOT		= new BlockItem("tin_ingot", Material.IRON, BlockItems.TIN_INGOT);
	public static final BlockItem	ALUMINIUM_INGOT	= new BlockItem("aluminium_ingot", Material.IRON, BlockItems.ALUMINIUM_INGOT);
	public static final BlockItem	SILVER_INGOT	= new BlockItem("silver_ingot", Material.IRON, BlockItems.SILVER_INGOT);
	public static final BlockItem	IRON_INGOT		= (BlockItem) new BlockItem("iron_ingot", Material.IRON, BlockItems.IRON_INGOT).setHiddenBlock();
	public static final BlockItem	GOLD_INGOT		= (BlockItem) new BlockItem("gold_ingot", Material.IRON, BlockItems.GOLD_INGOT).setHiddenBlock();

	public static final BlockItem	COPPER_NUGGET		= new BlockItem("copper_nugget", Material.IRON, BlockItems.COPPER_NUGGET);
	public static final BlockItem	TIN_NUGGET			= new BlockItem("tin_nugget", Material.IRON, BlockItems.TIN_NUGGET);
	public static final BlockItem	ALUMINIUM_NUGGET	= new BlockItem("aluminium_nugget", Material.IRON, BlockItems.ALUMINIUM_NUGGET);
	public static final BlockItem	SILVER_NUGGET		= new BlockItem("silver_nugget", Material.IRON, BlockItems.SILVER_NUGGET);
	public static final BlockItem	IRON_NUGGET			= (BlockItem) new BlockItem("iron_nugget", Material.IRON, BlockItems.IRON_NUGGET).setHiddenBlock();
	public static final BlockItem	GOLD_NUGGET			= (BlockItem) new BlockItem("gold_nugget", Material.IRON, BlockItems.GOLD_NUGGET).setHiddenBlock();

	public static final BlockWire	COPPER_WIRE		= new BlockWire("copper_wire", Material.IRON, ConductiveMetals.COPPER, false);
	public static final BlockWire	TIN_WIRE		= new BlockWire("tin_wire", Material.IRON, ConductiveMetals.TIN, false);
	public static final BlockWire	ALUMINIUM_WIRE	= new BlockWire("aluminium_wire", Material.IRON, ConductiveMetals.ALUMINIUM, false);
	public static final BlockWire	SILVER_WIRE		= new BlockWire("silver_wire", Material.IRON, ConductiveMetals.SILVER, false);
	public static final BlockWire	IRON_WIRE		= new BlockWire("iron_wire", Material.IRON, ConductiveMetals.IRON, false);
	public static final BlockWire	GOLD_WIRE		= new BlockWire("gold_wire", Material.IRON, ConductiveMetals.GOLD, false);

	public static final BlockWire	COPPER_ENAMEL		= new BlockWire("copper_enamel", Material.IRON, ConductiveMetals.COPPER, true);
	public static final BlockWire	TIN_ENAMEL			= new BlockWire("tin_enamel", Material.IRON, ConductiveMetals.TIN, true);
	public static final BlockWire	ALUMINIUM_ENAMEL	= new BlockWire("aluminium_enamel", Material.IRON, ConductiveMetals.ALUMINIUM, true);
	public static final BlockWire	SILVER_ENAMEL		= new BlockWire("silver_enamel", Material.IRON, ConductiveMetals.SILVER, true);
	public static final BlockWire	IRON_ENAMEL			= new BlockWire("iron_enamel", Material.IRON, ConductiveMetals.IRON, true);
	public static final BlockWire	GOLD_ENAMEL			= new BlockWire("gold_enamel", Material.IRON, ConductiveMetals.GOLD, true);

	// 1.12
	public static final BlockCrusher	CRUSHER	= new BlockCrusher("crusher", Material.IRON);
	public static final BlockCoiler		COILER	= new BlockCoiler("coiler", Material.IRON);

	public static final BlockStrongPistonBase		STRONG_PISTON			= new BlockStrongPistonBase("strong_piston", false);
	public static final BlockStrongPistonBase		STRONG_PISTON_STICKY	= new BlockStrongPistonBase("strong_piston_sticky", true);
	public static final BlockStrongPistonExtension	STRONG_PISTON_HEAD		= new BlockStrongPistonExtension("strong_piston_head");

	public static final BlockTurbine			TURBINE			= new BlockTurbine("turbine", Material.IRON);
	public static final BlockPeripheralBlock	PERIPHERAL		= new BlockPeripheralBlock("peripheral", Material.IRON);
	public static final BlockCapacitorBank		CAPACITOR_BANK	= new BlockCapacitorBank("capacitor_bank", Material.IRON);

	public static final Block[] BLOCKS = {

			COPPER_ORE, TIN_ORE, ALUMINIUM_ORE, SILVER_ORE, BAUXITE_ORE, OSMIUM_ORE, TUNGSTEN_ORE,

			COPPER_BLOCK, TIN_BLOCK, ALUMINIUM_BLOCK, SILVER_BLOCK, OSMIUM_BLOCK, TUNGSTEN_BLOCK, TITANIUM_BLOCK,

			// COPPER_, TIN_, ALUMINIUM_, SILVER_, IRON_, GOLD_,

			COPPER_SPOOL, TIN_SPOOL, ALUMINIUM_SPOOL, SILVER_SPOOL, IRON_SPOOL, GOLD_SPOOL,

			COPPER_INGOT, TIN_INGOT, ALUMINIUM_INGOT, SILVER_INGOT, IRON_INGOT, GOLD_INGOT,

			COPPER_NUGGET, TIN_NUGGET, ALUMINIUM_NUGGET, SILVER_NUGGET, IRON_NUGGET, GOLD_NUGGET,

			COPPER_WIRE, TIN_WIRE, ALUMINIUM_WIRE, SILVER_WIRE, IRON_WIRE, GOLD_WIRE,

			COPPER_ENAMEL, TIN_ENAMEL, ALUMINIUM_ENAMEL, SILVER_ENAMEL, IRON_ENAMEL, GOLD_ENAMEL,

			CRUSHER, COILER,

			STRONG_PISTON, STRONG_PISTON_STICKY, STRONG_PISTON_HEAD,

			TURBINE, PERIPHERAL, /* CAPACITOR_BANK, */ };

	public static final Set<Block> HIDDEN_BLOCKS = Sets.newHashSet(STRONG_PISTON_HEAD, PERIPHERAL);

	public static Set<Block> getHiddenBlocks() {
		for (int i = 0; i < BLOCKS.length; i++) {
			if (BLOCKS[i] instanceof BlockBase && ((BlockBase) Blocks.BLOCKS[i]).isHiddenBlock())
				HIDDEN_BLOCKS.add(BLOCKS[i]);
		}
		return HIDDEN_BLOCKS;
	}

	public static List<Block> getOres() {
		List<Block> OreBlocks = new ArrayList<Block>();
		for (int i = 0; i < BLOCKS.length; i++) {
			if (BLOCKS[i] instanceof BlockOre)
				OreBlocks.add(BLOCKS[i]);
		}
		return OreBlocks;
	}

	public static final Set<Block> TILE_ENTITIES = Sets.newHashSet();

	public static Set<Block> getTileEntities() {
		for (int i = 0; i < BLOCKS.length; i++) {
			if (BLOCKS[i] instanceof BlockBase && ((BlockBase) Blocks.BLOCKS[i]).isTileEntity())
				HIDDEN_BLOCKS.add(BLOCKS[i]);
		}
		return HIDDEN_BLOCKS;
	}

	public static List<Block> getBlockItems() {
		List<Block> BlockItems = new ArrayList<Block>();
		for (int i = 0; i < BLOCKS.length; i++) {
			if (BLOCKS[i] instanceof BlockItem)
				BlockItems.add(BLOCKS[i]);
		}
		return BlockItems;
	}

}
