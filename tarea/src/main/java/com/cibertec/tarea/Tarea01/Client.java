package com.cibertec.tarea.Tarea01;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
public class Client {
    private static final int PORT =13;
    private static final String HOST ="localhost";

    public Client(){
        System.out.println("1|Clinet constructot " + PORT);
        try  {
            System.out.println("1|Connecting to server " + PORT);
            Socket socket = new Socket(HOST , PORT);
            System.out.println("2|Connected to server " + PORT);

            File[] files = new File("C:\\Users\\USER\\Desktop\\cibertec\\6 ciclo\\Desarrollo de servicios web 2\\fotos").listFiles();
            Random random = new Random();
            int index = random.nextInt(files.length);
            File file = files[index];

            System.out.println("Enviando archivo: " + file.getAbsolutePath());
                 //Flujo de datos de entrada y salida
            FileInputStream fis = new FileInputStream(file);
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());

                        //Envio de archivo
                        byte[] buffer = new byte[1024];
                        int count;
                        while ((count = fis.read(buffer)) > 0) {
                            salida.write(buffer, 0, count);
                        }
                        fis.close();
                        
                        System.out.println("Archivo enviado: " + file.getAbsolutePath());
            System.out.println("4>> Final for client ... "  );
            socket.close();
        
        } catch (IOException e) {
        // TODO: handle exception
            e.printStackTrace();
        } 
    }
    
    public static void main(String[] args) {
        new Client();
    }
}

