import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roma on 09.05.2017.
 */
public class Pair implements Serializable{

    User user;
    Song song;

    public Pair(User user,Song song) {
        this.user = new User(user);
        this.song = new Song(song);
    }


}
