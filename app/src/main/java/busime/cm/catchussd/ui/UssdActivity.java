package busime.cm.catchussd.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import busime.cm.catchussd.MainActivity;
import busime.cm.catchussd.R;
import busime.cm.catchussd.domain.Ussd;
import busime.cm.catchussd.util.EventBus;
import busime.cm.catchussd.util.Predicate;
import busime.cm.catchussd.util.SetupHelper;
import busime.cm.catchussd.util.Strings;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UssdActivity extends AppCompatActivity {

    class ActivityViews {

        @BindView(R.id.recycler_view)
        RecyclerView recyclerView;
    }

    class DialogViews {

        @BindView(R.id.dialog_et_ussd_query)
        EditText etUssdQuery;
    }

    class Logic {

        private Predicate<EditText> notEmptyPredicate =
                et -> et.getText() != null && !Strings.isNullOrEmpty(et.getText().toString());

        boolean isInputValid() {
            return notEmptyPredicate.apply(dialogViews.etUssdQuery);
        }

    }

    public class UiEventsReactor {

        @OnClick(R.id.button_add)
        public void onAddUssdButtonClicked(View button) {
            showAddUssdDialog();
        }

        public void onDialogDoneButtonClicked(DialogInterface dialog, int which) {
            if (!logic.isInputValid())
                return;

            presenter.addUssd(dialogViews.etUssdQuery.getText().toString());
            dialog.dismiss();
        }

        public void onUssdItemClicked(Ussd ussd) {
            presenter.runUssd(ussd);
        }
    }

    private Logic logic = new Logic();
    private ActivityViews activityViews = new ActivityViews();
    private DialogViews dialogViews = new DialogViews();
    private UiEventsReactor eventsReactor = new UiEventsReactor();
    private UssdListAdapter adapter = new UssdListAdapter(view -> activityViews.recyclerView.getChildAdapterPosition(view), eventsReactor);
    private UssdListPresenter presenter = new UssdListPresenter(MainActivity.INSTANCE.ussdStorage, adapter, MainActivity.INSTANCE.ussdExecutor);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ussd_list);

        ButterKnife.bind(activityViews, this);
        ButterKnife.bind(eventsReactor, this);

        activityViews.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityViews.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSetupConditions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadUssdList();
        MainActivity.INSTANCE.eventBus.subscribe(EventBus.Event.USSD_RESULT_RECEIVED, event -> presenter.loadUssdList());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.INSTANCE.eventBus.unsubscribe(EventBus.Event.USSD_RESULT_RECEIVED);
    }

    public void showAddUssdDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(R.layout.dialog_add_ussd)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, eventsReactor::onDialogDoneButtonClicked)
                .create();
        dialog.show();

        ButterKnife.bind(dialogViews, dialog);
    }

    private void checkSetupConditions() {
        if (!SetupHelper.isPermissionGranted(this) || !SetupHelper.isAccessibilityServiceEnabled(this)) {
            Intent intent = new Intent(this, SetupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
