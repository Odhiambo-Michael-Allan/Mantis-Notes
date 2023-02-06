package com.mantis.MantisNotesIterationOne.Utils;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.mantis.MantisNotesIterationOne.Adapters.GridLayoutNoteAdapter;
import com.mantis.MantisNotesIterationOne.Adapters.ListAdapter;
import com.mantis.MantisNotesIterationOne.Adapters.NotesAdapter;
import com.mantis.MantisNotesIterationOne.Adapters.SimpleLayoutAdapter;

public class RecyclerViewConfigurator {

    public static void configureSimpleListRecyclerView( RecyclerView recyclerView,
                                                        Context context ) {
        recyclerView.setLayoutManager( new LinearLayoutManager( context ) );
        NotesAdapter adapter = new SimpleLayoutAdapter();
        recyclerView.setAdapter( adapter );
        recyclerView.addItemDecoration( createDivider( context ) );
    }

    public static void configureGridLayoutRecyclerView( RecyclerView recyclerView,
                                                        Context context ) {
        removeDecorationsFrom( recyclerView );
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager( 2,
                StaggeredGridLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( layoutManager );
        NotesAdapter adapter = new GridLayoutNoteAdapter();
        recyclerView.setAdapter( adapter );
    }

    public static void configureListRecyclerView( RecyclerView recyclerView,
                                                  Context context ) {
        removeDecorationsFrom( recyclerView );
        recyclerView.setLayoutManager( new LinearLayoutManager( context ) );
        NotesAdapter adapter = new ListAdapter();
        recyclerView.setAdapter( adapter );

    }

    private static void removeDecorationsFrom( RecyclerView recyclerView ) {
        while ( recyclerView.getItemDecorationCount() > 0 ) {
            recyclerView.removeItemDecorationAt( 0 );
        }
    }

    private static MaterialDividerItemDecoration createDivider( Context context ) {
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration( context,
                LinearLayoutManager.VERTICAL );
        divider.setDividerInsetStart( 40 );
        divider.setDividerInsetEnd( 40 );
        return divider;
    }
}
