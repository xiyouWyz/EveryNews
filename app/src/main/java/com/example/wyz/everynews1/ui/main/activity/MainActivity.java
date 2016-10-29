package com.example.wyz.everynews1.ui.main.activity;

import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.example.wyz.everynews1.R;
import com.example.wyz.everynews1.ui.news.fragment.NewsMainFragment;
import com.example.wyz.everynews1.ui.photo.fragment.PhotoMainFragment;
import com.example.wyz.everynews1.ui.video.fragment.VideoMainFragment;

import butterknife.BindView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.id;


public class MainActivity extends AppCompatActivity {
    final String TAG="MainActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawLayout)
    DrawerLayout drawLayout;
    @BindView(R.id.nav_head)
    NavigationView navigationView;
    @BindView(R.id.drawer_container)
    FrameLayout drawer_container;
    @BindView(R.id.tabs)
    TabLayout mTabs;

    private ActionBarDrawerToggle drawerToggle;
    NewsMainFragment newsMainFragment;
    PhotoMainFragment photoMainFragment;
    VideoMainFragment videoMainFragment;
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar("EveryNews");
        initDrawerToggle();
        initNavigationItem(navigationView);
        initTabs();
    }

    private void initTabs() {

    }

    private void initNavigationItem(NavigationView view) {
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                fm=getSupportFragmentManager();
                switch (menuItem.getItemId())
                {
                    case R.id.nav_news:
                        newsMainFragment=new NewsMainFragment();
                        fm.beginTransaction().replace(R.id.drawer_container,newsMainFragment).commit();
                        drawLayout.closeDrawers();
                        break;
                    case R.id.nav_photo:
                        photoMainFragment=new PhotoMainFragment();
                        fm.beginTransaction().replace(R.id.drawer_container,photoMainFragment).commit();
                        drawLayout.closeDrawers();
                        break;
                    case R.id.nav_video:
                        videoMainFragment=new VideoMainFragment();
                        fm.beginTransaction().replace(R.id.drawer_container,videoMainFragment).commit();
                        drawLayout.closeDrawers();
                        break;
                }

                return true;
            }
        });
        Log.d(TAG,String.valueOf(view.getMenu().getItem(0).getItemId()));
    }

    private  void initToolbar(String title)
    {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private  void initDrawerToggle()
    {
        drawerToggle = new ActionBarDrawerToggle(this, drawLayout, toolbar, 0, 0);
        drawerToggle.syncState();
        drawLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {

            case  R.id.action_settings:
                Toast.makeText(MainActivity.this,"Settings",Toast.LENGTH_SHORT).show();
                break;
            case  R.id.action_settings1:
                Toast.makeText(MainActivity.this,"Settings1",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click();
        }
        return  false;
    }
    //是否点击了退出按钮
    private static Boolean isExit = false;
    private void exitBy2Click() {
        Timer tExit=null;
        if(isExit==false)
        {
            //准备退出
            isExit=true;
            Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            tExit=new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit=false;       //取消退出
                }
            },2000 );// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        }
        else
        {
            finish();
            System.exit(0);
        }
    }
}
