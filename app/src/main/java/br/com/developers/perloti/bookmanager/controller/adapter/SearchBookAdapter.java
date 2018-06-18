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
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import br.com.developers.perloti.bookmanager.R;
import br.com.developers.perloti.bookmanager.controller.DetailBookActivity;
import br.com.developers.perloti.bookmanager.util.BMUtil;
import br.com.developers.perloti.bookmanager.util.JsonUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by perloti on 18/06/18.
 */

public class SearchBookAdapter extends RecyclerView.Adapter<SearchBookAdapter.Holder> {

    private LinkedTreeMap linkedTreeMap = new LinkedTreeMap();
    private List<LinkedTreeMap> linkedTreeMaps = new ArrayList<>();
    private Context context;

    public SearchBookAdapter(Context context, List<LinkedTreeMap> items) {
        this.context = context;
        this.linkedTreeMaps = items;
    }

    @NonNull
    @Override
    public SearchBookAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new Holder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchBookAdapter.Holder holder, int position) {
        linkedTreeMap = linkedTreeMaps.get(position);
        holder.render(linkedTreeMap);
    }

    @Override
    public int getItemCount() {
        return linkedTreeMaps.size();
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


        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void render(LinkedTreeMap linkedTreeMap) {
            final String idBook = JsonUtil.getString(linkedTreeMap, "id");
            LinkedTreeMap volumeInfo = JsonUtil.getObject(linkedTreeMap, "volumeInfo");
            String title = JsonUtil.getString(volumeInfo, "title");
            textViewTitle.setText(title);
            List<String> authors = JsonUtil.getStringList(volumeInfo, "authors");
            StringBuilder builder = new StringBuilder();
            if (authors != null && !authors.isEmpty())
                for (int i = 0; i < authors.size(); i++) {
                    if (i == authors.size() - 1) {
                        builder.append(authors.get(i).toUpperCase());
                    } else {
                        builder.append(authors.get(i).toUpperCase() + ", ");
                    }
                }
            textViewAuthor.setText(builder);
            LinkedTreeMap imageLinks = JsonUtil.getObject(volumeInfo, "imageLinks");
            String thubmail = JsonUtil.getString(imageLinks, "thumbnail");

            Glide.with(context).load(thubmail).into(imageView);
            buttonViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailBookActivity.class);
                    intent.putExtra(BMUtil.ID_BOOK, idBook);
                    context.startActivity(intent);
                }
            });

        }
    }
}
