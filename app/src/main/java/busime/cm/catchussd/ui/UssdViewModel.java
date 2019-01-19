package busime.cm.catchussd.ui;

import java.util.List;

import busime.cm.catchussd.domain.Ussd;

public interface UssdViewModel {
    void setDataAndInvalidateView(List<Ussd> data);
}
