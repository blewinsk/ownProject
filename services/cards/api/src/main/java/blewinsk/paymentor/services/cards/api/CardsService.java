package blewinsk.paymentor.services.cards.api;

import blewinsk.paymentor.commons.exceptions.NoSuchBankException;
import blewinsk.paymentor.services.cards.api.exceptions.InvalidPanException;

import java.util.List;

public interface CardsService {

    CardInfo getCardInfoForPan(String pan) throws InvalidPanException, NoSuchBankException;

    List<CardInfo> getCardsInfoForPanList(List<String> panList) throws InvalidPanException, NoSuchBankException;

    CardInfo getCardInfoForIin(String iin);

    List<CardInfo> getCardsInfoForIinList(List<String> iinList);

    void addBank(String iin, String bankName);
}
