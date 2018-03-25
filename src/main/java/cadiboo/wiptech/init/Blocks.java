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
	public static final BlockCopperRail COPPER_RAIL = new BlockCopperRail("copper_rail", Material.CIRCUITS);
	public static final BlockCopperWire COPPER_WIRE = new BlockCopperWire("copper_wire", Material.CIRCUITS);
	public static final BlockCopperNugget COPPER_NUGGET = new BlockCopperNugget("copper_nugget_block", Material.IRON);
	public static final BlockCopperIngot COPPER_INGOT = new BlockCopperIngot("copper_ingot_block", Material.IRON);
	public static final BlockCopperEnamel COPPER_ENAMEL = new BlockCopperEnamel("copper_enamel", Material.CIRCUITS);

	public static final BlockTinBlock TIN_BLOCK = new BlockTinBlock("tin_block", Material.IRON);
	public static final BlockTinOre TIN_ORE = new BlockTinOre("tin_ore", Material.ROCK);
	public static final BlockTinSpool TIN_SPOOL = new BlockTinSpool("tin_spool", Material.IRON);
	public static final BlockTinRail TIN_RAIL = new BlockTinRail("tin_rail", Material.CIRCUITS);
	public static final BlockTinWire TIN_WIRE = new BlockTinWire("tin_wire", Material.CIRCUITS);
	public static final BlockTinNugget TIN_NUGGET = new BlockTinNugget("tin_nugget_block", Material.IRON);
	public static final BlockTinIngot TIN_INGOT = new BlockTinIngot("tin_ingot_block", Material.IRON);
	public static final BlockTinEnamel TIN_ENAMEL = new BlockTinEnamel("tin_enamel", Material.CIRCUITS);

	public static final BlockAluminiumBlock ALUMINIUM_BLOCK = new BlockAluminiumBlock("aluminium_block", Material.IRON);
	public static final BlockAluminiumOre ALUMINIUM_ORE = new BlockAluminiumOre("aluminium_ore", Material.ROCK);
	public static final BlockAluminiumSpool ALUMINIUM_SPOOL = new BlockAluminiumSpool("aluminium_spool", Material.IRON);
	public static final BlockAluminiumRail ALUMINIUM_RAIL = new BlockAluminiumRail("aluminium_rail", Material.CIRCUITS);
	public static final BlockAluminiumWire ALUMINIUM_WIRE = new BlockAluminiumWire("aluminium_wire", Material.CIRCUITS);
	public static final BlockAluminiumNugget ALUMINIUM_NUGGET = new BlockAluminiumNugget("aluminium_nugget_block", Material.IRON);
	public static final BlockAluminiumIngot ALUMINIUM_INGOT = new BlockAluminiumIngot("aluminium_ingot_block", Material.IRON);
	public static final BlockAluminiumEnamel ALUMINIUM_ENAMEL = new BlockAluminiumEnamel("aluminium_enamel", Material.CIRCUITS);

	public static final BlockSilverBlock SILVER_BLOCK = new BlockSilverBlock("silver_block", Material.IRON);
	public static final BlockSilverOre SILVER_ORE = new BlockSilverOre("silver_ore", Material.ROCK);
	public static final BlockSilverSpool SILVER_SPOOL = new BlockSilverSpool("silver_spool", Material.IRON);
	public static final BlockSilverRail SILVER_RAIL = new BlockSilverRail("silver_rail", Material.CIRCUITS);
	public static final BlockSilverWire SILVER_WIRE = new BlockSilverWire("silver_wire", Material.CIRCUITS);
	public static final BlockSilverNugget SILVER_NUGGET = new BlockSilverNugget("silver_nugget_block", Material.IRON);
	public static final BlockSilverIngot SILVER_INGOT = new BlockSilverIngot("silver_ingot_block", Material.IRON);
	public static final BlockSilverEnamel SILVER_ENAMEL = new BlockSilverEnamel("silver_enamel", Material.CIRCUITS);


	public static final BlockGoldSpool GOLD_SPOOL = new BlockGoldSpool("gold_spool", Material.IRON);
	public static final BlockGoldRail GOLD_RAIL = new BlockGoldRail("gold_rail", Material.CIRCUITS);
	public static final BlockGoldWire GOLD_WIRE = new BlockGoldWire("gold_wire", Material.CIRCUITS);
	public static final BlockGoldIngot GOLD_INGOT = new BlockGoldIngot("gold_ingot_block", Material.IRON);
	public static final BlockGoldNugget GOLD_NUGGET = new BlockGoldNugget("gold_nugget_block", Material.IRON);
	public static final BlockGoldEnamel GOLD_ENAMEL = new BlockGoldEnamel("gold_enamel", Material.CIRCUITS);

	public static final BlockBauxiteOre BAUXITE_ORE = new BlockBauxiteOre("bauxite_ore", Material.ROCK);

	public static final BlockTungstenOre TUNGSTEN_ORE = new BlockTungstenOre("tungsten_ore", Material.ROCK);
	public static final BlockTungstenBlock TUNGSTEN_BLOCK = new BlockTungstenBlock("tungsten_block", Material.IRON);

	public static final BlockOsmiumOre OSMIUM_ORE = new BlockOsmiumOre("osmium_ore", Material.ROCK);
	public static final BlockOsmiumBlock OSMIUM_BLOCK = new BlockOsmiumBlock("osmium_block", Material.IRON);

	public static final BlockCrusher CRUSHER = new BlockCrusher("crusher", Material.IRON);
	public static final BlockCoiler COILER = new BlockCoiler("coiler", Material.IRON);

	public static final BlockStrongPistonBase STRONG_PISTON = new BlockStrongPistonBase("strong_piston", null);

	public static final BlockTurbine TURBINE = new BlockTurbine("turbine", Material.IRON);
	public static final BlockCapacitorBank CAPACITOR_BANK = new BlockCapacitorBank("capacitor_bank", Material.IRON);


	public static final Block[] BLOCKS = {

			COPPER_BLOCK,
			COPPER_ORE,
			COPPER_SPOOL,
			COPPER_RAIL,
			COPPER_WIRE,
			COPPER_NUGGET,
			COPPER_INGOT,
			COPPER_ENAMEL,
			
			TIN_BLOCK,
			TIN_ORE,
			TIN_SPOOL,
			TIN_RAIL,
			TIN_WIRE,
			TIN_NUGGET,
			TIN_INGOT,
			TIN_ENAMEL,
			
			ALUMINIUM_BLOCK,
			ALUMINIUM_ORE,
			ALUMINIUM_SPOOL,
			ALUMINIUM_RAIL,
			ALUMINIUM_WIRE,
			ALUMINIUM_NUGGET,
			ALUMINIUM_INGOT,
			ALUMINIUM_ENAMEL,
			
			SILVER_BLOCK,
			SILVER_ORE,
			SILVER_SPOOL,
			SILVER_RAIL,
			SILVER_WIRE,
			SILVER_NUGGET,
			SILVER_INGOT,
			SILVER_ENAMEL,
			
			GOLD_SPOOL,
			GOLD_RAIL,
			GOLD_WIRE,
			GOLD_NUGGET,
			GOLD_INGOT,
			GOLD_ENAMEL,
			
			BAUXITE_ORE,
			TUNGSTEN_ORE,
			TUNGSTEN_BLOCK,
			OSMIUM_ORE,
			OSMIUM_BLOCK,
			
			CRUSHER,
			COILER,
			
			STRONG_PISTON,
			
			TURBINE,
			CAPACITOR_BANK,
	};


	private static List<Block> Ores = new ArrayList();
	private static Boolean OresIterated = Boolean.valueOf(false);

	public static List<Block> getOres()
	{
		if (!OresIterated.booleanValue())
		{
			for (int i = 0; i < BLOCKS.length; i++)
			{
				Block block = BLOCKS[i];
				if (!Ores.contains(block))
				{
					if ((block instanceof BlockBase) && ((BlockBase)block).isOreBlock())
					{
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
			for (int i = 0; i < BLOCKS.length; i++)
			{
				Block block = BLOCKS[i];
				if (!TileEntities.contains(block))
				{
					if (((BlockBase)block).isTileEntity())
					{
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
