package it.unimi.di.sweng.moviebot.formatter;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.omertron.themoviedbapi.model.media.Video;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import it.unimi.di.sweng.moviebot.model.MovieReferences;
import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class BasicFormatterTest {

	private MovieFormatter formatter;

	@Before
	public void setUp() {
		formatter = new BasicFormatter();
		UsersInfo.INSTANCE.clearInfo();
		MovieReferences.clearAllReferences();
	}

	@Test
	public void genreReplyTest() {
		List<MovieBasic> movies = new ArrayList<MovieBasic>();

		MovieBasic movie1 = new MovieBasic();
		movie1.setTitle("Back to the Future");
		movies.add(movie1);
		MovieBasic movie2 = new MovieBasic();
		movie2.setTitle("Back to the Future II");
		movies.add(movie2);
		MovieBasic movie3 = new MovieBasic();
		movie3.setTitle("Back to the Future III");
		movies.add(movie3);

		assertEquals("Popular movies for the given genre are :\n" + "- Back to the Future\n"
				+ "- Back to the Future II\n" + "- Back to the Future III\n", formatter.genreReplyString(movies));

		assertEquals("No popular movies found for the given genre.",
				formatter.genreReplyString(new ArrayList<MovieBasic>()));
	}

	@Test
	public void ignoreMovieReplyTest() {
		MovieInfo movie = new MovieInfo();
		movie.setTitle("Avengers: Age of Ultron");
		assertEquals("The movie \"Avengers: Age of Ultron\" has been added to the blacklist.",
				formatter.ignoreMovieReplyString(movie));
	}

	@Test
	public void ignoredListReplyTest() {
		UsersInfo.INSTANCE.blacklist(1, MovieReferences.getMovieReference(1, "IT"));
		UsersInfo.INSTANCE.blacklist(1, MovieReferences.getMovieReference(2, "E.T."));
		UsersInfo.INSTANCE.blacklist(1, MovieReferences.getMovieReference(3, "EX"));
		assertEquals("The movies in your blacklist are :\n" + "- IT\n- E.T.\n- EX\n",
				formatter.ignoredListReplyString(UsersInfo.INSTANCE.getUserMovies(1).blacklisted()));

		assertEquals("There are no movies in your blacklist.",
				formatter.ignoredListReplyString(new ArrayList<MovieReference>()));
	}

	@Test
	public void notToWatchReplyTest() {
		MovieInfo movie = new MovieInfo();
		movie.setTitle("Hitman 47");
		assertEquals("The movie \"Hitman 47\" has been removed from the watchlist.",
				formatter.notToWatchMovieReplyString(movie));
	}

	@Test
	public void overviewReplyTest() {
		MovieInfo movie = new MovieInfo();
		movie.setOverview("Daniel Atlas è un mago delle carte con cui produce effetti magici e seduce fanciulle.");
		assertEquals(
				"About the movie :\nDaniel Atlas è un mago delle carte con cui produce effetti magici e seduce fanciulle.",
				formatter.overviewReplyString(movie));
	}

	@Test
	public void popularReplyTest() {
		List<MovieBasic> movies = new ArrayList<MovieBasic>();

		MovieBasic movie1 = new MovieBasic();
		movie1.setTitle("The Lord of the Ring I");
		movies.add(movie1);
		MovieBasic movie2 = new MovieBasic();
		movie2.setTitle("The Lord of the Ring II");
		movies.add(movie2);
		MovieBasic movie3 = new MovieBasic();
		movie3.setTitle("The Lord of the Ring III");
		movies.add(movie3);

		assertEquals(
				"The most popular movies at the moment are :\n"
						+ "- The Lord of the Ring I\n- The Lord of the Ring II\n- The Lord of the Ring III\n",
				formatter.popularReplyString(movies));

		assertEquals("No popular movies found.", formatter.popularReplyString(new ArrayList<MovieBasic>()));
	}

	@Test
	public void recentReplyTest() {
		List<MovieBasic> movies = new ArrayList<MovieBasic>();

		MovieBasic movie1 = new MovieBasic();
		movie1.setTitle("Batman Begins");
		movies.add(movie1);
		MovieBasic movie2 = new MovieBasic();
		movie2.setTitle("The Dark Knight");
		movies.add(movie2);
		MovieBasic movie3 = new MovieBasic();
		movie3.setTitle("The Dark Knight Rises");
		movies.add(movie3);
		assertEquals(
				"Popular movies released after the given date are :\n"
						+ "- Batman Begins\n- The Dark Knight\n- The Dark Knight Rises\n",
				formatter.recentReplyString(movies));

		assertEquals("No popular movies released after the given date found.",
				formatter.recentReplyString(new ArrayList<MovieBasic>()));
	}

	@Test
	public void toWatchReplyTest() {
		MovieInfo movie = new MovieInfo();
		movie.setTitle("Ghostbuster");
		assertEquals("The movie \"Ghostbuster\" has been added to the watchlist.",
				formatter.toWatchMovieReplyString(movie));
	}

	@Test
	public void watchReplyTest() {
		MovieInfo movie = new MovieInfo();
		movie.setTitle("Star Wars IV");
		assertEquals("The movie \"Star Wars IV\" has been added to the watched list.",
				formatter.watchMovieReplyString(movie));
	}

	@Test
	public void unwatchReplyTest() {
		MovieInfo movie = new MovieInfo();
		movie.setTitle("Jurassic Park");
		assertEquals("The movie \"Jurassic Park\" has been removed from the watched list.",
				formatter.unwatchMovieReplyString(movie));
	}

	@Test
	public void unignoreReplyTest() {
		MovieInfo movie = new MovieInfo();
		movie.setTitle("Sharknado II");
		assertEquals("The movie \"Sharknado II\" has been removed from the blacklist.",
				formatter.unignoreMovieReplyString(movie));
	}

	@Test
	public void toWatchListReplyTest() {
		UsersInfo.INSTANCE.addToWatchlist(1, MovieReferences.getMovieReference(1, "Epic Movie"));
		UsersInfo.INSTANCE.addToWatchlist(1, MovieReferences.getMovieReference(2, "Horror Movie"));
		UsersInfo.INSTANCE.addToWatchlist(1, MovieReferences.getMovieReference(3, "Scary Movie"));

		assertEquals("The movies on your watchlist are :\n" + "- Epic Movie\n- Horror Movie\n- Scary Movie\n",
				formatter.toWatchListReplyString(UsersInfo.INSTANCE.getUserMovies(1).watchlist()));

		assertEquals("There are no movies on your watchlist.",
				formatter.toWatchListReplyString(new ArrayList<MovieReference>()));
	}

	@Test
	public void watchedListReplyTest() {
		UsersInfo.INSTANCE.watch(1, MovieReferences.getMovieReference(1, "Inception"));
		UsersInfo.INSTANCE.watch(1, MovieReferences.getMovieReference(2, "The Wolf of Wall Street"));
		UsersInfo.INSTANCE.watch(1, MovieReferences.getMovieReference(3, "Revenant"));

		assertEquals("Movies you've already watched :\n" + "- Inception\n- The Wolf of Wall Street\n- Revenant\n",
				formatter.watchedListReplyString(UsersInfo.INSTANCE.getUserMovies(1).watched()));

		assertEquals("You haven't watched any movies yet.",
				formatter.watchedListReplyString(new ArrayList<MovieReference>()));
	}

	@Test
	public void trailerReplyTest() {
		List<Video> trailers = new ArrayList<Video>();
		List<Video> trailers2 = new ArrayList<Video>();

		Video trailer1 = new Video();
		trailer1.setKey("fdoi9uyEFPM");
		trailer1.setSite("youtube");
		trailers.add(trailer1);
		Video trailer2 = new Video();
		trailer2.setKey("_j_mEixBPMw");
		trailer2.setSite("youtube");
		trailers.add(trailer2);

		assertEquals(
				"The available trailers on YouTube are :\n"
						+ "- https://www.youtube.com/watch?v=fdoi9uyEFPM\n- https://www.youtube.com/watch?v=_j_mEixBPMw\n",
				formatter.trailerReplyString(trailers));
		assertEquals("There are no trailer video.", formatter.trailerReplyString(trailers2));
	}
}
