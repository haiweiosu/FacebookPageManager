package haiweisu.facebookpagesmanager;

import android.graphics.Color;
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
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This Section Fragment has multiple section chioces
 * where each contains a view
 */

public class SectionFragment extends Fragment {
    private static final String argSectionNumber = "section_number";
    private static final String pageId = "1908563859417632";
    private static final String accountPath = "/me/accounts";
    private static final String getPublishedFeedPath = "/" + pageId + "/feed";
    private static final String getUnpublishedPath = "/" + pageId + "/promotable_posts";
    private static final String getPublishedPosts = "/" + pageId + "/posts";
    private static final String getPage = "/" + pageId + "/";
    private static final String invalidNumber = "Invalid number ";
    private static final String errorMessage = "Error Message ";

    public SectionFragment() {

    }

    /**
     * This newInstance method will return a new instance
     * of this fragment
     */
    public static SectionFragment newInstance(int sectionNumber) {
        SectionFragment sFragment = new SectionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(argSectionNumber, sectionNumber);
        sFragment.setArguments(bundle);
        return sFragment;
    }

    /**
     * This function will return a new instance of Facebook Graph API
     */

    private GraphRequestAsyncTask getGraphRequest(final TableLayout tableLayout, final String argSectionNumber, final StringBuilder fbPublishedPosta) {

        final Bundle bundle = new Bundle();
        if (argSectionNumber.equals("2")) {
            bundle.putBoolean("is_published", false);
        }
        GraphRequestAsyncTask request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                accountPath,
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject jsonObject = response.getJSONObject();
                        try {
                            JSONArray infos = jsonObject.getJSONArray("data");
                            JSONObject objectdata = infos.getJSONObject(0);
                            String accessToken = objectdata.getString("access_token");

                            Log.d("Access Token :", AccessToken.getCurrentAccessToken().getToken());
                            AccessToken accessToken_obj = new AccessToken(accessToken, AccessToken.getCurrentAccessToken().getApplicationId(),
                                    AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(),
                                    AccessToken.getCurrentAccessToken().getDeclinedPermissions(), AccessToken.getCurrentAccessToken().getSource(),
                                    AccessToken.getCurrentAccessToken().getExpires(), AccessToken.getCurrentAccessToken().getLastRefresh());

                            new GraphRequest(accessToken_obj,
                                    argSectionNumber.equals("2") ? getUnpublishedPath : getPublishedFeedPath,
                                    argSectionNumber.equals("2") ? bundle : null, HttpMethod.GET,
                                    new GraphRequest.Callback() {
                                        public void onCompleted(GraphResponse response) {
                                            onNewGraphRequestComplete(response);
                                        }
                                    }).executeAsync();
                        } catch (JSONException e) {
                            Log.d(e.getMessage(), errorMessage);
                        }
                    }

                    private void onNewGraphRequestComplete(GraphResponse response) {
                        if (response.getError() != null) {
                            return;
                        }

                        try {
                            JSONArray info = response.getJSONObject().getJSONArray("data");
                            for (int i = 0; i < info.length(); i++) {
                                JSONObject o = info.getJSONObject(i);
                                String message = o.getString("message");
                                String createdTime = o.getString("created_time");
                                if (argSectionNumber.equals("2")) {
                                    fbPublishedPosta.append(message);
                                }
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
                                tv2.setTextColor(Color.parseColor("#AEBEE0"));
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
        ).executeAsync();
        return request;
    }

    /**
     * This is function will be called when ARG_SECTION_ NUMBER is 4
     * And it will get number of views and likes for given post.
     *
     * @param response
     */
    private void onCompletedForViewsAndLikes(GraphResponse response, TableLayout tableLayout) {

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
                    tv2.setTextColor(Color.parseColor("#AEBEE0"));
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
            Log.d(response.getError().getErrorMessage(), errorMessage);
        }
    }

    /**
     * This function will get Facebook Pages stats.
     *
     * @param response
     * @param tableLayout
     */

    private void onCompletedForPageStats(GraphResponse response, TableLayout tableLayout) {

        if (response.getError() != null) {
            Log.d(response.getError().getErrorMessage(),errorMessage);
            return;
        }
        try {
            JSONObject insights = response.getJSONObject().getJSONObject("insights");
            Log.d("Facebook Page Stats", insights.toString());

            JSONArray data = insights.getJSONArray("data");

            TextView tv = new TextView(getActivity());
            tv.setText("Monthly Stats");
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setTextSize(20);

            TableRow tr = new TableRow(getActivity());
            tr.addView(tv);
            tr.setPadding(0, 0, 0, 25);

            tableLayout.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);

                String name = obj.getString("name");
                String values = obj.getJSONArray("values").getJSONObject(0).getString("value");

                if (name.equals("page_positive_feedback_by_type")) {
                    String num_likes = obj.getJSONArray("values").getJSONObject(0).getJSONObject("value").getString("like");
                    String num_comments = obj.getJSONArray("values").getJSONObject(0).getJSONObject("value").getString("comment");

                    TextView tv0 = new TextView(getActivity());
                    tv0.setText("page positive feedback by type" + ": ");
                    tv0.setTextColor(Color.parseColor("#AEBEE0"));
                    tv0.setTextSize(20);

                    TableRow tr0 = new TableRow(getActivity());
                    tr0.addView(tv0);
                    tr0.setPadding(0, 0, 0, 15);

                    TextView tv1 = new TextView(getActivity());
                    tv1.setText("       Like" + ": " + num_likes);
                    tv1.setTextColor(Color.parseColor("#AEBEE0"));
                    tv1.setTextSize(20);

                    TableRow tr1 = new TableRow(getActivity());
                    tr1.addView(tv1);
                    tr1.setPadding(0, 0, 0, 15);

                    TextView tv2 = new TextView(getActivity());
                    tv2.setText("       Comment" + ": " + num_comments);
                    tv2.setTextColor(Color.parseColor("#AEBEE0"));
                    tv2.setTextSize(20);

                    TableRow tr2 = new TableRow(getActivity());
                    tr2.addView(tv2);
                    tr2.setPadding(0, 0, 0, 15);

                    tableLayout.addView(tr0, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tableLayout.addView(tr1, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tableLayout.addView(tr2, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                } else {
                    name = name.replace("_", " ");
                    TextView tv1 = new TextView(getActivity());
                    tv1.setText(name + ": " + values);
                    tv1.setTextColor(Color.parseColor("#B03060"));
                    tv1.setTextSize(20);

                    TableRow tr1 = new TableRow(getActivity());
                    tr1.addView(tv1);
                    tr1.setPadding(0, 0, 0, 15);
                    tableLayout.addView(tr1, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                }

            }

        } catch (JSONException e) {
            Log.d("JSON Exception", e.getMessage());
        }
    }


    /**
     * This function will generaste an object of GraphRequest when
     * ARGS_SECTION_NUMBER is 4 or 3.
     */

    private GraphRequestAsyncTask getGraphRequestForViewsAndLikes(final TableLayout tableLayout, final String ARG_SECTION_NUMBER) {
        GraphRequestAsyncTask requestForViewsAndLikes =
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        accountPath,
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                onRequestComplete(response, ARG_SECTION_NUMBER, tableLayout);
                            }
                        }
                ).executeAsync();
        return requestForViewsAndLikes;
    }

    private void onRequestComplete(GraphResponse response, final String ARG_SECTION_NUMBER, final TableLayout tableLayout) {
        JSONObject JsonObject = response.getJSONObject();
        try {
            JSONArray infos = JsonObject.getJSONArray("data");
            JSONObject objectinfos = infos.getJSONObject(0);
            String accessToken = objectinfos.getString("access_token");

            Bundle bundle = new Bundle();
            if (ARG_SECTION_NUMBER.equals("4")) {
                bundle.putString("fields", "message,insights.metric(post_impressions,post_reactions_like_total)");
            }
            if (ARG_SECTION_NUMBER.equals("3")) {
                bundle.putString("fields", "insights.metric(page_impressions, page_views_total, page_impressions_unique, " +
                        "page_engaged_users, page_positive_feedback_by_type).date_preset(today)");

            }
            // Get the aceestoken object for page
            final AccessToken accessToken_obj = new AccessToken(accessToken, AccessToken.getCurrentAccessToken().getApplicationId(),
                    AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(),
                    AccessToken.getCurrentAccessToken().getDeclinedPermissions(), AccessToken.getCurrentAccessToken().getSource(),
                    AccessToken.getCurrentAccessToken().getExpires(), AccessToken.getCurrentAccessToken().getLastRefresh());

            new GraphRequest(accessToken_obj, ARG_SECTION_NUMBER.equals("4") ? getPublishedPosts : getPage, bundle, HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            if (ARG_SECTION_NUMBER.equals("4")) {
                                onCompletedForViewsAndLikes(response, tableLayout);
                                return;
                            }
                            if (ARG_SECTION_NUMBER.equals("3")) {
                                onCompletedForPageStats(response, tableLayout);
                            }
                        }
                    }).executeAsync();

        } catch (JSONException j) {
            Log.d(j.getMessage(), "Error Message");
        }
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

        int n = getArguments().getInt(argSectionNumber);
        switch (n) {
            case 1:
                getGraphRequest(tableLayout, "1", publishedPosts);
                break;
            case 2:
                getGraphRequest(tableLayout, "2", publishedPosts);
                break;
            case 3:
                getGraphRequestForViewsAndLikes(tableLayout, "3");
                break;
            case 4:
                getGraphRequestForViewsAndLikes(tableLayout, "4");
                break;
            default:
                throw new IllegalStateException(invalidNumber + n);
        }
        return rootView;
    }
}


