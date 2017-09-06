import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roma on 06.05.2017.
 */
public class SongPartition implements Serializable {


    public static List<String> get_parts(String[] words) {
        ArrayList<String> parts = new ArrayList<>();

        for (String word : words) {
            if (word.length() > 2) {
                for (int k = 0; k < word.length() - 2; k++) {
                    parts.add(word.substring(k, k + 3));
                }
            } else if (word.length() >= 2) {
                parts.add(word);
            }
        }
        return parts;
    }


    public static List<String> get_parts(String name) {
       return (ArrayList<String>)get_parts(clear(name).toLowerCase().split("\\s+"));
    }


    public static String clear(String artist) {

        artist = artist.replaceAll("\\[.*?\\]|\\(.*?\\)|\\{.*?\\}"," ");
        artist = artist.replaceAll("[^a-zA-Zа-яА-я0-9їіё]"," ");
        if (artist.length() > 2) {
            artist = artist.replaceAll("([0-9]+)"," $1");
        }
        return artist.toLowerCase();

    }

    public static boolean isSimilar(Song song1, Song song2) {

        int match = 0;
        for (String part : song1.parts) {
            if (song2.parts.contains(part)) {
                match++;
            }
        }
        float ratio = (float)match / Math.max(Math.max(song1.parts.size(), song2.parts.size()),1);
        if ( ratio >= 0.55f) {
            return true;
        }
        else {
            return false;
        }

    }


}
