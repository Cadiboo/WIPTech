package cadiboo.wiptech.init;

import cadiboo.wiptech.block.*;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class Blocks
{
	public static final BlockCopperBlock COPPER_BLOCK = new BlockCopperBlock("copper_block", Material.IRON);
	public static final BlockCopperOre COPPER_ORE = new BlockCopperOre("copper_ore", Material.ROCK);
	public static final BlockCopperSpool COPPER_SPOOL = new BlockCopperSpool("copper_spool", Material.IRON);
	public static final BlockGoldSpool GOLD_SPOOL = new BlockGoldSpool("gold_spool", Material.IRON);
	public static final BlockBauxiteOre BAUXITE_ORE = new BlockBauxiteOre("bauxite_ore", Material.ROCK);
	public static final BlockAluminiumOre ALUMINIUM_ORE = new BlockAluminiumOre("aluminium_ore", Material.ROCK);
	public static final BlockCrusher CRUSHER = new BlockCrusher("crusher", Material.IRON);
	public static final BlockCoiler COILER = new BlockCoiler("coiler", Material.IRON);
	public static final BlockStrongPistonBase STRONG_PISTON = new BlockStrongPistonBase("strong_piston", null);
	public static final BlockTungstenOre TUNGSTEN_ORE = new BlockTungstenOre("tungsten_ore", Material.ROCK);
	public static final BlockOsmiumOre OSMIUM_ORE = new BlockOsmiumOre("osmium_ore", Material.ROCK);
	public static final BlockCopperIngot COPPER_INGOT = new BlockCopperIngot("copper_ingot_block", Material.IRON);
	public static final BlockGoldIngot GOLD_INGOT = new BlockGoldIngot("gold_ingot_block", Material.IRON);
	public static final BlockCopperNugget COPPER_NUGGET = new BlockCopperNugget("copper_nugget_block", Material.IRON);
	public static final BlockGoldNugget GOLD_NUGGET = new BlockGoldNugget("gold_nugget_block", Material.IRON);
	public static final BlockCopperRail COPPER_RAIL = new BlockCopperRail("copper_rail", Material.CIRCUITS);
	public static final BlockCopperWire COPPER_WIRE = new BlockCopperWire("copper_wire", Material.CIRCUITS);
	public static final BlockGoldRail GOLD_RAIL = new BlockGoldRail("gold_rail", Material.CIRCUITS);
	public static final BlockGoldWire GOLD_WIRE = new BlockGoldWire("gold_wire", Material.CIRCUITS);
	public static final BlockCopperEnamel COPPER_ENAMEL = new BlockCopperEnamel("copper_enamel", Material.CIRCUITS);
	public static final BlockGoldEnamel GOLD_ENAMEL = new BlockGoldEnamel("gold_enamel", Material.CIRCUITS);
	public static final BlockTurbine TURBINE = new BlockTurbine("turbine", Material.IRON);
	public static final BlockCapacitorBank CAPACITOR_BANK = new BlockCapacitorBank("capacitor_bank", Material.IRON);
	
	
	public static final Block[] BLOCKS = {

			COPPER_BLOCK, 

			COPPER_ORE, 
			BAUXITE_ORE, 
			ALUMINIUM_ORE, 
			OSMIUM_ORE, 
			TUNGSTEN_ORE, 

			COPPER_SPOOL, 
			GOLD_SPOOL, 

			CRUSHER, 
			COILER, 
			
			TURBINE,
			CAPACITOR_BANK,

			STRONG_PISTON, 
			
			COPPER_INGOT, 
			GOLD_INGOT, 
			COPPER_NUGGET, 
			GOLD_NUGGET, 
			COPPER_RAIL, 
			GOLD_RAIL, 
			COPPER_WIRE, 
			GOLD_WIRE, 
			COPPER_ENAMEL, 
			GOLD_ENAMEL
	};
	
	
	private static List<Block> Ores = new ArrayList();
	private static Boolean OresIterated = Boolean.valueOf(false);

	public static List<Block> getOres()
	{
		if (!OresIterated.booleanValue())
		{
			Block[] arrayOfBlock;
			int j = (arrayOfBlock = BLOCKS).length;
			for (int i = 0; i < j; i++)
			{
				Block block = arrayOfBlock[i];
				if (!Ores.contains(block))
				{
					if (((block instanceof BlockBase)) && 
							(((BlockBase)block).isOreBlock()))
					{
						block.setHardness(3.0F);
						Ores.add(block);
					}
				}
				else {
					OresIterated = Boolean.valueOf(true);
				}
			}
		}
		return Ores;
	}

	private static List<Block> TileEntities = new ArrayList();
	private static Boolean TileEntitiesIterated = Boolean.valueOf(false);

	public static List<Block> getTileEntities()
	{
		if (!TileEntitiesIterated.booleanValue())
		{
			Block[] arrayOfBlock;
			int j = (arrayOfBlock = BLOCKS).length;
			for (int i = 0; i < j; i++)
			{
				Block block = arrayOfBlock[i];
				if (!TileEntities.contains(block))
				{
					if (((BlockBase)block).isTileEntity())
					{
						block.setHardness(4.0F);
						TileEntities.add(block);
					}
				}
				else {
					TileEntitiesIterated = Boolean.valueOf(true);
				}
			}
		}
		return TileEntities;
	}
}
