package cadiboo.wiptech.util;

import net.minecraft.util.ResourceLocation;

public class ModResourceLocation extends ResourceLocation {

	public ModResourceLocation(final String resourceName) {
		super(resourceName.toLowerCase());
	}

	public ModResourceLocation(final String resourceDomain, final String resourcePath) {
		super(resourceDomain.toLowerCase(), resourcePath.toLowerCase());
	}

	public ModResourceLocation(final ResourceLocation resourceLocation) {
		super(resourceLocation.getResourceDomain().toLowerCase(), resourceLocation.getResourcePath().toLowerCase());
	}

	@Override
	public String toString() {
		if (this.resourceDomain.equals("")) {
			return this.resourcePath;
		}
		return super.toString();
	}

}
