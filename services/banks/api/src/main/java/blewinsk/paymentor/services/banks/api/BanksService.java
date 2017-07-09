package blewinsk.paymentor.services.banks.api;

import blewinsk.paymentor.commons.exceptions.NoSuchBankException;
import blewinsk.paymentor.commons.exceptions.TechnicalException;

public interface BanksService {

    Boolean isBankSuspended(String bankId) throws NoSuchBankException, TechnicalException;

}
