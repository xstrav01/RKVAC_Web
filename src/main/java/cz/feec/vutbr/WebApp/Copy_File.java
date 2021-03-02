package cz.feec.vutbr.WebApp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Copy_File {
    protected String source = "";
    protected String destination = "";

    public Copy_File(String source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void start(){
        try (var fis = Files.newInputStream(Path.of(source));
             var fos = Files.newOutputStream(Path.of(destination))) {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0) {

                fos.write(buffer, 0, length);
            }
            System.out.println("-----------------------------COPYING FILE-----------------------------");
            System.out.println("source: " + source);
            System.out.println("desination: " + destination);
            System.out.println("----------------------------------DONE---------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
