package com.mantis.takenotes.Commands;

import android.content.Context;
import android.content.Intent;
import com.mantis.takenotes.Models.NotesViewModel;

import com.mantis.takenotes.data.source.local.Note;
import java.util.List;

public class ShareCommand implements Command {

    private Context context;
    private List<Note> notesToShare;
    private NotesViewModel notesViewModel;

    public ShareCommand( Context context, List<Note> notesToShare, NotesViewModel notesViewModel ) {
        this.context = context;
        this.notesToShare = notesToShare;
        this.notesViewModel = notesViewModel;
    }

    @Override
    public void execute() {
        sendIntent();
    }

    private void sendIntent() {
        Intent intent = new Intent( Intent.ACTION_SEND );
        intent.setType( "text/plain" );
        intent.putExtra( Intent.EXTRA_TEXT, getNotesToShare() );
        this.context.startActivity( Intent.createChooser( intent, "Select Option" ) );
    }

    private String getNotesToShare() {
        StringBuilder stringBuilder = new StringBuilder();
        for ( Note note : this.notesToShare )
            stringBuilder.append( String.format( "%s\n%s\n", note.getTitle(), note.getDescription() ) );
        return stringBuilder.toString();
    }
}
