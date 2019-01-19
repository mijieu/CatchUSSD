package busime.cm.catchussd.ui;

import busime.cm.catchussd.data.UssdExecutor;
import busime.cm.catchussd.data.UssdRepository;
import busime.cm.catchussd.domain.Ussd;

public class UssdListPresenter {
    private UssdRepository repository;
    private UssdViewModel viewModel;
    private UssdExecutor executor;

    public UssdListPresenter(UssdRepository ussdRepository, UssdViewModel viewModel, UssdExecutor executor) {
        this.repository = ussdRepository;
        this.viewModel = viewModel;
        this.executor = executor;
    }

    public void addUssd(String code) {
        Ussd ussd = new Ussd();
        ussd.setCode(code);
        repository.addUssd(ussd);
        loadUssdList();
    }

    public void loadUssdList() {
        viewModel.setDataAndInvalidateView(repository.getUssdList());
    }

    public void runUssd(Ussd ussd) {
        executor.run(ussd);
    }
}
