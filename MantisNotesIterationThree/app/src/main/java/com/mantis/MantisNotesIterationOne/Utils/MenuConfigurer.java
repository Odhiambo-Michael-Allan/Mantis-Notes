package com.mantis.MantisNotesIterationOne.Utils;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.mantis.MantisNotesIterationOne.R;

import java.util.ArrayList;

public class MenuConfigurer {

    private static ArrayList<MenuConfigurationListener> listeners = new ArrayList<>();

    public static void configureMenu( Toolbar toolbar ) {
        Menu optionsMenu = toolbar.getMenu();
        MenuItem viewOption = optionsMenu.findItem( R.id.view_option );
        viewOption.getSubMenu().findItem( R.id.simple_list_option ).setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( MenuItem item ) {
                System.out.println( "SIMPLE LIST OPTION SELECTED" );
                notifyListenersOfSimpleListClick();
                return true;
            }
        } );
        viewOption.getSubMenu().findItem( R.id.grid_option ).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println( "GRID OPTION SELECTED" );
                notifyListenersOfGridOptionClick();
                return true;
            }
        } );
        viewOption.getSubMenu().findItem( R.id.list_option ).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.
                return true;
            }
        })
    }

    public static void addListener( MenuConfigurationListener listener ) {
        listeners.add( listener );
    }

    public interface MenuConfigurationListener {
        void simpleListSelected();
        void gridViewSelected();
    }
}
