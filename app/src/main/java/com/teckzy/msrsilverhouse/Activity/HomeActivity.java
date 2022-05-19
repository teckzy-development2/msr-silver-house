package com.teckzy.msrsilverhouse.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.teckzy.msrsilverhouse.BottomFragment.CartFragment;
import com.teckzy.msrsilverhouse.BottomFragment.CategoryFragment;
import com.teckzy.msrsilverhouse.BottomFragment.HomeFragment;
import com.teckzy.msrsilverhouse.BottomFragment.ProfileFragment;
import com.teckzy.msrsilverhouse.BuildConfig;
import com.teckzy.msrsilverhouse.R;
import com.teckzy.msrsilverhouse.SubFragment.AboutFragment;
import com.teckzy.msrsilverhouse.SubFragment.ContactUsFragment;
import com.teckzy.msrsilverhouse.SubFragment.NeedHelpFragment;
import com.teckzy.msrsilverhouse.SubFragment.PrivacyPolicyFragment;
import com.teckzy.msrsilverhouse.SubFragment.TermsAndConditionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener
{
    Fragment fragment = null;
    Toolbar toolbar;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView tvUsername;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView ivWhatsapp, ivCall,ivLogo;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        frameLayout = findViewById(R.id.frameLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        View header = navigationView.getHeaderView(0);
        ivLogo = header.findViewById(R.id.ivLogo);
        tvUsername = header.findViewById(R.id.tvUsername);
        ivWhatsapp = findViewById(R.id.ivWhatsapp);
        ivCall = findViewById(R.id.ivCall);

        Glide.with(ivLogo)
                .load(R.drawable.msr)
                .fitCenter()
                .into(ivLogo);

        sharedPreferences = getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        showRemoveCartCount();

        bottomNavigationView.setItemIconTintList(null);

        navigationView.setItemIconTintList(null);

        if (fragment == null) {
            fragment = new HomeFragment();
            loadFragment(fragment);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                displaySelectedScreen(menuItem.getItemId());
                return true;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                displaySelectedScreen(menuItem.getItemId());
                return true;
            }
        });

        ivWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    url = "https://wa.me/919553993885";
                    PackageManager pm = getApplicationContext().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Whatsapp app not installed in your phone", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        ivCall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "+91 95539 93885"));
                startActivity(intent);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        toggle.syncState();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void showRemoveCartCount() {
        if (sharedPreferences.getString("name", "").equals("")) {
            tvUsername.setText("Hi, Guest");
            removeBadge(bottomNavigationView, R.id.navigationCart);
        } else {
            if (sharedPreferences.getInt("cart_count", 0) == 0) {
                removeBadge(bottomNavigationView, R.id.navigationCart);
            } else {
                showBadge(this, bottomNavigationView, R.id.navigationCart, sharedPreferences.getInt("cart_count", 0));
            }
            tvUsername.setText(sharedPreferences.getString("name", ""));
        }
    }

    public static void showBadge(Context context, BottomNavigationView bottomNavigationView, @IdRes int itemId, int value) {
        removeBadge(bottomNavigationView, itemId);
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.notification_badge, bottomNavigationView, false);

        TextView text = badge.findViewById(R.id.notifications_badge);
        text.setText(String.valueOf(value));
        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }

    private void displaySelectedScreen(int itemId) {
        switch (itemId) {
            /* bottom navigation menu */
            case R.id.navigationHome:
                showRemoveCartCount();
                fragment = new HomeFragment();
                loadFragment(fragment);
                break;

            case R.id.navigationMenu:
                showRemoveCartCount();
                fragment = new CategoryFragment();
                loadFragment(fragment);
                break;

            case R.id.navigationCart:
                showRemoveCartCount();
                if (sharedPreferences.getInt("customer_id", 0) == 0) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    fragment = new CartFragment();
                }
                loadFragment(fragment);
                break;

            case R.id.navigationProfile:
                showRemoveCartCount();
                fragment = new ProfileFragment();
                loadFragment(fragment);
                break;

            /* side navigation menu */

            case R.id.navigationAbout:
                showRemoveCartCount();
                fragment = new AboutFragment();
                loadFragment(fragment);
                break;
            case R.id.navigationContactUs:
                showRemoveCartCount();
                fragment = new ContactUsFragment();
                loadFragment(fragment);
                break;
            case R.id.navigationTermsAndConditions:
                showRemoveCartCount();
                fragment = new TermsAndConditionFragment();
                loadFragment(fragment);
                break;
            case R.id.navigationPrivacypolicy:
                showRemoveCartCount();
                fragment = new PrivacyPolicyFragment();
                loadFragment(fragment);
                break;
            case R.id.navigationNeedHelp:
                showRemoveCartCount();
                fragment = new NeedHelpFragment();
                loadFragment(fragment);
                break;
            case R.id.navigationShareapp:
                showRemoveCartCount();
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MSR Silver House");
                    String shareMessage = "Let me recommend you this application\n\n";
                    //shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
        }
    }

    public void loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null);
            ft.commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {
        tellFragments();
        super.onBackPressed();
    }

    private void tellFragments() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f != null && f instanceof HomeFragment)

                ((HomeFragment) f).onBackPressed();
        }
    }
}