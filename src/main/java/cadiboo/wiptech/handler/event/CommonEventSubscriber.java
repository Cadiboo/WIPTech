package cadiboo.wiptech.handler.event;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockBase;
import cadiboo.wiptech.block.BlockIngotBase;
import cadiboo.wiptech.block.BlockNuggetBase;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Entities;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.init.Recipes;
import cadiboo.wiptech.item.ItemHammer;
import cadiboo.wiptech.tileentity.TileEntityCapacitorBank;
import cadiboo.wiptech.tileentity.TileEntityCoiler;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import cadiboo.wiptech.tileentity.TileEntityTurbine;
import cadiboo.wiptech.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber(modid = Reference.ID)
public class CommonEventSubscriber {

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		event.getRegistry().registerAll(Entities.ENTITIES);
		WIPTech.logger.info("Registered Entities");
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(cadiboo.wiptech.init.Blocks.BLOCKS);
		WIPTech.logger.info("Registered Blocks");

		GameRegistry.registerTileEntity(TileEntityCrusher.class, Reference.ID + "TileEntityCrusher");
		GameRegistry.registerTileEntity(TileEntityCoiler.class, Reference.ID + "TileEntityCoiler");
		GameRegistry.registerTileEntity(TileEntityTurbine.class, Reference.ID + "TileEntityTurbine");
		GameRegistry.registerTileEntity(TileEntityCapacitorBank.class, Reference.ID + "TileEntityCapacitorBank");

		WIPTech.logger.info("Registered TileEntities");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {

		event.getRegistry().registerAll(cadiboo.wiptech.init.Items.ITEMS);

		WIPTech.logger.info("Registered Items");

		for (int i = 0; i < Blocks.BLOCKS.length; i++) {
			if (Blocks.BLOCKS[i] instanceof BlockBase && !((BlockBase) Blocks.BLOCKS[i]).isHiddenBlock())
				event.getRegistry()
						.register(new ItemBlock(Blocks.BLOCKS[i]).setRegistryName(Blocks.BLOCKS[i].getRegistryName()));
		}

		WIPTech.logger.info("And ItemBlocks");

		for (int i = 0; i < Blocks.getOres().size(); i++) {
			String name = Blocks.getOres().get(i).getUnlocalizedName().replace("_ore", "").replace("tile.", "");
			if (name.length() > 0) {
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				OreDictionary.registerOre("ore" + name, Blocks.getOres().get(i));
				name = null;
			}
		}

		for (int i = 0; i < Items.getIngots().size(); i++) {
			String name = Items.getIngots().get(i).getUnlocalizedName().replace("_ingot", "").replace("item.", "");
			if (name.length() > 0) {
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				OreDictionary.registerOre("ingot" + name, Items.getIngots().get(i));
				name = null;
			}
		}

		for (int i = 0; i < Items.getNuggets().size(); i++) {
			String name = Items.getNuggets().get(i).getUnlocalizedName().replace("_nugget", "").replace("item.", "");
			if (name.length() > 0) {
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				OreDictionary.registerOre("nugget" + name, Items.getNuggets().get(i));
				name = null;
			}
		}

		WIPTech.logger.info("Registered OreDictionary");

	}

	/*
	 * @SubscribeEvent public static void
	 * registerOreDict(RegistryEvent.Register<NOPE> event) {
	 * 
	 * }
	 */

	public static final Item GOLD_NUGGET = Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_nugget"));
	public static final Item GOLD_INGOT = Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_ingot"));
	public static final Item IRON_NUGGET = Item.REGISTRY.getObject(new ResourceLocation("minecraft", "iron_nugget"));
	public static final Item IRON_INGOT = Item.REGISTRY.getObject(new ResourceLocation("minecraft", "iron_ingot"));

	private static boolean isItemPlaceable(Item item) {
		return item == GOLD_NUGGET || item == GOLD_INGOT || item == IRON_NUGGET || item == IRON_INGOT
				|| Items.getNuggets().contains(item) || Items.getIngots().contains(item);
	}

	private static Block blockToPlace(Item item) {

		if (item == GOLD_NUGGET)
			return cadiboo.wiptech.init.Blocks.GOLD_NUGGET;
		else if (item == GOLD_INGOT)
			return cadiboo.wiptech.init.Blocks.GOLD_INGOT;
		else if (item == IRON_NUGGET)
			return cadiboo.wiptech.init.Blocks.IRON_NUGGET;
		else if (item == IRON_INGOT)
			return cadiboo.wiptech.init.Blocks.IRON_INGOT;
		// TODO redo these to that they are a FINAL list
		else if (Items.getNuggets().contains(item))
			return ForgeRegistries.BLOCKS.getValue(
					new ResourceLocation(Reference.ID, item.getUnlocalizedName().replace("item.", "") + "_block"));
		else if (Items.getIngots().contains(item))
			return ForgeRegistries.BLOCKS.getValue(
					new ResourceLocation(Reference.ID, item.getUnlocalizedName().replace("item.", "") + "_block"));

		return net.minecraft.init.Blocks.AIR;
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static EnumActionResult BlockRightClickEvent(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		EntityPlayer player = event.getEntityPlayer();
		Item cachedItem = event.getItemStack().getItem();

		if (!(world.getBlockState(pos).getBlock() instanceof BlockAnvil) || event.getFace() != EnumFacing.UP)
			return EnumActionResult.PASS;

		if (!(world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up()))) {
			if (Blocks.getIngotBlocks().contains((world.getBlockState(pos.up()).getBlock()))
					|| Blocks.getNuggetBlocks().contains((world.getBlockState(pos.up()).getBlock()))) {
				event.setCanceled(true);
				return EnumActionResult.FAIL;
			}
			return EnumActionResult.PASS;
		}

		boolean flag = false;

		if (!isItemPlaceable(cachedItem))
			flag = isItemPlaceable(player.getHeldItem(EnumHand.values()[event.getHand().ordinal() ^ 1]).getItem())
					&& event.getItemStack().isEmpty();
		else // placeable
		{
			if (world.isRemote) {
				EnumHand hand = flag ? EnumHand.values()[event.getHand().ordinal() ^ 1] : event.getHand();
				player.setActiveHand(hand);
				player.swingArm(hand);
			}
			event.getWorld().setBlockState(event.getPos().up(), blockToPlace(cachedItem).getDefaultState()
					.withProperty(BlockIngotBase.FACING, world.getBlockState(pos).getValue(BlockAnvil.FACING)));
			if (!player.isCreative())
				event.getItemStack().shrink(1);
		}
		if (flag || isItemPlaceable(cachedItem)) {
			event.setCanceled(true);
			return EnumActionResult.FAIL;
		}
		return EnumActionResult.PASS;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onHarvest(BlockEvent.HarvestDropsEvent event) {
		if (event.getHarvester() == null)
			return;
		if (!(event.getWorld().getBlockState(event.getPos().down()).getBlock() instanceof BlockAnvil))
			return;
		if (!Blocks.getIngotBlocks().contains(event.getState().getBlock())
				&& !Blocks.getNuggetBlocks().contains(event.getState().getBlock()))
			return;
		ItemStack itemStackToDrop = new ItemStack(BlockNuggetBase.getItemToDrop(event.getState().getBlock()));
		if (event.getHarvester().getHeldItemMainhand().getItem() instanceof ItemHammer)
			if (event.getDrops().size() > 0)
				for (int i = 0; i < event.getDrops().size(); i++) {
					if (ItemStack.areItemsEqualIgnoreDurability(event.getDrops().get(i), itemStackToDrop)) {
						event.getDrops().set(i, ((ItemStack) Recipes.getHammerResult(itemStackToDrop).get(1)).copy());
						return;
					}
				}
		event.getDrops().add(((ItemStack) Recipes.getHammerResult(itemStackToDrop).get(1)).copy());
		WIPTech.logger.info(event.getDrops().get(0));
		WIPTech.logger.info(itemStackToDrop);
		itemStackToDrop = null;
	}
}