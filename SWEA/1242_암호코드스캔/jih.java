import java.io.*;
import java.util.*;

public class Solution {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int height = 0;
    static boolean[][] visited;
    static int digitSum = 0;
    static int N, M;
    static String[] map;
    static double[][] ratios = {
            {3, 2, 1, 1},
            {2, 2, 2, 1},
            {2, 1, 2, 2},
            {1, 4, 1, 1},
            {1, 1, 3, 2},
            {1, 2, 3, 1},
            {1, 1, 1, 4},
            {1, 3, 1, 2},
            {1, 2, 1, 3},
            {3, 1, 1, 2}
    };

    public static void main(String[] args) throws IOException {
        int T = Integer.parseInt(br.readLine());
        for(int test = 1; test <= T; test++){
            init();
            long answer = solve();
            System.out.println("#" + test + " " + answer);
        }
    }

    static void init() throws IOException {

        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line);
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new String[N];

        for(int r = 0; r < N; r++){
            line = br.readLine();

            StringBuilder sb = new StringBuilder();
            for(int c = 0; c < M; c++){
                String hex = line.substring(c, c + 1);
                int i = Integer.parseInt(hex, 16);
                String binary = Integer.toString(i , 2);

                for(int j = 0; j < 4 - binary.length(); j++){
                    sb.append("0");
                }
                sb.append(binary);
            }

            map[r] = sb.toString();
        }

//        System.out.println(map[0]);
//        System.out.println(Arrays.toString(map));

        visited = new boolean[N][M * 4];
    }

    static long solve(){
        long sum = 0;

//        System.out.println(map[0].length() - 1);

        for(int row = 0; row < map.length - 4; row++){
            for(int col = map[0].length() - 1; col >= 0; col--){
                if(visited[row][col]){
                    continue;
                }

                if(map[row].charAt(col) == '1' && ( col == map[0].length() - 1 || map[row].charAt(col + 1) == '0')){
                    int scale = getScale(row, col);

//                    System.out.println("row, col, scale  = " + row + " " + col + " " + scale);

                    if(scale == -1){
                        continue;
                    }

                    if(isCode(row, col - 56 * scale + 1, scale)){
//                        System.out.println("visit = " + row + " " + col);

                        visit(row, col - 56 * scale + 1, 56 * scale, height);
                        sum += digitSum;
                    }
                }
            }
        }

        return sum;
    }

    static int getScale(int row, int col){
        // 뒤에서 앞으로 스캔하면서 스케일을 구한다
        int[] counter = new int[4];
        int i = 3;
        for(int c = col; c >= 0; c--){
            if(i % 2 == 1 && map[row].charAt(c) != '1'){
                i--;
            }
            if(i % 2 == 0 && map[row].charAt(c) != '0'){
                i--;
            }
            if(i < 0){
                break;
            }
            counter[i]++;
        }

//        System.out.println("getScale : " + Arrays.toString(counter));

        int scale = Integer.MAX_VALUE;
        for(int j = 0; j < 4; j++){
            if(counter[j] < scale){
                scale = counter[j];
            }
        }

        if(scale <= 0){
            return -1;
        }

        for(int j = 0; j < ratios.length; j++){
            boolean isMatched = true;

            double[] ratio = ratios[j];

            for(int k = 0; k < 4; k++){
                if(ratio[k] != ratio(counter[k], scale)){
                    isMatched = false;
                }
            }

            if(isMatched){
                return scale;
            }
        }

        return -1;
    }

    static double ratio(int count, int scale){
        return (double) count / scale;
    }

    // 암호코드 규칙을 만족하고, 주변이 0으로 둘러 쌓여있고, 높이가 같은 숫자인가
    static boolean isCode(int row, int col, int scale){
        if(col < 0){
            return false;
        }

        int oddSum = 0;
        int evenSum = 0;
        int validCode = 0;

        digitSum = 0;

        for(int i = 0; i < 8; i++){
            int digit = getDigit(row, col + scale * 7 * i, scale);

            if(digit == -1){
//                System.out.println("not digit");
                return false;
            }

            if(i % 2 == 0){
                oddSum += digit;
            }
            if(i % 2 == 1 && i != 7){
                evenSum += digit;
            }
            if(i == 7){
                validCode = digit;
            }

            digitSum += digit;
        }

        if((oddSum * 3 + evenSum + validCode) % 10 != 0){
//            System.out.println("validation failed");
            return false;
        }

        height = 1;
        for(int r = row; r + 1 < N; r++){
            String line = map[r].substring(col, col + scale * 8);
            String nextLine = map[r + 1].substring(col, col + scale * 8);
            if(!line.equals(nextLine)){
                break;
            }
            height++;
        }

        if(height < 5){
//            System.out.println("height < 5");
            return false;
        }

        return true;
    }

    static int getDigit(int row, int col, int scale){
        int[] counter = new int[4];
        int i = 0;
        for(int c = col; c < col + scale * 7; c++){
            if(i % 2 == 1 && map[row].charAt(c) != '1'){
                i++;
            }
            if(i % 2 == 0 && map[row].charAt(c) != '0'){
                i++;
            }
            if(i > 3){
                break;
            }
            counter[i]++;
        }

//        System.out.println(map[row].substring(col, col + scale * 7));

//        System.out.println("getDigit : " + Arrays.toString(counter));
//

        int minCounter = Integer.MAX_VALUE;
        for(int j = 0; j < 4; j++){
            if(counter[j] < minCounter){
                minCounter = counter[j];
            }
        }

        if(minCounter != scale){
            return -1;
        }

        for(int j = 0; j < ratios.length; j++){
            boolean isMatched = true;

            double[] ratio = ratios[j];

            for(int k = 0; k < 4; k++){
                if(ratio[k] != ratio(counter[k], scale)){
//                    System.out.println("pass " + j);
                    isMatched = false;
                }
            }

            if(isMatched){
//                System.out.println("matched " + j);
                return j;
            }
        }

//        System.out.println("non match");
        return -1;
    }

    static void visit(int row, int col, int width, int height){
        for(int r = 0; r < height; r++){
            for(int c = 0; c < width; c++){
                visited[row + r][col + c] = true;
            }
        }
    }
}