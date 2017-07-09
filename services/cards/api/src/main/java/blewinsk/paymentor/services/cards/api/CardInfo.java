package blewinsk.paymentor.services.cards.api;

//immutable object, if there will be a more properties we can develop some Builder for this class
public class CardInfo {

    private final String iin;
    private final String bankName;
    private final boolean bankSuspended;

    public CardInfo(String iin, String bankName, boolean bankSuspended) {
        this.iin = iin;
        this.bankName = bankName;
        this.bankSuspended = bankSuspended;
    }

    public String getIin() {
        return iin;
    }

    public String getBankName() {
        return bankName;
    }

    public boolean isBankSuspended() {
        return bankSuspended;
    }

    @Override
    public String toString() {
        return new StringBuffer().append("Card info: IIN: ").append(iin).append(", bank name: ").append(bankName)
            .append(", bank is ")
            .append(bankSuspended ? " " : "not ").append("suspended").toString();
    }
}
