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
import com.mantis.MantisNotesIterationOne.databinding.FragmentFrequentlyUsedBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FrequentlyUsedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrequentlyUsedFragment extends Fragment {

    private static final boolean SHOW_ARCHIVE_OPTION = true;
    private FragmentFrequentlyUsedBinding binding;
    private NotesAdapter notesAdapter;
    private NotesViewModel notesViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FrequentlyUsedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static FrequentlyUsedFragment newInstance( String param1, String param2 ) {
        FrequentlyUsedFragment fragment = new FrequentlyUsedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        binding = FragmentFrequentlyUsedBinding.inflate( inflater, container, false );
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
                this.getActivity().getApplication() ) ).create( NotesViewModel.class );
        //observeLayoutState();
        observeFrequentlyUsedNotes();
    }

//    private void observeLayoutState() {
//        notesViewModel.getLayoutState().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged( Integer integer ) {
//                Logger.log( "SETTING VIEW STATE" );
//                configureRecyclerView( integer );
//                MenuConfigurator.checkSelectedLayoutType( integer, binding.
//                        frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout.
//                        toolbar.getMenu() );
//            }
//        } );
//    }

    private void configureRecyclerView( int layoutType ) {
        RecyclerView recyclerView = binding.frequentlyUsedFragmentContent
                .frequentlyUsedFragmentNotesRecyclerView.recyclerview;
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
        binding.frequentlyUsedFragmentContent.textEmpty.setText( R.string.no_notes );
        notesAdapter = ( NotesAdapter ) recyclerView.getAdapter();
        notesAdapter.setEmptyView( binding.frequentlyUsedFragmentContent.layoutEmpty );
        notesAdapter.setNotesViewModel( notesViewModel );
    }

    private void reloadAdapterData( NotesAdapter oldAdapter ) {
        Menu menu = binding.frequentlyUsedFragmentContent.
                frequentlyUsedFragmentAppBarLayout.toolbar.getMenu();
        if ( oldAdapter == null )
            notesAdapter.setData( new ArrayList<>(), menu );
        else
            notesAdapter.setData( oldAdapter.getData(), menu );
    }

    private void configureListenerOnNotesAdapter() {
        notesAdapter.addListener(new NotesAdapter.NoteAdapterListener() {
            @Override
            public void onViewHolderClicked( View view, int viewHolderPosition ) {
                FrequentlyUsedFragmentDirections.ActionNavFrequentlyUsedToNavAddNote action =
                        FrequentlyUsedFragmentDirections
                                .actionNavFrequentlyUsedToNavAddNote(
                                        notesAdapter.getData().get( viewHolderPosition ).getId(),
                                        AddNoteFragment.FREQUENT_FRAGMENT );
                Navigation.findNavController( view ).navigate( action );
            }

            @Override
            public void onViewHolderLongClicked( int viewHolderPosition ) {
                Note noteSelected = notesAdapter.getData().get( viewHolderPosition );
                NoteActionDialog actionDialog = NoteActionDialog.newInstance( SHOW_ARCHIVE_OPTION );
                actionDialog.addListener( new NoteActionDialog.NoteActionDialogListener() {
                    @Override
                    public void deleteSelected() {
                        notesViewModel.deleteFrequentFragmentNoteReference( noteSelected.getId() );
                    }

                    @Override
                    public void onArchiveSelected() {
                        notesViewModel.archiveFrequentFragmentNote( noteSelected.getId() );
                    }

                    @Override
                    public void onUnarchiveSelected() {
                        // Not possible here..
                    }
                } );
                actionDialog.show( ((AppCompatActivity) getContext()).getSupportFragmentManager(), "note-actions" );
            }
        } );
    }

    private void observeFrequentlyUsedNotes() {
        notesViewModel.getFrequentFragmentNotesList().observe( getViewLifecycleOwner(),
                new Observer<List<Note>>() {
                    @Override
                    public void onChanged( List<Note> notes ) {
                        Logger.log( "SETTING FREQUENTLY USED NOTES LIST" );
                        notesAdapter.setData( notes, binding.frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout.toolbar.getMenu() );
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
        NavigationUI.setupWithNavController( binding.frequentlyUsedFragmentContent
                .frequentlyUsedFragmentAppBarLayout.toolbar, controller, appBarConfiguration );
        NavigationUI.setupWithNavController( navigationView, controller );
        binding.frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout
                .toolbar.inflateMenu( R.menu.minimal_options_menu);
        MenuConfigurator.configureMenu( binding.frequentlyUsedFragmentContent
                .frequentlyUsedFragmentAppBarLayout.toolbar.getMenu() );
    }



    @Override
    public void onResume() {
        super.onResume();
        binding.navView.getMenu().findItem( R.id.nav_frequently_used ).setChecked( true );
        binding.frequentlyUsedFragmentContent.frequentlyUsedFragmentAppBarLayout.mainTitle.setText( getString( R.string.frequently_used_fragment ) );
    }
}