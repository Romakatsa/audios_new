import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roma on 17.05.2017.
 */
public class Song {

    public Song(String name, List<String> parts) {
        this.name = name;
        this.parts = new ArrayList<>(parts);
    }

    public Song(String name, List<String> parts, int lang) {
        this.name = name;
        this.parts = new ArrayList<>(parts);
        this.lang = lang;
    }

    public Song(Song song) {
        this.name = song.name;
        this.lang = song.lang;
        this.parts = new ArrayList<>(song.parts);
    }


    @Override
    public boolean equals(Object s) {

        if (!(s instanceof Song)) {
            return false;
        }
        Song song = (Song) s;
        return this.name.equals(song.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }


    String name;
    ArrayList<String> parts;
    int lang; //0-unknown, 1-eng, 2-rus, 3-ukr

}
