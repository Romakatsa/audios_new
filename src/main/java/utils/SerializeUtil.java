import java.io.*;
import java.util.ArrayList;

/**
 * Created by Roma on 22.05.2017.
 */
public class SerializeUtil {

    public static void serealizeObj(Object obj,String file_name) throws IOException {

        FileOutputStream pairs_out = new FileOutputStream(file_name);
        ObjectOutputStream pairs_oos = new ObjectOutputStream(pairs_out);
        pairs_oos.writeObject(obj);
        pairs_oos.close();

    }

    public static Object deserealizeObj(String file_name) {

        Object obj = null;
        try
        {
            FileInputStream inputFileStream = new FileInputStream(file_name);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputFileStream);
            obj  = objectInputStream.readObject();
            objectInputStream.close();
            inputFileStream.close();
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException i)
        {
            i.printStackTrace();
        }
        return obj;

    }

    public static void serealizeCoords(byte[][] choises, String filename) {

        PrintWriter f = null;
        try {
            f = new PrintWriter(new FileWriter(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0;i<choises.length;i++) {
            for(int j=0; j<choises[0].length; j++) {
                if (choises[i][j] > 0) {
                    f.println(i + "\t" + j);
                }
            }
        }
        f.close();

    }



}
