package br.com.developers.perloti.bookmanager.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.com.developers.perloti.bookmanager.R;
import br.com.developers.perloti.bookmanager.controller.DetailBookActivity;
import br.com.developers.perloti.bookmanager.model.Book;
import br.com.developers.perloti.bookmanager.model.BookRepository;
import br.com.developers.perloti.bookmanager.util.BMUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by perloti on 18/06/18.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.Holder> {

    private List<Book> books = new ArrayList<>();
    private Context context;
    private Book book;

    public BookAdapter(Context context, List<Book> items) {
        this.context = context;
        this.books = items;
    }

    @NonNull
    @Override
    public BookAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_book_item, parent, false);
        return new BookAdapter.Holder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.Holder holder, int position) {
        book = books.get(position);
        holder.render(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_title)
        TextView textViewTitle;
        @BindView(R.id.text_view_author)
        TextView textViewAuthor;
        @BindView(R.id.image)
        ImageView imageView;
        @BindView(R.id.button_view_more)
        Button buttonViewMore;
        @BindView(R.id.imageview_delete)
        ImageView imageViewDelete;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void render(final Book book) {
            final String idBook = book.getId();
            String title = book.getTitle();
            textViewTitle.setText(title);
            String authors = book.getAuthor();
            textViewAuthor.setText(authors);
            String thubmail = book.getThubmail();

            Glide.with(context).load(thubmail).into(imageView);

            buttonViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailBookActivity.class);
                    intent.putExtra(BMUtil.ID_BOOK, idBook);
                    context.startActivity(intent);
                }
            });

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BookRepository.removeById(idBook);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
