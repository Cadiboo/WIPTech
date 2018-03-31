package cadiboo.wiptech.handler.event;

import java.util.List;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockCrusher;
import cadiboo.wiptech.client.render.entity.RenderEntityFerromagneticProjectileFactory;
import cadiboo.wiptech.client.render.entity.RenderEntityNapalmFactory;
import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile;
import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.handler.EnumHandler;
import cadiboo.wiptech.handler.EnumHandler.FerromagneticProjectiles;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Entities;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.init.Recipes;
import cadiboo.wiptech.item.ItemCopperIngot;
import cadiboo.wiptech.item.ItemCopperNugget;
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
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

@Mod.EventBusSubscriber
public class EventSubscriber {

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event)
	{
		event.getRegistry().registerAll(Entities.ENTITIES);
		WIPTech.logger.info("Registered Entities");
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(cadiboo.wiptech.init.Blocks.BLOCKS);
		WIPTech.logger.info("Registered Blocks");

		GameRegistry.registerTileEntity(TileEntityCrusher.class, Reference.ID+"TileEntityCrusher");
		GameRegistry.registerTileEntity(TileEntityCoiler.class, Reference.ID+"TileEntityCoiler");
		GameRegistry.registerTileEntity(TileEntityTurbine.class, Reference.ID+"TileEntityTurbine");
		GameRegistry.registerTileEntity(TileEntityCapacitorBank.class, Reference.ID+"TileEntityCapacitorBank");

		WIPTech.logger.info("Registered TileEntities");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		
		event.getRegistry().registerAll(cadiboo.wiptech.init.Items.ITEMS);
		
		WIPTech.logger.info("Registered Items");
		
		for(Block block : cadiboo.wiptech.init.Blocks.BLOCKS) {
			event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}
		
		WIPTech.logger.info("And ItemBlocks");

		
		for(Block block : cadiboo.wiptech.init.Blocks.getOres()) {
			WIPTech.logger.info(block.getUnlocalizedName());
			String name = block.getUnlocalizedName().replace("_ore", "").replace("tile.", "");
			WIPTech.logger.info(name);
			if(name.length()>0) {
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				OreDictionary.registerOre("ore"+name, block);
			}
		}

		for(Item item : cadiboo.wiptech.init.Items.getIngots()) {
			String name = item.getUnlocalizedName().replace("_ingot", "").replace("item.", "");
			if(name.length()>0) {
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				OreDictionary.registerOre("ingot"+name, item);
			}
		}

		for(Item item : cadiboo.wiptech.init.Items.getNuggets()) {
			String name = item.getUnlocalizedName().replace("_nugget", "").replace("item.", "");
			if(name.length()>0) {
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				OreDictionary.registerOre("nugget"+name, item);
			}
		}

		WIPTech.logger.info("Registered OreDictionary");
		
		
	}

	@SubscribeEvent
	public static EnumActionResult BlockRightClickEvent(PlayerInteractEvent.RightClickBlock event)
	{
		Item item = event.getItemStack().getItem();
		ItemStack stack = event.getItemStack();
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		EnumFacing side = event.getFace();
		Item gold_nugget = Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_nugget"));
		Item gold_ingot = Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_ingot"));
		if (((item instanceof ItemCopperNugget)) || 
				((item instanceof ItemCopperIngot)) || 
				(item == gold_nugget) || 
				(item == gold_ingot)) {
			if (((world.getBlockState(pos).getBlock() instanceof BlockAnvil)) && 
					(side == EnumFacing.UP) && 
					(world.getBlockState(pos.up()) == net.minecraft.init.Blocks.AIR.getDefaultState()))
			{
				if ((item instanceof ItemCopperNugget)) {
					world.setBlockState(pos.up(), cadiboo.wiptech.init.Blocks.COPPER_NUGGET.getDefaultState());
				} else if ((item instanceof ItemCopperIngot)) {
					world.setBlockState(pos.up(), cadiboo.wiptech.init.Blocks.COPPER_INGOT.getDefaultState());
				} else if (item == gold_nugget) {
					world.setBlockState(pos.up(), cadiboo.wiptech.init.Blocks.GOLD_NUGGET.getDefaultState());
				} else if (item == gold_ingot) {
					world.setBlockState(pos.up(), cadiboo.wiptech.init.Blocks.GOLD_INGOT.getDefaultState());
				}
				if (!event.getEntityPlayer().isCreative()) {
					stack.shrink(1);
				}
				event.setCanceled(true);
				return EnumActionResult.SUCCESS;
			}
		}
		if (event.getHand() == EnumHand.MAIN_HAND) {
			if (event.getEntityPlayer().isSneaking()) {
				if ((event.getWorld().getBlockState(event.getPos()).getBlock() != null) && ((event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockCrusher)))
				{
					event.setUseBlock(Result.ALLOW);
					WIPTech.logger.info("onBlockActivated event called for block " + event.getWorld().getBlockState(event.getPos()).getBlock() + " because PlayerInteractEvent.RightClickBlock#setUseBlock was set to " + event.getUseBlock());
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.PASS;
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public static void onHarvest(BlockEvent.HarvestDropsEvent event)
	{
		if (event.getHarvester() != null)
		{
			List<ItemStack> drops = event.getDrops();
			if(drops!=null && drops.size()>0) {
				ItemStack stack = (ItemStack)drops.get(0);
				Item item = stack.getItem();
				World world = event.getWorld();
				BlockPos pos = event.getPos();

				//replace all this with init.Recipes.getHammerRecipe
				Block eventBlock = event.getState().getBlock();

				if ((eventBlock == cadiboo.wiptech.init.Blocks.COPPER_NUGGET) || 
						(eventBlock == cadiboo.wiptech.init.Blocks.COPPER_INGOT) || 
						(eventBlock == cadiboo.wiptech.init.Blocks.GOLD_NUGGET) || 
						(eventBlock == cadiboo.wiptech.init.Blocks.GOLD_INGOT)) {
					if (((world.getBlockState(pos.down()).getBlock() instanceof BlockAnvil)) && 
							((event.getHarvester().getHeldItemMainhand().getItem() instanceof ItemHammer)))
					{
						drops.set(0, ((ItemStack) Recipes.getHammerResult(stack).get(1)).copy());
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onBreak(BlockEvent.BreakEvent event) {}


	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		for (Block block: Blocks.BLOCKS)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
		}


		for (Item item: Items.ITEMS)
		{
			if(!item.getHasSubtypes()) {
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
			} else {

				if(item instanceof ItemFerromagneticProjectile) {
					for(FerromagneticProjectiles rod : EnumHandler.FerromagneticProjectiles.values()) { 
						ModelLoader.setCustomModelResourceLocation(item, rod.getID(), new ModelResourceLocation("wiptech:rods/" + rod.getName()));
					}
				}

			}
		}
		WIPTech.logger.info("Registered models");

		/*for (EntityEntry entity: Entities.ENTITIES)
		{

		}*/

		RenderingRegistry.registerEntityRenderingHandler(EntityNapalm.class, new RenderEntityNapalmFactory());
		RenderingRegistry.registerEntityRenderingHandler(EntityFerromagneticProjectile.class, new RenderEntityFerromagneticProjectileFactory());

		WIPTech.logger.info("Registered Entity Renders");
	}
}