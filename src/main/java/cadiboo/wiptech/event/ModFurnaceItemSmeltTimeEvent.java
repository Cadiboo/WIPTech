package cadiboo.wiptech.event;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * {@link ModFurnaceItemSmeltTimeEvent} is fired when determining the smelt time value for an ItemStack. <br>
 * <br>
 * This event is fired from {@link ModEventFactory#getItemSmeltTime(ItemStack)}.<br>
 * <br>
 * This event is {@link Cancelable} to prevent later handlers from changing the value.<br>
 * <br>
 * This event does not have a result. {@link HasResult}<br>
 * <br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
 **/
@Cancelable
public class ModFurnaceItemSmeltTimeEvent extends Event {

	@Nonnull
	private final ItemStack	itemStack;
	private int				smeltTime;

	public ModFurnaceItemSmeltTimeEvent(@Nonnull final ItemStack itemStack, final int smeltTime) {
		this.itemStack = itemStack;
		this.smeltTime = smeltTime;
	}

	/**
	 * Get the ItemStack in question.
	 */
	@Nonnull
	public ItemStack getItemStack() {
		return this.itemStack;
	}

	/**
	 * Set the smelt time for the given ItemStack.
	 */
	public void setSmeltTime(final int smeltTime) {
		this.smeltTime = smeltTime;
		this.setCanceled(true);
	}

	/**
	 * The resulting value of this event, the smelt time for the ItemStack.
	 */
	public int getSmeltTime() {
		return this.smeltTime;
	}

}
