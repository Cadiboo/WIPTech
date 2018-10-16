package cadiboo.wiptech.util.resourcelocation;

import net.minecraft.util.ResourceLocation;

public class ModResourceLocation extends ResourceLocation {

	public ModResourceLocation(final ResourceLocationNamespace resourceNamespace, final ModResourceLocationPath resourcePath) {
		super(resourceNamespace.toString(), resourcePath.toString());
	}

	public ModResourceLocation(final String resourceNamespace, final String resourcePath) {
		this(new ResourceLocationNamespace(resourceNamespace), new ModResourceLocationPath(resourcePath));
	}

	public ModResourceLocation(final ResourceLocation resourceLocation) {
		this(new ResourceLocationNamespace(resourceLocation.getNamespace()).toString(), new ModResourceLocationPath(resourceLocation.getPath()).toString());
	}

	protected ModResourceLocation(final String... resourceName) {
		this(new ResourceLocationNamespace(resourceName[0]).toString(), new ModResourceLocationPath(resourceName[1]).toString());
	}

	public ModResourceLocation(final String resourceLocation) {
		this(splitObjectName(resourceLocation));
	}

	@Override
	public String toString() {
		if (this.namespace.equals("")) {
			return this.path;
		}
		return super.toString();
	}

}
