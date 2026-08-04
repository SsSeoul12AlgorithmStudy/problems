import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Solution {
    /*
    * 26-8-4 13:25 ~
    *
    * 균형점의 x좌표를 구하자
    *
    * ===
    *
    * 이분탐색인가?
    * x의 오차가 주어져 있으니 종료 조건은 힘의 균형이 바뀌는 지점 부근에 도착할 때
    *
    * N - 1개의 균형점은 어디에 있을까?
    * 균형이 바뀌는 지점은 자성체 지점이나 두 자성체 사이 밖에 없다
    *
    * */
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int N;
    static double[] pos;
    static double[] mass;
    static int[] balances;

    public static void main(String[] args) throws IOException {
        int T = Integer.parseInt(br.readLine());

        for(int test = 1; test <= T; test++){
            init();
            String answer = solve();
            System.out.printf("#%d %s\n", test, answer);
        }
    }

    static void init() throws IOException {
        N = Integer.parseInt(br.readLine());

        pos = new double[N];
        mass = new double[N];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for(int i = 0; i < N; i++){
            pos[i] = Double.parseDouble(st.nextToken());
        }

        for(int i = 0; i < N; i++){
            mass[i] = Double.parseDouble(st.nextToken());
        }

        balances = new int[N];
    }

    static String solve(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i + 1 < pos.length; i++){
            sb.append(String.format("%.10f", findBalance(pos[i], pos[i + 1])));
        }
        return sb.toString();
    }

    static double findBalance(double start, double end) {
        double lo = start;
        double hi = end;
        while (hi - lo >= 1e-12) {
            double mid = (lo + hi) / 2;
            if (isRightGreater(mid)) {
                hi = mid;
            } else {
                lo = mid;
            }
        }
        return hi;
    }

    static boolean isRightGreater(double mid){
        double leftSum = 0.0;
        double rightSum = 0.0;
        for(int i = 0; i < N; i++){
            if(mid > pos[i]){
                leftSum += mass[i] / ((mid - pos[i]) * (mid - pos[i]));
            }
            if(pos[i] > mid) {
                rightSum += mass[i] / ((pos[i] - mid) * ( pos[i] - mid));
            }
        }
        return leftSum < rightSum;
    }
}