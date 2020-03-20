package ng.dev.blockbustr;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_top_rated, R.id.nav_most_viewed,
                R.id.nav_now_showing).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_movie_details) {
                hideBottomNavigation();
            } else {
                showBottomNavigation();
            }
        });

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void hideBottomNavigation() {
        // bottom_navigation is BottomNavigationView
        if (navView.getVisibility() == View.VISIBLE && navView.getAlpha() == 1f) {
            navView.animate()
                    .alpha(0f)
                    .withEndAction(() -> navView.setVisibility(View.GONE))
                    .setDuration(400);
        }
    }

    private void showBottomNavigation() {
        // bottom_navigation is BottomNavigationView
        if (navView.getVisibility() == View.GONE && navView.getAlpha() == 0f) {
            navView.setVisibility(View.VISIBLE);
            navView.animate()
                    .alpha(1f)
                    .setDuration(400);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
