#include "msp.h"
#include "Clock.h"
#include "motor.h"
#include "init.h"
#include <stdio.h>
#include <stdint.h>

#define SPEED 2300
#define LEFT_SPEED 2000
#define RIGHT_SPEED 2000
#define LOW_SPEED 2100
#define MAX_SPEED 4000
#define ROTATE_SPEED 3000

void section1() {
    while(1) {
        P5->OUT |= 0x08;
        P9->OUT |= 0x04;

        P7->DIR = 0xFF;
        P7->OUT = 0xFF;

        Clock_Delay1us(10);

        P7->DIR = 0x00;
        int timerTest;
        int ts1;
        int ts2;
        int ts3;
        int ts4;
        int ts5;
        int ts6;
        int ts7;
        int ts8;
        ts1 = 0;
        ts2 = 0;
        ts3 = 0;
        ts4 = 0;
        ts5 = 0;
        ts6 = 0;
        ts7 = 0;
        ts8 = 0;
        for(timerTest=0 ; timerTest<10000 ; timerTest++) {
            int s1;
            int s2;
            int s3;
            int s4;
            int s5;
            int s6;
            int s7;
            int s8;
            s1 = P7->IN & 0x01; // 1
            s2 = P7->IN & 0x02; // 2
            s3 = P7->IN & 0x04; // 3
            s4 = P7->IN & 0x08; // 4
            s5 = P7->IN & 0x10; // 5
            s6 = P7->IN & 0x20; // 6
            s7 = P7->IN & 0x40; // 7
            s8 = P7->IN & 0x80; // 8



            if(!ts1 && !s1) {
                ts1 = timerTest;
            }
            if(!ts2 && !s2) {
                ts2 = timerTest;
            }
            if(!ts3 && !s3) {
                ts3 = timerTest;
            }
            if(!ts4 && !s4) {
                ts4 = timerTest;
            }
            if(!ts5 && !s5) {
                ts5 = timerTest;
            }
            if(!ts6 && !s6) {
                ts6 = timerTest;
            }
            if(!ts7 && !s7) {
                ts7 = timerTest;
            }
            if(!ts8 && !s8) {
                ts8 = timerTest;
            }
        }

        right_forward();
        left_forward();


        if(ts1 > 200 && ts1<800) {
            move_forward(1, SPEED);
            rotate_right_degree(ROTATE_SPEED, 70);
            move_forward(1, SPEED);

            P5->OUT &= ~0x08;
            P9->OUT &= ~0x04;
            break;
        }
        else if(ts2 > 200 && ts2 < 800) {
            left_forward();
            right_backward();
            move(1000,1000);
        }
        else if (ts7 > 200 && ts7 < 800) {
            left_backward();
           right_forward();
           move(1000,1000);
        } 
        else if (ts3 > 200 && ts3 < 800) {
            move(LOW_SPEED, SPEED);
        }
        
        else if (ts6 > 200 && ts6 < 800) {
            move(SPEED, LOW_SPEED);
        } 
        
        else {
            move(SPEED, SPEED);
        }

        P5->OUT &= ~0x08;
        P9->OUT &= ~0x04;

        Clock_Delay1ms(10);
    }
}

void section1_without_tracing() {
    while(1) {
        P5->OUT |= 0x08;
        P9->OUT |= 0x04;

        P7->DIR = 0xFF;
        P7->OUT = 0xFF;

        Clock_Delay1us(10);

        P7->DIR = 0x00;
        int timerTest;
        int ts1;
        int ts2;
        int ts3;
        int ts4;
        int ts5;
        int ts6;
        int ts7;
        int ts8;
        ts1 = 0;
        ts2 = 0;
        ts3 = 0;
        ts4 = 0;
        ts5 = 0;
        ts6 = 0;
        ts7 = 0;
        ts8 = 0;
        for(timerTest=0 ; timerTest<10000 ; timerTest++) {
            int s1;
            int s2;
            int s3;
            int s4;
            int s5;
            int s6;
            int s7;
            int s8;
            s1 = P7->IN & 0x01; // 1
            s2 = P7->IN & 0x02; // 2
            s3 = P7->IN & 0x04; // 3
            s4 = P7->IN & 0x08; // 4
            s5 = P7->IN & 0x10; // 5
            s6 = P7->IN & 0x20; // 6
            s7 = P7->IN & 0x40; // 7
            s8 = P7->IN & 0x80; // 8



            if(!ts1 && !s1) {
                ts1 = timerTest;
            }
            if(!ts2 && !s2) {
                ts2 = timerTest;
            }
            if(!ts3 && !s3) {
                ts3 = timerTest;
            }
            if(!ts4 && !s4) {
                ts4 = timerTest;
            }
            if(!ts5 && !s5) {
                ts5 = timerTest;
            }
            if(!ts6 && !s6) {
                ts6 = timerTest;
            }
            if(!ts7 && !s7) {
                ts7 = timerTest;
            }
            if(!ts8 && !s8) {
                ts8 = timerTest;
            }
        }

        right_forward();
        left_forward();


        if(ts1 > 200 && ts1<800) {
            move_forward(1, SPEED);
            rotate_right_degree(ROTATE_SPEED, 70);
            move_forward(1, SPEED);

            P5->OUT &= ~0x08;
            P9->OUT &= ~0x04;
            break;
        }
        
        else if (ts3 > 200 && ts3 < 800) {
            move(LOW_SPEED, SPEED);
        }
        
        else if (ts6 > 200 && ts6 < 800) {
            move(SPEED, LOW_SPEED);
        } 
        
        else {
            move(SPEED, SPEED);
        }

        P5->OUT &= ~0x08;
        P9->OUT &= ~0x04;

        Clock_Delay1ms(10);
    }
}


void move_until_white() {
    while(1) {
        P5->OUT |= 0x08;
        P9->OUT |= 0x04;

        P7->DIR = 0xFF;
        P7->OUT = 0xFF;

        Clock_Delay1us(10);

        P7->DIR = 0x00;
        int timerTest;
        int ts1;
        int ts2;
        int ts3;
        int ts4;
        int ts5;
        int ts6;
        int ts7;
        int ts8;
        ts1 = 0;
        ts2 = 0;
        ts3 = 0;
        ts4 = 0;
        ts5 = 0;
        ts6 = 0;
        ts7 = 0;
        ts8 = 0;
        for(timerTest=0 ; timerTest<10000 ; timerTest++) {
            int s1;
            int s2;
            int s3;
            int s4;
            int s5;
            int s6;
            int s7;
            int s8;
            s1 = P7->IN & 0x01; // 1
            s2 = P7->IN & 0x02; // 2
            s3 = P7->IN & 0x04; // 3
            s4 = P7->IN & 0x08; // 4
            s5 = P7->IN & 0x10; // 5
            s6 = P7->IN & 0x20; // 6
            s7 = P7->IN & 0x40; // 7
            s8 = P7->IN & 0x80; // 8



            if(!ts1 && !s1) {
                ts1 = timerTest;
            }
            if(!ts2 && !s2) {
                ts2 = timerTest;
            }
            if(!ts3 && !s3) {
                ts3 = timerTest;
            }
            if(!ts4 && !s4) {
                ts4 = timerTest;
            }
            if(!ts5 && !s5) {
                ts5 = timerTest;
            }
            if(!ts6 && !s6) {
                ts6 = timerTest;
            }
            if(!ts7 && !s7) {
                ts7 = timerTest;
            }
            if(!ts8 && !s8) {
                ts8 = timerTest;
            }
        }

        right_forward();
        left_forward();


        if(ts4<200 && ts5<200) {
            move(0,0);
            P5->OUT &= ~0x08;
            P9->OUT &= ~0x04;
            break;
        }
        else if (ts3 > 200 && ts3 < 800) {
            move(LOW_SPEED, SPEED);
        }
        else if (ts6 > 200 && ts6 < 800) {
            move(SPEED, LOW_SPEED);
        } 
        else {
            move(SPEED, SPEED);
        }

        P5->OUT &= ~0x08;
        P9->OUT &= ~0x04;

        Clock_Delay1ms(10);
    }
}

void move_lap2() {
    while(1) {
        P5->OUT |= 0x08;
        P9->OUT |= 0x04;

        P7->DIR = 0xFF;
        P7->OUT = 0xFF;

        Clock_Delay1us(10);

        P7->DIR = 0x00;
        int timerTest;
        int ts1;
        int ts2;
        int ts3;
        int ts4;
        int ts5;
        int ts6;
        int ts7;
        int ts8;
        ts1 = 0;
        ts2 = 0;
        ts3 = 0;
        ts4 = 0;
        ts5 = 0;
        ts6 = 0;
        ts7 = 0;
        ts8 = 0;
        for(timerTest=0 ; timerTest<10000 ; timerTest++) {
            int s1;
            int s2;
            int s3;
            int s4;
            int s5;
            int s6;
            int s7;
            int s8;
            s1 = P7->IN & 0x01; // 1
            s2 = P7->IN & 0x02; // 2
            s3 = P7->IN & 0x04; // 3
            s4 = P7->IN & 0x08; // 4
            s5 = P7->IN & 0x10; // 5
            s6 = P7->IN & 0x20; // 6
            s7 = P7->IN & 0x40; // 7
            s8 = P7->IN & 0x80; // 8



            if(!ts1 && !s1) {
                ts1 = timerTest;
            }
            if(!ts2 && !s2) {
                ts2 = timerTest;
            }
            if(!ts3 && !s3) {
                ts3 = timerTest;
            }
            if(!ts4 && !s4) {
                ts4 = timerTest;
            }
            if(!ts5 && !s5) {
                ts5 = timerTest;
            }
            if(!ts6 && !s6) {
                ts6 = timerTest;
            }
            if(!ts7 && !s7) {
                ts7 = timerTest;
            }
            if(!ts8 && !s8) {
                ts8 = timerTest;
            }
        }

        right_forward();
        left_forward();


        if((ts4>200 && ts5>200) && (ts3>200 || ts6>200)) {
            move_forward(2, SPEED);
            P5->OUT &= ~0x08;
            P9->OUT &= ~0x04;
            break;
        }
        else if (ts3 > 200 && ts3 < 800) {
            move(LOW_SPEED, SPEED);
        }
        else if (ts6 > 200 && ts6 < 800) {
            move(SPEED, LOW_SPEED);
        } 
        else {
            move(SPEED, SPEED);
        }

        P5->OUT &= ~0x08;
        P9->OUT &= ~0x04;

        Clock_Delay1ms(10);
    }
}

void section3() {

    while(1) {
        P5->OUT |= 0x08;
        P9->OUT |= 0x04;

        P7->DIR = 0xFF;
        P7->OUT = 0xFF;

        Clock_Delay1us(10);

        P7->DIR = 0x00;
        int timerTest;
        int ts1;
        int ts2;
        int ts3;
        int ts4;
        int ts5;
        int ts6;
        int ts7;
        int ts8;
        ts1 = 0;
        ts2 = 0;
        ts3 = 0;
        ts4 = 0;
        ts5 = 0;
        ts6 = 0;
        ts7 = 0;
        ts8 = 0;
        for(timerTest=0 ; timerTest<10000 ; timerTest++) {
            int s1;
            int s2;
            int s3;
            int s4;
            int s5;
            int s6;
            int s7;
            int s8;
            s1 = P7->IN & 0x01; // 1
            s2 = P7->IN & 0x02; // 2
            s3 = P7->IN & 0x04; // 3
            s4 = P7->IN & 0x08; // 4
            s5 = P7->IN & 0x10; // 5
            s6 = P7->IN & 0x20; // 6
            s7 = P7->IN & 0x40; // 7
            s8 = P7->IN & 0x80; // 8



            if(!ts1 && !s1) {
                ts1 = timerTest;
            }
            if(!ts2 && !s2) {
                ts2 = timerTest;
            }
            if(!ts3 && !s3) {
                ts3 = timerTest;
            }
            if(!ts4 && !s4) {
                ts4 = timerTest;
            }
            if(!ts5 && !s5) {
                ts5 = timerTest;
            }
            if(!ts6 && !s6) {
                ts6 = timerTest;
            }
            if(!ts7 && !s7) {
                ts7 = timerTest;
            }
            if(!ts8 && !s8) {
                ts8 = timerTest;
            }
        }

        right_forward();
        left_forward();


        if(ts1 > 200 || ts8>200) {

            move_forward(4, SPEED);
            P5->OUT &= ~0x08;
            P9->OUT &= ~0x04;
            break;
        }
        else if (ts7 > 200 && ts7 < 800) {
            left_backward();
           right_forward();
           move(1000,1000);
        } 
        else if(ts2 > 200 && ts2 < 800) {
            left_forward();
            right_backward();
            move(1000,1000);
        }
        else if (ts3 > 200 && ts3 < 800) {
            move(LOW_SPEED, SPEED);
        }
        
        else if (ts6 > 200 && ts6 < 800) {
            move(SPEED, LOW_SPEED);
        } 
        
        else {
            move(SPEED, SPEED);
        }

        P5->OUT &= ~0x08;
        P9->OUT &= ~0x04;

        Clock_Delay1ms(10);
    }
}


void section4() {
    while(1) {
        P5->OUT |= 0x08;
        P9->OUT |= 0x04;

        P7->DIR = 0xFF;
        P7->OUT = 0xFF;

        Clock_Delay1us(10);

        P7->DIR = 0x00;
        int timerTest;
        int ts1;
        int ts2;
        int ts3;
        int ts4;
        int ts5;
        int ts6;
        int ts7;
        int ts8;
        ts1 = 0;
        ts2 = 0;
        ts3 = 0;
        ts4 = 0;
        ts5 = 0;
        ts6 = 0;
        ts7 = 0;
        ts8 = 0;
        for(timerTest=0 ; timerTest<10000 ; timerTest++) {
            int s1;
            int s2;
            int s3;
            int s4;
            int s5;
            int s6;
            int s7;
            int s8;
            s1 = P7->IN & 0x01; // 1
            s2 = P7->IN & 0x02; // 2
            s3 = P7->IN & 0x04; // 3
            s4 = P7->IN & 0x08; // 4
            s5 = P7->IN & 0x10; // 5
            s6 = P7->IN & 0x20; // 6
            s7 = P7->IN & 0x40; // 7
            s8 = P7->IN & 0x80; // 8



            if(!ts1 && !s1) {
                ts1 = timerTest;
            }
            if(!ts2 && !s2) {
                ts2 = timerTest;
            }
            if(!ts3 && !s3) {
                ts3 = timerTest;
            }
            if(!ts4 && !s4) {
                ts4 = timerTest;
            }
            if(!ts5 && !s5) {
                ts5 = timerTest;
            }
            if(!ts6 && !s6) {
                ts6 = timerTest;
            }
            if(!ts7 && !s7) {
                ts7 = timerTest;
            }
            if(!ts8 && !s8) {
                ts8 = timerTest;
            }
        }

        right_forward();
        left_forward();


        if(ts8 > 200 && ts8 <800) {
            // 留⑤걹  꽱 꽌  씤 떇 븿 -> 硫덉땄, 90 룄  쉶 쟾
            move_forward(1,SPEED);
            rotate_left_degree(ROTATE_SPEED, 73);
            move_forward(1,SPEED);
            P5->OUT &= ~0x08;
            P9->OUT &= ~0x04;
            break;
        }
        else if (ts7 > 200 && ts7 < 800) {
            left_backward();
           right_forward();
           move(1000,1000);
        } 
         else if(ts2 > 200 && ts2 < 800) {
            left_forward();
            right_backward();
            move(1000,1000);
        }
        else if (ts3 > 200 && ts3 < 800) {
            move(LOW_SPEED, SPEED);
        }
        else if (ts6 > 200 && ts6 < 800) {
            move(SPEED, LOW_SPEED);
        } 
        
        else {
            move(SPEED, SPEED);
        }

        P5->OUT &= ~0x08;
        P9->OUT &= ~0x04;

        Clock_Delay1ms(10);
    }
}
void section4_without_tracing() {
    while(1) {
        P5->OUT |= 0x08;
        P9->OUT |= 0x04;

        P7->DIR = 0xFF;
        P7->OUT = 0xFF;

        Clock_Delay1us(10);

        P7->DIR = 0x00;
        int timerTest;
        int ts1;
        int ts2;
        int ts3;
        int ts4;
        int ts5;
        int ts6;
        int ts7;
        int ts8;
        ts1 = 0;
        ts2 = 0;
        ts3 = 0;
        ts4 = 0;
        ts5 = 0;
        ts6 = 0;
        ts7 = 0;
        ts8 = 0;
        for(timerTest=0 ; timerTest<10000 ; timerTest++) {
            int s1;
            int s2;
            int s3;
            int s4;
            int s5;
            int s6;
            int s7;
            int s8;
            s1 = P7->IN & 0x01; // 1
            s2 = P7->IN & 0x02; // 2
            s3 = P7->IN & 0x04; // 3
            s4 = P7->IN & 0x08; // 4
            s5 = P7->IN & 0x10; // 5
            s6 = P7->IN & 0x20; // 6
            s7 = P7->IN & 0x40; // 7
            s8 = P7->IN & 0x80; // 8



            if(!ts1 && !s1) {
                ts1 = timerTest;
            }
            if(!ts2 && !s2) {
                ts2 = timerTest;
            }
            if(!ts3 && !s3) {
                ts3 = timerTest;
            }
            if(!ts4 && !s4) {
                ts4 = timerTest;
            }
            if(!ts5 && !s5) {
                ts5 = timerTest;
            }
            if(!ts6 && !s6) {
                ts6 = timerTest;
            }
            if(!ts7 && !s7) {
                ts7 = timerTest;
            }
            if(!ts8 && !s8) {
                ts8 = timerTest;
            }
        }

        right_forward();
        left_forward();


        if(ts8 > 200 && ts8 <800) {
            // 留⑤걹  꽱 꽌  씤 떇 븿 -> 硫덉땄, 90 룄  쉶 쟾
            move_forward(1,SPEED);
            rotate_left_degree(ROTATE_SPEED, 70);
            move_forward(1,SPEED);
            P5->OUT &= ~0x08;
            P9->OUT &= ~0x04;
            break;
        }
        else if (ts3 > 200 && ts3 < 800) {
            move(LOW_SPEED, SPEED);
        }
        else if (ts6 > 200 && ts6 < 800) {
            move(SPEED, LOW_SPEED);
        } 
        
        else {
            move(SPEED, SPEED);
        }

        P5->OUT &= ~0x08;
        P9->OUT &= ~0x04;

        Clock_Delay1ms(10);
    }
}


void main(void){
    init_all();

    int turn_count; // 占쏙옙占쏙옙 횟占쏙옙 占쏙옙占 
    int lap_count = 0;

    while (lap_count < 2) {
        // 구간1 : 첫번째 센서 인식할때까지 직진 & 인식 후 90도 우회전
        section1();
        // 구간2 : 첫번째 센서 인식할때까지 직진 & 인식 후 90도 우회전
        section1();
        // 구간3 : 인식해도 회전 안함
        section3();
        // 구간4 : 첫번째 센서 인식할때까지 직진 & 인식 후 90도 우회전
        section1();
        // 구간5 : 마지막 센서 인식할때까지 직진 & 인식 후 90도 좌회전
        section4();
        // 구간6 : 첫번째 센서 인식할때까지 직진 & 인식 후 90도 우회전
        section1();
        // 구간7 : 마지막 센서 인식할때까지 직진 & 인식 후 90도 좌회전
        section4();
        // 구간8 : 인식해도 회전 안함
        section3();
        // 구간9 : 마지막 센서 인식할때까지 직진 & 인식 후 90도 좌회전
        section4();
        // 구간10 : 마지막 센서 인식할때까지 직진 & 인식 후 90도 좌회전
        section4();
        rotate_left_degree(ROTATE_SPEED,10);

        // 구간11 : line tracing 부분
        section1(); // first shortcut
        section1();

        section4(); // second shortcut
        u_turn(5,SPEED);
        section4_without_tracing(); 

         section1_without_tracing(); // sharp curve
         move_forward(3,SPEED);
         u_turn2(7,SPEED);

        section4(); // third shortcut
        u_turn2(3,SPEED);
        section4();
        u_turn(3,SPEED);
        
        section1_without_tracing();
        
        section1();

        // 구간12 : 지그재그 부분
        // 45도 회전 todo
        // move_forward(10,SPEED);
        move_until_white();
        rotate_right_degree(ROTATE_SPEED, 40);

        section4();
        section1();

        section4();
        section1();

        section4();
        section1();

        section4();
        move_forward(5,SPEED);
        rotate_right_degree(ROTATE_SPEED, 40);
        // 45도 회전 todo

        // 구간13 : 마지막 센서 인식할때까지 직진 & 인식 후 90도 좌회전
        section4();
        // 구간15 : 첫번째 센서 인식할때까지 직진 & 인식 후 90도 우회전
        section1();

        // 라인 끝까지 달리기
        // 340도 회전후 살짝 직진
        move_until_white();
        rotate_right_degree(ROTATE_SPEED,140);
        move_forward(15,SPEED);

        section4();
        section1();
        section1();
        section4();
        section4();
        section4();
        section1();
        
        move_until_white();
        rotate_right_degree(ROTATE_SPEED,100);
        move_until_white();
        rotate_left_degree(ROTATE_SPEED,100);

        section1();
        section1();
        section4();
        section1();


        move_lap2();

        //  시작 랩 인식
        lap_count++;
    }
    move(0,0);
    
}
