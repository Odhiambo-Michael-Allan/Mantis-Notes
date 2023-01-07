package com.mantis.MantisNotesIterationOne.UI;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.navigation.NavigationView;
import com.mantis.MantisNotesIterationOne.Adapters.NotesAdapter;
import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;
import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.databinding.FragmentTrashBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrashFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentTrashBinding binding;
    private NotesAdapter adapter;
    private NotesViewModel notesViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrashFragment newInstance(String param1, String param2) {
        TrashFragment fragment = new TrashFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        binding = FragmentTrashBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        setupToolbar();
        setupRecyclerView();
        setupNotesViewModel();
    }

    private void setupToolbar() {
        NavController navController = NavHostFragment.findNavController( this );
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder( R.id.nav_home, R.id.nav_frequently_used, R.id.nav_trash )
                .setOpenableLayout( drawerLayout )
                .build();
        NavigationUI.setupWithNavController( binding.trashFragmentContent
                .trashFragmentAppBarLayout.toolbar, navController, appBarConfiguration );
        NavigationUI.setupWithNavController( navigationView, navController );
    }

    private void setupRecyclerView() {
        binding.trashFragmentContent.trashFragmentNotesRecyclerView.recyclerview
                .setLayoutManager( new LinearLayoutManager( getContext() ) );
        binding.trashFragmentContent.textEmpty.setText( getString( R.string.no_notes ) );
        adapter = new NotesAdapter();
        adapter.setEmptyView( binding.trashFragmentContent.layoutEmpty );
        adapter.setData( new ArrayList<>() );
        adapter.addListener( new NotesAdapter.NoteAdapterListener() {
            @Override
            public void onViewHolderClicked( View view, int viewHolderPosition ) {
            }
        } );
        binding.trashFragmentContent.trashFragmentNotesRecyclerView
                .recyclerview.setAdapter( adapter );
        binding.trashFragmentContent.trashFragmentNotesRecyclerView.recyclerview
                .addItemDecoration( createDivider() );
    }

    private MaterialDividerItemDecoration createDivider() {
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration( getContext(), LinearLayoutManager.VERTICAL );
        divider.setDividerInsetStart( 40 );
        divider.setDividerInsetEnd( 40 );
        return divider;
    }

    private void setupNotesViewModel() {
        notesViewModel = new ViewModelProvider( requireActivity() )
                .get( NotesViewModel.class );
        notesViewModel.getDeletedNotes().observe( getViewLifecycleOwner(),
                new Observer<ArrayList<Note>>() {
                    @Override
                    public void onChanged( ArrayList<Note> notes ) {
                        adapter.setData( notes );
                    }
                } );
        adapter.setNotesViewModel( notesViewModel );
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.navView.getMenu().findItem( R.id.nav_trash ).setChecked( true );
        binding.trashFragmentContent.trashFragmentAppBarLayout.mainTitle
                .setText( getString( R.string.trash ) );
    }
}