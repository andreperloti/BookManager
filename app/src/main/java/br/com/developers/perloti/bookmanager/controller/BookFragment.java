package br.com.developers.perloti.bookmanager.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.developers.perloti.bookmanager.R;
import br.com.developers.perloti.bookmanager.controller.adapter.BookAdapter;
import br.com.developers.perloti.bookmanager.model.Book;
import br.com.developers.perloti.bookmanager.model.BookRepository;
import br.com.developers.perloti.bookmanager.util.ApplicationUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by perloti on 16/06/18.
 */

public class BookFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.textview_empty)
    TextView textViewEmpty;
    @BindView(R.id.view_empty)
    View viewEmpty;
    @BindView(R.id.view_content)
    View viewContent;

    BookAdapter adapter;
    private static final String TYPE_LIST = "TYPE_LIST";
    private String typeList;

    public BookFragment setArguments(String cat) {
        BookFragment fragment = new BookFragment();
        Bundle arguments = new Bundle();
        arguments.putString(TYPE_LIST, cat);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) typeList = getArguments().getString(TYPE_LIST);
        if (getActivity() != null) getActivity().setTitle(getResTitle());
        setupComponents();
        onUpdate();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public int getResTitle() {
        return ApplicationUtil.context.getResources().getIdentifier(
                "title_" + typeList, "string",
                ApplicationUtil.context.getPackageName());
    }

    public int getLabelViewEmpty() {
        return ApplicationUtil.context.getResources().getIdentifier(
                "empty_view_" + typeList, "string",
                ApplicationUtil.context.getPackageName());
    }

    private void setupComponents() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        registerForContextMenu(recyclerView);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primary_dark, R.color.primary, R.color.primary_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefreshing(true);
                onUpdate();
            }
        });
    }

    private void onUpdate() {
        List<Book> booksAll = BookRepository.getBookByStatus(typeList);
        if (booksAll != null && !booksAll.isEmpty()) {
            adapter = new BookAdapter(getActivity(), booksAll);
            recyclerView.setAdapter(adapter);
            viewContent.setVisibility(View.VISIBLE);
            viewEmpty.setVisibility(View.GONE);
        } else {
            viewEmpty.setVisibility(View.VISIBLE);
            viewContent.setVisibility(View.GONE);
            textViewEmpty.setText(getLabelViewEmpty());
        }
        setRefreshing(false);
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
