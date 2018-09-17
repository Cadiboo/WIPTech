package cadiboo.wiptech.item;

import java.util.HashSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.capability.attachments.AttachmentList;
import cadiboo.wiptech.capability.attachments.CapabilityAttachmentList;
import cadiboo.wiptech.capability.attachments.burst.CapabilityCircuitBurstShots;
import cadiboo.wiptech.capability.attachments.burst.CircuitBurstShots;
import cadiboo.wiptech.capability.energy.IEnergyUser;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import cadiboo.wiptech.capability.inventory.IInventoryUser;
import cadiboo.wiptech.capability.inventory.ModItemStackHandler;
import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
import cadiboo.wiptech.util.ModEnums.CircuitTypes;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import scala.actors.threadpool.Arrays;

public abstract class ItemHandheldGun extends Item implements IModItem {

	private static final AttachmentPoints[] REQUIRED_ATTACHMENT_POINTS = new AttachmentPoints[] { AttachmentPoints.CIRCUIT };

	private final AttachmentPoints[] attachmentPoints;

	public ItemHandheldGun(final String name, final AttachmentPoints... attachmentPoints) {
		ModUtil.setRegistryNames(this, name);

		final HashSet<AttachmentPoints> points = new HashSet<>();
		points.addAll(Arrays.asList(attachmentPoints));
		points.addAll(Arrays.asList(REQUIRED_ATTACHMENT_POINTS));

		this.attachmentPoints = points.toArray(new AttachmentPoints[0]);
		this.setMaxDamage(100);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumAction getItemUseAction(final ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack stack) {
		return 72000;
	}

	@Override
	public boolean showDurabilityBar(final ItemStack stack) {
		return true;
	}

	public abstract void shoot(final World world, final EntityPlayer player, AttachmentList attachmentList);

	/* when player starts right clicking */
	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {

		final ItemStack itemstack = player.getHeldItem(hand);

		final AttachmentList attachmentList = itemstack.getCapability(CapabilityAttachmentList.ATTACHMENT_LIST, null);
		if (attachmentList == null) {
			return new ActionResult<>(EnumActionResult.FAIL, itemstack);
		}
		final ItemStack circuitStack = attachmentList.getAttachment(AttachmentPoints.CIRCUIT);
		final Item circuitItem = circuitStack.getItem();

		if (circuitStack.isEmpty() || (circuitItem == null)) {
			return new ActionResult<>(EnumActionResult.FAIL, itemstack);
		}

		if (circuitItem instanceof ItemCircuit) {
			final CircuitTypes circuit = ((ItemCircuit) circuitItem).getType();
			switch (circuit) {
			case AUTO:
			case BURST3:
			case BURST5:
			case MANUAL:
				player.setActiveHand(hand);
				return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
			default:
				return new ActionResult<>(EnumActionResult.FAIL, itemstack);
			}
		}

		return new ActionResult<>(EnumActionResult.PASS, itemstack);

	}

	/* when player stops right clicking */
	@Override
	public void onPlayerStoppedUsing(final ItemStack stack, final World world, final EntityLivingBase entityLiving, final int timeLeft) {

		if (!(entityLiving instanceof EntityPlayer)) {
			return;
		}

		final EntityPlayer player = (EntityPlayer) entityLiving;
		final EnumHand hand = player.getActiveHand();

		final ItemStack itemstack = player.getHeldItem(hand);

		final AttachmentList attachmentList = itemstack.getCapability(CapabilityAttachmentList.ATTACHMENT_LIST, null);
		if (attachmentList == null) {
			return;
		}
		final ItemStack circuitStack = attachmentList.getAttachment(AttachmentPoints.CIRCUIT);
		final Item circuitItem = circuitStack.getItem();

		if (circuitStack.isEmpty() || (circuitItem == null)) {
			return;
		}

		if (circuitItem instanceof ItemCircuit) {
			final CircuitTypes circuit = ((ItemCircuit) circuitItem).getType();
			switch (circuit) {
			case AUTO:
				this.shoot(world, player, attachmentList);
				break;
			case BURST3:
			case BURST5:
				final CircuitBurstShots shots = circuitStack.getCapability(CapabilityCircuitBurstShots.CIRCUIT_BURST_SHOTS, null);
				if (shots != null) {
					if (shots.canShoot()) {
						shots.incrementShotsTaken();
						this.shoot(world, player, attachmentList);
					}
					shots.resetShotsTaken();
				}
				break;
			case MANUAL:
				this.shoot(world, player, attachmentList);
				break;
			default:
				return;
			}
		}

		return;

	}

	/* called every tick right click is held down */
	@Override
	public void onUsingTick(final ItemStack stack, final EntityLivingBase entityLiving, final int count) {

		if (!(entityLiving instanceof EntityPlayer)) {
			return;
		}

		final EntityPlayer player = (EntityPlayer) entityLiving;
		final World world = player.world;
		final EnumHand hand = player.getActiveHand();

		final ItemStack itemstack = player.getHeldItem(hand);

		final AttachmentList attachmentList = itemstack.getCapability(CapabilityAttachmentList.ATTACHMENT_LIST, null);
		if (attachmentList == null) {
			return;
		}
		final ItemStack circuitStack = attachmentList.getAttachment(AttachmentPoints.CIRCUIT);
		final Item circuitItem = circuitStack.getItem();

		if (circuitStack.isEmpty() || (circuitItem == null)) {
			return;
		}

		if (circuitItem instanceof ItemCircuit) {
			final CircuitTypes circuit = ((ItemCircuit) circuitItem).getType();
			switch (circuit) {
			case AUTO:
				this.shoot(world, player, attachmentList);
				break;
			case BURST3:
			case BURST5:
				final CircuitBurstShots shots = circuitStack.getCapability(CapabilityCircuitBurstShots.CIRCUIT_BURST_SHOTS, null);
				if ((shots != null) && shots.canShoot()) {
					shots.incrementShotsTaken();
					this.shoot(world, player, attachmentList);
				}
				break;
			default:
			case MANUAL:
				return;
			}
		}

		return;

	}

	/* when the item is FINISHED being used (I.e. when getMaxItemUseDuration is exceded) */
	@Override
	public ItemStack onItemUseFinish(final ItemStack stack, final World worldIn, final EntityLivingBase entityLiving) {
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	/* block right clicked */
	@Override
	public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	/* block right clicked, before block is notifed */
	@Override
	public EnumActionResult onItemUseFirst(final EntityPlayer player, final World world, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ, final EnumHand hand) {
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	@Override
	public final ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final NBTTagCompound nbt) {
		return new ICapabilitySerializable<NBTTagCompound>() {

			final ItemStack				itemStack		= stack;
			final ModEnergyStorage		energy			= new ModEnergyStorage(1000);
			final ModItemStackHandler	inventory		= new ModItemStackHandler();
			final AttachmentList		attachments		= new AttachmentList(ItemHandheldGun.this.attachmentPoints);
			final String				ATTACHMENTS_TAG	= "attachments";

			@Override
			public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing facing) {
				return this.getCapability(capability, facing) != null;
			}

			@Nullable
			@Override
			public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
				if (capability == CapabilityEnergy.ENERGY) {
					return (T) this.getEnergy();
				}

				if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
					return (T) this.getInventory();
				}

				if (capability == CapabilityAttachmentList.ATTACHMENT_LIST) {
					return (T) this.getAttachmentList();
				}

				return null;
			}

			@Override
			public NBTTagCompound serializeNBT() {
				final NBTTagCompound compound = new NBTTagCompound();
				compound.setTag(IEnergyUser.ENERGY_TAG, this.getEnergy().serializeNBT());
				compound.setTag(IInventoryUser.INVENTORY_TAG, this.getInventory().serializeNBT());
				compound.setTag(this.ATTACHMENTS_TAG, this.getAttachmentList().serializeNBT());
				return compound;
			}

			@Override
			public void deserializeNBT(final NBTTagCompound compound) {
				if (compound.hasKey(IEnergyUser.ENERGY_TAG)) {
					this.getEnergy().deserializeNBT(compound.getCompoundTag(IEnergyUser.ENERGY_TAG));
				}
				if (compound.hasKey(IInventoryUser.INVENTORY_TAG)) {
					this.getInventory().deserializeNBT(compound.getCompoundTag(IInventoryUser.INVENTORY_TAG));
				}
				if (compound.hasKey(this.ATTACHMENTS_TAG)) {
					this.getAttachmentList().deserializeNBT(compound.getCompoundTag(this.ATTACHMENTS_TAG));
				}

			}

			public AttachmentList getAttachmentList() {
				return this.attachments;
			}

			public ModEnergyStorage getEnergy() {
				return this.energy;
			}

			public ModItemStackHandler getInventory() {
				return this.inventory;
			}

		};
	}

	@Override
	public void readNBTShareTag(final ItemStack stack, final NBTTagCompound compound) {
		super.readNBTShareTag(stack, compound);
		if ((compound == null) || !compound.hasKey("serialized")) {
			return;
		}

		stack.deserializeNBT(compound.getCompoundTag("serialized"));
	}

	@Override
	public NBTTagCompound getNBTShareTag(final ItemStack stack) {
		final NBTTagCompound compound = new NBTTagCompound();
		final NBTTagCompound superCompound = super.getNBTShareTag(stack);
		if (superCompound != null) {
			compound.merge(superCompound);
		}
		compound.setTag("serialized", stack.serializeNBT());
		return compound;
	}

	@Override
	public boolean getShareTag() {
		return super.getShareTag() && true;
	}

}
