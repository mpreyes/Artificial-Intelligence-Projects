import java.util.Random;
import java.io.*;
import java.util.*;
import java.util.HashMap;


/**
 * Created by madelynreyes on 2/12/18.
 */
public class stockRule {
    //class that drives the entire program
    //For general project outline, see the text file "Project Outline"
    //In our program, 261 days represents a year regardless of the actual date. thus we can look at about 1044 "real" days worth
    //of data for a 3 year period (with a buffer of a previous year so we can calculate the SMA/MAX/EMA functions.

    //five files, Date, Opening, Close prices
    String stringrule;
    double stockfitness;
    double selection;

    public stockRule(String rule, double fitness, double select){
        stringrule = rule;
        stockfitness = fitness;
        selection = select;

    }

    public static void initializeHash (Scanner company, File f, HashMap<Integer, Double> CompanyPrice){

        try{
            company = new Scanner(f); // scanner for reading file
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        int i =0;
        while(company.hasNextLine()){
            String [] companyStr = company.nextLine().split(",");
            // String date = companyStr[0];
            // CompanyDate.put(i,date);
            // String open = companyStr[1]; //not needed, but eh
            Double close = Double.parseDouble(companyStr[2]);
            CompanyPrice.put(i,close);
            i++;
        }
        company.close();

    }
    //time period set to two years
    public static double evaluateCompany(String rule1,char andor1, String rule2,char andor2, String rule3,HashMap<Integer,Double> companyPrice ){

        double startupDeposit = 100000;
        int stocks = 0;
        double gainsAccount = 0;

        Boolean result = null;  //handles buy/sell action
        for (int a = 261; a < companyPrice.size(); a++) {
            // System.out.println("rule1 " + rule1.substring(1) + " rule2 " + rule2.substring(1) + " rule3 " + rule3.substring(1));

            if((!rule1.substring(1).equals("000")) && (!rule2.substring(1).equals("000")) && (!rule3.substring(1).equals("000"))){
                boolean smaBool = SMA(rule1, companyPrice, companyPrice.get(a));
                boolean emaBool = EMA(rule2, companyPrice, companyPrice.get(a));
                boolean  maxBool = MAX(rule3, companyPrice, companyPrice.get(a));

                boolean exp1;
                if(andor1 == '&'){
                    exp1 =  (smaBool && emaBool);
                }
                else{
                    exp1 =  (smaBool || emaBool);
                }
                if(andor2 == '&'){
                    result = (exp1 && maxBool);
                }
                else{
                    result = (exp1 || maxBool);
                }
                //System.out.println("result1 " + result);
            }


            else if((!rule1.substring(1).equals("000")) && (!rule2.substring(1).equals("000")) && rule3.substring(1).equals("000")){
                boolean smaBool = SMA(rule1, companyPrice, companyPrice.get(a));
                boolean emaBool = EMA(rule2, companyPrice, companyPrice.get(a));

                if(andor1 == '&'){
                    result = (smaBool && emaBool);
                }
                else{
                    result = (smaBool || emaBool);
                }
                // System.out.println("result2 " + result);
            }

            else if((!rule1.substring(1).equals("000")) && rule2.substring(1).equals("000") && (!rule3.substring(1).equals("000"))){
                boolean smaBool = SMA(rule1, companyPrice, companyPrice.get(a));
                boolean  maxBool = MAX(rule3, companyPrice, companyPrice.get(a));

                if(andor1 == '&'){
                    result = (smaBool && maxBool);
                }
                else{
                    result = (smaBool || maxBool);
                }
                // System.out.println("result3 " + result);

            }
            else if(rule1.substring(1).equals("000") && (!rule2.substring(1).equals("000")) && (!rule3.substring(1).equals("000"))){
                boolean emaBool = EMA(rule2,companyPrice, companyPrice.get(a));
                boolean  maxBool = MAX(rule3, companyPrice, companyPrice.get(a));

                if(andor1 == '&'){
                    result = (emaBool && maxBool);
                }
                else{
                    result = (emaBool || maxBool);
                }
                // System.out.println("result4 " + result);

            }
            else if(!rule1.substring(1).equals("000") && (rule2.substring(1).equals("000")) && (rule3.substring(1).equals("000"))){ //single rule S

                result = SMA(rule1, companyPrice, companyPrice.get(a));

            }
            else if(rule1.substring(1).equals("000") && (!rule2.substring(1).equals("000")) && (rule3.substring(1).equals("000"))){ //single rule E

                result = EMA(rule2,  companyPrice, companyPrice.get(a));

            }
            else if(rule1.substring(1).equals("000") && (rule2.substring(1).equals("000")) && (!rule3.substring(1).equals("000"))){ //single rule M

                result  = MAX(rule3, companyPrice, companyPrice.get(a));
            }

            if((startupDeposit > 7) && (result == Boolean.TRUE)){ //if there is money, and it says to buy
                // System.out.println("buy " + startupDeposit);
                // System.out.println("buy: starupdep "+ startupDeposit + " stocks " + stocks);
                startupDeposit -= 7; //transaction fee
                double stocksBought = Math.floor(startupDeposit / companyPrice.get(a));
                // System.out.println("stocksBought " + stocksBought);
                stocks  += stocksBought;
                // System.out.println("company price " + companyPrice.get(i));
                //System.out.println("stocks " + stocks);
                double spent = companyPrice.get(a) * stocksBought;
                // System.out.println("  bought. spent " + spent);
                startupDeposit -= spent;
                if(startupDeposit < 100000 && (gainsAccount > 0)){
                    if((startupDeposit + gainsAccount) >= 100000){
                        double difference =  100000 - startupDeposit; //how much we need
                        // System.out.println("diff " + difference);
                        gainsAccount -= difference;
                        startupDeposit = 100000;
                    }

                }
            }
            else if((stocks > 0) && (result == Boolean.FALSE)) { //otherwise, sell
                //System.out.println("sell:  starupdep "+ startupDeposit + " stocks " + stocks);
                startupDeposit -= 7; //transaction fee
                stocks  -= Math.floor(startupDeposit / companyPrice.get(a));
                double gain = companyPrice.get(a) * stocks;
                startupDeposit += gain;
                if(startupDeposit > 100000){
                    //System.out.println("startup deposit " + startupDeposit);
                    double difference = startupDeposit - 100000;
                    gainsAccount += difference;
                    startupDeposit = 100000.0;
                    // System.out.println("sold. gainsAcc " + gainsAccount);

                }
            }

        }
        if(stocks > 0){ //sell remaining stocks
            startupDeposit += (stocks * companyPrice.get(companyPrice.size()-1));
            stocks = 0;
        }
//        if(result == null){ //the stock did not buy/ sell for the period, penalize
//            startupDeposit /= 2; // do not penalize
//        }
        return startupDeposit;

    }


    public static String Rule(String Rule1,String days1,String ANDOR, String Rule2, String days2, String ANDOR2, String Rule3, String days3){

        String rule = Rule1 + days1 + ANDOR + Rule2 + days2 + ANDOR2 + Rule3 + days3;

        return rule;
    }

    //In this rule, if the actual closing price of a share or stock is higher than the SMA
    // over N periods of time, then a buy signal is sent; otherwise a sell signal is sent.

    public static boolean SMA (String rule, HashMap<Integer, Double> companyPrice, Double compare){

        //calculate SMA over N days
        char S = rule.charAt(0);
        String parseNo = rule.substring(1); //gets everything after the initial letter
        int N = 0;

        if(parseNo.charAt(0) == '0'){
            N = Integer.parseInt(parseNo.substring(1));
        }
        else if(parseNo.substring(0,2).equals("00")){
            N = (int) parseNo.charAt(2);
            System.out.println("N should be a single digit " + N);
        }
        else {
            N = Integer.parseInt(parseNo);
        }

        double sumN = 0.0;
        // System.out.println("N " + N);
        for(int i = (261-N); i <= 261; i++){
            //companyDate.get(i);
            sumN  += companyPrice.get(i);
        }

        double smaResult =  sumN / N;
        //System.out.println("SMA rule  " + rule + " SMA answer " + smaResult + " compared with " + compare);
        if(smaResult <= compare){ //if closing price is higher than SMA over N periods

            return true;
        }
        else{
            return false;
        }

    }

    //The second basic rule locates the maximum closing price from a period of N days.
    // If this maximum is less than the actual closing price, then a buy signal is sent;
    // otherwise a sell signal is sent.

    public static boolean MAX ( String rule,HashMap<Integer, Double> companyPrice, Double compare){ //true buy, false sell

        //calculate MAX over N days
        char M = rule.charAt(0);
        String parseNo = rule.substring(1); //gets everything after the initial letter
        //  System.out.println("parseNo " + parseNo);
        int N = 0;
        if(parseNo.charAt(0) == '0'){
            N = Integer.parseInt(parseNo.substring(1));
        }
        else if(parseNo.substring(0,2).equals("00")){
            N = (int) parseNo.charAt(2);
        }
        else {
            N = Integer.parseInt(parseNo);
        }

        // System.out.println("N " + N);
        ArrayList<Double> comp = new ArrayList<Double>();
        for(int i = (261-N); i <= 260; i++){
            //System.out.println("i " + i);
            double price = companyPrice.get(i);
            comp.add(price);
        }

        double maxValue =  Collections.max(comp);
        //System.out.println("MAX rule  " + rule + " Max answer " + maxValue + " compared with " + compare);

        if(maxValue <= compare){ //if maximum is less than the closing price, buy
            return true; //buy
        }
        else{
            return false; //sell
        }

    }

    // The third basic rule uses the Exponential Moving Average (EMA). If the actual closing price of a
    // share or stock is higher than the EMA over N periods of time, then a buy signal is sent; otherwise
    // a sell signal is sent.

    public static boolean EMA (String rule, HashMap<Integer, Double> companyPrice, Double compare){
        //calculate EMA over N days
        //EMA: {Close - EMA(previous day)} x multiplier + EMA(previous day).
        char E = rule.charAt(0);
        String parseNo = rule.substring(1); //gets everything after the initial letter
        int  N = 0;
        if(parseNo.charAt(0) == '0'){
            N = Integer.parseInt(parseNo.substring(1));
        }
        else if(parseNo.substring(0,2).equals("00")){
            N = (int) parseNo.charAt(2);
        }
        else{
            N = Integer.parseInt(parseNo);
        }
        double sumN = 0.0;
        //System.out.println("N " + N);
        for(int i = (261-N); i <= 261; i++){
            // System.out.println("I " + i);
            sumN  += companyPrice.get(i);
        }

        double previousEMA =  sumN / N; //starting previous price is SMA over that period
        double alpha = (2 / (N + 1));
        double num = 0.0;
        double denom  = 0.0;
        int pow = 0;
        /// System.out.println("N " +  N);

        double EMA = 0;
        //EMA = ((Current price - Previous EMA) × k) + Previous EMA
        for(int i=(261-N); i <= 261; i++) {
            // companyDate.get(i);
            //System.out.println("EMA i " + i);
            double currentPrice = companyPrice.get(i);
            // EMA = (((currentPrice - previousEMA) * alpha) + previousEMA);
            // previousEMA = EMA;
            double previousPrice = companyPrice.get(i);
            num +=  Math.pow((1 - alpha),pow) * previousPrice;
            denom += Math.pow((1- alpha),pow);
            pow++;
        }
        double answer = num /denom;
        //double answer = EMA;
        //System.out.println("EMA answer " + answer);
        //System.out.println("answer " + answer);
        /// System.out.println("EMA rule  " + rule + " EMA answer " + answer + " compared with " + compare);
        if(answer < compare){ //if actual closing price is higher than EMA, buy
            return true; //buy
        }
        else{
            return false; //sell
        }

    }

    //stockrule

    public static void evaluateFitness(stockRule s, HashMap<Integer, Double> ksPrice,  HashMap<Integer, Double> aetPrice,  HashMap<Integer, Double> amznPrice,
                                       HashMap<Integer, Double> baPrice,  HashMap<Integer, Double> bacPrice, HashMap<Integer, Double> costPrice,  HashMap<Integer, Double> cvsPrice,
                                       HashMap<Integer, Double> disPrice,  HashMap<Integer, Double> fbPrice,  HashMap<Integer, Double> fdxPrice, HashMap<Integer, Double> gmPrice,
                                       HashMap<Integer, Double> hasPrice, HashMap<Integer, Double> hdPrice,  HashMap<Integer, Double> hmcPrice,  HashMap<Integer, Double> ibmPrice,
                                       HashMap<Integer, Double> koPrice, HashMap<Integer, Double> krPrice,  HashMap<Integer, Double> lowPrice,  HashMap<Integer, Double> matPrice,
                                       HashMap<Integer, Double> msftPrice, HashMap<Integer, Double> nsanyPrice,  HashMap<Integer, Double> pepPrice,  HashMap<Integer, Double> snePrice,
                                       HashMap<Integer, Double> tgtPrice,  HashMap<Integer, Double> tmPrice,  HashMap<Integer, Double> tslaPrice,  HashMap<Integer, Double> twtrPrice,
                                       HashMap<Integer, Double> upsPrice, HashMap<Integer, Double> wmtPrice,  HashMap<Integer, Double> xomPrice ){ //taking in initial population and company info
        //ArrayList<stockRule> s
        //first determine what type of rule it is
        //then, determine what functions to use,
        //run rule through 5 companies.
        //sum
        //set fitness
        //  for(int i =0; i < s.size();i++) {
        //System.out.println("size " + s.size());
        // String rule = s.get(i).stringrule;
        String rule1 = s.stringrule.substring(0,4);
        char andor1 = s.stringrule.charAt(4);
        String rule2 = s.stringrule.substring(5,9);
        char andor2 = s.stringrule.charAt(9);
        String rule3 = s.stringrule.substring(10);

        double ksAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, ksPrice);
        double aetAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, aetPrice);
        double amznAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, amznPrice);
        double baAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3,  baPrice);
        double bacAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, bacPrice);
        double costAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, costPrice);
        double cvsAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, cvsPrice);
        double disAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, disPrice);
        double fbAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3,  fbPrice);
        double fdxcAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, fdxPrice);

        double gmAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, gmPrice);
        double hasAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, hasPrice);
        double hdAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, hdPrice);
        double hmcAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3,  hmcPrice);
        double ibmAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, ibmPrice);
        double koAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, koPrice);
        double krAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, krPrice);
        double lowAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, lowPrice);
        double  matAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3,  matPrice);
        double msftAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, msftPrice);

        double nsanyAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, ksPrice);
        double pepAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, pepPrice);
        double sneAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, snePrice);
        double tgtAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3,  tgtPrice);
        double tmAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, tmPrice);
        double tslaAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, tslaPrice);
        double twtrAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, twtrPrice);
        double upsAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, upsPrice);
        double wmtAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3,  wmtPrice);
        double xomcAccount = evaluateCompany( rule1, andor1, rule2, andor2, rule3, xomPrice);


        s.stockfitness = (ksAccount + aetAccount +amznAccount + baAccount + bacAccount + costAccount+cvsAccount+disAccount+ fbAccount+
                fdxcAccount+ gmAccount+hasAccount+ hdAccount+hmcAccount+ibmAccount+koAccount+krAccount+lowAccount+matAccount+
                msftAccount+nsanyAccount+pepAccount+sneAccount+tgtAccount+tmAccount+ tslaAccount+twtrAccount+upsAccount+
                wmtAccount + xomcAccount);
        // System.out.println(" Fitness " + s.get(i).stockfitness);
        // }

        // System.out.println("Eval fitness");

    }

    public static int select(ArrayList<stockRule> s) {  //just select random rule
        Random r = new Random();
        double rVar = r.nextDouble();  //variable between 0 -> 1
        int index = 0;

        for(int i =0; i < s.size(); i++){
            rVar -= s.get(i).selection;
            if(rVar < 0){
                index =  i;
                break;
            }
        }
        if(index == 0){
            return s.size()-1;
        }
        else{
            return index;
        }



    }

    public static stockRule Crossover(stockRule father, stockRule mother){
        // System.out.println("father " + father.stringrule);
        // System.out.println("mother " + mother.stringrule);
        String fRule1 = father.stringrule.substring(0,4); //I chose to mutate the entire rule portion
        char fandor1 = father.stringrule.charAt(4);        //as it wasn't specified how exactly to perform
        String fRule2 = father.stringrule.substring(5,9);   //the crossover
        char fandor2 = father.stringrule.charAt(9);
        String fRule3 = father.stringrule.substring(10);

        String mRule1 = mother.stringrule.substring(0,4);
        char mandor1 = mother.stringrule.charAt(4);
        String mRule2 = mother.stringrule.substring(5,9);
        char mandor2 = mother.stringrule.charAt(9);
        String mRule3 = mother.stringrule.substring(10);

        Random g = new Random();
        int d =  g.nextInt(14);
        String childR = ""; //choose at which point to crossover
        if( d >= 0 && d <=2){ //we change out the first rule
            childR =  (mRule1 + fandor1 + fRule2 + fandor2 + fRule3);
            //System.out.println("change out first " + childR);

        }
        else if(d > 2 && d <= 4){ //we change out the second
            childR =  (mRule1 + mandor1 + mRule2 + fandor2 + fRule3);
            //System.out.println("change out second " + childR);
        }
        else if(d > 4 && d <= 6){ //we change out the second
            childR =  (fRule1 + mandor2 + mRule2 + fandor1 + mRule3);
            //System.out.println("change out second " + childR);
        }
        else if(d > 6 && d <= 8){ //we change out the second
            childR =  (fRule1 + fandor1 + mRule2 + mandor1 + fRule3);
            //System.out.println("change out second " + childR);
        }
        else{ //change the third
            childR =  (fRule1 + fandor2 + fRule2 + mandor2 + mRule3);
            // System.out.println("change out third " + childR);
        }

        // System.out.println("crossover " + childR);
        return new stockRule(childR, mother.stockfitness,0.0); //for now, set to 0

    }

    public static void Mutate(stockRule childRule){

        String originalRule = childRule.stringrule;
        //System.out.println("original rule " + childRule.stringrule);
        char child [] = childRule.stringrule.toCharArray();
        for (int i =0; i < child.length; i++) {
            Random r = new Random();
            int probability = r.nextInt(1000);

            if(probability == 1) { //probability of mutation for each char is set to .001
                // System.out.println("Mutation occured");
                if (child[i] == '&') {
                    child[i] = '|';
                } else if (child[i] == '|') {
                    child[i] = '&';
                }
                else if (child[i] == 's' || child[i] == 'e' || child[i] == 'm') {
                    Random g = new Random();
                    int a = g.nextInt(3); //generate a random number between 0 and 2
                    String aa = a + "";
                    child[i + 1] = aa.charAt(0);
                    int b = g.nextInt(7); //max is 6
                    String bb = b + "";
                    child[i + 2] = bb.charAt(0);
                    int c = g.nextInt(10); //max is 5
                    String cc = c + "";
                    if(child[i+2] == '6'){ //if 2nd pos is 6
                        int e = g.nextInt(5); //max is  1
                        String ee = 1 + "";
                        child[i + 3] = ee.charAt(0);
                    }
                    else {
                        child[i + 3] = cc.charAt(0);
                    }
                }
                else{ //mutating the indiv numbers
                    Random g = new Random();
                    int d = g.nextInt(10); //generate a random number between 0 and 9 and set it
                    String dd = d + "";
                    if(i == 1 || i == 6 || i == 11){ //making sure first pos in number is between 0,2 (261)
                        int b = g.nextInt(3);
                        String bb = b + "";
                        child[i] = bb.charAt(0);
                    }
                    else if(i == 2 || i == 7 || i == 12){ //second pos can be at max 6
                        int b = g.nextInt(7);
                        String bb = b + "";
                        child[i] = bb.charAt(0);
                    }
                    else if (i ==3 || i == 8 || i == 13 ){

                        if(child[i-1] == '6'){
                            int s = g.nextInt(6); //last pos can be at most 5 when 2nd is 6
                            String ss = 1 + "";
                            child[i] = ss.charAt(0);
                        }
                        else{
                            child[i] = dd.charAt(0);
                        }
                    }
                }

            }
        }
        String mutatedChild = "";
        for (char cp: child) { //putting the string back together
            mutatedChild += cp;
        }
        //  System.out.println(" mutation " + mutatedChild);
        childRule.stringrule = mutatedChild;
    }

    public static void Roulette(ArrayList<stockRule> s){

        double totalFitness = 0.0;
        for(int i=0; i < s.size();i++){
            totalFitness += s.get(i).stockfitness;
        }

        for(int j =0; j < s.size(); j++){  // setting the probability of fitness
            double indivFitness = s.get(j).stockfitness;
            s.get(j).selection = indivFitness / totalFitness;
        }
        //  System.out.println("Roulette");
    }

    public static int answerSoFar(ArrayList<stockRule> s ){
        double maxVal = 0.0;
        int index = 0;

        for(int i= 0; i < s.size();i++){
            if(s.get(i).stockfitness > maxVal){
                maxVal = s.get(i).stockfitness;
            }
        }

        for(int k =0; k < s.size();k++){
            if(s.get(k).stockfitness == maxVal){
                index = k;
                break;
            }
        }
        // System.out.println("answer so far " + index);
        return index;
    }

    static int trade(ArrayList<stockRule> s, HashMap<Integer, Double> ksPrice,  HashMap<Integer, Double> aetPrice,  HashMap<Integer, Double> amznPrice,
                     HashMap<Integer, Double> baPrice,  HashMap<Integer, Double> bacPrice, HashMap<Integer, Double> costPrice,  HashMap<Integer, Double> cvsPrice,
                     HashMap<Integer, Double> disPrice,  HashMap<Integer, Double> fbPrice,  HashMap<Integer, Double> fdxPrice, HashMap<Integer, Double> gmPrice,
                     HashMap<Integer, Double> hasPrice, HashMap<Integer, Double> hdPrice,  HashMap<Integer, Double> hmcPrice,  HashMap<Integer, Double> ibmPrice,
                     HashMap<Integer, Double> koPrice, HashMap<Integer, Double> krPrice,  HashMap<Integer, Double> lowPrice,  HashMap<Integer, Double> matPrice,
                     HashMap<Integer, Double> msftPrice, HashMap<Integer, Double> nsanyPrice,  HashMap<Integer, Double> pepPrice,  HashMap<Integer, Double> snePrice,
                     HashMap<Integer, Double> tgtPrice,  HashMap<Integer, Double> tmPrice,  HashMap<Integer, Double> tslaPrice,  HashMap<Integer, Double> twtrPrice,
                     HashMap<Integer, Double> upsPrice, HashMap<Integer, Double> wmtPrice,  HashMap<Integer, Double> xomPrice ){

        int iterations =  0;
        int currentGen = 0;
        int generation = 0;
        int current = -1;

        for(int i =0; i < s.size();i++) { //evaluate fitness for initial population
            evaluateFitness(s.get(i), ksPrice, aetPrice, amznPrice, baPrice, bacPrice,costPrice,cvsPrice, disPrice,fbPrice,
                    fdxPrice,gmPrice,hasPrice,hdPrice,hmcPrice,ibmPrice,koPrice,krPrice,lowPrice,matPrice,msftPrice,nsanyPrice,pepPrice,snePrice,
                    tgtPrice, tmPrice,tslaPrice,twtrPrice,upsPrice,wmtPrice,xomPrice);
        }


        while(iterations < 200){ //iterations are set to 200
            //evaluate fitness with for all 5 accounts
            Roulette(s);

            int f = select(s);
            int m  = select(s);
            // System.out.println("f index " + f + " m index " + m);

            Random r = new Random();
            currentGen++;
            int crossoverRate = r.nextInt(101);

            if(crossoverRate < 80){ //crossover rate is set to 0.8
                stockRule child1 = Crossover(s.get(f), s.get(m));
                stockRule child2 = Crossover(s.get(m), s.get(f));
                Mutate(child1);
                Mutate(child2);

                evaluateFitness(child1, ksPrice, aetPrice, amznPrice, baPrice, bacPrice,costPrice,cvsPrice, disPrice,fbPrice,
                        fdxPrice,gmPrice,hasPrice,hdPrice,hmcPrice,ibmPrice,koPrice,krPrice,lowPrice,matPrice,msftPrice,nsanyPrice,pepPrice,snePrice,
                        tgtPrice, tmPrice,tslaPrice,twtrPrice,upsPrice,wmtPrice,xomPrice);

                evaluateFitness(child2, ksPrice, aetPrice, amznPrice, baPrice, bacPrice,costPrice,cvsPrice, disPrice,fbPrice,
                        fdxPrice,gmPrice,hasPrice,hdPrice,hmcPrice,ibmPrice,koPrice,krPrice,lowPrice,matPrice,msftPrice,nsanyPrice,pepPrice,snePrice,
                        tgtPrice, tmPrice,tslaPrice,twtrPrice,upsPrice,wmtPrice,xomPrice);

                s.add(child1);
                s.add(child2);

                Roulette(s);
            }

            int k = answerSoFar(s);
            String fittest =  s.get(k).stringrule;
            // System.out.println("fittest " + fittest);

            if(k != -1 && k != current){
                current = k;
                generation = currentGen;
            }

           // System.out.println("iterations " +  currentGen);
            iterations++;
        }
        return current;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(" Welcome to the Jungle ");
        String rule1  = "s050&e040&m060"; //SMA 50 & EMA 40 & MAX 60. test string

        String rule11 = Rule("s","011", "&","e","000", "&", "m","000"); //5 of triple
        String rule12 = Rule("s","003", "|","e","021", "|", "m","000");
        String rule13 = Rule("s","000", "&","e","000", "&", "m","101");

        String rule14 = Rule("s","120", "&","e","120", "|", "m","000");
        String rule15 = Rule("s","054", "|","e","000", "|", "m","111");


        String rule21 = Rule("s","099", "&","e","019", "&", "m","000"); //5 of triple
        String rule22 = Rule("s","243", "|","e","023", "|", "m","231");
        String rule23 = Rule("s","102", "&","e","000", "&", "m","000");
        String rule24 = Rule("s","120", "&","e","067", "|", "m","101");
        String rule25 = Rule("s","250", "|","e","015", "|", "m","230");


        String rule31 = Rule("s","012", "&","e","231", "&", "m","086"); //5 of triple
        String rule32 = Rule("s","000", "|","e","023", "|", "m","201");
        String rule33 = Rule("s","012", "&","e","240", "&", "m","090");
        String rule34 = Rule("s","120", "&","e","000", "|", "m","012");
        String rule35 = Rule("s","112", "|","e","015", "|", "m","212");

        String rule41 = Rule("s","000", "&","e","231", "&", "m","128"); //5 of triple
        String rule42 = Rule("s","122", "|","e","023", "|", "m","122");
        String rule43 = Rule("s","102", "&","e","010", "&", "m","108");
        String rule44 = Rule("s","045", "&","e","000", "|", "m","012");
        String rule45 = Rule("s","021", "|","e","017", "|", "m","010");


        ArrayList<stockRule> initialPop = new ArrayList<stockRule>();
        initialPop.add(new stockRule(rule11,0.0,0.0)); //adding a rule, and initial fitness of 0.0
        initialPop.add(new stockRule(rule12,0.0,0.0));
        initialPop.add(new stockRule(rule13,0.0,0.0));
        initialPop.add(new stockRule(rule14,0.0, 0.0));
        initialPop.add(new stockRule(rule15,0.0, 0.0));

        initialPop.add(new stockRule(rule21,0.0, 0.0));
        initialPop.add(new stockRule(rule22,0.0,0.0));
        initialPop.add(new stockRule(rule23,0.0,0.0));
        initialPop.add(new stockRule(rule24,0.0,0.0));
        initialPop.add(new stockRule(rule25,0.0,0.0));

        initialPop.add(new stockRule(rule31,0.0,0.0));
        initialPop.add(new stockRule(rule32,0.0,0.0));
        initialPop.add(new stockRule(rule33,0.0,0.0));
        initialPop.add(new stockRule(rule34,0.0,0.0));
        initialPop.add(new stockRule(rule35,0.0,0.0));

        initialPop.add(new stockRule(rule41,0.0,0.0));
        initialPop.add(new stockRule(rule42,0.0,0.0));
        initialPop.add(new stockRule(rule43,0.0,0.0));
        initialPop.add(new stockRule(rule44,0.0,0.0));
        initialPop.add(new stockRule(rule45,0.0,0.0));



        File KS = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/005930.KS.csv");
        File AET = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/AET.csv");
        File AMZN = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/AMZN.csv");
        File BA = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/BA.csv");
        File BAC = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/BAC.csv");
        File COST = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/COST.csv");
        File CVS = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/CVS.csv");
        File DIS = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/DIS.csv");
        File FB = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/FB.csv");
        File FDX = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/FDX.csv");

        File GM = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/GM.csv");
        File HAS = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/HAS.csv");
        File HD = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/HD.csv");
        File HMC = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/HMC.csv");
        File IBM = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/IBM.csv");
        File KO = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/KO.csv");
        File KR = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/KR.csv");
        File LOW = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/LOW.csv");
        File MAT = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/MAT.csv");
        File MSFT = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/MSFT.csv");

        File NSANY = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/NSANY.csv");
        File PEP = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/PEP.csv");
        File SNE = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/SNE.csv");
        File TGT = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/TGT.csv");
        File TM = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/TM.csv");
        File TSLA = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/TSLA.csv");
        File TWTR = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/TWTR.csv");
        File UPS = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/UPS.csv");
        File WMT = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/WMT.csv");
        File XOM = new File("/Users/madelynreyes/IdeaProjects/MakinMoneyWithAIExperiment/src/XOM.csv");



        Scanner ks = new Scanner(KS);
        Scanner aet = new Scanner(AET);
        Scanner amzn = new Scanner(AMZN);
        Scanner ba = new Scanner(BA);
        Scanner bac = new Scanner(BAC);
        Scanner cost = new Scanner(COST);
        Scanner cvs = new Scanner(CVS);
        Scanner dis = new Scanner(DIS);
        Scanner fb = new Scanner(FB);
        Scanner fdx = new Scanner(FDX);

        Scanner gm = new Scanner(GM);
        Scanner has = new Scanner(HAS);
        Scanner hd = new Scanner(HD);
        Scanner hmc = new Scanner(HMC);
        Scanner ibm = new Scanner(IBM);
        Scanner ko = new Scanner(KO);
        Scanner kr = new Scanner(KR);
        Scanner low = new Scanner(LOW);
        Scanner mat = new Scanner(MAT);
        Scanner msft = new Scanner(MSFT);

        Scanner nsany = new Scanner(NSANY);
        Scanner pep = new Scanner(PEP);
        Scanner sne = new Scanner(SNE);
        Scanner tgt = new Scanner(TGT);
        Scanner tm = new Scanner(TM);
        Scanner tsla = new Scanner(TSLA);
        Scanner twtr = new Scanner(TWTR);
        Scanner ups = new Scanner(UPS);
        Scanner wmt = new Scanner(WMT);
        Scanner xom = new Scanner(XOM);


        HashMap<Integer, Double> ksClose =  new HashMap<Integer, Double>();
        HashMap<Integer, Double> aetClose =  new HashMap<Integer, Double>();
        HashMap<Integer, Double> amznClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> bacClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> baClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> costClose =  new HashMap<Integer, Double>();
        HashMap<Integer, Double> cvsClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> disClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> fbClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> fdxClose = new HashMap<Integer, Double>();

        HashMap<Integer, Double> gmClose =  new HashMap<Integer, Double>();
        HashMap<Integer, Double> hasClose =  new HashMap<Integer, Double>();
        HashMap<Integer, Double> hdClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> hmcClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> ibmClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> koClose =  new HashMap<Integer, Double>();
        HashMap<Integer, Double> krClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> lowClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> matClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> msftClose = new HashMap<Integer, Double>();

        HashMap<Integer, Double> nsanyClose =  new HashMap<Integer, Double>();
        HashMap<Integer, Double> pepClose =  new HashMap<Integer, Double>();
        HashMap<Integer, Double> sneClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> tgtClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> tmClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> tslaClose =  new HashMap<Integer, Double>();
        HashMap<Integer, Double> twtrClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> upsClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> wmtClose = new HashMap<Integer, Double>();
        HashMap<Integer, Double> xomClose = new HashMap<Integer, Double>();



        initializeHash(ks, KS,ksClose);
        initializeHash(aet, AET,aetClose);
        initializeHash(amzn, AMZN,amznClose);
        initializeHash(bac, BAC,bacClose);
        initializeHash(ba, BA, baClose);
        initializeHash(cost, COST, costClose);
        initializeHash(cvs, CVS,cvsClose);
        initializeHash(dis, DIS,disClose);
        initializeHash(fb, FB,fbClose);
        initializeHash(fdx, FDX,fdxClose);


        initializeHash(gm, GM,gmClose);
        initializeHash(has, HAS,hasClose);
        initializeHash(hd, HD,hdClose);
        initializeHash(hmc, HMC, hmcClose);
        initializeHash(ibm, IBM, ibmClose);
        initializeHash(ko, KO, koClose);
        initializeHash(kr, KR,krClose);
        initializeHash(low, LOW,lowClose);
        initializeHash(mat, MAT,matClose);
        initializeHash(msft, MSFT,msftClose);

        initializeHash(nsany, NSANY,nsanyClose);
        initializeHash(pep, PEP, pepClose);
        initializeHash(sne, SNE, sneClose);
        initializeHash(tgt, TGT, tgtClose);
        initializeHash(tm, TM, tmClose);
        initializeHash(tsla, TSLA, tslaClose);
        initializeHash(twtr, TWTR,twtrClose);
        initializeHash(ups, UPS,upsClose);
        initializeHash(wmt, WMT,wmtClose);
        initializeHash(xom, XOM,xomClose);


        int j =  trade(initialPop, ksClose, aetClose, amznClose, baClose, bacClose,costClose,cvsClose, disClose,fbClose,
                fdxClose,gmClose,hasClose,hdClose,hmcClose,ibmClose,koClose,krClose,lowClose,matClose,msftClose,nsanyClose,pepClose,sneClose,
                tgtClose, tmClose,tslaClose,twtrClose,upsClose,wmtClose,xomClose);

        if(j != -1){ //answer found
            System.out.println("Solution: ");
            String rule = initialPop.get(j).stringrule;
            double fitness = initialPop.get(j).stockfitness;
            System.out.println("Answer: "  + rule + " " + fitness);

        }
        else{ //not found
            System.out.println("No answer");
        }


    }
}




