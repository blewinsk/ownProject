package blewinsk.own.project.card.api;

import blewinsk.own.project.card.api.exceptions.InvalidPANException;
import blewinsk.own.project.commons.exceptions.NoSuchBankException;

import java.util.List;

public interface CardsService {

    CardInfo getCardInfoForPAN(String pan) throws InvalidPANException, NoSuchBankException;

    List<CardInfo> getCardsInfoForPANs(List<String> PANs) throws InvalidPANException, NoSuchBankException;

    CardInfo getCardInfoForIIN(String INN) throws NoSuchBankException;

    List<CardInfo> getCardsInfoForIINs(List<String> INNs) throws NoSuchBankException;

    void addBank(String INN, String bankName);
}
