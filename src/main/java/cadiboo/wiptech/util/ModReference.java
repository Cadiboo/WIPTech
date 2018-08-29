package cadiboo.wiptech.util;

public final class ModReference {

	public static final String CLIENT_PROXY_CLASS = "cadiboo.wiptech.client.Proxy";
	public static final String SERVER_PROXY_CLASS = "cadiboo.wiptech.server.Proxy";
	public static final String GUI_FACTORY_CLASS = "";

	public static final String MOD_ID = "wiptech";
	public static final String NAME = "WIPTech";
	public static final String VERSION = "0.0.0.0";

	public static final String ACCEPTED_VERSIONS = "[1.12.2, 1.13]";
	public static final String DEPENDENCIES = "required-after:minecraft;" + "required-after:forge@[14.23.4.2723,);" + "";

	public static final String ENERGY_UNIT = "PNs";

	public static final boolean CAN_BE_DEACTIVATED = false;
	public static final int ARMOR_MATERIAL_HARDNESS_MULTIPLIER = 5; // TODO this

	/**
	 * 
	 * @author Cadiboo
	 * @see <a href=
	 *      "https://mcforge.readthedocs.io/en/latest/conventions/versioning/">Forge
	 *      Versioning Docs</a>
	 */
	public static final class Version {

		/**
		 * This is our Mod's Name.
		 * 
		 * @return our Mod's Name
		 */
		public static final String getModName() {
			return NAME;
		}

		/**
		 * This is our Mod's Mod Id that is used for stuff like resource locations.
		 * 
		 * @return our Mod's Mod Id that is used for stuff like resource locations
		 */
		public static final String getModId() {
			return MOD_ID;
		}

		/**
		 * This is the Minecraft version we're modding for.<br>
		 * It is changed every time we start modding for a new Minecraft version.<br>
		 * It is never reset.
		 * 
		 * @return the Minecraft version we're modding for
		 */
		public static final String getMinecraftVersion() {
			return "1.12.2";
		}

		/**
		 * This is our Mod's Major Mod Version.<br>
		 * It is changed when modify game mechanics or remove items, blocks, tile
		 * entities, etc.<br>
		 * It is never reset.
		 * 
		 * @return our Mod's Major Mod Version
		 */
		public static final int getMajorModVersion() {
			return 0;
		}

		/**
		 * This is our Mod's Major API Version.<br>
		 * It is changed when modify API mechanics by changing the order or variables of
		 * enums, changing return types of methods or remove public methods
		 * altogether.<br>
		 * It is reset when we update to a new Minecraft version or our Major Mod
		 * Version increments.
		 * 
		 * @return our Mod's Major API Version
		 */
		public static final int getMajorApiVersion() {
			return 0;
		}

		/**
		 * This is our Mod's Minor Mod Version.<br>
		 * It is changed when add new mechanics or add items, blocks, tile entities,
		 * etc. It is also changed when we depreciate public methods in our API (This is
		 * because the change not considered a Major API change since it doesn’t break
		 * the API).<br>
		 * It is reset when we update to a new Minecraft version, our Major Mod Version
		 * increments or our Major API Version increments.
		 * 
		 * @return our Mod's Minor Mod Version
		 */
		public static final int getMinorModVersion() {
			return 0;
		}

		/**
		 * This is our Mod's Patch Version.<br>
		 * It is changed when we patch small problems that do not cause a change to any
		 * greater versions.<br>
		 * It is reset when we update to a new Minecraft version, our Major Mod Version
		 * increments, our Major API Version increments or our Minor Mod Version
		 * increments.
		 * 
		 * @return our Mod's Patch Version
		 */
		public static final int getPatchVersion() {
			return 77;
		}

		/**
		 * This is our Mod's Suffix including our PreRelease Version.<br>
		 * It can be any one of the following values:
		 * <ul>
		 * <a href=
		 * "https://mcforge.readthedocs.io/en/latest/conventions/versioning/#final-release">final</a><br>
		 * <a href=
		 * "https://mcforge.readthedocs.io/en/latest/conventions/versioning/#pre-releases">alpha</a><br>
		 * <a href=
		 * "https://mcforge.readthedocs.io/en/latest/conventions/versioning/#pre-releases">beta</a><br>
		 * <a href=
		 * "https://mcforge.readthedocs.io/en/latest/conventions/versioning/#pre-releases">betaX</a><br>
		 * <a href=
		 * "https://mcforge.readthedocs.io/en/latest/conventions/versioning/#release-candidates">rcX</a><br>
		 * </ul>
		 * 
		 * @return our Mod's Suffix including our PreRelease Version
		 */
		public static final String getSuffix() {
			String suffix = "alpha";
			if (getPreReleaseVersion() > 0) {
				suffix += getPreReleaseVersion();
			}
			return suffix;
		}

		/**
		 * This is our Mod's PreRelease Version.<br>
		 * It is changed when we add new features that are not quite done yet.<br>
		 * It is reset when we update to a new Minecraft version, our Major Mod Version
		 * increments, our Major API Version increments, our Minor Mod Version
		 * increments or our Patch Version increments.
		 * 
		 * @return our Mod's PreRelease Version
		 */
		public static final int getPreReleaseVersion() {
			return 0;
		}

		/**
		 * This is our Mod's Version.<br>
		 * It is our Mod's Name, our Mod's Major Mod version, our Mod's Major API
		 * version, our Mod's Minor Mod version and our Mod's Patch version in the
		 * format <code>MAJORMOD.MAJORAPI.MINOR.PATCH</code>
		 * 
		 * @return our Mod's full Version
		 */
		public static final String getVersion() {
			String version = String.format("%d.%d.%d.%d", getMajorModVersion(), getMajorApiVersion(), getMinorModVersion(), getPatchVersion());
			return version;
		}

		/**
		 * This is our Mod's full Version.<br>
		 * It is our Mod's Name, our Mod's Minecraft Version, our Mod's Major Mod
		 * version, our Mod's Major API version, our Mod's Minor Mod version, our Mod's
		 * Patch version and our Mod's Suffix in the format
		 * <code>MODNAME MCVERSION-MAJORMOD.MAJORAPI.MINOR.PATCH[-SUFFIX]</code>
		 * 
		 * @return our Mod's full Version
		 */
		public static final String getFullVersion() {
			String version = String.format("%s %s-%d.%d.%d.%d", getMinecraftVersion(), getMajorModVersion(), getMajorApiVersion(), getMinorModVersion(), getPatchVersion());
			if (!getSuffix().equals("")) {
				version += "-" + getSuffix();
			}
			return version;
		}

	}

}
