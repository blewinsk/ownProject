package blewinsk.own.project.card.impl;

import blewinsk.own.project.banks.api.BanksService;
import blewinsk.own.project.commons.exceptions.TechnicalException;
import blewinsk.own.project.card.api.CardInfo;
import blewinsk.own.project.card.api.CardsService;
import blewinsk.own.project.card.api.exceptions.InvalidPANException;
import blewinsk.own.project.commons.exceptions.NoSuchBankException;
import blewinsk.own.project.commons.exceptions.ServiceException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@ComponentScan(basePackages = "blewinsk.own.project.card.impl")
public class CardsServiceImpl implements CardsService {

    private static final Logger LOG = LogManager.getLogger(CardsServiceImpl.class);

    //it will be always static or should i take it from DB?
    private Map<String, String> iinRangesMap = new HashMap<String, String>() {{
        put("^5(3[5-9][8-9]|4[\\d]{2})[\\d]{2}$", "RolBank");
        put("^52[\\d]{4}$", "nBank");
        put("^6[0-1]{1}[\\d]{4}$", "Kakao SA");
        put("^53([0-4][0-9]|5[0-7])[\\d]{2}$", "OMG Bank Łódzki");// 5358 is in 2 banks so i remove it from this bank,
        // the original regexp is "^53([0-4][0-9]|5[0-8])[\\d]{2}$"
    }};

    private static int INN_LENGTH = 6; //via Wikipedia - The first six digits of a card number
    private static int PAN_MAN_LENGTH = 16; //via Wiki
    private static int PAN_MIN_LENGTH = 12; //via Wiki

    private final BanksService banksService;

    @Autowired
    public CardsServiceImpl(BanksService banksService) {
        this.banksService = banksService;
    }

    public CardInfo getCardInfoForPAN(String panNumber) throws InvalidPANException, NoSuchBankException {
        if (invalidPanNumber(panNumber)) {
            throw new InvalidPANException(panNumber);
        }
        return getCardInfoForIIN(panNumber.substring(INN_LENGTH));
    }

    @Override
    public List<CardInfo> getCardsInfoForPANs(List<String> panNumbers) throws InvalidPANException, NoSuchBankException {
        List<CardInfo> cardInfos = new LinkedList<>();
        for (String panNumber : panNumbers) {
            cardInfos.add(getCardInfoForPAN(panNumber));
        }
        return cardInfos;
    }

    @Override
    public CardInfo getCardInfoForIIN(String iin) throws NoSuchBankException {
        String bankName = iinRangesMap.keySet().stream().filter(iin::matches).findFirst()
            .orElseThrow(() -> new NoSuchBankException(null));
        //if innNumber be like "abcabc" we won't find bank and throw exception

        LOG.info("Found bank name {0} for {1} {2}", bankName, "INN " + iin);

        Boolean isBankSuspended;

        try {
            isBankSuspended = banksService.isBankSuspended(bankName);
            //is it a exception when bank is suspended? my service should throw exception or the one which calls me should check it?
        } catch (TechnicalException e) {
            LOG.error("Error receiving bank info", e);
            throw new ServiceException();
        }
        return new CardInfo(iin, bankName,
            BooleanUtils.isTrue(isBankSuspended));//BooleanUtils because banksService return "big" boolean
    }

    @Override
    public List<CardInfo> getCardsInfoForIINs(List<String> IINs) throws NoSuchBankException {
        List<CardInfo> cardInfos = new LinkedList<>();
        for (String IIN : IINs) {
            cardInfos.add(getCardInfoForIIN(IIN));
        }
        return cardInfos;
    }

    private boolean invalidPanNumber(String panNumber) {
        int panLength = panNumber == null ? 0 : panNumber.length();
        return panLength < INN_LENGTH || panLength > PAN_MAN_LENGTH
            || panLength < PAN_MIN_LENGTH;//we validate PAN so check min length too
    }

    /* I don't know if i get this sentence good:
    Your service shall maintain a list of known banks and their ranges. An initial list
        of banks and their ranges is known, but it is not yet known whether will it ever
        expand or change (it is, however, purposefully incomplete)
     */
    @Override
    public void addBank(String INN, String bankName) {
        //should it be here some kind of validation?
        if(iinRangesMap.values().contains(bankName)) {
            throw new ServiceException();
        }
        iinRangesMap.put(INN, bankName);
    }
}