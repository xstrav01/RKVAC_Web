package cz.feec.vutbr.WebApp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


@Controller
public class WebController {
    @GetMapping("/personalization")
    public String getPersonalization(Model model) throws IOException {
        ArrayList arr;
        arr = Read_file.read("/home/rkvac/rkvac_web/rkvac-protocol/build/data/Issuer/user_list.csv");
        //arr = Read_file.read("C:\\Users\\Bromy\\Documents\\user_list.txt");
        model.addAttribute("list",arr);
        return "personalization";
    }

    @GetMapping("/issue-attributes")
    public String getAttributes(Model model) throws IOException {
        ArrayList arr;
        arr = Read_file.read("/home/rkvac/rkvac_web/rkvac-protocol/build/data/Issuer/user_list.csv");
        //arr = Read_file.read("C:\\Users\\Bromy\\Documents\\user_list.txt");
        model.addAttribute("list",arr);
        return "issue-attributes";
    }

    @GetMapping("/issue-attributes-ticket")
    public String getAttributes_ticket(Model model) throws IOException {
        ArrayList arr;
        arr = Read_file.read("/home/rkvac/rkvac_web/rkvac-protocol/build/data/Issuer/user_list.csv");
        //arr = Read_file.read("C:\\Users\\Bromy\\Documents\\user_list.txt");
        model.addAttribute("list",arr);
        return "issue-attributes-ticket";
    }

    @GetMapping("/issue-attributes-eid")
    public String getAttributes_eid(Model model) throws IOException {
        ArrayList arr;
        arr = Read_file.read("/home/rkvac/rkvac_web/rkvac-protocol/build/data/Issuer/user_list.csv");
        //arr = Read_file.read("C:\\Users\\Bromy\\Documents\\user_list.txt");
        model.addAttribute("list",arr);
        return "issue-attributes-eid";
    }

    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/RA")
    public String getRA(Model model) throws IOException {
        ArrayList arr;
        arr = Read_file.read("/home/rkvac/rkvac_web/rkvac-protocol/build/data/RA/ra_rh.csv");
        //arr = Read_file.read("C:\\Users\\Bromy\\Documents\\test.txt");
        model.addAttribute("list",arr);

        return "RA";
    }

    @GetMapping("/Verifier")
    public String getVerifier(Model model) throws IOException {
        ArrayList arr;
        arr = Read_file.read("/home/rkvac/rkvac_web/rkvac-protocol/build/data/Verifier/ve_requests.log");
        //arr = Read_file.read("C:\\Users\\Bromy\\Documents\\user_list.txt");
        model.addAttribute("list",arr);
        return "verifier";
    }



    @GetMapping("/access-allowed")
    public String getAllowed() {
        return "access_allowed";
    }

    @GetMapping("/access-denied")
    public String getDenied() {
        return "access_denied";
    }

    @GetMapping("/identity-check")
    public String getICheck() {
        return "identity-check";
    }

    @GetMapping("/identity-check-ticket")
    public String getICheck_ticket() {
        return "identity-check-ticket";
    }

    @GetMapping("/identity-check-eid")
    public String getICheck_eid() {
        return "identity-check-eid";
    }

    @GetMapping("/success")
    public String getSuccess() {
        return "success";
    }

    @GetMapping("/fail")
    public String getFail() {
        return "fail";
    }




    @RequestMapping(value = "personalisation", method = {RequestMethod.GET, RequestMethod.POST})
    public String personalization(@RequestParam String firstname, @RequestParam String lastname, Model model) throws IOException, InterruptedException {

        RKVAC_handler rkvac = new RKVAC_handler();

        rkvac.addAttributes("-p");
        rkvac.addUser_input(firstname);
        rkvac.addUser_input(lastname);

        String container = rkvac.start();

        if (!container.contains("Card personalization successful")){
            model.addAttribute("container", container);
            return "fail";
        }

        model.addAttribute("url","/personalization");
        return "success";
    }

    @GetMapping("/RA-handlers")
    public String ra_handlers(Model model, Model list) throws IOException, InterruptedException {

        RKVAC_handler rkvac = new RKVAC_handler();

        rkvac.addAttributes("-r");

        String container = rkvac.start();

        if (!container.contains("Generate pseudonyms for current revocation handler")){
            model.addAttribute("container", container);
            return "fail";
        }
        model.addAttribute("url","/RA");
        return "success";
    }




    @RequestMapping(value = "issuing-attributes", method = {RequestMethod.GET, RequestMethod.POST})
    public String issuing_attributes(@RequestParam String names, @RequestParam String employer, @RequestParam String employee_id, @RequestParam String employee_position, Model model) throws IOException, InterruptedException {
        String write = "N";
        String database = "web_cache";

        RKVAC_handler rkvac = new RKVAC_handler();

        rkvac.addAttributes("-i");
        rkvac.addAttributes("-a");
        rkvac.addAttributes(database);

        rkvac.addUser_input("3");
        rkvac.addUser_input(names);
        rkvac.addUser_input(employee_id);
        rkvac.addUser_input(employer);
        rkvac.addUser_input(employee_position);
        rkvac.addUser_input(write);


        String container = rkvac.start();

        //Search for verify cardholder line + err handling
        //Scanner scanner = new Scanner(container);
        //String test = "err";
        //while (scanner.hasNextLine()) {
        //    String line = scanner.nextLine();
       //     if (line.contains("Verify cardholder")) {
        //        model.addAttribute("line", line);
        //        test = line;
        //    }
        //}
        //scanner.close();

        String line = "err";
        line = StringUtils.substringBetween(container, "Verify cardholder", "identity");
        model.addAttribute("line", line);

        if (line == "err") {
            model.addAttribute("container", container);
            return "fail";
        }

        //kopirovani databaze
        //Copy_File cf = new Copy_File("/home/rkvac/rkvac_web/rkvac-protocol/build/data/Issuer/"+database,"/home/rkvac/rkvac_web/rkvac-protocol/build/data/Verifier/"+database);
        //cf.start();

        model.addAttribute("database",database);
        model.addAttribute("names",names);
        model.addAttribute("employee_id",employee_id);
        model.addAttribute("employer",employer);
        model.addAttribute("employee_position",employee_position);

        System.out.println("Deleting ..Issuer/" + database);
        File myObj = new File("/home/rkvac/rkvac_web/rkvac-protocol/build/data/Issuer/"+database);
        myObj.delete();

        return "identity-check";
    }

    @RequestMapping(value = "issuing-attributes-ecard", method = {RequestMethod.GET, RequestMethod.POST})
    public String issuing_attributes_ecard(@RequestParam String names, @RequestParam String employer, @RequestParam String employee_id, @RequestParam String employee_position, @RequestParam String database, Model model) throws IOException, InterruptedException {
        String write = "Y";


        RKVAC_handler rkvac = new RKVAC_handler();

        rkvac.addAttributes("-i");
        rkvac.addAttributes("-a");
        rkvac.addAttributes(database);

        rkvac.addUser_input("3");
        rkvac.addUser_input(names);
        rkvac.addUser_input(employee_id);
        rkvac.addUser_input(employer);
        rkvac.addUser_input(employee_position);
        rkvac.addUser_input(write);


        String container = rkvac.start();


        //kopirovani databaze
        //Copy_File cf = new Copy_File("/home/rkvac/rkvac_web/rkvac-protocol/build/data/Issuer/"+database,"/home/rkvac/rkvac_web/rkvac-protocol/build/data/Verifier/"+database);
        //cf.start();


        if (!container.contains("Issuer part complete")){
            model.addAttribute("container", container);
            return "fail";
        }

        model.addAttribute("url","/issue-attributes");
        return "success";

    }

    @RequestMapping(value = "issuing-attributes-ticket", method = {RequestMethod.GET, RequestMethod.POST})
    public String issuing_attributes_ticket(@RequestParam String names, @RequestParam String cnumber, @RequestParam String ticket, Model model) throws IOException, InterruptedException {
        String write = "N";
        String database = "web_cache";

        RKVAC_handler rkvac = new RKVAC_handler();

        rkvac.addAttributes("-i");
        rkvac.addAttributes("-a");
        rkvac.addAttributes(database);

        rkvac.addUser_input("2");
        rkvac.addUser_input(names);
        rkvac.addUser_input(cnumber);
        rkvac.addUser_input(ticket);
        rkvac.addUser_input(write);


        String container = rkvac.start();


        String line = "err";
        line = StringUtils.substringBetween(container, "Verify cardholder", "identity");
        model.addAttribute("line", line);

        if (line == "err") {
            model.addAttribute("container", container);
            return "fail";
        }

        //kopirovani databaze
        //Copy_File cf = new Copy_File("/home/rkvac/rkvac_web/rkvac-protocol/build/data/Issuer/"+database,"/home/rkvac/rkvac_web/rkvac-protocol/build/data/Verifier/"+database);
        //cf.start();

        model.addAttribute("database",database);
        model.addAttribute("names",names);
        model.addAttribute("ticket",ticket);
        model.addAttribute("cnumber",cnumber);


        System.out.println("Deleting ..Issuer/" + database);
        File myObj = new File("/home/rkvac/rkvac_web/rkvac-protocol/build/data/Issuer/"+database);
        myObj.delete();

        return "identity-check-ticket";
    }

    @RequestMapping(value = "issuing-attributes-ticket-ver", method = {RequestMethod.GET, RequestMethod.POST})
    public String issuing_attributes_ticket_ver(@RequestParam String names, @RequestParam String cnumber, @RequestParam String ticket, @RequestParam String database, Model model) throws IOException, InterruptedException {
        String write = "Y";

        RKVAC_handler rkvac = new RKVAC_handler();

        rkvac.addAttributes("-i");
        rkvac.addAttributes("-a");
        rkvac.addAttributes(database);

        rkvac.addUser_input("2");
        rkvac.addUser_input(names);
        rkvac.addUser_input(cnumber);
        rkvac.addUser_input(ticket);
        rkvac.addUser_input(write);


        String container = rkvac.start();


        if (!container.contains("Issuer part complete")){
            model.addAttribute("container", container);
            return "fail";
        }

        model.addAttribute("url","/issue-attributes-ticket");
        return "success";
    }

    @RequestMapping(value = "issuing-attributes-eid", method = {RequestMethod.GET, RequestMethod.POST})
    public String issuing_attributes_eid(@RequestParam String names, @RequestParam String birthdate, @RequestParam String nationality, @RequestParam String residence, @RequestParam String sex, Model model) throws IOException, InterruptedException {
        String write = "N";
        String database = "web_cache";

        RKVAC_handler rkvac = new RKVAC_handler();

        rkvac.addAttributes("-i");
        rkvac.addAttributes("-a");
        rkvac.addAttributes(database);

        rkvac.addUser_input("1");
        rkvac.addUser_input(names);
        rkvac.addUser_input(birthdate);
        rkvac.addUser_input(nationality);
        rkvac.addUser_input(residence);
        rkvac.addUser_input(sex);
        rkvac.addUser_input(write);


        String container = rkvac.start();


        String line = "err";
        line = StringUtils.substringBetween(container, "Verify cardholder", "identity");
        model.addAttribute("line", line);

        if (line == "err") {
            model.addAttribute("container", container);
            return "fail";
        }

        //kopirovani databaze
        //Copy_File cf = new Copy_File("/home/rkvac/rkvac_web/rkvac-protocol/build/data/Issuer/"+database,"/home/rkvac/rkvac_web/rkvac-protocol/build/data/Verifier/"+database);
        //cf.start();

        model.addAttribute("database",database);
        model.addAttribute("names",names);
        model.addAttribute("birthdate",birthdate);
        model.addAttribute("nationality",nationality);
        model.addAttribute("residence", residence);
        model.addAttribute("sex", sex);


        System.out.println("Deleting ..Issuer/" + database);
        File myObj = new File("/home/rkvac/rkvac_web/rkvac-protocol/build/data/Issuer/"+database);
        myObj.delete();

        return "identity-check-eid";
    }

    @RequestMapping(value = "issuing-attributes-eid-ver", method = {RequestMethod.GET, RequestMethod.POST})
    public String issuing_attributes_eid_ver(@RequestParam String names, @RequestParam String birthdate, @RequestParam String nationality, @RequestParam String residence, @RequestParam String database, @RequestParam String sex, Model model) throws IOException, InterruptedException {
        String write = "Y";

        RKVAC_handler rkvac = new RKVAC_handler();

        rkvac.addAttributes("-i");
        rkvac.addAttributes("-a");
        rkvac.addAttributes(database);

        rkvac.addUser_input("1");
        rkvac.addUser_input(names);
        rkvac.addUser_input(birthdate);
        rkvac.addUser_input(nationality);
        rkvac.addUser_input(residence);
        rkvac.addUser_input(sex);
        rkvac.addUser_input(write);


        String container = rkvac.start();


        if (!container.contains("Issuer part complete")){
            model.addAttribute("container", container);
            return "fail";
        }

        model.addAttribute("url","/issue-attributes-eid");
        return "success";
    }



    @RequestMapping(value = "verify-attributes", method = {RequestMethod.GET, RequestMethod.POST})
    public String verify_attributes(@RequestParam String names, @RequestParam String employer, @RequestParam String employee_id, @RequestParam String employee_position, @RequestParam String number, @RequestParam String database) throws IOException, InterruptedException {

        RKVAC_handler rkvac = new RKVAC_handler();

        rkvac.addAttributes("-v");
        rkvac.addAttributes("-a");
        rkvac.addAttributes(database);

        //kdyz databaze existuje nacti atributy z ni
        File f = new File("/home/rkvac/rkvac_web/rkvac-protocol/build/data/Verifier/"+database);
        if(f.exists() && !f.isDirectory()) {
            rkvac.addUser_input(number);
        }
        else {
            rkvac.addUser_input("3");
            rkvac.addUser_input(names);
            rkvac.addUser_input(employee_id);
            rkvac.addUser_input(employer);
            rkvac.addUser_input(employee_position);
            rkvac.addUser_input(number);

        }
        String container = rkvac.start();

        return "done";
    }

    @RequestMapping(value = "verify-ecard", method = {RequestMethod.GET, RequestMethod.POST})
    public String verify_ecard(Model model) throws IOException, InterruptedException {
        String database = "ecard";

        RKVAC_handler rkvac = new RKVAC_handler();

        rkvac.addAttributes("-v");
        rkvac.addAttributes("-a");
        rkvac.addAttributes(database);

        rkvac.addUser_input("4");

        String container = rkvac.start();
        if (!container.contains("ACCESS ALLOWED")){
            model.addAttribute("container", container);
            return "access_denied";
        }
        return "access_allowed";
    }

    @RequestMapping(value = "verify-ticket", method = {RequestMethod.GET, RequestMethod.POST})
    public String verify_ticket(Model model) throws IOException, InterruptedException {
        String database = "ticket";

        RKVAC_handler rkvac = new RKVAC_handler();

        rkvac.addAttributes("-v");
        rkvac.addAttributes("-a");
        rkvac.addAttributes(database);

        rkvac.addUser_input("3");

        String container = rkvac.start();

        if (!container.contains("ACCESS ALLOWED")){
            model.addAttribute("container", container);
            return "access_denied";
        }
        return "access_allowed";
    }

    @RequestMapping(value = "verify-eid", method = {RequestMethod.GET, RequestMethod.POST})
    public String verify_eid(Model model) throws IOException, InterruptedException {
        String database = "eid";

        RKVAC_handler rkvac = new RKVAC_handler();

        rkvac.addAttributes("-v");
        rkvac.addAttributes("-a");
        rkvac.addAttributes(database);

        rkvac.addUser_input("3");

        String container = rkvac.start();
        if (!container.contains("ACCESS ALLOWED")){
            model.addAttribute("container", container);
            return "access_denied";
        }
        return "access_allowed";
    }


}