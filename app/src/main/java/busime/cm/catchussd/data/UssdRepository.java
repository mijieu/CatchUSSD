package busime.cm.catchussd.data;

import java.util.List;

import busime.cm.catchussd.domain.Ussd;

public interface UssdRepository {
    void addUssd(Ussd ussd);

    void removeUssd(Ussd ussd);

    void updateUssd(Ussd ussd);

    List<Ussd> getUssdList();
}
