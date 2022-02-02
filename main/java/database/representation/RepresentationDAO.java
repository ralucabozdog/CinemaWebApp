package database.representation;

import model.Movie;
import model.Representation;

import java.util.List;

public interface RepresentationDAO {
    Representation getRepresentationById(Integer id);

    List<Representation> getAllRepresentationsOfMovieInTheatre(int movie, int theatre);

    List<Representation> getAllRepresentationsInTheatreForDay(int theatre, int dayOfWeek);

    public List<Representation> getAllRepresentations();

    public boolean insertRepresentation(Representation representation);

    public boolean deleteRepresentation(Representation representation);

    //List<Representation> getAllRepresentationsOfMovieInTheatreForWeek(int movie, int theatre);

    //boolean deleteRepresentation(Representation representation);

    boolean updateRepresentation(Representation representation, int representationId);

    Integer getId(Representation representation);

    //boolean createRepresentation(Representation representation);
}
