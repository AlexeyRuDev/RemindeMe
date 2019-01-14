package com.example.rudnev.remindme.components;

import com.example.rudnev.remindme.annotations.RemindMeAppliacationScope;
import com.example.rudnev.remindme.modules.RepositoryModel;
import com.example.rudnev.remindme.viewmodels.FragmentsViewModel;
import com.example.rudnev.remindme.viewmodels.NoteViewModel;


import dagger.Component;

@RemindMeAppliacationScope
@Component(modules = {RepositoryModel.class})
public interface RemindMeComponent {
    void injectNoteViewModel(NoteViewModel noteViewModel);
    void injectFragmentViewModel(FragmentsViewModel fragmentsViewModel);
}
