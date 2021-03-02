package cz.feec.vutbr.WebApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Read_file {
    public static ArrayList read(String path) throws IOException {
        ArrayList arr = new ArrayList();

        File file = new File(path);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null)
            arr.add(st+"<br />");



        return arr;
    }


}
