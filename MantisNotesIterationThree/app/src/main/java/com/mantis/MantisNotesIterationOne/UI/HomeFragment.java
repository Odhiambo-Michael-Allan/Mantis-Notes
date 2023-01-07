package com.mantis.MantisNotesIterationOne.UI;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.text.Html;
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
import com.google.android.material.navigation.NavigationView;
import com.mantis.MantisNotesIterationOne.Adapters.NotesAdapter;
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
        binding.homeFragmentContent.notesRecyclerView.recyclerview.setLayoutManager(
                new LinearLayoutManager( getContext() ) );
        binding.homeFragmentContent.textEmpty.setText( Html.fromHtml( getString( R.string.text_empty_message ) ) );
        notesAdapter = new NotesAdapter();
        notesAdapter.setEmptyView( binding.homeFragmentContent.layoutEmpty );
        notesAdapter.setData( new ArrayList<>() );
        notesAdapter.addListener(new NotesAdapter.NoteAdapterListener() {
            @Override
            public void onViewHolderClicked( View view, int viewHolderPosition ) {
                HomeFragmentDirections.ActionNavHomeToNavAddNote action =
                        HomeFragmentDirections.actionNavHomeToNavAddNote(
                                viewHolderPosition );
                Navigation.findNavController( view ).navigate( action );
            }
        } );
        binding.homeFragmentContent.notesRecyclerView.recyclerview.setAdapter( notesAdapter );
        binding.homeFragmentContent.notesRecyclerView.recyclerview.addItemDecoration( createDivider() );
    }

    private MaterialDividerItemDecoration createDivider() {
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration( getContext(), LinearLayoutManager.VERTICAL );
        divider.setDividerInsetStart( 40 );
        divider.setDividerInsetEnd( 40 );
        return divider;
    }

    private void setupNotesViewModel() {
        notesViewModel = new ViewModelProvider( requireActivity() ).get( NotesViewModel.class );
        notesViewModel.getNotes().observe( getViewLifecycleOwner(), new Observer<ArrayList<Note>>() {
            @Override
            public void onChanged( ArrayList<Note> notes ) {
                notesAdapter.setData( notes );
            }
        } );
        notesAdapter.setNotesViewModel( notesViewModel );
    }

    private void setupToolbar() {
        NavController controller = NavHostFragment.findNavController( this );
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder( R.id.nav_home, R.id.nav_frequently_used, R.id.nav_trash )
                .setOpenableLayout( drawerLayout )
                .build();
        NavigationUI.setupWithNavController( binding.homeFragmentContent.appBarLayout.toolbar,
                controller, appBarConfiguration );
        NavigationUI.setupWithNavController( navigationView, controller );
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.navView.getMenu().findItem( R.id.nav_home ).setChecked( true );
    }


    private void setupFloatingActionButton() {
        binding.homeFragmentContent.fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                HomeFragmentDirections.ActionNavHomeToNavAddNote action =
                        HomeFragmentDirections.actionNavHomeToNavAddNote(
                                -1 );
                Navigation.findNavController( v ).navigate( action );
            }
        } );
    }
}