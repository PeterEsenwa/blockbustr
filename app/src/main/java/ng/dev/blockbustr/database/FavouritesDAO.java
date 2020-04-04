package ng.dev.blockbustr.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ng.dev.blockbustr.models.MovieDetails;

@Dao
public interface FavouritesDAO {

    @Query("SELECT * FROM favourites ORDER BY title")
    LiveData<List<MovieDetails>> loadFavourites();

    @Insert
    void addNewFavourite(MovieDetails movieDetails);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavourite(MovieDetails movieDetails);

    @Delete
    void deleteFavourite(MovieDetails movieDetails);
}
