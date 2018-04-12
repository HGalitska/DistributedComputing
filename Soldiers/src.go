package main

import (
	"fmt"
	"sync"
	"time"
	"math/rand"
)

var seed = rand.NewSource(time.Now().UnixNano())
var random = rand.New(seed)

type SoldiersSquad struct {
	orientationArray [] string
	sync.WaitGroup
}

func newSquad(n int, m int) *SoldiersSquad {
	array := make([]string, n)
	for i := 0; i < n; i++ {
		array[i] = "-"
	}
	for i := 0; i < m; i++ {
		dummy := random.Intn(n)
		array[dummy] = "|"
	}
	var newSquad = SoldiersSquad{orientationArray: array}
	return &newSquad
}

func reverse(orientation string) string {
	if orientation == "-" {
		return "|"
	}
	return "-"
}

func routine(array *SoldiersSquad, group *sync.WaitGroup, start int, arraySize int) {
	for i := start; i < arraySize; i++ {
		if array.orientationArray[i] != array.orientationArray[i+1] {
			array.orientationArray[i] = reverse(array.orientationArray[i])
			array.orientationArray[i+1] = reverse(array.orientationArray[i+1])
			i++
		}
	}
	group.Done()
}

func main() {
	var N = 100
	var M = 5

	squad := newSquad(N, M)
	var group = new(sync.WaitGroup)

	OK := false
	for !OK {
		group.Add(2)

		if squad.orientationArray[0] != squad.orientationArray[1] {
			squad.orientationArray[1] = reverse(squad.orientationArray[1])
		}

		OK = true
		// walk through the squad to search for collision
		for i := 1; i < N-1; i++ {
			if squad.orientationArray[i] != squad.orientationArray[i+1] {
				OK = false
			}
			fmt.Print(squad.orientationArray[i])
		}
		fmt.Println()

		go routine(squad, group, 1, N/2 + 1)
		go routine(squad, group, N/2+1, N-1)

		group.Wait()
	}
}
