import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Roma on 09.05.2017.
 */
public class SongListeners  implements Serializable{

    Song song;
    HashSet<User> uids;

    public SongListeners(Song song, Set<User> uids) {
        this.song = new Song(song);
        this.uids = new HashSet<User>(uids);
    }

}

