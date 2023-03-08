package com.mantis.takenotes.UI.ArchiveFragment;

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

import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.mantis.takenotes.Adapters.NotesAdapter;
import com.mantis.takenotes.Models.NotesViewModel;
import com.mantis.takenotes.R;

import com.mantis.takenotes.UI.MenuManagers.EditMenuManager.EditMenuManager;
import com.mantis.takenotes.databinding.TakeNotesFragmentArchiveBinding;

/**
 * @author - Michael Allan Odhiambo
 * @email - odhiambomichaelallan@gmail.com
 * @date - 4th March 2023
 */

public class ArchiveFragmentEditMenuManager extends EditMenuManager {

    private TakeNotesFragmentArchiveBinding binding;

    public ArchiveFragmentEditMenuManager( Fragment owner, NotesAdapter notesAdapter,
                                           NotesViewModel notesViewModel,
                                           TakeNotesFragmentArchiveBinding binding ) {
        super( owner, notesAdapter, notesViewModel );
        this.binding = binding;
    }
    @Override
    protected NotesAdapter getAdapter() {
        return ( NotesAdapter ) binding.archiveFragmentContent.
                notesRecyclerView.recyclerView.getAdapter();
    }

    @Override
    protected CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return binding.archiveFragmentContent.appBarLayout.collapsingToolbar;
    }

    @Override
    protected View getEditOptions() {
        return binding.archiveFragmentContent.appBarLayout.editOptions;
    }

    @Override
    protected Toolbar getMainToolbar() {
        return binding.archiveFragmentContent.appBarLayout.toolbar;
    }

    @Override
    protected void showFloatingActionButton( boolean editingEnabled ) {
        return;
    }

    @Override
    protected TextView getMainTitleTextView() {
        return binding.archiveFragmentContent.appBarLayout.mainTitle;
    }

    @Override
    protected Toolbar getEditingToolbar() {
        return binding.archiveFragmentContent.editOptionToolbar;
    }

    @Override
    protected String getTitle() {
        return owner.getString( R.string.archive );
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return binding.archiveFragmentContent.notesRecyclerView.recyclerView;
    }

    @Override
    protected void showEditingToolBar( boolean show ) {
        super.showEditingToolBar( show );
        if ( show )
            hideArchiveOption( getEditingToolbar(), true );
    }
}
