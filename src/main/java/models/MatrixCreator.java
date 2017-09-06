import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.la4j.matrix.sparse.*;

/**
 * Created by Roma on 10.05.2017.
 */
public class MatrixCreator {


    public static CRSMatrix getSparceChoisesMatrix(byte[][] choisesMatrix) {

        CRSMatrix sparseChoisesMatrix = new CRSMatrix(choisesMatrix[0].length,choisesMatrix.length);

        for (int u =0;u < choisesMatrix[0].length; u++) {
            for (int i = 0; i < choisesMatrix.length; i++) {
                if (choisesMatrix[i][u] == 1) {
                    sparseChoisesMatrix.set(u, i, 1); // transpose original
                }
            }
        }

        //return sparse transposed choisesMatrix
        return sparseChoisesMatrix;



    }


    public static CRSMatrix getSparseChoisesMatrix(List<SongListeners> listenersList) {

        long startMatrix = System.currentTimeMillis();

        BiMap<Integer, String> usersBiMap = HashBiMap.create();
        //BiMap<String, Integer> songsBiMap = HashBiMap.create();
        Set<User> uniqueUsers = listenersList.stream().flatMap(i->i.uids.stream()).collect(Collectors.toSet());
        int userIndex = 0;
        int choises = 0;


        try {
            SerializeUtil.serealizeObj(usersBiMap,"userBiMap");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //int[][] choisesMatrix = new int[listenersList.size()][uniqueUsers.size()];

        for(User user:uniqueUsers) {
            usersBiMap.put(userIndex,user.uid);
            userIndex++;
        }

        //FlexCompRowMatrix choisesMatrix = new FlexCompRowMatrix(listenersList.size(),uniqueUsers.size());

        CRSMatrix choisesMatrix = new CRSMatrix(listenersList.size(),uniqueUsers.size());

        int songIndex = 0;
        for(SongListeners listeners : listenersList) {
            for(User uid:listeners.uids) {
                choisesMatrix.set(songIndex,usersBiMap.inverse().get(uid.uid),1);
                choises++;
            }
            songIndex++;
        }



        long endMatrix = System.currentTimeMillis();
        System.out.println("Matrix created "+ (endMatrix-startMatrix) + "millisec's.");

        return choisesMatrix;

    }


    public static int[][] getChoisesMatrix(List<SongListeners> listenersList) {

        long startMatrix = System.currentTimeMillis();

        BiMap<Integer, String> usersBiMap = HashBiMap.create();
        //BiMap<String, Integer> songsBiMap = HashBiMap.create();
        Set<User> uniqueUsers = listenersList.stream().flatMap(i->i.uids.stream()).collect(Collectors.toSet());
        int userIndex = 0;
        int choises = 0;

        int[][] choisesMatrix = new int[uniqueUsers.size()][listenersList.size()];

        for(User user:uniqueUsers) {
            usersBiMap.put(userIndex,user.uid);
            userIndex++;
        }

        int songIndex = 0;
        for(SongListeners listeners : listenersList) {
            for(User uid:listeners.uids) {
                choisesMatrix[songIndex][usersBiMap.inverse().get(uid.uid)] = 1;
                choises++;
            }
            songIndex++;
        }

        long endMatrix = System.currentTimeMillis();

        System.out.println("Matrix created "+ (endMatrix-startMatrix) + "millisec's.");

        return choisesMatrix;

    }

    public static int[][] getFeautureDecomposition(List<SongListeners> listenersList, List<SongListeners> dislikesList, int totalRatings) {

        int userFeautures = 1;
        int itemFeautures = 1;
        long startMatrix = System.currentTimeMillis();

        BiMap<Integer, String> usersBiMap = HashBiMap.create();
        //BiMap<String, Integer> songsBiMap = HashBiMap.create();
        Set<User> uniqueUsers = listenersList.stream().flatMap(i->i.uids.stream()).collect(Collectors.toSet());
        int userIndex = 0;


        int songsSize = listenersList.size();
        int usersSize = uniqueUsers.size();


        int[][] choisesMatrix = new int[totalRatings][songsSize+usersSize+userFeautures+itemFeautures+1]; //1 stands for target vector

        for(User user:uniqueUsers) {
            usersBiMap.put(userIndex,user.uid);
            userIndex++;
        }
        int ratingIndex = 0;
        int songIndex = 0;
        for(SongListeners listeners : listenersList) {
            for(User uid:listeners.uids) {
                choisesMatrix[ratingIndex][songIndex] = 1;
                choisesMatrix[ratingIndex][songsSize + usersBiMap.inverse().get(uid.uid)] = 1;
                choisesMatrix[ratingIndex][songsSize + usersSize] = uid.sex;
                //choisesMatrix[ratingIndex][songsSize + usersSize + uid.sex] = 1;
                choisesMatrix[ratingIndex][songsSize + usersSize + userFeautures] = listeners.song.lang;
                //choisesMatrix[ratingIndex][songsSize + usersSize + userFeautures + listeners.song.lang] = 1;
                choisesMatrix[ratingIndex][songsSize+usersSize+userFeautures+itemFeautures +1 -1] = 1; //liked
                ratingIndex++;
            }
            songIndex++;
        }

        long endMatrix = System.currentTimeMillis();

        System.out.println("Matrix created "+ (endMatrix-startMatrix) + "millisec's.");

        return choisesMatrix;

    }

    public static List<byte[][]> splitMatrix(byte[][] choisesMatrix, float testPortion) {

        List<byte[][]> samples = new ArrayList<>(2);
        byte[][] trainset;
        byte[][] testset;

            trainset = new byte[choisesMatrix.length][choisesMatrix[0].length];
            testset = new byte[choisesMatrix.length][choisesMatrix[0].length];

            for (int u =0;u < choisesMatrix[0].length; u++) {
                for (int i=0; i < choisesMatrix.length;i++) {
                    if (choisesMatrix[i][u] == 1) {
                        if (Math.random() < testPortion) {
                            testset[i][u] = 1;
                        }
                        else {
                            trainset[i][u] = 1;
                        }
                    }
                }
            }
        samples.add(trainset);
        samples.add(testset);
        return samples;
    }









}
