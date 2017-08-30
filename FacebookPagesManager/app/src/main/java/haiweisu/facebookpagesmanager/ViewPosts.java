package haiweisu.facebookpagesmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
        fbFragmentPagerAdapter = new FragmentPagerAdapter() {
            @Override
            public Fragment getItem(int position) {
                return Place
            }

            @Override
            public int getCount() {
                return 0;
            }
        }
    }
}
