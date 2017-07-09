package blewinsk.paymentor.global;

import blewinsk.paymentor.services.cards.api.CardInfo;
import blewinsk.paymentor.services.cards.api.CardsService;
import blewinsk.paymentor.services.cards.api.exceptions.InvalidPanException;
import blewinsk.paymentor.commons.exceptions.NoSuchBankException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class PaymentorApplication {

    private static final int ADD_CARD_NUMBER = 1;
    private static final int SHOW_CARD_LIST = 2;
    private static final int GET_CARD_INFO = 3;
    private static final int EXIT_OPTION = 4;

    private static final Logger LOG = LogManager.getLogger(PaymentorApplication.class);

    private final CardsService cardsService;

    public PaymentorApplication(){
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/global-beans.xml");
        cardsService = context.getBean(CardsService.class);
    }

    public static void main(String[] args) {

        PaymentorApplication paymentorApplication = new PaymentorApplication();
        LOG.info("Run paymentor application");
        paymentorApplication.runMenu();
    }

    private void runMenu() {
        int option = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<String> cardNumbers = new LinkedList<>();
        while (option != EXIT_OPTION) {
            try {
                writeOptions();
                option = Integer.valueOf(br.readLine());
                switch(option) {
                    case ADD_CARD_NUMBER:
                        System.out.println("Enter card number: ");
                        cardNumbers.add(br.readLine());
                        break;
                    case SHOW_CARD_LIST:
                        writeListInfo(cardNumbers);
                        break;
                    case GET_CARD_INFO:
                        try {
                            List<CardInfo> cardInfos = cardsService.getCardsInfoForPanList(cardNumbers);
                            writeListInfo(cardInfos);
                        } catch (InvalidPanException | NoSuchBankException e) {
                            System.out.println("At least one of card number is invalid");
                        } finally {
                            if(!cardNumbers.isEmpty()) {
                                cardNumbers.clear();
                                System.out.println("List of card numbers is cleared");
                            }
                        }
                        break;
                    case EXIT_OPTION:
                        System.out.println("Exit the program");
                        break;
                    default:
                        System.out.println("Invalid option number");
                }
            } catch (IOException | NumberFormatException | NullPointerException e) {
                System.out.println("Invalid format");
            }
        }
    }

    private void writeListInfo(List list) {
        if(!list.isEmpty()) {
            for(int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + ". " + list.get(i));
            }
        } else {
            System.out.println("List of card numbers is empty");
        }
    }

    private void writeOptions() {
        System.out.println();
        System.out.println("Chose your option:");
        System.out.println(ADD_CARD_NUMBER + ". Add card number");
        System.out.println(SHOW_CARD_LIST + ". Show entered card numbers");
        System.out.println(GET_CARD_INFO + ". Get info for entered card numbers");
        System.out.println(EXIT_OPTION + ". Exit application");
        System.out.println("Option: ");
    }

}
