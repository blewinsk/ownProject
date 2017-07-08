package blewinsk.own.project.banks.impl;

import blewinsk.own.project.banks.api.BanksService;
import blewinsk.own.project.commons.exceptions.NoSuchBankException;
import blewinsk.own.project.commons.exceptions.TechnicalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class BanksServiceImpl implements BanksService {

    public Boolean isBankSuspended(String bankId) throws NoSuchBankException, TechnicalException {
        throw new UnsupportedOperationException("Missing dev");
    }
}
