package com.p4.accountsaver.ui.account;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.p4.accountsaver.model.SecurityQuestion;
import com.p4.accountsaver.model.SecurityQuestions;
import com.p4.accountsaver.ui.account.adapters.SecurityQuestionAdapter;

public class SecurityQuestionsViewModel extends ViewModel implements SecurityQuestionAdapter.SecurityQuestionCell.SecurityQuestionListener {
    public final ObservableField<SecurityQuestions> securityQuestions = new ObservableField<>();
    public final ObservableBoolean canEdit = new ObservableBoolean(false);

    private MutableLiveData<Void> mShowAddDialogEvent = new MutableLiveData<>();

    public void start(SecurityQuestions questions, boolean canEdit) {
        securityQuestions.set(questions);
        this.canEdit.set(canEdit);
    }

    public void showAddDialog() {
        mShowAddDialogEvent.setValue(null);
    }

    public void addSecurityQuestion(SecurityQuestion securityQuestion) {
        securityQuestions.get().addQuestion(securityQuestion);
    }

    @Override
    public boolean onSecurityQuestionLongClicked(SecurityQuestion securityQuestion) {
        if (canEdit.get()) {
            securityQuestions.get().removeQuestion(securityQuestion);
            return true;
        }
        return false;
    }

    // region Get Set
    public LiveData<Void> getShowAddDialogEvent() {
        return mShowAddDialogEvent;
    }
    // endregion
}
