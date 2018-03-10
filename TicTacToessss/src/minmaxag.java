/**
 * Created by madelynreyes on 2/24/18.
 */
import java.lang.reflect.Array;
import java.util.*;

public class minmaxag {

    public minmaxag(){}

    public int move(String state){ //gets state, returns an index //minimax decision

       int index = -1;
       int maxMin = Integer.MIN_VALUE;


       // ArrayList<String> successors =  generateSuccessors(state); //all possible states from current state
        int current;

        for(int i =0 ; i < state.length() ;i++){
            if(state.charAt(i) == '_') {
                String newState = state.substring(0,i) + 'O' + state.substring(i+1);
                current = minValue(newState);
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


    public int maxValue(String state){ //returns a utility value
       // System.out.println("MaxValue " + state);
        int util = Utility(state);
        if(!state.contains("_") || util != 0) { //checking for draw
            return util;
        }

            int v = Integer.MIN_VALUE; //to represent negative infinity
            ArrayList<String> successors = generateSuccessors(state);
            for (int i = 0; i < successors.size(); i++) {
                v = Math.max(v, minValue(successors.get(i)));

            }

            //System.out.println("v in max " + v);
            return v;


    }

    public int minValue(String state){ //returns a utility value

        int util = Utility(state);
        if(!state.contains("_") || util != 0) { //checking for draw
            return util;
        }

             int v = Integer.MAX_VALUE; //positive infinity
             ArrayList<String> successors = generateSuccessors(state);
             for (int i = 0; i < successors.size(); i++) {
                 // System.out.println("successors in min " + i + " " + successors.get(i));
                 v = Math.min(v, maxValue(successors.get(i)));

             }
             // System.out.println("v in min " + v);
             return v;

    }


//    {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, //horizontal wins
//    {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, //vertical wins
//    {0, 4, 8}, {2, 4, 6}
    public int Utility(String state){
        //System.out.println("utility " + state);
        int utility = 0;
        char stateEval [] = state.toCharArray();
        int moves = 0;
        for(int d =0; d < stateEval.length;d++){
            if(stateEval[d] != 'X' && stateEval[d] != 'O'){
                moves++;
            }
        }
       // System.out.println("moves " + moves);
       // System.out.println("Checking for horizontal/vertical/diagonal win");

        if((stateEval[0] == stateEval[1] && stateEval[1] == stateEval[2]) && (stateEval[0] != '_')){ //horizontal wins
            if(stateEval[0] == 'X'){ //X has won in a row state
                utility = -10;
            }
            else if(stateEval[0] == 'O'){
                utility = 10;
            }
        }

        else if((stateEval[3] == stateEval[4] && stateEval[4] == stateEval[5]) && (stateEval[3] != '_')){ //horizontal wins
            if(stateEval[3] == 'X'){ //X has won in a row state
                utility = -10;
            }
            else if(stateEval[3] == 'O'){
                utility = 10;
            }
        }
        else if((stateEval[6] == stateEval[7] && stateEval[7] == stateEval[8]) && (stateEval[6] != '_')){ //horizontal wins
            if(stateEval[6] == 'X'){ //X has won in a row state
                utility = -10;
            }
            else if(stateEval[6] == 'O'){
                utility = 10;
            }
        }
        else if((stateEval[0] == stateEval[3] && stateEval[3] == stateEval[6]) && (stateEval[0] != '_')){ //vertical wins
            if(stateEval[0] == 'X'){ //X has won in a row state
                utility = -10;
            }
            else if(stateEval[0] == 'O'){
                utility = 10;
            }
        }
        else if((stateEval[1] == stateEval[4] && stateEval[4] == stateEval[7]) && (stateEval[1] != '_')){ //vertical wins
            if(stateEval[1] == 'X'){ //X has won in a row state
                utility = -10;
            }
            else if(stateEval[1] == 'O'){
                utility = 10;
            }
        }
        else if((stateEval[2] == stateEval[5] && stateEval[5] == stateEval[8]) && (stateEval[2] != '_')){ //vertical wins
            if(stateEval[2] == 'X'){ //X has won in a row state
                utility = -10;
            }
            else if(stateEval[2] == 'O'){
                utility = 10;
            }
        }

        else if((stateEval[0] == stateEval[4] && stateEval[4] == stateEval[8]) && (stateEval[0] != '_')){ //checking for diagonal wins
            if(stateEval[0] == 'X'){ //X has won in a diagonal state
                utility = -10;
            }
            else if(stateEval[0] == 'O'){
                utility = 10;
            }
        }
        else if((stateEval[2] == stateEval[4] && stateEval[4] == stateEval[6]) && (stateEval[2] != '_')){

            if(stateEval[2] == 'X'){ //X has won in a diagonal state
                utility = -10;
            }
            else if(stateEval[2] == 'O'){
                utility = 10;
            }
        }

        //otherwise its not at a terminal state, return -100
        //System.out.println("utility value " +utility);
        return utility;
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
            int h = 8;
            for (int j = 0; j < (9 - moves); j++) { //generating 9-n boards
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
            int h = 8;
            for (int j = 0; j < (9 - moves); j++) { //generating 9-n boards
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
//
//        for(int i =0; i < stateSuccessors.size();i++){
//            System.out.println("successor " + i + " " + stateSuccessors.get(i));
//        }

        return stateSuccessors;


    }



}

