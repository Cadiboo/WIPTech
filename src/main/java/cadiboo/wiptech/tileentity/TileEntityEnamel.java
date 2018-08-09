package cadiboo.wiptech.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;

public class TileEntityEnamel extends TileEntityWire {

	@Override
	public boolean shouldElectrocuteEntity(Entity entity) {
		return false;
	}

	@Override
	public List<Entity> getElectrocutableEntities() {
		return new ArrayList<Entity>(0);
	}

}
