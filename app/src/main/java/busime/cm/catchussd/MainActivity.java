package busime.cm.catchussd;

import android.app.Application;

import busime.cm.catchussd.data.UssdExecutor;
import busime.cm.catchussd.data.UssdRepository;
import busime.cm.catchussd.data.UssdRunner;
import busime.cm.catchussd.data.UssdSimpleStorage;
import busime.cm.catchussd.security.PermissionManager;
import busime.cm.catchussd.util.EventBus;

public class MainActivity extends Application {

    public static PermissionManager permissionManager;

    public UssdExecutor ussdExecutor;
    public EventBus eventBus;
    public UssdRepository ussdStorage;

    public static MainActivity INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        permissionManager = new PermissionManager();
        ussdStorage = new UssdSimpleStorage(this);
        ussdExecutor = new UssdRunner(this);
        eventBus = new EventBus(this);

        INSTANCE = this;
    }

}
