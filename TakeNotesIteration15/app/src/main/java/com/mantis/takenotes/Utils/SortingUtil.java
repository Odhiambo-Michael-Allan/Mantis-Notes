package com.mantis.takenotes.Utils;

import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.Repository.Note;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import java.util.List;

public class SortingUtil {

    public static List<Note> sortList(List<Note> notesList, boolean ascending,
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

    private static List<Note> sortNotesListAccordingToTitle(List<Note> notesList,
                                                            boolean ascending ) {
        if ( ascending )
            notesList = sortNotesListAccordingToTitleAscending( notesList );
        else
            notesList = sortNotesListAccordingToTitleDescending( notesList );
        return notesList;
    }

    private static List<Note> sortNotesListAccordingToTitleAscending(List<Note> notesList ) {
        List<Note> notesWithTitle = getNotesWithTitlesFrom( notesList );
        List<Note> notesWithoutTitle = getNotesWithoutTitlesFrom( notesList );
        Comparator<Note> titleAscendingComparator = new TitleAscendingComparator();
        Collections.sort( notesWithTitle, titleAscendingComparator );
        notesList = new ArrayList<>();
        addNotesWithoutTitleBackToNotesList( notesList, notesWithoutTitle );
        notesList.addAll( notesWithTitle );
        return notesList;
    }

    private static ArrayList<Note> getNotesWithTitlesFrom(List<Note> notesList ) {
        ArrayList<Note> notesWithTitle = new ArrayList<>();
        Iterator i = notesList.iterator();
        while ( i.hasNext() ) {
            Note note = (Note) i. next();
            if ( !note.getTitle().equals( "" ) ) {
                notesWithTitle.add( note );
            }
        }
        return notesWithTitle;
    }

    private static List<Note> getNotesWithoutTitlesFrom(List<Note> notesList ) {
        ArrayList<Note> notesWithoutTitle = new ArrayList<>();
        Iterator i = notesList.iterator();
        while ( i.hasNext() ) {
            Note note = (Note) i. next();
            if ( note.getTitle().equals( "" ) ) {
                notesWithoutTitle.add( note );
            }
        }
        return notesWithoutTitle;
    }

    private static List<Note> sortNotesListAccordingToTitleDescending(List<Note> notesList ) {
        List<Note> notesWithTitle = getNotesWithTitlesFrom( notesList );
        List<Note> notesWithoutTitle = getNotesWithoutTitlesFrom( notesList );
        Comparator<Note> titleDescendingComparator = new TitleDescendingComparator();
        Collections.sort( notesWithTitle, titleDescendingComparator );
        notesList = notesWithTitle;
        addNotesWithoutTitleBackToNotesList( notesList, notesWithoutTitle );
        return notesList;
    }

    private static void addNotesWithoutTitleBackToNotesList( List<Note> notesList,
                                                      List<Note> notesWithoutTitle ) {
        notesList.addAll( notesWithoutTitle );
    }

    // ----------------------------- DATE CREATED ----------------------------------------


    private static List<Note> sortNotesListAccordingToDateCreated(List<Note> notesList,
                                                                  boolean ascending ) {
        if ( ascending )
            notesList = sortNotesListAccordingToDateCreatedAscending( notesList );
        else
            notesList = sortNotesListAccordingToDateCreatedDescending( notesList );
        return notesList;
    }

    private static List<Note> sortNotesListAccordingToDateCreatedAscending(
            List<Note> notesList ) {
        Comparator<Note> dateCreatedAscendingComparator = new DateCreatedAscendingComparator();
        Collections.sort( notesList, dateCreatedAscendingComparator );
        return notesList;
    }

    private static List<Note> sortNotesListAccordingToDateCreatedDescending(
            List<Note> notesList ) {
        Comparator<Note> dateCreatedDescendingComparator = new DateCreatedDescendingComparator();
        Collections.sort( notesList, dateCreatedDescendingComparator );
        return notesList;
    }


    // ----------------------------- DATE LAST MODIFIED -------------------------------

    private static List<Note> sortNotesListAccordingToDateLastModified(
            List<Note> notesList, boolean ascending ) {
        if ( ascending )
            notesList = sortNotesListAccordingToDateLastModifiedAscending( notesList );
        else
            notesList = sortNotesListAccordingToDateLastModifiedDescending( notesList );
        return notesList;
    }

    private static List<Note> sortNotesListAccordingToDateLastModifiedAscending(
            List<Note> notesList ) {
        Comparator<Note> dateLastModifiedAscendingComparator = new DateLastModifiedAscendingComparator();
        Collections.sort( notesList, dateLastModifiedAscendingComparator );
        return notesList;
    }

    private static List<Note> sortNotesListAccordingToDateLastModifiedDescending(
            List<Note> notesList ) {
        Comparator<Note> dateLastModifiedDescendingComparator = new
                DateLastModifiedDescendingComparator();
        Collections.sort( notesList, dateLastModifiedDescendingComparator );
        return notesList;
    }

    // -------------------- Comparators used in sorting -------------------------
    private static class TitleAscendingComparator implements Comparator<Note> {

        @Override
        public int compare(Note o1, Note o2 ) {
            return o1.getTitle().compareTo( o2.getTitle() );
        }
    }

    private static class TitleDescendingComparator implements Comparator<Note> {

        @Override
        public int compare(Note o1, Note o2 ) {
            return o2.getTitle().compareTo( o1.getTitle() );
        }
    }

    private static class DateCreatedAscendingComparator implements Comparator<Note> {

        @Override
        public int compare(Note o1, Note o2 ) {
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
        public int compare(Note o1, Note o2 ) {
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
        public int compare(Note o1, Note o2 ) {
            if ( o1.getDateLastModified().after( o2.getDateLastModified() ) )
                return 1;
            else if ( o1.getDateLastModified().before( o2.getDateLastModified() ) )
                return -1;
            else
                return 0;
        }
    }

    private static class DateLastModifiedDescendingComparator implements Comparator<Note> {

        @Override
        public int compare(Note o1, Note o2 ) {
            if ( o1.getDateLastModified().before( o2.getDateLastModified() ) )
                return 1;
            else if ( o1.getDateLastModified().after( o2.getDateLastModified() ) )
                return -1;
            else
                return 0;
        }
    }
}
