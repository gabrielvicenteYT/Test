package wdl;

import net.minecraft.entity.Entity;
import wdl.api.IEntityEditor;
import wdl.api.IWDLMod;
import wdl.api.IWDLModDescripted;

/**
 * Realigns entities to their serverside positions, to mitigate entity drift.
 * This is necessary for entities that move clientside, most importantly boats
 * (example: http://i.imgur.com/3QQchZL.gifv).
 * <br/>
 * This is also an example of how an {@link IWDLMod} would be implemented.
 */
public class EntityRealigner implements IEntityEditor, IWDLModDescripted {
	@Override
	public boolean isValidEnvironment(String version) {
		return true;
	}

	@Override
	public String getEnvironmentErrorMessage(String version) {
		return null;
	}

	@Override
	public String getDisplayName() {
		return "Entity realigner";
	}

	@Override
	public String getMainAuthor() {
		return "Pokechu22";
	}

	@Override
	public String[] getAuthors() {
		return null;
	}

	@Override
	public String getURL() {
		return null;
	}

	@Override
	public String getDescription() {
		return "Realigns entities to their serverside position to deal with " +
				"entities that drift clientside (for example, boats).";
	}

	@Override
	public boolean shouldEdit(Entity e) {
		// We make sure that at least one of serverPosX, y, and
		// z is not 0 because an entity with a server pos of 0,
		// 0, 0 probably has a different way of setting up its
		// position (for example, paintings).
		// No sane entity will be at 0, 0, 0.  And moving them
		// to it can effectively delete entities - see
		// https://github.com/uyjulian/LiteModWDL/issues/4.
		// (I also think this is the cause for the "world going
		// invisible" issue).
		return e.serverPosX != 0 || e.serverPosY != 0 || e.serverPosZ != 0;
	}

	@Override
	public void editEntity(Entity e) {
		System.out.println("Realigning " + e);
		e.posX = convertServerPos(e.serverPosX);
		e.posY = convertServerPos(e.serverPosY);
		e.posZ = convertServerPos(e.serverPosZ);
		System.out.println("Realigned " + e);
	}
	
	/**
	 * Converts a position from the fixed-point version that a packet
	 * (or {@link Entity#serverPosX} and the like use) into a double.
	 *
	 * @see
	 *      <a href="http://wiki.vg/Protocol#Fixed-point_numbers">
	 *      wiki.vg on Fixed-point numbers</a>
	 *
	 * @param serverPos
	 * @return The double version of the position.
	 */
	private static double convertServerPos(int serverPos) {
		return serverPos / 32.0;
	}
}
