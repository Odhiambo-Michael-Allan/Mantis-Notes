package com.mantis.takenotes.Adapters;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import com.mantis.takenotes.R;
import com.mantis.takenotes.ViewHolders.NoteViewHolder;

import com.mantis.takenotes.ViewHolders.SimpleListViewHolder;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class SimpleLayoutAdapter extends NotesAdapter {
    @Override
    protected View getNoteView( @NonNull ViewGroup parent ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.take_notes_simple_list_note_view, parent, false );
        return view;
    }

    @Override
    protected NoteViewHolder getNoteViewHolder( View view, NotesAdapter adapter ) {
        return new SimpleListViewHolder( view, adapter );
    }
}
