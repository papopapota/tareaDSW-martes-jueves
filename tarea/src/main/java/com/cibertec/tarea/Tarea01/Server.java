package com.cibertec.tarea.Tarea01;
import java.beans.Statement;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    private static final int PORT = 13;
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/bd_imagen";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "admin";
    public Server(){
          System.out.println("1 >> [ini] Server constructor");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
                System.out.println("2>>waiting for the client ..");
                while (true) {
                    Socket clienSocket = serverSocket.accept();
                    System.out.println("3>> acepted client connection");
                    
                    //Generar nombre del archivo
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_mm_ss");
                    String nombreArchivo = "foto_" + sdf.format(new Date()) + ".png";
                    String rutaArchivo = "tarea\\src\\main\\java\\com\\cibertec\\tarea\\Tarea01\\" +nombreArchivo;
                    File file = new File(rutaArchivo);
                    System.out.println("Archivo creado : " + file.getAbsolutePath());
                    
                    // flujo de entrada y salida 
                    FileOutputStream fos = new FileOutputStream(file);
                    DataInputStream entrada = new DataInputStream(clienSocket.getInputStream());

                    //recibiendo archivo
                    byte[] buffer = new byte[1024];
                    int count ;
                    while ((count = entrada.read(buffer))>0) {
                        fos.write(buffer , 0 , count);
                    }
                    fos.close();
                    System.out.println("Archivo recibido: " + file.getAbsolutePath());
                    guardarDatos(nombreArchivo , Integer.parseInt(file.length() +"")  ,entrada );
                    System.out.println("4>> final for the client");
                    clienSocket.close();
                }
              
               
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } 

    }
    public void guardarDatos(String nombre  , int tamano ,DataInputStream  archivo){
        try{
            Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            System.out.println("Conexion exitosa con la BD");
            String sql = "Insert into tb_imagen (nombre , tamano, archivo) values  (?,?,?) ";
            PreparedStatement pStatement = con.prepareStatement(sql);
            //pStatement.setInt(1, 1);
            pStatement.setString(1, nombre);
            pStatement.setInt(2, tamano);
            pStatement.setBlob(3, archivo);

            int lineasInsertadas = pStatement.executeUpdate();
            if (lineasInsertadas>0) {
                System.out.println("Insert exito");
            }
            pStatement.close();
            con.close();
        } catch (Exception e) {
            // TODO: handle Exception
            e.printStackTrace();
        }finally{
            

        }
    }
    public static void main(String[] args) {
        new Server();
    }
}
