package blewinsk.paymentor.services.banks.impl;

import blewinsk.paymentor.services.banks.api.BanksService;
import blewinsk.paymentor.commons.exceptions.NoSuchBankException;
import blewinsk.paymentor.commons.exceptions.TechnicalException;
import org.springframework.stereotype.Component;

@Component
public class BanksServiceImpl implements BanksService {

    public Boolean isBankSuspended(String bankId) throws NoSuchBankException, TechnicalException {
        return null;
        //throw new UnsupportedOperationException("Missing dev");
    }
}
