package main

import (
	"time"
	"sync"
	"math/rand"
	"fmt"
)

var seed = rand.NewSource(time.Now().UnixNano())
var random = rand.New(seed)

type AwesomeArrays struct {
	myArray [][] int
	sync.WaitGroup
}

func initArrays(n, arraySize int) *AwesomeArrays {
	arrayList := make([][]int, n, arraySize)
	for i := 0; i < n; i++ {
		arrayList[i] = randomArray(arraySize)
	}
	return &AwesomeArrays{myArray: arrayList}
}

func randomArray(arraySize int) []int {
	array := make([]int, arraySize)
	for i := 0; i < arraySize; i++ {
		array[i] = random.Intn(10)
	}
	return array
}

func printAwesome(list *AwesomeArrays) {
	for _, number := range list.myArray {
		fmt.Println(number)
	}
	fmt.Println("")
}

func check(list *AwesomeArrays, n int) bool {
	gesamt := make([]int, n)
	for i := 0; i < n; i++ {
		sum := 0
		for _, j := range list.myArray[i] {
			sum += j
		}
		gesamt[i] = sum
	}

	var OK = true
	for i := range gesamt {
		if gesamt[0] != gesamt[i] {
			OK = false
		}
	}
	if OK {
		fmt.Print(gesamt)
		return true
	} else {
		return false
	}
}

func routine(list *AwesomeArrays, group *sync.WaitGroup, thread, arraySize int) {
	index := random.Intn(arraySize)
	op := random.Intn(2)

	var array = list.myArray[thread]
	if op == 0 && array[index] < 50{
		array[index]++
	} else if array[index] > 0 {
		array[index]--
	}

	group.Done()
}

func main() {
	var n = 3
	var size = 3
	arrays := initArrays(n, size)
	group := new(sync.WaitGroup)

	OK := false
	for !OK {
		group.Add(n)
		go routine(arrays, group, 0, size)
		go routine(arrays, group, 1, size)
		go routine(arrays, group, 2, size)

		if check(arrays, n) {
			OK = true
			fmt.Println(" ---------> HURRAY!")
		}

		printAwesome(arrays)
		group.Wait()
	}
}
