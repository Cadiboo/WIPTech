package cadiboo.wiptech.util;

import net.minecraft.util.ResourceLocation;

public class ModResourceLocation extends ResourceLocation {

	public ModResourceLocation(String resourceName) {
		super(resourceName);
	}

	public ModResourceLocation(String resourceDomainIn, String resourcePathIn) {
		super(resourceDomainIn, resourcePathIn);
	}

	@Override
	public String toString() {
		if (resourceDomain.equals(""))
			return resourcePath;
		return super.toString();
	}

}
