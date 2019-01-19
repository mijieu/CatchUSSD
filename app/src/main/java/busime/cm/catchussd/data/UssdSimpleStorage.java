package busime.cm.catchussd.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import busime.cm.catchussd.domain.Ussd;
import busime.cm.catchussd.ui.Serializer;

public class UssdSimpleStorage implements UssdRepository {

    private static final String PREFERENCES_NAME = "SimpleStorage";
    private Context context;

    public UssdSimpleStorage(Context context) {
        this.context = context;
    }

    @Override
    public void addUssd(Ussd ussd) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        preferences.edit()
                .putString(ussd.getId(), Serializer.serialize(ussd))
                .apply();
    }

    @Override
    public void removeUssd(Ussd ussd) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        preferences.edit()
                .remove(ussd.getId())
                .apply();
    }

    @Override
    public void updateUssd(Ussd ussd) {
        addUssd(ussd);
    }

    @Override
    public List<Ussd> getUssdList() {
        List<Ussd> ussdList = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Map<String, ?> serializedUssdMap = preferences.getAll();
        for (String ussdId : serializedUssdMap.keySet()) {
            ussdList.add(Serializer.deserialize((String) serializedUssdMap.get(ussdId)));
        }
        return ussdList;
    }
}
