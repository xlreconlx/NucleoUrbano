package com.premiosnucleourbano.nucleourbano.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.premiosnucleourbano.nucleourbano.R;
import com.premiosnucleourbano.nucleourbano.models.Artista;

/**
 * Created by ander on 6/08/2017.
 */

public class ArtistaViewHolder extends RecyclerView.ViewHolder {
    public TextView artistView;
    public ImageView artistImage;

    public ArtistaViewHolder(View itemView){
        super(itemView);

        artistView = (TextView) itemView.findViewById(R.id.artist_text);
        artistImage = (ImageView)itemView.findViewById(R.id.img_favorite);
    }

    public void bindToArtista(Artista artist, View.OnClickListener favoriteClickListener){
        this.artistView.setText(artist.nombre);

        artistImage.setOnClickListener(favoriteClickListener);
    }

}
