package cadiboo.wiptech.event;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class ModEventFactory {

	public static int getItemSmeltTime(@Nonnull final ItemStack itemStack) {
		final ModFurnaceItemSmeltTimeEvent event = new ModFurnaceItemSmeltTimeEvent(itemStack, 200);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.getSmeltTime() < 0) {
			return 200;
		}
		return event.getSmeltTime();
	}

}
