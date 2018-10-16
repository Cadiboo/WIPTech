package cadiboo.wiptech.util.resourcelocation;

import java.util.Locale;

public class ResourceLocationNamespace {

	private final String namespace;

	public ResourceLocationNamespace(final String resourceLocationNameSpace) {
		this.namespace = org.apache.commons.lang3.StringUtils.isEmpty(resourceLocationNameSpace) ? "minecraft" : resourceLocationNameSpace.toLowerCase(Locale.ROOT);
	}

	@Override
	public int hashCode() {
		return this.namespace.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return this.namespace.equals(obj);
	}

	@Override
	public String toString() {
		return this.namespace.toString();
	}

}
