import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.Random;

public class cplex {

    cplex(int[][] id, int[][] od, int[][]place) {
        int days=id.length;
        int vibs=id[0].length;//id[0].length;
        Random rng=new Random();
        //int[][] id = {{100, 90, 100}, {85, 80, 10}, {0, 0, 50}, {34, 42, 0}};
        //int[][] od = {{100, 40, 50}, {20, 70, 20}, {60, 35, 30}, {16, 55, 24}};
        //int[][] place = places(days,vibs);//{{0, 0, 1}, {0, 0, 1}, {0, 0, 1}, {0, 0, 1}};
        int[] grcost = {75, 80};
        int[] Cap = capacities();//{90, 90, 90, 90, 80};
        double[][] movementcost = randmovecost(Cap.length,rng);//{{0.5, 0.6, 0.7, 0.8, 0.7}, {0.4, 0.5, 0.6, 0.8, 0.6}, {0.6, 0.7, 0.3, 0.5, 0.4}};
        double[] sorter = {104.4, 105.9};
        double[][] handling = randhandlingcost(vibs,rng);//{{2, 0}, {1, 0}, {1.5, 0}};

        try {
            IloCplex cple = new IloCplex();
            //DecisionVar x
            IloNumVar[][] x = new IloNumVar[vibs][];
            for (int i = 0; i < vibs; i++) {
                x[i] = cple.boolVarArray(2);
            }
            //DecisionVar Y
            IloNumVar[][][] Y = new IloNumVar[vibs][days][];
            for (int i = 0; i < vibs; i++) {
                for (int d = 0; d < days; d++) {
                    Y[i][d] = cple.boolVarArray(Cap.length);
                }
            }
            //DecisionVar A
            IloNumVar[][] A = new IloNumVar[days][];
            for(int d=0;d<days;d++){
                A[d]=cple.numVarArray(Cap.length,0,Double.MAX_VALUE);
            }
            //DecisionVar M
            IloNumVar[][][] M = new IloNumVar[vibs][days][];
            for(int d=0;d<days;d++){
                for (int i=0;i<vibs;i++){
                    M[i][d]=cple.numVarArray(Cap.length,0,Double.MAX_VALUE);
                }
            }
            //DecisionVar O
            IloNumVar[][][] O = new IloNumVar[vibs][days][];
            for(int d=0;d<days;d++){
                for (int i=0;i<vibs;i++){
                    O[i][d]=cple.numVarArray(Cap.length,0,Double.MAX_VALUE);
                }
            }
            //DecisionVar B
            IloNumVar[][] B = new IloNumVar[days][];
            for (int n = 0; n < days; n++) {
                B[n] = cple.boolVarArray(Cap.length);
            }
            //DecisionVar L
            IloNumVar[][][] L = new IloNumVar[vibs][days][];
            for (int i = 0; i < vibs; i++) {
                for (int d = 0; d < days; d++) {
                    L[i][d] = cple.boolVarArray(Cap.length);
                }
            }

            //Objective
            IloLinearNumExpr obj = cple.linearNumExpr();
            for (int d = 0; d < days; d++) {
                for (int i = 0; i < vibs; i++) {
                    for (int n = 0; n < Cap.length; n++) {
                        obj.addTerm(movementcost[0][n] * (1-place[d][i]), M[i][d][n]);
                        obj.addTerm(movementcost[1][n] * (place[d][i]), M[i][d][n]);
                        obj.addTerm(movementcost[2][n], O[i][d][n]);
                    }
                }
            }
            for (int d = 0; d < days; d++) {
                for (int i = 0; i < vibs; i++) {
                    for (int j = 0; j < 2; j++) {
                        obj.addTerm(id[d][i] * grcost[j] * place[d][i], x[i][j]);
                        obj.addTerm(id[d][i] * sorter[j] * (1 - place[d][i]), x[i][j]);
                        obj.addTerm(od[d][i] * handling[i][j], x[i][j]);
                    }
                }
            }
            cple.addMinimize(obj);

            //Constraint1
            for (int a = 0; a < vibs; a++) {
                IloLinearNumExpr const1 = cple.linearNumExpr();
                for (int b = 0; b < 2; b++) {
                    const1.addTerm(1, x[a][b]);
                }
                cple.addEq(const1, 1);
            }

            //Constraint2
            for (int n = 0; n < Cap.length; n++) {
                IloLinearNumExpr const2 = cple.linearNumExpr();
                const2.addTerm(1, A[0][n]);
                cple.addEq(const2,0);
            }

            //Constraint3
            for (int d = 0; d < days; d++) {
                for (int n = 0; n < Cap.length; n++) {
                    IloLinearNumExpr const3 = cple.linearNumExpr();
                    for (int i = 0; i < vibs; i++) {
                        const3.addTerm(1, Y[i][d][n]);
                    }
                    cple.addLe(const3, 1);
                }
            }
            //Constraint 4,6 and 7
            for (int d = 0; d < days - 1; d++) {
                for (int i = 0; i < vibs; i++) {
                    for (int n = 0; n < Cap.length; n++) {
                        IloLinearNumExpr const4 = cple.linearNumExpr();
                        IloLinearNumExpr const6 = cple.linearNumExpr();
                        IloLinearNumExpr const7 = cple.linearNumExpr();
                        const4.addTerm(1, Y[i][d + 1][n]);
                        const4.addTerm(-1, L[i][d][n]);
                        const6.addTerm(1, L[i][d][n]);
                        const6.addTerm(-1, B[d + 1][n]);
                        const7.addTerm(1, L[i][d][n]);
                        const7.addTerm(-1, Y[i][d][n]);
                        const7.addTerm(-1, B[d + 1][n]);
                        cple.addGe(const4, 0);
                        cple.addLe(const6, 0);
                        cple.addGe(const7, -1);
                    }
                }
            }

            //Constraint 5,9,10 and 11

            for (int d = 0; d < days; d++) {
                for (int i = 0; i < vibs; i++) {
                    for (int n = 0; n < Cap.length; n++) {
                        IloLinearNumExpr const5 = cple.linearNumExpr();
                        IloLinearNumExpr const9 = cple.linearNumExpr();
                        IloLinearNumExpr const10 = cple.linearNumExpr();
                        IloLinearNumExpr const11 = cple.linearNumExpr();
                        const5.addTerm(1, L[i][d][n]);
                        const5.addTerm(-1, Y[i][d][n]);
                        const9.addTerm(1, A[d][n]);
                        const9.addTerm(1, M[i][d][n]);
                        const10.addTerm(1, M[i][d][n]);
                        const10.addTerm(-id[d][i], Y[i][d][n]);
                        const11.addTerm(1, O[i][d][n]);
                        const11.addTerm(-od[d][i], Y[i][d][n]);
                        cple.addLe(const5, 0);
                        cple.addLe(const9, Cap[n]);
                        cple.addLe(const10, 0);
                        cple.addLe(const11, 0);
                    }
                }
            }

            //Constraint 8,12 and 13
            for (int d = 0; d < days; d++) {
                for (int i = 0; i < vibs; i++) {
                    IloLinearNumExpr const8 = cple.linearNumExpr();
                    IloLinearNumExpr const12 = cple.linearNumExpr();
                    IloLinearNumExpr const13 = cple.linearNumExpr();
                    for (int n = 0; n < Cap.length; n++) {
                        const8.addTerm(Cap[n], Y[i][d][n]);
                        const12.addTerm(1, M[i][d][n]);
                        const13.addTerm(1, O[i][d][n]);
                    }
                    cple.addGe(const8, id[d][i]);
                    cple.addEq(const12, id[d][i]);
                    cple.addEq(const13, od[d][i]);
                }
            }

            //Constraint 14
            for (int d = 0; d < days - 1; d++) {
                for (int n = 0; n < Cap.length; n++) {
                    IloLinearNumExpr const14 = cple.linearNumExpr();
                    const14.addTerm(1, A[d + 1][n]);
                    const14.addTerm(-1, A[d][n]);
                    for (int i = 0; i < vibs; i++) {
                        const14.addTerm(-1, M[i][d][n]);
                        const14.addTerm(1, O[i][d][n]);
                    }
                    cple.addEq(const14, 0);
                }
            }

            //Constraint 15 and 16
            for (int d = 1; d < days; d++) {
                for (int n = 0; n < Cap.length; n++) {
                    IloLinearNumExpr const15 = cple.linearNumExpr();
                    IloLinearNumExpr const16 = cple.linearNumExpr();
                    const15.addTerm(1, B[d][n]);
                    const15.addTerm(-1, A[d][n]);
                    const16.addTerm(Cap[n], B[d][n]);
                    const16.addTerm(-1, A[d][n]);
                    cple.addLe(const15, 0);
                    cple.addGe(const16, 0);
                }
            }

            if (cple.solve()) {
                int p = 0;
                int c = 0;
                System.out.println("obj =" + cple.getObjValue());
                for (int i = 0; i < vibs; i++) {
                    for (int j = 0; j < 2; j++) {
                        //System.out.print((int) cple.getValue(x[i][j]));
                        if (j == 0) {
                            p += (int) cple.getValue(x[i][j]);
                            //System.out.print(",");
                        } else {
                            c += (int) cple.getValue(x[i][j]);
                        }
                    }
                    //System.out.println();
                }
                System.out.println("pool: " + p + " country: " + c);
            } else {
                System.out.println("System can not be solved");
            }
            cple.end();
        } catch (IloException e) {
            e.printStackTrace();
        }

    }

    private int[] capacities() {
        int adres=1000;
        int[] caps= new int[adres];
        for (int i=0;i<adres;i++){
            caps[i]=500;
        }
        return caps;
    }

    private double[][] randhandlingcost(int l, Random rng) {
        double[][] cost=new double[l][2];
        for (int i=0;i<l;i++){
            for (int j=0;j<2;j++){
                if(j==0){
                    cost[i][j]=rng.nextDouble();
                }
                else{
                    cost[i][j]=0;
                }
            }
        }
        return cost;
    }

    private double[][] randmovecost(int length, Random rng) {
        double[][] cost=new double[3][length];
        for (int i=0;i<3;i++){
            for (int j=0;j<length;j++){
                cost[i][j]=rng.nextDouble();
            }
        }
        return cost;
    }

    private int[][] places(int length, ArrayList<VIB> vibs) {
        int[][] place= new int[length][vibs.size()];
        for (int d=0;d<length;d++){
            for (int i=0;i<vibs.size();i++){
                place[d][i]=vibs.get(i).place;
            }
        }
        return place;
    }


}
