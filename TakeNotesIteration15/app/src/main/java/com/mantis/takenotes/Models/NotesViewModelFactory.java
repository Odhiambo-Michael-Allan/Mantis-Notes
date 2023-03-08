package com.mantis.takenotes.Models;

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

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mantis.takenotes.Repository.NoteRepository;

/**
 * This is a factory used for creating NotesViewModel instances..
 *
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */
public class NotesViewModelFactory implements ViewModelProvider.Factory {

    private final NoteRepository noteRepository;

    public NotesViewModelFactory( NoteRepository noteRepository ) {
        this.noteRepository = noteRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create( @NonNull Class<T> modelClass ) {
        return ( T ) new NotesViewModel( noteRepository );
    }
}
