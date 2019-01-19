package busime.cm.catchussd.data;

import busime.cm.catchussd.domain.Ussd;

public interface UssdExecutor {
    void run(Ussd ussd);

    void setResponse(String result);
}
