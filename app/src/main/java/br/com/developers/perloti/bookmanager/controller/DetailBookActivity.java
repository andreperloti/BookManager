package br.com.developers.perloti.bookmanager.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.developers.perloti.bookmanager.R;
import br.com.developers.perloti.bookmanager.model.Book;
import br.com.developers.perloti.bookmanager.model.BookRepository;
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

public class DetailBookActivity extends AppCompatActivity {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.text_view_title)
    TextView textViewTitle;
    @BindView(R.id.text_view_author)
    TextView textViewAuthor;
    @BindView(R.id.text_view_description)
    TextView textViewdescription;
    @BindView(R.id.progressBar)
    ProgressBar progressBarImage;
    @BindView(R.id.view_content)
    View viewContent;
    @BindView(R.id.view_error)
    View viewError;
    @BindView(R.id.button_try_again)
    Button buttonErrorTryAgain;
    @BindView(R.id.button_add_wishlist)
    Button buttonAddWishlist;
    @BindView(R.id.button_add_read)
    Button buttonAddRead;

    private Book bookCurrent;
    private String idBook;
    private LinkedTreeMap linkedTreeMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getIdBook();
        setupComponents();
        downloadBookById();
    }

    private void downloadBookById() {
        if (BMUtil.statusNetwork(this)) {
            setRefreshing(true);
            ClienteAPI.MyRetrofit.getInstance().getBookById(idBook)
                    .enqueue(new Callback<LinkedTreeMap>() {
                        @Override
                        public void onResponse(Call<LinkedTreeMap> call, Response<LinkedTreeMap> response) {
                            int statusCode = response.code();
                            Log.e(BMUtil.TAG_REQUEST, "Detail book StatusCode: " + statusCode);
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
                            Log.e(BMUtil.TAG_REQUEST, "Error request detail book ");
                            setRefreshing(false);
                            viewError.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    private void onUpdate() {
        if (linkedTreeMap == null || linkedTreeMap.isEmpty()) {
            viewContent.setVisibility(View.GONE);
        } else {
            viewContent.setVisibility(View.VISIBLE);
            LinkedTreeMap volumeInfo = JsonUtil.getObject(linkedTreeMap, "volumeInfo");
            String title = JsonUtil.getString(volumeInfo, "title");
            textViewTitle.setText(title);
            String description = JsonUtil.getString(volumeInfo, "description");
            if (description != null && !description.equals("")) {
                textViewdescription.setText(Html.fromHtml(description));
                textViewdescription.setVisibility(View.VISIBLE);
            } else {
                textViewdescription.setVisibility(View.GONE);
            }

            List<String> authorList = JsonUtil.getStringList(volumeInfo, "authors");
            StringBuilder authors = new StringBuilder();
            if (authorList != null && !authorList.isEmpty())
                for (int i = 0; i < authorList.size(); i++) {
                    if (i == authorList.size() - 1) {
                        authors.append(authorList.get(i).toUpperCase());
                    } else {
                        authors.append(authorList.get(i).toUpperCase() + ", ");
                    }
                }
            textViewAuthor.setText(authors);

            LinkedTreeMap image = JsonUtil.getObject(volumeInfo, "imageLinks");
            String thubmail = JsonUtil.getString(image, "thumbnail");
            String large = JsonUtil.getString(image, "large");

            Picasso.with(this).load(large).error(R.drawable.ic_book)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            if (progressBarImage != null)
                                progressBarImage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            imageView.setContentDescription("Error");
                            if (progressBarImage != null)
                                progressBarImage.setVisibility(View.GONE);
                        }
                    });

            bookCurrent = new Book.Builder(idBook, title)
                    .author(authors.toString())
                    .thubmail(thubmail)
                    .thubmailLarge(large)
                    .build();
        }

    }

    private void setRefreshing(final boolean refreshing) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(refreshing);
            }
        });
    }

    private void setupComponents() {
        viewContent.setVisibility(View.GONE);
        progressBarImage.setVisibility(View.VISIBLE);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primary_dark, R.color.primary, R.color.primary_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadBookById();
            }
        });
        buttonErrorTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadBookById();
            }
        });


        buttonAddRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book bookById = BookRepository.getBookById(idBook);
                if (bookById != null && bookById.getStatus().equals("read")) {
                    buttonAddRead.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_add, 0, 0, 0);
                    BookRepository.removeById(idBook);
                } else {
                    bookCurrent.setStatus("read");
                    BookRepository.save(bookCurrent);
                    buttonAddRead.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_remove, 0, 0, 0);
                    buttonAddWishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_add, 0, 0, 0);
                }
            }
        });
        buttonAddWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book bookById = BookRepository.getBookById(idBook);
                if (bookById != null && bookById.getStatus().equals("wishlist")) {
                    buttonAddWishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_add, 0, 0, 0);
                    BookRepository.removeById(idBook);
                } else {
                    bookCurrent.setStatus("wishlist");
                    BookRepository.save(bookCurrent);
                    buttonAddRead.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_add, 0, 0, 0);
                    buttonAddWishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_remove, 0, 0, 0);
                }
            }
        });

        Book bookById = BookRepository.getBookById(idBook);
        if (bookById != null) {
            if (bookById.getStatus().equals("read")) {
                buttonAddRead.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_remove, 0, 0, 0);
                buttonAddWishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_add, 0, 0, 0);
            } else if (bookById.getStatus().equals("wishlist")) {
                buttonAddRead.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_add, 0, 0, 0);
                buttonAddWishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_remove, 0, 0, 0);
            }
        } else {
            buttonAddRead.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_add, 0, 0, 0);
            buttonAddWishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_add, 0, 0, 0);
        }
    }

    private void getIdBook() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idBook = extras.getString(BMUtil.ID_BOOK);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
