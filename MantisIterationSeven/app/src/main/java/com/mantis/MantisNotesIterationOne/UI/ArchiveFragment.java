package com.mantis.MantisNotesIterationOne.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;
import com.mantis.MantisNotesIterationOne.Adapters.NotesAdapter;
import com.mantis.MantisNotesIterationOne.Dialogs.NoteActionDialog;
import com.mantis.MantisNotesIterationOne.Logger;
import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;
import com.mantis.MantisNotesIterationOne.Models.NotesViewModelFactory;
import com.mantis.MantisNotesIterationOne.data.source.DefaultNoteRepository;
import com.mantis.MantisNotesIterationOne.data.source.local.Note;
import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.Utils.MenuConfigurator;
import com.mantis.MantisNotesIterationOne.Utils.RecyclerViewConfigurator;
import com.mantis.MantisNotesIterationOne.databinding.FragmentArchiveBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArchiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchiveFragment extends Fragment {

    private static final boolean SHOW_ARCHIVE_OPTION = false;

    private FragmentArchiveBinding binding;
    private NotesAdapter notesAdapter;
    private NotesViewModel notesViewModel;

    public ArchiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static ArchiveFragment newInstance( String param1, String param2 ) {
        ArchiveFragment fragment = new ArchiveFragment();
        Bundle args = new Bundle();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
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
        setupNotesViewModel();
        setupToolbar();
        configureRecyclerView( NotesViewModel.LAYOUT_STATE_SIMPLE_LIST );
    }

    private void setupNotesViewModel() {
        notesViewModel = new NotesViewModelFactory( DefaultNoteRepository.getRepository(
                this.getActivity().getApplication() )).create( NotesViewModel.class );
        //observeLayoutState();
        observeArchivedNotes();
    }

//    private void observeLayoutState() {
//        notesViewModel.getLayoutState().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged( Integer integer ) {
//                Logger.log( "SETTING VIEW STATE" );
//                configureRecyclerView( integer );
//                MenuConfigurator.checkSelectedLayoutType( integer, binding.
//                        archiveFragmentContent.archiveFragmentAppBarLayout.toolbar.getMenu() );
//            }
//        } );
//    }

    private void configureRecyclerView( int layoutType ) {
        RecyclerView recyclerView = binding.archiveFragmentContent
                .archiveFragmentNotesRecyclerView.recyclerview;
        NotesAdapter oldAdapter = ( NotesAdapter ) recyclerView.getAdapter();
        createAppropriateLayoutType( layoutType, recyclerView );
        configureRecyclerViewComponents( recyclerView );
        reloadAdapterData( oldAdapter );
        configureListenerOnNotesAdapter();
    }

    private void createAppropriateLayoutType( int layoutType, RecyclerView recyclerView ) {
        if ( layoutType == NotesViewModel.LAYOUT_STATE_SIMPLE_LIST)
            createSimpleListLayout( recyclerView );
        else if ( layoutType == NotesViewModel.LAYOUT_STATE_GRID)
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
        binding.archiveFragmentContent.textEmpty.setText( R.string.no_notes );
        notesAdapter = ( NotesAdapter ) recyclerView.getAdapter();
        notesAdapter.setEmptyView( binding.archiveFragmentContent.layoutEmpty );
        notesAdapter.setNotesViewModel( notesViewModel );
    }

    private void reloadAdapterData( NotesAdapter oldAdapter ) {
        Menu menu = binding.archiveFragmentContent.archiveFragmentAppBarLayout.toolbar.getMenu();
        if ( oldAdapter == null )
            notesAdapter.setData( new ArrayList<>(), menu );
        else
            notesAdapter.setData( oldAdapter.getData(), menu );
    }

    private void configureListenerOnNotesAdapter() {
        notesAdapter.addListener(new NotesAdapter.NoteAdapterListener() {
            @Override
            public void onViewHolderClicked( View view, int viewHolderPosition ) {
                ArchiveFragmentDirections.ActionNavArchiveToNavAddNote action =
                        ArchiveFragmentDirections
                                .actionNavArchiveToNavAddNote(
                                        notesAdapter.getData().get( viewHolderPosition ).getId(),
                                        AddNoteFragment.ARCHIVE_FRAGMENT );
                Navigation.findNavController( view ).navigate( action );
            }

            @Override
            public void onViewHolderLongClicked( int viewHolderPosition ) {
                Note noteSelected = notesAdapter.getData().get( viewHolderPosition );  // !!!!!!!!!
                NoteActionDialog actionDialog = NoteActionDialog.newInstance( SHOW_ARCHIVE_OPTION );
                actionDialog.addListener( new NoteActionDialog.NoteActionDialogListener() {
                    @Override
                    public void deleteSelected() {
                        notesViewModel.deleteArchiveFragmentNoteReference( noteSelected.getId() );
                    }

                    @Override
                    public void onArchiveSelected() {
                        // Do nothing..
                    }

                    @Override
                    public void onUnarchiveSelected() {
                        notesViewModel.unarchiveNote( noteSelected.getId() );
                    }
                } );
                actionDialog.show( ((AppCompatActivity) getContext()).getSupportFragmentManager(),
                        "note-actions" );
            }
        } );
    }

    private void observeArchivedNotes() {
        notesViewModel.getArchiveFragmentNotesList().observe(getViewLifecycleOwner(),
                new Observer<List<Note>>() {
                    @Override
                    public void onChanged( List<Note> notes ) {
                        Logger.log( "SETTING ARCHIVED NOTES LIST" );
                        notesAdapter.setData( notes, binding.archiveFragmentContent.archiveFragmentAppBarLayout.toolbar.getMenu() );
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