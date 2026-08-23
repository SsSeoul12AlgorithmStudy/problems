/*
우선순위 큐로 add할때마다 거리 계산하게 이중for문 접근했다가 25억번(50,000*100*500) 연산해서 시초나고
30,000, 500*100 짜리 배열 도는 방법으로 수정.....
*/

import java.util.*;

class UserSolution {
    static final int MAX_ID = 50000;
    static final int MAX_POWER = 30000;
    int mLimit; 
    Radio[] radios = new Radio[MAX_ID]; //입력받은 순으로 통신기 저장
    Radio[] radioById = new Radio[MAX_ID + 1]; //아이디로 통신기 저장
    int radioCount;
    int[] count = new int[MAX_POWER + 1]; //해당 파워를 갖는 통신기의 개수 저장

    class Radio {
        int id, freq, y, x;

        Radio(int id, int freq, int y, int x) {
            this.id = id;
            this.freq = freq;
            this.y = y;
            this.x = x;
        }
    }

    void init(int N, int mLimit) {
        this.mLimit = mLimit; //무선통신기 연결 파워제한
        radioCount = 0; //통신기 개수
        Arrays.fill(radioById, null);
    }

    void addRadio(int K, int[] mID, int[] mFreq, int[] mY, int[] mX) {
        for (int i = 0; i < K; i++) {
            Radio radio = new Radio(mID[i],mFreq[i],mY[i],mX[i]); 
            radios[radioCount++] = radio;
            radioById[radio.id] = radio;
        }
    }

    int getMinPower(int mID, int mCount) {
        Radio target = radioById[mID]; //무선연결을 시도하는 통신기
        Arrays.fill(count, 0);

        // 존재하는 모든 통신기 돌면서 파워 계산 
        for (int i = 0; i < radioCount; i++) {
            Radio radio = radios[i];

            if (radio.id == mID) continue; // 나 자신은 연결 불가
            
            int distance = Math.abs(target.x - radio.x) + Math.abs(target.y - radio.y); 
            int power = distance * 10; //필요한 파워 계산
            if (target.freq != radio.freq) power += 1000;

            if (power > mLimit) continue; //제한된 파워보다 높으면 불가

            count[power]++; //해당 파워에 속하는 통신기 개수 저장
        }

        int result = 0; //파워 합산
        int selected = 0; //선택한 통신기 개수

        //파워가 약한 순으로 탐색하면서 통신기 고르기
        for (int power = 0; power <= mLimit; power++) {
            if (count[power] == 0) continue;

            int need = mCount - selected; //선택해야하는 통신기 개수
            int take = Math.min(count[power], need); //현재 파워에서 몇개의 통신기를 선택했는지

            result += power * take;
            selected += take;

            if (selected == mCount)  break; //다 골랐으면 탐색 종료
        }

        return result;
    }
}
