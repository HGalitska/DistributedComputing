package main

import (
	"fmt"
	"sync"
)

var Garden struct {
	sync.RWMutex
	matrix [][]int
}

func main() {
	fmt.Println("Hello")
}
