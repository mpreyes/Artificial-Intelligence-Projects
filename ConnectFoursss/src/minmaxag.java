/**
 * Created by madelynreyes on 2/25/18.
 */
import java.util.*;

public class minmaxag {

    //public static char charState [];
    public minmaxag() {}

    public int move(String state){

        int index = -1;
        int maxMin = Integer.MIN_VALUE;


        // ArrayList<String> successors =  generateSuccessors(state); //all possible states from current state
        int current;
        int alpha = Integer.MIN_VALUE; //negative infinity
        int beta = Integer.MAX_VALUE; //positive infinity
        int depth = 3;
        for(int i =0 ; i < state.length(); i++){
            if(state.charAt(i) == '_') {
                String newState = state.substring(0,i) + 'O' + state.substring(i+1);
                current = minValue(newState,alpha, beta,depth);
                // System.out.println("current " + current);
                if (current > maxMin) { //we max move from the minimum ones
                    //System.out.println("maxMin " + maxMin);
                    maxMin = current;
                    index = i;
                    //System.out.println("i " + i);
                }
            }
        }

        // System.out.println("Index "+ index);
        return index;

    }


    public int minValue(String state,int alpha, int beta, int depth){ //returns a utility value

        int util = Utility(state);
        if(!state.contains("_") || util != 0 || depth == 0) { //checking for draw
            //System.out.println("depth is 0 " + depth );
            return util;
        }

        int v = Integer.MAX_VALUE; //positive infinity
        ArrayList<String> successors = generateSuccessors(state);
        for (int i = 0; i < successors.size(); i++) {
            // System.out.println("successors in min " + i + " " + successors.get(i));
            v = Math.min(v, maxValue(successors.get(i), alpha, beta, depth-1));
            beta = Math.min(beta,v);
            if(beta <= alpha){
                return v;
            }
           // System.out.println("beta "+ beta);

        }
        // System.out.println("v in min " + v);
        return v;

    }

    public int maxValue(String state, int alpha, int beta, int depth){ //returns a utility value
         //System.out.println("depth " + depth);
        int util = Utility(state);
        if(!state.contains("_") || util != 0 || depth == 0) { //checking for draw
            return util;
        }

        int v = Integer.MIN_VALUE; //to represent negative infinity
        ArrayList<String> successors = generateSuccessors(state);
        for (int i = 0; i < successors.size(); i++) {
            v = Math.max(v, minValue(successors.get(i),alpha, beta, depth-1));
            alpha = Math.max(alpha,v);
            if(beta <= alpha){
                return v;
            }

            //System.out.println("alpha "+ alpha);
        }

        //System.out.println("v in max " + v);
        return v;


    }


    public int Utility(String state){
        //System.out.println("state util " + state);
        char [] charState = state.toCharArray();
        int utility = 0;
        for(int i = 0; i < 42; i++){
            // System.out.println("charstate " + charState[i]);
            if(straightup(i, charState) || straightdown(i,charState) || left(i, charState) || right(i,charState) ||
                    diagleftup(i, charState) || diagrightup(i, charState) || diagrightdown(i, charState) || diagleftdown(i,charState)){
                if(charState[i] == 'X'){
                    utility = -10;

                }
                else if(charState[i] == 'O'){
                    utility = 10;

                }
            }
            }

        return utility;
    }

    public static boolean straightup(int b, char charState []){
        int [] list = new int[4];
        int val = b;
        boolean filled = true;
        for(int i = 0; i< 4; i++){
            if (val  >= 0){
                list[i] = val;
                val -= 7;
            }
            else{filled = false;}
        }
        if (filled == false)
            return false;
        else{

            if(charState[list[0]] == charState[list[1]] &&
                    charState[list[1]] == charState[list[2]] &&
                    charState[list[2]] == charState[list[3]] &&
                    charState[list[3]] != '_')
                return true;
            else
                return false;
        }

    }

    public static boolean straightdown(int b, char charState []){
        int [] list = new int[4];
        int val = b;
        boolean filled = true;
        for(int i = 0; i< 4; i++){
            if (val  < 42){
                list[i] = val;
                val += 7;
            }
            else{filled = false;}
        }
        if (filled == false)
            return false;
        else{
            if(charState[list[0]] == charState[list[1]] &&
                    charState[list[1]] == charState[list[2]] &&
                    charState[list[2]] == charState[list[3]] &&
                    charState[list[3]] != '_')
                return true;
            else
                return false;
        }
    }

    public static int findlevel(int b)
    {
        int level = 0;

        for(int i = 6; i < 42; i+=7)
            if(b > i)
                level += 1;

        return level;

    }
    public static boolean left(int b, char charState []){
        int [] list = new int[4];
        int val = b;
        boolean filled = true;
        int level = findlevel(b);

        for(int i = 0; i< 4; i++){
            if (findlevel(val) == level && val >= 0){
                list[i] = val;
                val--;
            }
            else{filled = false;}
        }
        if (filled == false)
            return false;
        else{
            if(charState[list[0]] == charState[list[1]] &&
                    charState[list[1]] == charState[list[2]] &&
                    charState[list[2]] == charState[list[3]] &&
                    charState[list[3]] != '_')
                return true;
            else
                return false;
        }
    }
    public static boolean right(int b, char charState []){
        int [] list = new int[4];
        int val = b;
        boolean filled = true;
        int level = findlevel(b);

        for(int i = 0; i< 4; i++){
            if (findlevel(val) == level && val < 42){
                list[i] = val;
                val++;
            }
            else{filled = false;}
        }
        if (filled == false)
            return false;
        else{
            if(charState[list[0]] == charState[list[1]] &&
                    charState[list[1]] == charState[list[2]] &&
                    charState[list[2]] == charState[list[3]] &&
                    charState[list[3]] != '_')
                return true;
            else
                return false;
        }
    }
    public static boolean diagleftup(int b, char charState []){
        int [] list = new int[4];
        int levela = 0;
        int levelb = 1;
        int val = b;
        boolean filled = true;
        for(int i = 0; i< 4; i++){
            if (val  >= 0 && Math.abs(levela - levelb) == 1){
                levela = findlevel(val);
                list[i] = val;
                val -= 8;
                levelb = findlevel(val);
            }
            else{filled = false;}
        }
        if (filled == false)
            return false;
        else{
            if(charState[list[0]] == charState[list[1]] &&
                    charState[list[1]] == charState[list[2]] &&
                    charState[list[2]] == charState[list[3]] &&
                    charState[list[3]] != '_') {
                //System.out.println("diag " + charState[list[0]] + charState[list[1]] +charState[list[2]] +  charState[list[3]] );
                return true;
            }
            else {
                return false;
            }
        }
    }
    public static boolean diagrightdown(int b, char charState []){
        int [] list = new int[4];
        int val = b;
        int levela = 0;
        int levelb = 1;
        boolean filled = true;
        for(int i = 0; i< 4; i++){
            if (val  < 42 && Math.abs(levela - levelb) == 1){
                levela = findlevel(val);
                list[i] = val;
                val += 8;
                levelb = findlevel(val);
            }
            else{filled = false;}
        }
        if (filled == false)
            return false;
        else{
            if(charState[list[0]] == charState[list[1]] &&
                    charState[list[1]] == charState[list[2]] &&
                    charState[list[2]] == charState[list[3]] &&
                    charState[list[3]] != '_')
                return true;
            else
                return false;
        }
    }
    public static boolean diagleftdown(int b, char charState []){
        int [] list = new int[4];
        int levela = 0;
        int levelb = 1;
        int val = b;
        boolean filled = true;
        for(int i = 0; i< 4; i++){
            if (val  < 42 && Math.abs(levela - levelb) == 1){
                levela = findlevel(val);
                list[i] = val;
                val += 6;
                levelb = findlevel(val);
            }
            else{filled = false;}
        }
        if (filled == false)
            return false;
        else{
            if(charState[list[0]] == charState[list[1]] &&
                    charState[list[1]] == charState[list[2]] &&
                    charState[list[2]] == charState[list[3]] &&
                    charState[list[3]] != '_')
                return true;
            else
                return false;

        }
    }
    public static boolean diagrightup(int b, char charState []){
        int [] list = new int[4];
        int levela = 0;
        int levelb = 1;
        int val = b;
        boolean filled = true;
        for(int i = 0; i< 4; i++){
            if (val  >= 0 && Math.abs(levela - levelb) == 1){
                levela = findlevel(val);
                list[i] = val;
                val -= 6;
                levelb = findlevel(val);
            }
            else{filled = false;}
        }
        if (filled == false)
            return false;
        else{
            if(charState[list[0]] == charState[list[1]] &&
                    charState[list[1]] == charState[list[2]] &&
                    charState[list[2]] == charState[list[3]] &&
                    charState[list[3]] != '_')
                return true;
            else
                return false;
        }
    }


    public ArrayList<String> generateSuccessors(String state){
        //we need to generate 9-n boards
        ArrayList<String> stateSuccessors = new ArrayList<String>();

        int moves =  0;
        int xmoves = 0;
        int ymoves = 0;
        for(int i =0; i < state.length();i++){
            if(state.charAt(i) == 'X'){
                xmoves++;
            }
            else if(state.charAt(i) == 'O'){
                ymoves++;
            }
        }
        // System.out.println("xmoves "+ xmoves);
        moves = (xmoves + ymoves);
        // System.out.println("movess " + moves);
        //if more xmoves, it is O's turn. and vice versa

        if(xmoves > ymoves) { //create O board
            // System.out.println("Creating O board");
            int h = 41;
            for (int j = 0; j < (42 - moves); j++) { //generating 9-n boards
                char [] newState = state.toCharArray();
                for(int i =0; i < newState.length;i++){
                    if(newState[h] == 'X'  || newState[h] == 'O'){
                        h--;

                    }
                    else{
                        newState[h] = 'O';
                        h--;
                        break;

                    }
                }
                String newStateS = "";
                for(int d =0; d < newState.length;d++){ //converting char array to string
                    newStateS += newState[d];
                }
                stateSuccessors.add(newStateS);
            }
        }
        else  if(xmoves == ymoves){ //create X board
            //System.out.println("Creating X board");
            int h = 41;
            for (int j = 0; j < (42 - moves); j++) { //generating 9-n boards
                char [] newState = state.toCharArray();
                for(int i =0; i < newState.length;i++){
                    if(newState[h] == 'X'  || newState[h] == 'O'){
                        h--;

                    }
                    else{
                        newState[h] = 'X';
                        h--;
                        break;

                    }
                }
                String newStateS = "";
                for(int d =0; d < newState.length;d++){ //converting char array to string
                    newStateS += newState[d];
                }
                //System.out.println("newStates " + newStateS);
                stateSuccessors.add(newStateS);
            }
        }

//        for(int i =0; i < stateSuccessors.size();i++){
//            System.out.println("successor " + i + " " + stateSuccessors.get(i));
//        }

        return stateSuccessors;


    }







}

