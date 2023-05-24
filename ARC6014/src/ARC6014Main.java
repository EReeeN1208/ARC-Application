import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Ref;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

/************************************************

 ARC#6014 Programming Challenge

 Author: Eren Kural

 Date: 14 May 2023

 "╓╴ "
 "╟╴ "
 "╙╴ "
 ************************************************/


public class ARC6014Main {

    public static void main(String[] Args) {

        //INITIALIZATION
        Scanner Input = new Scanner(System.in);

        DecimalFormat decFormat = new DecimalFormat("000");

        File InputCSV = new File("Materials.csv");//<--- FILE PATH GOES HERE

        InputReader FileInput = new InputReader();

        ArrayList<Material> Materials = FileInput.ReadFile(InputCSV);

        double PriceTotal = 0;
        double WeightTotal = 0;

        //MAIN PROGRAM STARTS
        System.out.println("\n");
        System.out.println("Welcome to the ARC 6014 Material Calculator.");
        System.out.println("You are going to be presented with the materials. Please enter the amount you want for each material");
        System.out.println("Some are measured in pieces, some by other units like metres or metre-squares You can enter double values.\n");

        //LOOP FOR GETTING INPUT ON MATERIAL AMOUNTS
        for(int i = 0; i<Materials.size(); i++) {
            System.out.println("\n╓╴ [Material No " + decFormat.format(i + 1) + "]: ");
            System.out.println("╟╴ " + Materials.get(i).toStringA());
            System.out.print("╟╴ " + "How much of the item will you use? (Int/Double value)>>> ");
            try {
                Materials.get(i).setAmount(Input.nextDouble());
            }
            catch (Exception e) {
                System.out.println("╟╴ " + "Could not recognise input. Using a value of 0.");
                Materials.get(i).setAmount(0);
            }

            System.out.println("╙╴ " + Materials.get(i).toStringB());
        }

        //LOOP FOR ADDING UP STATS FOR EACH ITEM
        for(int i = 0; i<Materials.size(); i++) {
            PriceTotal += Materials.get(i).getSpecifiedAmountPrice();
            WeightTotal += Materials.get(i).getSpecifiedAmountWeight();
        }

        System.out.println("\n╓╴ Total Information:");
        System.out.println("╟╴ Price: " + PriceTotal);
        System.out.println("╙╴ Weight: " + WeightTotal);





        
    }
}

class Material {

    private String Name;
    private String Code;
    private String Type; //Indicates the type of the product, and if it will be acquired in quantities/bulk. Example values: Pieces, meters, meter squares...
    private double Price; //in usd. X usd per Pieces, meters, meter squares...
    private double Weight; //in kilograms. X kg per Pieces, meters, meter squares...
    private double Amount = 0; //This is the only variable inputted by the user and not the CSV. It is how much of the item will be used.
    private double SpecifiedAmountPrice = 0;
    private double SpecifiedAmountWeight = 0;

    DecimalFormat moneyFormat = new DecimalFormat("0.##$");
    DecimalFormat weightFormat = new DecimalFormat("0.###kg");

    public Material() {

    }

    public Material(String Name, String Code, String Type, double Price, double Weight) {
        this.Name = Name;
        this.Code = Code;
        this.Type = Type;
        this.Price = Price;
        this.Weight = Weight;
    }

    //STANDARD GETTER METHODS
    public String getName() {
        return Name;
    }
    public String getCode() {
        return Code;
    }
    public String getType() {
        return Type;
    }
    public double getPrice() {
        return Price;
    }
    public double getWeight() {
        return Weight;
    }
    public double getAmount() {
        return Amount;
    }

    public double getSpecifiedAmountPrice() {
        return SpecifiedAmountPrice;
    }
    public double getSpecifiedAmountWeight() {
        return SpecifiedAmountWeight;
    }

    //STANDARD SETTER METHODS
    public void setName(String a) {
        Name = a;
    }
    public void setCode(String a) {
        Code = a;
    }
    public void setType(String a) {
        Type = a;
    }
    public void setPrice(double a) {
        Price = a;
    }
    public void setWeight(double a) {
        Weight = a;
    }
    public void setAmount(double a) {
        Amount = a;
        Refresh();
    }

    public void Refresh(){
        SpecifiedAmountPrice = Price * Amount;
        SpecifiedAmountWeight = Weight * Amount;
    }

    @Override
    public String toString() {
        return  ("Name: " + Name + ", Code: " + Code + ", Type: " + Type + ", Price: " + moneyFormat.format(Price) + ", Weight: " + weightFormat.format(Weight) +
                ", Specified Amount: " + Amount + ", Specified Amount Price: " + moneyFormat.format(SpecifiedAmountPrice) + ", Specified Amount Weight: " + weightFormat.format(SpecifiedAmountWeight));
    }
    public String toStringA() {
        return ("Name: " + Name + ", Code: " + Code + ", Type: " + Type + ", Price: " + moneyFormat.format(Price) + ", Weight: " + weightFormat.format(Weight));
    }

    public String toStringB() {
        return ("Specified Amount: " + Amount + ", Specified Amount Price: " + moneyFormat.format(SpecifiedAmountPrice) + ", Specified Amount Weight: " + weightFormat.format(SpecifiedAmountWeight));
    }
}

class InputReader {//This class is for parsing the CSV of materials into an array.


    public InputReader() {

    }


    public ArrayList<Material> ReadFile(File ReportsFile) {


        ArrayList<Material> SalesDataArray = new ArrayList<Material>();

        BufferedReader Reader;
        String Line;

        try {
            Reader = new BufferedReader(new FileReader(ReportsFile));

            Line = Reader.readLine();
            while (Line != null) {
                try {
                    SalesDataArray.add(ParseLine(Line));
                }
                catch (Exception e) {
                    System.out.println("╓╴ " + "Error While parsing string \"" + Line + "\" into a Material Object. Exception:");
                    System.out.println("╟╴ " + e.toString());
                    System.out.println("╟╴ " + "No Material Object will be returned.");
                    System.out.println("╙╴ " + "This exception was most likely thrown from the header of the csv");
                }
                Line = Reader.readLine();
            }
            Reader.close();
        }
        catch(IOException e) {
            System.out.println("╓╴ " + "IOException thrown");
            System.out.println("╟╴ " + "This error is most likely caused because java cannot find the CSV File.");
            System.out.println("╙╴ " + "You can change the path for the File in the Main Class.");
        }

        return SalesDataArray;
    }

    public Material ParseLine(String Line) {

        String[] LineData = Line.split(",");

        String A = LineData[0];
        String B = LineData[1];
        String C = LineData[2];
        double D = Double.parseDouble(LineData[3]);
        double E = Double.parseDouble(LineData[4]);

        return new Material(A, B, C, D, E);
    }
}
