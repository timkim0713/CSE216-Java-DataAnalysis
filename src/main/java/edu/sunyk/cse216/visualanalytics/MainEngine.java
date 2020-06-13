package edu.sunyk.cse216.visualanalytics;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.WindowConstants;
import org.jfree.ui.RefineryUtilities;
/**
 *
 * @author Daekyung Kim (Tim Kim) for CSE 216 Assignment #1
 */
public class MainEngine {
    
    HashMap<String, List<String>> tickerMap = new HashMap<String, List<String>>(); //Creates alphabetical listing of tickers
    HashMap<String, File> fileMap = new HashMap<String, File>(); //Creates mapping of ticker and corresponding file
    private String userTicker;
    
    public static void main(String[] args) {
         
          boolean stopLoop = false;
          Scanner input=  new Scanner(System.in);

            MainEngine mainEngine = new MainEngine();
            System.out.println("Arguments are: " + args[0]);
            mainEngine.scanDir(args[0]);
          
            while(!stopLoop){
                mainEngine.tickerMap.forEach((key, value) -> System.out.println(key));
                mainEngine.userInteraction();   
                boolean validInput = false;
                System.out.println("Continue? (Y/N)");
                    while(!validInput){
                    String userContinue = input.nextLine();
                    if(userContinue.equals("N") || userContinue.equals("n")){
                        System.out.println("End of the Program");
                        stopLoop = true;
                        validInput = true;
                        break;
                    }else if(userContinue.equals("Y") || userContinue.equals("y")){ 
                        System.out.println("Continuing the Program");
                        stopLoop = false;
                        validInput = true;
                    }else{
                        System.out.println("Invalid input. Please enter in Y/N form");
                        validInput = false;
                    }
                 }
         }    
    }
    
    
    public void scanDir(String folderPath){
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                //Update tickeMap 
                updateTickerMap(file.getName().trim());
                //update fileMap
                updateFileMap(file);
            }
        }
    }
    
    private void updateTickerMap(String filename){
        StringTokenizer stkz = new StringTokenizer(filename, ".");
        List<String> tickersList = new ArrayList<>();
        if (tickerMap.containsKey(filename.substring(0, 1))) {
            tickersList = tickerMap.get(filename.substring(0, 1));
            tickersList.add(stkz.nextToken());
            tickerMap.remove(filename.substring(0, 1));
            tickerMap.put(filename.substring(0, 1), tickersList);
        } else {
            tickersList.add(stkz.nextToken());
            tickerMap.put(filename.substring(0, 1), tickersList);
        }
    }
    
    
    private void updateFileMap(File file){
        StringTokenizer stkz = new StringTokenizer(file.getName(), ".");
        fileMap.put(stkz.nextToken(), file);
    }


    private void userInteraction() {
      
            Scanner input=  new Scanner(System.in);
            boolean validInput = true;
            String userInput = null;
                while(validInput){
                    System.out.println("Enter ticker initial: ");
                    userInput = input.nextLine();

                    if(tickerMap.containsKey(userInput)){
                            for(String s : tickerMap.get(userInput)){
                             System.out.println(s);   
                            }
                            validInput = false;
                    }else{
                        System.out.println("Invalid input. Please try again");
                    }
                }   
            validInput = true;
            while(validInput){     
                System.out.println("Enter ticker code: ");
                userTicker = input.nextLine();

                if(tickerMap.get(userInput).contains(userTicker)){
                    validInput=false;
                    System.out.println("Reading File");
                    readCSVFile(userTicker, fileMap.get(userTicker));
                }else{
                    System.out.println("Invalid input. Please try again");
                }
            }
    }
    
    private boolean checkDatePattern(String input, String data) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(input);
            format.parse(data);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    
    
    private void readCSVFile(String tickerCode, File file) {
        List<String[]> userBoundData= new ArrayList<>();
        String path = file.getAbsolutePath();
        System.out.println(path);
   
        try ( CSVReader reader = new CSVReader(new FileReader(path))) {

             List<String[]> arrOfStr = reader.readAll();    // List of arrays - row/column
   
                String startD = arrOfStr.get(1)[0];
                String endD = arrOfStr.get(arrOfStr.size()-1)[0];
                     
                System.out.print("StartDate = "+startD);
                System.out.println(" EndDate = " +endD);
                
                
            Scanner input2=  new Scanner(System.in);
            boolean validDates = false;
            String userStartD = null;
            String userEndD = null;
            
            while(!validDates){
                    validDates = true;
                  
                    System.out.println("Enter valid startDate in the format YYYY-MM-DD: ");
                    userStartD = input2.nextLine();
                    while(!checkDatePattern("yyyy-MM-dd", userStartD)){
                        System.out.println("Invalid Date Format, Try Again.");
                        System.out.println("Enter valid startDate in the format YYYY-MM-DD: ");
                        userStartD = input2.nextLine();
                    }
                    
                    System.out.println("Enter valid endDate in the format YYYY-MM-DD: ");
                    userEndD = input2.nextLine();
                    while(!checkDatePattern("yyyy-MM-dd", userEndD)){
                        System.out.println("Invalid Date Format, Try Again.");
                        System.out.println("Enter valid endDate in the format YYYY-MM-DD: ");
                        userEndD = input2.nextLine();
                    }
                    
                     
                      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                     
                        Date dateStartD = formatter.parse(startD);
                        Date dateEndD = formatter.parse(endD);

                        Date startDate = formatter.parse(userStartD);
                        Date endDate = formatter.parse(userEndD);
                        
                        if(dateStartD.compareTo(startDate) >0||dateEndD.compareTo(endDate)<0||
                           startDate.compareTo(dateEndD)>0||endDate.compareTo(dateStartD) <0||startDate.compareTo(endDate)>0){
                            System.out.println("Invalid Date Input, Try Again.");
                            validDates = false;
                        }
                     
            }

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateStringStart = userStartD + " 00:00:00";
    String dateStringEnd = userEndD + " 00:00:00";

    try {
      
        Date startDate = formatter.parse(dateStringStart);
        Date endDate = formatter.parse(dateStringEnd);
        System.out.println("startDate = " + startDate + " EndDate = " + endDate) ;
         
        int startIndex=-1;
        int endIndex=-1;
        DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        for(int x=1;x< arrOfStr.size()-1;x++){
                    
                    Date current = formatter2.parse(arrOfStr.get(x)[0]);
                    Date cnext = formatter2.parse(arrOfStr.get(x+1)[0]);
                            if(current.compareTo(startDate) >= 0 && cnext.compareTo(startDate)==1){
                                startIndex= x;
                                break;
                            }      
         }
        if(startIndex==-1){ //special case for the the last index.
            startIndex = arrOfStr.size()-1;
            endIndex = arrOfStr.size()-1;
        }
         for(int x=startIndex;x< arrOfStr.size()-1;x++){
                    Date current = formatter2.parse(arrOfStr.get(x)[0]);
                    Date next =formatter2.parse(arrOfStr.get(x+1)[0]);
                            if(current.compareTo(endDate) >= 0 && next.compareTo(endDate)==1){
                                if(current.compareTo(endDate)==0){
                                    endIndex = x;
                                }else{
                                endIndex= x-1;
                                }
                                break;
                            }  
         }
            //This list is the data the user selected - all the cases covered..
           try{
                for(int x=startIndex;x<=endIndex; x++){
                    userBoundData.add(arrOfStr.get(x));
                }  
           }catch(Exception c){
              System.out.println("The given dates are not valid"); 
           }
     
    } catch (ParseException e) {
    }
       System.out.println("Enter chart type number from the following: ");
       System.out.println("1. High-Low chart  ");
       System.out.println("2. Open-High-Low-Close chart  ");
       System.out.println("3. Volume Chart  ");
       
       String userChartAnswer = input2.nextLine();
       boolean valid = true;
       
       while(valid){
           
            valid = false;
                 switch (userChartAnswer) {
                     case "1":
                         //high low chart
                         System.out.println(userChartAnswer);
                               HighLowChart demo = new HighLowChart("High Low Chart | " + userTicker, userBoundData);
                               demo.pack();
                               RefineryUtilities.centerFrameOnScreen(demo);
                               demo.setVisible(true);

                         break;
                     case "2":
                         //ohlcc chart
                         System.out.println(userChartAnswer);
                                 OHLCChart example = new OHLCChart("OHLC Stock Chart | "+ userTicker, userBoundData, userTicker);
                                 example.setSize(800, 400);
                                 example.setLocationRelativeTo(null);
                                 example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                                 example.setVisible(true);

                         break;
                     case "3":
                         //volume chart
                         System.out.println(userChartAnswer);
                             VolumeChart vc = new VolumeChart("Volume Chart | "+ userTicker, userBoundData);
                             vc.pack();
                             RefineryUtilities.centerFrameOnScreen(vc);
                             vc.setVisible(true);
                         break;
                     default:
                            System.out.println("Not a match. Please Try again.");
                            System.out.println("Enter chart type number from the following: ");
                            System.out.println("1. High-Low chart  ");
                            System.out.println("2. Open-High-Low-Close chart  ");
                            System.out.println("3. Volume Chart  ");
                            userChartAnswer = input2.nextLine();
                            valid = true;
                         
                 }
        }
        }  catch (Exception ex) {
            Logger.getLogger(MainEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
        
   
        
        
 }

