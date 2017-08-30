package haiweisu.facebookpagesmanager;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TableLayout;

/**
 * Created by haiweisu on 8/27/17.
 */

public class ViewPosts extends AppCompatActivity {

    // A PagerAdapter may implement a form of View recycling
    // if desired or use a more sophisticated method of managing page Views
    // such as Fragment transactions where each page is represented by its own Fragment.
    private FragmentPagerAdapter fbFragmentPagerAdapter;

    /**
     * The {@link ViewPager} will host section contents
     */

    private ViewPager fbViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fbFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return SectionFragment.newInstance(position + 1);
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public String getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Published Posts";
                    case 1:
                        return "Unpublished Posts";
                    case 2:
                        return "Page Statistics";
                    case 3:
                        return "Post Statistics";
                }
                return null;
            }
        };

        fbViewPager = (ViewPager) findViewById(R.id.container);
        fbViewPager.setAdapter(fbFragmentPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(fbViewPager);
    }

    /**
     * This function will add items to the action bar if exists
     * @param menu
     * @return true
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_);
    }
}
