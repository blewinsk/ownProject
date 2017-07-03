package blewinsk.own.project.banks.api;

import blewinsk.own.project.commons.exceptions.TechnicalException;
import blewinsk.own.project.commons.exceptions.NoSuchBankException;

public interface BanksService {

    Boolean isBankSuspended(String bankId) throws NoSuchBankException, TechnicalException;

}
