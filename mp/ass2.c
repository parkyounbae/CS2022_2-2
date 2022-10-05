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

    int sw1,i=1;

    Clock_Init48MHz();
	led_init();
	switch_init();

	while (1) {
	    sw1 = P1->IN & 0x02;

	    if(!sw1){
	        P2->OUT = 0;
	        i<<=1;
	        if(i==0b1000) i=1;
	        Clock_Delay1ms(100);
	    } else {
	        turn_on_led(i);
	    }
	}

}
