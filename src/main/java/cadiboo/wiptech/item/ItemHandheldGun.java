package cadiboo.wiptech.item;

import java.util.HashSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.capability.attachments.AttachmentList;
import cadiboo.wiptech.capability.attachments.CapabilityAttachmentList;
import cadiboo.wiptech.capability.attachments.IAttachmentUser;
import cadiboo.wiptech.capability.attachments.circuitdata.CapabilityCircuitData;
import cadiboo.wiptech.capability.attachments.circuitdata.CircuitData;
import cadiboo.wiptech.capability.energy.IEnergyUser;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import cadiboo.wiptech.capability.inventory.IInventoryUser;
import cadiboo.wiptech.capability.inventory.ModItemStackHandler;
import cadiboo.wiptech.util.ModEnums.AttachmentPoint;
import cadiboo.wiptech.util.ModEnums.UsePhase;
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

	private static final AttachmentPoint[] REQUIRED_ATTACHMENT_POINTS = new AttachmentPoint[] { AttachmentPoint.CIRCUIT };

	private final AttachmentPoint[] attachmentPoints;

	public ItemHandheldGun(final String name, final AttachmentPoint... attachmentPoints) {
		ModUtil.setRegistryNames(this, name);

		final HashSet<AttachmentPoint> points = new HashSet<>();
		points.addAll(Arrays.asList(attachmentPoints));
		points.addAll(Arrays.asList(REQUIRED_ATTACHMENT_POINTS));

		this.attachmentPoints = points.toArray(new AttachmentPoint[0]);
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
		final ItemStack circuitStack = attachmentList.getAttachment(AttachmentPoint.CIRCUIT);
		if (circuitStack.isEmpty()) {
			return new ActionResult<>(EnumActionResult.FAIL, itemstack);
		}
		final CircuitData circuitData = circuitStack.getCapability(CapabilityCircuitData.CIRCUIT_DATA, null);

		if (circuitData == null) {
			return new ActionResult<>(EnumActionResult.FAIL, itemstack);
		}

		if (circuitData.canShoot(UsePhase.START)) {
			circuitData.incrementShotsTaken();
			this.shoot(world, player, attachmentList);
			player.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
		}

		for (final UsePhase phase : UsePhase.values()) {
			if (circuitData.canShootOnPhase(phase)) {
				player.setActiveHand(hand);
				return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
			}
		}

		return new ActionResult<>(EnumActionResult.FAIL, itemstack);

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

		final ItemStack circuitStack = attachmentList.getAttachment(AttachmentPoint.CIRCUIT);
		if (circuitStack.isEmpty()) {
			return;
		}

		final CircuitData circuitData = circuitStack.getCapability(CapabilityCircuitData.CIRCUIT_DATA, null);

		if (circuitData == null) {
			return;
		}

		if (circuitData.canShoot(UsePhase.TICK)) {
			circuitData.incrementShotsTaken();
			this.shoot(world, player, attachmentList);
		}

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
		final ItemStack circuitStack = attachmentList.getAttachment(AttachmentPoint.CIRCUIT);
		if (circuitStack.isEmpty()) {
			return;
		}
		final CircuitData circuitData = circuitStack.getCapability(CapabilityCircuitData.CIRCUIT_DATA, null);

		if (circuitData == null) {
			return;
		}

		if (circuitData.canShoot(UsePhase.END)) {
			circuitData.incrementShotsTaken();
			this.shoot(world, player, attachmentList);
		}

		player.resetActiveHand();

		circuitData.resetShotsTaken();
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
		return new CapabilityProvider(stack, nbt);
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

	public class CapabilityProvider implements ICapabilitySerializable<NBTTagCompound>, IEnergyUser, IInventoryUser, IAttachmentUser {

		final ItemStack				itemStack;
		final ModEnergyStorage		energy		= new ModEnergyStorage(1000);
		final ModItemStackHandler	inventory	= new ModItemStackHandler();
		final AttachmentList		attachments	= new AttachmentList(ItemHandheldGun.this.attachmentPoints);

		public CapabilityProvider(final ItemStack stack, final NBTTagCompound nbt) {
			this.itemStack = stack;
		}

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
				return (T) this.getAttachments();
			}

			return null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			final NBTTagCompound compound = new NBTTagCompound();
			compound.merge(IEnergyUser.super.serializeNBT());
			compound.merge(IInventoryUser.super.serializeNBT());
			compound.merge(IAttachmentUser.super.serializeNBT());
			return compound;
		}

		@Override
		public void deserializeNBT(final NBTTagCompound compound) {
			IEnergyUser.super.deserializeNBT(compound);
			IInventoryUser.super.deserializeNBT(compound);
			IAttachmentUser.super.deserializeNBT(compound);
		}

		@Override
		public AttachmentList getAttachments() {
			return this.attachments;
		}

		@Override
		public ModEnergyStorage getEnergy() {
			return this.energy;
		}

		@Override
		public ModItemStackHandler getInventory() {
			return this.inventory;
		}

	}

}
