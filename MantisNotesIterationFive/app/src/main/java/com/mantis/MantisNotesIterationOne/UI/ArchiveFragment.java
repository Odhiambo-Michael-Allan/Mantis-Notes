package com.mantis.MantisNotesIterationOne.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mantis.MantisNotesIterationOne.Adapters.NotesAdapter;
import com.mantis.MantisNotesIterationOne.Dialogs.NoteActionDialog;
import com.mantis.MantisNotesIterationOne.Logger;
import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;
import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.Utils.MenuConfigurator;
import com.mantis.MantisNotesIterationOne.Utils.RecyclerViewConfigurator;
import com.mantis.MantisNotesIterationOne.databinding.FragmentArchiveBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArchiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchiveFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentArchiveBinding binding;
    private NotesAdapter adapter;
    private NotesViewModel notesViewModel;

    public ArchiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArchiveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArchiveFragment newInstance( String param1, String param2 ) {
        ArchiveFragment fragment = new ArchiveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( getArguments() != null ) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        binding = FragmentArchiveBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        Logger.log( "ARCHIVE FRAGMENT VIEW CREATED" );
        setupNotesViewModel();
        setupToolbar();
    }

    private void setupNotesViewModel() {
        notesViewModel = new ViewModelProvider( requireActivity() ).get( NotesViewModel.class );
        observeLayoutState();
        observeArchivedNotes();
    }

    private void observeLayoutState() {
        notesViewModel.getLayoutState().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged( Integer integer ) {
                Logger.log( "SETTING VIEW STATE" );
                configureRecyclerView( integer );
                MenuConfigurator.checkSelectedLayoutType( integer, binding.
                        archiveFragmentContent.archiveFragmentAppBarLayout.toolbar.getMenu() );
            }
        } );
    }

    private void configureRecyclerView( int layoutType ) {
        RecyclerView recyclerView = binding.archiveFragmentContent
                .archiveFragmentNotesRecyclerView.recyclerview;
        NotesAdapter oldAdapter = ( NotesAdapter ) recyclerView.getAdapter();
        Logger.log( String.format( "Old Adapter: %s", oldAdapter ) );
        createAppropriateLayoutType( layoutType, recyclerView );
        configureRecyclerViewComponents( recyclerView );
        reloadAdapterData( oldAdapter );
        configureListenerOnNotesAdapter();
    }

    private void createAppropriateLayoutType( int layoutType, RecyclerView recyclerView ) {
        if ( layoutType == NotesViewModel.VIEW_STATE_SIMPLE_LIST )
            createSimpleListLayout( recyclerView );
        else if ( layoutType == NotesViewModel.VIEW_STATE_GRID )
            createGridLayout( recyclerView );
        else
            createListLayout( recyclerView );
    }

    private void createSimpleListLayout( RecyclerView recyclerView ) {
        RecyclerViewConfigurator.configureSimpleListRecyclerView( recyclerView, getContext() );
    }

    private void createGridLayout( RecyclerView recyclerView ) {
        RecyclerViewConfigurator.configureGridLayoutRecyclerView( recyclerView, getContext() );
    }

    private void createListLayout( RecyclerView recyclerView ) {
        RecyclerViewConfigurator.configureListRecyclerView( recyclerView, getContext() );
    }

    private void configureRecyclerViewComponents( RecyclerView recyclerView ) {
        binding.archiveFragmentContent.textEmpty.setText( Html.fromHtml( getString( R.string.text_empty_message ) ) );
        adapter = ( NotesAdapter ) recyclerView.getAdapter();
        adapter.setEmptyView( binding.archiveFragmentContent.layoutEmpty );
        adapter.setNotesViewModel( notesViewModel );
    }

    private void reloadAdapterData( NotesAdapter oldAdapter ) {
        Menu menu = binding.archiveFragmentContent.archiveFragmentAppBarLayout.toolbar.getMenu();
        if ( oldAdapter == null )
            adapter.setData( new ArrayList<>(), menu );
        else
            adapter.setData( oldAdapter.getData(), menu );
    }

    private void configureListenerOnNotesAdapter() {
        adapter.addListener( new NotesAdapter.NoteAdapterListener() {
            @Override
            public void onViewHolderClicked( View view, int viewHolderPosition ) {
                ArchiveFragmentDirections.ActionNavArchiveToNavAddNote action =
                        ArchiveFragmentDirections
                                .actionNavArchiveToNavAddNote( viewHolderPosition,
                                        AddNoteFragment.ARCHIVE_FRAGMENT );
                Navigation.findNavController( view ).navigate( action );
            }

            @Override
            public void onViewHolderLongClicked( int viewHolderPosition ) {
                NoteActionDialog actionDialog = NoteActionDialog.newInstance( viewHolderPosition, false );
                actionDialog.addListener( new NoteActionDialog.NoteActionDialogListener() {
                    @Override
                    public void deleteSelected() {
                        notesViewModel.deleteNoteFromArchiveList( viewHolderPosition );
                    }

                    @Override
                    public void onArchiveSelected() {
                        // Do nothing..
                    }

                    @Override
                    public void onUnarchiveSelected() {
                        Logger.log( "UNARCHIVE NOTE AT POSITION: " + viewHolderPosition );
                        Toast.makeText( getContext(), "Not yet implemented", Toast.LENGTH_SHORT ).show();
                    }
                } );
                actionDialog.show( ((AppCompatActivity) getContext()).getSupportFragmentManager(), "note-actions" );
            }
        } );
    }

    private void observeArchivedNotes() {
        notesViewModel.getArchivedNotes().observe(getViewLifecycleOwner(),
                new Observer<ArrayList<Note>>() {
                    @Override
                    public void onChanged( ArrayList<Note> notes ) {
                        Logger.log( "SETTING ARCHIVED NOTES LIST" );
                        adapter.setData( notes, binding.archiveFragmentContent.archiveFragmentAppBarLayout.toolbar.getMenu() );
                    }
                } );
    }

    private void setupToolbar() {
        NavController controller = NavHostFragment.findNavController( this );
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder( R.id.nav_home, R.id.nav_frequently_used, R.id.nav_trash, R.id.nav_archive )
                .setOpenableLayout( drawerLayout )
                .build();
        NavigationUI.setupWithNavController( binding.archiveFragmentContent
                .archiveFragmentAppBarLayout.toolbar, controller, appBarConfiguration );
        NavigationUI.setupWithNavController( navigationView, controller );
        binding.archiveFragmentContent.archiveFragmentAppBarLayout
                .toolbar.inflateMenu( R.menu.minimal_options_menu );
        MenuConfigurator.configureMenu( binding.archiveFragmentContent
                .archiveFragmentAppBarLayout.toolbar.getMenu() );
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.navView.getMenu().findItem( R.id.nav_archive ).setChecked( true );
        binding.archiveFragmentContent.archiveFragmentAppBarLayout.mainTitle
                .setText( getString( R.string.archive ) );
    }
}