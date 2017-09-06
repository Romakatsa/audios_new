import org.la4j.Matrix;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {

            System.out.println(AudioScrapper.stream_scrap());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
