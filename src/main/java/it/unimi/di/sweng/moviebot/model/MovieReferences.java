package it.unimi.di.sweng.moviebot.model;

import java.util.HashMap;
import java.util.Map;

public class MovieReferences {
	public static class MovieReference {
		@Override
		public int hashCode() {
			return this.ID;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof MovieReference) {
				MovieReference that = (MovieReference) obj;
				return this.ID == that.ID;
			}
			return false;
		}

		public final int ID;
		public final String TITLE;

		private MovieReference(int ID, String TITLE) {
			this.ID = ID;
			this.TITLE = TITLE;
		}
	}

	private static Map<Integer, MovieReference> movieReferences = new HashMap<Integer, MovieReference>();

	private MovieReferences() {
	}

	public static MovieReference getMovieReference(int id, String title) {
		if (!movieReferences.containsKey(id))
			movieReferences.put(id, new MovieReference(id, title));

		return movieReferences.get(id);
	}

	public static void clearAllReferences() {
		movieReferences.clear();
	}
}