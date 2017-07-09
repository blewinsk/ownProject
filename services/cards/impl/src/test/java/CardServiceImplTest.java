import blewinsk.paymentor.commons.exceptions.NoSuchBankException;
import blewinsk.paymentor.services.banks.api.BanksService;
import blewinsk.paymentor.services.cards.api.CardInfo;
import blewinsk.paymentor.services.cards.api.exceptions.InvalidPanException;
import blewinsk.paymentor.services.cards.impl.CardsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceImplTest {

    @Mock
    private BanksService banksService;

    private CardsServiceImpl cardsService;

    private String regexpPrefix = "^";
    private String regexpSufix = "$";

    private String bankKey1 = "000000";
    private String bankName1 = "bankOne";
    private String bankKey2 = "111111";
    private String bankName2 = "bankTwo";
    private String unknowBank = "asdasd";

    @Before
    public void setup() {
        cardsService = new CardsServiceImpl(banksService);
        cardsService.getIinRangesMap().clear();
        cardsService.getIinRangesMap().put(regexpPrefix + bankKey1 + regexpSufix, bankName1);
        cardsService.getIinRangesMap().put(regexpPrefix + bankKey2 + regexpSufix, bankName2);
    }

    @Test
    public void getInvalidPANExceptionForEmptyString() {
        Throwable throwable = catchThrowable(() -> cardsService.getCardInfoForPan(""));

        assertThat(throwable).isExactlyInstanceOf(InvalidPanException.class);
    }

    @Test
    public void getCardInfoForSingleIIN() {

        doReturn(true).when(banksService).isBankSuspended(any());

        CardInfo cardInfo = cardsService.getCardInfoForIin(bankKey1);

        assertThat(cardInfo.getIin()).isEqualTo(bankKey1);
        assertThat(cardInfo.getBankName()).isEqualTo(bankName1);
        assertThat(cardInfo.isBankSuspended()).isEqualTo(true);
    }

    @Test
    public void getNoBankExceptionWhenUnknownBankInMap() {
        Throwable throwable = catchThrowable(() -> cardsService.getCardInfoForIin(unknowBank));

        assertThat(cardsService.getIinRangesMap().keySet()).doesNotContain(unknowBank);
        assertThat(throwable).isExactlyInstanceOf(NoSuchBankException.class);
    }

    @Test
    public void getCardInfoWhenBanksServiceReturnNull() {
        doReturn(null).when(banksService).isBankSuspended(any());

        CardInfo cardInfo = cardsService.getCardInfoForIin(bankKey1);

        assertThat(cardInfo.getIin()).isEqualTo(bankKey1);
        assertThat(cardInfo.getBankName()).isEqualTo(bankName1);
        assertThat(cardInfo.isBankSuspended()).isEqualTo(false);
    }

    @Test
    public void getCardInfosForIINList() {
        List<String> list = new ArrayList<>();
        list.add(bankKey1);
        list.add(bankKey2);

        List<CardInfo> cardInfos = cardsService.getCardsInfoForIinList(list);

        assertThat(list).hasSameSizeAs(cardInfos);
    }
}