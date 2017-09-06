import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Map.*;

/**
 * Created by Roma on 03.05.2017.
 */
public class Filter {

    public static ArrayList<SongListeners> group_stream(List<Pair> pairs) {

        System.out.println("0"+ new String(new char[47]).replace("\0", "-") + "100%");
        int size = pairs.size();
        int division = (int)(size/50);
        int nextdiv = size - division;

        ArrayList<SongListeners> listenersList = new ArrayList<>();
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12");
        while(!pairs.isEmpty()) {

            Pair first = pairs.get(0);
            Map<Boolean, List<Pair>> similar = pairs.stream().parallel().collect(Collectors.partitioningBy(pair -> SongPartition.isSimilar(first.song, pair.song)));
            Set<User> uids = similar.get(true).stream().map(i -> i.user).collect(Collectors.toSet());
            if (uids.size() < 5) {
                pairs = similar.get(false);
                //pairs = new ArrayList<Pair>(similar.get(false));
                continue;
            }
            Optional<Song> frequent = similar.get(true).stream().parallel().map(i -> i.song).collect(Collectors.groupingBy(s -> s, Collectors.counting())).entrySet().stream().max(Comparator.comparing(Entry::getValue)).map(i->i.getKey());
            Song song = frequent.orElse(null);
            SongListeners listeners = new SongListeners(song, uids);
            listenersList.add(listeners);
            //pairs = new ArrayList<Pair>(similar.get(false));
            pairs = similar.get(false);
            //System.out.println(pairs.size());
            if (pairs.size() < nextdiv) {
                System.out.print("-");
                nextdiv = nextdiv - division;
            }
        }

        return listenersList;

    }



}
