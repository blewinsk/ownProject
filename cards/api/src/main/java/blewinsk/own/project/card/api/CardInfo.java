package blewinsk.own.project.card.api;

//immutable object, if there will be a more properties we can develop some Builder for this class
public class CardInfo {

    private final String cardNo;
    private final String bankName;
    private final boolean bankSuspended;

    public CardInfo(String cardNo, String bankName, boolean bankSuspended) {
        this.cardNo = cardNo;
        this.bankName = bankName;
        this.bankSuspended = bankSuspended;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public boolean isBankSuspended() {
        return bankSuspended;
    }
}
