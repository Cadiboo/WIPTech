package cadiboo.wiptech.client;

//import cadiboo.wiptech.ItemMagneticMetalRod;
//import cadiboo.wiptech.MagneticMetalRods;
import cadiboo.wiptech.Reference;
import cadiboo.wiptech.Registry;
import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Items;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ClientRegistry extends Registry {

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
				/*if(item instanceof ItemMagneticMetalRod) {
					for(MagneticMetalRods rod : EnumHandler.MagneticMetalRods.values()) { 
						//find a way to move models from block to item
						//ModelLoader.setCustomModelResourceLocation(item, rod.getID(), new ModelResourceLocation(rod.getName() + "=" + MagneticMetalRods.values()[rod.getID()]));
						ModelLoader.setCustomModelResourceLocation(item, rod.getID(), new ModelResourceLocation(Reference.ID+":rods/" + rod.getName()));
						//ModelLoader.setCustomModelResourceLocation(item, rod.getID(), new ModelResourceLocation(Reference.ID+":magnetic_metal_rod", rod.getName()));
					}
				}*/
			}
		}

		WIPTech.logger.info("Registered models");
	}
	
	
	@Override
	public void logLogicalSide() {
		WIPTech.logger.info("Client");
	}
	
}
