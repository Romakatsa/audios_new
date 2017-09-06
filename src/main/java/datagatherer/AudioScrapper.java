import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Roma on 09.05.2017.
 */
public class AudioScrapper {

    public static final String URL = "https://vk.com/al_audio.php";
    public static final String file_in = "src/users.txt";
    public static final String file_out = "src/artists.txt";
    static String format = "access_hash=&act=load_section&al=1&claim=0&offset=1&owner_id=%s&playlist_id=-1&type=playlist";

    public static int stream_scrap() throws IOException {

        int total = 0;
        long timeStart = System.currentTimeMillis();

        List<String> uids = new ArrayList<String>();
        List<String> used_uids = null;
        BufferedReader br = new BufferedReader(new FileReader(new File(file_in).getAbsoluteFile()));

        try (LineNumberReader lnr = new LineNumberReader(br)) {
            for (String line; (line = lnr.readLine()) != null; ) {

                if (lnr.getLineNumber() <= 0000) {
                    continue;
                }
                if (lnr.getLineNumber() > 10000) {
                    break;
                }
                uids.add(line);
            }
        }

        URL obj = new URL(URL);

        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "10");
        List<Pair> pairs = IntStream.range(0,uids.size()).parallel().boxed().flatMap(i ->
                getUserList(uids.get(i),obj).stream()).collect(Collectors.toList());


        System.out.println(pairs.size());
        ArrayList<SongListeners> songListeners = Filter.group_stream(pairs);
        long timeEnd = System.currentTimeMillis();

        //System.out.println("after grouping = "+ songListeners.size());
        System.out.println("time = "+ (timeEnd-timeStart));

        //return pairs.size();
        return 1;
    }

    /*
    public static int popularity(String song_name, List<SongListeners> songListeners) {


        SongPartition search = new SongPartition(song_name,Pair.get_parts(Pair.clear(song_name).toLowerCase().split("\\s+")));
        IntStream.range(0, songListeners.size()).parallel().mapToObj(i-> songListeners.get(i))
                .filter(i-> Filter.isSimilar(search,new SongPartition(i.song))).forEach(i-> System.out.println("song: " + i.song + " popular: "+ i.uids.size()));

        return 1;
    }
    */

    public static ArrayList<Pair> getUserList(String uid, URL obj) {
        ArrayList<Pair> pairs = new ArrayList<>();
        HttpURLConnection con = null;
        StringBuffer response = new StringBuffer();
        try {
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            con.setRequestProperty("Cookie", "remixsid=eeae07c31408f331273d08b0a0a5dcebc5b719cfb97ca225482b1");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Content-Length", "98");
            con.setRequestProperty("Host", "vk.com");

            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(String.format(format, uid));

            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"windows-1251" ));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        }
        catch (IOException e) {
            //return pairs;
        }
        finally {
            if (con != null) {
                con.disconnect();
            }
        }

        int start = response.indexOf("[[");
        int end = response.lastIndexOf("]]");
        if (start < 0 || end < 0)
            return pairs;

        String audio_list = response.substring(start + 1, end+1);
        //System.out.println(audio_list);
        int totalCountIndex = response.indexOf("totalCount");
        int totalCount = Integer.parseInt(response.substring(totalCountIndex + 12, response.indexOf(",", totalCountIndex + 12)));

        pairs = ListParser.stream_parse(audio_list, totalCount, uid);

        return pairs;

    }




}
