package main

import (
	"log"
	"sync"
)

type item struct {
	name  string
	price int
}

var wg sync.WaitGroup

func main() {
	var fromWarehouse = make(chan item)
	var inTrunk = make(chan item)

	wg.Add(3)
	go Ivanov(&fromWarehouse)
	go Petrov(&fromWarehouse, &inTrunk)
	go Nechiporchuk(&inTrunk)
	wg.Wait()

	log.Println("Done")
}

func Ivanov(fromWarehouse *chan item) {
	defer wg.Done()

	for i := 0; i < 5; i++ {
		var treasure = item{"Item", i}
		log.Println("Stolen ", treasure)
		*fromWarehouse <- treasure
	}
	*fromWarehouse <- item{"Item", -1}
	log.Println("1 done")
}

func Petrov(fromWarehouse *chan item, inTrunk *chan item) {
	defer wg.Done()

	for {
		select {
		case newItem := <-*fromWarehouse:
			if newItem.price == -1 {
				log.Println("2 done")
				*inTrunk <- item{"Item", -1}
				return
			}
			log.Println("Put ", newItem, " in trunk.")
			*inTrunk <- newItem
		default:
			continue
		}
	}
	*inTrunk <- item{"Item", -1}
}

func Nechiporchuk(inTrunk *chan item) {
	defer wg.Done()

	sum := 0
	for {
		select {
		case newItem := <-*inTrunk:
			if newItem.price == -1 {
				log.Println("3 done")
				return
			}
			sum = sum + newItem.price
			log.Println("Counted ", newItem, " total: ", sum)
		default:
			continue
		}
	}
}
