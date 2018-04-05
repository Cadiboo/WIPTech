package cadiboo.wiptech.handler.event;

import java.util.List;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockBase;
import cadiboo.wiptech.block.BlockCrusher;
import cadiboo.wiptech.client.render.entity.RenderEntityFerromagneticProjectileFactory;
import cadiboo.wiptech.client.render.entity.RenderEntityNapalmFactory;
import cadiboo.wiptech.client.render.tileentity.TESRCrusher;
import cadiboo.wiptech.client.render.tileentity.TESRTurbine;
import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile;
import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.handler.EnumHandler;
import cadiboo.wiptech.handler.EnumHandler.FerromagneticProjectiles;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Entities;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.init.Recipes;
import cadiboo.wiptech.item.ItemFerromagneticProjectile;
import cadiboo.wiptech.item.ItemHammer;
import cadiboo.wiptech.tileentity.TileEntityCapacitorBank;
import cadiboo.wiptech.tileentity.TileEntityCoiler;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import cadiboo.wiptech.tileentity.TileEntityTurbine;
import cadiboo.wiptech.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber
public class EventSubscriber {

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

	//TODO this was just for testing remove me
	public static final Item xxx = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "gold_nugget"));
	
	public static final Item gold_nugget = Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_nugget"));
	public static final Item gold_ingot = Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_ingot"));

	private static boolean isItemPlaceable(Item item) {
		return item == gold_nugget || item == gold_ingot || item == Items.COPPER_NUGGET || item == Items.COPPER_INGOT
				|| item == Items.ALUMINIUM_NUGGET || item == Items.ALUMINIUM_INGOT || item == Items.TIN_NUGGET
				|| item == Items.TIN_INGOT;
	}

	private static boolean isHammerableBlock(Block blockIn) {
		if ((blockIn == cadiboo.wiptech.init.Blocks.COPPER_NUGGET)) {
			return true;
		} else if (blockIn == cadiboo.wiptech.init.Blocks.COPPER_INGOT) {
			return true;
		} else if (blockIn == cadiboo.wiptech.init.Blocks.GOLD_NUGGET) {
			return true;
		} else if (blockIn == cadiboo.wiptech.init.Blocks.GOLD_INGOT) {
			return true;
		}
		return false;
	}

	private static Block itemToPlace(Item item) {

		if ((item == Items.COPPER_NUGGET)) {
			return cadiboo.wiptech.init.Blocks.COPPER_NUGGET;
		} else if ((item == Items.COPPER_INGOT)) {
			return cadiboo.wiptech.init.Blocks.COPPER_INGOT;
		} else if (item == gold_nugget) {
			return cadiboo.wiptech.init.Blocks.GOLD_NUGGET;
		} else if (item == gold_ingot) {
			return cadiboo.wiptech.init.Blocks.GOLD_INGOT;
		}
		return net.minecraft.init.Blocks.AIR;
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static EnumActionResult BlockRightClickEvent(PlayerInteractEvent.RightClickBlock event) {

		// TODO redo this so least-likely is called first
		if (event.getHand() == EnumHand.MAIN_HAND) {
			if (event.getEntityPlayer().isSneaking()) {
				if ((event.getWorld().getBlockState(event.getPos()).getBlock() != null)
						&& ((event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockCrusher))) {
					event.setUseBlock(Result.ALLOW);
					WIPTech.logger.info("onBlockActivated event called for block "
							+ event.getWorld().getBlockState(event.getPos()).getBlock()
							+ " because PlayerInteractEvent.RightClickBlock#setUseBlock was set to "
							+ event.getUseBlock());
					return EnumActionResult.SUCCESS;
				}
			}
		}

		World world = event.getWorld();
		BlockPos pos = event.getPos();
		EntityPlayer player = event.getEntityPlayer();
		if (world.getBlockState(pos).getBlock() instanceof BlockAnvil && event.getFace() == EnumFacing.UP)
			if (world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up())) {
				boolean flag = false;

				if (!isItemPlaceable(event.getItemStack().getItem()))
					flag = isItemPlaceable(
							player.getHeldItem(EnumHand.values()[event.getHand().ordinal() ^ 1]).getItem())
							&& event.getItemStack().isEmpty();
				else // placeable
				{
					if (world.isRemote) {
						EnumHand hand = flag ? EnumHand.values()[event.getHand().ordinal() ^ 1] : event.getHand();
						player.setActiveHand(hand);
						player.swingArm(hand);
					}
					event.getWorld().setBlockState(event.getPos().up(),
							itemToPlace(event.getItemStack().getItem()).getDefaultState());
					if (!player.isCreative())
						event.getItemStack().shrink(1);
				}
				if (flag || isItemPlaceable(event.getItemStack().getItem())) {
					event.setCanceled(true);
					return EnumActionResult.FAIL;
				}
			} else if (isHammerableBlock(world.getBlockState(pos.up()).getBlock())) {
				event.setCanceled(true);
				return EnumActionResult.FAIL;
			}
		return EnumActionResult.PASS;
	}

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public static void onHarvest(BlockEvent.HarvestDropsEvent event) {
		if (event.getHarvester() != null) {
			List<ItemStack> drops = event.getDrops();
			if (drops != null && drops.size() > 0) {
				ItemStack stack = (ItemStack) drops.get(0);
				Item item = stack.getItem();
				World world = event.getWorld();
				BlockPos pos = event.getPos();

				// replace all this with init.Recipes.getHammerRecipe
				Block eventBlock = event.getState().getBlock();

				if ((eventBlock == cadiboo.wiptech.init.Blocks.COPPER_NUGGET)
						|| (eventBlock == cadiboo.wiptech.init.Blocks.COPPER_INGOT)
						|| (eventBlock == cadiboo.wiptech.init.Blocks.GOLD_NUGGET)
						|| (eventBlock == cadiboo.wiptech.init.Blocks.GOLD_INGOT)) {
					if (((world.getBlockState(pos.down()).getBlock() instanceof BlockAnvil))
							&& ((event.getHarvester().getHeldItemMainhand().getItem() instanceof ItemHammer))) {
						drops.set(0, ((ItemStack) Recipes.getHammerResult(stack).get(1)).copy());
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onBreak(BlockEvent.BreakEvent event) {
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (Block block : Blocks.BLOCKS) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
					new ModelResourceLocation(block.getRegistryName(), "inventory"));
		}

		for (Item item : Items.ITEMS) {
			if (!item.getHasSubtypes()) {
				ModelLoader.setCustomModelResourceLocation(item, 0,
						new ModelResourceLocation(item.getRegistryName(), "inventory"));
			} else {

				if (item instanceof ItemFerromagneticProjectile) {
					for (FerromagneticProjectiles rod : EnumHandler.FerromagneticProjectiles.values()) {
						ModelLoader.setCustomModelResourceLocation(item, rod.getID(),
								new ModelResourceLocation("wiptech:rods/" + rod.getName()));
					}
				}

			}
		}
		WIPTech.logger.info("Registered models");

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrusher.class, new TESRCrusher());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurbine.class, new TESRTurbine());
		WIPTech.logger.info("Registered TileEntity Renders");

		/*
		 * for (EntityEntry entity: Entities.ENTITIES) {
		 * 
		 * }
		 */

		RenderingRegistry.registerEntityRenderingHandler(EntityNapalm.class, new RenderEntityNapalmFactory());
		RenderingRegistry.registerEntityRenderingHandler(EntityFerromagneticProjectile.class,
				new RenderEntityFerromagneticProjectileFactory());

		WIPTech.logger.info("Registered Entity Renders");
	}
}