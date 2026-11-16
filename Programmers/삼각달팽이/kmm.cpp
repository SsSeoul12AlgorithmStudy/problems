#include <string>
#include <vector>

using namespace std;

//그냥 빙글빙글 돌면 됨
vector<int> solution(int n) {
    vector<int> answer;
    
    //2차원 배열 생성
    vector<vector<int>> board(n, vector<int>(n, 0));

    int x = -1;
    int y = 0;
    int num = 1;
    
    //돌아돌아
    //아래, 오른쪽, 위
    int dx[] = {1, 0, -1};
    int dy[] = {0, 1, -1};
    
    //n번 꺾음
    for (int i = 0; i < n; i++) {
        //n부터 점점 하나씩 깎이며 내려감
        for (int j = i; j < n; j++) {
            
            //방향에 따라 증가
            x += dx[i % 3];
            y += dy[i % 3];

            board[x][y] = num++;
        }
    }

    //2차원 배열을 출력
    for (int i = 0; i < n; i++) {
        for (int j = 0; j <= i; j++) {
            answer.push_back(board[i][j]);
        }
    }

    return answer;
}
// #include <string>
// #include <vector>

// using namespace std;
// //정수 n 매개변수
// //밑변의 길이와 높이가 n인 삼각형에서 맨 위 꼭짓점부터 반시계 방향으로 달팽이 채우기를 진행
// //첫 행부터 마지막 행까지 모두 순서대로 합친 새로운 배열을 return

// //[1,2,x,3,x,x,4,5,6,7]
// //규칙 1:
// //1 이후 0
// //2 이후 1
// //3 이후 2
// //4를 하는데 n이 4니까 쭉 일렬로 나열
// //[y,y,9,y,10,8,y,y,y,y]

// //[y,y,12,y,13,11,y,14,15,10,y,y,y,y,y]

// //[y,y,15,y,16,14,y,17,21,13,y,18,19,20,12,y,y,y,y,y,y]
// //규칙 2:
// //4를 하는거 바로 앞에는 *2로 시작

// //규칙 3:
// //그 뒤로 분할된 뒤 배열들의 끝을 +1하면서 진행
// //규칙 4:
// //다시 앞에서부터 카운트 진행
// //끝 큐까지 도달하면 다시 뒤에서부터 진행....

// //1. 규칙1대로 우선 채워넣기
// //2. 남은 x들 시작좌표, 끝 좌표 채워넣기? (2차원 배열로)
// //3. 2차원 배열 계속 왔다갔다 탐색하면서 앞/뒤 탐색 계속하기


// vector<int> solution(int n) {
//     vector<int> answer;
//     int vectorSize = n * (n + 1) / 2;
//     answer.assign(vectorSize, -1);
    
//     int idx = 0;
//     int jump = 1;
//     int num = 1;
    
//     int saveNum = -1;
//     while(num <= vectorSize) {
//         answer[idx] = num;
//         idx += jump;
        
//         jump++;
//         num++;
//         if(num == n) {
//             answer[idx] = num;
//             for(int i = 0; i < n - 1; i++) {
//                 answer[++idx] = ++num;
//             }
//             break;
//         }
//     }
    
//     //시작과 끝이 적혀있는 1차원 벡터
//     vector<pair<int,int>> cords;
//     //y가 적혀있는 배열들
//     vector<vector<int>> v;
//     for(int i = 0; i < vectorSize; i++) {
        
//     }
    
//     return answer;
// }
