#include "msp.h"
#include "Clock.h"
#include <stdio.h>

#define LED_RED 1
#define LED_GREEN (LED_RED << 1)
#define LED_BLUE (LED_RED << 2)

void turn_on_led(int color) {
    P2->OUT &= ~0x07;
    P2->OUT |= color;
}

void turn_off_led() {
    P2->OUT &= ~0x07;
}

void switch_init() {
    P1->SEL0 &= ~0x12;
    P1->SEL1 &= ~0x12;

    P1->DIR &= ~0x12;
    P1->REN |= 0x12;
    P1->OUT |= 0x12;
}

void led_init() {
    P2->SEL0 &= ~0x07;
    P2->SEL1 &= ~0x07;

    P2->DIR |= 0x07;

    P2->OUT &= ~0x07;
}

void motor_init (void)
{
P3->SEL0 &= ~0xC0;
P3->SEL1 &= ~0xC0;
P3->DIR |= 0xC0;
P3->OUT &= ~0xC0;
P5->SEL0 &= ~0x30;
P5->SEL1 &= ~0x30;
P5->DIR |= 0x30;
P5->OUT &= ~0x30;
P2->SEL0 &= ~0xC0;
P2->SEL1 &- ~0xC0;
P2->DIR |= 0xC0;
P2->OUT &= ~0xC0;
}

void moveForward () {
    P5 -> OUT &= ~0x30;
    P2->OUT |= 0xC0;
    P3->OUT |= 0xc0;
}

void stopPoppi() {
    P2->OUT &= ~0xC0;
}

void main(void)
{
    Clock_Init48MHz();
	led_init();
	motor_init ();

	// IR sensor
	P5->SEL0 &= ~0x08;
	P5->SEL1 &= ~0x08;
	P5->DIR |= 0x08;
	P5->OUT &= ~0x08;

	P9->SEL0 &= ~0x04;
	P9->SEL1 &= ~0x04;
	P9->DIR |= 0x04;
	P9->OUT &= ~0x04;

	P7->SEL0 &= ~0xFF;
	P7->SEL1 &= ~0xFF;
	P7->DIR &= ~0xFF;

	while(1) {
	    P5->OUT |= 0x08;
	    P9->OUT |= 0x04;

	    P7->DIR = 0xFF;
	    P7->OUT = 0xFF;

	    Clock_Delay1us(10);

	    P7->DIR = 0x00;

	    int sensor5timer;
	    for(sensor5timer=0 ; sensor5timer<10000 ; sensor5timer++) {
	        int sensor5;
	        sensor5 = P7->IN & 0x10;
	        if(!sensor5){
	            break;
	        }
	        Clock_Delay1us(1);
	    }

	    int sensor4timer;
        for(sensor4timer=0 ; sensor4timer<10000 ; sensor4timer++) {
            int sensor4;
            sensor4 = P7->IN & 0x08;
            if(!sensor4){
                break;
            }
            Clock_Delay1us(1);
        }


        int sensor2timer;
        for(sensor2timer=0 ; sensor2timer<10000 ; sensor2timer++) {
            int sensor2;
            sensor2 = P7->IN & 0x02;
            if(!sensor2){
                break;
            }
            Clock_Delay1us(1);
        }

        int sensor7timer;
        for(sensor7timer=0 ; sensor7timer<10000 ; sensor7timer++) {
            int sensor7;
            sensor7 = P7->IN & 0x40;
            if(!sensor7){
                break;
            }
            Clock_Delay1us(1);
        }


        if(sensor5timer>800 && sensor5timer<1100 & sensor4timer>800 && sensor4timer<1100) {
            //moveForward();
            if(sensor7timer < 400 & sensor2timer <400) {
                moveForward ();
            }
        } else {
            stopPoppi();

        }

//	    int i;
//	    for(i=0 ; i<10000 ; i++) {
//	        int sensor;
//	        int sensor2;
//	        sensor = P7->IN & 0x10;
//	        sensor2 = P7->IN & 0x08;
//
//	        sensor3 = P7->IN & 0x02;
//	        sensor4 = P7->IN & 0x40;
//
//	        if(!sensor || !sensor2) {
//	            if(i>500 && i<900) {
//	                turn_on_led(LED_RED);
//	            }else{
//	                turn_off_led();
//	            }
//	            break;
//	        }
//	        Clock_Delay1us(1);
//	    }

	    P5->OUT &= ~0x08;
	    P9->OUT &= ~0x04;

	    Clock_Delay1ms(10);
	}

}
