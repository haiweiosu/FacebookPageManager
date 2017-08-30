package haiweisu.facebookpagesmanager;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This Section Fragment has multiple section chioces
 * where each contains a view
 */

public class SectionFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String PAGE_ID = "1908563859417632";
    private static final String accountPATH = "/me/accounts";
    private static final String getFeedPATH = "/" + PAGE_ID + "/feed";

    public SectionFragment() {

    }

    /**
     * This newInstance method will return a new instance
     * of this fragment
     */
    public static SectionFragment newInstance(int sectionNumber) {
        SectionFragment sFragment = new SectionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, sectionNumber);
        sFragment.setArguments(bundle);
        return sFragment;
    }

    /**
     * This function will return a new instance of Facebook Graph API
     */

    private GraphRequest getGraphRequest(View view, final TableLayout tableLayout,
                                         StringBuilder publishedPosts, StringBuilder unpublishedPosts) {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                accountPATH,
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject jsonObject = response.getJSONObject();
                        try {
                            JSONArray infos = jsonObject.getJSONArray("data");
                            JSONObject objectInfo = infos.getJSONObject(0);
                            String accessToken = objectInfo.getString("access_token");

                            Log.d("Access Token :", AccessToken.getCurrentAccessToken().getToken());

                            new GraphRequest(AccessToken.getCurrentAccessToken(), getFeedPATH,
                                    null, HttpMethod.GET,
                                    new GraphRequest.Callback() {
                                        public void onCompleted(GraphResponse response) {
                                            if (response.getError() == null) {
                                                try {
                                                    JSONArray info = response.getJSONObject().getJSONArray("data");
                                                    for (int i = 0; i < info.length(); i++) {
                                                        JSONObject o = info.getJSONObject(i);
                                                        String message = o.getString("message");
                                                        String createdTime = o.getString("created_time");

                                                        TextView view1 = new TextView(getActivity());
                                                        view1.setText(message);
                                                        view1.setTextSize(15);
                                                        view1.setSingleLine(false);

                                                        try {
                                                            createdTime = new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a").format(
                                                                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(o.getString("created_time"))).toString();
                                                        } catch (ParseException e) {
                                                            Log.d(e.getMessage(), "Parse Error Message");
                                                        }

                                                        TextView tv2 = new TextView(getActivity());
                                                        tv2.setText("(Created time: " + createdTime + ")");
                                                        tv2.setTextColor(Color.parseColor("#B03060"));
                                                        tv2.setTextSize(15);

                                                        TableRow tr1 = new TableRow(getActivity());
                                                        tr1.addView(view1);
                                                        tr1.setPadding(0, 0, 0, 1);
                                                        TableRow tr2 = new TableRow(getActivity());
                                                        tr2.addView(tv2);
                                                        tr2.setPadding(0, 0, 0, 15);
                                                        tableLayout.addView(tr1, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                                        tableLayout.addView(tr2, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                                    }
                                                } catch (JSONException e) {
                                                    Log.d("JSON Exception", e.getMessage());
                                                }
                                            }
                                        }
                                    }).executeAsync();
                        } catch (JSONException e) {
                            Log.d(e.getMessage(), "Error Message");
                        }
                    }
                }
        ).executeAsync();
        return request;
    }

    /**
     * This function will generaste an object of GraphRequest when
     * ARGS_SECTION_NUMER is 4 or 3.
     */

    private GraphRequest getGraphRequestForViewsAndLikes(View view, final TableLayout tableLayout,
                                                         StringBuilder publishedPosts, StringBuilder unpublishedPosts) {

        GraphRequest requestForViewsAndLikes =
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/accounts",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject JsonObject = response.getJSONObject();
                        try {
                            JSONArray infos = JsonObject.getJSONArray("data");
                            JSONObject objectinfos = infos.getJSONObject(0);
                            String accessToken = objectinfos.getString("access_token");

                            Bundle bundle = new Bundle();
                            bundle.putString("fields", "message,insights.metric(post_impressions,post_reactions_like_total)");

                            new GraphRequest(AccessToken.getCurrentAccessToken(), getFeedPATH, bundle, HttpMethod.GET,
                                    new GraphRequest.Callback() {
                                        public void onCompleted(GraphResponse response) {

                                            if (response.getError() == null) {

                                                try {
                                                    JSONArray infos = response.getJSONObject().getJSONArray("data");
                                                    Log.d("POST_LEVEL STATS", infos.toString());

                                                    for (int i = 0; i < infos.length(); i++) {
                                                        JSONObject obj = infos.getJSONObject(i);
                                                        String message = obj.getString("message");

                                                        JSONObject insights = obj.getJSONObject("insights");
                                                        String num_views = insights.getJSONArray("data").getJSONObject(0).getJSONArray("values").getJSONObject(0).getString("value");
                                                        String num_likes = insights.getJSONArray("data").getJSONObject(1).getJSONArray("values").getJSONObject(0).getString("value");

                                                        TextView tv1 = new TextView(getActivity());
                                                        tv1.setText(message);
                                                        tv1.setTextSize(20);
                                                        tv1.setSingleLine(false);

                                                        TextView tv2 = new TextView(getActivity());
                                                        tv2.setText("(Views: " + num_views + ", Likes: " + num_likes + ")");
                                                        tv2.setTextColor(Color.parseColor("#B03060"));
                                                        tv2.setTextSize(15);

                                                        TableRow tr1 = new TableRow(getActivity());
                                                        tr1.addView(tv1);
                                                        tr1.setPadding(0, 0, 0, 1);
                                                        TableRow tr2 = new TableRow(getActivity());
                                                        tr2.addView(tv2);
                                                        tr2.setPadding(0, 0, 0, 15);
                                                        tableLayout.addView(tr1, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                                        tableLayout.addView(tr2, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                                                    }
                                                } catch (JSONException e) {
                                                    Log.d("JSON Exception", e.getMessage());
                                                }
                                            } else {
                                                Log.d(response.getError().getErrorMessage(), "Error Message :");
                                            }
                                        }
                                    }).executeAsync();

                        } catch (JSONException j) {
                            Log.d(j.getMessage(), "Error Message");
                        }
                    }
                }
        );
        return requestForViewsAndLikes;
    }


    /**
     * @param layoutInflater
     * @param container
     * @param saveInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstanceState) {
        View rootView = layoutInflater.inflate(R.layout.fragment_view_posts, container, false);
        final TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.maintable);
        final StringBuilder publishedPosts = new StringBuilder();
        final StringBuilder unPublishedPosts = new StringBuilder();

        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
            GraphRequest graphRequest1 = getGraphRequest(rootView, tableLayout, publishedPosts, unPublishedPosts);
        }
        if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
            GraphRequest graphRequest2 = getGraphRequest(rootView, tableLayout, publishedPosts, unPublishedPosts);
        }
        if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
            GraphRequest graphRequest2 = getGraphRequest(rootView, tableLayout, publishedPosts, unPublishedPosts);
        }
        if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
            GraphRequest graphRequest2 = getGraphRequest(rootView, tableLayout, publishedPosts, unPublishedPosts);
        }





    }


}
