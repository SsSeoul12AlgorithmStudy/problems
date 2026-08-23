import java.util.*;

class UserSolution {

    final int[] dr = { -1, 0, 1, 0 };
    final int[] dc = { 0, 1, 0, -1 };

    int N;
    int[][] city;
    Map<String, Integer> gradeToIndexMap;
    Map<Integer, String> indexToGradeMap;

    List<String> grades;
    StringBuilder sb;

    /*
    * 초기화 함수
    * 최초 한번만 호출
    * */
	void init(int N, int M, String mGrade[][]) {
        this.N = N;

        this.grades = new ArrayList<>();
        this.sb = new StringBuilder();

        for(int length = 1; length <= 3; length++){
            makeGrades(length, "");
        }

        this.grades.sort((a, b) -> a.compareTo(b));

        this.gradeToIndexMap = new HashMap<>();
        this.indexToGradeMap = new HashMap<>();
        for(int i = 0; i < grades.size(); i++){
            gradeToIndexMap.put(grades.get(i), i);
            indexToGradeMap.put(i, grades.get(i));
        }

//        System.out.println(gradeToIndexMap);

        this.city = new int[N][N];
        for(int r = 0; r < N; r++){
            for(int c = 0; c < N; c++){
                city[r][c] = gradeToIndexMap.get(mGrade[r][c]);
            }
        }

//        printCity_debug();
	}

    void makeGrades(int depth, String grade){
        if(depth <= 0){
            this.grades.add(grade);
            return;
        }

        for(int i = 0; i < 26; i++){
            makeGrades(depth - 1, grade + String.valueOf((char) ('A' + i)));
        }
    }

    /*
    * 중간에 값을 변경하는 함수
    * 최대 1만번 호출
    * */
	void change(int mRow, int mCol, int mDir, int mLength, String mGrade) {
        if(mDir == 0){
            for(int i = 0; i < mLength; i++){
                city[mRow + i][mCol] = gradeToIndexMap.get(mGrade);
            }

//            printCity_debug();

            return;
        }
        for(int i = 0; i < mLength; i++){
            city[mRow][mCol + i] = gradeToIndexMap.get(mGrade);
        }

//       printCity_debug();

	}

    void printCity_debug(){
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                System.out.print(city[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /*
    * L거리만큼 이동할 때 필요한 최소 권한을 구하는 함수
    * 최대 200번 호출
    * */
	String calculate(int L, int sRow, int sCol, int eRow, int eCol) {
        int lo = -1;
        int hi = this.grades.size();

        while(lo + 1 < hi){
            int mid = (lo + hi) / 2;

//            System.out.println("binary mid = " + mid);

            if(canArriveAt(L, mid, sRow, sCol, eRow, eCol)){
                lo = mid;
            }
            else{
                hi = mid;
            }
        }

//        System.out.println(lo + " " + hi);

        return indexToGradeMap.get(lo);
	}

    boolean canArriveAt(int L, int gradeLimit, int sRow, int sCol, int eRow, int eCol){
        boolean[][] visited = new boolean[N][N];
        Queue<int[]> qu = new LinkedList<>();
        qu.offer(new int[] { sRow, sCol, 0 });

        while(!qu.isEmpty()){
            int[] cur = qu.poll();

            for(int i = 0; i < 4; i++){
                int nRow = cur[0] + dr[i];
                int nCol = cur[1] + dc[i];

                if(outOfRange(nRow) || outOfRange(nCol)){
                    continue;
                }

                if(visited[nRow][nCol]){
                    continue;
                }

                if(city[nRow][nCol] < gradeLimit){
                    continue;
                }

                if(nRow == eRow && nCol == eCol){
                    return cur[2] + 1 <= L;
                }

                qu.offer(new int[] { nRow, nCol, cur[2] + 1 });
                visited[nRow][nCol] = true;
            }
        }

        return false;
    }

    boolean outOfRange(int coord){
        return coord < 0 || coord >= N;
    }
}

