package blewinsk.own.project.banks.api;

import blewinsk.own.project.commons.exceptions.NoSuchBankException;
import blewinsk.own.project.commons.exceptions.TechnicalException;

public interface BanksService {

    Boolean isBankSuspended(String bankId) throws NoSuchBankException, TechnicalException;

}
