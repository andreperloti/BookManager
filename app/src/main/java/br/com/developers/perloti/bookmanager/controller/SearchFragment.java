package br.com.developers.perloti.bookmanager.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.developers.perloti.bookmanager.R;
import br.com.developers.perloti.bookmanager.controller.adapter.SearchBookAdapter;
import br.com.developers.perloti.bookmanager.util.ApplicationUtil;
import br.com.developers.perloti.bookmanager.util.BMUtil;
import br.com.developers.perloti.bookmanager.util.JsonUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by perloti on 16/06/18.
 */

public class SearchFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.text_view_label)
    TextView textViewLabelList;
    @BindView(R.id.view_error)
    View viewError;
    @BindView(R.id.button_try_again)
    Button buttonErrorTryAgain;
    @BindView(R.id.textview_empty)
    TextView textViewEmpty;
    @BindView(R.id.view_empty)
    View viewEmpty;
    @BindView(R.id.view_content)
    View viewContent;

    private SearchBookAdapter adapter;
    private LinkedTreeMap linkedTreeMap;
    private String searchCurrent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        if (getActivity() != null)
            getActivity().setTitle(R.string.title_search);

        setupComponents();
        generateSearchRandom();
        downloadBooks();
        return view;
    }

    private void generateSearchRandom() {
        Random randomGenerator = new Random();
        ArrayList<String> items = new ArrayList<>();
        items.add("php");
        items.add("java");
        items.add("android");
        items.add("arduino");
        items.add("firebase");
        int index = randomGenerator.nextInt(items.size());
        searchCurrent = items.get(index);
    }


    void onUpdate() {
        List<LinkedTreeMap> items = JsonUtil.getList(linkedTreeMap, "items");
        if (items != null && !items.isEmpty()) {
            String label = ApplicationUtil.context.getString(R.string.displaying_results_from) + " " + searchCurrent;
            textViewLabelList.setText(label);
            adapter = new SearchBookAdapter(getActivity(), items);
            recyclerView.setAdapter(adapter);
            viewContent.setVisibility(View.VISIBLE);
            viewEmpty.setVisibility(View.GONE);
        } else {
            viewEmpty.setVisibility(View.VISIBLE);
            viewContent.setVisibility(View.GONE);
            textViewEmpty.setText(R.string.no_results_for_this_search_try_again_with_other_words);
        }
    }


    private void downloadBooks() {
        if (BMUtil.statusNetwork(getActivity())) {
            setRefreshing(true);
            ClienteAPI.MyRetrofit.getInstance().getBook(searchCurrent)
                    .enqueue(new Callback<LinkedTreeMap>() {
                        @Override
                        public void onResponse(Call<LinkedTreeMap> call, Response<LinkedTreeMap> response) {
                            int statusCode = response.code();
                            Log.e(BMUtil.TAG_REQUEST, "getBook statusCode: " + statusCode);
                            if (BMUtil.isStatusCode(statusCode)) {
                                viewError.setVisibility(View.GONE);
                                linkedTreeMap = response.body();
                                onUpdate();
                            } else {
                                viewError.setVisibility(View.VISIBLE);
                            }
                            setRefreshing(false);
                        }

                        @Override
                        public void onFailure(Call<LinkedTreeMap> call, Throwable t) {
                            Log.e(BMUtil.TAG_REQUEST, "ERROR getBook - onFailure ");
                            setRefreshing(false);
                            viewError.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() < 4) {
                    BMUtil.toastShort(getActivity().getString(R.string.minimum_of_three_characters));
                    return true;
                } else {
                    searchCurrent = query;
                    downloadBooks();
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupComponents() {
        viewContent.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        registerForContextMenu(recyclerView);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primary_dark, R.color.primary, R.color.primary_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadBooks();
            }
        });
        buttonErrorTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadBooks();
            }
        });
    }

    private void setRefreshing(final boolean refreshing) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(refreshing);
            }
        });
    }

}
