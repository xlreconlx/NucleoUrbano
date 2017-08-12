package com.premiosnucleourbano.nucleourbano.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.premiosnucleourbano.nucleourbano.ArtistActivity;
import com.premiosnucleourbano.nucleourbano.R;
import com.premiosnucleourbano.nucleourbano.models.Categorias;
import com.premiosnucleourbano.nucleourbano.viewholder.CategoriaViewHolder;

/**
 * Created by ander on 3/08/2017.
 */

public abstract class CategoriaListFragment extends Fragment {
    private static final String TAG = "CategoriaListFragment";
    // [START referencia a la bd]
    private DatabaseReference mDatabase;
    // [END referencia a la bd]
    private FirebaseRecyclerAdapter<Categorias, CategoriaViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public CategoriaListFragment(){}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.all_categoria, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        //Buscamos en la vista el recyclerView
        mRecycler = (RecyclerView) rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Categorias, CategoriaViewHolder>(Categorias.class, R.layout.item_categoria,
                CategoriaViewHolder.class, postsQuery) {

            @Override
            protected void populateViewHolder(final CategoriaViewHolder viewHolder, final Categorias model, final int position) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), ArtistActivity.class);
                        intent.putExtra(ArtistActivity.EXTRA_CATE_KEY, postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.bindToCategoria(model);

            }
        };
        mRecycler.setAdapter(mAdapter);
        }
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public abstract Query getQuery(DatabaseReference databaseReference);

}
