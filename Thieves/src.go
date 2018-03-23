package main

import (
	"log"
	"time"
	"fmt"
)

type item struct {
	name  string
	price int
}

func main() {
	var fromWarehouse = make(chan item)
	var inTrunk = make(chan item)

	go Ivanov(&fromWarehouse)
	go Petrov(&fromWarehouse, &inTrunk)
	go Nechiporchuk(&inTrunk)
	fmt.Scanln()
}

func Ivanov(fromWarehouse *chan item) {
	for i := 0; i < 3; i++ {
		var treasure = item{"Item", i}
		log.Println("Stolen ", treasure)
		*fromWarehouse <- treasure
	}
	//close(*fromWarehouse)
}

func Petrov(fromWarehouse *chan item, inTrunk *chan item) {
	time.Sleep(1000)
	for {
		newItem := <- *fromWarehouse
		log.Println("Put ", newItem, " in trunk.")
		*inTrunk <- newItem
	}
	//close(*inTrunk)
}

func Nechiporchuk(inTrunk *chan item) {
	sum := 0
	for {
		newItem := <- *inTrunk
		sum = sum + newItem.price
		log.Println("Counted ", newItem, " total: ", sum)
	}
}
