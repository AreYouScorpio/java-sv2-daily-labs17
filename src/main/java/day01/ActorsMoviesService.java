package day01;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ActorsMoviesService {

    private ActorsRepository actorsRepository;
    private MoviesRepository moviesRepository;
    private ActorsMoviesRepository actorsMoviesRepository;

    public ActorsMoviesService(ActorsRepository actorsRepository, MoviesRepository moviesRepository, ActorsMoviesRepository actorsMoviesRepository) {
        this.actorsRepository = actorsRepository;
        this.moviesRepository = moviesRepository;
        this.actorsMoviesRepository = actorsMoviesRepository;
    }

    public void insertMovieWithActors(String title, LocalDate releasedDate, List<String> actorNames) {
        long movie_id = moviesRepository.saveMovie(title, releasedDate);
        for (String actual : actorNames) {
            long actor_id;
            Optional<Actor> found = actorsRepository.findActorByName(actual);
            if (found.isPresent()) {
                actor_id = found.get().getId();
            } else {
                actor_id = actorsRepository.saveActor(actual);
            }
            actorsMoviesRepository.insertActorAndMovieId(actor_id, movie_id);
        }
    }
}

