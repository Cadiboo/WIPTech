package cadiboo.wiptech.util;

import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.tileentity.TileEntityBase;
import net.minecraftforge.energy.EnergyStorage;

//Coppied from https://www.programcreek.com/java-api-examples/?code=canitzp/Metalworks/Metalworks-master/src/main/java/de/canitzp/metalworks/

/**
 * @author canitzp
 */

public class CustomEnergyStorage extends EnergyStorage {

    @Nullable
    private TileEntityBase tileToUpdate = null;

    public CustomEnergyStorage(int capacity) {
        super(capacity);
    }

    public CustomEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public int forceReceive(int maxReceive, boolean simulate){
        int energyReceived = Math.min(capacity - this.getEnergyStored(), Math.min(this.maxReceive, maxReceive));
        if (!simulate){
            this.setEnergy(this.getEnergyStored() + energyReceived);
        }
        this.notifyTile();
        return energyReceived;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if(canReceive()){
            int energyReceived = Math.min(capacity - this.getEnergyStored(), Math.min(this.maxReceive, maxReceive));
            if (!simulate){
                this.setEnergy(this.getEnergyStored() + energyReceived);
            }
            this.notifyTile();
            return energyReceived;
        }
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if(canExtract()){
            int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
            if (!simulate){
                this.setEnergy(this.getEnergyStored() - energyExtracted);
            }
            notifyTile();
            return energyExtracted;
        }
        return 0;
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }

    public int getExtractTransfer(){
        return this.maxExtract;
    }

    public int getReceiveTransfer(){
        return this.maxReceive;
    }

    public CustomEnergyStorage setTile(@Nullable TileEntityBase tile){
        this.tileToUpdate = tile;
        return this;
    }

    protected void notifyTile(){
        if(this.tileToUpdate != null){
            this.tileToUpdate.syncToClients();
            this.tileToUpdate.markDirty();
        }
    }

    public CustomEnergyStorage copy(){
        return new CustomEnergyStorage(this.capacity, this.maxReceive, this.maxExtract, this.energy).setTile(this.tileToUpdate);
    }

    @Nullable
    public TileEntityBase getTileToUpdate() {
        return tileToUpdate;
    }
}