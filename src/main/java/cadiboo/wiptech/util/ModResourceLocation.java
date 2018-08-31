package cadiboo.wiptech.util;

import net.minecraft.util.ResourceLocation;

public class ModResourceLocation extends ResourceLocation {

	public ModResourceLocation(final String resourceName) {
		super(resourceName);
	}

	public ModResourceLocation(final String resourceDomain, final String resourcePath) {
		super(resourceDomain, resourcePath);
	}

	public ModResourceLocation(final ResourceLocation resourceLocation) {
		super(resourceLocation.getResourceDomain(), resourceLocation.getResourcePath());
	}

	@Override
	public String toString() {
		if (this.resourceDomain.equals("")) {
			return this.resourcePath;
		}
		return super.toString();
	}

}
