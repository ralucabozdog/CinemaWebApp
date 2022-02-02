package database.screen;

import model.Screen;

import java.util.List;

public interface ScreenDAO {
    Screen getScreenById(Integer id);

    Integer getIdOfScreen(String cinema, String screenName);

    //boolean deleteMovie(Movie movie);

    //boolean updateMovie(Movie movie);

    //boolean createMovie(Movie movie);
}
