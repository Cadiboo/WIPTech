package cadiboo.wiptech.util.resourcelocation;

import java.util.Locale;

public class ModResourceLocationPath {

	private final String path;

	public ModResourceLocationPath(final String resourceLocationPath) {
		this.path = resourceLocationPath.toLowerCase(Locale.ROOT);
	}

	@Override
	public int hashCode() {
		return this.path.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return this.path.equals(obj);
	}

	@Override
	public String toString() {
		return this.path.toString();
	}

}
