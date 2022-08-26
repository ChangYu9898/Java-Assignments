
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Multi-threaded server program that multiplies matrices using fork-join
 * framework.
 *
 * @author vanting
 */
public class Server implements Runnable {

    private Socket socket;

    public Server() {
    }

    public Server(Socket socket) {
        this.socket = socket;
    }

    /**
     * Driver function. Start this server at port 33333.
     */
    public static void main(String[] args) {
        start(33333);
    }

    /**
     * Start matrix server at the specified port. It should accept and handle
     * multiple client requests concurrently.
     *
     * @param port port number listened by the server
     */
    public static void start(int port) {

        // your implementation here
        ServerSocket serverSocket = null;
        boolean listening = true;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 33333.");
            System.exit(-1);
        }
        while (listening) {
            try {
                new Thread(new Server56858552(serverSocket.accept())).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


    /**
     * Handle a matrix client request. It reads two matrices from socket,
     * compute their product, and then send the product matrix back to the
     * client.
     */

    @Override
    public void run() {

        // your implementation here
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        Matrix matA = new Matrix();
        Matrix matB = new Matrix();
        Matrix product = new Matrix();
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            matA = (Matrix) in.readObject();
            matB = (Matrix) in.readObject();
            product = multiThreadMultiply(matA, matB);
            out.writeObject(product);
            /*System.out.println(matA);
            System.out.println(matB);
            System.out.println(matA.equals(matB));*/
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Compute A x B using fork-join framework.
     *
     * @param matA matrix A
     * @param matB matrix B
     * @return the matrix product of AxB
     */
    public static Matrix multiThreadMultiply(Matrix matA, Matrix matB) {

        Matrix product = null;
        // your implementation here
        Matrix[] product_buffer =new Matrix[1];
        ParallelMultiply task = new ParallelMultiply(matA, matB, product_buffer);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(task);
        product = product_buffer[0];
        return product;
    }
}

/**
 * Design a recursive and resultless ForkJoinTask. It splits the matrix multiplication
 * into multiple tasks to be executed in parallel.
 *
 */
class ParallelMultiply extends RecursiveAction {

    // your implementation here
    private Matrix A;
    private Matrix B;
    private Matrix[] product;

    private long[][] buffer;

    public ParallelMultiply(Matrix a, Matrix b, Matrix[] c) {
        A = a;
        B = b;
        product = c;
    }

    @Override
    protected void compute() {
        buffer = new long[A.row()][B.col()];
        ArrayList<RecursiveAction> multitasks = new ArrayList<RecursiveAction>();
        for (int i = 0; i < buffer.length; i++)
            for (int j = 0; j < buffer[0].length; j++)
                multitasks.add(new computeOneRow(i, j));
        invokeAll(multitasks);
        product[0]=new Matrix(buffer);
    }
    public class computeOneRow extends RecursiveAction {
        int i,j;
        public computeOneRow(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public void compute() {
            for (int k = 0; k < A.col(); k++)
                buffer[i][j]+= A.at(i,k) * B.at(k,j);
        }
    }
}



