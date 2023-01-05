package com.mantis.MantisNotesIterationOne.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;
import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.databinding.FragmentHomeBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentHomeBinding binding;
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if ( getArguments() != null ) {
            mParam1 = getArguments().getString( ARG_PARAM1 );
            mParam2 = getArguments().getString( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        setupRecyclerView();
        setupNotesViewModel();
        setupToolbar();
        setupFloatingActionButton();
    }

    private void setupRecyclerView() {
        binding.notesRecyclerView.recyclerview.setLayoutManager(
                new LinearLayoutManager( getContext() ) );
        notesAdapter = new NotesAdapter();
        binding.notesRecyclerView.recyclerview.setAdapter( notesAdapter );
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration( getContext(), LinearLayoutManager.VERTICAL );
        divider.setDividerInsetStart( 40 );
        divider.setDividerInsetEnd( 40 );
        binding.notesRecyclerView.recyclerview.addItemDecoration( divider );
    }

    private void setupNotesViewModel() {
        notesViewModel = new ViewModelProvider( requireActivity() ).get( NotesViewModel.class );
        notesViewModel.getNotes().observe( getViewLifecycleOwner(), new Observer<ArrayList<Note>>() {
            @Override
            public void onChanged( ArrayList<Note> notes ) {
                notesAdapter.setData( notes );
            }
        } );
    }

    private void setupToolbar() {
        NavController controller = NavHostFragment.findNavController( this );
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder( controller.getGraph() ).build();
        NavigationUI.setupWithNavController( binding.toolbar, controller, appBarConfiguration );

    }

    private void setupFloatingActionButton() {
        binding.fab.setOnClickListener( Navigation.createNavigateOnClickListener(
                R.id.action_nav_home_to_nav_add_note, null ) );
    }
}