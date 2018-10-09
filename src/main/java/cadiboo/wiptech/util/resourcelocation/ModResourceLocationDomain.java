package cadiboo.wiptech.util.resourcelocation;

import java.util.Locale;

public class ModResourceLocationDomain {

	private final String domain;

	public ModResourceLocationDomain(final String resourceLocationDomain) {
		this.domain = org.apache.commons.lang3.StringUtils.isEmpty(resourceLocationDomain) ? "minecraft" : resourceLocationDomain.toLowerCase(Locale.ROOT);
	}

	@Override
	public int hashCode() {
		return this.domain.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return this.domain.equals(obj);
	}

	@Override
	public String toString() {
		return this.domain.toString();
	}

}
