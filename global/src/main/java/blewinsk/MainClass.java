package blewinsk;

import blewinsk.own.project.card.api.CardsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainClass {

    private final CardsService cardsService;

    public MainClass(){
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/global-beans.xml");
        cardsService = context.getBean(CardsService.class);
    }

    public static void main(String[] args) {

        MainClass mainClass = new MainClass();
        System.out.println("Elo Elo");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
