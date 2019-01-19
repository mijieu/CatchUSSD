package busime.cm.catchussd.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import busime.cm.catchussd.domain.Ussd;
import busime.cm.catchussd.util.SetupHelper;

public class UssdRunner implements UssdExecutor {

    private static final String ENCODED_HASH = Uri.encode("#");

    private Context context;
    private Ussd pendingUssd;

    public UssdRunner(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void run(Ussd ussd) {
        if (!SetupHelper.isPermissionGranted(context)) {
            return;
        }
        pendingUssd = ussd.clone();
        String ussdCode = ussd.getCode().replaceAll("#", ENCODED_HASH);
        Uri uri = Uri.parse("tel:" + ussdCode);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void setResponse(String result) {
        if (pendingUssd != null) {
            pendingUssd.setResponse(result);
            //App.INSTANCE.ussdStorage.updateUssd(pendingUssd);
            pendingUssd = null;
        }
    }
}
