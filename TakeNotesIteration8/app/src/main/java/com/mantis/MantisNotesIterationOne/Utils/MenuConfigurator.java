package com.mantis.MantisNotesIterationOne.Utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;
import com.mantis.MantisNotesIterationOne.R;

import java.util.ArrayList;
import java.util.Iterator;

public class MenuConfigurator {

    private static ArrayList<MenuConfigurationListener> listeners = new ArrayList<>();


    public static void configureMenu( Menu menu ) {
        SubMenu viewOptionSubMenu = menu.findItem( R.id.view_option ).getSubMenu();
        configureSimpleListOption( viewOptionSubMenu );
        configureListOption( viewOptionSubMenu );
        configureGridOption( viewOptionSubMenu );
    }

    private static void configureSimpleListOption( SubMenu viewOptionSubMenu ) {
        viewOptionSubMenu.findItem( R.id.simple_list_option )
                .setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick( MenuItem item ) {
                        System.out.println( "SIMPLE LIST OPTION SELECTED" );
                        notifyListenersSimpleListOptionSelected();
                        return true;
                    }
                } );
    }

    private static void configureListOption( SubMenu viewOptionSubMenu ) {
        viewOptionSubMenu.findItem( R.id.list_option )
                .setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick( MenuItem item ) {
                        System.out.println( "LIST OPTION SELECTED" );
                        notifyListenersListOptionSelected();
                        return true;
                    }
                } );
    }

    private static void configureGridOption( SubMenu viewOptionSubMenu ) {
        viewOptionSubMenu.findItem( R.id.grid_option )
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick( MenuItem item ) {
                        System.out.println( "GRID OPTION SELECTED" );
                        checkGridOption( viewOptionSubMenu );
                        notifyListenersGridOptionSelected();
                        return true;
                    }
                } );
    }

    private static void notifyListenersSimpleListOptionSelected() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            MenuConfigurationListener listener = ( MenuConfigurationListener ) i.next();
            listener.onSimpleListOptionSelected();
        }
    }

    private static void notifyListenersListOptionSelected() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            MenuConfigurationListener listener = ( MenuConfigurationListener ) i.next();
            listener.onListOptionSelected();
        }
    }

    private static void notifyListenersGridOptionSelected() {
        Iterator i = listeners.iterator();
        while ( i.hasNext() ) {
            MenuConfigurationListener listener = ( MenuConfigurationListener ) i.next();
            listener.onGridOptionSelected();
        }
    }

    public static void addListener( MenuConfigurationListener listener ) {
        listeners.add( listener );
    }

    public static void checkSelectedLayoutType( int layoutType, Menu menu ) {
        SubMenu viewOptionSubMenu = menu.findItem( R.id.view_option ).getSubMenu();
        uncheckAllMenuItems( viewOptionSubMenu );
        if ( layoutType == NotesViewModel.LAYOUT_STATE_GRID )
            checkGridOption( viewOptionSubMenu );
        else if ( layoutType == NotesViewModel.LAYOUT_STATE_LIST )
            checkListOption( viewOptionSubMenu );
        else
            checkSimpleListOption( viewOptionSubMenu );
    }

    private static void uncheckAllMenuItems( SubMenu viewOptionSubMenu ) {
        viewOptionSubMenu.findItem( R.id.simple_list_option ).setIcon( null );
        viewOptionSubMenu.findItem( R.id.simple_list_option ).setTitle( "Simple List" );
        viewOptionSubMenu.findItem( R.id.list_option ).setIcon( null );
        viewOptionSubMenu.findItem( R.id.list_option ).setTitle( "List" );
        viewOptionSubMenu.findItem( R.id.grid_option ).setIcon( null );
        viewOptionSubMenu.findItem( R.id.grid_option ).setTitle( "Grid" );
    }

    private static void checkGridOption( SubMenu viewOptionSubMenu ) {
        viewOptionSubMenu.findItem( R.id.grid_option ).setIcon(R.drawable.check_icon);
        viewOptionSubMenu.findItem( R.id.grid_option ).getIcon().setTint(Color.parseColor( "#0BFFFF"));
        SpannableString s = new SpannableString( "Grid" );
        s.setSpan( new ForegroundColorSpan( Color.parseColor( "#0BFFFF" ) ), 0, s.length(), 0 );
        viewOptionSubMenu.findItem( R.id.grid_option ).setTitle( s );
    }

    private static void checkListOption( SubMenu viewOptionSubMenu ) {
        viewOptionSubMenu.findItem( R.id.list_option ).setIcon( R.drawable.check_icon );
        viewOptionSubMenu.findItem( R.id.list_option ).getIcon().setTint( Color.parseColor( "#0BFFFF" ) );
        SpannableString s = new SpannableString( "List" );
        s.setSpan( new ForegroundColorSpan( Color.parseColor( "#0BFFFF" ) ), 0, s.length(), 0 );
        viewOptionSubMenu.findItem( R.id.list_option ).setTitle( s );
    }

    private static void checkSimpleListOption( SubMenu viewOptionSubMenu ) {
        viewOptionSubMenu.findItem( R.id.simple_list_option ).setIcon( R.drawable.check_icon );
        viewOptionSubMenu.findItem( R.id.simple_list_option ).getIcon().setTint( Color.parseColor( "#0BFFFF" ) );
        SpannableString s = new SpannableString( "Simple List" );
        s.setSpan( new ForegroundColorSpan( Color.parseColor( "#0BFFFF" ) ), 0, s.length(), 0 );
        viewOptionSubMenu.findItem( R.id.simple_list_option ).setTitle( s );
    }


    public interface MenuConfigurationListener {
        void onSimpleListOptionSelected();
        void onGridOptionSelected();
        void onListOptionSelected();
    }
}
