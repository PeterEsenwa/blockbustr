package ng.dev.blockbustr.database;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ng.dev.blockbustr.models.MovieDetails;
import ng.dev.blockbustr.utils.DateConverter;
import ng.dev.blockbustr.utils.GenreListConverter;

@Database(entities = {MovieDetails.class}, version = 2, exportSchema = false)
@TypeConverters({DateConverter.class, GenreListConverter.class})
public abstract class FavouritesDB extends RoomDatabase {

    public static final Executor dbIO = Executors.newSingleThreadExecutor();
    private static final Object LOCK = new Object();
    private static final String DB_NAME = "favourites";
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE favourites ADD COLUMN genres TEXT");
        }
    };
    public static Executor dbReadIO;
    private static FavouritesDB sFavouritesDB;

    public static FavouritesDB getInstance(Context context) {
        dbReadIO = ContextCompat.getMainExecutor(context);
        if (sFavouritesDB == null) {
            synchronized (LOCK) {
                sFavouritesDB = Room
                        .databaseBuilder(context.getApplicationContext(), FavouritesDB.class, FavouritesDB.DB_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        return sFavouritesDB;
    }

    public abstract FavouritesDAO dao();
}
