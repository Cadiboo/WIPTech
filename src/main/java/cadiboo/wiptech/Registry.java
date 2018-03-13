package cadiboo.wiptech;

import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Registry {
	
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event)
		{	
			event.getRegistry().registerAll(Blocks.BLOCKS);
			WIPTech.logger.info("Registered blocks");
			//GameRegistry.registerTileEntity(TileEntityCrusher.class, Reference.ID+"TileEntityCrusher");
			//GameRegistry.registerTileEntity(TileEntityCoiler.class, Reference.ID+"TileEntityCoiler");
			WIPTech.logger.info("Registered TileEntities");
		}

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event)
		{	
			event.getRegistry().registerAll(Items.ITEMS);

			for (Block block : Blocks.BLOCKS)
			{
				event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
			}

			WIPTech.logger.info("Registered items");
			//Register OreDict
			//Utils.getLogger().info("Registered OreDictionary");
			//GameRegistry.addSmelting(ModBlocks.COPPER_ORE, new ItemStack(ModItems.COPPER_INGOT, 3), 0F);
			//GameRegistry.addSmelting(ModBlocks.ALUMINUM_ORE, new ItemStack(ModItems.ALUMINUM_INGOT, 3), 0F);
			//GameRegistry.addSmelting(ModBlocks.OSMIUM_ORE, new ItemStack(ModItems.OSMIUM_INGOT, 3), 0F);
			//GameRegistry.addSmelting(ModBlocks.TUNGSTEN_ORE, new ItemStack(ModItems.TUNGSTEN_INGOT, 3), 0F);
			
		}

		@SubscribeEvent
		public static void BlockRightClickEvent(PlayerInteractEvent.RightClickBlock event)
		{
			if(event.getHand() == EnumHand.MAIN_HAND)
			{
				if(event.getEntityPlayer().isSneaking())
				{
					/*if(event.getWorld().getBlockState(event.getPos()).getBlock() != null && event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockCrusher)
					{
						event.setUseBlock(net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW);
						WIPTech.logger.info("onBlockActivated event called for block "+event.getWorld().getBlockState(event.getPos()).getBlock()+" because PlayerInteractEvent.RightClickBlock#setUseBlock was set to "+event.getUseBlock());
					}
					*/
				}
			}
		}

		public void logLogicalSide() {
			WIPTech.logger.info("Sever");
		}
	}