package com.premiosnucleourbano.nucleourbano.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.premiosnucleourbano.nucleourbano.ArtistActivity;
import com.premiosnucleourbano.nucleourbano.R;
import com.premiosnucleourbano.nucleourbano.models.Artista;
import com.premiosnucleourbano.nucleourbano.models.Votos;
import com.premiosnucleourbano.nucleourbano.viewholder.ArtistaViewHolder;


/**
 * Created by ander on 6/08/2017.
 */

public abstract class ArtistaListFragment extends Fragment {
    private static final String TAG = "ArtistaListFragment";
    // [START referencia a la bd]
    private DatabaseReference mDatabase;
    // [END referencia a la bd]
    private FirebaseRecyclerAdapter<Artista, ArtistaViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private String mCatetKey;
    public static final String EXTRA_CATE_KEY = "cate_key";
    public static final int NUM_COLUMS = 2;

    public ArtistaListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.all_artist, container, false);
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mCatetKey = getActivity().getIntent().getExtras().getString(ArtistActivity.EXTRA_CATE_KEY);
        if (mCatetKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_CATE_KEY");
        }
        //Buscamos en la vista el recyclerView
        mRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_artist);
        mRecycler.setHasFixedSize(true);

        return rootView;
}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set up Layout Manager, reverse layout
        //Modificacion
        // mManager = new LinearLayoutManager(getActivity());
         mManager = new GridLayoutManager(getActivity(), NUM_COLUMS);
        //mManager.setReverseLayout(true);
        //mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        DatabaseReference artistRef = mDatabase.child("artistas").child(mCatetKey);
        Query postsQuery = artistRef;
        mAdapter = new FirebaseRecyclerAdapter<Artista, ArtistaViewHolder>(Artista.class, R.layout.item_artist,
                ArtistaViewHolder.class, postsQuery) {

            @Override
            protected void populateViewHolder(final ArtistaViewHolder viewHolder, final Artista model, final int position) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();


                viewHolder.bindToArtista(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        DatabaseReference globalVototRef = mDatabase.child("votos").child(mCatetKey).child(postRef.getKey());
                        DatabaseReference votoUserRef = mDatabase.child("votouser").child(getUid()).child(mCatetKey);

                       // onFavoriteClicked(globalVototRef);
                        getUserVoto(votoUserRef,globalVototRef);
                    }
                });

            }
        };
        mRecycler.setAdapter(mAdapter);
     }

     public boolean getUserVoto(DatabaseReference votoRef,final DatabaseReference postRef){
         final DatabaseReference  votoRefUser = votoRef;
        votoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    votoRefUser.setValue(true);
                    isUserVoto(postRef);
                }else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Puedes Votar Mañana Nuevamente", Snackbar.LENGTH_LONG).show();
                   // Toast.makeText(getContext(), "Ya has Votado en esta Categoria," +
                    //        "Intenta Mañana Nuevamente", Toast.LENGTH_SHORT).show();
                }

             }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadGetUserVoto:onCancelled", databaseError.toException());
                // ...
            }
        });
         return true;
     }

     public void isUserVoto(DatabaseReference postRef){
         onFavoriteClicked(postRef);
     }

     public void onFavoriteClicked(DatabaseReference postRef){
        final DatabaseReference  votoRef = postRef;
         postRef.runTransaction(new Transaction.Handler() {
             @Override
             public Transaction.Result doTransaction(MutableData mutableData) {
                 Votos v = mutableData.getValue(Votos.class);
               //  Toast.makeText(getContext(), "Votando 1", Toast.LENGTH_SHORT).show();
                 if (v == null) {
                      Votos vote = new Votos(1L);
                      mutableData.setValue(vote);
                      return Transaction.success(mutableData);
                 }
                 System.out.println("Votos:"+v.votos);
                 v.votos = v.votos + 1L;
                 mutableData.setValue(v);
                 return Transaction.success(mutableData);
         }

             @Override
             public void onComplete(DatabaseError databaseError, boolean b,
                                    DataSnapshot dataSnapshot) {
                 // Transaction completed
                 Snackbar.make(getActivity().findViewById(android.R.id.content),
                         "Gracias por Tu Voto", Snackbar.LENGTH_LONG).show();
                //Toast.makeText(getContext(), "Gracias por Tu Voto", Toast.LENGTH_SHORT).show();
                 Log.d(TAG, "postTransaction:onComplete:" + databaseError);
             }

         });
     }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
