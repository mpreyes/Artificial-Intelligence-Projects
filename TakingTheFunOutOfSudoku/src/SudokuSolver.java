/**
 * Created by madelynreyes on 2/27/18.
 */
import com.sun.org.apache.bcel.internal.generic.LNEG;
import sun.java2d.loops.GeneralRenderer;

import java.util.*;

public class SudokuSolver {


    public String solve(String state){
        //state  = "__1__3__4_6__4__3_5__8__1__9__7__2___1__5__8___7__6__9__6__5__1_5__3__4_1__6__9__";
        System.out.println("state: " + state);
        char [] bState = state.toCharArray();
        LinkedList<String> q = new LinkedList<String>();

        for(int d =0; d < bState.length;d++){
            q.add(bState[d] + "");
        }
        //linked list that holds variables with domains mapped to them
        //solution is found when domain reaches 1
        char alphabet [] = "ABCDEFGHI".toCharArray();
        char numbers [] = "123456789".toCharArray();
        LinkedHashMap<String,String> grid = new LinkedHashMap<String, String>();
        for(int i =0; i < 9; i++){
            String al = alphabet[i] + "";
            for(int j =0; j < 9; j++){
                if(!q.get(0).equals("_")){
                    grid.put(al + "" + numbers[j], q.get(0));
                    q.remove(0);
                }
                else {

                    grid.put(al + "" + numbers[j], "123456789");
                    q.remove(0);
                }
               // grid.add(n);
            }
        }


        System.out.println("Grid " + grid.toString()); //our variables with domains

//        String res = "";
//        for(String value : resolvedGrid.values()){
//            res += value;
//        }
//        System.out.println("result " + res);
        String result = AC_3(grid,state);
        System.out.println("After backtracking " + result);
        return result;

    }


    public LinkedHashMap<String,String> checkConstraints(LinkedHashMap<String,String> csp, LinkedList<LinkedList<String>> rcb, String compare, String qAtJ){

        for(int b=0; b < rcb.size();b++){
            if(rcb.get(b).contains((qAtJ))){ //this should always be true for 1 box
                for(int d =0; d < rcb.get(b).size(); d++){
                    if(!rcb.get(b).get(d).equals(qAtJ)) {
                        String domain = csp.get(rcb.get(b).get(d));
                        String newDomain = "";
                        for (char c : domain.toCharArray()) {
                            if (!(c + "").equals(compare)) {
                                newDomain += c;
                            }
                        }
                        csp.put(rcb.get(b).get(d), newDomain);
                    }
                }

            }
        }

        return csp;

    }



    public String AC_3(LinkedHashMap<String,String> csp, String state){

        //Dooooont juuudge me dr.....arriiiisoooaaaa ;_;
        //betcha wont find this comment in no one else code.

        LinkedList<LinkedList<String>> boxes = new LinkedList<LinkedList<String>>();
        LinkedList<LinkedList<String>> rows = new LinkedList<LinkedList<String>>();
        LinkedList<LinkedList<String>> cols = new LinkedList<LinkedList<String>>();

        boxes.add(new LinkedList<String>(Arrays.asList("A1","A2","A3","B1","B2","B3","C1","C2","C3")));
        boxes.add(new LinkedList<String>(Arrays.asList("A4","A5","A6","B4","B5","B6","C4","C5","C6")));
        boxes.add(new LinkedList<String>(Arrays.asList("A7","A8","A9","B7","B8","B9","C7","C8","C9")));
        boxes.add(new LinkedList<String>(Arrays.asList("D1","D2","D3","E1","E2","E3","F1","F2","F3")));
        boxes.add(new LinkedList<String>(Arrays.asList("D4","D5","D6","E4","E5","E6","F4","F5","F6")));
        boxes.add(new LinkedList<String>(Arrays.asList("D7","D8","D9","E7","E8","E9","F7","F8","F9")));
        boxes.add(new LinkedList<String>(Arrays.asList("G1","G2","G3","H1","H2","H3","I1","I2","I3")));
        boxes.add(new LinkedList<String>(Arrays.asList("G4","G5","G6","H4","H5","H6","I4","I5","I6")));
        boxes.add(new LinkedList<String>(Arrays.asList("G7","G8","G9","H7","H8","H9","I7","I8","I9")));

        cols.add(new LinkedList<String>(Arrays.asList("A1","B1","C1","D1","E1","F1","G1","H1","I1")));
        cols.add(new LinkedList<String>(Arrays.asList("A2","B2","C2","D2","E2","F2","G2","H2","I2")));
        cols.add(new LinkedList<String>(Arrays.asList("A3","B3","C3","D3","E3","F3","G3","H3","I3")));
        cols.add(new LinkedList<String>(Arrays.asList("A4","B4","C4","D4","E4","F4","G4","H4","I4")));
        cols.add(new LinkedList<String>(Arrays.asList("A5","B5","C5","D5","E5","F5","G5","H5","I5")));
        cols.add(new LinkedList<String>(Arrays.asList("A6","B6","C6","D6","E6","F6","G6","H6","I6")));
        cols.add(new LinkedList<String>(Arrays.asList("A7","B7","C7","D7","E7","F7","G7","H7","I7")));
        cols.add(new LinkedList<String>(Arrays.asList("A8","B8","C8","D8","E8","F8","G8","H8","I8")));
        cols.add(new LinkedList<String>(Arrays.asList("A9","B9","C9","D9","E9","F9","G9","H9","I9")));


      LinkedList<String> q = new LinkedList<String>(); //initially a queue of arcs
        LinkedList<String> variables = new LinkedList<String>();

        for(String k: csp.keySet()){
            variables.add(k);
        }

        LinkedList<String> variables2rows = new LinkedList<String>(variables);

        for(int i =0; i < 9; i++){
            LinkedList<String> row = new LinkedList<String>();
            for(int d =0; d < 9; d++){
                row.add(variables2rows.get(0));
                variables2rows.remove();
            }
            rows.add(row);
        }

        for(int i =0; i < 81; i++){
            q.add(variables.get(i));
        }

        for(int j =0; j < q.size();j++){
            if(csp.get(q.get(j)).length() == 1){ //if the domain is one variable
                String qAtJ = q.get(j);
                String compare = csp.get(q.get(j));
                csp = checkConstraints( csp,  boxes, compare,  qAtJ);
                csp = checkConstraints( csp,  rows, compare,  qAtJ);
                csp = checkConstraints( csp,  cols, compare,  qAtJ);
            }
        }

        System.out.println("csp after reducing the domains " + csp.toString());
         String result =  backtrack_search(csp,q,rows,cols,boxes,state);
         return result;

    }

    public boolean isConsistentCheck(LinkedList<LinkedList<String>> brc, String assignment, String qAtI,LinkedList<String> q, char cc) {

            for (int b = 0; b < brc.size(); b++) {
                if (brc.get(b).contains((qAtI))) { //this should always be true for 1 box
                    for (int d = 0; d < brc.get(b).size(); d++) {
                        if (!brc.get(b).get(d).equals(qAtI)) {
                            int index = q.indexOf(brc.get(b).get(d));
                            if (assignment.charAt(index) == cc) {
                                return false;
                            }

                        }

                    }

                }
            }

        return  true;
    }

    public String backtrack_search(LinkedHashMap<String,String> csp,LinkedList<String> q, LinkedList<LinkedList<String>> rows, LinkedList<LinkedList<String>> cols, LinkedList<LinkedList<String>> boxes, String assignment){
        return backtrack(csp, q, rows, cols, boxes, assignment);

    }

    public String backtrack(LinkedHashMap<String,String> csp, LinkedList<String> q, LinkedList<LinkedList<String>> rows, LinkedList<LinkedList<String>> cols, LinkedList<LinkedList<String>> boxes,  String assignment){

        if(!assignment.contains("_")){
            return assignment;
        }

        String qAtI = "";
        int ind = -1;
        int comp  = Integer.MAX_VALUE;
        for(int i=0; i < q.size(); i++) { //doing the min value of the domains so it runs faster

             if(comp > csp.get(q.get(i)).length() && assignment.charAt(i) == '_'){
                 comp = csp.get(q.get(i)).length();
                 qAtI = q.get(i);
                 ind = i;
             }
        }

            if(assignment.charAt(ind) == '_'){
                String variable = csp.get(qAtI); //domain

                for (char cc : variable.toCharArray()) {

                    boolean isConsistentBox;
                    boolean isConsistentRow;
                    boolean isConsistentCol;

                        isConsistentBox = isConsistentCheck(boxes, assignment, qAtI, q, cc);
                        isConsistentRow = isConsistentCheck(rows, assignment, qAtI, q, cc);
                        isConsistentCol = isConsistentCheck(cols, assignment, qAtI, q, cc);


                        if (isConsistentBox && isConsistentRow && isConsistentCol) {
                            assignment = assignment.substring(0, ind) + cc + assignment.substring(ind + 1);

                            String result = backtrack_search(csp, q, rows, cols, boxes, assignment);


                            if (!result.equals("Failure")) {
                                return result;
                            }
                            assignment = assignment.substring(0, ind) + "_" + assignment.substring(ind + 1);

                        }


                    }


                }

        return "Failure";
    }

}

