package cz.feec.vutbr.WebApp;

import java.io.*;
import java.util.ArrayList;

public class RKVAC_handler {

    protected ArrayList<String> options = new ArrayList<String>();
    protected ArrayList<String> user_input = new ArrayList<String>();
    protected final static String HOME_DIRECTORY = "/home/rkvac/rkvac_web/rkvac-protocol/build/";
    protected final static String APPLICATION = "./rkvac-protocol-multos-1.0.0";

    public RKVAC_handler(ArrayList<String> attributes, ArrayList<String> user_input) {
        this.options = attributes;
        this.user_input = user_input;
        attributes.add(APPLICATION);
    }
    public RKVAC_handler(){
        options.add(APPLICATION);
    }

    public String printResults(Process process) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        String container = "";
        while ((line = reader.readLine()) != null) {
            container = container + line + "<br />";
            System.out.println(line);
        }
        return container;
    }

    public ArrayList<String> getAttributes() {
        return options;
    }

    public void setAttributes(ArrayList<String> attributes) {
        this.options = attributes;
    }
    public void addAttributes(String attribute) {
        this.options.add(attribute);
    }

    public ArrayList<String> getUser_input() {
        return user_input;
    }

    public void setUser_input(ArrayList<String> user_input) {
        this.user_input = user_input;
    }
    public void addUser_input(String user_input) {
        this.user_input.add(user_input);
    }

    public String start() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(options);
        pb.directory(new File(HOME_DIRECTORY));
        Process process = pb.start();


        String line;
        OutputStream stdin = process.getOutputStream ();
        InputStream stderr = process.getErrorStream ();
        InputStream stdout = process.getInputStream ();

        BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        if (!user_input.isEmpty()) {
            Thread thread = new Thread() {
                public void run() {
                    System.out.println("New thread created.");
                    System.out.println("-----------WRITING------------");
                    user_input.forEach((n) -> {
                        try {
                            writer.write(n + "\n");
                            System.out.println(n);
                            writer.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            };

            thread.start();
            thread.join();
            System.out.println("---------DONE WRITING---------");
        }

        return printResults(process);
    }
}
