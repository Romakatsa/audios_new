import java.util.Random;
//import org.jblas.*;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.sparse.*;
import org.la4j.matrix.*;
import org.la4j.matrix.dense.*;
import org.la4j.inversion.*;

/**
 * Created by Roma on 21.05.2017.
 */
public class ImplicitMF {

    int factors = 10;
    int maxIterations = 10;
    float w0 = 0.05f;
    float reg = 0.1f;
    float init_mean = 0;
    float init_sigma = 0.01f;
    CRSMatrix ratings;
    CRSMatrix ratingsT;
    //FloatMatrix weights;
    //FloatMatrix U;
    //FloatMatrix V;
    //FloatMatrix VtV;
    //FloatMatrix UtU;
    DenseMatrix W;
    DenseMatrix V;
    DenseMatrix U;
    DenseMatrix VtV;
    DenseMatrix UtU;
    int rows;
    int cols;



    public ImplicitMF(CRSMatrix rates) {

        Random r = new Random(System.currentTimeMillis());

        this.ratings = rates;
        this.ratingsT = (CRSMatrix)rates.transpose();
        this.rows = ratings.rows();
        this.cols = ratings.columns();
        //this.weights = new FloatMatrix.zeros(rows,cols);

        W = new Basic2DMatrix(rows,cols);


        for (int i=0; i < rows; i++) {
            for (int j = 0; j< cols; j++) {
                W.set(i,j,ratings.getOrElse(i,j,0));
                //weights.put(i,j,(float)ratings.getOrElse(i,j,w0));
            }
        }

        //initialize Users And Items factors randomly
        U = DenseMatrix.random(rows,factors,r);
        V = DenseMatrix.random(cols,factors,r);

    }



       /*
        for (int k =0;k < factors;k++) {
            for (int i=0;i < rows; i++)
                U.put(i,k,init_mean + init_sigma*(float)r.nextGaussian());
            for (int j=0;j < cols; j++)
                V.put(j,k,init_mean + init_sigma*(float)r.nextGaussian());
        }
        */




    public void fit() {

        for (int it = 0;it < maxIterations; it++) {

            //VtV = Vt*V
            //V.transpose().mmuli(V,VtV);

            VtV = V.transpose().multiply(V).toDenseMatrix();
            for (int i=0;i<rows;i++) {
                update_user(i);
            }
            UtU = U.transpose().multiply(U).toDenseMatrix();
            for (int j=0;j<cols;j++) {
                update_item(j);
            }


        }

    }

    public void auc() {

    }


    private void update_user(int user_index) {

        SparseMatrix Wu = SparseMatrix.identity(cols);
        Vector Ru = ratings.getRow(user_index);
        for (int i =0;i<cols;i++) {
            Wu.set(i,i,W.get(user_index,i));
        }

        Matrix invQWQ = new GaussJordanInverter(VtV.multiply(w0).add(V.transpose().multiply(Wu.subtract(SparseMatrix.diagonal(cols,w0))).multiply(V)).add(SparseMatrix.diagonal(cols,reg))).inverse();
        Vector Ui = invQWQ.multiply(V.transpose()).multiply(Wu).multiply(Ru);

        U.setRow(user_index,Ui);

    }


    private void update_item(int item_index) {

        SparseMatrix Wi = SparseMatrix.identity(rows);
        Vector Ri = ratings.getColumn(item_index);
        for (int i =0;i<rows;i++) {
            Wi.set(i,i,W.get(i,item_index));
        }

        Matrix invPWP = new GaussJordanInverter(UtU.multiply(w0).add(U.transpose().multiply(Wi.subtract(SparseMatrix.diagonal(rows,w0))).multiply(U)).add(SparseMatrix.diagonal(rows,reg))).inverse();
        Vector Vi = invPWP.multiply(U.transpose()).multiply(Wi).multiply(Ri);

        V.setRow(item_index,Vi);

    }

    public Matrix getUsersFactors() {
        return this.U;
    }

    public Matrix getItemsFactor() {
        return this.V;
    }



}
