package by.mtz.reminder.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import by.mtz.reminder.R;

/**
 * Created by Roma on 07.10.2016.
 */

public class BaseActivity extends AppCompatActivity{

    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private Toolbar mToolbar;
    private ImageView homeButton;
    private TextView toolbarTv;
    private ImageButton toolbarButton;

    Context context = this;

     View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("adasdad","asdasdasda");
            mDrawer.openDrawer(GravityCompat.START);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initToolbarAndDrawerWithReadableName(String title) {

        Log.d("init","i am here");
        toolbarTv = (TextView)findViewById(R.id.toolbarTv);
        toolbarTv.setText(title);

        View child = (View)getLayoutInflater().inflate(R.layout.drawer_header, null);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        nvDrawer.addHeaderView(child);

        mToolbar = (Toolbar) findViewById(R.id.auth_toolbar);
        setSupportActionBar(mToolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawerContent(nvDrawer);

        toolbarButton = (ImageButton) mToolbar.findViewById(R.id.drawer_button);
        toolbarButton.setOnClickListener(click);
    }


    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }


    private void selectDrawerItem(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case R.id.actions:{
                Intent myIntent = new Intent(this, ActionListActivity.class);
                this.startActivity(myIntent);
                break;
            }
            case R.id.persons:{
                Intent myIntent = new Intent(this, PersonsListActivity.class);
                this.startActivity(myIntent);
                break;
            }
            case R.id.home:{
                Intent myIntent = new Intent(this, MainActivity.class);
                this.startActivity(myIntent);
                break;
            }
        }
    }
}
