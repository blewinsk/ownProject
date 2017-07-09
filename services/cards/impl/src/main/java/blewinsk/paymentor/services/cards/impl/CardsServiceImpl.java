package blewinsk.paymentor.services.cards.impl;

import blewinsk.paymentor.commons.exceptions.NoSuchBankException;
import blewinsk.paymentor.commons.exceptions.TechnicalException;
import blewinsk.paymentor.services.banks.api.BanksService;
import blewinsk.paymentor.services.cards.api.CardInfo;
import blewinsk.paymentor.services.cards.api.CardsService;
import blewinsk.paymentor.services.cards.api.exceptions.InvalidPanException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CardsServiceImpl implements CardsService {

    private static final Logger LOG = LogManager.getLogger(CardsServiceImpl.class);

    //should i take it from DB?
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

    public CardInfo getCardInfoForPan(String panNumber) throws InvalidPanException, NoSuchBankException {
        if (invalidPanNumber(panNumber)) {
            throw new InvalidPanException(panNumber);
        }
        return getCardInfoForIin(panNumber.substring(INN_LENGTH));
    }

    @Override
    public List<CardInfo> getCardsInfoForPanList(List<String> panList) throws InvalidPanException, NoSuchBankException {
        return panList.stream().map(this::getCardInfoForPan).collect(Collectors.toList());
    }

    @Override
    public CardInfo getCardInfoForIin(String iin) {

        String bankName = iinRangesMap.entrySet().stream().filter(entrySet -> iin.matches(entrySet.getKey()))
            .findFirst()
            .orElseThrow(() -> new NoSuchBankException(null)).getValue();

        LOG.info("Found bank name {0} for IIN {2}", bankName, iin);

        Boolean isBankSuspended;

        try {
            isBankSuspended = banksService.isBankSuspended(bankName);
            //is it a exception when bank is suspended? my service should throw exception or the one which calls me should check it?
        } catch (TechnicalException exception) {
            LOG.error("Error receiving bank info", exception);
            throw exception;
        }
        return new CardInfo(iin, bankName,
            BooleanUtils.isTrue(isBankSuspended));//BooleanUtils because banksService return "big" boolean
    }

    @Override
    public List<CardInfo> getCardsInfoForIinList(List<String> iinList) {
        return iinList.stream().map(this::getCardInfoForIin).collect(Collectors.toList());
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
    public void addBank(String iin, String bankName) {
        //should it be here some kind of validation?
        if (iinRangesMap.values().contains(bankName)) {
            throw new TechnicalException();
        }
        iinRangesMap.put(iin, bankName);
    }

    public Map<String, String> getIinRangesMap() {
        return iinRangesMap;
    }
}
