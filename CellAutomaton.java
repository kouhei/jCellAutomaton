import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
public class CellAutomaton{
    //TOWANT: Stringではなくboolで管理
    //TODO: existは曖昧なので、はっきりした名前に
    //FIX: xとy逆に!!!
    private boolean shouldStopAnimate;
    private final String noneStr;
    private final String existStr;
    private final int maxWidth;
    private final int maxHeight;
    private final int judgeCountMin;
    private final int judgeCountMax;
    private boolean[][] cells;

    CellAutomaton(){
    //TODO:引数取るコンストラクタ作る
        this.shouldStopAnimate = false;
        // this.noneStr = " ";
        // this.existStr = "#";

        //背景色を変える
        this.noneStr = "  ";//"\u001b[00;40m  \u001b[00m";//"\u001b[00;47m \u001b[00m";//MEMO:空白の所に表示したい文字を入れる
        //文字色を変える
        this.existStr = "\u001b[00;42m  \u001b[00m";//"\u001b[00;33m#\u001b[00m";//MEMO:#のところに表示したい文字を入れる
        this.maxWidth = 70;
        this.maxHeight = 47;
        this.judgeCountMin = 1;
        this.judgeCountMax = 7;
        this.cells = new boolean[this.maxHeight][this.maxWidth];

        //TODO:設定できるように and HashMapで作る
        //FIX:汎用的に書く!!!
        int[][] initExistCells = new int[3][3];
        initExistCells[0][0] = this.maxHeight/2;
        initExistCells[0][1] = this.maxWidth/2-1;
        initExistCells[1][0] = this.maxHeight/2-1;
        initExistCells[1][1] = this.maxWidth/2;
        initExistCells[2][0] = this.maxHeight/2;
        initExistCells[2][1] = this.maxWidth/2+1;

        initCells(initExistCells);
    }

    public void startAnimate(int stopMillSec){
        int cnt = 0;
        while (!this.shouldStopAnimate){
            System.out.println(show());
            sleep(stopMillSec);
            updateCells();
            // if(cnt++ >= 1000){
            //     this.shouldStopAnimate = true;
            // }
        }


        // int num = 20;
        // long[] asd = new long[num];
        // for(int i = 0; i < num; i++){
        //     long start = System.currentTimeMillis();
        //     for (int j = 0; j < 500; j++) {
        //         System.out.println(show());
        //         sleep(stopMillSec);
        //         if(i % 2 == 0){
        //             updateCells();
        //         }else{
        //             updateCellsA();
        //         }
        //     }
        //     long end = System.currentTimeMillis();
        //     asd[i] = (end - start);
        //     // if(i % 2 == 1){
        //     //     System.out.println();
        //     // }
        // }

        // for (int i = 0; i < num; i++) {
        //     System.out.println(asd[i]);
        //     if(i % 2 == 1){
        //         System.out.println();
        //     }
        // }
        // System.out.println();
        // int sum = 0;
        // int sumA = 0;
        // for (int i=0; i < num; i++) {
        //     if(i % 2 == 0){
        //         sum += asd[i];
        //     }else{
        //         sumA += asd[i];
        //     }
        // }
        // System.out.println(("ave: " + sum * 2 / num));
        // System.out.println(("aveA: " + sumA * 2 / num));
    }

    public void stopAnimate(){
        this.shouldStopAnimate = true;
    };

    //noneStrで塗りつぶし、初期存在セルをexistStrにする
    //initExistCells == {x, y}
    private void initCells(int[][] initExistCells){
        boolean isExistCell = false;
        for (int i = 0; i < this.cells.length; i++) {
            for(int j = 0; j < this.cells[i].length; j++){
                for (int[] existCell : initExistCells) {
                    if(existCell[0] == i && existCell[1] == j){
                        isExistCell = true;
                        break;
                    }
                }
                if(isExistCell){
                    this.cells[i][j] = true;
                    isExistCell = false;
                }else{
                    this.cells[i][j] = false;
                }
            }
        }
    }

    //TODO:関数名再考
    private String show(){
        String res = "";
        for(int i = 0; i < this.cells.length; i++){
            for(int j = 0; j < this.cells[i].length; j++){
                // res += this.cells[i][j];
                if(this.cells[i][j]){
                    res += this.existStr;
                }else{
                    res += this.noneStr;
                }
                if(j == this.maxWidth - 1){
                    //if(i != this.maxHeight - 1){//MEMO:最終行は改行いらない
                        res += "\n";
                    //}
                }else{
                    //MEMO:色で表示するときは不要
                    //res += ", ";
                }
            }
        }
        return res;
    }

    //周囲8マスのなかでexistCellなのが何個あるか
    private int pointCount(int x, int y){
        int cnt = 0;
        if(x-1 >= 0 && y-1 >= 0 && this.cells[x-1][y-1]){
            cnt++;
        }
        if(x+1 <= this.maxHeight-1 && y+1 <= this.maxWidth-1 && this.cells[x+1][y+1]){
            cnt++;
        }

        if(x-1 >= 0 && this.cells[x-1][y]){
            cnt++;
        }
        if(y-1 >= 0 && this.cells[x][y-1]){
            cnt++;
        }

        if(x+1 <= this.maxHeight-1 && this.cells[x+1][y]){
            cnt++;
        }
        if(y+1 <= this.maxWidth-1 && this.cells[x][y+1]){
            cnt++;
        }

        if(x-1 >= 0 && y+1 <= this.maxWidth-1 && this.cells[x-1][y+1]){
            cnt++;
        }
        if(x+1 <= this.maxHeight-1 && y-1 >= 0 && this.cells[x+1][y-1]){
            cnt++;
        }
        return cnt;
    }

    private void updateCellsA(){
        //boolean[][] changedCells = this.cells;
        boolean[][] changedCells = new boolean[this.maxHeight][this.maxWidth];
        for(int i = 0; i < changedCells.length; i++){
            for(int j = 0; j < changedCells[i].length; j++){
                int cnt = pointCount(i, j);
                if(this.judgeCountMin <= cnt && cnt <= this.judgeCountMax){
                    changedCells[i][j] = true;
                }else{
                    changedCells[i][j] = false;
                }
            }
        }
        this.cells = changedCells;
    }


    private void updateCells(){
        // ArrayList<Integer[]> changedCells = new ArrayList<Integer[]>();
        LinkedList<Integer[]> changedCells = new LinkedList<Integer[]>();
        for(int i = 0; i < this.cells.length; i++){
            for(int j = 0; j < this.cells[i].length; j++){
                int cnt = pointCount(i, j);
                if(this.judgeCountMin <= cnt && cnt <= this.judgeCountMax){
                    if(!this.cells[i][j]){
                        changedCells.add(new Integer[]{i, j, 1});
                    }
                }else{
                    // changedCells[i][j] = false;
                    if(this.cells[i][j]){
                        changedCells.add(new Integer[]{i, j, 0});
                    }
                }
            }
        }
        for (Integer[] e : changedCells) {
            if(e[2] == 1){
                this.cells[e[0]][e[1]] = true;
            }else{
                this.cells[e[0]][e[1]] = false;
            }
        }
    }

    private void sleep(int sec){
        try{
            Thread.sleep(sec);
        } catch (InterruptedException e){
        }
    }
}
