package com.mantis.MantisNotesIterationOne.Utils;

import com.mantis.MantisNotesIterationOne.Models.Note;
import com.mantis.MantisNotesIterationOne.Models.NotesViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class SortingUtil {

    public static ArrayList<Note> sortList( ArrayList<Note> notesList, boolean ascending,
                                            int sortingStrategy ) {
        if ( sortingStrategy == NotesViewModel.TITLE )
            notesList = sortNotesListAccordingToTitle( notesList, ascending );
        else if ( sortingStrategy == NotesViewModel.DATE_CREATED )
            notesList = sortNotesListAccordingToDateCreated( notesList, ascending );
        else
            notesList = sortNotesListAccordingToDateLastModified( notesList, ascending );
        return notesList;
    }

    // ----------------------------- TITLE ----------------------------------

    private static ArrayList<Note> sortNotesListAccordingToTitle( ArrayList<Note> notesList,
                                                       boolean ascending ) {
        if ( ascending )
            notesList = sortNotesListAccordingToTitleAscending( notesList );
        else
            notesList = sortNotesListAccordingToTitleDescending( notesList );
        return notesList;
    }

    private static ArrayList<Note> sortNotesListAccordingToTitleAscending( ArrayList<Note> notesList ) {
        ArrayList<Note> notesWithTitle = getNotesWithTitlesFrom( notesList );
        ArrayList<Note> notesWithoutTitle = getNotesWithoutTitlesFrom( notesList );
        Comparator<Note> titleAscendingComparator = new TitleAscendingComparator();
        Collections.sort( notesWithTitle, titleAscendingComparator );
        notesList = new ArrayList<>();
        addNotesWithoutTitleBackToNotesList( notesList, notesWithoutTitle );
        notesList.addAll( notesWithTitle );
        return notesList;
    }

    private static ArrayList<Note> getNotesWithTitlesFrom( ArrayList<Note> notesList ) {
        ArrayList<Note> notesWithTitle = new ArrayList<>();
        Iterator i = notesList.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i. next();
            if ( !note.getTitle().equals( "" ) ) {
                notesWithTitle.add( note );
            }
        }
        return notesWithTitle;
    }

    private static ArrayList<Note> getNotesWithoutTitlesFrom( ArrayList<Note> notesList ) {
        ArrayList<Note> notesWithoutTitle = new ArrayList<>();
        Iterator i = notesList.iterator();
        while ( i.hasNext() ) {
            Note note = ( Note ) i. next();
            if ( note.getTitle().equals( "" ) ) {
                notesWithoutTitle.add( note );
            }
        }
        return notesWithoutTitle;
    }

    private static ArrayList<Note> sortNotesListAccordingToTitleDescending( ArrayList<Note> notesList ) {
        ArrayList<Note> notesWithTitle = getNotesWithTitlesFrom( notesList );
        ArrayList<Note> notesWithoutTitle = getNotesWithoutTitlesFrom( notesList );
        Comparator<Note> titleDescendingComparator = new TitleDescendingComparator();
        Collections.sort( notesWithTitle, titleDescendingComparator );
        notesList = notesWithTitle;
        addNotesWithoutTitleBackToNotesList( notesList, notesWithoutTitle );
        return notesList;
    }

    private static void addNotesWithoutTitleBackToNotesList( ArrayList<Note> notesList,
                                                      ArrayList<Note> notesWithoutTitle ) {
        notesList.addAll( notesWithoutTitle );
    }

    // ----------------------------- DATE CREATED ----------------------------------------


    private static ArrayList<Note> sortNotesListAccordingToDateCreated( ArrayList<Note> notesList,
                                                      boolean ascending ) {
        if ( ascending )
            notesList = sortNotesListAccordingToDateCreatedAscending( notesList );
        else
            notesList = sortNotesListAccordingToDateCreatedDescending( notesList );
        return notesList;
    }

    private static ArrayList<Note> sortNotesListAccordingToDateCreatedAscending(
            ArrayList<Note> notesList ) {
        Comparator<Note> dateCreatedAscendingComparator = new DateCreatedAscendingComparator();
        Collections.sort( notesList, dateCreatedAscendingComparator );
        return notesList;
    }

    private static ArrayList<Note> sortNotesListAccordingToDateCreatedDescending(
            ArrayList<Note> notesList ) {
        Comparator<Note> dateCreatedDescendingComparator = new DateCreatedDescendingComparator();
        Collections.sort( notesList, dateCreatedDescendingComparator );
        return notesList;
    }


    // ----------------------------- DATE LAST MODIFIED -------------------------------

    private static ArrayList<Note> sortNotesListAccordingToDateLastModified(
            ArrayList<Note> notesList, boolean ascending ) {
        if ( ascending )
            notesList = sortNotesListAccordingToDateLastModifiedAscending( notesList );
        else
            notesList = sortNotesListAccordingToDateLastModifiedDescending( notesList );
        return notesList;
    }

    private static ArrayList<Note> sortNotesListAccordingToDateLastModifiedAscending(
            ArrayList<Note> notesList ) {
        Comparator<Note> dateLastModifiedAscendingComparator = new DateLastModifiedAscendingComparator();
        Collections.sort( notesList, dateLastModifiedAscendingComparator );
        return notesList;
    }

    private static ArrayList<Note> sortNotesListAccordingToDateLastModifiedDescending(
            ArrayList<Note> notesList ) {
        Comparator<Note> dateLastModifiedDescendingComparator = new
                DateLastModifiedDescendingComparator();
        Collections.sort( notesList, dateLastModifiedDescendingComparator );
        return notesList;
    }

    // -------------------- Comparators used in sorting -------------------------
    private static class TitleAscendingComparator implements Comparator<Note> {

        @Override
        public int compare( Note o1, Note o2 ) {
            return o1.getTitle().compareTo( o2.getTitle() );
        }
    }

    private static class TitleDescendingComparator implements Comparator<Note> {

        @Override
        public int compare( Note o1, Note o2 ) {
            return o2.getTitle().compareTo( o1.getTitle() );
        }
    }

    private static class DateCreatedAscendingComparator implements Comparator<Note> {

        @Override
        public int compare( Note o1, Note o2 ) {
            if ( o1.getDateCreated().after( o2.getDateCreated() ) )
                return 1;
            else if ( o1.getDateCreated().before( o2.getDateCreated() ) )
                return -1;
            else
                return 0;
        }
    }

    private static class DateCreatedDescendingComparator implements Comparator<Note> {

        @Override
        public int compare( Note o1, Note o2 ) {
            if ( o1.getDateCreated().before( o2.getDateCreated() ) )
                return 1;
            else if ( o1.getDateCreated().after( o2.getDateCreated() ) )
                return -1;
            else
                return 0;
        }
    }

    private static class DateLastModifiedAscendingComparator implements Comparator<Note> {

        @Override
        public int compare( Note o1, Note o2 ) {
            if ( o1.getDatelastModified().after( o2.getDatelastModified() ) )
                return 1;
            else if ( o1.getDatelastModified().before( o2.getDatelastModified() ) )
                return -1;
            else
                return 0;
        }
    }

    private static class DateLastModifiedDescendingComparator implements Comparator<Note> {

        @Override
        public int compare( Note o1, Note o2 ) {
            if ( o1.getDatelastModified().before( o2.getDatelastModified() ) )
                return 1;
            else if ( o1.getDatelastModified().after( o2.getDatelastModified() ) )
                return -1;
            else
                return 0;
        }
    }
}
