package com.mantis.takenotes.Commands;

/**
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.Intent;
import com.mantis.takenotes.Models.NotesViewModel;

import com.mantis.takenotes.Repository.Note;
import java.util.List;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

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
