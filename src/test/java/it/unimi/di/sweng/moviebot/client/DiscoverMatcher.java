package it.unimi.di.sweng.moviebot.client;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.omertron.themoviedbapi.model.discover.Discover;

public class DiscoverMatcher extends BaseMatcher<Discover> {

	private Discover discover;

	public DiscoverMatcher(Discover discover) {
		this.discover = discover;
	}

	@Override
	public boolean matches(Object item) {
		if (item != null && item instanceof Discover) {
			Discover that = (Discover) item;
			return this.discover.getParams().getEntries().equals(that.getParams().getEntries());
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("Compares two Discover instances");
	}

}
