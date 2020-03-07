package br.com.adrianojunior.savemynotes.activity;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.adrianojunior.savemynotes.R;
import br.com.adrianojunior.savemynotes.fragments.DevFrag;
import br.com.adrianojunior.savemynotes.fragments.NotesFrag;
import de.hdodenhof.circleimageview.CircleImageView;

public class BaseActivity extends livroandroid.lib.activity.BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BaseActivity";

    protected DrawerLayout drawerLayout;

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    // Configura a Toolbar
    protected void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    // Configura o Nav Drawer
    protected void setupNavDrawer(FirebaseUser user) {
        // Drawer Layout
        final ActionBar actionBar = getSupportActionBar();
        // Ícone do menu do nav drawer
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null && drawerLayout != null) {
            // Trata o evento de clique no menu.
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            // Seleciona a linha
                            menuItem.setChecked(true);
                            // Fecha o menu
                            drawerLayout.closeDrawers();
                            // Trata o evento do menu
                            onNavDrawerItemSelected(menuItem);
                            return true;
                        }
                    });

            setNavViewValues(navigationView, user);
        }
    }


    private void setNavViewValues(NavigationView navView, FirebaseUser user) {
        View headerView = navView.getHeaderView(0);
        TextView tNome = headerView.findViewById(R.id.textViewUsername);
        TextView tEmail =  headerView.findViewById(R.id.textViewUserEmail);
        CircleImageView imgView = headerView.findViewById(R.id.imageViewUser);
        tNome.setText(user.getDisplayName());
        tEmail.setText(user.getEmail());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.default_image);
        Glide.with(getContext()).applyDefaultRequestOptions(requestOptions)
                .load(user.getPhotoUrl()).into(imgView);
    }

    // Trata o evento do menu lateral
    private void onNavDrawerItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_notes:
                replaceFragment(new NotesFrag());
                getSupportActionBar().setTitle(R.string.my_notes);
                break;
            case R.id.nav_action_sair:
                logOut();
                break;
            case R.id.nav_desenvolvedor:
                replaceFragment(new DevFrag());
                getSupportActionBar().setTitle("Desenvolvedor");
                break;
        }
    }

    // Adiciona o fragment no centro da tela
    protected void replaceFragment(Fragment frag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag, "TAG").commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Trata o clique no botão que abre o menu.
                if (drawerLayout != null) {
                    openDrawer();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    // Abre o menu lateral
    protected void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    // Fecha o menu lateral
    protected void closeDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    protected void sendToLogin() {
        Intent loginIntent = new Intent(getContext(), LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    protected void sendToMain() {
        Intent mainIntent = new Intent(getContext(), MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    protected void logOut() {
        FirebaseAuth.getInstance().signOut();
        sendToLogin();
    }
}