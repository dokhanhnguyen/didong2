package vn.dangyen.onthitracnghiem.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import vn.dangyen.onthitracnghiem.R;
import vn.dangyen.onthitracnghiem.fragment.ScoreboardFragment;
import vn.dangyen.onthitracnghiem.fragment.HomeFragment;
import vn.dangyen.onthitracnghiem.fragment.SelectFragment;
import vn.dangyen.onthitracnghiem.fragment.Test_Fragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.main_toolbar);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            showDefaultFragment();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //lựa chọn trang chủ
            case R.id.nav_home:
                showDefaultFragment();
                break;
                //sự kiện lựa chọn bảng điểm
            case R.id.nav_scoreboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ScoreboardFragment()).commit();
                toolbar.setTitle("Bảng điểm");
                break;
            case  R.id.nav_test:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new Test_Fragment()).commit();
                toolbar.setTitle("Thi Online");
                break;
            case  R.id.nav_exam:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new SelectFragment()).commit();
                toolbar.setTitle("Import Đề Thi");
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDefaultFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Exam UNETI");
        }
    }
}
