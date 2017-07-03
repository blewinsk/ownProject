package blewinsk.own.project.banks.impl;

import blewinsk.own.project.banks.api.BanksService;
import blewinsk.own.project.commons.exceptions.TechnicalException;
import blewinsk.own.project.commons.exceptions.NoSuchBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackages = "blewinsk.own.project.banks.impl")
public class BanksServiceImpl implements BanksService {

    @Autowired
    public BanksServiceImpl() {
    }

    public Boolean isBankSuspended(String bankId) throws NoSuchBankException, TechnicalException {
        throw new UnsupportedOperationException("Missing dev");
    }
}