package cadiboo.wiptech.handler;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockItem;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Entities;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.tileentity.TileEntityCapacitorBank;
import cadiboo.wiptech.tileentity.TileEntityCoiler;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import cadiboo.wiptech.tileentity.TileEntityPeripheral;
import cadiboo.wiptech.tileentity.TileEntityTurbine;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.Reference;
import cadiboo.wiptech.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = Reference.ID)
public class EventSubscriber {

	@SubscribeEvent
	public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
		event.getRegistry().registerAll(Entities.ENTITIES);
		WIPTech.info("Registered Entities");
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(cadiboo.wiptech.init.Blocks.BLOCKS);
		WIPTech.info("Registered Blocks");

		GameRegistry.registerTileEntity(TileEntityCrusher.class, Reference.ID + "TileEntityCrusher");
		GameRegistry.registerTileEntity(TileEntityCoiler.class, Reference.ID + "TileEntityCoiler");
		GameRegistry.registerTileEntity(TileEntityTurbine.class, Reference.ID + "TileEntityTurbine");
		GameRegistry.registerTileEntity(TileEntityCapacitorBank.class, Reference.ID + "TileEntityCapacitorBank");
		GameRegistry.registerTileEntity(TileEntityWire.class, Reference.ID + "TileEntityWire");
		GameRegistry.registerTileEntity(TileEntityAssemblyTable.class, Reference.ID + "TileEntityAssemblyTable");
		GameRegistry.registerTileEntity(TileEntityPeripheral.class, Reference.ID + "TileEntityPeripheral");
		WIPTech.info("Registered TileEntities");
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(cadiboo.wiptech.init.Items.ITEMS);
		WIPTech.info("Registered Items");

		for (int i = 0; i < Blocks.BLOCKS.length; i++) {
			if (Blocks.getHiddenBlocks().contains(Blocks.BLOCKS[i]))
				continue;
			event.getRegistry().register(new ItemBlock(Blocks.BLOCKS[i]).setRegistryName(Blocks.BLOCKS[i].getRegistryName()));
		}
		WIPTech.info("And ItemBlocks");

		WIPTech.error("use the 1.13 vanilla equivalent of ore dict! ore dict is already being phased out in this mod");
		// WIPTech.logger.info("Registered OreDictionary");

	}

	@SubscribeEvent
	public static void registerRecipes(final RegistryEvent.Register<IRecipe> event) {
		WIPTech.info("registerRecipes", event);
	}

	private static Block getBlockItem(Item item) {
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Reference.ID, item.getRegistryName().getResourcePath()));
		// TODO make an API for this
	}

	private static boolean isFromCompatibleMod(String resourceDomain) {
		return Utils.isEqual(resourceDomain, "minecraft", Reference.ID);
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static EnumActionResult BlockRightClickEvent(final PlayerInteractEvent.RightClickBlock event) {
		// is anvil & face=up
		if (!(Utils.getBlockFromPos(event.getWorld(), event.getPos()) instanceof BlockAnvil) || event.getFace() != EnumFacing.UP)
			return EnumActionResult.PASS;
		// make stack to check the other hand's stack if this stack is empty
		ItemStack stack = event.getItemStack();
		ItemStack otherStack = event.getEntityPlayer().getHeldItem(EnumHand.values()[event.getHand().ordinal() ^ 1]);
		if (stack.isEmpty() && !otherStack.isEmpty()) {
			stack = otherStack;
			otherStack = event.getItemStack();
		}

		// stop other hand from activating it
		if (Utils.getBlockFromPos(event.getWorld(), event.getPos().up()) instanceof BlockItem && (getBlockItem(stack.getItem()) != null || getBlockItem(otherStack.getItem()) != null)) {
			event.setCanceled(true);
			return EnumActionResult.FAIL;
		}
		// TODO api?
		if (!isFromCompatibleMod(event.getItemStack().getItem().getRegistryName().getResourceDomain()))
			return EnumActionResult.PASS;
		// TODO API!

		// not tungsten/osmium/titanium
		if (event.getItemStack().getItem().getRegistryName().getResourceDomain().equals(Reference.ID) && !(Block.getBlockFromItem(stack.getItem()) instanceof BlockItem))
			return EnumActionResult.PASS;

		// get the block
		Block block = getBlockItem(stack.getItem());

		// make sure the block is placeable
		if (block == null)
			return EnumActionResult.PASS;
		// make sure the block can be placed at the location
		if (!(Utils.getBlockFromPos(event.getWorld(), event.getPos()) instanceof BlockAnvil) || !Utils.getBlockFromPos(event.getWorld(), event.getPos().up()).isReplaceable(event.getWorld(), event.getPos().up()))
			return EnumActionResult.PASS;
		// place the block
		event.getWorld().setBlockState(event.getPos().up(),
				block.getStateForPlacement(event.getWorld(), event.getPos().up(), event.getFace(), (float) event.getHitVec().x, (float) event.getHitVec().y, (float) event.getHitVec().z, event.getItemStack().getMetadata(), event.getEntityPlayer()),
				2);
		if (!event.getEntityPlayer().isCreative())
			event.getItemStack().shrink(1);
		event.setCanceled(true);
		return EnumActionResult.FAIL;

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
		// make sure they can hammer it
		if (event.getHarvester().getHeldItem(EnumHand.values()[event.getHarvester().getActiveHand().ordinal()]).getItem() != Items.HAMMER)
			return;
		// get the name
		String[] res = event.getState().getBlock().getRegistryName().getResourcePath().split("_");
		if (res.length < 2)
			return;
		// set the drops
		event.getDrops().set(0, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Reference.ID, res[0] + (res[1].equals("ingot") ? "_rail" : "_wire")))));
	}

}