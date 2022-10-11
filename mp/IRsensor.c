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

void main(void)
{
    Clock_Init48MHz();
	led_init();

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

	    int i;
	    for(i=0 ; i<10000 ; i++) {
	        int sensor;
	        int sensor2;
	        sensor = P7->IN & 0x10;
	        sensor2 = P7->IN & 0x08;

	        if(!sensor || !sensor2) {
	            if(i>500 && i<900) {
	                turn_on_led(LED_RED);
	            }else{
	                turn_off_led();
	            }
	            break;
	        }
	        Clock_Delay1us(1);
	    }

	    P5->OUT &= ~0x08;
	    P9->OUT &= ~0x04;

	    Clock_Delay1ms(10);
	}

}
