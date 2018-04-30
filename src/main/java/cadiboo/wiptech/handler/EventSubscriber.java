package cadiboo.wiptech.handler;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockItem;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Entities;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.tileentity.TileEntityCapacitorBank;
import cadiboo.wiptech.tileentity.TileEntityCoiler;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import cadiboo.wiptech.tileentity.TileEntityTurbine;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.Reference;
import cadiboo.wiptech.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = Reference.ID)
public class EventSubscriber {

	@SubscribeEvent
	public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
		event.getRegistry().registerAll(Entities.ENTITIES);
		WIPTech.logger.info("Registered Entities");
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(cadiboo.wiptech.init.Blocks.BLOCKS);
		WIPTech.logger.info("Registered Blocks");

		GameRegistry.registerTileEntity(TileEntityCrusher.class, Reference.ID + "TileEntityCrusher");
		GameRegistry.registerTileEntity(TileEntityCoiler.class, Reference.ID + "TileEntityCoiler");
		GameRegistry.registerTileEntity(TileEntityTurbine.class, Reference.ID + "TileEntityTurbine");
		GameRegistry.registerTileEntity(TileEntityCapacitorBank.class, Reference.ID + "TileEntityCapacitorBank");
		GameRegistry.registerTileEntity(TileEntityWire.class, Reference.ID + "TileEntityWire");

		WIPTech.logger.info("Registered TileEntities");
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {

		event.getRegistry().registerAll(cadiboo.wiptech.init.Items.ITEMS);

		WIPTech.logger.info("Registered Items");

		for (int i = 0; i < Blocks.BLOCKS.length; i++) {
			if (Blocks.getHiddenBlocks().contains(Blocks.BLOCKS[i]))
				continue;
			event.getRegistry().register(new ItemBlock(Blocks.BLOCKS[i]).setRegistryName(Blocks.BLOCKS[i].getRegistryName()));
		}

		WIPTech.logger.info("And ItemBlocks");

		// for (int i = 0; i < Blocks.getOres().size(); i++) {
		// String name = Blocks.getOres().get(i).getUnlocalizedName().replace("_ore",
		// "").replace("tile.", "");
		// if (name.length() > 0) {
		// name = name.substring(0, 1).toUpperCase() + name.substring(1);
		// OreDictionary.registerOre("ore" + name, Blocks.getOres().get(i));
		// name = null;
		// }
		// }

		// for (int i = 0; i < Items.getIngots().size(); i++) {
		// String name = Items.getIngots().get(i).getUnlocalizedName().replace("_ingot",
		// "").replace("item.", "");
		// if (name.length() > 0) {
		// name = name.substring(0, 1).toUpperCase() + name.substring(1);
		// OreDictionary.registerOre("ingot" + name, Items.getIngots().get(i));
		// name = null;
		// }
		// }
		//
		// for (int i = 0; i < Items.getNuggets().size(); i++) {
		// String name =
		// Items.getNuggets().get(i).getUnlocalizedName().replace("_nugget",
		// "").replace("item.", "");
		// if (name.length() > 0) {
		// name = name.substring(0, 1).toUpperCase() + name.substring(1);
		// OreDictionary.registerOre("nugget" + name, Items.getNuggets().get(i));
		// name = null;
		// }
		// }

		WIPTech.logger.error("use the 1.13 equivalent of ore dict! ore dict is already being phased out in this mod");
		WIPTech.logger.info("Registered OreDictionary");

	}

	private static boolean isBlockItem(Item item) {
		return Block.getBlockFromItem(item) instanceof BlockItem || item == Items.IRON_INGOT || item == Items.GOLD_INGOT || item == Items.IRON_NUGGET || item == Items.GOLD_NUGGET;
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static EnumActionResult BlockRightClickEvent(final PlayerInteractEvent.RightClickBlock event) {
		if (!(Utils.getBlockFromPos(event.getWorld(), event.getPos()) instanceof BlockAnvil) || event.getFace() != EnumFacing.UP)
			return EnumActionResult.PASS;

		if (Utils.getBlockFromPos(event.getWorld(), event.getPos().up()) instanceof BlockItem
				&& (isBlockItem(event.getItemStack().getItem()) || isBlockItem(event.getEntityPlayer().getHeldItem(EnumHand.values()[event.getHand().ordinal() ^ 1]).getItem()))) {
			event.setCanceled(true);
			return EnumActionResult.FAIL;
		}

		// should be put on more than one line
		// event stack is placeable ||(other hand stack is placeable && event stack=air)
		if (isBlockItem(event.getItemStack().getItem())
				|| (isBlockItem(event.getEntityPlayer().getHeldItem(EnumHand.values()[event.getHand().ordinal() ^ 1]).getItem()) && event.getItemStack().isEmpty())) {
			if (Blocks.COPPER_INGOT.canPlaceBlockAt(event.getWorld(), event.getPos().up())) {
				event.getWorld().setBlockState(event.getPos().up(), BlockItem.getBlockToPlace(event.getItemStack().getItem()).getStateForPlacement(event.getWorld(), event.getPos().up(),
						event.getFace(), (float) event.getHitVec().x, (float) event.getHitVec().y, (float) event.getHitVec().z, event.getItemStack().getMetadata(), event.getEntityPlayer()), 2);
				if (!event.getEntityPlayer().isCreative())
					event.getItemStack().shrink(1);
				event.setCanceled(true);
				return EnumActionResult.FAIL;
			}
		}

		return EnumActionResult.PASS;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void BlockBreakEvent(final BlockEvent.BreakEvent event) {
		if (event.getState().getBlock() instanceof BlockAnvil) {
			if (!(Utils.getBlockFromPos(event.getWorld(), event.getPos().up()) instanceof BlockItem))
				return;
			event.getWorld().destroyBlock(event.getPos().up(), !event.getPlayer().isCreative());
		}

	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onHarvest(final BlockEvent.HarvestDropsEvent event) {
		if (event.getHarvester() == null)
			return;
		if (!(event.getState().getBlock() instanceof BlockItem))
			return;
		if (!(Utils.getBlockFromPos(event.getWorld(), event.getPos().down()) instanceof BlockAnvil))
			return;
		if (event.getHarvester().getHeldItem(EnumHand.values()[event.getHarvester().getActiveHand().ordinal()]).getItem() != Items.HAMMER)
			return;

		// TODO un-jerry-rig this and make it not hard coded
		if (event.getState().getBlock() == Blocks.COPPER_INGOT)
			event.getDrops().set(0, new ItemStack(Items.COPPER_RAIL));
		else if (event.getState().getBlock() == Blocks.COPPER_NUGGET)
			event.getDrops().set(0, new ItemStack(Blocks.COPPER_WIRE));
		else if (event.getState().getBlock() == Blocks.TIN_INGOT)
			event.getDrops().set(0, new ItemStack(Items.TIN_RAIL));
		else if (event.getState().getBlock() == Blocks.TIN_NUGGET)
			event.getDrops().set(0, new ItemStack(Blocks.TIN_WIRE));
		else if (event.getState().getBlock() == Blocks.ALUMINIUM_INGOT)
			event.getDrops().set(0, new ItemStack(Items.ALUMINIUM_RAIL));
		else if (event.getState().getBlock() == Blocks.ALUMINIUM_NUGGET)
			event.getDrops().set(0, new ItemStack(Blocks.ALUMINIUM_WIRE));
		else if (event.getState().getBlock() == Blocks.SILVER_INGOT)
			event.getDrops().set(0, new ItemStack(Items.SILVER_RAIL));
		else if (event.getState().getBlock() == Blocks.SILVER_NUGGET)
			event.getDrops().set(0, new ItemStack(Blocks.SILVER_WIRE));
		else if (event.getState().getBlock() == Blocks.IRON_INGOT)
			event.getDrops().set(0, new ItemStack(Items.IRON_RAIL));
		else if (event.getState().getBlock() == Blocks.IRON_NUGGET)
			event.getDrops().set(0, new ItemStack(Blocks.IRON_WIRE));
		else if (event.getState().getBlock() == Blocks.GOLD_INGOT)
			event.getDrops().set(0, new ItemStack(Items.GOLD_RAIL));
		else if (event.getState().getBlock() == Blocks.GOLD_NUGGET)
			event.getDrops().set(0, new ItemStack(Blocks.GOLD_WIRE));

	}

}